package AdventOfCode2022.Dec12crap;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;


public class Dec12crap {
    
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec12/res/input.txt");
        Hill.init(input);

        Hill elfHill = new Hill();
        elfHill.traverseHill();
        System.out.println("Min steps needed is: " + Hill.minSteps());
    }
}


class Hill {
    private static ArrayList<ArrayList<Integer>> map;
    private static Pos start, stop;
    @SuppressWarnings("unused")
    private static ArrayList<Pos> minVisited;
    private static Integer minSteps;
    private Pos curPos;
    private ArrayList<Pos> visited;
    private Integer steps;

    static Integer minSteps() {
        return minSteps;
    }
    void printvisited() {
        for (Pos p : visited) {
            char c = (char)(int)height(p);
            System.out.print(c);
        }
        System.out.println();
    }
    void traverseHill() {
        if (atGoal()) {
            if (minSteps == -1 || steps < minSteps) {
                minSteps = steps;
                minVisited = visited;
                System.out.println("Reached the goal in: " + steps);
            } 
            return;
        }
        if (minSteps > -1 && minSteps <= steps) {
            return;
        }
        boolean[] movedDir = new boolean[] {false, false, false, false};
        boolean[] movePrio = movePrioUDLR2();
        boolean[] canMove = canMoveUDLR();

        for (int i = 0; i < movePrio.length ; i++) {
            if (movePrio[i] && canMove[i]) {
                move(i);
                movedDir[i] = true;
            }
        }
        for (int i = 0; i < canMove.length; i++) {
            if (canMove[i] && !movedDir[i]) {
                move(i);
                movedDir[i] = true;
            }
        }
    }
    boolean[] canMoveUDLR() {
        Pos n;
        boolean[] canMoveUDLR = new boolean[] {false, false, false, false};
        n = new Pos(curPos.row-1, curPos.col);
//        if (n.row >= 0 && height(n) <= height(curPos) + 1 && !visited(n)) {
        if (n.row >= 0 && (height(n) == height(curPos) + 1 || height(n) == height(curPos)) && !visited(n)) {
                canMoveUDLR[0] = true;
        }
        n = new Pos(curPos.row+1, curPos.col);
//        if (n.row < map.size() && height(n) <= height(curPos) + 1 && !visited(n)) {
        if (n.row < map.size() && (height(n) == height(curPos) + 1 || height(n) == height(curPos)) && !visited(n)) {
                canMoveUDLR[1] = true;
        }
        n = new Pos(curPos.row, curPos.col-1);
//        if (n.col >= 0 && height(n) <= height(curPos) + 1 && !visited(n)) {
        if (n.col >= 0 && (height(n) == height(curPos) + 1 || height(n) == height(curPos)) && !visited(n)) {
                canMoveUDLR[2] = true;
        }
        n = new Pos(curPos.row, curPos.col+1);
//        if (n.col < map.get(0).size() && height(n) <= height(curPos) + 1 && !visited(n)) {
        if (n.col < map.get(0).size() && (height(n) == height(curPos) + 1 || height(n) == height(curPos)) && !visited(n)) {
                canMoveUDLR[3] = true;
        }
        return canMoveUDLR;

    }
    boolean[] movePrioUDLR(){
        boolean[] prioUDLR = new boolean[] {false,false,false,false};
        if (curPos.row < stop.row) {prioUDLR[0] = true;}
        if (curPos.row > stop.row) {prioUDLR[1] = true;}
        if (curPos.col > stop.col) {prioUDLR[2] = true;}
        if (curPos.col < stop.col) {prioUDLR[3] = true;}
        return prioUDLR;
    }
    boolean[] movePrioUDLR2(){
        Pos n = new Pos(curPos.row, curPos.col);
        boolean[] prioUDLR = new boolean[] {false,false,false,false};
        n.row = curPos.row+1;
        if (height(curPos) == height(n)+1) {prioUDLR[0] = true;}
        n.row = curPos.row-1;
        if (height(curPos) == height(n)+1) {prioUDLR[1] = true;}
        n.row = curPos.row;
        n.col = curPos.col-1;
        if (height(curPos) == height(n)+1) {prioUDLR[2] = true;}
        n.col = curPos.col+1;
        if (height(curPos) == height(n)+1) {prioUDLR[3] = true;}
        return prioUDLR;
    }

    void move(int dir) {
        Pos n;
        switch (dir) {
            case 0: //up
                n = new Pos(curPos.row-1, curPos.col);
                break;
            case 1: //down
                n = new Pos(curPos.row+1, curPos.col);
                break;
            case 2: //left
                n = new Pos(curPos.row, curPos.col-1);
                break;
            default: //right
                n = new Pos(curPos.row, curPos.col+1);
        }
        ArrayList<Pos> v = new ArrayList<Pos>(visited);
        v.add(n);
        Hill nh = new Hill(steps+1, v, new Pos(n.row, n.col));
        nh.traverseHill();
    }

    Hill() {
        steps = 0;
        visited = new ArrayList<Pos>();
        visited.add(start);
        curPos = new Pos(start.row, start.col);
    }
    Hill(Integer steps, ArrayList<Pos> visited, Pos curPos) {
        this.steps = steps;
        this.visited = visited;
        this.curPos = curPos;
    }

    boolean visited(Pos pos) {
        for (Pos p : visited) {
            if (pos.row.equals(p.row) && pos.col.equals(p.col)) {
                return true;
            }
        }
        return false;
    }
    Integer height (Pos p) {
        return map.get(p.row).get(p.col);
    }
    boolean atGoal() {
        return (curPos.row == stop.row && curPos.col == stop.col);
    }

    static void init(ArrayList<String> input) {
        map = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < input.size(); i++) {
            char[] ca = input.get(i).toCharArray();
            ArrayList<Integer> maprow = new ArrayList<Integer>();
            for (int j = 0; j < ca.length; j++) {
                if (ca[j] == 'S') {
                    start = new Pos(i, j);
                    maprow.add(Integer.valueOf('a'));
                    continue;
                }
                if (ca[j] == 'E') {
                    stop = new Pos(i, j);
                    maprow.add(Integer.valueOf('z'));
                    continue;
                }
                maprow.add(Integer.valueOf(ca[j]));
            }
            map.add(maprow);
        }
        minVisited = new ArrayList<Pos>();
        minSteps = -1;
    }
}
class Pos {
    Integer row, col;
    Pos(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }
}

