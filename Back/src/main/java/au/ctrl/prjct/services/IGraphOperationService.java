package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;

import java.util.List;
import java.util.ArrayList;

public interface IGraphOperationService {
    int getNode(String nodeName);
    List<Edge> getNodeEdges(String nodeName);
    boolean edgeExists(Edge edge);
    boolean clearGraph();
    // ArrayList<ArrayList<String>> getAllLoops(String node);
}
