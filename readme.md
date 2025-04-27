# RocketChipImport
尝试把rocketchip项目引入到自己的项目中

## 使用的RocketChip和BOOM
均使用自己仓库托管的分支:
- [rocket-chip](https://github.com/SeddonShen/rocket-chip/tree/dev-difftest-boom)
- [riscv-boom](https://github.com/SeddonShen/riscv-boom/tree/dev-difftest)
## 注意事项
- verilator需要直接编译最新Git仓库: (目前可用) Verilator 5.033 devel rev v5.032-66-gcc1133c0d
- rocket-chip应该指定mill版本为`0.11.7`
- rocket-chip编译用firtool为：
```bash
$ firtool --version
LLVM (http://llvm.org/):
  LLVM version 17.0.0git
  Optimized build.
CIRCT firtool-1.43.0
```
## 编译命令
### 仅编译Chisel
```bash
mill -i generator[6.5.0].runMain freechips.rocketchip.system.FuzzMain --target-dir /root/research/rocket-chip/build/rtl --full-stacktrace --split-verilog
```
- --split-verilog 在新版本的firrtl里面并不支持，可能有别的名字，请在编译的时候主动指定。
### 在根目录下的编译命令(形式化验证用)
```bash
mill -i testsoc.runMain testsoc.SimMain --target-dir ./build/rtl --full-stacktrace -X sverilog
```
- 目前直接拷贝了之前rocket-chip里面能用的bootrom，之后可能需要一些修改。

### Make编译
```bash
  make clean && make emu REF=$(pwd)/ready-to-run/riscv64-spike-so -j32 
```

## 关于BOOM的编译
- 类似于Rocket Core，BOOM的核心名称是boomcore

## 关于环境变量
- 需要注意指定NOOP_HOME到根目录
```bash
  export NOOP_HOME=$(pwd)
```
- 或者直接运行
```bash
  source env.sh
```

## 运行命令
```bash
  ./build/emu -i ./ready-to-run/microbench.bin
```