TALK ABOUT THE REQUIRED JAR FILES

Overview

Architecture

Packages

Genetic algorithm

Fitness evaluation

Configuration

Dependencies

Compile

Run

Output CSV

Performance

Extending the optimiser


## Building
This project was developed against the COMSOL Multiphysics 6.0 Java API.
The COMSOL Java libraries are proprietary and are therefore not included in this repository.
To build the project, install COMSOL 6.0 (or a compatible version) and configure the required JARs in the Maven project.


# Java Optimisation Engine

This directory contains the Java implementation of the genetic-algorithm optimisation and its integration with COMSOL Multiphysics.

The system evaluates binary MASER geometries using finite-element simulations and uses the resulting Purcell factor as the fitness function.

## Overview

The Java application combines:

- a binary genetic algorithm implemented using Jenetics;
- COMSOL's Java API;
- automated geometry/material reassignment;
- finite-element simulation;
- resonant-frequency correction;
- Lagrange interpolation;
- fitness evaluation;
- optimisation logging;
- CSV result export.

The Java application evaluates candidate geometries sequentially using a **single shared COMSOL `Model`**.

This design avoids maintaining a separate COMSOL model instance for every member of the genetic-algorithm population.

## Architecture

```text
                         Java Application
                               │
                               ▼
                        ┌─────────────┐
                        │   Main      │
                        └──────┬──────┘
                               │
                               ▼
                    ┌────────────────────┐
                    │ ModelInitialiser   │
                    │                    │
                    │ Load shared COMSOL │
                    │ Model              │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │ GeneticAlgorithm   │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │ Fitness evaluation │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │ DomainAssignment   │
                    │ MaterialManager    │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │ StudyRunner        │
                    │ SimulationReader   │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │ PermittivityTuner  │
                    └─────────┬──────────┘
                              │
                              ▼
                    Purcell factor / fitness
```

## Directory Structure

```text
java/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            ├── app/
            │   └── Main.java
            ├── config/
            │   ├── Constants.java
            │   └── GAConfig.java
            ├── exported/
            │   └── ComsolModel.java
            ├── ga/
            │   ├── FitnessFunctionFactory.java
            │   ├── GenerationLogger.java
            │   └── GeneticAlgorithm.java
            ├── io/
            │   └── CsvManager.java
            ├── model/
            │   ├── DomainAssignment.java
            │   └── SimulationResult.java
            ├── simulation/
            │   ├── MaterialManager.java
            │   ├── ModelInitialiser.java
            │   ├── PermittivityTuner.java
            │   ├── SimulationReader.java
            │   └── StudyRunner.java
            └── util/
                └── DomainUtils.java
```

## Genetic Algorithm

The optimisation uses a binary representation of the dielectric geometry.

Each chromosome contains:

```text
100 binary variables
```

Each variable determines the material assigned to one cell of the `10 × 10` sub-grid:

| Chromosome value | Material                 |
| ---------------- | ------------------------ |
| `false`          | Dielectric (`matDielec`) |
| `true`           | Air (`matAir`)           |

The resulting search space contains:

```text
2¹⁰⁰
```

possible binary geometries.

The optimisation is single-objective and maximises the Purcell factor.

### GA Configuration

| Parameter             | Value                |
| --------------------- | -------------------- |
| Representation        | Binary chromosome    |
| Design variables      | 100                  |
| Population size       | 20                   |
| Selection             | Tournament selection |
| Tournament size       | 3                    |
| Crossover             | Single-point         |
| Crossover probability | 0.8                  |
| Mutation probability  | 0.05                 |
| Offspring fraction    | 0.8                  |
| Elitism               | 2                    |
| Termination           | 250 generations      |
| Fitness               | Purcell factor       |
| Optimisation          | Maximisation         |

The exact Jenetics version should be taken from `pom.xml`.

## Fitness Evaluation

For each candidate chromosome:

1. Decode the binary chromosome.
2. Assign air/dielectric materials to the corresponding sub-grid domains.
3. Run the COMSOL finite-element study.
4. Read the resonant frequency and Purcell factor.
5. Correct the global relative permittivity if required.
6. Re-run the simulation.
7. Repeat until the resonant frequency converges to `1.45 GHz` or the correction limit is reached.
8. Return the final Purcell factor as the fitness.

A candidate that does not reach the target frequency within the allowed correction iterations receives:

```text
fitness = 0.0
```

## Frequency Correction

The resonance correction is implemented in `PermittivityTuner`.

The first two correction estimates use the approximate relationship:

```text
f ∝ 1 / √εᵣ
```

Once three historical simulation points are available, the tuner constructs a three-point Lagrange interpolating polynomial to estimate the global relative permittivity corresponding to the target frequency.

Conceptually:

```text
Initial geometry
      │
      ▼
COMSOL solve
      │
      ├── frequency
      └── Purcell factor
      │
      ▼
Frequency correction
      │
      ▼
Update global εᵣ
      │
      ▼
COMSOL solve
      │
      ▼
...
      │
      ▼
Converged at 1.45 GHz?
      │
   ┌──┴──┐
  Yes    No
   │      │
   ▼      ▼
Fitness  Continue
```

Each candidate therefore requires:

```text
1 initial simulation
```

plus up to 10 additional correction simulations, for a maximum of **11 COMSOL solves per candidate**.

## Shared COMSOL Model

A key architectural decision is the reuse of a single COMSOL model.

The population contains 20 chromosomes, but the application does not instantiate 20 independent COMSOL models.

