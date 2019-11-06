# Solver di sequenti

Applicazione per Android che esegue la derivazione dei sequenti. In futuro sarà anche in grado di classificarli e nel caso sia un'opinione.

# Input 

Il programma cerca di leggere l'intera stringa di input come un sequente o una singola proposizione, senza lasciare nessun carattere non letto alla fine.

Gli spazi sono completamente facoltativi e non modificano l'output del programma.

## Atomo

Un atomo è una lettera dell'alfabeto maiuscola esclusa la `V` perché viene usata come simbolo di `V`.

## Vero e Falso

I simboli di "vero" accettati sono `1` e `tt`.
I simboli di "falso" accettati sono `0` e `⊥`.

## Not

I simboli di "not" accettati sono `¬`, `!` e `~`.

Il not è l'operazione con la precedenza maggiore. Quando viene composto con degli atomi oppure vero/falso non è necessario inserirlo tra parentesi, ad esempio `¬A & B` è lo stesso di `(¬A) & B`, mentre per negare tutto l'and è necessario usare le parentesi `¬(A & B)`.

## And

I simboli di "and" accettati sono `&` e `∧`.

Ha una precedenza inferiore al not e uguale all'or. È quindi necessario aggiungere delle parentesi agli operandi almeno che questi non siano degli atomi, vero/falso oppure la loro negazione. È necessario aggiungere delle parentesi anche nel caso gli operandi siano degli and o degli or.

Esempi:
 - `A & B`
 - `A ∧ B`
 - `(A & B) & C` (notare come sono necessarie le parentesi)
 - `¬A & ¬B` (non sono necessarie le parentesi)

## Or

I simboli di "or" accettati sono `V` e `∨`.

Ha la stessa precedenza dell'and ed è del tutto analogo ad esso (eccetto ovviamente il significato).

## Implica

I simboli di "implica" accettati sono `->` e `→`.

Ha una precedenza inferiore all'and e all'or, pertanto è necessario aggiungere delle parentesi solo nel caso in cui agli operandi siano presenti altri implica.

Esempi:
 - `A & B -> B & A` equivale a `(A & B) -> (B & A)`
 - `(A -> B) -> C` e `A -> (B -> C)` richiedono entrambi le parentesi.

## Sequente

I simboli di "sequente" accettati sono `⊢` e `|-`.

Un sequente viene identificato da due liste di proposizioni separate dal simbolo di sequente. Le proposizioni sono separate da virgole (`,`) e non è necessario aggiungere inserirle all'interno di parentesi. Le liste possono anche essere vuote.

Il simbolo di sequente può essere assente, in questo caso l'input verrà letto come una proposizione singola non nulla e verrà derivata ponendola alla destra del simbolo di sequente. In questo modo il sequente avrà la stessa verità della proposizione immessa.

Esempi:
 - `A & B, C, D |- E, F -> D`
 - `A & B |-` è composto da solo una proposizione a sinistra e nessuna a destra ed è accettato
 - `|- A & B` è composto da solo una proposizione a destra e nessuna a sinistra ed è accettato
 - `|-` è il sequente vuoto ed è accettato
 - `A & B` equivale al sequente `|- A & B`

# Bugs
 - Una derivazione troppo lunga o larga si estenderà oltre i bordi dello schermo e non sarà possibile scrollare per vederla completamente. Si pianifica in futuro di rendere lo scroll 2D possibile
 - La parte più a destra della derivazione (solitamente una regola) viene in parte tagliata

# Immagine di esempio:

![Immagine di esempio](./example-output.jpg)
