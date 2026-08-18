// =================================================================================================
// Model parameters
// =================================================================================================

// Define grid parameters
model.param().set("h1", "3[mm]");
model.param().set("h2", "14[mm]");
model.param().set("h3", "3[mm]");
model.param().set("r1", "2[mm]");
model.param().set("r2", "6.25[mm]");
model.param().set("r3", "15[mm]");

// Define NxN subgrid resolution parameter
model.param().set("N", "10");

// Define subgrid parameters for "blk22"
model.param().set("h2_sub", "h2/N");
model.param().set("r2_sub", "(r2-r1)/N");

// =================================================================================================
// Geometry
// =================================================================================================

// Create a 2D axisymmetric geometry
model.geom().create("geom", 2);
model.geom("geom").axisymmetric(true);

// Create main 3x3 grid
// Feature tag for each block in 3x3 grid
String[][] tags = {
    {"blk11", "blk12", "blk13"},
    {"blk21", "blk22", "blk23"},
    {"blk31", "blk32", "blk33"}
};

// Height of each grid row
String[] h = {"h3", "h2", "h1"};
// Width of each grid column
String[] r = {"r1", "r2-r1", "r3-r2"};

// Create eah grid in outer 3x3 grid
// Centre block omitted as replaced by NxN sub-grid
for (int i = 0; i < 3; i++) {
	for (int j = 0; j < 3; j++) {
		if (i == 1 && j == 1) {
            continue;           // Skip center block ("blk22")
        }
		model.geom("geom").feature().create(tags[i][j], "Rectangle");
		model.geom("geom").feature(tags[i][j]).set("size", new String[]{r[j], h[i]});
		model.geom("geom").feature(tags[i][j]).set("pos", new String[]{
			(j == 0) ? "0" : (j == 1) ? "r1" : "r2",
			(i == 0) ? "0" : (i == 1) ? "h3" : "h3+h2"
        });
	}
}

// Create NxN subgrid within "blk22"
// Retrieve user-defined sub-grid resolution
int N = (int) model.param().evaluate("N");
for (int i = 0; i < N; i++) {
    for (int j = 0; j < N; j++) {
        // Ensure unique names (e.g., "sub0_0", "sub24_24", "sub101_101")
        String tag = String.format("sub%d_%d", i, j);
        model.geom("geom").feature().create(tag, "Rectangle");
        model.geom("geom").feature(tag).set("size", new String[]{"r2_sub", "h2_sub"});
        model.geom("geom").feature(tag).set("pos", new String[]{
            "r1 + "+ j +"*r2_sub",
            "h3 + "+ i +"*h2_sub"
        });
    }
}

// Build geometry
model.geom("geom").run();

// =================================================================================================
// Materials
// =================================================================================================

// Create dielectric material
model.material().create("matDielec", "Common");
model.material("matDielec").label("Strontium Titanate");
model.material("matDielec").propertyGroup("def")
    .set("relpermittivity", new String[]{
        "316.3-j*0.0333378019259766", "0", "0",
        "0", "316.3-j*0.0333378019259766", "0",
        "0", "0", "316.3-j*0.0333378019259766"
    });
model.material("matDielec").propertyGroup("def")
    .set("electricconductivity", new String[]{
        "0[S/m]", "0", "0",
        "0", "0[S/m]", "0",
        "0", "0", "0[S/m]"
    });
model.material("matDielec").propertyGroup("def")
    .set("relpermeability", new String[]{
        "1", "0", "0",
        "0", "1", "0",
        "0", "0", "1"
    });

// Create "Air" material
model.material().create("matAir", "Common");
model.material("matAir").label("Air");
model.material("matAir").propertyGroup("def")
    .set("relpermittivity", new String[]{
        "1", "0", "0",
        "0", "1", "0",
        "0", "0", "1"
    });
model.material("matAir").propertyGroup("def")
    .set("electricconductivity", new String[]{
        "0[S/m]", "0", "0",
        "0", "0[S/m]", "0",
        "0", "0", "0[S/m]"
    });
model.material("matAir").propertyGroup("def")
    .set("relpermeability", new String[]{
        "1", "0", "0",
        "0", "1", "0",
        "0", "0", "1"
    });

// Create "Copper" as a surface material (for boundaries)
model.material().create("matCopper", "Common");
model.material("matCopper").label("Copper");
model.material("matCopper").propertyGroup("def")
    .set("relpermittivity", new String[]{
        "1", "0", "0",
        "0", "1", "0",
        "0", "0", "1"
    });
model.material("matCopper").propertyGroup("def")
    .set("electricconductivity", new String[]{
        "5.998e7[S/m]", "0", "0",
        "0", "5.998e7[S/m]", "0",
        "0", "0", "5.998e7[S/m]"
    });
model.material("matCopper").propertyGroup("def")
    .set("relpermeability", new String[]{
        "1", "0", "0",
        "0", "1", "0",
        "0", "0", "1"
    });

// Assign dielectric material to all domains by default
model.material("matDielec").selection().all();

//
// Manual COMSOL configuration required
//
// 1. Assign Air material to every outer domain
//      In "Materials" > "Air" in Model Builder, click on all domains in 3x3 grid apart from central
//      rectangle (which contains NxN subgrid)
//
// 2. Assign Copper to external boundaries
//      "Materials" > "Copper", change "Geometric entity level" to "Boundary". Click on all outer
//      boundaries apart from boundaries at r = 0
//

// =================================================================================================
// Physics
// =================================================================================================

// Create EM Waves physics
model.physics().create("emw", "ElectromagneticWaves", "geom");
model.physics("emw").selection().all();

//
// Manual COMSOL configuration required
//
// 1. Apply Axisymmetric boundary condition to r = 0 boundary
//
// 2. Apply a Perfect Electric Conductor (PEC) boundary condition
//      to all external boundaries except r = 0
//

// =================================================================================================
// Mesh
// =================================================================================================

//Add "Extremely coarse" mesh
model.mesh().create("mesh1", "geom");
model.mesh("mesh1").automatic(true);   // Enables physics-controlled mesh
model.mesh("mesh1").autoMeshSize(9);   // 9 = Extremely coarse
model.mesh("mesh1").run();


// =================================================================================================
// Solver
// =================================================================================================

// Configure eigenfrequency study
model.study().create("std1");
model.study("std1").create("eig", "Eigenfrequency");
model.study("std1").feature("eig").set("neigs", 1);
model.study("std1").feature("eig").set("shift", "1.45[GHz]");   // Search around this frequecny

// Configure solver for eigenfrequency study
model.sol().create("sol1");
model.sol("sol1").study("std1");

// Study step
model.sol("sol1").create("st1", "StudyStep");
model.sol("sol1").feature("st1").set("study", "std1");
model.sol("sol1").feature("st1").set("studystep", "eig");

// Variables (optional unless overriding initialization)
model.sol("sol1").create("v1", "Variables");
model.sol("sol1").feature("v1").set("control", "eig");

// Configure eigenvalue solver
model.sol("sol1").create("e1", "Eigenvalue");
model.sol("sol1").feature("e1").set("control", "eig");
model.sol("sol1").feature("e1").set("shift", "1.45[GHz]");      // Search around this frequecny
model.sol("sol1").feature("e1").set("neigs", 1);

// Run solver
//model.study("std1").run();

// Add "Derived Values" and "Table"
