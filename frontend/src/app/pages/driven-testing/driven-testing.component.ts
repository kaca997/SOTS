import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { DomainService } from 'app/services/domain.service';
import { TestService } from 'app/services/test.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-driven-testing',
  templateUrl: './driven-testing.component.html',
  styleUrls: ['./driven-testing.component.css']
})
export class DrivenTestingComponent implements OnInit {

  private testId: number;
  private kstateProb: any;
  private test: any;
  private error = null;
  private counter = 1;
  private finalState : any;
  private isFinalState : boolean = false;
  idd = "HHH"
  
  stateNodes = []
  stateLinks = [];
  statesLoaded = false;
  cyclicGraph = false;
  transitivity = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router, 
    private testService: TestService,
    private toastr: ToastrService,
    private ds: DomainService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.testId = params['id']);
    this.getKStateProb(this.testId);
    this.getTestToDo();
    // console.log(this.test)
  }

  getKStateProb(id : number) {
    this.testService.getForDrivenTesting(this.testId).subscribe(
      res => {
        this.kstateProb = res;
        console.log(this.kstateProb);
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
  }

  getTestToDo() : void{
    this.testService.getTest(this.testId).subscribe(
      test => {
        this.test = test;
        console.log(this.test);
        this.error = null;
      },
      error => {
        console.log(error);
        this.error = error.error;
        this.toastr.error(error.error);
      }
    );
  }

  next(): void {
    this.kstateProb.questions.push(this.kstateProb.questionToAsk);
    console.log(this.kstateProb);
    this.submitQuestion(this.kstateProb);
  }

  submitQuestion(state : any) {
    this.testService.submitQuestion(state, this.testId).subscribe(
      res => {
        if (this.checkFinalState(res)) {
        this.getState();
          return;
        }
        
        this.kstateProb = res;
        this.counter = this.kstateProb.questions.length + 1;
        console.log(this.kstateProb);
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    );
  }

  checkFinalState(state:any) {
    if (state.finalState != null) {
      let all = state.finalState.problems
      console.log(all)
      this.isFinalState = true;
      this.finalState = state.finalState;
      return true;
    }
    return false;
  }

  findState(name): number{
    var i: number;
    for(i = 0; i < this.stateNodes.length; i++){
      if(this.stateNodes[i].id === name){
        return i
      }
    }
  }

  getState(): void{
    // var finalName = "{ ";
    // finalName = finalName.substring(0, finalName.length - 2)
    // finalName += " }"

    this.ds.getStatesTest(this.testId).subscribe(
      graph =>{
        console.log(this.finalState)
        graph.nodes.forEach(el => {
          var check = false;
          if(this.finalState.problems.length != 0){
            var problemNames = el.name.substring(2, el.name.length-2).split(", ")
            // console.log(problemNames);
            // console.log(problems.length)
            if(this.finalState.problems.length == problemNames.length){
              check = true;
              this.finalState.problems.forEach(problem => {
                if(!problemNames.includes(problem.name)){
                  check = false;
                  return;
                }
              });
            }
          }
          else{
            if(el.name == "{}"){
              check = true;
            }
          }
          console.log("Name: ", el.name)
          // console.log("CURR?: ", finalName ===el.name)
          const node = {id:el.name, group: el.group, currentState: check, reflexive: false}
          this.stateNodes.push(node);
        });
        graph.relations.forEach(relation => {
          var i = this.findState(relation.surmiseFrom)
          var j =this.findState(relation.surmiseTo)
          // console.log(i);
          // console.log(this.stateNodes[i]);
          // console.log(j);
          const link = {source: this.stateNodes[i], target: this.stateNodes[j], left: false, right: true};
          // console.log(link);
          this.stateLinks.push(link)
          
        });
        this.checkStates();
        this.statesLoaded = true;
        // console.log(this.stateLinks)

        // this.checkRealLinks()
        // this.checkExpectedLinks()
      },
      error => {
        console.log(error);
        this.toastr.error(error.error);
      }
    )
  }
  checkStates(){
    let okLinks = this.stateLinks
    console.log("STate", this.stateLinks)
    console.log("State nodes", this.stateNodes)
    this.stateLinks = []
    let i
    let link
    let checkLinks
    // console.log("Ok links", okLinks)
    for(i=0; i<okLinks.length; i++){
      link = okLinks[i]
      checkLinks = okLinks.filter(obj => obj !== link);
      // console.log("Check: ", checkLinks)
      // console.log("Link, ",link)
      // let mouseDown = link.source
      // let mouseUp = link.target
      // this.findParentsForNodes([link.source], link.target, checkLinks)
      this.findChildrenForNodes([link.source], link.target, checkLinks)
      if(this.cyclicGraph === true || this.transitivity === true){
        // // this.links.splice(link);
        // console.log("Remove ciclyc")
        this.transitivity = false; 
        this.cyclicGraph = false;
      }else{
        // console.log("OKK", link)
         this.stateLinks.push(link)
      }
    }
  }

  findChildrenForNodes(nodes, mainNode, links){
    var children = []
    nodes.forEach(node => {
      var childrenLinks = links.filter(function (link) {
        return link.source.id === node.id;
      });
      // console.log("DECA: ", childrenLinks)
      children = childrenLinks.map(link => link.target )
      // console.log("DECA CVOROVI:", children)
      if(children.length === 0){
        return
      }else{
        this.checkChildren(children, mainNode)
        if(this.transitivity === true){
          return
        }else{
          this.findChildrenForNodes(children, mainNode, links)
        }
      }
      // console.log("Parents: ", parents)
    });
  }

  checkChildren(children, mainNode){
    // console.log("main ",mainNode)
    children.forEach(child => {
      // console.log("GLAVNI", mainNode)
      // console.log("Provera", child)
      if(child.id == mainNode.id){
        this.transitivity = true;
        return;
      }     
    });;
  } 
}
