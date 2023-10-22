package AdventOfCode2022.Dec15;

import AdventOfCode2022.Common.Helpers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class Dec15 {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList<String> input = Helpers.imp("Dec15/res/input.txt");

        SensorZone sz = new SensorZone();
        sz.parseInput(input);
        System.out.println("part 1: " + sz.coveredAtYamt(2000000));
        System.out.println("part 2: " + sz.posBeaconPos(4000000));
    }
}

class SensorZone {
    ArrayList<SensorBeacon> zoneSBlist;
    SensorZone() {
        zoneSBlist = new ArrayList<SensorBeacon>();
    }

    Long posBeaconPos(Integer upperBound) {
        for (int i = 0; i < upperBound; i++) {
            ArrayList<Integer[]> coveredPerBeakon = coveredAtY(i);
            Collections.sort(coveredPerBeakon, new Comparator<Integer[]>() {
                public int compare(Integer[] ia, Integer[] ib) {
                    return ia[0].compareTo(ib[0]);
                }
            });
            ArrayList<Integer[]> coveredTot = new ArrayList<Integer[]>();
            coveredTot.add(coveredPerBeakon.get(0));
            for (int j = 1; j < coveredPerBeakon.size(); j++) {
                if (coveredPerBeakon.get(j)[0] <= coveredTot.get(coveredTot.size()-1)[1]) {
                    if (coveredPerBeakon.get(j)[1] > coveredTot.get(coveredTot.size()-1)[1]) {
                        coveredTot.get(coveredTot.size()-1)[1] = coveredPerBeakon.get(j)[1];
                    }
                    continue;
                }
                coveredTot.add(coveredPerBeakon.get(j));
            }
            if (coveredTot.size() > 1) {
                return (Long.valueOf(coveredTot.get(0)[1]) * Long.valueOf(4000000)) + Long.valueOf(i);
            }
        }
        return Long.valueOf(-1);
    }

    Integer coveredAtYamt(Integer y) {
        ArrayList<Integer[]> coveredPerBeakon = coveredAtY(y);
        Collections.sort(coveredPerBeakon, new Comparator<Integer[]>() {
            public int compare(Integer[] ia, Integer[] ib) {
                return ia[0].compareTo(ib[0]);
            }
        });
        ArrayList<Integer[]> coveredTot = new ArrayList<Integer[]>();
        coveredTot.add(coveredPerBeakon.get(0));
        for (int i = 1; i < coveredPerBeakon.size(); i++) {
            if (coveredPerBeakon.get(i)[0] <= coveredTot.get(coveredTot.size()-1)[1]) {
                if (coveredPerBeakon.get(i)[1] > coveredTot.get(coveredTot.size()-1)[1]) {
                    coveredTot.get(coveredTot.size()-1)[1] = coveredPerBeakon.get(i)[1];
                }
                continue;
            }
            coveredTot.add(coveredPerBeakon.get(i));
        }
        Integer totY = 0;
        for (Integer[] mm : coveredTot) {
            HashSet<Integer> hsb = new HashSet<Integer>();
            for (SensorBeacon sb : zoneSBlist) {
                if (sb.beaconXY[1].equals(y) && sb.beaconXY[0] >= mm[0] && sb.beaconXY[0] <= mm[1]) {
                    hsb.add(sb.beaconXY[0]);
                }
            }
            totY-= hsb.size();
            totY += mm[1] - mm[0];
        }
        return totY;
    }

    ArrayList<Integer[]> coveredAtY (Integer y) {
        ArrayList<Integer[]> coveredIntatY = new ArrayList<Integer[]>();
        for (SensorBeacon sb : zoneSBlist) {
            Integer[] sbint = sb.xMinMaxforY(y);
            if (sbint[0] == null) { //no x-coord covered at y.
                continue;
            }
            coveredIntatY.add(sbint);
        }
        return coveredIntatY;
    }

    void parseInput(ArrayList<String> input) {
        for (String s : input) {
            Integer sx,sy,bx,by;
            s = s.replaceAll("Sensor at ", "");
            s = s.replaceAll(": closest beacon is at", ",");
            s = s.replaceAll("x=", "");
            s = s.replaceAll("y=", "");
            s = s.replaceAll(" ", "");
            String[] sp = s.split(",");
            sx = Integer.parseInt(sp[0]);
            sy = Integer.parseInt(sp[1]);
            bx = Integer.parseInt(sp[2]);
            by = Integer.parseInt(sp[3]);
            SensorBeacon sb = new SensorBeacon(sx, sy, bx, by);
            zoneSBlist.add(sb);
        }
    }

}

class SensorBeacon {
    Integer[] sensorXY;
    Integer[] beaconXY;
    Integer maxLen, xmin, xmax, ymin, ymax;

    SensorBeacon(Integer sensorX, Integer sensorY, Integer beaconX, Integer beaconY) {
        sensorXY = new Integer[2];
        sensorXY[0] = sensorX;
        sensorXY[1] = sensorY;
        beaconXY = new Integer[2];
        beaconXY[0] = beaconX;
        beaconXY[1] = beaconY;
        setXYlen();
    }

    Integer[] xMinMaxforY(int y) {
        Integer[] xmm = new Integer[2];
        if (y < ymin || y > ymax) {
            return xmm;
        }
        int dy = Math.abs(y - sensorXY[1]);
        int dx = maxLen - dy;
        xmm[0] = sensorXY[0] - dx;
        xmm[1] = sensorXY[0] + dx +1; //exclusive index
        return xmm;
    }

    
    private void setXYlen() {
        Integer dx = Math.abs(sensorXY[0] - beaconXY[0]);
        Integer dy = Math.abs(sensorXY[1] - beaconXY[1]);
        maxLen = dx + dy;
        xmin = sensorXY[0] - maxLen;
        xmax = sensorXY[0] + maxLen;
        ymin = sensorXY[1] - maxLen;
        ymax = sensorXY[1] + maxLen;
    }
    
    boolean covers (Integer x, Integer y, boolean checkbeakon) {
        Integer curdx = Math.abs(sensorXY[0] - x);
        Integer curdy = Math.abs(sensorXY[1] - y);
        Integer curdxy = curdx + curdy;
        if (beaconXY[0].equals(x) && beaconXY[1].equals(y) && checkbeakon) {
            return false;
        }
        if (curdxy <= maxLen) {
            return true;
        }
        return false;
    }
}



