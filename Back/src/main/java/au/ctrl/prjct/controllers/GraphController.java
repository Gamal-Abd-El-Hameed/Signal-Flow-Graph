package au.ctrl.prjct.controllers;

import au.ctrl.prjct.graph.Edge;
import au.ctrl.prjct.graph.SignalFlowGraph;
import au.ctrl.prjct.payloads.InputPayload;
import au.ctrl.prjct.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
public class GraphController {
    private SignalFlowGraph graph;
    private IInputHandler inputHandler;
    private ITransferFunctionService transferService;
    private IForwardPathsService pathsService;
    private ILoopsService loopsService;
    private OutputHandler outputHandler;

    @Autowired
    public GraphController(SignalFlowGraph graph,
                           IInputHandler inputHandler,
                           ITransferFunctionService transferService,
                           IForwardPathsService pathsService,
                           ILoopsService loopsService,
                           OutputHandler outputHandler) {
        this.graph = graph;
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        this.transferService = transferService;
        this.pathsService = pathsService;
        this.loopsService = loopsService;
    }

    @PostMapping("/input")
    public boolean takeEdges(@RequestBody InputPayload payload) {
        System.out.println(payload.getNodes());
        System.out.println(payload.getSources());
        System.out.println(payload.getDestinations());
        System.out.println(payload.getGains());
        var res = inputHandler.handleInput(payload);
        for(List<Edge> edgeList : graph.getEdges()) {
            System.out.println(edgeList);
        }
        return res;
    }

    @GetMapping("/output")
    public double returnOutput() {
        pathsService.getForwardPaths(graph, new ArrayList<>());
        loopsService.getGraphLoops();
        System.out.println("Printing paths...");
        for(List<Edge> path: graph.getPaths()) {
            System.out.println(path);
        }
        System.out.println("Printing loops...");
        for(List<Edge> loop: graph.getLoops()) {
            System.out.println(loop);
        }
        var res = transferService.result();

        ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingIndex =transferService.getNonTouchingIndexes();
        System.out.println("HI Loops Size: "+graph.getLoops().size());
        for(int i=0;i<nonTouchingIndex.size();i++){
            for (int j=0;j<nonTouchingIndex.get(i).size();j++){
                for (int k=0;k<nonTouchingIndex.get(i).get(j).size();k++){
                    System.out.println("order: "+ i + " , subsetNum: " + j +" , index: "+ k +" :" +nonTouchingIndex.get(i).get(j).get(k));
                }
            }
        }
        System.out.println(res);
        return res;
    }

    @DeleteMapping("/clear")
    public boolean clearGraph() {
        var res = new GraphOperationService(graph).clearGraph();
        this.transferService.clear();
        System.out.println(graph.getNodes());
//        for(List<Edge> edgeList : graph.getEdges()) {
//            System.out.println(edgeList);
//        }
        return res;
    }

    @GetMapping("/paths")
    public String sendPaths() {
        System.out.println("Paths Nour:" +  this.outputHandler.getPaths(graph.getPaths()));
        return this.outputHandler.getPaths(graph.getPaths());
    }

    @GetMapping("/loops")
    public String sendLoops() {
        System.out.println("loops Nour:" +  this.outputHandler.getLoops(graph.getLoops()));
        return this.outputHandler.getLoops(graph.getLoops());
    }

    @GetMapping("/individualLoops")
    public String sendIndividualLoops() {
        System.out.println("individualLoops Nour:" +  this.outputHandler.getNonTouchingLoops(this.transferService.getNonTouchingIndexes()));
        return this.outputHandler.getNonTouchingLoops(this.transferService.getNonTouchingIndexes());
    }

    @GetMapping("/deltas")
    public String sendDeltas() {
        System.out.println("delta Nour:" +  this.transferService.getDeltas());
        return this.outputHandler.getDelta( this.transferService.getDeltas());
    }
}
