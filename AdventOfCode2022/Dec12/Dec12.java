package AdventOfCode2022.Dec12;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;
import java.util.HashMap;


public class Dec12 {
    
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec12/res/input.txt");
        Hill elfHill = new Hill(input);
//        elfHill.climbHill();
//        System.out.println("minSteps: " + Hill.minSteps);
        Integer fewestForChar = elfHill.climbHill(elfHill.startpos('a'));
        System.out.println("minSteps: " + fewestForChar);

    }
}


class Hill {
    private static HashMap<Integer, HashMap<Integer, Integer>> map;
    private static Integer mapRows, mapCols;
    private static HashMap<Integer, HashMap<Integer, Integer>> visitedInXSteps;
    private static Pos start, stop;
    static Integer minSteps = -1;

    ArrayList<Pos> startpos(int c) {
        int c2 = c;
        c2++;
        ArrayList<Pos> spos = new ArrayList<Pos>();

        for (int i = 0; i < mapRows; i++) {
            for (int j = 0; j < mapCols; j++) {
                if (map.get(i).get(j).equals(c)) {
                    if ((i > 0 && map.get(i-1).get(j).equals(c2))
                       || (i < mapRows-1 && map.get(i+1).get(j).equals(c2))
                       || (j > 0 && map.get(i).get(j-1).equals(c2))
                       || (j < mapCols-1 && map.get(i).get(j+1).equals(c2)))
                    {
                        Pos sp = new Pos(i, j);
                            spos.add(sp);
                    }
                }
            }
        }
        return spos;
    }
    Integer climbHill(ArrayList<Pos> from) {
        Integer minStepsFrom = -1;

        for (Pos sp : from) {
            minSteps = -1;
            start = sp;
            climbHill();
            if (minStepsFrom == -1 || minStepsFrom > minSteps) {
                minStepsFrom = minSteps;
            }
        }

        return minStepsFrom;
    }
    
    void climbHill() {
        visitedInXSteps = new HashMap<Integer, HashMap<Integer, Integer>>();
        Pos pos = new Pos(start.row, start.col);
        setVisited(pos, 0);
        move(pos, 0);

    }

    void move(Pos p, int s) {
        move(p, s, "U");
        move(p, s, "D");
        move(p, s, "L");
        move(p, s, "R");
    }
    void move(Pos p, int s, String dir) {
        if (atGoal(p)) { // abort if at goal. possibly set minsteps
            if (minSteps > s || minSteps == -1) {
                minSteps = s;
            }
            return;
        }
        Pos next = new Pos(p.row, p.col);
        switch (dir) {
            case "U":
                if (p.row.equals(0)) { //cant move up
                    return;
                }
                next.row--;
                break;
            case "D":
                if (p.row.equals(mapRows - 1)) { //cant move down
                    return;
                }
                next.row++;
                break;
            case "L":
                if (p.col.equals(0)) { //cant move left
                    return;
                }
                next.col--;
                break;
            default: //right
                if (p.col.equals(mapCols - 1)) { //cant move right
                    return;
                }
                next.col++;
                break;
        }
        if (height(next) > height(p) +1) { //cannot move this high in one step 
            return;
        }
        if (getVisited(next) <= s && getVisited(next) != -1) { //allready been here in fewer or equally many steps.
            return;
        }

        //        Hill nextHill = new Hill(next, steps++);
        setVisited(next, s);
        s++;
        move(next, s);
    }
    Hill(ArrayList<String> input) {
        map = new HashMap<Integer, HashMap<Integer, Integer>>();
        for (int i = 0; i < input.size(); i++) {
            char[] ca = input.get(i).toCharArray();
            HashMap<Integer, Integer> maprow = new HashMap<Integer, Integer>();
            for (int j = 0; j < ca.length; j++) {
                if (ca[j] == 'S') {
                    start = new Pos(i, j);
                    maprow.put(j, Integer.valueOf('a'));
                    continue;
                }
                if (ca[j] == 'E') {
                    stop = new Pos(i, j);
                    maprow.put(j, Integer.valueOf('z'));
                    continue;
                }
                maprow.put(j, Integer.valueOf(ca[j]));
            }
            mapCols = ca.length;
            map.put(i, maprow);
        }
        mapRows = input.size();
    }
    Integer height(Pos p) {
        return map.get(p.row).get(p.col);
    }
    Integer getVisited(Pos p) {
        if (visitedInXSteps.get(p.row) != null && visitedInXSteps.get(p.row).get(p.col) != null) {
            return visitedInXSteps.get(p.row).get(p.col);
        }
        return -1;
    }
    boolean atGoal(Pos p) {
        return (p.row.equals(stop.row) && p.col.equals(stop.col));
    }
    void setVisited(Pos p, Integer steps) {
        HashMap<Integer, Integer> hm;
        if (visitedInXSteps.get(p.row) == null) {
            hm = new HashMap<Integer, Integer>();
        } else {
            hm = visitedInXSteps.get(p.row);
        }
        hm.put(p.col, steps);
        visitedInXSteps.put(p.row, hm);
    }
    class Pos {
        Integer row, col;
        Pos(Integer row, Integer col) {
            this.row = row;
            this.col = col;
        }
    }  
}

