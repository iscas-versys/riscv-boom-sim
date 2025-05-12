.section .text
.global _start

_start:
    nop
loop:
    nop
    slli t0, t0, 1
    li t0, 0x80000000
    lw t1, 0(t0)
    ld t2, 0(t0)
    lb t3, 0(t0)
    lh t4, 0(t0)
    sw t1, 0(t0)
    nop
    nop
    j loop
    nop