"""
Analysis utilities for processing pentacene maser simulation results.

This file contains:

* Population extraction from QuantumCumulants solutions.
* Combination of pump-on and pump-off population trajectories.

The symbolic model is defined in symbolic_model.jl.
Simulation execution is defined in simulation.jl.
"""

# -------------------------------------------------

# Population calculation

# -------------------------------------------------

"""
Calculate the population of a given pentacene energy level.

The returned population is weighted by the relative number of molecules
in each cluster.
"""

function calc_population(sol, level)

return sum(
    N_val / 1e17 .* real.(sol[σ(level, level)[1]])
    for (N_val, σ) in [
        (N₁_val, σA),
        (N₂_val, σB),
        (N₃_val, σC),
        (N₄_val, σD),
        (N₅_val, σE),
        (N₆_val, σF),
        (N₇_val, σG),
        (N₈_val, σH),
        (N₉_val, σI),
        (N₁₀_val, σJ),
    ]
)

end

# -------------------------------------------------

# Population extraction from initial simulation

# -------------------------------------------------

# Populations during pumping

P30 = calc_population(sol, 3)
P40 = calc_population(sol, 4)
P50 = calc_population(sol, 5)

# Populations after pumping

P30_off = calc_population(sol_off, 3)
P40_off = calc_population(sol_off, 4)
P50_off = calc_population(sol_off, 5)

# -------------------------------------------------

# Combined population trajectories

# -------------------------------------------------

P30_comb = [P30; P30_off[2:end]]

P40_comb = [P40; P40_off[2:end]]

P50_comb = [P50; P50_off[2:end]]

t_comb = [sol.t; sol_off.t[2:end]]
