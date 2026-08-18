"""
Simulation parameters for the pentacene maser model.

This file contains:

* QuantumCumulants symbolic parameters.
* Numerical parameter values.
* Initial model configuration.
* Parameter vectors passed into the symbolic equations.

Physical constants are defined in constants.jl.
"""

using QuantumCumulants

# -------------------------------------------------

# Symbolic parameters

# -------------------------------------------------

# Population sizes

@cnumbers N₁ N₂ N₃ N₄ N₅ N₆ N₇ N₈ N₉ N₁₀

# Cavity and spin frequencies

@cnumbers ωₘ ω₅₃ Δₘ

# Cavity loss, thermal and pumping parameters

@cnumbers κ nₘₜₕ ξ

# Spin-photon coupling strengths

@cnumbers g₅₃A g₅₃B g₅₃C g₅₃D g₅₃E
@cnumbers g₅₃F g₅₃G g₅₃H g₅₃I g₅₃J

# Molecular transition rates

@cnumbers kₛₚ
@cnumbers k₂₃ k₂₄ k₂₅
@cnumbers k₃₁ k₄₁ k₅₁
@cnumbers k₃₄ k₄₃ k₃₅ k₅₃ k₄₅ k₅₄

# Spin dephasing rates

@cnumbers χ₃₄ χ₃₅ χ₄₅

# -------------------------------------------------

# Molecular cluster parameters

# -------------------------------------------------

# Number of molecules represented by each cluster

N_cluster = 1e17 * 0.1

N₁_val = N_cluster
N₂_val = N_cluster
N₃_val = N_cluster
N₄_val = N_cluster
N₅_val = N_cluster
N₆_val = N_cluster
N₇_val = N_cluster
N₈_val = N_cluster
N₉_val = N_cluster
N₁₀_val = N_cluster

# -------------------------------------------------

# Molecular transition parameters

# -------------------------------------------------

ω₅₃_val = 0.0

kₛₚ_val = 4.2e7

# Intersystem crossing rates

k₂₃_val = 6.9e7 * 0.08
k₂₄_val = 6.9e7 * 0.16
k₂₅_val = 6.9e7 * 0.76

# Decay rates from levels 3, 4, 5

k₃₁_val = 0.2e4
k₄₁_val = 1.4e4
k₅₁_val = 2.2e4

# Spin relaxation between levels

k₃₄_val = 2.8e4
k₄₃_val = k₃₄_val

k₃₅_val = 1.1e4
k₅₃_val = k₃₅_val

k₄₅_val = 0.4e4
k₅₄_val = k₄₅_val

# -------------------------------------------------

# Spin dephasing parameters

# -------------------------------------------------

χ₃₄_val = 1.1e6
χ₃₅_val = 1.1e6
χ₄₅_val = 1.1e6

# -------------------------------------------------

# Optical pumping parameters

# -------------------------------------------------

λₚ = 592e-9
σ_λp = 2e-21
Aₚ = 1.9e-6

P = 2000
T = 293

# Pumping rate

ξ_val = (λₚ * σ_λp) / (2π * ħ * c * Aₚ) * P

# -------------------------------------------------

# Cavity parameters

# -------------------------------------------------

ωₘ_val = 2π * 1.45e9

κ_val = 2π * 0.4e6

Δₘ_val = 0.0

nₘₜₕ_val = 1.0 / (exp(ħ * ωₘ_val / (k_B * T)) - 1)

# -------------------------------------------------

# Spin-photon coupling parameters

# -------------------------------------------------

g₅₃_val = 0.23

g₅₃A_val = g₅₃_val
g₅₃B_val = g₅₃_val
g₅₃C_val = g₅₃_val
g₅₃D_val = g₅₃_val
g₅₃E_val = g₅₃_val

g₅₃F_val = g₅₃_val
g₅₃G_val = g₅₃_val
g₅₃H_val = g₅₃_val
g₅₃I_val = g₅₃_val
g₅₃J_val = g₅₃_val

# -------------------------------------------------

# Parameter vectors for QuantumCumulants equations

# -------------------------------------------------

ps = [
N₁, N₂, N₃, N₄, N₅, N₆, N₇, N₈, N₉, N₁₀,

```
ωₘ, ω₅₃, Δₘ,

κ, nₘₜₕ, ξ,

g₅₃A, g₅₃B, g₅₃C, g₅₃D, g₅₃E,
g₅₃F, g₅₃G, g₅₃H, g₅₃I, g₅₃J,

kₛₚ,

k₂₃, k₂₄, k₂₅,

k₃₁, k₄₁, k₅₁,

k₃₄, k₄₃, k₃₅, k₅₃, k₄₅, k₅₄,

χ₃₄, χ₃₅, χ₄₅,
```

]

p0 = [
N₁_val, N₂_val, N₃_val, N₄_val, N₅_val,
N₆_val, N₇_val, N₈_val, N₉_val, N₁₀_val,

```
ωₘ_val, ω₅₃_val, Δₘ_val,

κ_val, nₘₜₕ_val, ξ_val,

g₅₃A_val, g₅₃B_val, g₅₃C_val, g₅₃D_val, g₅₃E_val,
g₅₃F_val, g₅₃G_val, g₅₃H_val, g₅₃I_val, g₅₃J_val,

kₛₚ_val,

k₂₃_val, k₂₄_val, k₂₅_val,

k₃₁_val, k₄₁_val, k₅₁_val,

k₃₄_val, k₄₃_val, k₃₅_val,
k₅₃_val, k₄₅_val, k₅₄_val,

χ₃₄_val, χ₃₅_val, χ₄₅_val,
```

]

# -------------------------------------------------

# Comparison parameters

# -------------------------------------------------

Q_initial = 21067
Vₘ_initial = 2.10e-7

Q_optimised = 138170
Vₘ_optimised = 3.63e-7

Vₘ_baseline = Vₘ_initial

κ_initial = ωₘ_val / Q_initial
κ_optimised = ωₘ_val / Q_optimised

g₅₃_initial = g₅₃_val * sqrt(Vₘ_baseline / Vₘ_initial)
g₅₃_optimised = g₅₃_val * sqrt(Vₘ_baseline / Vₘ_optimised)
