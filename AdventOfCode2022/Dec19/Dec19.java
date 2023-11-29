package AdventOfCode2022.Dec19;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec19 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec19/res/inptest.txt");
        RobotFactory rf = new RobotFactory(input);
        rf.calcMaxGeodes();
        for (Blueprint b : rf.blueprints) {
            System.out.println("Blueprint: " + b.id + " can produces " + b.maxNoGeodes + " in 24 minutes. Quality level: " + b.getQualityLevel());
        }
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
    void calcMaxGeodes() {
        for (Blueprint b : blueprints) {
            b.calcMaxGeodes(24);
        }
    }
}
class Blueprint {
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
    
    void calcMaxGeodes(Integer time) {
        Integer[] workers = new Integer[] {1,0,0,0};
        Integer[] resources = new Integer[] {0,0,0,0};
        boolean[] posObjectives = posibleObjectives(workers);
        for (int i = 0; i < 4; i++) {
            if (posObjectives[i]) {
                calcMaxGeodes(time, workers, resources, i);
            }
        }
    }

    private void calcMaxGeodes(Integer time, Integer[] workers, Integer[] resources, Integer objective) {
        Integer inProduction = null;
        if (objCritOk(objective, workers)) {
            inProduction = objective;
            objective = null;
        }
        time--;
        for (int i = 0; i < workers.length; i++) {
            resources[i]+= workers[i];
        }
        if (time == 0) {
            if (resources[3] > maxNoGeodes) {
                maxNoGeodes = resources[3];
            }
        } else if (inProduction == null) {
            calcMaxGeodes(time, workers, resources, objective);
        } else {
            workers[inProduction]++;
            boolean[] posObjectives = posibleObjectives(workers);
            for (int i = 0; i < 4; i++) {
                if (posObjectives[i]) {
                    calcMaxGeodes(time, workers, resources, i);
                }
            }
        }
    }

    private boolean objCritOk(Integer objective, Integer[] workers) {
        for (int i = 0; i < 4; i++) {
            if (objective == 0) {
                if (oreRobotCost[i] != 0 && workers[i] < 1) {
                    return false;
                }
            }
            if (objective == 1) {
                if (clayRobotCost[i] != 0 && workers[i] < 1) {
                    return false;
                }
            }
            if (objective == 2) {
                if (obsidianRobotCost[i] != 0 && workers[i] < 1) {
                    return false;
                }
            }
            if (objective == 3) {
                if (geodeRobotCost[i] != 0 && workers[i] < 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean[] posibleObjectives(Integer[] workers) {
        boolean[] objectives = new boolean[] {true,true,true,true};
        for (int i = 0; i < 4; i++) {
            if (oreRobotCost[i] > 0 && workers[0] < 1) {
                objectives[0] = false;
            }
            if (clayRobotCost[i] > 0 && workers[1] < 1) {
                objectives[1] = false;
            }
            if (obsidianRobotCost[i] > 0 && workers[2] < 1) {
                objectives[2] = false;
            }
            if (geodeRobotCost[i] > 0 && workers[3] < 1) {
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
