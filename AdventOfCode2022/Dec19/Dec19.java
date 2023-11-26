package AdventOfCode2022.Dec19;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec19 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec19/res/inptest.txt");
        RobotFactory rf = new RobotFactory(input);
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
}
class Blueprint {
    private int id, maxNoGeodes;
    private int[] oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost;
    Blueprint(String s) {
        String[] sa = s.split(":");
        id = Integer.parseInt(sa[0].replaceAll("Blueprint ", ""));
        String[] rc = sa[1].split("\\.");
        oreRobotCost = parseRobotCost(rc[0]);
        clayRobotCost = parseRobotCost(rc[1]);
        obsidianRobotCost = parseRobotCost(rc[2]);
        geodeRobotCost = parseRobotCost(rc[3]);
    }
    
    void calcQualityLevel() {
        
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
