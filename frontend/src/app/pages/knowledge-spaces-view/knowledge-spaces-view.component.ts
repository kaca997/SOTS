import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { DomainService } from 'app/services/domain.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-knowledge-spaces-view',
  templateUrl: './knowledge-spaces-view.component.html',
  styleUrls: ['./knowledge-spaces-view.component.css']
})
export class KnowledgeSpacesViewComponent implements OnInit {

  domainId : number;
  domainName = ""
  courseName =""
  nodes = []
  realLinks = [];
  expectedLinks = [];
  idd1 = "aaa"
  idd2 = "beee"
  realKS: any;

  dataLoaded = false;
  
  constructor(private route: ActivatedRoute, private ds: DomainService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.domainId = params['id']);
    this.getDomain();
  }

  findNode(name): number{
    var i: number;
    for(i = 0; i < this.nodes.length; i++){
      if(this.nodes[i].id === name){
        return i
      }
    }
}

  getRealKS():void {
     this.ds.getRealKS(this.domainId).subscribe(
          ks => {
            // console.log(ks) 
            this.realKS = ks
          },
          error => {
            // console.log(console.error);
          });
        
  }

  getDomain(): void{
    // this.getRealKS();

    this.ds.getDomain(this.domainId).subscribe(
      domain => {
        // console.log(domain)
        this.domainName = domain.domainName;
        this.courseName = domain.courseName;
        domain.problems.forEach(problem => {
          const node = { id: problem,  reflexive: false};
          this.nodes.push(node);
        });
        
        domain.expectedKnowledgeSpace.forEach(relation => {
          var i = this.findNode(relation.surmiseFrom)
          var j =this.findNode(relation.surmiseTo)
          const link = {source: this.nodes[i], target: this.nodes[j], left: false, right: true};
          this.expectedLinks.push(link)
        });
        
        domain.realKnowledgeSpace.forEach(relation => {
          var i = this.findNode(relation.surmiseFrom)
          var j =this.findNode(relation.surmiseTo)
          const link = {source: this.nodes[i], target: this.nodes[j], left: false, right: true};
          this.realLinks.push(link)
        });

        this.dataLoaded = true;
        // console.log("Links: ", this.expectedLinks)
        // console.log("Real: ", this.realLinks)
        // console.log("N:", this.nodes);
        
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
  }
}