import { AfterViewInit, EventEmitter,Component, Input, OnInit, Output, ɵbypassSanitizationTrustResourceUrl } from '@angular/core';
import * as d3 from 'd3';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.scss']
})
export class GraphComponent implements AfterViewInit {

  cyclicGraph = false;
  transitivity = false;

  @Input() width;
  @Input() height;
  @Input() idd;
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

  @Input() nodes: Array<any>;
  @Input() readOnly = false;
  // nodes = [
  //   { id: "Root Problem", reflexive: false }
  // ];
  @Input() links : Array<any>;
    // { source: this.nodes[0], target: this.nodes[1], left: false, right: true },
    // { source: this.nodes[1], target: this.nodes[2], left: false, right: true }
  constructor(private toastr: ToastrService) {}
  ngAfterViewInit() {
    // this.checkLinks()
    // console.log(this.links)
    // console.log(this.nodes)
    this.svg = d3.select(`#${this.idd}`)
      .attr('oncontextmenu', 'return false;')
      .attr('width', this.width)
      .attr('height', this.height);

    this.force = d3.forceSimulation()
      .force('link', d3.forceLink().id((d: any) => d.id).distance(100))
      .force('charge', d3.forceManyBody().strength(-1000))
      .force('x', d3.forceX(this.width / 2))
      .force('y', d3.forceY(this.height / 2))
      .on('tick', () => this.tick());

    // init D3 drag support
    this.drag = d3.drag()
      .on('start', (event, d: any) => {
        // if(this.readOnly) return;
        if (!event.active) this.force.alphaTarget(0.3).restart();

        d.fx = d.x;
        d.fy = d.y;
      })
      .on('drag', (event,d: any) => {
        // if(this.readOnly) return;
        d.fx = event.x;
        d.fy = event.y;
      })
      .on('end', (event,d: any) => {
        // if(this.readOnly) return;
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
    if(!this.readOnly){
    this.svg.on('mousedown', (event,dataItem, value, source) => this.mousedown(event,dataItem, value, source))
      .on('mousemove', (event, dataItem) => this.mousemove(event,dataItem))
      .on('mouseup', (dataItem) => this.mouseup(dataItem));
    }
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

      // this.checkLinks();
    
    // path (link) group
    // console.log(this.nodes)
    // console.log(this.links)
        // console.log(this.links)
    // this.links = okLinks;
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
        if(this.readOnly) return;
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
        if(this.readOnly) return;
        if (!this.mousedownNode || d === this.mousedownNode) return;
        // enlarge target node
        d3.select(this).attr('transform', 'scale(1.1)');
      })
      .on('mouseout', function (d) {
        if(this.readOnly) return;
        if (!this.mousedownNode || d === this.mousedownNode) return;
        // unenlarge target node
        d3.select(this).attr('transform', '');
      })
      .on('mousedown', (event, d) => {
        if(this.readOnly) return;
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
        if(this.readOnly) return;
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

        this.findParentsForNodes([this.mousedownNode], this.mouseupNode, this.links)
        if(this.cyclicGraph === true){
          this.cyclicGraph = false
          this.toastr.warning("Graph can not be cyclic!")
          return
        }

        this.findChildrenForNodes([this.mousedownNode], this.mouseupNode, this.links)
        if(this.transitivity === true){
          this.transitivity = false
          this.toastr.warning("Graph is transitive!")
          return
        }
        // add link to graph (update if exists)
        // NB: links are strictly source < target; arrows separately specified by booleans
        // const isRight = this.mousedownNode.id < this.mouseupNode.id;
        // const source = isRight ? this.mousedownNode : this.mouseupNode;
        // const target = isRight ? this.mouseupNode : this.mousedownNode;
        const source = this.mousedownNode
        const target = this.mouseupNode
        // console.log("Source: ", source);
        // console.log("Target: ", target);
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
          this.checkLinks();
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
    // console.log(this.mousedownNode)
    console.log(event)
    // console.log(d3.pointer(event.currentTarget)[0]);
    var x = event.pageX - document.getElementById(this.idd).getBoundingClientRect().x
    var y = event.pageY - document.getElementById(this.idd).getBoundingClientRect().y
    this.dragLine.attr('d', `M${this.mousedownNode.x},${this.mousedownNode.y}L${x},${y}`);

    this.restart(event);
  }

  mouseup(source: any) {
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

  findParentsForNodes(nodes, mainNode, links){
    var parents = []
    nodes.forEach(node => {
      var parentsLinks = links.filter(function (link) {
        return link.target.id === node.id;
      });
      parents = parentsLinks.map(link => link.source )
      // console.log("Parents: ", parents)
      if(parents.length === 0){
        return
      }else{
        this.checkParents(parents, mainNode)
        if(this.cyclicGraph === true){
          return
        }else{
          this.findParentsForNodes(parents, mainNode, links)
        }
      }
    });
  }

  checkParents(parents, mainNode){
    parents.forEach(parent => {
      if(parent.id == mainNode.id){
        this.cyclicGraph = true;
        return;
      }     
    });;
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
  checkLinks(){
    let okLinks = this.links
    this.links = []
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
         this.links.push(link)
      }
    }
  }
  
  deleteProblem(){
    if (this.selectedNode) {
      this.nodes.splice(this.nodes.indexOf(this.selectedNode), 1);
      this.spliceLinksForNode(this.selectedNode);
    } else if (this.selectedLink) {
      // console.log("Linkovi", this.links)
      this.links.splice(this.links.indexOf(this.selectedLink), 1);
      // console.log(this.links)
    }
    this.selectedLink = null;
    this.selectedNode = null;
    this.restart(event);
  }
}
