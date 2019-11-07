package com.skifire13.solversequenti.solver

import java.lang.StringBuilder

data class Sequente(
    val propsSx: List<Proposizione>,
    val propsDx: List<Proposizione> // Invertito l'ordine rispetto l'originale
) {
    override fun toString(): String {
        val builder = StringBuilder()
        if(propsSx.isNotEmpty()) {
            propsSx.joinTo(builder, separator = ", ")
            builder.append(' ')
        }
        builder.append('⊢')
        if(propsDx.isNotEmpty()) {
            builder.append(' ')
            propsDx.asReversed().joinTo(builder, separator = ", ")
        }
        return builder.toString()
    }
}