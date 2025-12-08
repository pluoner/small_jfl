package adventofcode.y2025.dec2;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import adventofcode.common.Helpers;
public class Dec2 {
    private static final Logger LOGGER = Logger.getLogger(Dec2.class.getName());
    public static void main(String[] args) {
        List<String> input = Helpers.imp("y2025/dec2/res/input.txt");
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
            Long nSilly = nextSilly(interval[0]);
            while (true) {
                if (nSilly > interval[1]) {
                    break;
                }
                totSilly++;
                nSilly = nextSilly(nSilly);
            }
            LOGGER.log(Level.INFO, "Interval: {0,number,#}-{1,number,#}", new Object[]{interval[0], interval[1]});
        }
        // LOGGER.log(Level.INFO, "On zero: {0,number,#}", atzeroCounter);
        // LOGGER.log(Level.INFO, "Passed zero: {0,number,#}", passedZeroCounter + atzeroCounter);
    }
    private static Long nextSilly(Long n) {
        
        return null;
    }
}