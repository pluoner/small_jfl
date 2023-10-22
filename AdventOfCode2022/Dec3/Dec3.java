package AdventOfCode2022.Dec3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Dec3 {

    public static void main(String[] args) {
        BufferedReader br;
        ArrayList<Sack> sacks = new ArrayList<Sack>();
        int totcont = 0;
        try {
            br = new BufferedReader(
                new FileReader("/home/jg/Documents/Code/small_jfl/AdventOfCode2022/Dec3/lib/input.txt")
            );
            String l = br.readLine();
            while (l != null) {
                if (!l.equals("")) {
                    sacks.add(new Sack(l));
                }
                l = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Sack s : sacks) {
            totcont += s.ccval;
        }
        System.out.println("Total contents of common item in all sacks: " + totcont);

        //Part 2
        int sumP2 = 0;
        for (int g = 0; g < sacks.size(); g+=3) {
            HashSet<String> checked = new HashSet<String>();
            String cs1 = sacks.get(g).cont;
            for (int i = 0; i < cs1.length(); i++) {
                String cur = cs1.substring(i,i+1);
                if (!checked.contains(cs1.substring(i,i+1))) {
                    if (sacks.get(g+1).itemInSack(cur) && sacks.get(g+2).itemInSack(cur)) {
                        int cc = cur.charAt(0);
                        if (cc > 90) {
                            sumP2 += cc - 96; 
                        } else {
                            sumP2 += cc - 38;
                        }
                        break;
                    }
                    checked.add(cur);
                }
            }
        }
        System.out.println("Total for part 2: " + sumP2);
    }

    private static class Sack {
        private String cont;
        private char cc;
        private int ccval;
        Sack(String s) {
            cont = s;
            int comp2ind = s.length() / 2;
            for (int i = 0; i < comp2ind; i++) {
                for (int j = comp2ind; j < s.length(); j++)
                if (cont.substring(i, i+1).equals(cont.substring(j,j+1))) {
                    cc = cont.charAt(i);
                    break;
                }
            }
            if (cc > 90) {
                ccval = cc - 96; 
            } else {
                ccval = cc - 38;
            }
        }

        private boolean itemInSack(String s) {
            if (cont.indexOf(s) == -1) {
                return false;
            }
            return true;
        }
    }
}

