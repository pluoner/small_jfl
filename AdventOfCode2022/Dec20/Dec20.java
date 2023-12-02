package AdventOfCode2022.Dec20;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec20 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec20/res/input.txt");
        ArrayList<Integer> encryptedFile = new ArrayList<>();
        for (String s : input) {
            encryptedFile.add(Integer.parseInt(s));
        }
        mixList(encryptedFile);
        int zeroIx = 0;
        for (int i = 0; i < encryptedFile.size(); i++) {
            if (encryptedFile.get(i) == 0) {
                zeroIx = i;
                break;
            }
        }
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += encryptedFile.get((zeroIx + (1000 * (i + 1))) % encryptedFile.size()); //todo
        }
        System.out.println("Sum of values at 1000, 2000 and 3000 is: " + sum);
    }

    private static void mixList(ArrayList<Integer> encryptedFile) {
        ArrayList<Integer> movedIdx = new ArrayList<>();
        int curVal = 0;
        int nidx = 0;
        for (int idx = 0; idx < encryptedFile.size(); idx++) {
            if (movedIdx.contains(idx)) {
                continue;
            }
            curVal = encryptedFile.get(idx);
            nidx = idx + curVal % encryptedFile.size();
            if (nidx < 0) {
                nidx += encryptedFile.size();
            }
            if (nidx > encryptedFile.size()) {
                nidx = nidx % encryptedFile.size();
            }
            encryptedFile.remove(idx);
            encryptedFile.add(nidx, curVal);
            for (int ix : movedIdx) {
                if (nidx >= ix) {
                    ix--;
                }
            }
            movedIdx.add(nidx);
            if (nidx > idx) {
                idx--; // to counter loop iteration to next idx
            }
        }
    }
}

