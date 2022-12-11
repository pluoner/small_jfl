package AdventOfCode2022.Common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Helpers {
    public static ArrayList<String> imp(String file) {
        BufferedReader br;
        ArrayList<String> ilist = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(file));
            String l = br.readLine();
            while (l != null) {
                ilist.add(l);
                l = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ilist;
    }
}