import { Component, OnInit, ɵbypassSanitizationTrustResourceUrl } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CourseService } from 'app/services/course.service';
import { DomainService } from 'app/services/domain.service';
import * as d3 from 'd3';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.scss']
})
export class GraphComponent implements OnInit {
  
  // svg :any
  // margin = {top: 10, right: 30, bottom: 30, left: 40}
  // width = 400 - this.margin.left - this.margin.right
  // height = 400 - this.margin.top - this.margin.bottom;
  // link : any
  // node: any

  cyclicGraph = false;
  courses: { id: number, name: string }[] = [
    { "id": 0, "name": "Available" },
    { "id": 1, "name": "Ready" },
    { "id": 2, "name": "Started" }
];



  newProblemForm : FormGroup;
  domainForm : FormGroup;
  width = 1050;
  height = 400;
  colors = d3.scaleOrdinal(d3.schemeCategory10);

  svg: any;
  force: any;
  path: any;
  circle: any;
  drag: any;
  dragLine: any;
  // mouse event vars
  selectedNode = null;
  selectedLink = null;
  mousedownLink = null;
  mousedownNode = null;
  mouseupNode = null;

  nodes = [
    { id: "Root Problem", reflexive: false }
  ];
  links = [
    // { source: this.nodes[0], target: this.nodes[1], left: false, right: true },
    // { source: this.nodes[1], target: this.nodes[2], left: false, right: true }
  ];

  constructor(private fb: FormBuilder, private cs: CourseService, private ds: DomainService, private router : Router, private toastr : ToastrService) { 
    this.newProblemForm = this.fb.group({
      problemName : [null, Validators.required]
    });

    this.domainForm = this.fb.group({
      domainName : [null, Validators.required],
      courseName: [null, Validators.required]
    })
  }

  ngOnInit() {
    this.getCourses()

    this.svg = d3.select('#graphContainer')
      .attr('oncontextmenu', 'return false;')
      .attr('width', this.width)
      .attr('height', this.height);

    this.force = d3.forceSimulation()
      .force('link', d3.forceLink().id((d: any) => d.id).distance(150))
      .force('charge', d3.forceManyBody().strength(-500))
      .force('x', d3.forceX(this.width / 2))
      .force('y', d3.forceY(this.height / 2))
      .on('tick', () => this.tick());

    // init D3 drag support
    this.drag = d3.drag()
      .on('start', (event, d: any) => {
        if (!event.active) this.force.alphaTarget(0.3).restart();

        d.fx = d.x;
        d.fy = d.y;
      })
      .on('drag', (event,d: any) => {
        d.fx = event.x;
        d.fy = event.y;
      })
      .on('end', (event,d: any) => {
        if (!event.active) this.force.alphaTarget(0.3);

        d.fx = null;
        d.fy = null;
      });


    // define arrow markers for graph links
    this.svg.append('svg:defs').append('svg:marker')
      .attr('id', 'end-arrow')
      .attr('viewBox', '0 -5 10 10')
      .attr('refX', 6)
      .attr('markerWidth', 5)
      .attr('markerHeight', 5)
      .attr('orient', 'auto')
      .append('svg:path')
      .attr('d', 'M0,-5L10,0L0,5')
      .attr('fill', 'grey');

    this.svg.append('svg:defs').append('svg:marker')
      .attr('id', 'start-arrow')
      .attr('viewBox', '0 -5 10 10')
      .attr('refX', 4)
      .attr('markerWidth', 5)
      .attr('markerHeight', 5)
      .attr('orient', 'auto')
      .append('svg:path')
      .attr('d', 'M10,-5L0,0L10,5')
      .attr('fill', 'grey');


    // line displayed when dragging new nodes
    this.dragLine = this.svg.append('svg:path')
      .attr('class', 'link dragline hidden')
      .attr('d', 'M0,0L0,0')
      .attr("fill", "grey")

    // handles to link and node element groups
    this.path = this.svg.append('svg:g').selectAll('path');
    this.circle = this.svg.append('svg:g').selectAll('g');

    // app starts here
    this.svg.on('mousedown', (event,dataItem, value, source) => this.mousedown(event,dataItem, value, source))
      .on('mousemove', (event, dataItem) => this.mousemove(event,dataItem))
      .on('mouseup', (dataItem) => this.mouseup(dataItem));
    this.restart(null);
  }


