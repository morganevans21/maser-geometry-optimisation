"""
QuantumCumulants symbolic model construction for the pentacene maser.

This file defines:

* Hilbert spaces.
* Quantum operators.
* Hamiltonian.
* Lindblad jump operators.
* Observable operators.
* Mean-field equations.

Simulation parameters are defined in parameters.jl.
"""

using ModelingToolkit
using QuantumCumulants

# -------------------------------------------------

# Hilbert spaces

# -------------------------------------------------

# Resonator Fock space

hc = FockSpace(:resonator)

# Pentacene molecular cluster spaces

hPentaceneA_ = NLevelSpace(:PentaceneA, 5)
hPentaceneA = ClusterSpace(hPentaceneA_, N₁, 2)

hPentaceneB_ = NLevelSpace(:PentaceneB, 5)
hPentaceneB = ClusterSpace(hPentaceneB_, N₂, 2)

hPentaceneC_ = NLevelSpace(:PentaceneC, 5)
hPentaceneC = ClusterSpace(hPentaceneC_, N₃, 2)

hPentaceneD_ = NLevelSpace(:PentaceneD, 5)
hPentaceneD = ClusterSpace(hPentaceneD_, N₄, 2)

hPentaceneE_ = NLevelSpace(:PentaceneE, 5)
hPentaceneE = ClusterSpace(hPentaceneE_, N₅, 2)

hPentaceneF_ = NLevelSpace(:PentaceneF, 5)
hPentaceneF = ClusterSpace(hPentaceneF_, N₆, 2)

hPentaceneG_ = NLevelSpace(:PentaceneG, 5)
hPentaceneG = ClusterSpace(hPentaceneG_, N₇, 2)

hPentaceneH_ = NLevelSpace(:PentaceneH, 5)
hPentaceneH = ClusterSpace(hPentaceneH_, N₈, 2)

hPentaceneI_ = NLevelSpace(:PentaceneI, 5)
hPentaceneI = ClusterSpace(hPentaceneI_, N₉, 2)

hPentaceneJ_ = NLevelSpace(:PentaceneJ, 5)
hPentaceneJ = ClusterSpace(hPentaceneJ_, N₁₀, 2)

# Product Hilbert space containing the resonator and all molecular clusters

h =
hc ⊗
hPentaceneA ⊗
hPentaceneB ⊗
hPentaceneC ⊗
hPentaceneD ⊗
hPentaceneE ⊗
hPentaceneF ⊗
hPentaceneG ⊗
hPentaceneH ⊗
hPentaceneI ⊗
hPentaceneJ

# -------------------------------------------------

# Quantum operators

# -------------------------------------------------

@qnumbers a::Destroy(h)

# Transition operators for each molecular cluster

σA(i, j) = Transition(h, :σA, i, j, 2)
σB(i, j) = Transition(h, :σB, i, j, 3)
σC(i, j) = Transition(h, :σC, i, j, 4)
σD(i, j) = Transition(h, :σD, i, j, 5)
σE(i, j) = Transition(h, :σE, i, j, 6)
σF(i, j) = Transition(h, :σF, i, j, 7)
σG(i, j) = Transition(h, :σG, i, j, 8)
σH(i, j) = Transition(h, :σH, i, j, 9)
σI(i, j) = Transition(h, :σI, i, j, 10)
σJ(i, j) = Transition(h, :σJ, i, j, 11)

# -------------------------------------------------

# Hamiltonian

# -------------------------------------------------

H =
Δₘ * a' * a +
g₅₃A * (a' * sum(σA(3, 5)) + a * sum(σA(5, 3))) +
g₅₃B * (a' * sum(σB(3, 5)) + a * sum(σB(5, 3))) +
g₅₃C * (a' * sum(σC(3, 5)) + a * sum(σC(5, 3))) +
g₅₃D * (a' * sum(σD(3, 5)) + a * sum(σD(5, 3))) +
g₅₃E * (a' * sum(σE(3, 5)) + a * sum(σE(5, 3))) +
g₅₃F * (a' * sum(σF(3, 5)) + a * sum(σF(5, 3))) +
g₅₃G * (a' * sum(σG(3, 5)) + a * sum(σG(5, 3))) +
g₅₃H * (a' * sum(σH(3, 5)) + a * sum(σH(5, 3))) +
g₅₃I * (a' * sum(σI(3, 5)) + a * sum(σI(5, 3))) +
g₅₃J * (a' * sum(σJ(3, 5)) + a * sum(σJ(5, 3)))

# -------------------------------------------------

# Lindblad operators

# -------------------------------------------------

function jump_ops(σ)
[
σ(2, 1),
σ(1, 2),
σ(3, 2),
σ(4, 2),
σ(5, 2),

    σ(1, 3),
    σ(1, 4),
    σ(1, 5),
    σ(3, 5),
    σ(5, 3),

    σ(4, 5),
    σ(5, 4),
    σ(3, 4),
    σ(4, 3),

    σ(3, 3) - σ(4, 4),
    σ(3, 3) - σ(5, 5),
    σ(4, 4) - σ(5, 5),
]

end

σ_clusters = [
σA,
σB,
σC,
σD,
σE,
σF,
σG,
σH,
σI,
σJ,
]

J = vcat(
map(jump_ops, σ_clusters)...,
[a, a'],
)

# -------------------------------------------------

# Lindblad rates

# -------------------------------------------------

cluster_rates = [
ξ, ξ + kₛₚ,
k₂₃, k₂₄, k₂₅,
k₃₁, k₄₁, k₅₁,
k₃₄, k₄₃,
k₃₅, k₅₃,
k₄₅, k₅₄,
0.5 * χ₃₄, 0.5 * χ₃₅, 0.5 * χ₄₅,
]

rates = [
repeat(cluster_rates, 10)...,
(1.0 + nₘₜₕ) * κ,
nₘₜₕ * κ,
]

# -------------------------------------------------

# Observables

# -------------------------------------------------

ops = [
a' * a,

σA(2, 2)[1], σA(3, 3)[1], σA(4, 4)[1], σA(5, 5)[1],
σB(2, 2)[1], σB(3, 3)[1], σB(4, 4)[1], σB(5, 5)[1],
σC(2, 2)[1], σC(3, 3)[1], σC(4, 4)[1], σC(5, 5)[1],
σD(2, 2)[1], σD(3, 3)[1], σD(4, 4)[1], σD(5, 5)[1],
σE(2, 2)[1], σE(3, 3)[1], σE(4, 4)[1], σE(5, 5)[1],
σF(2, 2)[1], σF(3, 3)[1], σF(4, 4)[1], σF(5, 5)[1],
σG(2, 2)[1], σG(3, 3)[1], σG(4, 4)[1], σG(5, 5)[1],
σH(2, 2)[1], σH(3, 3)[1], σH(4, 4)[1], σH(5, 5)[1],
σI(2, 2)[1], σI(3, 3)[1], σI(4, 4)[1], σI(5, 5)[1],
σJ(2, 2)[1], σJ(3, 3)[1], σJ(4, 4)[1], σJ(5, 5)[1],

]

# -------------------------------------------------

# Generate QuantumCumulants equations

# -------------------------------------------------

eqs = meanfield(
ops,
H,
J;
rates=rates,
order=2,
)

eqs_c = complete(eqs, order=2)
