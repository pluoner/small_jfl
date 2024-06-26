package AdventOfCode2022.Dec19;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

import AdventOfCode2022.Common.Helpers;

public class Dec19 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec19/res/input.txt");
        RobotFactory rf = new RobotFactory(input);
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;
        // rf.calcMaxGeodes(false);
        // for (Blueprint b : rf.blueprints) {
        //     System.out.println("Blueprint: " + b.id + " can produces " + b.maxNoGeodes + " in 24 minutes. Quality level: " + b.getQualityLevel());
        // }
        // System.out.println("Summed quality level: " + rf.qualityLevelSum());
        rf.calcMaxGeodes(true);
        elapsedTime = (new Date()).getTime() - startTime;
        for (int i = 0; i < 3; i++) {
            System.out.println("Blueprint: " + rf.blueprints.get(i).id + " can produces " + rf.blueprints.get(i).maxNoGeodes + " in 32 minutes.");
        }
        System.out.println("multiplied max geodes of first three: " + rf.multfirst3() + " - Ran in: " + elapsedTime);
    }
}

class RobotFactory {
    ArrayList<Blueprint> blueprints;
    RobotFactory(ArrayList<String> inputBluePrints) {
        blueprints = new ArrayList<>();
        for (String s : inputBluePrints) {
            blueprints.add(new Blueprint(s));
        }
    }
    void calcMaxGeodes(boolean pt2) {
        if (pt2) {
            for (int i = 0; i < 3; i++) {
                blueprints.get(i).calcMaxGeodes(32);
            }
        } else {
            for (Blueprint b : blueprints) {
                b.calcMaxGeodes(24);
            }
        }
    }
    Integer qualityLevelSum() {
        Integer sum = 0;
        for (Blueprint b : blueprints) {
            sum += b.getQualityLevel();
        }
        return sum;
    }
    Integer multfirst3() {
        return blueprints.get(0).maxNoGeodes * blueprints.get(1).maxNoGeodes * blueprints.get(2).maxNoGeodes; 
    }
}
class Blueprint {
    HashMap<String,Integer> checkedStates;
    int id, maxNoGeodes;
    int[] oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost;
    Blueprint(String s) {
        String[] sa = s.split(":");
        id = Integer.parseInt(sa[0].replaceAll("Blueprint ", ""));
        String[] rc = sa[1].split("\\.");
        oreRobotCost = parseRobotCost(rc[0]);
        clayRobotCost = parseRobotCost(rc[1]);
        obsidianRobotCost = parseRobotCost(rc[2]);
        geodeRobotCost = parseRobotCost(rc[3]);
    }

    int maxSpend(int resource) {
        int ms = 0;
        ms += oreRobotCost[resource];
        ms += clayRobotCost[resource];
        ms += obsidianRobotCost[resource];
        ms += geodeRobotCost[resource];
        return ms;
    }

    void calcMaxGeodes(int time) {
        checkedStates = new HashMap<>();
        int[] workers = new int[] {1,0,0,0};
        boolean[] posObjectives = posibleObjectives(workers);
        for (int i = 0; i < 4; i++) {
            if (posObjectives[i]) {
                calcMaxGeodes(time, new int[] {1,0,0,0}, new int[] {0,0,0,0}, i);
            }
        }
    }

    private void calcMaxGeodes(int time, int[] workers, int[] resources, int objective) {
        String k = "";
        for (int i = 0; i < 4; i++) {
            k = k + workers[i] + ";" + resources[i] + ";";
        }
        Integer t = checkedStates.get(k);
        if (t != null && t < time) {
            return;
        } else {
            checkedStates.put(k, time);
        }
        for (int i = 0; i < 3; i++) {
            if (resources[i] >= 35) { // arbitrary exit if to many resources has been collected.
                return;
            }
        }
        int inProduction = -1;
        if (objCritOk(objective, resources)) {
            resources = payRobotCost(objective, resources);
            inProduction = objective;
            objective = -1;
        }
        time--;
        for (int i = 0; i < workers.length; i++) {
            resources[i]+= workers[i];
        }
        if (time == 0) {
            if (resources[3] > maxNoGeodes) {
                maxNoGeodes = resources[3];
            }
        } else if (inProduction == -1) {
            calcMaxGeodes(time, workers, resources, objective);
        } else {
            workers[inProduction]++;
            boolean[] posObjectives = posibleObjectives(workers);
            for (int i = 0; i < 4; i++) {
                if (posObjectives[i]) {
                    int[] w = workers.clone();
                    int[] r = resources.clone();
                    calcMaxGeodes(time, w, r, i);
                }
            }
        }
    }

    private int[] payRobotCost(int objective, int[] resources) {
        for (int i = 0; i < 4; i++) {
            if (objective == 0) {
                resources[i] -= oreRobotCost[i];
            }
            if (objective == 1) {
                resources[i] -= clayRobotCost[i];
            }
            if (objective == 2) {
                resources[i] -= obsidianRobotCost[i];
            }
            if (objective == 3) {
                resources[i] -= geodeRobotCost[i];
            }
        }
        return resources;
    }

    private boolean objCritOk(int objective, int[] resources) {
        for (int i = 0; i < 4; i++) {
            if (objective == 0) {
                if (oreRobotCost[i] > resources[i]) {
                    return false;
                }
            }
            if (objective == 1) {
                if (clayRobotCost[i] > resources[i]) {
                    return false;
                }
            }
            if (objective == 2) {
                if (obsidianRobotCost[i] > resources[i]) {
                    return false;
                }
            }
            if (objective == 3) {
                if (geodeRobotCost[i] > resources[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean[] posibleObjectives(int[] workers) {
        boolean[] objectives = new boolean[] {true,true,true,true};
        for (int i = 0; i < 4; i++) {
            if (oreRobotCost[i] > 0 && workers[i] < 1 || maxSpend(0) <= workers[0]) {
                objectives[0] = false;
            }
            if (clayRobotCost[i] > 0 && workers[i] < 1 || maxSpend(1) <= workers[0]) {
                objectives[1] = false;
            }
            if (obsidianRobotCost[i] > 0 && workers[i] < 1 || maxSpend(2) <= workers[2]) {
                objectives[2] = false;
            }
            if (geodeRobotCost[i] > 0 && workers[i] < 1) {
                objectives[3] = false;
            }
        }
        return objectives;
    }

    int getQualityLevel() {
        return id * maxNoGeodes;
    }

    private int[] parseRobotCost(String costRow) {
        int[] ia = new int[4]; //can never cost geodes but adding a slot for that anyway.
        int st = -1;
        for (int i = 0; i < costRow.length(); i++) {
            if (Character.isDigit(costRow.charAt(i))) {
                if (st == -1) {
                    st = i;
                }
             } else if (i > 0 && Character.isDigit(costRow.charAt(i-1))) {
                int cost = Integer.parseInt(costRow.substring(st, i));
                switch(costRow.substring(i + 1, i + 3)) {
                    case "or":
                        ia[0] = cost;
                        break;
                    case "cl":
                        ia[1] = cost;
                        break;
                    case "ob":
                        ia[2] = cost;
                        break;
                }
                st = -1;
                i = i + 3;
            }
        }
        return ia;
    }
};
