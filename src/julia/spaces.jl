"""
Construction of the Hilbert spaces used by the pentacene maser model.
"""

const CLUSTER_NAMES = (
    :A,
    :B,
    :C,
    :D,
    :E,
    :F,
    :G,
    :H,
    :I,
    :J,
)

"""
    ModelSpaces

Container holding the Hilbert spaces used by the model.
"""
struct ModelSpaces
    cavity::FockSpace
    clusters::Vector{ClusterSpace}
    total::ProductSpace
end

"""
    build_spaces(params)

Construct the cavity Hilbert space, the ten pentacene cluster spaces,
and the combined product space.
"""
function build_spaces(::MaserParameters)

    cavity = FockSpace(:resonator)

    clusters = ClusterSpace[]

    for name in CLUSTER_NAMES
        single_space = NLevelSpace(Symbol("Pentacene", name), 5)

        push!(
            clusters,
            ClusterSpace(
                single_space,
                @cnumbers(Symbol("N", name)...),
                2,
            ),
        )
    end

    total = cavity

    for cluster in clusters
        total = total ⊗ cluster
    end

    return ModelSpaces(
        cavity,
        clusters,
        total,
    )
end
