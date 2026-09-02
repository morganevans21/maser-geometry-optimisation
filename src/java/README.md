# Java Genetic Algorithm Optimisation Engine
Core implementation of the genetic algorithm for MASER resonator geometry optimisation, including COMSOL integration, baseline comparisons, and experiment tracking.

## System Overview
This component implements the optimisation loop that drives the MASER geometry search:

1. Generate candidate geometries from binary chromosomes
2. Evaluate each via COMSOL finite-element simulation (Purcell factor fitness)
3. Select parents using tournament selection (size=3)
4. Recombine via single-point crossover (probability=0.8)
5. Mutate via bit flips (probability=0.05)
6. Preserve elite individuals (count=2)
7. Iterate for 250 generations

The architecture separates concerns into clearly defined modules for maintainability and scientific rigor.

## Key Implementation Features

### Genetic Algorithm Configuration
- Chromosome: 100-bit binary string (10×10 dielectric/air grid)
- Population: 20 individuals
- Selection: Tournament selection (tournament size=3)
- Crossover: Single-point (probability=0.8)
- Mutation: Bit-flip (probability=0.05)
- Elitism: 2 individuals preserved each generation
- Termination: 250 generations
- Objective: Maximise Purcell factor (Q/Vₘ)

### Baseline Algorithms (for Comparison)
Each baseline matches the GA's computational budget (5,000 fitness evaluations):

1. Random Search
  - Uniform random sampling from 2¹⁰⁰ possible geometries
  - Identical chromosome representation and fitness evaluation
  - Same seeding mechanism for reproducibility
  - Purpose: Establish whether GA outperforms undirected search

2. Hill Climbing
  - Greedy local search accepting only improving moves
  - Starts from random solution
  - Identical evaluation budget and chromosome representation
  - Purpose: Test whether evolutionary operators provide advantage over local improvement

### COMSOL Integration Architecture
**Critical Design Choice**: Single reusable COMSOL model instance

- Avoids 5,000 model constructions/remeshings (prohibitive cost)
- Java optimisation layer sequentially evaluates all 20 individuals/generation
- Material/domain assignments updated per chromosome via COMSOL Java API
- Study execution and result extraction handled through simulation layer

**Simulation Pipeline** (per fitness evaluation):

1. Decode binary chromosome → material assignments
2. Apply frequency correction scaling factor (from Java constraint handler)
3. Update COMSOL model material properties
4. Execute electromagnetic frequency-domain study
5. Extract Q-factor and mode volume → compute Purcell factor
6. Return fitness to genetic algorithm

### Frequency Constraint Handling
**Problem**: Geometry changes shift resonant frequency away from 1.45 GHz target

**Solution**: Adaptive permittivity scaling with Lagrange interpolation (Java layer)

- Maintains recent history of (permittivity scaling, frequency) pairs
- Uses 3-point Lagrange polynomial to estimate required scaling for 1.45 GHz
- Numerically inverts polynomial to solve for scaling factor
- Avoids costly geometric remeshing while maintaining constraint satisfaction

### Experiment Tracking & Reproducibility
**Automated Metadata Output** (prefixed with `EXPERIMENT_METADATA:`):

```text
{
  "experiment_id": "uuid",
  "method": "genetic_algorithm|random_search|hill_climb",
  "seed": 42,
  "population_size": 20,
  "generations": 250,
  "mutation_probability": 0.05,
  "crossover_probability": 0.8,
  "target_frequency_hz": 1450000000,
  "objective": "purcell_factor",
  "loss_tangent": 0.0,
  "software": {
    "java": "11",
    "comsol": "6.0.0.405",
    "jenetics": "version"
  },
  "runtime_seconds": 28800,
  "fem_evaluations": 5000,
  "best_fitness": 6.55557e11,
  "best_chromosome": "101010...001"
}
```

Reproducibility Protocol:

1. Fix random seed (controls chromosome initialization and stochastic operators)
2. Record GA configuration (population size, generations, rates)
3. Document software versions (Java, COMSOL, Jenetics)
4. Capture experiment metadata output from each run
5. Archive result files (results/raw/ and results/processed/)

