package com.comsol.model;

/**
 * Stub for COMSOL Model class to allow compilation without COMSOL installation.
 */
public class Model {

    public Study study(String studyName) {
        return new Study();
    }

    public Result result() {
        return new Result();
    }

    public Material material(String materialName) {
        return new Material();
    }
}