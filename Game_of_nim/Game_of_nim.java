/* The game of Nim is played as follows. Starting with three heaps, each containing a variable number of items, 
two players take turns removing one or more items from a single pile. The player who eventually is forced to take the last stone loses.
For example, if the initial heap sizes are 3, 4, and 5, a game could be played like this:
- P1 takes 3 items from B. - P2 takes 2 items from C.
- P1 takes 1 items from B. - P2 takes 2 items from C
- P1 takes 3 items from A. - P2 takes 1 items from C -> Player one wins
Given a list of non-zero starting values [a, b, c], and assuming optimal play, determine whether the first player has a forced win. */

package Game_of_nim;
import java.util.Arrays;
public class Game_of_nim {
    public static void main(String[] args) {
        int[] start = {1,8,3};
        Arrays.sort(start);
        Nim game = new Nim(start);
    }
}
