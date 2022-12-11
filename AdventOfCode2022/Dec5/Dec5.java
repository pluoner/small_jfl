package AdventOfCode2022.Dec5;

import java.util.ArrayList;
import java.util.Stack;

import AdventOfCode2022.Common.Helpers;

public class Dec5 {

    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("/home/jonathan/Documents/Code/small_jfl/AdventOfCode2022/Dec5/res/input.txt");

        CratePiles cp = new CratePiles(input);
        CratePiles cp2 = new CratePiles(input, true);

        for (Stack<String> p : cp.piles) {
            System.out.print(p.peek());
        }
        System.out.println();

        for (Stack<String> p : cp2.piles) {
            System.out.print(p.peek());
        }
        System.out.println();
    }

    private static class CratePiles {
        ArrayList<Stack<String>> piles = new ArrayList<Stack<String>>();
        ArrayList<int[]> inst = new ArrayList<int[]>();
        int iSidx = 0, pSidx = 0;

        CratePiles(ArrayList<String> in) {
            this(in, false);
        }
        CratePiles(ArrayList<String> in, boolean v9001) {
            findSidxs(in);
            parsePiles(in);
            parseInst(in);
            if (v9001) {
                execInst9001();
                return;
            }
            execInst();
        }
        private void execInst9001() {
            for (int inst[] : inst) {
                int repeat = inst[0];
                int from = inst[1];
                int to = inst[2];
                move9001(from-1,to-1,repeat);
            }
        }
        private void move9001(int fromidx, int toidx, int repeat) {
            Stack<String> swap = new Stack<String>();
            Stack<String> fp = piles.get(fromidx);
            Stack<String> tp = piles.get(toidx);
            for (int i = 0; i < repeat; i++) {
                swap.push(fp.pop());
            }
            for (int i = 0; i < repeat; i++) {
                tp.push(swap.pop());
            }
        }
        private void execInst() {
            for (int inst[] : inst) {
                int repeat = inst[0];
                int from = inst[1];
                int to = inst[2];

                for (int i = 0; i < repeat; i++) {
                    move(from-1, to-1);
                }
            }
        }
        private void move(int fromidx, int toidx) {
            Stack<String> fp = piles.get(fromidx);
            Stack<String> tp = piles.get(toidx);
            tp.push(fp.pop());
        }
        private void parseInst(ArrayList<String> in) {
            for (int i = iSidx; i < in.size(); i++) {
                int[] parIns = new int[3]; 
                String curIns = in.get(i);
                curIns = curIns.replaceAll("move ", "").replaceAll("from ", "").replaceAll("to ", "");

                int neidx = curIns.indexOf(" ");
                parIns[0] = Integer.parseInt(curIns.substring(0,neidx));
                curIns = curIns.substring(neidx+1, curIns.length());

                neidx = curIns.indexOf(" ");
                parIns[1] = Integer.parseInt(curIns.substring(0,neidx));
                curIns = curIns.substring(neidx+1, curIns.length());

                parIns[2] = Integer.parseInt(curIns);
                inst.add(parIns);
            }
        }
        private void parsePiles(ArrayList<String> in) {
            int pRowEnd = in.get(0).length();
            for (int i = pSidx; i >= 0; i--) {
                int curPile = 0;
                for (int j = 1; j < pRowEnd; j+=4) {
                    Stack<String> cs;
                    if (!in.get(i).substring(j,j+1).equals(" ")) {
                        if (i == pSidx) {
                            cs = new Stack<String>();
                            piles.add(curPile, cs);
                        } else {
                            cs = piles.get(curPile);
                        }
                        cs.push(in.get(i).substring(j,j+1));
                    }
                    curPile++;
                }
            }
        }

        private void findSidxs(ArrayList<String> in) {
            for (int i = 0; i < in.size(); i++) {
                if (in.get(i).equals("")) {
                    pSidx = i-2;
                    iSidx = i+1;
                    break;
                }
            }
        }
    }
}

