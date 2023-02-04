package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;
import au.ctrl.prjct.graph.SignalFlowGraph;

import java.util.List;

public interface IForwardPathsService {
     void getForwardPaths(SignalFlowGraph graph, List<Edge> forward_path);

}