  // update force layout (called automatically each iteration)
  tick() {
    // draw directed edges with proper padding from node centers
    this.path.attr('d', (d: any) => {
      const deltaX = d.target.x - d.source.x;
      const deltaY = d.target.y - d.source.y;
      const dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
      const normX = deltaX / dist;
      const normY = deltaY / dist;
      const sourcePadding = d.left ? 17 : 12;
      const targetPadding = d.right ? 17 : 12;
      const sourceX = d.source.x + (sourcePadding * normX);
      const sourceY = d.source.y + (sourcePadding * normY);
      const targetX = d.target.x - (targetPadding * normX);
      const targetY = d.target.y - (targetPadding * normY);

      return `M${sourceX},${sourceY}L${targetX},${targetY}`;
    });

    this.circle.attr('transform', (d) => `translate(${d.x},${d.y})`);
  }

  resetMouseVars() {
    this.mousedownNode = null;
    this.mouseupNode = null;
    this.mousedownLink = null;
  }

  // update graph (called when needed)
  restart(event) {
    // path (link) group
    // console.log(this.nodes)
    // console.log(this.links)
    this.path = this.path.data(this.links);

    // update existing links
    this.path.classed('selected', (d) => d === this.selectedLink)
      .style('marker-start', (d) => d.left ? 'url(#start-arrow)' : '')
      .style('marker-end', (d) => d.right ? 'url(#end-arrow)' : '');

    // remove old links
    this.path.exit().remove();

    // add new links
    this.path = this.path.enter().append('svg:path')
      .attr('class', 'link')
      .style("stroke-width", "3")
      .style('fill', 'grey')
      .style('stroke','grey')
      .classed('selected', (d) => d === this.selectedLink)
      .style('marker-start', (d) => d.left ? 'url(#start-arrow)' : '')
      .style('marker-end', (d) => d.right ? 'url(#end-arrow)' : '')
      .on('mousedown', (event, d) => {
        if (event.ctrlKey) return;

        // select link
        this.mousedownLink = d;
        this.selectedLink = (this.mousedownLink === this.selectedLink) ? null : this.mousedownLink;
        this.selectedNode = null;
        this.restart(event);
      })
      .merge(this.path);

    // circle (node) group
    // NB: the function arg is crucial here! nodes are known by id, not by index!
    this.circle = this.circle.data(this.nodes, (d) => d.id);

    // update existing nodes (reflexive & selected visual states)
    this.circle.selectAll('circle')
      .style('fill', (d) => (d === this.selectedNode) ? d3.rgb(this.colors('red')).brighter().toString() : this.colors('red'))
      .classed('reflexive', (d) => d.reflexive);

    // remove old nodes
    this.circle.exit().remove();

    // add new nodes
    const g = this.circle.enter().append('svg:g');

    g.append('svg:circle')
      .attr('class', 'node')
      .attr('r', 12)
      .style('fill', (d) => (d === this.selectedNode) ? d3.rgb(this.colors('red')).brighter().toString() : this.colors('red'))
      .style('stroke', (d) => d3.rgb(this.colors('red')).darker().toString())
      .classed('reflexive', (d) => d.reflexive)
      .on('mouseover', function (d) {
        if (!this.mousedownNode || d === this.mousedownNode) return;
        // enlarge target node
        d3.select(this).attr('transform', 'scale(1.1)');
      })
      .on('mouseout', function (d) {
        if (!this.mousedownNode || d === this.mousedownNode) return;
        // unenlarge target node
        d3.select(this).attr('transform', '');
      })
      .on('mousedown', (event, d) => {
        if (event.ctrlKey) return;

        // select node
        this.mousedownNode = d;
        this.selectedNode = (this.mousedownNode === this.selectedNode) ? null : this.mousedownNode;
        this.selectedLink = null;

        // reposition drag line
        this.dragLine
          .style('marker-end', 'url(#end-arrow)')
          .classed('hidden', false)
          .attr('d', `M${this.mousedownNode.x},${this.mousedownNode.y}L${this.mousedownNode.x},${this.mousedownNode.y}`);

        this.restart(event);
      })
      .on('mouseup', (event, dataItem: any) => {
        debugger;
        if (!this.mousedownNode) return;

        // needed by FF
        this.dragLine
          .classed('hidden', true)
          .style('marker-end', '');

        // check for drag-to-self
        this.mouseupNode = dataItem;
        if (this.mouseupNode === this.mousedownNode) {
          this.resetMouseVars();
          return;
        }

        // unenlarge target node
        d3.select(event.currentTarget).attr('transform', '');

        this.findParentsForNode(this.mousedownNode)
        if(this.cyclicGraph === true){
          console.log("Greska")
          this.cyclicGraph = false
          this.toastr.warning("Graph can not be cyclic!")
          return
        }
        // add link to graph (update if exists)
        // NB: links are strictly source < target; arrows separately specified by booleans
        // const isRight = this.mousedownNode.id < this.mouseupNode.id;
        // const source = isRight ? this.mousedownNode : this.mouseupNode;
        // const target = isRight ? this.mouseupNode : this.mousedownNode;
        const source = this.mousedownNode
        const target = this.mouseupNode
        console.log("Source: ", source);
        console.log("Target: ", target);
        if(target.id == "Root Problem"){
          this.toastr.warning("You can not add link to root problem")
          return
        }
        const link = this.links.filter((l) => l.source === source && l.target === target)[0];
        if (link) {
          // link.left = !isRight
          // link.right = isRight
        } else {
          this.links.push({ source, target, left: false, right: true});
        }

        // select new link
        this.selectedLink = link;
        this.selectedNode = null;
        this.restart(event);
      });

    // show node IDs
    g.append('svg:text')
      .attr('x', 0)
      .attr('y', 4)
      .attr('class', 'id')
      .attr('fill', 'black')
      .style("font-size", "14px")
      .text((d) => d.id);


    this.circle = g.merge(this.circle);

    // set the graph in motion
    this.force
      .nodes(this.nodes)
      .force('link').links(this.links);

    this.force.alphaTarget(0.3).restart();
  }

