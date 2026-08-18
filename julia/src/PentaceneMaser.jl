"""
PentaceneMaser

Simulation of a multi-cluster pentacene maser using QuantumCumulants.jl.

This module organises the project into separate files:

* constants.jl       : Universal physical constants.
* parameters.jl      : Symbolic and numerical simulation parameters.
* symbolic_model.jl  : QuantumCumulants model construction.
* simulation.jl      : Numerical ODE solving routines.
* analysis.jl        : Post-processing and population calculations.
* plotting.jl        : Figure generation and visualisation.
* utils.jl           : General helper functions.
  """

module PentaceneMaser

# -------------------------------------------------

# Dependencies

# -------------------------------------------------

using Colors
using ModelingToolkit
using OrdinaryDiffEq
using Plots
using QuantumCumulants
using SteadyStateDiffEq

# -------------------------------------------------

# Project files

# -------------------------------------------------

include("constants.jl")
include("parameters.jl")
include("symbolic_model.jl")
include("simulation.jl")
include("analysis.jl")
include("plotting.jl")
include("utils.jl")

# -------------------------------------------------

# Public interface

# -------------------------------------------------

export run_simulation
export calc_population
export create_plot

end
