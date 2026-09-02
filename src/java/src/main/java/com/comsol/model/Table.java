package com.comsol.model;

/**
 * Stub for COMSOL Table class.
 */
public class Table {

    public String[][] getTableData(boolean ignoreEmptyRows) {
        // Return a dummy 2D array with some data to match the expected usage in GenerationLogger.printInitialResults
        // The code expects at least 5 columns (index 0 to 4) and at least one row.
        return new String[][]{
                {"0.0", "0.0", "1.0", "1.0", "1.0"},
                {"0.0", "0.0", "2.0", "2.0", "2.0"}
        };
    }
}