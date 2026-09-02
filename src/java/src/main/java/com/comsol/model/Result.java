package com.comsol.model;

/**
 * Stub for COMSOL Result class.
 */
public class Result {

    public Numerical numerical(String name) {
        return new Numerical();
    }

    public Table table(String name) {
        return new Table();
    }
}