package com.skifire13.solversequenti

import com.skifire13.solversequenti.solver.*
import org.junit.Test
import org.junit.Assert.*

@Suppress("RemoveRedundantBackticks")
class ParserUnitTest {
    private fun sequenteProposizione(proposizione: Proposizione) = Sequente(emptyList(), listOf(proposizione))

    @Test fun `Atomo`() {
        assertEquals(
            sequenteProposizione(Atomo('A')),
            parse("A")
        )
        assertEquals(
            sequenteProposizione(Atomo('B')),
            parse("B")
        )
    }
    @Test fun `Vero`() {
        assertEquals(
            sequenteProposizione(Vero),
            parse("1")
        )
        assertEquals(
            sequenteProposizione(Vero),
            parse("tt")
        )
    }
    @Test fun `Falso`() {
        assertEquals(
            sequenteProposizione(Falso),
            parse("0")
        )
        assertEquals(
            sequenteProposizione(Falso),
            parse("⊥")
        )
    }
    @Test fun `And`() {
        assertEquals(
            sequenteProposizione(And(Atomo('A'), Atomo('B'))),
            parse("A & B")
        )
        assertEquals(
            sequenteProposizione(And(Vero, Atomo('C'))),
            parse("tt ∧ C")
        )
    }
    @Test fun `Or`() {
        assertEquals(
            sequenteProposizione(Or(Atomo('A'), Atomo('B'))),
            parse("A V B")
        )
        assertEquals(
            sequenteProposizione(Or(Atomo('C'), Falso)),
            parse("C ∨ ⊥")
        )
    }
    @Test fun `Implica`() {
        assertEquals(
            sequenteProposizione(Implica(Atomo('A'), Atomo('B'))),
            parse("A -> B")
        )
        assertEquals(
            sequenteProposizione(Implica(Atomo('C'), Atomo('D'))),
            parse("C → D")
        )
    }
    @Test fun `Not`() {
        assertEquals(
            sequenteProposizione(Not(Atomo('A'))),
            parse("!A")
        )
        assertEquals(
            sequenteProposizione(Not(Vero)),
            parse("!tt")
        )
    }
    @Test fun `Parentesi`() {
        assertEquals(
            sequenteProposizione(Or(
                And(Atomo('A'), Atomo('B')),
                And(Atomo('C'), Atomo('D'))
            )),
            parse("(A & B) V (C & D)")
        )
        assertEquals(
            sequenteProposizione(Not(And(Atomo('A'), Atomo('B')))),
            parse("¬(A & B)")
        )
    }
    @Test fun `Precedenze`() {
        assertEquals(
            sequenteProposizione(And(Not(Atomo('A')), Atomo('B'))),
            parse("¬A & B")
        )
        assertEquals(
            sequenteProposizione(Implica(
                And(Atomo('A'), Atomo('B')),
                Or(Atomo('C'), Atomo('D'))
            )),
            parse("A & B -> C V D")
        )
    }
    @Test fun `Spazi`() {
        assertEquals(
            sequenteProposizione(Or(
                And(Atomo('A'), Atomo('B')),
                And(Atomo('C'), Atomo('D'))
            )),
            parse("   (  A&B   )   V     (      C       &     D  )   ")
        )
        assertEquals(
            sequenteProposizione(And(Not(Atomo('A')), Atomo('B'))),
            parse("¬  A     &  B")
        )
        assertEquals(
            sequenteProposizione(Implica(
                And(Atomo('A'), Atomo('B')),
                Or(Atomo('C'), Atomo('D'))
            )),
            parse("A   &     B ->   C   V    D")
        )
    }
    @Test fun `Sequente`() {
        assertEquals(
            Sequente(emptyList(), emptyList()),
            parse("|-")
        )
        assertEquals(
            Sequente(emptyList(), emptyList()),
            parse("⊢")
        )
        assertEquals(
            Sequente(listOf(
                Atomo('A'),
                Atomo('B'),
                Atomo('C')
            ), emptyList()),
            parse("A, B, C⊢")
        )
        assertEquals(
            Sequente(listOf(
                And(Atomo('A'), Atomo('B')),
                Or(Atomo('C'), Atomo('D')),
                Atomo('E')
            ), listOf(
                Atomo('G'),
                Implica(Atomo('F'), Atomo('D'))
            )),
            parse("A & B, C V D, E ⊢ F -> D, G")
        )
    }
    // -» Means -> because we can't use > in function names
    @Suppress("NonAsciiCharacters")
    @Test fun `A -» B, (A & B) V D, A & B -» C V D |- ¬D`() {
        assertEquals(
            Sequente(
                listOf(
                    Implica(Atomo('A'), Atomo('B')),
                    Or(
                        And(Atomo('A'), Atomo('B')),
                        Atomo('D')
                    ),
                    Implica(
                        And(Atomo('A'), Atomo('B')),
                        Or(Atomo('C'), Atomo('D'))
                    )
                ),
                listOf(
                    Not(Atomo('D'))
                )
            ),
            parse("A -> B, (A & B) V D, A & B -> C V D |- ¬D")
        )
    }
    @Test fun `Sequente invalido`() {
        listOf(
            "",
            "   ",
            "()",
            "V",
            "t t",
            "A &",
            "& B",
            "A V",
            "V B",
            "A & B & C",
            "A V B V C",
            "A -> B -> C",
            "¬& A",
            "A & (B",
            "A , B , |- C",
            "A ,, B , |- C",
            "A ,B, ( |- C )",
            ", A |-"
        ).forEach {
            assertNull(parse(it))
        }
    }
}
