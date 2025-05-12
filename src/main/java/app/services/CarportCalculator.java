package app.services;

public class CarportCalculator {
    private static final int POSTS = 1;

    // Stolper
    public int calculatePole(int lenght, boolean addShed) {
        return poleCalc(lenght, addShed);
    }

    private int poleCalc(int lenght, boolean addShed) {
        if (addShed == true) {
            return 2 * (2 + (lenght - 140) / 340) + 4;
        } else {
            return 2 * (2 + (lenght - 140) / 340);
        }
    }
}
