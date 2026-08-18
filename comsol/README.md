# COMSOL Model Construction

This directory contains the COMSOL model and the Java Application Builder script used to construct the computational MASER geometry.

The COMSOL model forms the simulation backend for the genetic-algorithm optimisation implemented in [`../java`](../java/README.md).

## Overview

The COMSOL component is responsible for constructing and configuring the finite-element model used to evaluate candidate MASER geometries.

The model represents a pentacene MASER consisting of:

- a copper cavity;
- a strontium titanate dielectric;
- a configurable dielectric/air sub-grid;
- electromagnetic physics and boundary conditions;
- a finite-element mesh;
- frequency-domain studies and solvers.

The optimisation problem represents the dielectric geometry using a binary chromosome. Each chromosome contains 100 binary design variables corresponding to the cells of a `10 × 10` sub-grid.

The COMSOL model is subsequently controlled programmatically through the COMSOL Java API by the Java optimisation code.

## Directory Structure

```text
comsol/
├── ComsolModel.mph
├── README.md
└── script/
    └── Script.java
```

### `ComsolModel.mph`

The fully configured COMSOL model used by the optimisation pipeline.

It contains the completed geometry, materials, physics, mesh, studies and solver configuration required for the FEM simulations.

### `script/Script.java`

Java code intended to be executed inside COMSOL's Application Builder.

The script automates much of the model-construction process, including:

* MASER geometry
* the `10 × 10` optimisation sub-grid
* material definitions
* selected physics interfaces
* global parameters
* mesh configuration
* studies and solver configuration

Some configuration remains manual because the required operations could not be automated through the Application Builder interface available for this project.

## Model Construction Workflow

The model was constructed in the following stages:

```text
Application Builder Java script
          │
          ▼
Automatic model construction
          │
          ├── MASER geometry
          ├── 10 × 10 sub-grid
          ├── materials
          ├── physics
          ├── global parameters
          ├── mesh
          └── studies / solvers
          │
          ▼
Manual configuration
          │
          ├── azimuthal symmetry line
          └── selected material/domain assignments
          │
          ▼
Fully configured COMSOL model
          │
          ▼
Export Java model representation
          │
          ▼
Java optimisation project
```

## Design Representation

The optimisation geometry is represented as a binary chromosome containing 100 variables.

Each variable corresponds to one cell in the `10 × 10` sub-grid:

| Binary state | Material                 |
| ------------ | ------------------------ |
| `false`      | Dielectric (`matDielec`) |
| `true`       | Air (`matAir`)           |

During optimisation, the Java program modifies the material/domain assignments in a **single shared COMSOL `Model`** according to the chromosome currently being evaluated.

The population therefore does **not** consist of 20 separate COMSOL models. The 20 individuals are binary chromosomes evaluated sequentially using the same reusable COMSOL model.

## Resonance Frequency

Candidate geometries are evaluated at a target resonant frequency of:

```text
1.45 GHz
```

Changing the physical geometry directly would generally require the model to be remeshed. To avoid repeatedly reconstructing and remeshing the geometry, the optimisation pipeline instead adjusts the global relative permittivity.

The approximate relationship

```text
f ∝ 1 / √εᵣ
```

provides an initial estimate for the required permittivity correction.

After three simulated points are available, a three-point Lagrange interpolating polynomial is used to obtain a more accurate estimate of the global relative permittivity required to reach the target frequency.

The frequency-tuning procedure is implemented in the Java optimisation layer rather than in this COMSOL construction script.

## Running the Application Builder Script

`Script.java` is intended to be executed within COMSOL's Application Builder.

The exact Application Builder workflow depends on the COMSOL installation and project configuration. After execution, the resulting model should be checked manually for the configuration steps that could not be automated.

The completed model can then be exported as Java source for use by the optimisation project.

## COMSOL Version

The project was developed using:

```text
COMSOL Multiphysics 6.0.0.405
```

The generated Java model and API integration should therefore be treated as version-specific.

## Licence Requirement

Running the simulation pipeline requires a valid licensed installation of COMSOL Multiphysics with the functionality required by the model.

The full optimisation workflow cannot be reproduced from this repository using only the Java, Julia, and Python source code.

The Java optimisation project communicates with COMSOL through its Java API. COMSOL provides a standalone Java API environment for Java applications, including model initialisation and model loading.

## Relationship to the Java Component

The completed COMSOL model is consumed by the Java optimisation project:

```text
comsol/
    ComsolModel.mph
          │
          ▼
java/
    ModelInitialiser
          │
          ▼
    Shared COMSOL Model
          │
          ▼
    Genetic-algorithm fitness evaluation
```

See [`../java/README.md`](../java/README.md) for the optimisation and simulation-control implementation.

## Reproducibility

To reproduce the complete computational workflow, a user requires:

1. COMSOL Multiphysics 6.0.0.405 or a compatible environment
2. The appropriate COMSOL licence
3. Java 11
4. The Java project dependencies
5. The configured COMSOL model
6. The Java optimisation source code

The COMSOL model is therefore a required external dependency for reproducing the FEM-based optimisation results.

## Limitations

The Application Builder script does not automate every aspect of model construction. Some COMSOL configuration steps must be performed manually.

The model is also tied to the COMSOL environment and version used during development.

## Related Components

* [`../java/README.md`](../java/README.md) — genetic algorithm and COMSOL simulation control
* [`../julia/README.md`](../julia/README.md) — quantum MASER analysis
* [`../python/README.md`](../python/README.md) — data analysis and visualisation
* [`../README.md`](../README.md) — project overview
