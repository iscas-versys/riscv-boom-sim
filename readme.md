# RocketChipImport
尝试把rocketchip项目引入到自己的项目中

## 注意事项
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
```bash
mill -i generator[6.5.0].runMain freechips.rocketchip.system.FuzzMain --target-dir /root/research/rocket-chip/build/rtl --full-stacktrace --split-verilog
```
- --split-verilog 在新版本的firrtl里面并不支持，可能有别的名字，请在编译的时候主动指定。
## 在根目录下的编译命令
```bash
mill -i testsoc.runMain testsoc.SimMain --target-dir ./build/rtl --full-stacktrace
```
- 目前直接拷贝了之前rocket-chip里面能用的bootrom，之后可能需要一些修改。

## 关于BOOM的编译
- 类似于Rocket Core，BOOM的核心名称是boomcore