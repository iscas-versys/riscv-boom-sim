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