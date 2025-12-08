package adventofcode.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Helpers {
    private Helpers() {
        // prevent instantiation
    }

    private static String startPath = System.getProperty(
        "adventofcode.startPath",
        Paths.get(System.getProperty("user.dir"))
             .toAbsolutePath()
             .toString()
             + File.separator + "adventofcode" + File.separator
    );

    public static void setStartPath(String path) {
        if (path != null) {
            startPath = path;
        }
    }

    public static List<String> imp(String file) {
        ArrayList<String> ilist = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(startPath + file))) {
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

    public static void exp(String filepath, List<String> data) {
        try (FileWriter fw = new FileWriter(filepath)) {
            for (String r : data) {
                fw.write(r + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
