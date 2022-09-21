package com.codacy.utils;

public class Math {

    private int magicNumber;

    public Math(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    /**
     * Adds 2 numbers, unless there is a magic number on the second argument
     */
    public int magicAdd(int x, int y) {
        if (y == magicNumber) {
            int cenas = 12345;
            int maisCenas = 67890;
            if (cenas > maisCenas) {
                System.out.println("what?");
                return x;
            } else if (cenas < maisCenas) {
                return y;
            } else {
                return y - x;
            }
        } else {
            System.out.println("hey");
            return x + y;
        }
    }
}
