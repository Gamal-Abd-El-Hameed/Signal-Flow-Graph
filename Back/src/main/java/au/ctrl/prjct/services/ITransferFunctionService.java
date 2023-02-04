package au.ctrl.prjct.services;

import java.util.ArrayList;
import java.util.List;

public interface ITransferFunctionService {
    double pathGain (int pathIndex);
    double loopGain (int loopIndex);
    double delta (int pathIndex);
    ArrayList<ArrayList<ArrayList<Integer>>> getNonTouchingIndexes();
    List<Double> getDeltas();
    double result ();
    void clear();
}
