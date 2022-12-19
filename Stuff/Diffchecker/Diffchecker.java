package Stuff.Diffchecker;

import java.util.ArrayList;

import AdventOfCode2022.Common.*;

public class Diffchecker {

    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("/home/jonathan/Documents/Code/small_jfl/Stuff/Diffchecker/res/core3443diff");
        ArrayList<String> output = new ArrayList<String>();
        for (String ir : input) {
            if (ir.substring(0,1).equals("-")) {
                output.add(ir);
            }
        }
        Helpers.exp("/home/jonathan/Documents/Code/small_jfl/Stuff/Diffchecker/res/core3443diff-results", output);
    }
}
