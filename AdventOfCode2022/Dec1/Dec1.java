package AdventOfCode2022.Dec1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Dec1 {

    public static void main(String[] args) {
        BufferedReader br;
        int elf = 1;
        int calCarried = 0;
        HashMap<Integer, Integer> inv = new HashMap<Integer, Integer>();

        try {
            br = new BufferedReader(
                new FileReader("/home/jonathan/Documents/Code/small_jfl/AdventOfCode2022/Dec1/lib/input.txt")
            );
            String l = br.readLine();
            while (l != null) {
                if (l.equals("")) {
                    inv.put(elf, calCarried);
                    elf++;
                    calCarried = 0;
                } else {
                    try {
                        calCarried += Integer.parseInt(l);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                l = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Map.Entry<Integer, Integer>> linv = new ArrayList<Map.Entry<Integer, Integer>>(inv.entrySet());
        Collections.sort(
            linv,
            new Comparator<Map.Entry<Integer, Integer>>() {
                public int compare(Map.Entry<Integer, Integer> a, Map.Entry<Integer, Integer> b) {
                    return Integer.compare(b.getValue(), a.getValue());
                }
            }
        );
        
        int top1 = 0;
        int sumtop3 = 0;
        for (int i = 0; i < linv.size() && i < 3; i++) {
            if (i == 0) {
                top1 += linv.get(i).getValue();
            }
            sumtop3 += linv.get(i).getValue();
        }
        System.out.println("Sum of calories for the elf king: " + top1);
        System.out.println("Sum of calories of the top 3 elfs: " + sumtop3);
    }
}
