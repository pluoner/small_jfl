package AdventOfCode2022.Dec13;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Dec13 {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> input = Helpers.imp("Dec13/res/input.txt");
/*      part 1:
        ArrayList<ListNode[]> listPairs = new ArrayList<ListNode[]>();
        ListNode[] listPair;
        for (int i = 0; i < input.size()+1; i+=3) {
            listPair = new ListNode[2];
            listPair[0] = ListNode.parseInputRow(input.get(i).substring(1, input.get(i).length()-1));
            listPair[1] = ListNode.parseInputRow(input.get(i+1).substring(1, input.get(i+1).length()-1));
            listPairs.add(listPair);
        }
        for (int i = 0; i < listPairs.size(); i++) {
            ListNode.fixLists(listPairs.get(i)[0], listPairs.get(i)[1]);
        }
        int inOrder = 0;
        for (int i = 0; i < listPairs.size(); i++) {
            if (ListNode.compareNodes(listPairs.get(i)[0], listPairs.get(i)[1]) == -1) {
                System.out.println("listPairs " + (i+1) + " are in order.");
                inOrder+= i+1;
            } else {
                System.out.println("listPairs " + (i+1) + " not in order.");
            }
            
        }
        System.out.println("yo: " + inOrder);
        */

        //part 2
        ArrayList<ListNode> allLists = new ArrayList<ListNode>();
        allLists.add(ListNode.parseInputRow("[[2]]"));
        allLists.add(ListNode.parseInputRow("[[6]]"));
        allLists.get(0).ss = "start";
        allLists.get(1).ss = "stop";
        for (int i = 0; i < input.size(); i++) {
            if (!input.get(i).equals("")) {
                ListNode ln = ListNode.parseInputRow(input.get(i).substring(1, input.get(i).length()-1));
                boolean lnadded = false;
                for (int j = 0; j < allLists.size(); j++) {
                    ListNode ilist = (ListNode) ln.clone();
                    ListNode jlist = (ListNode) allLists.get(j).clone();
                    ListNode.fixLists(ilist, jlist);
                    if (ListNode.compareNodes(ilist, jlist) <= 0) {
                        allLists.add(j, ln);
                        lnadded = true;
                        break;
                    }
                }
                if (!lnadded) {
                    allLists.add(ln);
                }
            }
        }
        int sIdx = -1, eIdx = -1;
        for (int i = 0; i < allLists.size(); i++) {
            if (allLists.get(i).ss.equals("start")) {
                sIdx = i+1;
                continue;
            }
            if (allLists.get(i).ss.equals("stop")) {
                eIdx = i+1;
                break;
            }
        }
        System.out.println("part 2: " + (sIdx*eIdx));
    }
}

class ListNode implements Cloneable{
    String ss = "";
    String raw;
    Integer value;
    ArrayList<ListNode> list;

    static ListNode parseInputRow(String s) {
        ListNode lnl = new ListNode(new ArrayList<ListNode>());
        HashMap<Integer, Integer> bm = new HashMap<Integer, Integer>();
        Stack<Integer> bs = new Stack<Integer>();
        for (int i = 0; i < s.length(); i++) {
            if (s.substring(i,i+1).equals("[")) {
                bs.push(i);
            }
            if (s.substring(i,i+1).equals("]")) {
                bm.put(bs.pop(), i);
            }
        }
        int i = 0;
        while (i < s.length()) {
            if (s.substring(i,i+1).equals("[")) {
                lnl.list.add(parseInputRow(s.substring(i+1, bm.get(i))));
                i = bm.get(i)+2;
                if (i >= s.length()) {
                    break;
                }
            }
            if (s.substring(i,i+1).matches("[0-9]")) {
                Integer eIdx = -1;
                if (s.substring(i, s.length()).indexOf(",") == -1) {
                    eIdx = s.length();
                } else {
                    eIdx = i + s.substring(i, s.length()).indexOf(",");
                }
                lnl.list.add(new ListNode(Integer.parseInt(s.substring(i, eIdx))));
                i = eIdx+1;
            }
        }
        return lnl;
    }
    static int compareNodes(ListNode ln1, ListNode ln2) {
        if (ln1.value != null && ln2.value != null) {
            if (ln1.value > ln2.value) {
                return 1;
            }
            if (ln2.value > ln1.value) {
                return -1;
            }
            return 0;
        }
        if (ln1.list == null && ln2.list == null) {
            return 0;
        }
        if (ln1.list == null) {
            return -1;
        }        
        if (ln2.list == null) {
            return 1;
        }
        for (int i = 0; i < ln1.list.size(); i++) {
            if (i >= ln2.list.size()) {
                return 1;
            }
            int cn = compareNodes(ln1.list.get(i), ln2.list.get(i));
            if (cn != 0) {
                return cn;
            }
        }
        if (ln1.list.size() < ln2.list.size()) {
            return -1;
        }
        return 0;
    }

    static void fixLists(ListNode ln1, ListNode ln2) {
        if (ln1.value != null && ln2.value != null) {
            return;
        }
        if (ln1.value != null) {
            ArrayList<ListNode> alln = new ArrayList<ListNode>();
            ListNode nln = new ListNode(ln1.value);
            alln.add(nln);
            ln1.value = null;
            ln1.list = alln;
        }
        if (ln2.value != null) {
            ArrayList<ListNode> alln = new ArrayList<ListNode>();
            ListNode nln = new ListNode(ln2.value);
            alln.add(nln);
            ln2.value = null;
            ln2.list = alln;
        }
        int imax = ln1.list.size();
        if (imax > ln2.list.size()) {
            imax = ln2.list.size();
        }
        for (int i = 0; i < imax; i++) {
            fixLists(ln1.list.get(i), ln2.list.get(i));
        }
    }
    ListNode(Integer value) {
        this.value = value;
    }
    ListNode(ArrayList<ListNode> list) {
        this.list = list;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}