Instead:

```text
Chromosome 1 ─┐
Chromosome 2  │
Chromosome 3  │
     ...      ├──► Shared COMSOL Model
Chromosome 20 ┘
```

The model is sequentially reparameterised for each candidate.

This reduces the memory and model-management overhead associated with maintaining multiple COMSOL model instances.

## Main Components

### `Main`

Application entry point.

Initialises the application and starts the optimisation workflow.

### `ModelInitialiser`

Responsible for initialising/loading the COMSOL model used by the optimisation.

### `GeneticAlgorithm`

Configures and executes the genetic algorithm.

### `FitnessFunctionFactory`

Creates the fitness function used by the genetic algorithm and connects candidate chromosomes to the COMSOL evaluation process.

### `PermittivityTuner`

Implements the resonant-frequency correction procedure.

It uses the analytical frequency/permittivity relationship for the initial estimates and three-point Lagrange interpolation once sufficient simulation history is available.

### `StudyRunner`

Controls execution of the COMSOL study/solver.

### `SimulationReader`

Extracts simulation quantities, including resonant frequency and Purcell factor.

### `MaterialManager`

Controls the material configuration of the COMSOL model.

### `DomainAssignment`

Represents the relationship between binary design variables and COMSOL sub-grid domains.

### `GenerationLogger`

Records optimisation progress and generation-level information.

### `CsvManager`

Exports optimisation and simulation results for subsequent analysis.

## COMSOL Integration

The Java project communicates with COMSOL through the COMSOL Java API.

The optimisation therefore requires a licensed COMSOL installation.

COMSOL provides a standalone Java API environment through `ModelUtil.initStandalone(...)`, allowing Java applications to initialise the COMSOL API without requiring the normal GUI workflow.

The project can therefore run with COMSOL available in the background, with standalone operation used to avoid repeatedly opening the graphical interface.

## Requirements

### Software

* Java 11
* Maven
* COMSOL Multiphysics 6.0.0.405
* Valid COMSOL licence
* Jenetics version specified in `pom.xml`

The project was developed specifically against Java 11.

## Build

From this directory:

```bash
mvn clean compile
```

Maven is used primarily for:

* dependency management
* compilation
* test execution where applicable
* build configuration

Build artefacts are generated under:

```text
target/
```

`target/` is intentionally not committed to the repository.

## Running

The application entry point is:

```text
app.Main
```

The exact COMSOL environment configuration required to launch the application is installation-dependent because the COMSOL Java libraries must be available to the JVM.

Before running the optimisation:

1. Ensure COMSOL is installed and licensed.
2. Ensure the configured COMSOL model is available.
3. Ensure the COMSOL Java API is available to the Java runtime.
4. Ensure the project is compiled with Java 11.
5. Start the application through the configured Java/Maven environment.

## Outputs

The Java application exports optimisation data which is subsequently consumed by the analysis pipeline.

The result files include information such as:

* candidate chromosomes
* fitness values
* optimisation progress
* simulation results
* optimised designs
* failed/wrong-mode candidates

The exact filenames may change as the analysis pipeline is refined.

## Logging

The optimisation includes logging for monitoring long-running simulations and diagnosing failures.

This was particularly important because a full optimisation run can require many COMSOL solves.

Typical runs used:

```text
20 individuals / generation
250 generations
```

with each individual requiring between 1 and 11 COMSOL solves depending on frequency convergence.

A complete 250-generation run took approximately:

```text
8 hours
```

under the development environment.

## Reproducibility

The optimisation was executed over approximately 20 independent runs during the project.

Random seeds were not fixed in the original implementation. Consequently, exact reproduction of a particular optimisation trajectory is not guaranteed.

Reproducibility should instead be understood as the ability to recreate the optimisation procedure and compare the resulting distributions/performance across independent runs.

For stronger statistical reproducibility, future experiments should record the random seed for each run.

## Testing and Code Quality

The project was developed with a modular structure separating:

* optimisation
* simulation
* model configuration
* data I/O
* configuration
* utilities

The project also uses:

* Maven dependency management
* JavaDoc
* logging
* configuration classes
* static analysis
* tests where applicable

## Performance

The dominant computational cost is the COMSOL finite-element simulation.

The genetic algorithm itself is comparatively inexpensive; fitness evaluation is simulation-bound.

The frequency-correction procedure can increase the number of simulations required for an individual substantially:

```text
Minimum: 1 COMSOL solve
Maximum: 11 COMSOL solves
```

for each chromosome.

This makes efficient model reuse and automated simulation control important aspects of the implementation.

## Limitations

### COMSOL Dependency

The complete pipeline cannot run without a licensed COMSOL installation.

### Version Dependence

The project was developed against:

```text
Java 11
COMSOL 6.0.0.405
```

and the dependency versions specified in `pom.xml`.

### Randomness

Random seeds were not fixed, so exact GA trajectories are not deterministic.

### Frequency Correction

The permittivity correction assumes an approximate relationship between resonant frequency and relative permittivity and improves the estimate using local interpolation.

### Physical Mode Selection

The optimisation objective does not by itself guarantee that the resulting geometry operates in the desired `TE_01δ` mode. This became a significant limitation when realistic dielectric losses were introduced.

## Related Components

* `../comsol/README.md` — COMSOL model construction
* `../julia/README.md` — quantum MASER analysis
* `../python/README.md` — statistical/data analysis
* `../README.md` — project overview
