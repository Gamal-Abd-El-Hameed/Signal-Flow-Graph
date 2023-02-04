import { Component, OnInit } from '@angular/core';
import Konva from 'konva';
import { Vector2d } from 'konva/lib/types';
import { AdjacencyListItem } from './AdjacencyListItem';
import { ConnectionService } from './services/connection.service'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  private stage!: Konva.Stage;
  private layer!:Konva.Layer;
  private nodeID:number = 0
  private edgeID:number = 0
  private drawingStatus:string = "Stop"
  private lineBeingDrawn!: Konva.Line
  private srcNode!:number
  public gainValue: String = ""
  private adjacencyList:AdjacencyListItem[][] = [];
  private edgeList:number[][] = []
  private selectedLine!:Konva.Line | null
  private gainValues:Konva.Text[] = []
  public nodeList:String[] = []
  public ans: String = ""


  constructor(private service: ConnectionService) { }

  title = 'SigFlowGraph';

  ngOnInit(): void {
    this.stage = new Konva.Stage({
      container: 'container',
      width: window.innerWidth * (0.9),
      height: window.innerHeight * (0.8),

    });

    this.initializeStageBranchDrawing()

    this.layer = new Konva.Layer();

    this.stage.add(this.layer);


  }

  addNewNode(){

    let Node = new Konva.Circle({
      radius: 20,
      id: String(this.nodeID),
      fill:'#add8e6',
      stroke: 'black',
      strokeWidth: 2 })

    let group = new Konva.Group({
      x : 200,
      y: 200,
      draggable:true })

    group.add(Node)

    group.add(new Konva.Text({
          text: 'x'+String(this.nodeID++),
          fontSize: 20,
          x:-10,
          height: group.height(),
          fill: '#000',
          verticalAlign:'middle' }));


    this.adjacencyList.push([])

    this.nodeList.push("x" + Node.id())

    this.initializeBranchSource(group, Node)

    this.layer.add(group)

  }

  initializeStageBranchDrawing(){

    this.stage.on('mousemove', (e) => {

      if (this.drawingStatus == "Drawing") {
      var pos = this.stage.getPointerPosition()!
      var points = this.lineBeingDrawn.points()
      points[points.length-2] = pos.x;
      points[points.length-1] = pos.y;
      this.lineBeingDrawn.points(points);
      this.layer.batchDraw();

    }

    });

    this.stage.on('mousedown', (e) => {

      if (e.target == this.stage && this.selectedLine != null)
        {
          this.selectedLine.stroke("black")
          this.selectedLine = null
        }

      if (this.drawingStatus == "Drawing" && e.target == this.stage) {
      var pos = this.stage.getPointerPosition()!
      var points:number[] = this.lineBeingDrawn.points().slice()
      points.push(pos.x, pos.y)
      console.log(points)
      this.lineBeingDrawn.points(points);
      this.layer.batchDraw();

    }

    });

  }

  initializeBranchSource(source:Konva.Group, Node:Konva.Circle){

    source.on('mousedown', (e) => {

      var pos = this.stage.getPointerPosition()!

      if (this.drawingStatus == "Start") {

      source.draggable(false)

      this.lineBeingDrawn = this.createBranch(pos, source)

      this.layer.add(this.lineBeingDrawn);

      this.drawingStatus = "Drawing"

      this.srcNode = parseInt(Node.id())

    }

    else if (this.drawingStatus == "Drawing"){

      source.draggable(false)
      this.drawingStatus = "stop"
      var linePoints = this.lineBeingDrawn.points()
      linePoints[linePoints.length-2] = source.x()
      linePoints[linePoints.length-1] = source.y()
      this.lineBeingDrawn.points(linePoints)
      this.lineBeingDrawn.listening(true)
      var neighbour:AdjacencyListItem = new AdjacencyListItem(String(this.srcNode), Node.id(), 1)
      this.adjacencyList[this.srcNode].push(neighbour)
      this.edgeList.push([this.srcNode, this.adjacencyList[this.srcNode].length-1])
      this.addBranchProp(this.lineBeingDrawn.points())
      console.log(this.adjacencyList)
      console.log(this.edgeList)

    }
    });

  }

  addBranchProp(points:number[]){

    var arrowPoints:number[] = []
    let radians:number = 0
    if (points.length % 4 ==0)
      arrowPoints = [points[points.length/2 -2], points[points.length/2 -1], points[points.length/2], points[points.length/2 + 1]]

    else
      arrowPoints = [points[points.length/2 -1], points[points.length/2], points[points.length/2 + 1], points[points.length/2 + 2]]

    console.log("arrow Points", arrowPoints)
    var arrow = new Konva.Shape({
      stroke:'yellow',
      sceneFunc: function(context, arrow){

        var PI2 = Math.PI * 2
        var dx = arrowPoints[2] - arrowPoints[0]
        var dy = arrowPoints[3] - arrowPoints[1]

        radians = (Math.atan2(dy, dx) + PI2) % PI2
        var length = 15
        var width = 10

        context.beginPath();
        context.translate((arrowPoints[2] + arrowPoints[0])/2, (arrowPoints[1] + arrowPoints[3])/2);
        context.rotate(radians);
        context.moveTo(0, 0);
        context.lineTo(-length, width / 2);
        context.moveTo(0, 0);
        context.lineTo(-length, -width / 2);
        context.restore();
        context.fillStrokeShape(arrow);
      }
    })

    var dx =   arrowPoints[2] - arrowPoints[0]
    var dy = -1 * ( arrowPoints[3] - arrowPoints[1] )
    radians =  Math.atan2(dy, dx)

    var gainText = new Konva.Text({
      text: '1',
      fontSize: 20,
      x:(arrowPoints[2] + arrowPoints[0])/2 - 20 * Math.cos(Math.PI/2 - radians),
      y:(arrowPoints[3] + arrowPoints[1])/2 - 20 * Math.sin(Math.PI/2 - radians),
      height: 4,
      fill: '#000',
      verticalAlign:'middle' })

    this.gainValues.push(gainText)

    this.layer.add(gainText)
    this.layer.add(arrow)
    this.layer.draw()
  }

