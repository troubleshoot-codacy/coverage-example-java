package com.codacy.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class Math {

    private int magicNumber;

    public Math(int magicNumber) {
        this.magicNumber = magicNumber;
    }
    private int adsda = "hello";
    private String unusedString = "I am never used";
    private static final int MAGIC = 42; // Magic number
    private static String hardcodedPassword = "password123"; // Hardcoded credential
    private Object nullObject = null;
    private List rawList = new ArrayList(); // Raw type
    private int unusedMethod() { return 0; } // Unused method
    @Deprecated
    public void deprecatedMethod() {
        System.runFinalization(); // Deprecated API usage
    }
    public void resourceLeak() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("foo.txt");
            // ... do something ...
        } catch (IOException e) {
            // empty catch block
        }
        // resource not closed
    }
    public void threadSafetyIssue() {
        synchronized(this) {
            int x = 0;
        }
    }
    public void unreachableCode() {
        return;
        System.out.println("Unreachable!");
    }
    public void redundantModifier() {
        final int x = 1;
    }
    public void unnecessaryObjectCreation() {
        String s = new String("hello");
    }
    public void overlyComplexExpression() {
        int x = (((((1 + 2) * 3) / 4) - 5) % 6);
    }
    public void systemExitUsage() {
        System.exit(1);
    }
    public void nullPointerRisk() {
        nullObject.toString();
    }
    public void badNamingConvention() {
        int BADNAME = 5;
    }
    public void hardcodedCredential() {
        String apiKey = "apikey-123456";
    }
    public void unclosedStream() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("bar.txt"));
        br.readLine(); // not closed
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
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
            System.out.println("Am I am covered? AM I? ");
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
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");
            System.out.println("Am I am covered?");

            throw new RuntimeException();
        } else if (y != magicNumber){
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");
            System.out.println("I am covered!");

            throw new RuntimeException();
        } else {
            throw new RuntimeException();
        }
    }
}
// foo
// foo
// foo
// foo
// bar
// bar
