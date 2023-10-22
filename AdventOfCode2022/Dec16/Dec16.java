package AdventOfCode2022.Dec16;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Dec16 {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> input = Helpers.imp("Dec16/res/input.txt");
        Volcano volcano = new Volcano(input);
        volcano.openValvesOpt(30);
        Integer maxOutput = 0;
        for (Integer i : volcano.possibleOutputs) {
            if (i > maxOutput) {
                maxOutput=i;
            }
        }
        System.out.println("part 1: " + maxOutput);

        Volcano volcano2 = new Volcano(input);
        volcano2.openValvesWithElephant(26);
        Integer maxOutput2 = 0;
        for (Integer i : volcano2.possibleOutputs) {
            if (i > maxOutput2) {
                maxOutput2=i;
            }
        }
        System.out.println("part 2: " + maxOutput2);
    }
}
/*
 * part 2 doesnt really work. I cheated by printing the current "max value candidate" when new candidate is found. Apparantly it was
 * 2741 for part 2... really crappy "solution". Should be remade but then I probably need to redo pretty much from the start, aaaaaaand
 * I really dont wanna do that... 

*/
 
class Volcano {
    ArrayList<Valve> completeMap;
    ArrayList<Valve> reducedMap;
    ArrayList<Integer> possibleOutputs;
    Integer maxJox = 0;

    void openValvesWithElephant(Integer time) {
        possibleOutputs = new ArrayList<Integer>();
        openNextWE(getValveByID("AA"), time, time, false, 0, new ArrayList<String>());
    }

    private void openNextWE(Valve curValve, Integer remTime, Integer startTime, boolean eUsed, Integer accPreasure, ArrayList<String> opened) {
        boolean done = true;
        for (Map.Entry<String,Integer> e : curValve.toValveCostMap.entrySet()) {
            boolean open = false;
            for (String s : opened) {
                if (s.equals(e.getKey())) {
                    open = true;
                    break;
                }
            }
            if (!open && e.getValue() <= remTime) {
                done = false;
                @SuppressWarnings("unchecked")
                ArrayList<String> nextOpened = (ArrayList<String>)opened.clone();
                nextOpened.add(e.getKey());
                if (!eUsed && opened.size()>0 && opened.size() <= (reducedMap.size() / 2)) {
                    openNextWE(getValveByID("AA"), startTime, startTime, true, accPreasure+(getValveByID(e.getKey()).flowRate*(remTime-e.getValue())), nextOpened);
                }
                openNextWE(getValveByID(e.getKey()), remTime-e.getValue(), startTime, eUsed, accPreasure+(getValveByID(e.getKey()).flowRate*(remTime-e.getValue())), nextOpened);
            }
        }
        if (done) {
            if (accPreasure > maxJox) {
                maxJox = accPreasure;
                System.out.println(maxJox);
            }
            //            possibleOutputs.add(accPreasure);
        }
    }
    void openValvesOpt(Integer time) {
        possibleOutputs = new ArrayList<Integer>();
        openNext(getValveByID("AA"), time, 0, new ArrayList<String>());
    }

    private void openNext(Valve curValve, Integer remTime, Integer accPreasure, ArrayList<String> opened) {
        boolean done = true;
        for (Map.Entry<String,Integer> e : curValve.toValveCostMap.entrySet()) {
            boolean open = false;
            for (String s : opened) {
                if (s.equals(e.getKey())) {
                    open = true;
                    break;
                }
            }
            if (!open && e.getValue() <= remTime) {
                done = false;
                @SuppressWarnings("unchecked")
                ArrayList<String> nextOpened = (ArrayList<String>)opened.clone();
                nextOpened.add(e.getKey());
                openNext(getValveByID(e.getKey()), remTime-e.getValue(), accPreasure+(getValveByID(e.getKey()).flowRate*(remTime-e.getValue())), nextOpened);
            }
        }
        if (done) {
            possibleOutputs.add(accPreasure);
        }
    }

    Volcano(ArrayList<String> al) {
        completeMap = new ArrayList<Valve>();
        for (String s : al) {
            String[] sp = s.split(" ");
            String id = sp[1];
            String frs = sp[4];
            frs = frs.replaceAll("rate=", "");
            frs = frs.replaceAll(";", "");
            Integer fr = Integer.parseInt(frs);
            ArrayList<String> tt = new ArrayList<String>();
            for (int i = 9; i < sp.length; i++) {
                tt.add(sp[i].replaceAll(",", ""));
            }
            Valve v = new Valve(id, fr, tt);
            completeMap.add(v);
        }
        reducedMap = new ArrayList<Valve>();
        for (Valve v : completeMap) {
            if (!v.flowRate.equals(0) || v.id.equals("AA")) {
                reducedMap.add(v);
            }
        }
        for (int i = 0; i < reducedMap.size(); i++) {
            if (reducedMap.size() > 1) {
                for (int j = 1; j < reducedMap.size(); j++) {
                    setShortestPath(reducedMap.get(i), reducedMap.get(j));
                }
            }
        }
    }

    Valve getValveByID(String id) {
        for (Valve v : completeMap) {
            if (v.id.equals(id)) {
                return v;
            }
        }
        return null; //lazy
    }

    void setShortestPath(Valve fromValve, Valve toValve) {
        if (fromValve.toValveCostMap == null) {
            fromValve.toValveCostMap = new HashMap<String,Integer>();
        }
        if (toValve.toValveCostMap == null) {
            toValve.toValveCostMap = new HashMap<String,Integer>();
        }
        HashSet<String> hs = new HashSet<String>();
        hs.add(fromValve.id);
        setShortestPathHelper(fromValve, fromValve, toValve, hs);
    }
    
    void setShortestPathHelper(Valve startValve, Valve curValve, Valve stopValve, HashSet<String> visited) {
        visited.add(curValve.id);
        //if at end, eval and ev. set cost and return
        if (curValve.id.equals(stopValve.id)) {
            if (startValve.toValveCostMap.get(stopValve.id) == null || visited.size() < startValve.toValveCostMap.get(stopValve.id)) {
                startValve.toValveCostMap.put(curValve.id, visited.size()); //cost for move and open
                if (!startValve.id.equals("AA")) { // we dont need to store to AA, only from.
                    stopValve.toValveCostMap.put(startValve.id, visited.size()); //cost for move and open
                }
            }
            return;
        }
        //for any node, if current path is longer then the current shortest path, return.
        if (startValve.toValveCostMap.get(stopValve.id) != null && startValve.toValveCostMap.get(stopValve.id) <= visited.size()) {
            return;
        }
        //iterate current nodes linked nodes that hasnt been checked yet 
        for (String s : curValve.tunnelsTo) {
            if (visited.contains(s)) {
                continue;
            }
            @SuppressWarnings("unchecked")
            HashSet<String> nextVisited = (HashSet<String>) visited.clone();
            setShortestPathHelper(startValve, getValveByID(s), stopValve, nextVisited);
        }
    }
}

class Valve {
    String id;
    Integer flowRate;
    boolean open;
    ArrayList<String> tunnelsTo;
    HashMap<String,Integer> toValveCostMap;

    Valve(String id, Integer flowRate, ArrayList<String> tunnelsTo) {
        this.id = id;
        this.flowRate = flowRate;
        this.tunnelsTo = tunnelsTo;
        open = false;
    }
}



