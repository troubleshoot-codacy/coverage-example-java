package com.codacy.utils;

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

    public String calculateGrade(int score, boolean isExtraCredit, boolean isLate, boolean isRetake) {
        if (isExtraCredit) {
            if (score >= 95) {
                return "A+";
            } else if (score >= 90) {
                if (!isLate) {
                    return "A";
                } else {
                    if (!isRetake) {
                        return "A-";
                    } else {
                        return "B+";
                    }
                }
            } else if (score >= 80) {
                return "B";
            } else {
                return "C";
            }
        } else {
            if (score >= 90) {
                return "A";
            } else if (score >= 80) {
                if (isLate || isRetake) {
                    return "B-";
                } else {
                    return "B";
                }
            } else if (score >= 70) {
                if (isRetake && !isLate) {
                    return "C+";
                } else {
                    return "C";
                }
            } else {
                return "F";
            }
        }
    }

    // Bad practice: throwing a RuntimeException without a message or cause
    public void doBadPractice() {
        throw new IllegalArgumentException(); // Illegal argument test
    }
}
// foo
// foo
// foo
// foo
