package com.skifire13.solversequenti.solver

import com.skifire13.solversequenti.view.Measure

data class Derivazione(
    val sequente: Sequente,
    val regola: Regola,
    val albero: Albero
) {
    lateinit var measure: Measure
    val sequenteText = sequente.toString()
    val regolaText = regola.toString()
}

sealed class Albero

data class Ramo(val ramo: Derivazione) : Albero()
data class Rami(val ramoSx: Derivazione, val ramoDx: Derivazione) : Albero()
object Stop : Albero()

fun Sequente.derivazione(): Derivazione {
    fun <T> MutableList<T>.removeLast() { removeAt(this.size - 1) }

    // Assiomi
    for(propSx in propsSx) {
        // ax-⊥
        if(propSx == Falso) return Derivazione(this, Regola.AxF, Stop)
        // ax-id
        if(propsDx.any { it == propSx }) return Derivazione(this, Regola.AxId, Stop)
    }
    // ax-tt
    if(propsDx.any { it == Vero }) return Derivazione(this, Regola.AxT, Stop)

    val lastSx = propsSx.lastOrNull()
    val lastDx = propsDx.lastOrNull()

    // &-S
    if(lastSx is And) {
        val newPropsSx = propsSx.toMutableList()
        newPropsSx.removeLast()
        newPropsSx.add(lastSx.propSx)
        newPropsSx.add(lastSx.propDx)
        return Derivazione(this, Regola.AndS, Ramo(this.copy(propsSx = newPropsSx).derivazione()))
    }

    // V-D
    if(lastDx is Or) {
        val newPropsDx = propsDx.toMutableList()
        newPropsDx.removeLast()
        newPropsDx.add(lastDx.propDx)
        newPropsDx.add(lastDx.propSx)
        return Derivazione(this, Regola.OrD, Ramo(this.copy(propsDx = newPropsDx).derivazione()))
    }

    // ¬-S
    if(lastSx is Not) {
        val newPropsSx = propsSx.toMutableList()
        val newPropsDx = propsDx.toMutableList()
        newPropsSx.removeLast()
        newPropsDx.add(lastSx.prop)
        return Derivazione(this, Regola.NotS, Ramo(this.copy(propsSx = newPropsSx, propsDx = newPropsDx).derivazione()))
    }

    // ¬-D
    if(lastDx is Not) {
        val newPropsSx = propsSx.toMutableList()
        val newPropsDx = propsDx.toMutableList()
        newPropsDx.removeLast()
        newPropsSx.add(lastDx.prop)
        return Derivazione(this, Regola.NotD, Ramo(this.copy(propsSx = newPropsSx, propsDx = newPropsDx).derivazione()))
    }

    // ->-D
    if(lastDx is Implica) {
        val newPropsSx = propsSx.toMutableList()
        val newPropsDx = propsDx.toMutableList()
        newPropsDx.removeLast()
        newPropsDx.add(lastDx.propDx)
        newPropsSx.add(lastDx.propSx)
        return Derivazione(this, Regola.ImplicaD, Ramo(this.copy(propsSx = newPropsSx, propsDx = newPropsDx).derivazione()))
    }

    // &-D
    if(lastDx is And) {
        val newPropsDx1 = propsDx.toMutableList()
        val newPropsDx2 = propsDx.toMutableList()
        newPropsDx1.removeLast()
        newPropsDx2.removeLast()
        newPropsDx1.add(lastDx.propSx)
        newPropsDx2.add(lastDx.propDx)
        return Derivazione(this, Regola.AndD, Rami(
            this.copy(propsDx = newPropsDx1).derivazione(),
            this.copy(propsDx = newPropsDx2).derivazione()
        ))
    }

    // V-S
    if(lastSx is Or) {
        val newPropsSx1 = propsSx.toMutableList()
        val newPropsSx2 = propsSx.toMutableList()
        newPropsSx1.removeLast()
        newPropsSx2.removeLast()
        newPropsSx1.add(lastSx.propSx)
        newPropsSx2.add(lastSx.propDx)
        return Derivazione(this, Regola.OrS, Rami(
            this.copy(propsSx = newPropsSx1).derivazione(),
            this.copy(propsSx = newPropsSx2).derivazione()
        ))
    }

    // ->-S
    if(lastSx is Implica) {
        val newPropsSx1 = propsSx.toMutableList()
        val newPropsSx2 = propsSx.toMutableList()
        val newPropsDx1 = propsDx.toMutableList()
        newPropsSx1.removeLast()
        newPropsSx2.removeLast()
        newPropsDx1.add(lastSx.propSx)
        newPropsSx2.add(lastSx.propDx)
        return Derivazione(this, Regola.ImplicaS, Rami(
            this.copy(propsSx = newPropsSx1, propsDx = newPropsDx1).derivazione(),
            this.copy(propsSx = newPropsSx2).derivazione()
        ))
    }

    // sc-sx
    for((i, propSx) in propsSx.withIndex()) {
        if(propSx is Not || propSx is And || propSx is Or || propSx is Implica) {
            val newPropsSx = propsSx.toMutableList()
            newPropsSx[i] = propsSx.last()
            newPropsSx[newPropsSx.size - 1] = propSx
            return Derivazione(this, Regola.ScSx, Ramo(
                this.copy(propsSx = newPropsSx).derivazione()
            ))
        }
    }

    // sc-dx
    for((i, propDx) in propsDx.withIndex()) {
        if(propDx is Not || propDx is And || propDx is Or || propDx is Implica) {
            val newPropsDx = propsDx.toMutableList()
            newPropsDx[i] = propsDx.last()
            newPropsDx[newPropsDx.size - 1] = propDx
            return Derivazione(this, Regola.ScDx, Ramo(
                this.copy(propsDx = newPropsDx).derivazione()
            ))
        }
    }

    // Non derivabile
    return Derivazione(this, Regola.NonDerivabile, Stop)
}
