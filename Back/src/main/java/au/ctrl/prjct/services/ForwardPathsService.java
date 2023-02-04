package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;
import au.ctrl.prjct.graph.SignalFlowGraph;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForwardPathsService implements IForwardPathsService  {
    public void getForwardPaths(SignalFlowGraph graph, List<Edge> forward_path) {
       List<List<Edge>> adj_list=graph.getEdges();
       for(Edge i : adj_list.get(0)){
            boolean[]flags=new boolean[adj_list.size()];
            flags[0]=true;
            forward_path.add(i);
            dfs(graph,graph.getNodes().get(i.getToNode()),flags,forward_path);
            forward_path.remove(forward_path.get(forward_path.size()-1));
       }
       return;
    }
    private void dfs(SignalFlowGraph graph,int next_node,boolean[]flags,List<Edge>forward_path){
        if(graph.getEdges().get(next_node).isEmpty()){
            List<Edge>tmp=new ArrayList<>();
            tmp.addAll(forward_path);
            graph.getPaths().add(tmp);
            return;
        }
        flags[next_node]=true;
        List<List<Edge>> adj_list=graph.getEdges();
        for(Edge i : adj_list.get(next_node)){
            if(!flags[graph.getNodes().get(i.getToNode())]){
                forward_path.add(i);
                dfs(graph,graph.getNodes().get(i.getToNode()),flags,forward_path);
                forward_path.remove(forward_path.get(forward_path.size()-1));
            }
        }
        flags[next_node]=false;
    }
}