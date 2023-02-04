package au.ctrl.prjct.graph;

import java.util.Objects;

/**
 * Class that represents a weighted directed edge.
 * Immutable.
 */
public class Edge {
    /**
     * Node where the edge starts.
     */
    private String fromNode;
    /**
     * Node where the edge terminates.
     */
    private String toNode;
    /**
     * Gain between from node and to node represented as a <code>double</code>.
     */
    private double edgeGain;
    /**
     * Edge distinguishing identifier.
     */
    private int id;

    public Edge(String fromNode, String toNode, double edgeGain, int id) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.edgeGain = edgeGain;
        this.id = id;
    }

    public String getFromNode() {
        return fromNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    public String getToNode() {
        return toNode;
    }

    public double getEdgeGain() {
        return edgeGain;
    }

    public boolean isTouching(Edge edge) {
        if(this.toNode.equals(edge.toNode) || this.toNode.equals(edge.fromNode)
                || this.fromNode.equals(edge.toNode) || this.fromNode.equals(edge.fromNode))
            return true;
        else return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        return id == edge.id && edgeGain == edge.edgeGain && fromNode.equals(edge.fromNode) && toNode.equals(edge.toNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromNode, toNode, edgeGain);
    }

    @Override
    public String toString() {
        return "[From: " + this.fromNode + ", To: " + this.toNode + ", Gain: " + this.edgeGain + "]";
    }
}
