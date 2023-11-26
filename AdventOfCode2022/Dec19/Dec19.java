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
        for (String s : inputBluePrints) {
            blueprints.add(new Blueprint(s));
        }
    }
}
class Blueprint {
    int id;
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
    private int[] parseRobotCost(String costRow) {
        int[] ia = new int[4];
        return ia;
    }
};