  mousedown(event, dataItem: any, value: any, source: any) {
    // because :active only works in WebKit?
    this.svg.classed('active', true);

    if (event.ctrlKey || this.mousedownNode || this.mousedownLink) return;

    // insert new node at point
    const point = d3.pointer(event.currentTarget);
    // const point = d3.mouse(this);
    // const node = { id: "P"+ ++this.lastNodeId, reflexive: false};
    // this.nodes.push(node);

    // this.restart(event);
  }

  mousemove(event, source: any) {
    if (!this.mousedownNode) return;
    
    // update drag line
    this.dragLine.attr('d', `M${this.mousedownNode.x},${this.mousedownNode.y}L${d3.pointer(event.currentTarget)[0]},${d3.pointer(event.currentTarget)[1]}`);

    this.restart(event);
  }

  mouseup(source: any) {
    // console.log("up : ", this.mouseupNode)
    // if(this.cyclicGraph){
    //   console.log("Ciklicnoooo")
    // }else{
    //   console.log("Mozeee")
    // }
    if (this.mousedownNode) {
      // hide drag line
      this.dragLine
        .classed('hidden', true)
        .style('marker-end', '');
    }

    // because :active only works in WebKit?
    this.svg.classed('active', false);

    // clear mouse event vars
    this.resetMouseVars();
  }

  spliceLinksForNode(node) {
    const toSplice = this.links.filter((l) => l.source === node || l.target === node);
    for (const l of toSplice) {
      this.links.splice(this.links.indexOf(l), 1);
    }
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
    this.restart(null);
  }

  findParentsForNode(node){
    var parentsLinks = this.links.filter(function (link) {
      // console.log(link)
      // console.log("Node",  node)
      // console.log("Node id",  node.id)
      // // console.log(link.target.id)
      return link.target.id === node.id;
    });
    var parents = parentsLinks.map(link => link.source )
    console.log(parents)
    if(parents.length === 0){
      return
    }else{
      this.checkParents(parents)
      if(this.cyclicGraph === true){
        return
      }else{
        this.findParentsForNodes(parents)
      }
    }
  }

  findParentsForNodes(nodes){
    var parents = []
    nodes.forEach(node => {
      var parentsLinks = this.links.filter(function (link) {
        return link.target.id === node.id;
      });
      parents = parentsLinks.map(link => link.source )
      console.log("Parents: ", parents)
    });
    if(parents.length === 0){
      return
    }else{
      this.checkParents(parents)
      if(this.cyclicGraph === true){
        return
      }else{
        this.findParentsForNodes(parents)
      }
    }
  }

  checkParents(parents){
    parents.forEach(parent => {
      if(parent.id == this.mouseupNode.id){
        this.cyclicGraph = true;
        return;
      }
      
    });;
  }

  deleteProblem(){
    if (this.selectedNode) {
      if(this.selectedNode.id === 'Root Problem'){
        this.toastr.warning("You can not delete root problem")
        return;
      }
      this.nodes.splice(this.nodes.indexOf(this.selectedNode), 1);
      this.spliceLinksForNode(this.selectedNode);
    } else if (this.selectedLink) {
      this.links.splice(this.links.indexOf(this.selectedLink), 1);
    }
    this.selectedLink = null;
    this.selectedNode = null;
    this.restart(event);
  }

  saveDomen(){
    console.log(this.links)
    let domain : any = {}
    domain.domainName = this.domainForm.value.domainName;
    domain.courseId = +this.domainForm.value.courseName;
    domain.problemList = this.nodes.map(node => node.id);
    let relations = [] 
    this.links.forEach(link =>{
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
}
