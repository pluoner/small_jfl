package AdventOfCode2022.Dec6;

import java.util.ArrayList;
import java.util.HashSet;

import AdventOfCode2022.Common.Helpers;

public class Dec6 {

    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec6/res/input.txt");

        String data = input.get(0);

        for (int i = 4; i < data.length(); i++) {
            if (allUnique(data.substring(i-4,i))) {
                System.out.println("Unique 4-char sequence occured at: " + i);
                break;
            }
        }

        for (int i = 14; i < data.length(); i++) {
            if (allUnique(data.substring(i-14,i))) {
                System.out.println("Unique 14-char sequence occured at: " + i);
                break;
            }
        }

    }

    private static boolean allUnique(String s) {
        HashSet<String> checked = new HashSet<String>();
        for (int i = 0; i < s.length(); i++) {
            String cur = s.substring(i,i+1);
            if (checked.contains(cur)) {
                return false;
            }
            checked.add(cur);
        }
        return true;
    }
}

