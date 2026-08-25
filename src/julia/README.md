# Julia Quantum MASER Analysis

This directory contains the Julia implementation used to analyse the optimised MASER geometries using a quantum model and estimate theoretical output power.

The analysis uses [QuantumCumulants.jl](https://qojulia.github.io/QuantumCumulants.jl/) to construct and solve the relevant quantum dynamical equations.

## Overview

The Java/COMSOL optimisation identifies promising MASER geometries by maximising the Purcell factor.

The resulting optimised Purcell factor is then supplied to the Julia model, which calculates the theoretical MASER dynamics and output power.

The Julia component therefore provides an independent post-optimisation physics analysis rather than participating directly in the genetic algorithm.

## Workflow

```text
Optimised geometry
       │
       ▼
Final Purcell factor
       │
       ▼
Manual parameter input
       │
       ▼
Julia / QuantumCumulants
       │
       ├── Symbolic quantum model
       │
       ├── Cumulant / ODE formulation
       │
       ├── Numerical time evolution
       │
       └── Steady-state / output analysis
       │
       ▼
Theoretical MASER output power
       │
       ▼
Plots and analysis
```

## Directory Structure

```text
julia/
├── Manifest.toml
├── Project.toml
├── README.md
└── src/
    ├── analysis.jl
    ├── constants.jl
    ├── parameters.jl
    ├── PentaceneMaser.jl
    ├── plotting.jl
    ├── simulation.jl
    ├── spaces.jl
    ├── symbolic_model.jl
    └── utils.jl
```

## Main Module

`src/PentaceneMaser.jl` acts as the main module and entry point for the Julia model, coordinating the other components of the quantum analysis.

The supporting files separate:

* physical constants
* model parameters
* Hilbert/operator spaces
* symbolic model construction
* numerical simulation
* analysis
* plotting
* utility functionality

## Optimised Purcell Factor

The optimised Purcell factor obtained from the Java/COMSOL optimisation is manually entered into:

```text
src/parameters.jl
```

This value is then used by the quantum model when calculating the theoretical MASER behaviour.

The project currently uses this manual hand-off rather than automatically reading the value from the Java result files.

## Quantum Model

The quantum analysis uses `QuantumCumulants.jl` to formulate the relevant quantum dynamical system.

The workflow includes:

1. Definition of the MASER operators and physical parameters.
2. Construction of the symbolic model.
3. Generation of the corresponding dynamical equations.
4. Numerical solution of the resulting ODE system.
5. Analysis of the time evolution.
6. Calculation of theoretical output quantities.

`QuantumCumulants.jl` provides a symbolic framework for deriving and manipulating moment/cumulant equations before numerical solution.

## Outputs

The Julia component produces:

* theoretical MASER output power
* time-dependent simulation results
* plots of relevant dynamical quantities

The resulting figures are used to analyse the physical behaviour of the optimised design.

## Project Files

### `parameters.jl`

Contains the physical and simulation parameters used by the model, including the Purcell factor obtained from the optimisation.

### `constants.jl`

Contains physical and model constants.

### `spaces.jl`

Defines the relevant operator/Hilbert-space structures used by the quantum model.

### `symbolic_model.jl`

Constructs the symbolic quantum model.

### `simulation.jl`

Handles numerical simulation of the resulting dynamical system.

### `analysis.jl`

Processes the simulation results and calculates relevant quantities.

### `plotting.jl`

Generates visualisations of the model and simulation results.

### `utils.jl`

Contains shared helper functionality.

## Environment

The Julia environment is defined by:

```text
Project.toml
Manifest.toml
```

The project uses a pinned dependency environment rather than relying on globally installed Julia packages.

Key dependencies include:

* `QuantumCumulants`
* `ModelingToolkit`
* `DifferentialEquations`
* `OrdinaryDiffEq`
* `SteadyStateDiffEq`
* `DataFrames`
* `CSV`
* `Plots`
* `GR`
* `FFTW`
* `XLSX`

See `Project.toml` for the exact dependency specification and `Manifest.toml` for the resolved environment.

## Installation

A compatible Julia installation is required.

From this directory, instantiate the project environment:

```bash
julia --project=. -e 'using Pkg; Pkg.instantiate()'
```

This installs the dependencies specified by the project environment.

## Running

The main module is:

```text
src/PentaceneMaser.jl
```

The project was developed interactively in VS Code using the Julia extension.

To work with the project environment from a Julia session:

```bash
julia --project=.
```

Then load the project/module as appropriate for the analysis being performed.

The exact execution order of individual analysis scripts is intentionally kept in the source files rather than encoded as a single command-line interface.

## Reproducibility

The Julia environment is substantially reproducible because both:

```text
Project.toml
Manifest.toml
```

are included.

However, the optimised Purcell factor is currently transferred manually from the Java/COMSOL workflow into `parameters.jl`.

Therefore, reproducing the complete end-to-end workflow requires:

1. Reproducing or obtaining the optimised geometry.
2. Obtaining its final Purcell factor.
3. Entering that value into the Julia parameter configuration.
4. Instantiating the Julia environment.
5. Running the quantum analysis.

## Limitations

The Julia model represents a theoretical post-processing stage rather than a direct finite-element simulation.

Its output power therefore depends on the assumptions and parameters of the quantum model as well as the Purcell factor supplied by the optimisation.

The transfer of the optimised Purcell factor from Java to Julia is currently manual.

## Future Improvements

Potential improvements include:

* Automatically importing optimised results from the Java pipeline.
* Automated parameter sweeps.
* Automated generation of all publication figures.
* Recording simulation metadata alongside parameters.
* Integrating the Julia stage into a reproducible end-to-end workflow.

## Related Components

* `../README.md` — project overview
* `../java/README.md` — genetic algorithm and COMSOL optimisation
* `../comsol/README.md` — COMSOL model construction
* `../python/README.md` — data analysis
