package AdventOfCode2022.Dec20take2;

import java.util.ArrayList;

import AdventOfCode2022.Common.Helpers;

public class Dec20take2 {
    public static void main(String[] args) {
        ArrayList<String> input = Helpers.imp("Dec20take2/res/input.txt");
        //pt 1
        encryptedFile pt1 = new encryptedFile(input, null);
        pt1.mixListX(1);
        System.out.println("Part 1, Groove coordinates: " + pt1.getGrooveCoordinates());

        //pt 2
        int decryptKey = 811589153;
        encryptedFile pt2 = new encryptedFile(input, decryptKey);
        pt2.mixListX(10);
        System.out.println("Part 2, Groove coordinates: " + pt2.getGrooveCoordinates());

    }
}

class encryptedFile {
    private final Integer decryptKey;
    private Integer mixCounter, orgZeroIx;
    private final ArrayList<listNode> orgList;
    private ArrayList<listNode> curList;

    encryptedFile(ArrayList<String> input, Integer decryptKey) {
        if (decryptKey == null) {
            this.decryptKey = 1;
        } else {
            this.decryptKey = decryptKey;
        }
        mixCounter = 0;
        orgList = new ArrayList<>();
        curList = new ArrayList<>();

        int is = input.size();
        long nVal;
        for (int i = 0; i < input.size(); i++) {
            nVal = Integer.parseInt(input.get(i));
            if (nVal == 0) {
                this.orgZeroIx = i;
            }
            nVal *= this.decryptKey;
            listNode ln = new listNode(i, nVal, is);
            orgList.add(ln);
            curList.add(ln);
        }
    }

    public Long getGrooveCoordinates() {
        Long s = 0L;
        Integer cIx = orgList.get(orgZeroIx).getCurIx();
        for (int i = 0; i < 3; i++) {
            cIx += 1000;
            cIx = cIx % curList.size();
            s += curList.get(cIx).getVal();
        }
        return s;
    }
    public void mixListX(Integer noMix) {
        for (int i = 0; i < noMix; i++) {
            mixList();
        }
    }
    private void mixList() {
        for (listNode ln : orgList) {
            move(ln.getCurIx());
        }
        mixCounter++;
    }
    private void move(Integer ix) {
        listNode cln = curList.get(ix);
        curList.remove(cln);
        Integer nidx = ix + cln.getModVal();
        if (nidx >= curList.size() || -nidx >= curList.size()) {
            nidx = nidx % curList.size();
        }
        if (nidx < 0) {
            nidx += curList.size();
        }
        cln.setCurIx(nidx);
        curList.add(nidx, cln);
        if (nidx < ix) {
            for (int i = nidx + 1; i <= ix; i++) {
                curList.get(i).incCurIx();
            }
        } else if (ix < nidx) {
            for (int i = ix; i < nidx; i++) {
                curList.get(i).decCurIx();
            }
        }
    }
}
class listNode {
    private final Long val;
    private final Integer orgIx, modVal;
    private Integer curIx;

    listNode(Integer Ix, Long val, Integer listLen) {
        this.orgIx = Ix;
        this.curIx = Ix;
        this.val = val;
        this.modVal = Long.valueOf(val % (listLen -1)).intValue(); // -1 as the no. moves for one full rotation is one less than list size.
    }
    public Integer getCurIx() {
        return curIx;
    }
    public Integer getModVal() {
        return modVal;
    }
    public Integer getOrgIx() {
        return orgIx;
    }
    public Long getVal() {
        return val;
    }
    public void setCurIx(Integer curIx) {
        this.curIx = curIx;
    }
    public void incCurIx() {
        this.curIx++;
    }
    public void decCurIx() {
        this.curIx--;
    }
}