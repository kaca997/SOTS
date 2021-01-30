import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CourseService } from 'app/services/course.service';
import { DomainService } from 'app/services/domain.service';
import { ToastrService } from 'ngx-toastr';
import { GraphComponent } from '../graph/graph.component';

@Component({
  selector: 'app-new-domain',
  templateUrl: './new-domain.component.html',
  styleUrls: ['./new-domain.component.css'],
})
export class NewDomainComponent implements OnInit {

  @ViewChild(GraphComponent) child:GraphComponent;
  nodes = []
  links = []
  newProblemForm : FormGroup;
  domainForm : FormGroup;
  courses: { id: number, name: string }[] = [];
  idd="dom"
  constructor(private fb: FormBuilder, private cs: CourseService, private ds: DomainService, private router : Router, private toastr : ToastrService) {
    this.newProblemForm = this.fb.group({
      problemName : [null, Validators.required]
    });

    this.domainForm = this.fb.group({
      domainName : [null, Validators.required],
      courseName: [null, Validators.required]
    })
   }

  ngOnInit(): void {
    this.getCourses()
  }

  getCourses(): any{
    this.cs.getCoursesWithoutDomain().subscribe(
      courses => {
        this.courses = courses;
        console.log(this.courses)
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
  }

  addProblem(){
    const id = this.newProblemForm.value.problemName;
    let check = false
    this.nodes.forEach(function (node) {
      if(node.id === id){
        console.log(node)
        check = true

        return
      }
    });
    if (check){
      this.toastr.warning("Problem with that name already exists!")
      return
    }
    console.log("E")
    const node = { id: id,  reflexive: false};
    this.nodes.push(node);
    this.newProblemForm.value.problemName = "";
    this.child.restart(null);
  }

  saveDomen(){
    console.log(this.child.links)
    let domain : any = {}
    domain.domainName = this.domainForm.value.domainName;
    domain.courseId = +this.domainForm.value.courseName;
    domain.problemList = this.nodes.map(node => node.id);
    let relations = [] 
    this.child.links.forEach(link =>{
      let relation : any ={}
      relation.surmiseFrom = link.source.id;
      relation.surmiseTo = link.target.id;
      relations.push(relation)
    })
    domain.relations = relations
    console.log(domain)
    this.ds.creteDomain(domain).subscribe(
			result => {
				this.toastr.success('Domain successfully added!');
			},
			error => {
				console.log(error);
        this.toastr.error(error.error);
			}
		);
  }


  checkSelectedLinkOrNode(){
    if(this.child === undefined){
      return true;
    }
    if(!this.child.selectedNode && !this.child.selectedLink){
      return true
    }
    else{
      return false;
    }
  }


  deleteProblem(){
    this.child.deleteProblem()
  //   if (this.child.selectedNode) {
  //     this.nodes.splice(this.nodes.indexOf(this.child.selectedNode), 1);
  //     this.child.spliceLinksForNode(this.child.selectedNode);
  //   } else if (this.child.selectedLink) {
  //     console.log("Linkovi", this.links)
  //     this.links.splice(this.links.indexOf(this.child.selectedLink), 1);
  //     console.log(this.links)
  //   }
  //   this.child.selectedLink = null;
  //   this.child.selectedNode = null;
  //   this.child.restart(event);
  // }
  }
}
