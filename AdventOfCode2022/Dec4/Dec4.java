package AdventOfCode2022.Dec4;

import java.util.ArrayList;
import AdventOfCode2022.Common.Helpers;

public class Dec4 {

    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("/home/jonathan/Documents/Code/small_jfl/AdventOfCode2022/Dec4/res/input.txt");
        ArrayList<Pairs> pairs = new ArrayList<Pairs>();

        for (String s : input) {
            pairs.add(new Pairs(s));
        }
        int totOverlap = 0;
        int partOverlap = 0;
        for (Pairs p : pairs) {
            if (p.fullOverlap()) {
                totOverlap++;
            }
            if (p.partOverlap()) {
                partOverlap++;
            }
        }
        System.out.println("no. Pairs with full overlap: " + totOverlap);
        System.out.println("no. Pairs with partial overlap: " + partOverlap);
    }

    private static class Pairs {
        int[] secs = new int[4];
        Pairs(String ins) {
            int nextsecidx = 0;
            int prevstart = 0;
            for (int i = 0; i < ins.length(); i++) {
                if (ins.substring(i,i+1).equals("-") || ins.substring(i,i+1).equals(",")) {
                    secs[nextsecidx] = Integer.parseInt(ins.substring(prevstart,i));
                    nextsecidx++;
                    prevstart = i+1;
                }
            }
            secs[nextsecidx] = Integer.parseInt(ins.substring(prevstart,ins.length()));
        }
        private boolean fullOverlap() {
            if (secs[0] <= secs[2] && secs[1] >= secs[3]) {
                return true;
            }
            if (secs[2] <= secs[0] && secs[3] >= secs[1]) {
                return true;
            }
            return false;
        }
        private boolean partOverlap() {
            if ((secs[0] >= secs[2] && secs[0] <= secs[3]) || (secs[1] >= secs[2] && secs[1] <= secs[3] || (secs[0] <= secs[2] && secs[1] >= secs[3]))) {
                return true;
            }
            return false;
        }
    } 
}

