package au.ctrl.prjct;

import au.ctrl.prjct.graph.Edge;
import au.ctrl.prjct.graph.SignalFlowGraph;
import au.ctrl.prjct.services.ForwardPathsService;
import au.ctrl.prjct.services.GraphAddService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PrjctApplicationTests {
    @Test
    void test_forward_path3() {
        SignalFlowGraph test_graph = new SignalFlowGraph(new HashMap<>(), new ArrayList<>(new ArrayList<>()), new ArrayList<>(new ArrayList<>()), new ArrayList<>(new ArrayList<>()));
        GraphAddService test_service = new GraphAddService(test_graph);
        int id = 0;
        test_service.addNode("X0");
        test_service.addNode("X1");
        test_service.addNode("X2");
        test_service.addNode("X3");
        test_service.addNode("X4");
        test_service.addEdge(new Edge("START", "X0", 1, id++));
        test_service.addNode("X5");
        test_service.addEdge(new Edge("X0", "X1", 1, id++));
        test_service.addEdge(new Edge("X0", "X3", 1, id++));
        test_service.addEdge(new Edge("X1", "X2", 1, id++));
        test_service.addEdge(new Edge("X2", "X3", 1, id++));
        test_service.addEdge(new Edge("X2", "X5", 1, id++));
        test_service.addEdge(new Edge("X3", "X4", 1, id++));
        test_service.addEdge(new Edge("X4", "END", 1, id++));
        test_service.addEdge(new Edge("X2", "X3", 15, id++));
        test_service.addEdge(new Edge("X2", "X4", 10, id++));
        test_service.addEdge(new Edge("X3", "X2", -2, id++));
        test_service.addEdge(new Edge("X3", "X1", -2, id++));
        test_service.addEdge(new Edge("X4", "X5", 1, id++));
        new ForwardPathsService().getForwardPaths(test_graph, new ArrayList<>());
        //test_payload.convertPayloadToPathList(test_graph);
        //TransferFunctionService test_transfer_Service= new TransferFunctionService(test_graph);
        /*assertEquals("START X0 X1 X2 X3 X4 END",test_payload.getForward_paths().get(0));
        assertEquals("START X0 X1 X2 X3 X4 END",test_payload.getForward_paths().get(1));
        assertEquals("START X0 X1 X2 X4 END",test_payload.getForward_paths().get(2));
        assertEquals(1,test_payload.getPath_gain().get(0));
        assertEquals(15,test_payload.getPath_gain().get(1));
        assertEquals(10,test_payload.getPath_gain().get(2));
        */
        for (List<Edge> edgeList : test_graph.getPaths()) {
            for (Edge edge : edgeList) {
                System.out.println(edge.getFromNode() + " " + edge.getToNode() + " " + edge.getEdgeGain());
            }
            System.out.println("Path Completed");
        }
    }
}
