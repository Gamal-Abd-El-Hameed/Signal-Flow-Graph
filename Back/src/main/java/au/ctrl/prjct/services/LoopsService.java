package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoopsService implements ILoopsService{
    private IGraphOperationService opService;
    private IGraphAddService addService;

    @Autowired
    public LoopsService(IGraphOperationService opService, IGraphAddService addService) {
        this.opService = opService;
        this.addService = addService;
    }

    @Override
    public void getGraphLoops() {
        List<Edge> startEdges = this.opService.getNodeEdges("START");
        for(Edge edge: startEdges) {
            List<Edge> loopList = new ArrayList<>();
            loopList.add(edge);
            depthSearch(loopList);
        }
    }

    private void depthSearch(List<Edge> loop) {
        if(loop.get(loop.size() - 1).getToNode().equals("END")) {
            return;
        }
        for(int i = 0; i < loop.size() - 1; i++) {
            if(loop.get(loop.size() - 1).getToNode().compareTo(loop.get(i).getFromNode()) == 0) {
                this.addService.addLoop(loop.subList(i, loop.size()));
                return;
            }
        }
        String currNode = loop.get(loop.size() - 1).getToNode();
        for(Edge edge: this.opService.getNodeEdges(currNode)) {
            List<Edge> loopCopy = new ArrayList<>(loop);
            loopCopy.add(edge);
            depthSearch(loopCopy);
        }
    }
}
