package AdventOfCode2022.Dec11;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;
import java.util.Collections;


public class Dec11 {
    
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("/home/jonathan/Documents/Code/small_jfl/AdventOfCode2022/Dec11/res/input.txt");

        parseMonkeys(input);
        for (int i = 0; i < 10000; i++) {
            Monkey.round();
        }
        for (Monkey m : Monkey.monkeypack) {
            System.out.println("Monkey" + m.monkeyId + " has throwed a total of: " + m.throwCounter + " items");
        }
        System.out.println();
        System.out.println("The total level of monkey business now at: " + Monkey.levelOfMonkeyBusiness());
        
    }

    static void parseMonkeys(ArrayList<String> input) {
        ArrayList<Monkey> parsedMonkeys = new ArrayList<Monkey>();
        Integer mid = null, mtest = null, mtrue = null, mfalse = null;
        ArrayList<Integer> mitems = null;
        String[] mop = null;
        for (String ir : input) {
            String[] sp = ir.split(" ");
            if (sp[0].equals("Monkey")) {
                mid = Integer.parseInt(sp[1].substring(0,sp[1].indexOf(":")));
            }
            if (sp.length > 2) {
                if (sp[2].equals("Starting")) {
                    mitems = new ArrayList<Integer>();
                    String irmod = ir.substring(18, ir.length());
                    String[] sp2 = irmod.split(", ");
                    for (String s : sp2) {
                        mitems.add(Integer.parseInt(s));
                    }
                }
                if (sp[2].equals("Operation:")) {
                    mop = new String[] {sp[5], sp[6], sp[7]};
                    //5:old, 6:op, 7:19
                }
                if (sp[2].equals("Test:")) {
                    mtest = Integer.parseInt(sp[5]);
                }
                if (sp.length > 5) {
                    if (sp[5].equals("true:")) {
                        mtrue = Integer.parseInt(sp[9]);
                        //monkeytoid at 9
                    }
                    if (sp[5].equals("false:")) {
                        mfalse = Integer.parseInt(sp[9]);
                        //last, create new monkey and add to list.
                        Monkey nm = new Monkey(mid, mitems, mop, mtest, mtrue, mfalse);
                        parsedMonkeys.add(nm);
                    }
                }

            }

        }
    }
}

class Monkey {
    static boolean noStressRelife = true;
    static Integer commonDenom = 1;
    static ArrayList<Monkey> monkeypack = new ArrayList<Monkey>();
    Integer monkeyId, testInt, throwToifTrue, throwToifFalse, throwCounter;
    ArrayList<Integer> items;
    String[] operation;
    
    Monkey(Integer monkeyId, ArrayList<Integer> items, String[] worryLevelOp, Integer testInt, Integer monkeytrue, Integer monkeyfalse) {
        this.monkeyId = monkeyId;
        this.items = items;
        this.operation = worryLevelOp;
        this.testInt = testInt;
        this.throwToifTrue = monkeytrue;
        this.throwToifFalse = monkeyfalse;
        this.throwCounter = 0;
        monkeypack.add(this);
        commonDenom*= testInt;
    }

    static Long levelOfMonkeyBusiness() {
        ArrayList<Integer> al = new ArrayList<Integer>();
        for (Monkey m : monkeypack) {
            al.add(m.throwCounter);
        }
        Collections.sort(al);
        if (al.size() > 1) {
            return Long.valueOf(al.get(al.size()-1)) * Long.valueOf(al.get(al.size()-2));
        }
        return Long.valueOf(al.get(0));
    }
    static void round() {
        for (Monkey m : monkeypack) {
            m.turn();
        }
    }

    void turn() {
        while (items.size() > 0) {
            Monkey toMonkey = monkeypack.get(nextMonkeyToThrowTo());
            throwToMonkey(toMonkey);
            this.throwCounter++;
        }
    }

    void throwToMonkey(Monkey toMonkey) {
        toMonkey.items.add(this.items.remove(0));
    }

    Integer nextMonkeyToThrowTo() {
        Integer curitem = items.get(0);
        curitem = nextWorryLevel(curitem);
        items.set(0,curitem);
        if (curitem % testInt == 0) {
            return throwToifTrue;
        }
        return throwToifFalse;
    }

    Integer nextWorryLevel(Integer item) {
        Integer arg1, arg2;
        if (operation[0].equals("old")) {
            arg1 = item;
        } else {
            arg1 = Integer.parseInt(operation[0]);
        }
        if (operation[2].equals("old")) {
            arg2 = item;
        } else {
            arg2 = Integer.parseInt(operation[2]);
        }
        Long nextWL;
        if (operation[1].equals("*")) {
            nextWL = Long.valueOf(arg1) * Long.valueOf(arg2);

        } else {
            nextWL = Long.valueOf(arg1) + Long.valueOf(arg2);

        }
        if (noStressRelife) {
            if (nextWL > commonDenom) {
                nextWL = nextWL % Long.valueOf(commonDenom);
                return nextWL.intValue();
            }
            return nextWL.intValue();
        }
        return nextWL.intValue() / 3;
    }

}
