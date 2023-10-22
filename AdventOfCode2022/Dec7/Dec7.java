package AdventOfCode2022.Dec7;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec7 {
    
    public static void main(String[] args) {
        ArrayList<String> in = Helpers.imp("Dec7/res/input.txt");
        Dir root = new Dir(null, "/");
        Dir cd = null;

        for (String row : in) {
            String[] splitRow = row.split(" ");
            switch (splitRow[0]) {
                case "$" :
                    if ("cd".equals(splitRow[1])) {
                        if ("/".equals(splitRow[2])) {
                            cd = root;
                        } else if ("..".equals(splitRow[2])) {
                            cd = cd.moveUp();
                        } else {
                            cd = cd.openSubDir(splitRow[2]);
                        }
                    }
                    break;
                case "dir" :
                    Dir newDir = new Dir(cd, splitRow[1]);
                    cd.addDir(newDir);
                    break;
                default:
                    cd.newFile(splitRow[1], Integer.parseInt(splitRow[0]));
                    break;
            }
        }
        int j = totDirSizeUnder(root, 100000);
        System.out.println("yo: " + j);
        Dir sos = smallestOverSize(root, root, 5349983);
        System.out.println("plz: " + sos.totDirSize());

    }

    private static int totDirSizeUnder(Dir d, int limit) {
        int accSize = 0;
        if (d.totDirSize() <= limit) {
            accSize += d.totDirSize();
        }
        for (Dir sd : d.subDirs) {
            accSize += totDirSizeUnder(sd, limit);
        }
        return accSize;
    }
    private static Dir smallestOverSize(Dir cd, Dir curCand, int minSize) {        
        if (cd.totDirSize() >= minSize && cd.totDirSize() <= curCand.totDirSize()) {
            curCand = cd;
        }
        for (Dir d : cd.subDirs) {
            if (smallestOverSize(d, curCand, minSize).totDirSize() <= curCand.totDirSize()) {
                curCand = smallestOverSize(d, curCand, minSize);
            }
        }
        return curCand;
    }

}
class Dir {
    private Dir parent;
    private String name;
    private ArrayList<File> files = new ArrayList<>();
    public ArrayList<Dir> subDirs = new ArrayList<>();

    Dir(Dir parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    int totDirSize() {
        int accSize = 0;
        for (File f : files) {
            accSize += f.getSize();
        }
        for (Dir d : subDirs) {
            accSize += d.totDirSize();
        }
        return accSize;
    }

    void newFile(String name, int size) {
        File f = new File(name, size);
        files.add(f);
    }

    void addDir(Dir d) {
        this.subDirs.add(d);
    }

    Dir moveUp() {
        return parent;
    }

    Dir openSubDir(String name) {
        for (Dir d : subDirs) {
            if (d.name.equals(name)) {
                return d;
            }
        }
        Dir d = new Dir(this, name);
        this.subDirs.add(d);
        return d;
    
    }
}

class File {
    @SuppressWarnings("unused")
    private String name;
    private int size;
    
    File(String name, int size) {
        this.name = name;
        this.size = size;
    }
    int getSize() {
        return size;
    }
}
