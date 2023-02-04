package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;
import au.ctrl.prjct.graph.SignalFlowGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class GraphOperationService implements IGraphOperationService{
    SignalFlowGraph graph;
    

    @Autowired
    public GraphOperationService(SignalFlowGraph graph) {
        this.graph = graph;
    }

    @Override
    public int getNode(String nodeName) {
        return this.graph.getNodes().getOrDefault(nodeName, -1);
    }

    @Override
    public List<Edge> getNodeEdges(String nodeName) {
        int nodeIndex = this.getNode(nodeName);
        if(nodeIndex == -1) return null;
        return this.graph.getEdges().get(nodeIndex);
    }

    @Override
    public boolean edgeExists(Edge edge) {
        int fromIndex = this.getNode(edge.getFromNode());
        int toIndex = this.getNode(edge.getToNode());
        if(fromIndex == -1 || toIndex == -1) return false;
        List<Edge> edgeList = this.graph.getEdges().get(fromIndex);
        return edgeList.contains(edge);
    }

    @Override
    public boolean clearGraph() {
        try {
            graph.getNodes().clear();
            graph.getEdges().clear();
            graph.getPaths().clear();
            graph.getLoops().clear();
        } catch (UnsupportedOperationException ex) {
            return false;
        }
        graph.getNodes().put("START", 0);
        graph.getEdges().add(new ArrayList<>());
        graph.getNodes().put("END", 1);
        graph.getEdges().add(new ArrayList<>());
        return true;
    }

    // get the loopsService that start at the given node
    // @Override    
    // public ArrayList<ArrayList<String>> getAllLoops(String node) {
    //     // get all possible loopsService
    //     ArrayList<ArrayList<String>> duplicatedLoops = getAllPossibleLoops(node);
    //     // construct the loop without any duplicated nodes except start node
    //     ArrayList<ArrayList<String>> set = new ArrayList<>();        
    //     for (ArrayList<String> arrayList : duplicatedLoops)
    //     // remove duplicated loopsService
    //         set.add(getTheLoop(arrayList));
    //     removeDublicates(set);
    //     return set;
    // }

    // private ArrayList<ArrayList<String>> getAllPossibleLoops(String node) { /////////////////////
    //     HashMap<String, Boolean> isVisited = new HashMap<>();
    //     List<List<Edge>> edges = this.graph.getEdges(); 
    //     HashMap<String, List<Edge>> newHash = new HashMap<>();
    //     // for(List<Edge> i : edges)
    //     //     for(Edge j : i)
    //     //         isVisited.put(j.getFromNode(), false);
    //     for(int i = 0; i < edges.size(); i++) {
    //         if(edges.get(i).isEmpty()) continue;
    //         String startNode = edges.get(i).get(0).getFromNode();
    //         isVisited.put(startNode, false);
    //         newHash.put(startNode, edges.get(i));
    //     }       
    //     loopsService.clear();
    //     dfs(newHash,node,isVisited,new ArrayList<>());
    //     return loopsService;
    // }

    // private void dfs(HashMap<String, List<Edge>> adjHash, String node, HashMap<String,Boolean> visited, ArrayList<String> loop) {
    //     if(visited.get(node)) { // if it is the end node
    //         if(!loop.get(0).equals(node)) // if start != end (Another inner loop)
    //             loop.add(0, node); // add the node as the start node of the loop
    //         loopsService.add((ArrayList<String>) loop.clone()); // add the loop to loopsService
    //         return;
    //     }
    //     visited.put(node,true);

    //     for (Edge nei : adjHash.get(node)) {
    //         loop.add(nei.getToNode());
    //         dfs(adjHash,nei.getToNode(),visited,loop);
    //         loop.remove(loop.size() - 1); // remove the current node
    //     }
    //     visited.put(node,false); // will remove duplicated loopsService soon
    // }    
    
    // private ArrayList<String> getTheLoop(ArrayList<String> loop) {
    //     String end = loop.get(loop.size() - 1);
    //     ArrayList<String> ans = new ArrayList<>();
    //     ans.add(end);
    //     for(int i = loop.size() - 2; i >= 0; i--) {
    //         ans.add(loop.get(i));
    //         if (loop.get(i).equals(end)) break;
    //     }
    //     return reverseArray(ans);
    // }

    // private void removeDublicates (ArrayList<ArrayList<String>> loopsService) {
    //     for(int i = 0; i < loopsService.size() - 1; i++)
    //         for (int j = i + 1; j < loopsService.size(); j++)
    //             if (distinctEquals(loopsService.get(i), loopsService.get(j))) loopsService.remove(j);
    // }

    // private boolean distinctEquals(ArrayList<String> loop1, ArrayList<String> loop2) {
    //     HashSet<String> a, b;
    //     a = new HashSet<>(loop1);
    //     b = new HashSet<>(loop2);
    //     return a.equals(b);
    // }

    // private ArrayList<String> reverseArray(ArrayList<String> loop) {
    //     ArrayList<String> ans = new ArrayList<>();
    //     for(int i = loop.size() - 1; i >= 0; i--)
    //         ans.add(loop.get(i));        
    //     return ans;
    // }    
}
