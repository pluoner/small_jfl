package AdventOfCode2022.Dec20;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec20 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec20/res/input.txt");
        ArrayList<Integer[]> encryptedFile = new ArrayList<>();
        for (String s : input) {
            encryptedFile.add(new Integer[] {Integer.parseInt(s), 0, null});
        }
        //part 1:
        // mixList(encryptedFile);
        // int zeroIx = 0;
        // for (int i = 0; i < encryptedFile.size(); i++) {
        //     if (encryptedFile.get(i)[0] == 0) {
        //         zeroIx = i;
        //         break;
        //     }
        // }
        // int sum = 0;
        // for (int i = 0; i < 3; i++) {
        //     sum += encryptedFile.get((zeroIx + (1000 * (i + 1))) % encryptedFile.size())[0];
        // }
        // System.out.println();
        // System.out.println("Sum of values at 1000, 2000 and 3000 is: " + sum);

        //part 2:
        int decryptKey = 811589153;
        for (Integer[] ia : encryptedFile) {
            ia[0] *= decryptKey;
        }
        for (int i = 0; i < 10; i++) {
            mixList(encryptedFile);
        }
    }

    private static void mixList(ArrayList<Integer[]> encryptedFile) {
        int curVal = 0;
        int nidx = 0;
        for (int idx = 0; idx < encryptedFile.size();) {
            if (encryptedFile.get(idx)[1] == 1) {
                idx++;
                continue;
            }
            curVal = encryptedFile.get(idx)[0];
            encryptedFile.remove(idx);
            nidx = idx + curVal;
            if (nidx >= encryptedFile.size() || -nidx >= encryptedFile.size()) {
                nidx = nidx % encryptedFile.size();
            }
            if (nidx < 0) {
                nidx += encryptedFile.size();
            }
            encryptedFile.add(nidx, new Integer[] {curVal, 1});
            if (nidx <= idx) {
                idx++;
            }
        }
    }
}

