package com.codacy.utils;

public class Math {

    private int magicNumber;

    public Math(int magicNumber) {
        this.magicNumber = magicNumber;
    }



    private void emptyLoop() {
        for (int i = 0; i < 10; i++) { // violation 'Must have at least one statement'
        }
    
        try { // violation 'Must have at least one statement'
    
        } catch (Exception e) {
        // ignored
        }
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
}
