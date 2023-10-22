package AdventOfCode2022.Dec10;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;


public class Dec10 {
    
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec10/res/input.txt");
        ArrayList<Instruction> inst = new ArrayList<Instruction>();
        ArrayList<Instruction> inst2 = new ArrayList<Instruction>();
        for (String ir : input) {
            String[] sp = ir.split(" ");
            if (sp.length > 1) {
                inst.add(new Instruction(sp[0], sp[1]));
                inst2.add(new Instruction(sp[0], sp[1]));
            } else {
                inst.add(new Instruction(sp[0]));
                inst2.add(new Instruction(sp[0]));
            }
        }
        
        
        CPU device = new CPU(1, inst);
        int ss = device.signalStrength(new int[] {20,40,40,40,40,40});
        System.out.println("Sum of signal Strength at cycles 20/60/100/140/180/220: " + ss);
        
        CPU device2 = new CPU(1, inst2);
        System.out.println();
        device2.drawCRT();
    }
}

class CPU {
    ArrayList<Instruction> instructions;
    int xregD;
    int xreg;
    int cycle;
    int instructionIdx;
    Instruction currentInstruction;

    void drawCRT() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 40; j++) {
                execute();
                if (j <= xregD+1 && j >= xregD-1) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
    int signalStrength(int[] intervals) {
        int signalStrength = 0;
        for (int curint : intervals) {
            execX(curint);
            signalStrength += cycle * xregD;
        }
        return signalStrength;
    }
    void execX (int x) {
        for (int i = 0; i < x; i++) {
            execute();
        }
    }
    void execute() {
        xregD = xreg;
        if (currentInstruction.delay == 0) {
            if (currentInstruction.type.equals("addx")) {
                xreg += currentInstruction.addarg;
            }
            instructionIdx++;
            if (instructionIdx < instructions.size()) { //Last instruction...
                currentInstruction = instructions.get(instructionIdx);
            }
        } else {
            currentInstruction.delay--;
        }
        cycle++;
    }

    CPU(int xreg, ArrayList<Instruction> instructions) {
        this.xreg = xreg;
        this.xregD = xreg;
        this.instructions = instructions;
        this.cycle = 0;
        this.instructionIdx = 0;
        currentInstruction = instructions.get(0);
    }

}
class Instruction {
    String type;
    int addarg;
    int delay;

    Instruction(String type) {
        this.type = type;
        if (type.equals("noop")) {
            delay = 0;
        }
    }

    Instruction(String type, String addarg) {
        this.type = type;
        this.addarg = Integer.parseInt(addarg);
        if (type.equals("addx")) {
            delay = 1;
        }
    }
}
