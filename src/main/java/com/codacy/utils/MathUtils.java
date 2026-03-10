package com.codacy.utils;

public class MathUtils {

    private int magicNumber;

    public MathUtils(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    /**
     * Adds 2 numbers, unless there is a magic number on the second argument or 4th arguments is another push wiht changedssss
     */
    public int magicAdd(int x, int y) {
        if (y == magicNumber) {
            System.out.println("Magic ");
            return y - x;
        } else {
            return x + y;
        }
    }

    // Bad practice: throwing a RuntimeException without a message or cause
    public void doBadPractice() {
        throw new IllegalArgumentException(); // Illegal argument test
    }
}
