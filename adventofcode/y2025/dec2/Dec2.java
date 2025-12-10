package adventofcode.y2025.dec2;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import adventofcode.common.Helpers;
public class Dec2 {
    private static final Logger LOGGER = Logger.getLogger(Dec2.class.getName());
    public static void main(String[] args) {
        List<String> input = Helpers.imp("y2025/dec2/res/testinput.txt");
        List<Long[]> intervals = new ArrayList<>();
        for (String line : input) {
            String[] ints = line.split(",");
            for (String intervalStr : ints) {
                String[] parts = intervalStr.split("-");
                Long[] interval = new Long[2];
                interval[0] = Long.parseLong(parts[0]);
                interval[1] = Long.parseLong(parts[1]);
                intervals.add(interval);
            }
        }
        Integer totSilly = 0;
        for (Long[] interval : intervals) {
            Long nSilly = nextSilly(interval[0], false);
            while (true) {
                if (nSilly > interval[1]) {
                    break;
                }
                totSilly++;
                nSilly = nextSilly(nSilly, true);
            }
        }
        LOGGER.log(Level.INFO, "Silly count: {0,number,#}", totSilly);
    }
    private static Long nextSilly(Long n, boolean knownSilly) {
        int digits = noDigits(n);
        int sillyExp = (digits + 1) / 2;
        Long sillyBase = n / 10^sillyExp;
        if (knownSilly) {
            sillyBase++;
        }
        while (true) {
            Long candidate = buildSilly(sillyBase, sillyExp);
            if (candidate > n) {
                return candidate;
            }
            sillyBase++;
        }
    }

    private static Long buildSilly(Long sillyBase, int sillyExp) {
        return sillyBase * 10^sillyExp + sillyBase;
    }
    private static int noDigits(Long n) {
        int digits = 0;
        while (n > 0) {
            n /= 10;
            digits++;
        }
        return digits;
    }
}