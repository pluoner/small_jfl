package AdventOfCode2022.Dec2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dec2 {

    public static void main(String[] args) {
        BufferedReader br;
        ArrayList<Round> rounds = new ArrayList<Round>();
        ArrayList<Round> roundspart2 = new ArrayList<Round>();
        int totp1 = 0, totp2 = 0;
        int totp1part2 = 0, totp2part2 = 0;
        try {
            br = new BufferedReader(
                new FileReader("/home/jg/Documents/Code/small_jfl/AdventOfCode2022/Dec2/lib/input.txt")
            );
            String l = br.readLine();
            while (l != null) {
                if (!l.equals("")) {
                    rounds.add(new Round(l.substring(0,1), l.substring(2,3)));
                    roundspart2.add(new Round(l.substring(0,1), l.substring(2,3), true));
                }
                l = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Round r : rounds) {
            totp1 += r.poip1;
            totp2 += r.poip2;
        }
        for (Round r : roundspart2) {
            totp1part2 += r.poip1;
            totp2part2 += r.poip2;
        }
        System.out.println("p1 scored: " + totp1 + " - p2 scored: " + totp2);
        System.out.println("Part 2: p1 scored: " + totp1part2 + " - p2 scored: " + totp2part2);
    }

    private static class Round {
        private int p1, p2;
        int poip1 = 0, poip2 = 0;

        Round(String p1, String p2) {
            this(p1, p2, false);
        }
        Round(String p1, String p2, boolean part2) {
            if (part2) {
                switch (p1) {
                    case "A":   this.p1 = 0;
                                switch (p2) {
                                    case "X":   this.p2 = 2;
                                                break;
                                    case "Y":   this.p2 = 0;
                                                break;
                                    case "Z":   this.p2 = 1;
                                                break;
                                }
                                break;
                    case "B":   this.p1 = 1;
                                switch (p2) {
                                    case "X":   this.p2 = 0;
                                                break;
                                    case "Y":   this.p2 = 1;
                                                break;
                                    case "Z":   this.p2 = 2;
                                                break;
                                }
                                break;
                    case "C":   this.p1 = 2;
                                switch (p2) {
                                    case "X":   this.p2 = 1;
                                                break;
                                    case "Y":   this.p2 = 2;
                                                break;
                                    case "Z":   this.p2 = 0;
                                                break;
                                }
                                break;
                }
            } else {
                switch (p1) {
                    case "A":   this.p1 = 0;
                                break;
                    case "B":   this.p1 = 1;
                                break;
                    case "C":   this.p1 = 2;
                                break;
                }
                switch (p2) {
                    case "X":   this.p2 = 0;
                                break;
                    case "Y":   this.p2 = 1;
                                break;
                    case "Z":   this.p2 = 2;
                                break;
                }
            }
            roundpoints();
            shapepoints();            
        }

        private void roundpoints() {
            if (p1 == 0 && p2 == 2) {
                poip1 += 6;
            } else if (p1 == 2 && p2 == 0) {
                poip2 += 6;
            } else if (p1 > p2) {
                poip1 += 6;
            } else if (p1 < p2) {
                poip2 += 6;
            } else {
                poip1 += 3;
                poip2 += 3;
            }

        }

        private void shapepoints() {
            poip1 += p1 + 1;
            poip2 += p2 + 1;
        }
    }
}

