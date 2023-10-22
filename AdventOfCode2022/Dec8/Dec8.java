package AdventOfCode2022.Dec8;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec8 {
    public static ArrayList<ArrayList<Integer>> forest;
    
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec8/res/input.txt");
        ArrayList<ArrayList<Integer>> forest = new ArrayList<ArrayList<Integer>>();

        for (String ir : input) {
            ArrayList<Integer> row = new ArrayList<Integer>();
            for (int i = 0; i < ir.length(); i++) {
                row.add(Integer.parseInt(ir.substring(i,i+1)));
            }
            forest.add(row);
        }
        System.out.println("visible: " + countVisible(forest));
        System.out.println("Highest scenic score: " + highestScenicScore(forest));
    }

    private static int highestScenicScore(ArrayList<ArrayList<Integer>> forest) {
        int curCand = 0;
        for (int i = 0; i < forest.size(); i++) {
            for (int j = 0; j < forest.get(i).size(); j++) {
                int curTree = scenScore(forest, "N", i, j) * scenScore(forest, "S", i, j) * scenScore(forest, "W", i, j) * scenScore(forest, "E", i, j);
                if (curTree > curCand) {
                    curCand = curTree;
                }
            }
        }
        return curCand;
    }

    private static int scenScore(ArrayList<ArrayList<Integer>> forest, String dir, int r, int c) {
        int curh = forest.get(r).get(c);
        int curScenScore = 0;
        switch (dir) {
            case "N":
                for (int i = r-1; i >= 0; i--) {
                    curScenScore++;
                    if (forest.get(i).get(c) >= curh) {
                        break;
                    }
                }
                break;
            case "S":
                for (int i = r+1; i < forest.size(); i++) {
                    curScenScore++;
                    if (forest.get(i).get(c) >= curh) {
                        break;
                    }
                }
                break;
            case "W":
                for (int i = c-1; i >= 0; i--) {
                    curScenScore++;
                    if (forest.get(r).get(i) >= curh) {
                        break;
                    }
                }
                break;
            case "E":
                for (int i = c+1; i < forest.get(r).size(); i++) {
                    curScenScore++;
                    if (forest.get(r).get(i) >= curh) {
                        break;
                    }
                }
                break;
            }
        return curScenScore;
    }

    private static int countVisible(ArrayList<ArrayList<Integer>> forest) {
        int v = 0;
        for (int i = 0; i < forest.size(); i++) {
            for (int j = 0; j < forest.get(i).size(); j++) {
                if (checkVisible(forest, i, j)) {
                    v++;
                }
            }
        }
        return v;
    }

    private static boolean checkVisible(ArrayList<ArrayList<Integer>> forest, int row, int col) {
        return (checkdist(forest, "N", row, col) || checkdist(forest, "S", row, col) || checkdist(forest, "W", row, col) || checkdist(forest, "E", row, col));
    }

    private static boolean checkdist(ArrayList<ArrayList<Integer>> forest, String dir, int r, int c) {
        int curh = forest.get(r).get(c);
        switch (dir) {
            case "N":
                for (int i = 0; i < r; i++) {
                    if (forest.get(i).get(c) >= curh) {
                        return false;
                    }
                }
                break;
            case "S":
                for (int i = r+1; i < forest.size(); i++) {
                    if (forest.get(i).get(c) >= curh) {
                        return false;
                    }
                }
                break;
            case "W":
                for (int i = 0; i < c; i++) {
                    if (forest.get(r).get(i) >= curh) {
                        return false;
                    }
                }
                break;
            case "E":
                for (int i = c+1; i < forest.get(r).size(); i++) {
                    if (forest.get(r).get(i) >= curh) {
                        return false;
                    }
                }
                break;
            }
        return true;
    }
}
