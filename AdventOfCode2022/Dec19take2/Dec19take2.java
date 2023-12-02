package AdventOfCode2022.Dec19take2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import AdventOfCode2022.Common.Helpers;

public class Dec19take2 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec19take2/res/input.txt");
        RobotFactory rf = new RobotFactory(input);
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0L;
        rf.calcMaxGeodes(false);
        for (Blueprint b : rf.blueprints) {
            System.out.println("Blueprint: " + b.id + " can produces " + b.maxNoGeodes + " in 24 minutes. Quality level: " + b.getQualityLevel());
        }
        System.out.println("Summed quality level: " + rf.qualityLevelSum());
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
    int id, maxNoGeodes;
    int[][] robotCosts = new int[4][3];
    Blueprint(String s) {
        String[] sa = s.split(":");
        id = Integer.parseInt(sa[0].replaceAll("Blueprint ", ""));
        String[] rc = sa[1].split("\\.");
        robotCosts[0] = parseRobotCost(rc[0]);
        robotCosts[1] = parseRobotCost(rc[1]);
        robotCosts[2] = parseRobotCost(rc[2]);
        robotCosts[3] = parseRobotCost(rc[3]);
    }

    int maxSpend(int resource) {
        int ms = 0;
        ms += robotCosts[0][resource];
        ms += robotCosts[1][resource];
        ms += robotCosts[2][resource];
        ms += robotCosts[3][resource];
        return ms;
    }

    void calcMaxGeodes(int time) {
        int[] workers = new int[] {1,0,0,0};
        boolean[] posObjectives = possibleObjectives(workers);
        for (int i = 3; i >= 0; i--) {
            if (posObjectives[i]) {
                calcMaxGeodes(time, new int[] {1,0,0,0}, new int[] {0,0,0,0}, i);
            }
        }
    }

    private void calcMaxGeodes(int time, int[] workers, int[] resources, int objective) {
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
        if (time != 0 && maxNoGeodes >= theoreticalMaxNoGeodes(time, workers, resources)) {
            return;
        }
        if (time == 0) {
            if (resources[3] > maxNoGeodes) {
                maxNoGeodes = resources[3];
            }
        } else if (inProduction == -1) {
            calcMaxGeodes(time, workers, resources, objective);
        } else {
            workers[inProduction]++;
            boolean[] posObjectives = possibleObjectives(workers);
            for (int i = 3; i >= 0; i--) {
                if (posObjectives[i]) {
                    int[] w = workers.clone();
                    int[] r = resources.clone();
                    calcMaxGeodes(time, w, r, i);
                }
            }
        }
    }

    private int theoreticalMaxNoGeodes(int time, int[] workers, int[] resources) {
        int c = resources[3];
        c += workers[3] * time;
        for (int i = time; i > 0; i--) {
            c += i;
        }
        return c;
    }

    private int[] payRobotCost(int objective, int[] resources) {
        for (int i = 0; i < 3; i++) {
            resources[i] -= robotCosts[objective][i];
        }
        return resources;
    }

    private boolean objCritOk(int objective, int[] resources) {
        for (int i = 0; i < 3; i++) {
            if (robotCosts[objective][i] > resources[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean[] possibleObjectives(int[] workers) {
        boolean[] objectives = new boolean[] {true,true,true,true};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (robotCosts[j][i] > 0 && workers[i] < 1) {
                    objectives[j] = false;
                }
                if (j < 3 && maxSpend(0) <= workers[j]) {
                    objectives[j] = false;
                }
            }
        }
        return objectives;
    }

    int getQualityLevel() {
        return id * maxNoGeodes;
    }

    private int[] parseRobotCost(String costRow) {
        int[] ia = new int[3];
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
