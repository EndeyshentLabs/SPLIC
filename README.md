# SPLIC

SPLIC - Simple Programming Language In Clojure.
It's stack-based esoteric programming language written in Clojure.

## What it can?

It can:

- Sum up two number
- Subtract one number from another
- Divide one number by another
- Multiply one number by another
- Exit from program
- Halt the program(exit with 1)
- Print value on top of the stack
- Print value on top of the stack as ASCII character
- Dump all stack

Example:

```forth
123 2 * !
```

The program above will print `246` to standard output.


*Hello, World!* in SPLIC:

```forth
72 @ 101 @ 108 @ 108 @ 111 @ 44 @ 32 @ 87 @ 111 @ 114 @ 108 @ 100 @ 33 @
```

## Using

SPLIC intrepreter intreprets file named `src.spl` in current working directory.

To run the intrepreter use:

```console
$ clojure splic.clj
<program_output>
```
