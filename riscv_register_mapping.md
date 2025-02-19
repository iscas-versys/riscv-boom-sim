# RISC-V 寄存器对照表

| 寄存器编号 | 寄存器名称 | 描述                             |
|------------|------------|----------------------------------|
| `x0`       | `zero`     | 硬编码为 0，写入无效，读取始终返回 0。 |
| `x1`       | `ra`       | 返回地址寄存器（Return Address），用于存储函数返回地址。 |
| `x2`       | `sp`       | 栈指针寄存器（Stack Pointer），指向当前栈顶。 |
| `x3`       | `gp`       | 全局指针寄存器（Global Pointer），用于访问全局变量。 |
| `x4`       | `tp`       | 线程指针寄存器（Thread Pointer），用于线程局部存储。 |
| `x5`       | `t0`       | 临时寄存器 0（Temporary Register 0）。 |
| `x6`       | `t1`       | 临时寄存器 1（Temporary Register 1）。 |
| `x7`       | `t2`       | 临时寄存器 2（Temporary Register 2）。 |
| `x8`       | `s0` / `fp`| 保存寄存器 0（Saved Register 0）或帧指针（Frame Pointer）。 |
| `x9`       | `s1`       | 保存寄存器 1（Saved Register 1）。 |
| `x10`      | `a0`       | 参数寄存器 0（Argument Register 0），用于函数参数或返回值。 |
| `x11`      | `a1`       | 参数寄存器 1（Argument Register 1），用于函数参数或返回值。 |
| `x12`      | `a2`       | 参数寄存器 2（Argument Register 2）。 |
| `x13`      | `a3`       | 参数寄存器 3（Argument Register 3）。 |
| `x14`      | `a4`       | 参数寄存器 4（Argument Register 4）。 |
| `x15`      | `a5`       | 参数寄存器 5（Argument Register 5）。 |
| `x16`      | `a6`       | 参数寄存器 6（Argument Register 6）。 |
| `x17`      | `a7`       | 参数寄存器 7（Argument Register 7）。 |
| `x18`      | `s2`       | 保存寄存器 2（Saved Register 2）。 |
| `x19`      | `s3`       | 保存寄存器 3（Saved Register 3）。 |
| `x20`      | `s4`       | 保存寄存器 4（Saved Register 4）。 |
| `x21`      | `s5`       | 保存寄存器 5（Saved Register 5）。 |
| `x22`      | `s6`       | 保存寄存器 6（Saved Register 6）。 |
| `x23`      | `s7`       | 保存寄存器 7（Saved Register 7）。 |
| `x24`      | `s8`       | 保存寄存器 8（Saved Register 8）。 |
| `x25`      | `s9`       | 保存寄存器 9（Saved Register 9）。 |
| `x26`      | `s10`      | 保存寄存器 10（Saved Register 10）。 |
| `x27`      | `s11`      | 保存寄存器 11（Saved Register 11）。 |
| `x28`      | `t3`       | 临时寄存器 3（Temporary Register 3）。 |
| `x29`      | `t4`       | 临时寄存器 4（Temporary Register 4）。 |
| `x30`      | `t5`       | 临时寄存器 5（Temporary Register 5）。 |
| `x31`      | `t6`       | 临时寄存器 6（Temporary Register 6）。 |

---

## 说明
1. **临时寄存器 (`t0-t6`)**：用于临时存储数据，调用者无需保存。
2. **保存寄存器 (`s0-s11`)**：用于保存调用者的上下文，调用者需要保存和恢复。
3. **参数寄存器 (`a0-a7`)**：用于传递函数参数和返回值。
4. **特殊寄存器**：
   - `zero` (`x0`)：始终为 0。
   - `ra` (`x1`)：存储返回地址。
   - `sp` (`x2`)：栈指针。
   - `gp` (`x3`)：全局指针。
   - `tp` (`x4`)：线程指针。

---
