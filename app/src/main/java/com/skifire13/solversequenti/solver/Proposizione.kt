package com.skifire13.solversequenti.solver

sealed class Proposizione

object Vero : Proposizione() {
    override fun toString() = "tt"
}
object Falso : Proposizione() {
    override fun toString() = "⊥"
}
data class Atomo(val char: Char) : Proposizione() {
    override fun toString() = char.toString()
}
data class Not(val prop: Proposizione) : Proposizione() {
    override fun toString() = when(prop) {
        is Vero, is Falso, is Atomo, is Not -> "¬$prop"
        else -> "¬($prop)"
    }
}
data class And(val propSx: Proposizione, val propDx: Proposizione) : Proposizione() {
    override fun toString() = when(propSx) {
        is Vero, is Falso, is Atomo, is Not -> "$propSx"
        else -> "($propSx)"
    } + "&" + when(propDx) {
        is Vero, is Falso, is Atomo, is Not -> "$propDx"
        else -> "($propDx)"
    }
}
data class Or(val propSx: Proposizione, val propDx: Proposizione) : Proposizione() {
    override fun toString() = when(propSx) {
        is Vero, is Falso, is Atomo, is Not -> "$propSx"
        else -> "($propSx)"
    } + "∨" + when(propDx) {
        is Vero, is Falso, is Atomo, is Not -> "$propDx"
        else -> "($propDx)"
    }
}
data class Implica(val propSx: Proposizione, val propDx: Proposizione) : Proposizione() {
    override fun toString() = when(propSx) {
        is Implica -> "($propSx)"
        else -> "$propSx"
    } + "→" + when(propDx) {
        is Implica -> "($propDx)"
        else -> "$propDx"
    }
}
