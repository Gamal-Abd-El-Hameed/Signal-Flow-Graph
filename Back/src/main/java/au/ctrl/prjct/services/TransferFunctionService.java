package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;
import au.ctrl.prjct.graph.SignalFlowGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransferFunctionService implements ITransferFunctionService{
    SignalFlowGraph graph;
    IForwardPathsService forwardPathsService;
    ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingIndex;
    ArrayList<ArrayList<Integer>> subNonTouchingIndex;
    ArrayList<Integer> tempIndex;
    List<List<Edge>> loops;
    HashMap<Integer, Boolean> hashMap;
    List<List<Edge>> pathsList;
    List<Double> deltas;

    @Autowired
    public TransferFunctionService(SignalFlowGraph graph) {
        this.graph = graph;
        forwardPathsService = new ForwardPathsService();
        nonTouchingIndex = new ArrayList<>();
        subNonTouchingIndex = new ArrayList<>();
        hashMap = new HashMap<>();
        pathsList = new ArrayList<>();
        deltas = new ArrayList<>();
    }

    /*public void evalNonTouchingLoops ( int numberOfNonTouchingLoops, int startingIndex)  {

        for ( int i = startingIndex;  i < loops.size();  i ++) {
            boolean nonTouching = true;
            for (Integer index : tempIndex) {
                if (!isNonTouching(loops.get(i), loops.get(index))) {
                    nonTouching = false;
                    break;
                }
            }

            if (!nonTouching) continue;

            tempIndex.add(i);

            if (  numberOfNonTouchingLoops == 1 ){
                subNonTouchingIndex.add(new ArrayList<>(tempIndex));
            }
            else if (  numberOfNonTouchingLoops > 1  ) {
                numberOfNonTouchingLoops--;
                startingIndex++;
                evalNonTouchingLoops(numberOfNonTouchingLoops, startingIndex);
            }
            numberOfNonTouchingLoops++;
            startingIndex--;
            tempIndex.remove(tempIndex.size()-1);
        }
    }*/

    public void evalNonTouchingLoops(int order) {
        for(int i = 0; i < nonTouchingIndex.get(order-2).size(); i++) {
            ArrayList<ArrayList<Integer>> nonTouchingLoops = nonTouchingIndex.get(order-2);
            //[[0,1],[0,2], [1,2]] order 2
            //[[0],[1],[2]] order 1
            for(int j = 0; j < nonTouchingIndex.get(0).size(); j++) {
                boolean allNonTouching = true;
                for(int k = 0; k < nonTouchingLoops.get(i).size(); k++) {
                    allNonTouching = allNonTouching &&
                            isNonTouching(graph.getLoops().get(nonTouchingLoops.get(i).get(k)), graph.getLoops().get(j));
                }
                if(allNonTouching) {
                    if(nonTouchingIndex.size() < order) {
                        nonTouchingIndex.add(new ArrayList<>());
                    }
                    ArrayList<Integer> temp = new ArrayList<>(nonTouchingLoops.get(i));
                    temp.add(j);
                    Collections.sort(temp);
                    boolean isDuplicate = false;
                    for(ArrayList<Integer> indexList: nonTouchingIndex.get(order-1)) {
                        isDuplicate = indexList.equals(temp);
                        if(isDuplicate) break;
                    }
                    if(!isDuplicate) nonTouchingIndex.get(order-1).add(temp);
                }
            }
        }
    }

    public boolean isNonTouching(List<Edge> loopOrPath, List<Edge> loop){
        boolean res = true;
        for(Edge loopOrPathEdge : loopOrPath) {
            for(Edge loopEdge : loop) {
                if(loopOrPathEdge.isTouching(loopEdge)) {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    @Override
    public ArrayList<ArrayList<ArrayList<Integer>>> getNonTouchingIndexes() {
        return nonTouchingIndex;
    }

    @Override
    public double pathGain(int pathIndex) {
        double result = 1;
        List<Edge> path = graph.getPaths().get(pathIndex);
        for (Edge edge : path) result *= edge.getEdgeGain();
        System.out.println("In pathGain()");
        System.out.println(result);
        return result;
    }

    @Override
    public double loopGain(int loopIndex) {
        double result = 1;
        List<Edge> loop = loops.get(loopIndex);
        for (Edge edge : loop) result *= edge.getEdgeGain();
        System.out.println("In loopGain()");
        System.out.println(result);
        return result;
    }

    public double getNonTouchingSubset(int order, int pathIndex){
        double result = 0;
        double temp;
        if(pathIndex == -1){
            if(order-1 >= nonTouchingIndex.size()){
                return 0;
            }
            for(int i = 0; i< nonTouchingIndex.get(order-1).size(); i++) {
                System.out.println("Non touching index:");
                System.out.println(nonTouchingIndex.get(order - 1).get(i));
            }
            for(int i = 0; i< nonTouchingIndex.get(order-1).size(); i++){
                temp = 1;
                for(int j=0;j<nonTouchingIndex.get(order-1).get(i).size();j++){
                    temp *= loopGain(nonTouchingIndex.get(order-1).get(i).get(j));
                }
                System.out.println("Loop gain product is: " + temp);
                result += temp;
            }
        }else{
            if(order==1){
                for(int i=0;i<loops.size();i++){
                    if(hashMap.get(i)){
                        result += loopGain(i);
                    }
                }
            }else{
                boolean flag;
                if(order-1 >= nonTouchingIndex.size()){
                    return 0;
                }
                ArrayList<ArrayList<Integer>> nonTouchingLoopsTemp = nonTouchingIndex.get(order-1);
                for (ArrayList<Integer> integers : nonTouchingLoopsTemp) {
                    flag = true;
                    temp = 1;
                    for (int index : integers) {
                        if (!hashMap.get(index)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        for (Integer integer : integers) {
                            temp *= loopGain(integer);
                        }
                        result += temp;
                    }
                }
            }
        }
        System.out.println("1 - Delta for path no." + pathIndex + " is " + result + " for order: " + order);
        return result;
    }

    public void init() {
        loops = graph.getLoops();

        pathsList = this.graph.getPaths();

        for(int j = 0; j < loops.size(); j++){
            tempIndex = new ArrayList<>();
            tempIndex.add(j);
            subNonTouchingIndex.add(tempIndex);
        }

        nonTouchingIndex.add(subNonTouchingIndex);

        for(int i = 1; i <= nonTouchingIndex.size(); i++){
            evalNonTouchingLoops(i+1);
            if(i < nonTouchingIndex.size()) {
                System.out.println("nonTouchingIndex for order" + (i+1) + ": ");
                System.out.println(nonTouchingIndex.get(i));
            }
        }
    }

    public double loopsNonTouching(int pathIndex){
        int order = 1;
        double result = 0;

        if(pathIndex != -1){
            hashMap = new HashMap<>();

            for(int j=0;j<loops.size();j++){

                if(!hashMap.containsKey(j)){
                    hashMap.put(j, true);
                }else if(!hashMap.get(j)){
                    continue;
                }
                if(! isNonTouching(pathsList.get(pathIndex), loops.get(j))){
                    hashMap.put(j,false);
                }
            }
        }
        double temp;
        while(true){
            temp = getNonTouchingSubset(order, pathIndex);
            if(temp == 0){
                break;
            }
            if(order % 2 == 1){
                result -=  temp;
            }else{
                result +=  temp;
            }
            order++;
        }
        return result;
    }

    @Override
    public double delta(int pathIndex) {
        double result = 0;

        result += 1 + loopsNonTouching(pathIndex);

        if(pathIndex == -1) {
            this.deltas.add(0, result);
        } else {
            this.deltas.add(result);
        }
        return result;
    }

    @Override
    public double result() {
        init();
        int numberOfPaths = graph.getPaths().size();
        System.out.println(numberOfPaths);
        double numerator = 0;
        for (int i=0; i<numberOfPaths; i++){
            numerator += pathGain(i) * delta(i);
        }
        double denominator = delta(-1);
        return (numerator  / denominator);
    }

    @Override
    public void clear() {
        forwardPathsService = new ForwardPathsService();
        nonTouchingIndex = new ArrayList<>();
        subNonTouchingIndex = new ArrayList<>();
        hashMap = new HashMap<>();
        pathsList = new ArrayList<>();
        deltas = new ArrayList<>();
    }

    @Override
    public List<Double> getDeltas() {
        return deltas;
    }
}
