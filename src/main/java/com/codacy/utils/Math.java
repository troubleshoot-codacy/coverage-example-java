package com.codacy.utils;

import static java.lang.Math.*;

public class Math {

    private int magicNumber;

    public Math(int magicNumber) {
        this.magicNumber = magicNumber;
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
            return abs(y - x);
        } else if (y != magicNumber){
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            return abs(x + y);
        } else {
            throw new RuntimeException();
        }
    }
}
// foo
// foo
// foo
// foo
