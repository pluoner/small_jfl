package AdventOfCode2022.Dec17take2;

import AdventOfCode2022.Common.Helpers;

import java.util.ArrayList;

public class Dec17take2 {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> input = Helpers.imp("Dec17/res/inptest.txt");

        // Cave crumblingCave = new Cave(input.get(0));
        // crumblingCave.simulateXRocksFalling(2022);
        // System.out.println("Part 1, Height after X stones: " + crumblingCave.getCurrentHeight());
        // System.out.println("");
        Cave crumblingCave2 = new Cave(input.get(0));
        crumblingCave2.simulateManyRocksFalling(1000000000000L);
        System.out.println("Part 2, Height after Y stones: " + crumblingCave2.getCurrentHeight());
    }
}
 
class Cave {
    final static int RWALL = 7;
    Integer rocksLanded;
    ArrayList<Integer> rockFormation;
    String jetStreams;
    ArrayList<BlockType> blockTypes;
    Integer jetIdx, blockIdx, curFallingBlockStartRow;
    Block curFallingBlock;

    ArrayList<Integer> rowFormations = new ArrayList<>();

    Cave(String jetStreams) {
        this.jetStreams = jetStreams;
        this.rocksLanded = 0;
        this.rockFormation = new ArrayList<>();
        this.rockFormation.add(0, 0b1111111); //absolut cave floor
        this.blockTypes = new ArrayList<>();
        this.blockIdx = 0;
        this.jetIdx = 0;
        blockTypes.add(BlockType.VLINE);
        blockTypes.add(BlockType.CROSS);
        blockTypes.add(BlockType.BRCORNER);
        blockTypes.add(BlockType.HLINE);
        blockTypes.add(BlockType.SQUARE);
    }

    public void simulateManyRocksFalling(long i) {
        while (true) {
            simulateXRocksFalling(10000);
            System.out.println(getCurrentHeight());
        }

        // spawnRocksUntilPatternFound();
    }

    Integer getCurrentHeight() {
        return rockFormation.size() - 1;
    }
    Integer simulateXRocksFalling(Integer noRocks) {
        for (int i = 0; i < noRocks; i++) {
            spawnNextBlock();
            moveUntilLanded();
            // System.out.println("");
            // for (int j = rockFormation.size() -1 ; j >= 0 ; j--) {
            //     System.out.println(String.format("%7s", Integer.toBinaryString(rockFormation.get(j))).replace(' ', '0'));
            // }
        }
        return rockFormation.size();
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
        if (testMove("v")) {
            move("v");
            return true;
        }
        Integer blockRow, caveRow;
        for (int i = 0; i < curFallingBlock.blockSpace.size(); i++) {
            blockRow = curFallingBlock.blockSpace.get(i);
            caveRow = 0b0000000;
            if (rockFormation.size() > curFallingBlockStartRow + i) {
                caveRow = rockFormation.get(curFallingBlockStartRow + i);
                caveRow = caveRow | blockRow;
                rockFormation.set(curFallingBlockStartRow + i, caveRow);
                if (rockFormation.size() - 1 == curFallingBlockStartRow + i && i == curFallingBlock.blockSpace.size() && caveRow == 0b1111111) {
                    System.out.println("jooooo");
                }
            } else {
                caveRow = blockRow;
                rockFormation.add(caveRow);
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
        if (dir.equals("<")) {
            for (int i = 0; i < curFallingBlock.blockSpace.size(); i++) {
                curFallingBlock.blockSpace.set(i, curFallingBlock.blockSpace.get(i) << 1);
            }
        }
        if (dir.equals(">")) {
            for (int i = 0; i < curFallingBlock.blockSpace.size(); i++) {
                curFallingBlock.blockSpace.set(i, curFallingBlock.blockSpace.get(i) >> 1);
            }
        }
        if (dir.equals("v")) {
            curFallingBlockStartRow -= 1;
        }
    }

    private Boolean testMove(String dir) {
        Integer blockRow, caveRow;
        //Test left
        if (dir.equals("<")) {
            for (int i = 0; i < curFallingBlock.blockSpace.size(); i++) {
                blockRow = curFallingBlock.blockSpace.get(i);
                if ((blockRow & 0b1000000) > 0) {
                    return false;
                }
                blockRow = blockRow << 1;
                caveRow = 0b0000000;
                if (rockFormation.size() > curFallingBlockStartRow + i) {
                    caveRow = rockFormation.get(curFallingBlockStartRow + i);
                }
                if ((blockRow & caveRow) > 0) {
                    return false;
                }
            }
            return true;
        }
        //Test right
        if (dir.equals(">")) {
            for (int i = 0; i < curFallingBlock.blockSpace.size(); i++) {
                blockRow = curFallingBlock.blockSpace.get(i);
                if ((blockRow & 0b0000001) > 0) {
                    return false;
                }
                blockRow = blockRow >> 1;
                caveRow = 0b0000000;
                if (rockFormation.size() > curFallingBlockStartRow + i) {
                    caveRow = rockFormation.get(curFallingBlockStartRow + i);
                }
                if ((blockRow & caveRow) > 0) {
                    return false;
                }
            }
            return true;
        }
        //Test down
        if (dir.equals("v")) {
            for (int i = 0; i < curFallingBlock.blockSpace.size(); i++) {
                caveRow = 0b0000000;
                if (rockFormation.size() > curFallingBlockStartRow + i -1) {
                    caveRow = rockFormation.get(curFallingBlockStartRow + i - 1);
                }
                blockRow = curFallingBlock.blockSpace.get(i);
                if ((blockRow & caveRow) > 0) {
                    return false;
                }
            }
            return true;
        }
        return null;
    }

    private void spawnNextBlock() {
        curFallingBlock = new Block(blockTypes.get(blockIdx % blockTypes.size()));
        blockIdx++;
        curFallingBlockStartRow = rockFormation.size() + 3;
    }

}

class Block {
    BlockType blockType;
    ArrayList<Integer> blockSpace;

    Block(BlockType bt) {
        blockType = bt;
        blockSpace = new ArrayList<>();
        switch (bt) {
            case VLINE:
                blockSpace.add(0b0011110);
                break;
            case CROSS:
                blockSpace.add(0b0001000);
                blockSpace.add(0b0011100);
                blockSpace.add(0b0001000);
                break;
            case BRCORNER:
                blockSpace.add(0b0011100); //reversed ordering, bottom row comes first, other shapes are vertically symetric
                blockSpace.add(0b0000100);
                blockSpace.add(0b0000100);
                break;
            case HLINE:
                blockSpace.add(0b0010000);
                blockSpace.add(0b0010000);
                blockSpace.add(0b0010000);
                blockSpace.add(0b0010000);
                break;
            case SQUARE:
                blockSpace.add(0b0011000);
                blockSpace.add(0b0011000);
                break;
        }
    }
}

enum BlockType {
    VLINE,
    CROSS,
    BRCORNER,
    HLINE,
    SQUARE
}
