package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;

import java.util.List;

public interface IGraphAddService {
    boolean addNode(String nodeName);
    boolean addEdge(Edge edge);
    boolean addPath(List<Edge> pathEdges);
    boolean addLoop(List<Edge> loopEdges);
}
