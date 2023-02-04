package au.ctrl.prjct.services;

import au.ctrl.prjct.graph.Edge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutputHandler {

    @Autowired
    public OutputHandler(){

    }

    public String getPaths(List<List<Edge>> Paths){

        String res = "Forward Paths:\t";
        for (int i = 0; i < Paths.size(); i++){
            res += String.valueOf(i + 1) + "- " + Paths.get(i).get(0).getFromNode().toString() + ", ";

            for (int j = 1; j < Paths.get(i).size(); j++)
                res += Paths.get(i).get(j).getFromNode().toString() + ", ";

            res += Paths.get(i).get(Paths.get(i).size()-1).getToNode().toString() + "\n";
        }
        return res;

    }

    public String getLoops(List<List<Edge>> loops){

        String res = "Loops:\t";
        for (int i = 0; i < loops.size(); i++){
            res += "L" + String.valueOf(i) + ": " + loops.get(i).get(0).getFromNode().toString() + ", ";

            for (int j = 1; j < loops.get(i).size(); j++)
                res += loops.get(i).get(j).getFromNode().toString() + ", ";

            res += loops.get(i).get(0).getFromNode().toString() + "\n";
        }
        return res;

    }

   public String getNonTouchingLoops (ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingIndex){

       String res = "\nNon-Touching Loops:\n";
       for (int i = 1; i < nonTouchingIndex.size(); i++){
           if (nonTouchingIndex.get(i).size() == 0)
               continue;
           res += String.valueOf(i+1) +  "-Non-Touching loops:\t";
           for (int j = 0; j < nonTouchingIndex.get(i).size(); j++){
               res += String.valueOf(j + 1) + " - ";
               for (int k = 0; k < nonTouchingIndex.get(i).get(j).size(); k++){
                   res += "L"+ String.valueOf(nonTouchingIndex.get(i).get(j).get(k));
                   if (k != nonTouchingIndex.get(i).get(j).size() - 1)
                       res += ", ";
               }
               res += "\t\t";
           }
           res += "\n";
       }
       return res;
   }


    public String getDelta(List<Double> deltas) {

        if (deltas.size() != 0) {
            String res = "Deltas:\t";
            res += "Delta = " + String.valueOf(deltas.get(0)) + "\t\t";
            for (int i = 1; i < deltas.size(); i++)
                res += "Delta-" + String.valueOf(i) + " = " + String.valueOf(deltas.get(i)) + "\t\t";

            return res;
        }
        return "";
    }
}
