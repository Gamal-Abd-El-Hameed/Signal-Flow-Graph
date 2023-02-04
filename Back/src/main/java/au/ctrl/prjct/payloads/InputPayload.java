package au.ctrl.prjct.payloads;


import java.util.List;

public class InputPayload {
    private List<String> nodes;
    private List<String> sources;
    private List<String> destinations;
    private List<Double> gains;

    public InputPayload(List<String> nodes, List<String> sources, List<String> destinations, List<Double> gains) {
        this.nodes = nodes;
        this.sources = sources;
        this.destinations = destinations;
        System.out.println(gains);
        this.gains = gains;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public List<Double> getGains() {
        return gains;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public void setGains(List<Double> gains) {
        this.gains = gains;
    }
}