## Module Structure

```text
app/
  Main.java              → GA optimisation entry point
ga/
  GeneticAlgorithm.java  → Core GA implementation (Jenetics wrapper)
  FitnessFunctionFactory.java → Creates COMSOL-interfacing fitness functions
  GenerationLogger.java  → Generation-by-generation progress logging
  RandomSearch.java      → Uniform random sampling baseline
  HillClimbing.java      → Greedy local search baseline
config/
  GAConfig.java          → Tunable GA parameters
  Constants.java         → Physical constants and configuration
simulation/
  ModelInitialiser.java  → COMSOL model loading and setup
  MaterialManager.java   → Dynamic material/domain assignment per chromosome
  StudyRunner.java       → Study execution and result extraction
  SimulationReader.java  → Q-factor and mode volume extraction
  PermittivityTuner.java → Frequency correction logic (Lagrange interpolation)
io/
  CsvManager.java        → CSV output management for results and logs
model/
  DomainAssignment.java  → Binary chromosome → material mapping
  SimulationResult.java  → Container for simulation outputs
util/
  DomainUtils.java       → Helper functions for grid operations
```

## Running Experiments
**Standard GA Execution**:

```bash
cd src/java
mvn compile exec:java -Dexec.mainClass="main.java.app.Main" -Dexec.args="<optional_seed>"
```

**Baseline Comparisons** (same computational budget):

```bash
# Random Search
mvn compile exec:java -Dexec.mainClass="main.java.ga.RandomSearch" -Dexec.args="<seed>"

# Hill Climbing  
mvn compile exec:java -Dexec.mainClass="main.java.ga.HillClimbing" -Dexec.args="<seed>"
```

**Key Features**:

- Seeded runs for exact reproducibility
- Independent result directories per execution (results/raw/run_*.*/)
- JSON experiment metadata for each run
- Matched evaluation budgets across all algorithms (5,000 FEM evaluations)

## Validation & Quality Assurance
**Baseline Sanity Checks**:
- Random search should show no consistent improvement over generations
- Hill climbing should converge to local optimum
- GA should demonstrate clear advantage over both baselines

**Convergence Validation**:
- Generation-by-generation logging of best/average fitness
- Visual inspection of improvement trends
- Comparison against theoretical maximum (2¹⁰⁰ search space)

**Mode Validation**:
- Post-optimisation COMSOL verification of TE₀₁δ mode confinement
- Field distribution analysis for high-performing geometries
- Frequency validation at 1.45 GHz ± tolerance

## Limitations & Assumptions
**Computational Constraints**:
- Serial evaluation of COMSOL model (parallelization not implemented)
- Each FEM evaluation ≈5 seconds (dominant computational cost)
- Full 250-generation run ≈8 hours on development hardware

**Physical Simplifications**:
- Axisymmetric 2D approximation (excludes asymmetric geometries)
- Extremely coarse FEM mesh (trades accuracy for speed)
- Lossless dielectric baseline (idealised performance upper bound)
- Frequency control via permittivity scaling (approximate constraint handling)

**Known Issues**:
- Frequency correction accuracy depends on Lagrange interpolation quality
- Dielectric loss tangent introduction causes solver instability/mode switching
- Single shared model risks state contamination between evaluations (mitigated by full property reset)

## Relationship to Other Components
**COMSOL Dependency**:
- Consumes exported COMSOL Java model (comsol/ComsolModel.mph)
- Requires COMSOL 6.0.0.405 with Java API access
- See comsol/README.md for model details

**Julia Analysis Input**:
- Outputs optimised geometries and Purcell factors
- Manual transfer of best Purcell factor to julia/src/parameters.jl
- See julia/README.md for quantum analysis

**Python Analysis Input**:
- Processes CSV outputs from optimisation runs
- See python/README.md for data analysis

See root README.md for project overview and quantitative skills summary.

