package AdventOfCode2023.Common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Helpers {
    static String startPath = "/home/jg/Documents/Code/small_jfl/AdventOfCode2023/";
    public static ArrayList<String> imp(String file) {
        BufferedReader br;
        ArrayList<String> ilist = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(startPath + file));
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

    public static void exp(String filepath, ArrayList<String> data) {
        try {
            FileWriter fw = new FileWriter(filepath);
            for (String r : data) {
                fw.write(r + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
