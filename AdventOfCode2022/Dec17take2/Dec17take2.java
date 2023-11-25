package AdventOfCode2022.Dec17take2;

import AdventOfCode2022.Common.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dec17take2 {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> input = Helpers.imp("Dec17/res/input.txt");

        // Cave crumblingCave = new Cave(input.get(0));
        // crumblingCave.simulateXRocksFalling(2022);
        // System.out.println("Part 1, Height after X stones: " + crumblingCave.curHeight);
        // System.out.println("");
        Cave crumblingCave2 = new Cave(input.get(0));
        crumblingCave2.simulateManyRocksFalling(1000000000000L);
    }
}
 
class Cave {
    Long rocksLanded, curHeight;
    ArrayList<Integer> rockFormation;
    String jetStreams;
    ArrayList<BlockType> blockTypes;
    Integer jetIdx, blockIdx, curFallingBlockStartRow;
    Block curFallingBlock;
    HashMap<stateKey,stateValues> savedStates;

    Cave(String jetStreams) {
        this.jetStreams = jetStreams;
        this.rocksLanded = 0L;
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

    public void simulateManyRocksFalling(long noRocks) {
        savedStates = new HashMap<>();
        Boolean patternFound = false;
        stateKey tsk = null;
        stateValues tsv = null;
        while (!patternFound) {
            spawnNextBlock();
            moveUntilLanded();
            if (curHeight < 10) {
                continue;
            }
            ArrayList<Integer> l10 = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                l10.add(rockFormation.get(curHeight.intValue() - 9 + i));
            }
            stateKey sk = new stateKey(jetIdx % jetStreams.length(), blockIdx % blockTypes.size(), l10);
            stateValues sv = new stateValues(rocksLanded, curHeight);
            patternFound = savedStates.containsKey(sk);
            if (!patternFound) {
                savedStates.put(sk,sv);
            }
            tsk = sk;
            tsv = sv;
        }
        Long drock = tsv.rocksLanded() - savedStates.get(tsk).rocksLanded();
        Long dheight = tsv.curHeight() - savedStates.get(tsk).curHeight();
        Long mult = (noRocks - curHeight) / drock;
        Long calcRocks = drock * mult;
        Long calcHeight = dheight * mult;
        Long remStones = noRocks - rocksLanded - calcRocks;
        for (Long j = 0L; j < remStones ; j++) {
            spawnNextBlock();
            moveUntilLanded();
        }
        System.out.println("Total no stones: " + (calcRocks + rocksLanded));
        System.out.println("Total height: " + (calcHeight + curHeight));
    }

    Integer simulateXRocksFalling(Integer noRocks) {
        for (int i = 0; i < noRocks; i++) {
            spawnNextBlock();
            moveUntilLanded();
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
            } else {
                caveRow = blockRow;
                rockFormation.add(caveRow);
            }
        }
        curHeight = Long.valueOf(rockFormation.size() - 1);
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

record stateKey(Integer jetIdx, Integer blockIdx, ArrayList<Integer> last10) {
}

record stateValues(Long rocksLanded, Long curHeight) {}

enum BlockType {
    VLINE,
    CROSS,
    BRCORNER,
    HLINE,
    SQUARE
}
