package AdventOfCode2022.Dec17;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Dec17 {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> input = Helpers.imp("Dec17/res/inptest.txt");

        Cave crumblingCave = new Cave(input.get(0));
        Cave.setWalls(0, 7);
        crumblingCave.simulateXRocksFalling(2022);
        System.out.println("Part 1, Height after X stones: " + crumblingCave.curHeight);

    }
}
 
class Cave {
    static Integer LWALL, RWALL;
    Integer rocksLanded;
    Integer curHeight;
    HashMap<Integer, HashSet<Integer>> rocksFormationRowCol;
    String jetStreams;
    ArrayList<BlockType> blockTypes;
    Integer jetIdx, blockIdx;
    Block curFallingBlock;

    static void setWalls(Integer lwall, Integer rwall) {
        Cave.LWALL = lwall;
        Cave.RWALL = rwall;
    }

    Integer simulateXRocksFalling(Integer noRocks) {
        for (int i = 0; i < noRocks; i++) {
            spawnNextBlock();
            moveUntilLanded();
        }
        return curHeight;
    }

    private void moveUntilLanded() {
        boolean falling = true;
        while (falling) {
            jetMove();
            jetIdx++;
            falling = fallMove();
        }
    }

    private boolean fallMove() {
        if (testMove("down")) {
            move("down");
            return true;
        }
        for (BlockSpace bs : curFallingBlock.blockSpace) {
            int c = bs.col;
            int r = bs.row;
            if (r+1 > curHeight) {
                curHeight = r+1;
            }
            if (rocksFormationRowCol.get(r) == null) {
                HashSet<Integer> hs = new HashSet<>();
                hs.add(c);
                rocksFormationRowCol.put(r, hs);
            } else {
                rocksFormationRowCol.get(r).add(c);
            }
        }
        rocksLanded++;
        return false;

    }

    private void jetMove() {
        int idx = jetIdx % jetStreams.length();
        String dir = jetStreams.substring(idx,idx+1);
        if (testMove(dir)) {
            move(dir);
        }
    }

    private void move(String dir) {
        if (dir.equals("<") || dir.equals(">")) {
            Integer colMod = 1;
            if (dir.equals("<")) {
                colMod = -1;
            }
            for (BlockSpace bs : curFallingBlock.blockSpace) {
                bs.col += colMod;
            }
            return;
        }
        if (dir.equals("down")) {
            for (BlockSpace bs : curFallingBlock.blockSpace) {
                bs.row -= 1;
            }
        }
    }

    private boolean testMove(String dir) {
        if (dir.equals("<") || dir.equals(">")) {
            Integer colMod = 1;
            if (dir.equals("<")) {
                colMod = -1;
            }
            for (BlockSpace bs : curFallingBlock.blockSpace) {
                int c = bs.col + colMod;
                int r = bs.row;
                if (c < LWALL || c >= RWALL) {
                    return false;
                }
                if (rocksFormationRowCol.get(r) != null && rocksFormationRowCol.get(r).contains(c)) {
                    return false;
                }
            }
        }
        if (dir.equals("down")) {
            for (BlockSpace bs : curFallingBlock.blockSpace) {
                int c = bs.col;
                int r = bs.row - 1;
                if (r < 0) {
                    return false;
                }
                if (rocksFormationRowCol.get(r) != null && rocksFormationRowCol.get(r).contains(c)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void spawnNextBlock() {
        curFallingBlock = new Block(blockTypes.get(blockIdx % blockTypes.size()));
        curFallingBlock.adjustInitPos(curHeight+3, 2);
        blockIdx++;
    }

    Cave(String jetStreams) {
        this.jetStreams = jetStreams;
        this.rocksLanded = 0;
        this.curHeight = 0;
        this.rocksFormationRowCol = new HashMap<>();
        this.blockTypes = new ArrayList<>();
        this.blockIdx = 0;
        this.jetIdx = 0;
        blockTypes.add(BlockType.VLINE);
        blockTypes.add(BlockType.CROSS);
        blockTypes.add(BlockType.BRCORNER);
        blockTypes.add(BlockType.HLINE);
        blockTypes.add(BlockType.SQUARE);
    }
}

class Block {
    BlockType blockType;
    ArrayList<BlockSpace> blockSpace;
    Integer maxRow, maxCol, curRow, curCol;

    void adjustInitPos(Integer initRow, Integer initCol) {
        for (BlockSpace bs : blockSpace) {
            bs.col += initCol;
            bs.row += initRow;
        }
    }

    Block(BlockType bt) {
        blockType = bt;
        blockSpace = new ArrayList<>();
        switch (bt) {
            case VLINE:
            blockSpace.add(new BlockSpace(0,0));
            blockSpace.add(new BlockSpace(0,1));
            blockSpace.add(new BlockSpace(0,2));
            blockSpace.add(new BlockSpace(0,3));
            maxRow = 0;
            maxCol = 3;
            break;
            case CROSS:
            blockSpace.add(new BlockSpace(2,1));
            blockSpace.add(new BlockSpace(1,0));
            blockSpace.add(new BlockSpace(1,1));
            blockSpace.add(new BlockSpace(1,2));
            blockSpace.add(new BlockSpace(0,1));
            maxRow = 2;
            maxCol = 2;
            break;
            case BRCORNER:
            blockSpace.add(new BlockSpace(2,2));
            blockSpace.add(new BlockSpace(1,2));
            blockSpace.add(new BlockSpace(0,0));
            blockSpace.add(new BlockSpace(0,1));
            blockSpace.add(new BlockSpace(0,2));
            maxRow = 2;
            maxCol = 2;
            break;
            case HLINE:
            blockSpace.add(new BlockSpace(3,0));
            blockSpace.add(new BlockSpace(2,0));
            blockSpace.add(new BlockSpace(1,0));
            blockSpace.add(new BlockSpace(0,0));
            maxRow = 3;
            maxCol = 0;
            break;
            case SQUARE:
            blockSpace.add(new BlockSpace(1,0));
            blockSpace.add(new BlockSpace(1,1));
            blockSpace.add(new BlockSpace(0,0));
            blockSpace.add(new BlockSpace(0,1));
            maxRow = 1;
            maxCol = 1;
            break;
        }
    }
}

class BlockSpace {
    Integer row, col;
    BlockSpace(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }
}

enum BlockType {
    VLINE,
    CROSS,
    BRCORNER,
    HLINE,
    SQUARE
}
