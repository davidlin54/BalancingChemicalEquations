package com.davidlin54.chemistry.models;

/**
 * Created by David on 2016-11-21.
 */

public class Fraction {

    private long numerator;
    private long denominator;

    // create fraction with long or decimal
    public Fraction(double num) {
        double copy = num;
        long exp = 0;
        while (copy % 1 != 0) {
            copy *= 10;
            exp++;
        }
        numerator = (long) copy;
        denominator = (long) Math.pow(10, exp);
    }

    // create fraction
    public Fraction(long num, long denom) {
        numerator = num;
        denominator = denom;
    }

    // whole number
    public Fraction(long num) {
        numerator = num;
        denominator = 1;
    }

    // returns numerator
    public long getNumerator() {
        return numerator;
    }

    // returns denominator
    public long getDenominator() {
        return denominator;
    }

    // reduce fraction
    public void reduce() {
        long red = findGCD(numerator, denominator);
        numerator /= red;
        denominator /= red;
        if (numerator < 0) {
            numerator *= -1;
            denominator *= -1;
        }
        if (denominator < 0) {
            numerator *= -1;
            denominator *= -1;
        }
    }

    // find greatest common denominator
    private long findGCD(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    // find lease common multiple
    private long findLCM(long a, long b) {
        return (a*b)/findGCD(a, b);
    }

    // add fraction b to this fraction
    public Fraction add(Fraction b) {
        long ldenom = findLCM(denominator, b.denominator);
        long num = numerator*ldenom/denominator+b.numerator*ldenom/b.denominator;
        long denom = ldenom;
        Fraction rf = new Fraction(num, denom);
        rf.reduce();
        return rf;
    }

    // subtract fraction b from this fraction
    public Fraction subtract(Fraction b) {
        long ldenom = findLCM(denominator, b.denominator);
        long num = numerator*ldenom/denominator-b.numerator*ldenom/b.denominator;
        long denom = ldenom;
        Fraction rf = new Fraction(num, denom);
        rf.reduce();
        return rf;
    }

    // multiply fraction b to this fraction
    public Fraction multiply(Fraction b) {
        long num = numerator*b.numerator;
        long denom = denominator*b.denominator;
        Fraction rf = new Fraction(num, denom);
        rf.reduce();
        return rf;
    }

    // divide fraction b from this fraction
    public Fraction divide(Fraction b) {
        long num = numerator*b.denominator;
        long denom = denominator*b.numerator;
        Fraction rf = new Fraction(num, denom);
        rf.reduce();
        return rf;
    }

    // compare fraction b to this fraction
    public long compareTo(Fraction b) {
        long ldenom = findLCM(denominator, b.denominator);
        if (Math.abs(numerator*ldenom/denominator) > Math.abs(b.numerator*ldenom/b.denominator)) {
            return 1;
        } else if (Math.abs(numerator*ldenom/denominator) == Math.abs(b.numerator*ldenom/b.denominator)) {
            return 0;
        }
        return -1;
    }

    public boolean equalsZero() {
        return numerator == 0;
    }
}
