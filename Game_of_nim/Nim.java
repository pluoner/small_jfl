package Game_of_nim;
import java.util.*;

class Nim {
    static HashMap<Integer,HashMap<Integer,Integer>> losepos = new HashMap<Integer,HashMap<Integer,Integer>>();
    static ArrayList<int[]> upairs = new ArrayList<>();
    static int max_val = 1;
    int[] startpos;

    Nim(int[] start) {
        startpos = start;
        if (start[2] > max_val) {
            gen_upair(max_val, start[2]);
            gen_lose_pos();
            max_val = start[2];
        }
    }

    private void gen_upair(int from, int to) {
        for (int i = 1; i <= from; i++) { //fill up existing entries 
            for (int j = from + 1; j<= to; j++) {
                int[] curPair = new int[2];
                curPair[0] = i;
                curPair[1] = j;
                upairs.add(curPair);
            }
        }
        for (int i = from + 1; i <= to; i++) { //create new unique entries
            for (int j = i + 1; j<= to; j++) {
                int[] curPair = new int[2];
                curPair[0] = i;
                curPair[1] = j;
                upairs.add(curPair);
            }
        }
    }

    private void gen_lose_pos() {
        for (int i = 0; i < upairs.size(); i++) {
            int[] cl = upairs.get(i);
            int x = cl[0];
            int y = cl[1];
            for (int j = i + 1; j < upairs.size(); j++) {
                if (upairs.get(j)[0] == x) {
                    int z = upairs.get(j)[1];
                    losepos.get(x);
                }
            }
        }
    }
}
