package AdventOfCode2022.Dec9;

import java.util.ArrayList;
import java.util.HashSet;

import AdventOfCode2022.Common.Helpers;

public class Dec9 {
    
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("/home/jonathan/Documents/Code/small_jfl/AdventOfCode2022/Dec9/res/input.txt");
        ArrayList<MoveInst> moveInst = new ArrayList<MoveInst>();
        for (String ir : input) {
            MoveInst mi = new MoveInst(ir.substring(0,1), Integer.parseInt(ir.substring(2,ir.length())));
            moveInst.add(mi);
        }

        Rope simpleRope = new Rope(0, 0, 1);
        Rope myRope = new Rope(0, 0, 9);
        for (MoveInst mi : moveInst) {
            simpleRope.move(mi);
        }
        for (MoveInst mi : moveInst) {
            myRope.move(mi);
        }
        System.out.println("part 1: " + simpleRope.tailVisited.get(0).size());
        System.out.println("part 2: " + myRope.tailVisited.get(8).size());
    }
}

class Rope {
    int[] headPos;
    int[][] tailPos;
    ArrayList<HashSet<String>> tailVisited;

    Rope (int x, int y, int knots) {
        headPos = new int[2];
        tailPos = new int[knots][2];
        headPos[0] = x;
        headPos[1] = y;
        tailVisited = new ArrayList<HashSet<String>>();
        for (int i = 0; i < knots; i++) {
            tailPos[i][0] = x;
            tailPos[i][1] = y;
            HashSet<String> hs = new HashSet<String>();
            hs.add("X0Y0");
            tailVisited.add(i, hs);
        }
    }

    void prtStt(int prtsz) {
        for (int i = -prtsz; i < prtsz; i++) {
            for (int j = -prtsz; j < prtsz; j++) {
                boolean found = false;
                if (headPos[0] == j && headPos[1] == -i) {
                    System.out.print("H");
                    found = true;
                }
                if (!found) {
                    for (int tp = 0; tp < tailPos.length; tp++) {
                        if (tailPos[tp][0] == j && tailPos[tp][1] == -i) {
                            System.out.print(tp+1);
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("---");
    }

    void move(MoveInst mi) {
        for (int i = 0; i < mi.getLength(); i++) {
            moveHead(mi.getDir());
            moveTail();
//            prtStt(20);
        }
    }
    void moveHead(String dir) {
        switch (dir) {
            case "U":
                headPos[1] = headPos[1]+1;
                break;
            case "D":
                headPos[1] = headPos[1]-1;
                break;
            case "R":
                headPos[0] = headPos[0]+1;
                break;
            case "L":
                headPos[0] = headPos[0]-1;
                break;
        }
    }
    void moveTail() {
        for (int tp = 0; tp < tailPos.length; tp++) {
            int hx; 
            int hy;
            if (tp == 0) {
                hx = headPos[0];
                hy = headPos[1];
            } else {
                hx = tailPos[tp-1][0];
                hy = tailPos[tp-1][1];
            }
            int tx = tailPos[tp][0];
            int ty = tailPos[tp][1];
            int ntx = tx;
            int nty = ty;
            if (hx-tx > 1 || hx-tx < -1) {
                if (hx-tx > 1) {
                    ntx = hx-1;
                } else {
                    ntx = hx+1;
                }
                if (hy-ty > 0) {
                    nty=ty+1;
                } else if(hy-ty < 0) {
                    nty=ty-1;
                }
            }
            if (hy-ty > 1 || hy-ty < -1) {
                if (hy-ty > 1) {
                    nty = hy-1;
                } else {
                    nty = hy+1;
                }
                if (hx-tx > 0) {
                    ntx=tx+1;
                } else if(hx-tx < 0) {
                    ntx=tx-1;
                }
            }
            tailPos[tp][0] = ntx;
            tailPos[tp][1] = nty;
            tailVisited.get(tp).add("X" + ntx + "Y" + nty);
        }
    }
}
class MoveInst {
    private String dir;
    private int length;

    MoveInst(String dir, int length) {
        this.dir = dir;
        this.length = length;
    }

    String getDir() {
        return dir;
    }
    int getLength() {
        return length;
    }
}
