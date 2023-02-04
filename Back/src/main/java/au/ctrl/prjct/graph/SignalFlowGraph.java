package au.ctrl.prjct.graph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SignalFlowGraph {
    /**
     * Map of nodes in graph, keys are node names, values are adjacency list indices.
     * First and last indices are "START" and "END" nodes.
     */
    private Map<String, Integer> nodes;
    /**
     * List of directed edges in graph, implemented via adjacency list.
     * Second dimension lists' contents are edges such that the starting node
     * of the edge is the node referred to by the index of the first dimension.
     */
    private List<List<Edge>> edges;
    /**
     * List of paths between "START" and "END" nodes.
     * Second dimension lists' contents are the successive edges that lead from
     * START node to END node.
     */
    private List<List<Edge>> paths;
    /**
     * List of loops in graph, where a loop is defined as a sequence of edges that
     * start at a node and terminate at the same node.
     * Second dimension lists' contents are the successive edges that lead from a
     * node to itself.
     */
    private List<List<Edge>> loops;

    /**
     * Constructor for a signal flow graph object, takes four lists as parameter.
     * Provided lists are preferably empty, if not the lists are cleared.
     *
     * @param nodes <code>List</code> of <code>String</code> used to store nodes.
     * @param edges <code>List</code> of <code>List</code> of <code>Edge</code> used to store directed edges between nodes.
     * @param paths <code>List</code> of <code>List</code> of <code>Edge</code> used to store paths from START to END nodes.
     * @param loops <code>List</code> of <code>List</code> of <code>Edge</code> used to store loops.
     */
    @Autowired
    public SignalFlowGraph(Map<String, Integer> nodes,
                           @Qualifier("edges") List<List<Edge>> edges,
                           @Qualifier("paths") List<List<Edge>> paths,
                           @Qualifier("loops") List<List<Edge>> loops) {
        this.nodes = nodes;
        this.edges = edges;
        this.paths = paths;
        this.loops = loops;
        this.initGraph();
    }

    private void initGraph() {
        if(!this.nodes.isEmpty()) {
            this.nodes.clear();
        }
        if(!this.edges.isEmpty()) {
            this.edges.clear();
        }
        if(!this.paths.isEmpty()) {
            this.paths.clear();
        }
        if(!this.loops.isEmpty()) {
            this.loops.clear();
        }
        this.nodes.put("START", 0);
        this.edges.add(new ArrayList<>());
        this.nodes.put("END", 1);
        this.edges.add(new ArrayList<>());
    }

    public Map<String, Integer> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, Integer> nodes) {
        this.nodes = nodes;
    }

    public List<List<Edge>> getEdges() {
        return edges;
    }

    public void setEdges(List<List<Edge>> edges) {
        this.edges = edges;
    }

    public List<List<Edge>> getPaths() {
        return paths;
    }

    public void setPaths(List<List<Edge>> paths) {
        this.paths = paths;
    }

    public List<List<Edge>> getLoops() {
        return loops;
    }

    public void setLoops(List<List<Edge>> loops) {
        this.loops = loops;
    }
}
