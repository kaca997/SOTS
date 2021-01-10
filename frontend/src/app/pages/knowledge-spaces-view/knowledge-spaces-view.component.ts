import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { DomainService } from 'app/services/domain.service';
import { ToastrService } from 'ngx-toastr';
import { GraphComponent } from '../graph/graph.component';

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
  stateNodes = []
  stateLinks = [];
  realLinks = [];
  expectedLinks = [];
  tRealLinks = null;
  tExpectedLinks = null; 
  idd1 = "exp"
  idd2 = "real"
  idd3 = "states"
  realKS: any;
  cyclicGraph = false;
  transitivity = false;
  resultsAdd = []
  resultsUpdate = []
  resultsDelete = []

  dataLoaded = false;
  statesLoaded = false;
  constructor(private route: ActivatedRoute, private ds: DomainService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.domainId = params['id']);
    this.getState();
    console.log(this.dataLoaded)
    this.getDomain();
    console.log(this.stateNodes);
    console.log(this.stateLinks);
    // console.log(JSON.stringify(this.diff({}, {a:1, b:2, c:3})));
    // console.log(JSON.stringify(this.diff({a:1, b:2, c:3}, {})));
    // console.log(JSON.stringify(this.diff({a:1, b:2, c:3}, {b:20, c:30, d:40})));
  }


  findNode(name): number{
    var i: number;
    for(i = 0; i < this.nodes.length; i++){
      if(this.nodes[i].id === name){
        return i
      }
    }
  }
  findState(name): number{
    var i: number;
    for(i = 0; i < this.stateNodes.length; i++){
      if(this.stateNodes[i].id === name){
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

  getState(): void{
    this.ds.getStates(this.domainId).subscribe(
      graph =>{
        graph.nodes.forEach(el => {
          const node = {id:el.name, group: el.group, reflexive: false}
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
        this.checkRealLinks()
        this.checkExpectedLinks()
        this.dataLoaded = true;
        this.diff();
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

  checkRealLinks(){
    let okLinks = this.realLinks
    this.realLinks = []
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
         this.realLinks.push(link)
      }
    }
  }

  checkExpectedLinks(){
    let okLinks = this.expectedLinks
    this.expectedLinks = []
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
         this.expectedLinks.push(link)
      }
    }
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
  diff() {
    console.log("Source", this.expectedLinks)
    // console.log("Target", real)
    var result = [];
    this.expectedLinks.forEach(
      expectedLink =>{
        var updated = false;
        this.realLinks.forEach(
          realLink => {
            if(expectedLink.source.id === realLink.target.id && expectedLink.target.id === realLink.source.id){
              this.resultsUpdate.push({op: 'Update', src: expectedLink.source.id, trg: expectedLink.target.id})
              updated = true
              return
            }
          }
        )
        if(!updated){
          this.resultsDelete.push({op: 'Delete', src: expectedLink.source.id, trg: expectedLink.target.id})
        }
      }
    )
    this.realLinks.forEach(
      realLink =>{
        var found = false;
        this.expectedLinks.forEach(
          expectedLink =>{
            if((expectedLink.source.id === realLink.source.id && expectedLink.target.id === realLink.target.id) 
            ||(expectedLink.source.id === realLink.target.id && expectedLink.target.id === realLink.source.id)){
              found = true;
              return;
            }
          }
        )
        if(found === false){
          this.resultsAdd.push({op: 'Add', src: realLink.source.id, trg: realLink.target.id})
        }
      }
    )
    //   if(expectedLink.so == target.t) { 
    //     if(src[key] !== target[key]) {
    //       result.push({op:"update", name:key, value:target[key]});
    //     }
    //   } else {
    //     result.push({op:"delete", name:key});
    //   }
    // }
    // for(var key in target) {
    //   if(!(key in src)) {
    //     result.push({op:"add", name:key, value:target[key]});
    //   }
    // }
    console.log(result)
    //return result;
  }
}