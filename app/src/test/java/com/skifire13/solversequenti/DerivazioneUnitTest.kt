package com.skifire13.solversequenti

import com.skifire13.solversequenti.solver.*
import org.junit.Test
import org.junit.Assert.*

@Suppress("RemoveRedundantBackticks", "NonAsciiCharacters")
class DerivazioneUnitTest {
    @Test fun `ax-F`() {
        assertEquals(
            Derivazione(
                parse("A & B, 0, C |- D")!!,
                Regola.AxF,
                Stop
            ),
            parse("A & B, 0, C |- D")!!.derivazione()
        )
    }

    @Test fun `ax-tt`() {
        assertEquals(
            Derivazione(
                parse("A & B, C |- D, 1, E")!!,
                Regola.AxT,
                Stop
            ),
            parse("A & B, C |- D, 1, E")!!.derivazione()
        )
    }

    @Test fun `ax-id`() {
        assertEquals(
            Derivazione(
                parse("A & B, C |- D, A & B")!!,
                Regola.AxId,
                Stop
            ),
            parse("A & B, C |- D, A & B")!!.derivazione()
        )
    }

    @Test fun `Non derivabile`() {
        listOf(
            "|-",
            "A |-",
            "A, B |-",
            "|- A",
            "|- A, B",
            "A, B |- C, D",
            "tt |-",
            "|- 0",
            "tt |- 0"
        ).forEach {input ->
            assertEquals(
                Derivazione(
                    parse(input)!!,
                    Regola.NonDerivabile,
                    Stop
                ),
                parse(input)!!.derivazione()
            )
        }
    }

    @Test fun `&-S`() {
        assertEquals(
            Derivazione(
                parse("A, B & C |- D")!!,
                Regola.AndS,
                Ramo(parse("A, B, C |- D")!!.derivazione())
            ),
            parse("A, B & C |- D")!!.derivazione()
        )
    }

    @Test fun `V-D`() {
        assertEquals(
            Derivazione(
                parse("A |- B V C, D")!!,
                Regola.OrD,
                Ramo(parse("A |- B , C, D")!!.derivazione())
            ),
            parse("A |- B V C, D")!!.derivazione()
        )
    }

    @Test fun `¬-S`() {
        assertEquals(
            Derivazione(
                parse("A, ¬B |- C")!!,
                Regola.NotS,
                Ramo(parse("A |- B, C")!!.derivazione())
            ),
            parse("A, ¬B |- C")!!.derivazione()
        )
    }

    @Test fun `¬-D`() {
        assertEquals(
            Derivazione(
                parse("A |- ¬B, C")!!,
                Regola.NotD,
                Ramo(parse("A, B |- C")!!.derivazione())
            ),
            parse("A |- ¬B, C")!!.derivazione()
        )
    }

    @Test fun `implica-D`() {
        assertEquals(
            Derivazione(
                parse("A |- B -> C, D")!!,
                Regola.ImplicaD,
                Ramo(parse("A, B |- C, D")!!.derivazione())
            ),
            parse("A |- B -> C, D")!!.derivazione()
        )
    }

    @Test fun `&-D`() {
        assertEquals(
            Derivazione(
                parse("A |- B & C, D")!!,
                Regola.AndD,
                Rami(parse("A |- B, D")!!.derivazione(), parse("A |- C, D")!!.derivazione())
            ),
            parse("A |- B & C, D")!!.derivazione()
        )
    }

    @Test fun `V-S`() {
        assertEquals(
            Derivazione(
                parse("A, B V C |- D")!!,
                Regola.OrS,
                Rami(parse("A, B |- D")!!.derivazione(), parse("A, C |- D")!!.derivazione())
            ),
            parse("A, B V C |- D")!!.derivazione()
        )
    }

    @Test fun `implica-S`() {
        assertEquals(
            Derivazione(
                parse("A, B -> C |- D")!!,
                Regola.ImplicaS,
                Rami(parse("A |- B, D")!!.derivazione(), parse("A, C |- D")!!.derivazione())
            ),
            parse("A, B -> C |- D")!!.derivazione()
        )
    }

    @Test fun `sc-sx`() {
        assertEquals(
            Derivazione(
                parse("A & B, C |- D, E V F")!!,
                Regola.ScSx,
                Ramo(parse("C, A & B |- D, E V F")!!.derivazione())
            ),
            parse("A & B, C |- D, E V F")!!.derivazione()
        )
    }

    @Test fun `sc-dx`() {
        assertEquals(
            Derivazione(
                parse("A, B, C |- D, E V F")!!,
                Regola.ScDx,
                Ramo(parse("A, B, C |- E V F, D")!!.derivazione())
            ),
            parse("A, B, C |- D, E V F")!!.derivazione()
        )
    }
}