package Stuff.CoinToss;

public class CoinToss {

    public static void main(String[] args) {
        int even = 0;
        int odd = 0;
        for (int i = 0; i < 10000000; i++) {
            if (tossFair()) {
                even++;
            } else {
                odd++;
            }
        }
        System.out.println("even: " + even);
        System.out.println("odd: " + odd);
    }


    private static boolean tossFair() {
        int c = 0;
        boolean tb = tossBiased();
        while (tossBiased() == tb) {
            c++;
        }
        return c % 2 == 0;
    }
    private static boolean tossBiased() {
        return Math.random() < 0.99;
    }
}
