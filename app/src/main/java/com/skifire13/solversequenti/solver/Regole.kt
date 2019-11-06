package com.skifire13.solversequenti.solver

enum class Regola {
    AxId, AxF, AxT,
    ScSx, ScDx,
    AndS, AndD,
    OrS, OrD,
    NotS, NotD,
    ImplicaS, ImplicaD,
    NonDerivabile;

    // TODO(Spostare in strings.xml)
    override fun toString() = when(this) {
        AxId -> "ax-id"
        AxF -> "ax-⊥"
        AxT -> "ax-tt"
        ScSx -> "sc-sx"
        ScDx -> "sc-dx"
        AndS -> "&-S"
        AndD -> "&-D"
        OrS -> "∨-S"
        OrD -> "∨-D"
        NotS -> "¬-S"
        NotD -> "¬-D"
        ImplicaS -> "→-S"
        ImplicaD -> "→-D"
        NonDerivabile -> ""
    }
}