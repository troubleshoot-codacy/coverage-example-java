package com.codacy.utils;

public class Math2 {

    private int magicNumber;

    public Math2(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    /**
     * Adds 2 numbers, unless there is a magic number on the second argument
     */
    public int magicAdd(int x, int y) {
        if (y == magicNumber) {
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            return y - x;
        } else {
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            return x + y;
        }
    }

    static public int sum(int x, int y) {
        return x + y;
    }
}
