"""
Plotting utilities for the pentacene maser simulation.

This file contains:

* Figure generation.
* Plot styling.
* Visual comparison of simulation cases.

Simulation execution is defined in simulation.jl.
"""

using Colors
using Plots

# -------------------------------------------------

# Plotting function

# -------------------------------------------------

"""
Create a comparison plot of maser output power.

The function compares different cavity decay rates and
spin-photon coupling strengths.

Returns the generated plot object.
"""

function create_plot()

# Parameter pairs for comparison

param_pairs = [
    (κ_initial, g₅₃_initial),
    (κ_optimised, g₅₃_optimised),
]


# Plot colours

colors = [
    RGBA(0.067, 0.192, 0.431, 0.6),
    RGBA(0.855, 0.439, 0.839, 0.6),
]


styles = [
    :solid,
    :solid,
]


# Create plot

plt = plot(
    xlabel="Time [μs]",
    ylabel="Power [W]",
    xlim=(0, 10),
    grid=false,
    background_color=:white,
    legend=false,
    title="",
    framestyle=:box,
    guidefontsize=14,
    tickfontsize=12,
    legendfontsize=12,
)


# Run simulations and add curves

for (i, (κ, g₅₃)) in enumerate(param_pairs)

    t, P_out = run_simulation(κ, g₅₃)

    mask = t .<= 10e-6

    plot!(
        plt,
        t[mask] ./ 1e-6,
        P_out[mask],
        color=colors[i],
        linestyle=styles[i],
        linewidth=2.5,
    )

end


display(plt)

savefig(
    plt,
    "maser_output_custom1.png",
)


return plt

end
