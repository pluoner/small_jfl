package AdventOfCode2022.Dec14;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;
import java.util.HashMap;

public class Dec14 {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> input = Helpers.imp("/home/jonathan/Documents/Code/small_jfl/AdventOfCode2022/Dec14/res/input.txt");

        Cave elfCaveWithAbyss = new Cave();
        elfCaveWithAbyss.parseInput(input);
        elfCaveWithAbyss.fillCave();
        System.out.println("part 1 - grains of sand: " + elfCaveWithAbyss.grainsOfSand);

        Cave elfCaveWithFloor = new Cave();
        elfCaveWithFloor.parseInput(input);
        elfCaveWithFloor.addFloor();
        elfCaveWithFloor.fillCave();
        System.out.println("part 2 - grains of sand: " + elfCaveWithFloor.grainsOfSand);
    }
}

class Cave {
    Integer[] sandEntry;
    Integer grainsOfSand;
    Integer timeElapsed;
    Integer lastRow;
    HashMap<Integer, HashMap<Integer, Character>> colRowMap;
    Integer grainCol,grainRow;
    String grainStat;
    boolean caveFull;

    void fillCave() {
        setLastRow();
        caveFull = false;
        grainStat = "fall";
        grainCol = sandEntry[0];
        grainRow = sandEntry[1];
        while (caveFull == false && grainStat.equals("fall")) {
            moveSand();
        }
    }
    void addFloor() {
        setLastRow();
        int floorRow = lastRow + 2;
        //487,45 -> 487,38
        int floorColFrom = 500 - floorRow;
        int floorColTo = 500 + floorRow;
        lastRow = floorRow+1;
        String sf = new String(floorColFrom + "," + floorRow + " -> " + floorColTo + "," + floorRow);
        parseInputRow(sf);
    }

    private void moveSand() {
        if (grainRow > lastRow) { //into the abyss
            grainStat = "abyss";
        }
        if (get(grainCol, grainRow+1) == 'A') {
            grainRow+=1;
        } else if (get(grainCol-1, grainRow+1) == 'A') {
            grainRow+=1;
            grainCol-=1;
        } else if (get(grainCol+1, grainRow+1) == 'A') {
            grainRow+=1;
            grainCol+=1;
        } else {
            putSandToRest();
        }
    }
    private void putSandToRest() {
        set(grainCol, grainRow, 'S');
        grainsOfSand++;
        if (grainCol == sandEntry[0] && grainRow == sandEntry[1]) {
            caveFull = true;
        }
        grainCol = sandEntry[0];
        grainRow = sandEntry[1];
    }
    void setLastRow() {
        lastRow = 0;
        for (HashMap<Integer,Character> hm : colRowMap.values()) {
            for (Integer row : hm.keySet()) {
                if (row > lastRow) {
                    lastRow = row;
                }
            }
        }

    }

    void parseInput(ArrayList<String> inp) {
        for (String s : inp) {
            parseInputRow(s);
        }
    }
    void parseInputRow(String s) {
        s = s.replaceAll(" ", "");
        String[] sp = s.split("->");
        ArrayList<Integer[]> posMap = new ArrayList<Integer[]>();
        for (String t : sp) {
            String[] xy = t.split(",");
            Integer[] colrow = new Integer[2];
            colrow[0] = Integer.parseInt(xy[0]);
            colrow[1] = Integer.parseInt(xy[1]);
            posMap.add(colrow);
        }
        buildCave(posMap);
    }
    private void buildCave(ArrayList<Integer[]> posMap) {
        for (int i = 1; i < posMap.size(); i++) {
            Integer[] fromColRow = posMap.get(i-1);
            Integer[] toColRow = posMap.get(i);
            if (fromColRow[0].equals(toColRow[0])) {
                Integer col = fromColRow[0];
                if (fromColRow[1] < toColRow[1]) {
                    //down
                    for (int j = fromColRow[1]; j <= toColRow[1]; j++) {
                        set(col,j, 'R');
                    }
                } else {
                    //up
                    for (int j = fromColRow[1]; j >= toColRow[1]; j--) {
                        set(col,j, 'R');
                    }
                }
            } else {
                Integer row = fromColRow[1];
                if (fromColRow[0] < toColRow[0]) {
                    //right
                    for (int j = fromColRow[0]; j <= toColRow[0]; j++) {
                        set(j,row, 'R');
                    }
                } else {
                    //left
                    for (int j = fromColRow[0]; j >= toColRow[0]; j--) {
                        set(j,row, 'R');
                    }
                }
            }
        }
    }
    void set(Integer col, Integer row, Character material) {
        if (colRowMap.get(col) == null) {
            HashMap<Integer, Character> rowhs = new HashMap<Integer, Character>();
            colRowMap.put(col, rowhs);
        }
        colRowMap.get(col).put(row, material);
    }

    char get(Integer col, Integer row) {
        if (colRowMap.get(col) == null || colRowMap.get(col).get(row) == null) {
            return 'A';
        }
        return colRowMap.get(col).get(row);
    }

    Cave() {
        sandEntry = new Integer[]{500,0};
        grainsOfSand = 0;
        timeElapsed = 0;
        colRowMap = new HashMap<Integer, HashMap<Integer, Character>>();
    }
}



