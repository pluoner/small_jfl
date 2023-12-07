package AdventOfCode2022.Dec22;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec22 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec22/res/inptest.txt");
        //pt 1
        Map myMap = new Map(input);
        Position myPos = new Position(myMap, 0, 0, "R");
        myPos.followMapInstructions();
        System.out.println("row: " + myPos.row);
        System.out.println("col: " + myPos.col);
        System.out.println("facing: " + myPos.facing);
        //pt 2

    }
}
class Map {
    ArrayList<ArrayList<String>> map;
    ArrayList<String> instructions;
    Map(ArrayList<String> input) {
        map = new ArrayList<>();
        boolean mapdone = false;
        for (String is : input) {
            if (is.equals("")) {
                mapdone = true;
                continue;
            }
            if (mapdone) {
                instructions = parseInstructions(is);
                return;
            }
            map.add(parseMapRow(is));
        }
    }
    String getTerrainAt(Integer row, Integer col) {
        if (row >= map.size() || col >= map.get(row).size()) {
            return "";
        }
        return map.get(row).get(col);
    }
    private ArrayList<String> parseMapRow(String is) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < is.length(); i++) {
            res.add(is.substring(i, i+1).trim());
        }
        return res;
    }
    private ArrayList<String> parseInstructions(String is) {
        ArrayList<String> res = new ArrayList<>();
        int cs = 0;
        for (int i = 0; i < is.length(); i++) {
            if (is.substring(i, i+1).equals("R") || is.substring(i, i+1).equals("L")) {
                res.add(is.substring(cs, i));
                cs = i + 1;
                res.add(is.substring(i, i+1));
                continue;
            }
            if (i == is.length() - 1) {
                res.add(is.substring(cs, i+1));
            }
        }
        return res;
    }
    public Integer[] getNext(String facing, Integer row, Integer col) {
        Integer initrow = row;
        Integer initcol = col;
        Integer rowmod = 0;
        Integer colmod = 0;
        switch (facing) {
            case "R":
                colmod = 1;
                break;
            case "D":
                rowmod = 1;
                break;
            case "L":
                colmod = -1;
                break;
            case "U":
                rowmod = -1;
                break;
        }
        boolean nextFound = false;
        Integer curRow = row;
        Integer curCol = col;
        while (!nextFound) {
            row += rowmod;
            col += colmod;
            if (row >= map.size()) {
                row = 0;
            } else if (row < 0) {
                row = map.size() - 1;
            }
            if (col >= map.get(row).size()) {
                col = 0;
            } else if (col < 0) {
                col = map.get(row).size() - 1;
            }
            if (row == initrow && col == initcol) {
                nextFound = true;
            } else if (getTerrainAt(row, col).equals("#")) {
                nextFound = true;
            }
        }
        return new Integer[] {curRow, curCol};
    }
}

class Position {
    final Map map;
    Integer row, col;
    String facing;
    Position(Map map, Integer row, Integer col, String facing) {
        this.row = row;
        this.col = col;
        this.map = map;
        this.facing = facing;
    }
    void followMapInstructions() {
        for (String inst : map.instructions) {
            if (inst.equals("R") || inst.equals("L")) {
                turn(inst);
            } else {
                Integer i = Integer.parseInt(inst);
                move(i);
            }
        }
    }
    void move(Integer steps) {
        for (int i = 0; i < steps; i++) {
            move();
        }
    }

    void move() {
        Integer[] next = map.getNext(facing, row, col);
        row = next[0];
        col = next[1];
    }
    void turn(String direction) {
        if (direction.equals("R")) {
            switch (facing) {
                case "R":
                    facing = "D";
                    break;
                case "D":
                    facing = "L";
                    break;
                case "L":
                    facing = "U";
                    break;
                case "U":
                    facing = "R";
                    break;
            }
        } else {
            switch (facing) {
                case "R":
                    facing = "U";
                    break;
                case "D":
                    facing = "R";
                    break;
                case "L":
                    facing = "D";
                    break;
                case "U":
                    facing = "L";
                    break;
            }
        }
    }
}