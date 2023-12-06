package AdventOfCode2022.Dec21;

import java.util.ArrayList;
import java.util.HashMap;

import AdventOfCode2022.Common.Helpers;

public class Dec21 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec21/res/input.txt");
        //pt 1
        MonkeyChain mc = new MonkeyChain(input);
        System.out.println("Root monkey yell: " + mc.getMonkey("root").getNumber(false));

        //pt 1
        MonkeyChain mc2 = new MonkeyChain(input);
        System.out.println("Human must yell to satisfy root: " + mc2.getMonkey("root").searchHumnNum(null));

    }
}

class MonkeyChain {
    HashMap<String, Monkey> monkeys = new HashMap<>();

    MonkeyChain(ArrayList<String> inp) {
        for (String s : inp) {
            String[] sa = s.split(":");
            String id = sa[0].trim();
            Long number = null;
            String m1, m2, op;
            m1 = m2 = op = null;
            String[] sa2 = sa[1].replaceFirst(" ", "").split(" ");
            if (sa2.length == 1) {
                number = Long.parseLong(sa2[0]);
            } else {
                m1 = sa2[0].trim();
                op = sa2[1].trim();
                m2 = sa2[2].trim();
            }
            monkeys.put(id, new Monkey(this, id, m1, m2, op, number));
        }
    }

    Monkey getMonkey(String id) {
        return monkeys.get(id);
    }
}

class Monkey {
    private final MonkeyChain mc;
    private final String id, op, refMonkey1, refMonkey2;
    private final Long number;

    Monkey(MonkeyChain mc, String id, String monkey1, String monkey2, String op, Long number) {
        this.mc = mc;
        this.id = id;
        this.refMonkey1 = monkey1;
        this.refMonkey2 = monkey2;
        this.op = op;
        this.number = number;
    }
    Long getNumber(boolean pt2) {
        if (pt2 && this.id.equals("humn")) {
            return null;
        }
        if (this.number != null) {
            return this.number;
        }
        Long rm1num = mc.getMonkey(refMonkey1).getNumber(pt2);
        Long rm2num = mc.getMonkey(refMonkey2).getNumber(pt2);
        if (rm1num == null || rm2num == null) {
            return null;
        }
        Long r = 0L;
        switch (op) {
            case "*":
                r = rm1num * rm2num;
                break;
            case "/":
                r = rm1num / rm2num;
                break;
            case "+":
                r = rm1num + rm2num;
                break;
            case "-":
                r = rm1num - rm2num;
                break;
            default:
                break;
        }
        return r;
    }
    Long searchHumnNum(Long goal) {
        if (id.equals("humn")) {
            return goal;
        }
        Long rm1num = mc.getMonkey(refMonkey1).getNumber(true);
        Long rm2num = mc.getMonkey(refMonkey2).getNumber(true);
        if (goal == null) {
            if (rm1num == null) {
                return mc.getMonkey(refMonkey1).searchHumnNum(rm2num);
            }
            return mc.getMonkey(refMonkey2).searchHumnNum(rm1num);
        }
        Long r = 0L;
        switch (op) {
            case "*": // goal = rm1num * rm2num
                if (rm1num == null) {
                    r = goal / rm2num;
                } else {
                    r = goal / rm1num;
                }
                break;
            case "/": // goal = rm1num / rm2num
                if (rm1num == null) {
                    r = goal * rm2num;
                } else {
                    r = rm1num / goal;
                }
                break;
            case "+": // goal = rm1num + rm2num
                if (rm1num == null) {
                    r = goal - rm2num;
                } else {
                    r = goal - rm1num;
                }
                break;
            case "-": // goal = rm1num - rm2num
                if (rm1num == null) {
                    r = goal + rm2num;
                } else {
                    r = rm1num - goal;
                }
                break;
            default:
                break;
        }
        if (rm1num == null) {
            return mc.getMonkey(refMonkey1).searchHumnNum(r);
        }
        return mc.getMonkey(refMonkey2).searchHumnNum(r);
    }
}