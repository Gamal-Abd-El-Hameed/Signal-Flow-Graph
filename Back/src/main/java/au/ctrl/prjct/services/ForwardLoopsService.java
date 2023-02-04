/*package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;
import au.ctrl.prjct.graph.SignalFlowGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class ForwardLoopsService implements ILoopsService {
    private SignalFlowGraph graph;
    GraphOperationService service = new GraphOperationService(graph);
    private ArrayList<ArrayList<String>> loops = new ArrayList<>();

    @Autowired
    public ForwardLoopsService(SignalFlowGraph graph) {
        this.graph = graph;
    }

    // get the loopsService that start at the given node
    @Override
    public ArrayList<ArrayList<String>> getAllLoops(String node) {
        // get all possible loopsService
        ArrayList<ArrayList<String>> duplicatedLoops = getAllPossibleLoops(node);
        // construct the loop without any duplicated nodes except start node
        ArrayList<ArrayList<String>> set = new ArrayList<>();
        for (ArrayList<String> arrayList : duplicatedLoops) {
            ArrayList<String> tmp = getTheLoop(arrayList);
            if(isValidLoop(tmp))
                set.add(tmp);
        }

        // remove duplicated loopsService
        removeDublicates(set);
        setGraphLoops(set);
        return set;
    }

    void setGraphLoops(ArrayList<ArrayList<String>> set) {
        GraphOperationService service = new GraphOperationService(this.graph);
        List<List<Edge>> ans = new ArrayList<>(new ArrayList<>());
        int setSize = set.size();
        for(int i = 0; i < setSize; i++) {
            ArrayList<Edge> resLoop = new ArrayList<>();
            ArrayList<String> myLoop = set.get(i);
            int n = myLoop.size();
            for(int j = 0; j < n - 1; j++) {
                String fromNode = myLoop.get(j);
                String toNode = myLoop.get(j + 1);
                List<Edge> nodeEdges = service.getNodeEdges(fromNode);
                int nodeEdgesSize = nodeEdges.size();
                for(int k = 0; k < nodeEdgesSize; k++) {
                    Edge edge = nodeEdges.get(k);
                    if(toNode.equals(edge.getToNode())) {
                        resLoop.add(edge);
                        break;
                    }
                }
            }
            ans.add(resLoop);
        }
        this.graph.setLoops(ans);
    }

    private ArrayList<ArrayList<String>> getAllPossibleLoops(String node) {
        HashMap<String, Boolean> isVisited = new HashMap<>();
        List<List<Edge>> edges = this.graph.getEdges();
        HashMap<String, List<Edge>> adjacentNodes = new HashMap<>();
        for(int i = 0; i < edges.size(); i++) {
            if(edges.get(i).isEmpty()) continue;
            String startNode = edges.get(i).get(0).getFromNode();
//            isVisited.put(startNode, false);
//            if (edges.get(i).get(0).getFromNode().equals("END")) continue;
            adjacentNodes.put(startNode, edges.get(i));
        }
        for(int i = 0; i < edges.size(); i++) {
            List<Edge> tmp = edges.get(i);
            if(tmp.isEmpty()) continue;
            for(int j = 0; j < tmp.size(); j++) {
                Edge edge = tmp.get(j);
                isVisited.put(edge.getFromNode(), false);
                isVisited.put(edge.getToNode(), false);
            }
        }
        loops.clear();
        dfs(adjacentNodes, node, isVisited, new ArrayList<>());
        return loops;
    }

    private void dfs(HashMap<String, List<Edge>> adjacentNodes, String node, HashMap<String, Boolean> isVisited, ArrayList<String> loop) {
        if(node == null || node.equals("END")) return;
        if(isVisited.get(node) == true) { // if it is the end node
            if(!loop.get(0).equals(node)) // if start != end (Another inner loop)
                loop.add(0, node); // add the node as the start node of the loop
            loops.add((ArrayList<String>) loop.clone()); // add the loop to loopsService
            return;
        }
        isVisited.put(node, true);

        for (Edge nei : adjacentNodes.get(node)) {
            loop.add(nei.getToNode());
            dfs(adjacentNodes, nei.getToNode(), isVisited, loop);
            loop.remove(loop.size() - 1); // remove the current node from end
        }
        isVisited.put(node,false); /////////////////
    }

    private ArrayList<String> getTheLoop(ArrayList<String> loop) {
        // check if duplicated nodes
        String end = loop.get(loop.size() - 1);
        ArrayList<String> ans = new ArrayList<>();
        ans.add(end);
        for(int i = loop.size() - 2; i >= 0; i--) {
            ans.add(loop.get(i));
            if (loop.get(i).equals(end)) break;
        }
        // check the validity of the loop
        return reverseArray(ans);
    }

    private void removeDublicates (ArrayList<ArrayList<String>> loops) {
        for(int i = 0; i < loops.size() - 1; i++)
            for (int j = i + 1; j < loops.size(); j++)
                if (distinctEquals(loops.get(i), loops.get(j))) loops.remove(j);
    }

    private boolean distinctEquals(ArrayList<String> loop1, ArrayList<String> loop2) {
        HashSet<String> a, b;
        a = new HashSet<>(loop1);
        b = new HashSet<>(loop2);
        return a.equals(b);
    }

    private boolean isValidLoop(ArrayList<String> loop) {
        ArrayList<String> ans = new ArrayList<>();
        // Add all nodes except the start and end nodes
        for(int i = 1; i < loop.size() - 1; i++)
            ans.add(loop.get(i));
        for(int i = 0; i < ans.size() - 1; i++)
            for (int j = i + 1; j < ans.size(); j++)
                if (ans.get(i).equals(ans.get(j))) return false;
        return true;
    }

    private ArrayList<String> reverseArray(ArrayList<String> loop) {
        ArrayList<String> ans = new ArrayList<>();
        for(int i = loop.size() - 1; i >= 0; i--)
            ans.add(loop.get(i));
        return ans;
    }
}

 */