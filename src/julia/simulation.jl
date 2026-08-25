"""
Numerical simulation routines for the pentacene maser model.

This file contains:

* Initial condition setup.
* Pump-on and pump-off ODE simulations.
* Simulation helper functions.

The symbolic model is generated in symbolic_model.jl.
Simulation parameters are defined in parameters.jl.
"""

using ModelingToolkit
using OrdinaryDiffEq

# -------------------------------------------------

# Initial conditions

# -------------------------------------------------

# Set initial values of master equations to zero

u0 = zeros(ComplexF64, length(eqs_c))

# Set thermal equilibrium photon number as initial value

u0[1] = nₘₜₕ_val

u0[2] = 0.0
u0[3] = 0.0
u0[4] = 0.0
u0[5] = 0.0

# -------------------------------------------------

# Initial pump-on simulation

# -------------------------------------------------

@named sys = ODESystem(eqs_c)

# Solve pump-on dynamics from 0 to 200 μs

prob = ODEProblem(
sys,
u0,
(0.0, 200e-6),
ps .=> p0,
)

sol = solve(
prob,
Vern9(),
reltol=5e-10,
abstol=5e-10,
)

# -------------------------------------------------

# Pump-off simulation

# -------------------------------------------------

# Set final pump-on state as initial condition

u0_off = sol.u[end]

time_off = (200e-6, 500e-6)

# Set pumping rate to zero after pump-off

p0_off = copy(p0)
p0_off[16] = 0.0

prob_off = ODEProblem(
sys,
u0_off,
time_off,
ps .=> p0_off,
)

sol_off = solve(
prob_off,
RK4(),
)

# -------------------------------------------------

# Combined simulation output

# -------------------------------------------------

t_comb = [sol.t; sol_off.t[2:end]]

n_comb = [
real.(sol[a' * a]);
real.(sol_off[a' * a])[2:end]
]

println("Simulation complete!")

# -------------------------------------------------

# Reusable simulation function

# -------------------------------------------------

"""
Run the maser simulation for a given cavity decay rate and
spin-photon coupling strength.

Returns the simulation time vector and corresponding output power.
"""

function run_simulation(κ, g₅₃)


# Update parameter vector

p0 = [
    N₁_val, N₂_val, N₃_val, N₄_val, N₅_val,
    N₆_val, N₇_val, N₈_val, N₉_val, N₁₀_val,

    ωₘ_val, ω₅₃_val, Δₘ_val,

    κ,
    nₘₜₕ_val,
    ξ_val,

    g₅₃, g₅₃, g₅₃, g₅₃, g₅₃,
    g₅₃, g₅₃, g₅₃, g₅₃, g₅₃,

    kₛₚ_val,

    k₂₃_val, k₂₄_val, k₂₅_val,

    k₃₁_val, k₄₁_val, k₅₁_val,

    k₃₄_val,
    k₄₃_val,
    k₃₅_val,
    k₅₃_val,
    k₄₅_val,
    k₅₄_val,

    χ₃₄_val,
    χ₃₅_val,
    χ₄₅_val,
]


# Solve with pump on

prob = ODEProblem(
    sys,
    u0,
    (0.0, 200e-6),
    ps .=> p0,
)


sol = solve(
    prob,
    Vern9(),
    reltol=5e-10,
    abstol=5e-10,
)


# Solve with pump off

u0_off = sol.u[end]


p0_off = copy(p0)
p0_off[16] = 0.0


prob_off = ODEProblem(
    sys,
    u0_off,
    (200e-6, 500e-6),
    ps .=> p0_off,
)


sol_off = solve(
    prob_off,
    RK4(),
)


# Time and photon number

t = [sol.t; sol_off.t[2:end]]

n = [
    real.(sol[a' * a]);
    real.(sol_off[a' * a])[2:end]
]


# Output power

P_out = ħ * ωₘ_val * κ_val .* n


return t, P_out


end
