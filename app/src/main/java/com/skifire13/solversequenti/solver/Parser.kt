package com.skifire13.solversequenti.solver

fun parse(input: String) = Parser(input).sequente()

class Parser(private val input: String) {
    private var index = 0

    private fun readChar() = input.getOrNull(index++)
    private fun peekChar() = input.getOrNull(index)

    private inline fun <T> tryParse(func: () -> T?): T? {
        val savedIndex = index
        val result = func()
        if(result == null)
            index = savedIndex
        return result
    }

    private fun matchString(string: String) =
        tryParse {
            if (string.any { it != readChar() }) null
            else true
        } ?: false

    private fun matchStrings(vararg strings: String) = strings.any { matchString(it) }

    private fun whitespaces() {
        while(peekChar() == ' ') {
            index++
        }
    }

    private fun simboloNot() = matchStrings("¬", "!", "~")
    private fun simboloAnd() = matchStrings("&", "∧")
    private fun simboloOr() = matchStrings("V", "∨")
    private fun simboloImplica() = matchStrings("->", "→")
    private fun simboloSequente() = matchStrings("|-", "⊢")

    private fun vero() = if(matchStrings("1", "tt")) Vero else null
    private fun falso() = if(matchStrings("0", "⊥")) Falso else null

    private fun atomo() =
        tryParse {
            if(peekChar() == 'V') return@tryParse null
            val char = ('A'..'Z').firstOrNull { matchString(it.toString()) } ?: return@tryParse null
            Atomo(char)
        }

    private fun not(): Not? =
        tryParse {
            if(!simboloNot()) return@tryParse null
            whitespaces()
            val operando = operando() ?: return@tryParse null
            Not(operando)
        }
    private fun and(): And? =
        tryParse {
            val operando1 = operando() ?: return@tryParse null
            whitespaces()
            if(!simboloAnd()) return@tryParse null
            whitespaces()
            val operando2 = operando() ?: return@tryParse null
            And(operando1, operando2)
        }
    private fun or(): Or? =
        tryParse {
            val operando1 = operando() ?: return@tryParse null
            whitespaces()
            if(!simboloOr()) return@tryParse null
            whitespaces()
            val operando2 = operando() ?: return@tryParse null
            Or(operando1, operando2)
        }
    private fun implica(): Implica? =
        tryParse {
            val operando1 = tryParse { and() }
                ?: tryParse { or() }
                ?: tryParse { operando() }
                ?: return@tryParse null
            whitespaces()
            if(!simboloImplica()) return@tryParse null
            whitespaces()
            val operando2 = tryParse { and() }
                ?: tryParse { or() }
                ?: tryParse { operando() }
                ?: return@tryParse null
            Implica(operando1, operando2)
        }
    private fun operando(): Proposizione? =
        tryParse {
            if(!matchString("(")) return@tryParse null
            whitespaces()
            val proposizione = proposizione() ?: return@tryParse null
            whitespaces()
            if(!matchString(")")) return@tryParse null
            proposizione
        }
        ?: tryParse { vero() }
        ?: tryParse { falso() }
        ?: tryParse { atomo() }
        ?: tryParse { not() }

    private fun proposizione(): Proposizione? =
        tryParse { implica() }
        ?: tryParse { and() }
        ?: tryParse { or() }
        ?: tryParse { operando() }

    private fun listaProposizioni(): MutableList<Proposizione> {
        val proposizioni = mutableListOf<Proposizione>()
        proposizioni += proposizione() ?: return proposizioni
        while(true) {
            tryParse {
                whitespaces()
                if(!matchString(",")) return@tryParse null
                whitespaces()
                proposizioni += proposizione() ?: return@tryParse null
            } ?: return proposizioni
        }
    }

    internal fun sequente(): Sequente? {
        whitespaces()
        if(peekChar() == null) return null
        return tryParse {
            val propsSx = listaProposizioni()
            whitespaces()
            if (!simboloSequente()) return@tryParse null
            whitespaces()
            val propsDx = listaProposizioni()
            propsDx.reverse()
            whitespaces()
            if (readChar() == null) Sequente(propsSx, propsDx) else null
        }
        ?: tryParse {
            val prop = proposizione() ?: return null
            whitespaces()
            if(readChar() == null) Sequente(emptyList(), listOf(prop)) else null
        }
    }
}