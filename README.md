# Quantum Optimisation of MASER Designs

**Computational optimisation of MASER resonator geometry using a binary genetic algorithm coupled to automated finite-element electromagnetic simulation.**

> Master's project — MEng Materials Science and Engineering, Imperial College London

[hero architecture figure]

[![Java 11](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk\&logoColor=white)](https://www.oracle.com/java/)
[![Julia 1.9](https://img.shields.io/badge/Julia-1.9.4-9558B2?logo=julia\&logoColor=white)](https://julialang.org/)
[![Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven\&logoColor=white)](https://maven.apache.org/)
[![COMSOL](https://img.shields.io/badge/simulation-COMSOL-1F4E79)](https://www.comsol.com/)

---

## Overview
This project investigates whether evolutionary optimisation can discover novel resonator geometries for a **pentacene MASER** operating in the strong-coupling regime.

The design problem is formulated as a **100-dimensional binary optimisation problem**. Each chromosome represents a candidate dielectric geometry, with each binary variable selecting between dielectric material and air:

```text
false → dielectric (matDielec)
true  → air       (matAir)
```

The resulting search space contains [2^{100} \approx 1.27\times10^{30}] possible geometries.

A Java optimisation framework based on [Jenetics](https://jenetics.io/) generates candidate geometries and automatically evaluates them using a COMSOL finite-element model. The simulation pipeline constructs the geometry, remeshes and solves the model, extracts the Purcell factor, and returns the result to the genetic algorithm as the fitness value.

The best candidate geometries are subsequently analysed using a Julia-based quantum model built with `QuantumCumulants` to estimate their theoretical MASER output power.

## Key Results

### Lossless proof of concept

Under lossless dielectric conditions, the genetic algorithm discovered resonator geometries with substantially higher Purcell factors than the baseline design.

| Design      |         Purcell factor |
| ----------- | ---------------------: |
| Baseline    |   (1.003\times10^{11}) |
| Optimised   | (6.55557\times10^{11}) |
| Improvement |     **~6.54× / ~554%** |

The result demonstrates the feasibility of using evolutionary optimisation to search a high-dimensional discrete resonator design space.

### Important limitation

When a realistic dielectric loss tangent was introduced, the optimisation no longer reliably produced the desired **TE(_{01\delta})** operating mode.

Although the resulting geometries continued to satisfy the 1.45 GHz resonance condition, the electromagnetic solutions increasingly corresponded to spurious modes.

This exposed an important limitation of the original optimisation formulation: **maximising the Purcell factor alone does not guarantee that the resulting design operates in the physically desired mode.**

The project therefore serves primarily as a **proof of concept for computational MASER design optimisation**, while identifying mode selection and realistic dielectric losses as key requirements for a robust optimisation framework.

## Reasearch Question

Can a stochastic optimisation algorithm discover novel MASER resonator geometries that improve the electromagnetic properties relevant to MASER operation, while satisfying a fixed resonance condition?

The optimisation targets the Purcell factor,

[
F_P = \frac{Q}{V_\mathrm{mode}},
]

where (Q) is the resonator quality factor and (V_\mathrm{mode}) is the mode volume.

The optimisation problem can therefore be expressed as

[
\max_{\mathbf{x}\in{0,1}^{100}} F_P(\mathbf{x})
]

subject to the resonator operating at approximately

[
f_\mathrm{res}=1.45\ \mathrm{GHz}.
]

## Methodology
Physical model
Design space
Optimisation
FEM simulation
Analysis

### 1. Binary design representation

The resonator geometry is represented by a binary chromosome containing 100 design variables.

Each variable determines whether the corresponding region contains dielectric or air:

```text
xᵢ = 0 → dielectric
xᵢ = 1 → air
```

This representation provides a scalable discrete design space while allowing the genetic algorithm to modify the geometry using standard binary genetic operators.

The resulting search space contains approximately

[
1.27\times10^{30}
]

possible configurations, making exhaustive enumeration infeasible.

### 2. Genetic Algorithm

The optimisation was implemented in Java using the [Jenetics](https://jenetics.io/) evolutionary optimisation framework.

The principal configuration was:

| Parameter             |                   Value |
| --------------------- | ----------------------: |
| Representation        |       Binary chromosome |
| Number of variables   |                     100 |
| Population size       |                      20 |
| Selection             |    Tournament selection |
| Tournament size       |                       3 |
| Mutation probability  |                    0.05 |
| Crossover             |  Single-point crossover |
| Crossover probability |                     0.8 |
| Offspring fraction    |                     0.8 |
| Elitism               |           2 individuals |
| Maximum generations   |                     250 |
| Objective             | Maximise Purcell factor |
| Optimisation type     |        Single-objective |

Multiple independent optimisation runs were performed to investigate the stochastic behaviour of the algorithm.

### 3. Resonance-frequency control

A major computational challenge was maintaining the resonator at the target frequency of **1.45 GHz** while changing its geometry.

Directly changing the physical geometry would require the COMSOL model to be rebuilt and remeshed, increasing the computational cost of each objective evaluation.

Instead, the optimisation uses the approximate relationship

[
f_\mathrm{res}\propto\frac{1}{\sqrt{\epsilon_r}}
]

to introduce a scaling factor through the global relative permittivity.

This provides an efficient approximation to geometric scaling without requiring a complete geometric remeshing operation for every frequency adjustment.

To improve the accuracy of this correction, the most recent three simulated data points are used to construct a **Lagrange interpolating polynomial**, which estimates the scaling factor required to bring the resonant frequency to 1.45 GHz.

### 4. Automated finite-element simulation

The Java application interfaces with COMSOL through the COMSOL Java API.

For each candidate chromosome, the pipeline:

1. Decodes the binary chromosome.
2. Generates the corresponding dielectric geometry.
3. Applies the frequency-scaling procedure.
4. Generates the finite-element mesh.
5. Solves the electromagnetic model.
6. Extracts the relevant resonator properties.
7. Calculates the Purcell factor.
8. Returns the fitness value to the genetic algorithm.
9. Logs the simulation and optimisation results.

The simulation therefore acts as an expensive black-box objective function within the optimisation loop.

### 5. Quantum analysis

The optimised geometries are subsequently analysed using a Julia-based quantum model.

The Julia implementation uses [`QuantumCumulants`](https://qojulia.github.io/QuantumCumulants.jl/stable/) to construct and solve the relevant quantum dynamical model of the pentacene MASER.

The purpose of this stage is to translate the electromagnetic properties of the optimised resonator into a theoretical estimate of MASER output power.

This creates a separation between:

* **electromagnetic geometry optimisation**, and
* **quantum-system performance analysis**.

## System Architecture

                     MASER Model [COMSOL]
                            ↓
            Genetic Algorithm optimisation [Java]
                            ↓
                      FEM evaluation
                            ↓
                      250 generations ──┐
                            ↑           ↓
                            │       Frequency correction ───┐
                            │               ↑               ↓
                            │               ├───────────────┘
                            ├───────────────┘
                            ↓
                      Purcell fitness
                            ↓
                         Results
            ┌───────────────┴───────────────────┐
            ↓                                   ↓
Quantum simulation [Julia]          Statistical Analysis [Python]

## Experimental Design
Java optimisation framework
COMSOL integration
Julia analysis pipeline
failure handling / reproducibility / performance

The computational experiment consists of repeated stochastic optimisation runs using the same GA configuration.

The objective of the experimental analysis is to distinguish between:

* improvements caused by the evolutionary search process,
* stochastic variation between independent runs, and
* improvements that could be obtained simply by evaluating many random candidate geometries.

### Planned statistical validation

The repository will include fixed-seed experiments comparing:

1. Genetic algorithm optimisation.
2. Random search with a matched evaluation budget.
3. Multiple independent seeds for each method.

The analysis will report:

* best fitness,
* mean fitness,
* median fitness,
* standard deviation,
* interquartile range,
* convergence by generation, and
* computational cost.

This provides a more robust assessment than reporting only the single best optimisation run.

## Results
quantitative results
convergence
robustness
validation

### Lossless optimisation

The lossless model provided a proof of concept for the optimisation framework.

The best discovered geometry increased the Purcell factor from

[
1.003\times10^{11}
]

to

[
6.55557\times10^{11},
]

corresponding to approximately a **6.54× increase** or **554% improvement** relative to the baseline.

### Convergence

Optimisation results are analysed as a function of generation to determine:

* whether fitness improves consistently,
* how quickly the population converges,
* whether optimisation plateaus,
* and how much variability exists between independent runs.

### Mode-selection failure under realistic loss

Introducing a realistic dielectric loss tangent changed the optimisation landscape significantly.

The optimised geometries continued to satisfy the 1.45 GHz resonance condition, but the resulting electromagnetic solutions did not reliably correspond to the desired TE(_{01\delta}) mode.

This indicates that the original scalar objective,

[
F_P,
]

does not fully encode the physical requirements of the MASER.

A future optimisation formulation should therefore incorporate an explicit mode-selection criterion or physically motivated constraint in addition to the Purcell factor.

## Engineering

### Java optimisation framework

The Java implementation provides the main optimisation and simulation orchestration layer.

The codebase separates responsibilities into components for:

* application configuration,
* genetic optimisation,
* model representation,
* COMSOL simulation,
* input/output,
* result processing, and
* utilities.

The Maven project provides dependency management and a reproducible Java build.

### COMSOL automation

The COMSOL workflow is automated through the COMSOL Java API.

The automation covers:

* geometry construction,
* parameter configuration,
* meshing,
* finite-element solving,
* result extraction, and
* communication of the objective value back to the optimisation layer.

Because each simulation requires an electromagnetic FEM solve, the objective function is computationally expensive. A 250-generation optimisation therefore requires thousands of candidate evaluations and approximately eight hours for a typical run on the development system.

### Julia quantum modelling

The Julia implementation provides a separate analysis layer for modelling the quantum dynamics of the optimised pentacene MASER.

The project uses:

* `QuantumCumulants`
* explicit parameter/configuration files
* a Julia project environment
* dedicated modules for simulation, symbolic modelling, analysis and plotting.

### Python analysis

Python is used for supplementary data analysis and visualisation.

The analysis scripts operate on exported optimisation data and generated results rather than forming part of the optimisation loop.

## Technology Stack

| Component                  | Technology          |
| -------------------------- | ------------------- |
| Optimisation               | Java 11             |
| Genetic algorithm          | Jenetics            |
| Build system               | Maven               |
| Electromagnetic simulation | COMSOL Multiphysics |
| COMSOL integration         | COMSOL Java API     |
| Quantum modelling          | Julia 1.9.4         |
| Quantum library            | QuantumCumulants    |
| Data analysis              | Python + Julia      |
| Documentation              | LaTeX               |
| Version control            | Git                 |


## Repository Structure

```text
maser-geometry-optimisation/
│
├── comsol/
│   ├── ComsolModel.mph
│   ├── README.md
│   └── script/
│       └── Script.java
│
├── java/
│   ├── pom.xml
│   ├── README.md
│   └── src/
│       └── main/
│           └── java/
│               ├── app/
│               ├── config/
│               ├── exported/
│               ├── ga/
│               ├── io/
│               ├── model/
│               ├── simulation/
│               └── util/
│
├── julia/
│   ├── Project.toml
│   ├── Manifest.toml
│   ├── README.md
│   └── src/
│       ├── analysis.jl
│       ├── constants.jl
│       ├── parameters.jl
│       ├── PentaceneMaser.jl
│       ├── plotting.jl
│       ├── simulation.jl
│       ├── spaces.jl
│       ├── symbolic_model.jl
│       └── utils.jl
│
├── python/
│   ├── data_analysis.py
│   ├── README.md
│   └── requirements.txt
│
├── results/
│   ├── processed/
│   │   ├── ga-logs.csv
│   │   ├── ga-results.csv
│   │   ├── permittivity-scaling-results.csv
│   │   └── spurious-mode-results.csv
│   ├── raw/
│   │   ├── ga-logs.csv
│   │   └── ga-results.csv
│   └── plots/
│
├── docs/
│   ├── thesis/
│   │   ├── bibliography/
│   │   ├── chapters/
│   │   ├── figures/
│   │   ├── frontmatter/
│   │   ├── preamble/
│   │   ├── tables/
│   │   ├── thesis.tex
│   │   └── thesis.pdf
│   ├── presentation/
│   │   └── presentation.pdf
│   └── statistical-analysis/
│
└── README.md
```

## Installation

### Java

Requirements:

* Java 11
* Maven
* COMSOL Multiphysics with the required Java API access

Build the Java project with:

```bash
cd java
mvn clean package
```

Run the test suite with:

```bash
mvn test
```

### Julia

The Julia environment is specified by `Project.toml` and `Manifest.toml`.

From the repository root:

```julia
using Pkg
Pkg.activate("julia")
Pkg.instantiate()
```

### Python

Install the Python dependencies with:

```bash
cd python
python -m pip install -r requirements.txt
```

## Usage

### Full optimisation

The full optimisation pipeline requires a licensed COMSOL installation.

The general workflow is:

```text
1. Configure optimisation parameters
2. Launch COMSOL model
3. Start Java optimisation
4. Generate candidate binary geometries
5. Run FEM simulations
6. Extract Purcell factors
7. Evolve population
8. Export optimised designs
9. Analyse optimised designs using Julia
10. Generate figures and summary statistics using Julia/Python
```

See [`java/README.md`](java/README.md) and [`comsol/README.md`](comsol/README.md) for component-specific instructions.

### Julia analysis

The Julia environment can be used independently of COMSOL once the required simulation results have been generated.

```bash
cd julia
julia --project=. src/analysis.jl
```

### Python analysis

```bash
cd python
python data_analysis.py
```

### COMSOL Requirements

## Reproducibility

The optimisation is stochastic and therefore requires explicit experiment metadata for exact reproduction.

For reproducible experiments, record:

* random seed,
* GA configuration,
* software versions,
* COMSOL model version,
* simulation parameters,
* best chromosome,
* fitness value,
* runtime, and
* generated result files.

The full electromagnetic optimisation cannot currently be reproduced without a valid COMSOL installation and the appropriate software licence.

The Julia and Python analysis stages can be reproduced independently where the required result files are available.

Future experiments use fixed random seeds to make independent optimisation runs exactly repeatable.

## Limitations

### Computational cost

Each fitness evaluation requires a finite-element electromagnetic simulation. This makes the optimisation substantially more expensive than a conventional numerical optimisation problem.

### Serial simulation pipeline

The current implementation evaluates the COMSOL simulation backend serially. The optimisation framework is therefore constrained by the cost of individual FEM evaluations.

### Idealised lossless model

The strongest optimisation result was obtained under lossless dielectric conditions.

Introducing a realistic dielectric loss tangent caused the optimiser to identify geometries associated with spurious modes rather than the desired TE(_{01\delta}) mode.

### Objective-function specification

The current optimisation maximises the Purcell factor without explicitly encoding the desired electromagnetic mode.

Consequently, a high Purcell factor is not sufficient to guarantee a physically useful MASER design.

### Reproducibility

The original experimental runs were stochastic and were not recorded with fixed random seeds. Subsequent experiments should use deterministic seeds and retain complete run metadata.

## Future Work

Several extensions follow naturally from the limitations identified during the project.

### Multi-criteria optimisation

Extend the objective function to incorporate both Purcell-factor maximisation and mode selection.

For example, a future formulation could use a physically motivated constraint or penalty for solutions that do not correspond to TE(_{01\delta}).

### Realistic dielectric losses

Repeat the optimisation using realistic dielectric loss parameters and investigate how the additional loss changes the structure of the search space.

### Baseline comparison

Compare the genetic algorithm against random search using a matched number of expensive FEM evaluations.

### Hyperparameter analysis

Investigate the sensitivity of optimisation performance to:

* population size,
* mutation probability,
* crossover probability,
* tournament size, and
* number of generations.

### Surrogate modelling

The computational cost of the FEM simulation motivates investigating surrogate-assisted optimisation, where an inexpensive statistical model approximates the expensive simulation objective between high-fidelity evaluations.

### Parallel evaluation

Investigate parallel evaluation of independent candidate geometries where supported by the available COMSOL licensing and computational infrastructure.

### Alternative optimisation methods

Compare the genetic algorithm against other derivative-free optimisation approaches suitable for expensive, discrete and potentially multimodal objective functions.

## Documentation

The `docs/` directory contains the original Master's thesis, presentation, figures and supporting LaTeX source.

Component-specific documentation is also available in:

* [`comsol/README.md`](comsol/README.md)
* [`java/README.md`](java/README.md)
* [`julia/README.md`](julia/README.md)
* [`python/README.md`](python/README.md)

## Citation

If you use this work, please cite the Master's thesis:

```bibtex
@mastersthesis{evans2025quantum,
  author  = {Morgan Evans},
  title   = {Quantum Design and Simulation of MASERs in the Strong Coupling Limit},
  year    = {2025},
  type    = {Master's Thesis},
  note    = {[Online]. Available: \url{}}
}
```

## Acknowledgements

This project was completed as an individual Master's project in the Department of Materials at Imperial College London.

## Licence

MIT License (code) & Creative Commons Attribution 4.0 International (thesis, presentation, etc.)

The repository also contains simulation models and academic documentation with potentially different licensing considerations. Please review the licensing requirements of COMSOL, Imperial College London assets, third-party libraries and included fonts before redistributing modified versions.