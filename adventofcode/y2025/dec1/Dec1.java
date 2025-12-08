package adventofcode.y2025.dec1;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import adventofcode.common.Helpers;
public class Dec1 {
    private static final Logger LOGGER = Logger.getLogger(Dec1.class.getName());
    public static void main(String[] args) {
        List<String> input = Helpers.imp("y2025/dec1/res/input.txt");
        LOGGER.log(Level.INFO, "Input lines: {0}", input.size());
    }
}