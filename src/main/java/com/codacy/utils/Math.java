package com.codacy.utils;

public class Math {

    private int magicNumber;

    public Math(int magicNumber) {
        System.out.println("test modification");
        this.magicNumber = magicNumber;
    }

    public int add(int x, int y) {
        return x + y;
    }

    /**
     * Adds 2 numbers, unless there is a magic number on the second argument or 4th arguments is another push wiht changedssss
     */
    public int magicAdd(int x, int y) {
        if (y == magicNumber) {
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            return y - x;
        } else {
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            return x + y;
        }
    }
}
// foo
// foo
// foo
// foo
