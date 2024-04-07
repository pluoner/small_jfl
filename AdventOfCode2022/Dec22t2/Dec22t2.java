package AdventOfCode2022.Dec22t2;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec22t2 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec22t2/res/inptest.txt");
        //pt 1

        //pt 2

    }
}

class Map {
    ArrayList<Terrain[]> map;
    ArrayList<String> instructions;

    Map(ArrayList<String> input) {
        instructions = parseInstructions(input.get(input.size()-1));
        input.remove(input.size()-1);
        int maxw = 0;
        for (String s : input) {
            if (s.length() > maxw) {
                maxw = s.length();
            }
        }
        map = parseMap(input, maxw);
    }

    private ArrayList<Terrain[]> parseMap(ArrayList<String> input, int maxw) {
        ArrayList<Terrain[]> res = new ArrayList<>();
        for (String s : input) {
            Terrain[] row = new Terrain[maxw];
            for (int i = 0; i < maxw; i++) {
                // if (i >)
            }
        }
        return null;
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
}

enum Terrain {
    VOID,
    BLOCK,
    CLEAR
}