createBranch(pos:Vector2d, Node:Konva.Group):Konva.Line{

    var branch = new Konva.Line({
      id: String(this.edgeID++),
      stroke: 'black',
      listening: false,
      points: [Node.x(), Node.y(), pos.x, pos.y],
      strokeWidth: 3
    });

    branch.on('mousedown', (e) => {
       branch.stroke("Blue")
      if (this.selectedLine != null){
        this.selectedLine.stroke("black")
      }
       this.selectedLine = branch
       this.showGainValue(branch)
     });

    return branch

  }

  addNewBranch(){
    if (this.drawingStatus = "Stop")
    this.drawingStatus = "Start"
  }

  showGainValue(branch:Konva.Line){
    console.log(this.gainValue)
    this.gainValue = String(this.adjacencyList[this.edgeList[parseInt(branch.id())][0]][this.edgeList[parseInt(branch.id())][1]].gain)
  }

  getNewGain(value:string){

    var newGain = 0

    if (value != "")
      newGain = parseInt(value)

    if (this.selectedLine != null){
      this.adjacencyList[this.edgeList[parseInt(this.selectedLine.id())][0]][this.edgeList[parseInt(this.selectedLine.id())][1]].gain = parseFloat(value)
      this.gainValues[parseFloat(this.selectedLine.id())].text(value)
      this.layer.draw()
    }


    this.gainValue = value
  }

  clear() {

    this.nodeID = 0
    this.edgeID = 0
    this.drawingStatus = "Stop"
    this.gainValue = ""
    this.adjacencyList = [];
    this.edgeList = []
    this.selectedLine = null
    this.gainValues = []
    this.nodeList = []
    this.layer.destroy()
    this.layer = new Konva.Layer()
    this.stage.add(this.layer)
    this.ans = ""

    this.service.sendClear().subscribe( (data) => {
        console.log(data);
      }
    )
  }

  sendAdjacencyList(){
   var src: String = ""
   var dest:String = ""

   for (let x = 0; x < this.adjacencyList.length; x++){
    var start:boolean = true;
    for (let i = 0; i < this.adjacencyList.length && start; i++){

      for (let j = 0; j < this.adjacencyList[i].length; j++){

        if (this.adjacencyList[i][j].dest == String(x))
              {
                    start = false;
                    break;
              }

      }

     }
     if (start && src == "")
      src = String(x)
     else if (start && src != ""){
      alert("The Graph isn't Correct")
       return null
     }
   }

   if (src == "" ){
    alert("The Graph isn't correct")
    return null
   }

   for (let x = 0; x < this.adjacencyList.length; x++){
     if (this.adjacencyList[x].length == 0){
       if (dest != ""){
        alert("The Graph isn't Correct")
        return null
       }
       else
         dest = String(x)

     }
   }

  var listToSend:AdjacencyListItem[] = []

  for (let i = 0; i < this.adjacencyList.length; i ++)
  {
    for (let j = 0; j < this.adjacencyList[i].length; j++)
    {
      var newEdge:AdjacencyListItem = new AdjacencyListItem("x" + String(this.adjacencyList[i][j].src),
                                                            "x" + String(this.adjacencyList[i][j].dest),
                                                            this.adjacencyList[i][j].gain)

      if (this.adjacencyList[i][j].src == src)
        newEdge.src = "START"

      if (this.adjacencyList[i][j].dest == dest)
        newEdge.dest = "END"

      listToSend.push(newEdge)
    }
  }

  var nodeList:String [] = []
  for (let i = 0; i < this.nodeList.length; i++){
    if (this.nodeList[i] == "x" + src)
      nodeList.push("START")
    else if (this.nodeList[i] == "x" + dest)
      nodeList.push("END")
    else
      nodeList.push(this.nodeList[i])
  }

  return {edgeList: listToSend, nodeList:nodeList}
  }

    async solve() {
    var listToSend = this.sendAdjacencyList()
    if (listToSend != null) {
      let sentSuccessfully = false;
      this.service.sendLists(listToSend.edgeList, listToSend.nodeList).subscribe(data => {
        console.log(data)
        sentSuccessfully = true;
      })

      while (!sentSuccessfully) {
        await new Promise(r => setTimeout(r, 50));
      }

      let res:string = ""
      let paths:string = ""
      let loops:string = ""
      let individualLoops:string = ""
      let delta:string = ""

      sentSuccessfully = false;
      this.service.requestOutput().subscribe(data => {
        let res =  (data as String) 
        console.log(res)
        this.ans = "Transfer Function = " + res+ "\n"
        sentSuccessfully = true;
      })

      while (!sentSuccessfully) {
        await new Promise(r => setTimeout(r, 50));
      }

      sentSuccessfully = false;
      this.service.requestPaths().subscribe(data => {
        let paths = (data as string) 
        this.ans += paths 
        console.log(this.ans, paths)
        sentSuccessfully = true;
      })

      while (!sentSuccessfully) {
        await new Promise(r => setTimeout(r, 50));
      }

      sentSuccessfully = false;
      this.service.requestLoops().subscribe(data => {
        let loops = (data as string) 
        this.ans += loops 
        console.log(this.ans, loops)
        sentSuccessfully = true;
      })

      while (!sentSuccessfully) {
        await new Promise(r => setTimeout(r, 50));
      }

      sentSuccessfully = false;
      this.service.requestIndividualLoops().subscribe(data => {
        let individualLoops = (data as string) 
        this.ans += individualLoops 
        console.log(this.ans, individualLoops)
        sentSuccessfully = true;
      })

      while (!sentSuccessfully) {
        await new Promise(r => setTimeout(r, 50));
      }

      sentSuccessfully = false;
      this.service.requestDelta().subscribe(data => {
        let delta = (data as string) 
        this.ans += delta 
        console.log(this.ans, delta)
        sentSuccessfully = true;
      })

      while (!sentSuccessfully) {
        await new Promise(r => setTimeout(r, 50));
      }

      this.service.sendClear().subscribe( (data) => {
        console.log(data);
      }
    )
    }

  }

}

