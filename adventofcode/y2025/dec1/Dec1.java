package adventofcode.y2025.dec1;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import adventofcode.common.Helpers;
public class Dec1 {
    private static final Logger LOGGER = Logger.getLogger(Dec1.class.getName());
    public static void main(String[] args) {
        List<String> input = Helpers.imp("y2025/dec1/res/input.txt");
        int currentDial = 50;
        int atzeroCounter = 0;
        int passedZeroCounter = 0;
        for (String line : input) {
            int oldCurrentDial = currentDial;
            char direction = line.charAt(0);
            int i = Integer.parseInt(line.substring(1));
            passedZeroCounter += i / 100;
            if (direction == 'L') {
                currentDial -= i % 100;
            } else if (direction == 'R') {
                currentDial += i % 100;
            }
            if (currentDial < 0) {
                if (oldCurrentDial != 0) {
                    passedZeroCounter++;
                }
                currentDial += 100;
            } else if (currentDial >= 100) {
                if (currentDial > 100 && oldCurrentDial != 0) {
                    passedZeroCounter++;
                }
                currentDial -= 100;
            }
            if (currentDial == 0) {
                atzeroCounter++;
            }
        }
        LOGGER.log(Level.INFO, "On zero: {0,number,#}", atzeroCounter);
        LOGGER.log(Level.INFO, "Passed zero: {0,number,#}", passedZeroCounter + atzeroCounter);
    }
}