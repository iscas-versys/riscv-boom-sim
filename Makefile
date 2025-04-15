CHISEL_VERSION = 3.6.1

FUZZ_TOP  = testsoc.SimMain
BUILD_DIR = $(abspath ./build)

RTL_DIR    = $(BUILD_DIR)/rtl
RTL_SUFFIX = sv
TOP_V      = $(RTL_DIR)/SimTop.$(RTL_SUFFIX)

MILL_ARGS = --target-dir $(RTL_DIR) \
            --full-stacktrace \
			-X sverilog


# Coverage support
ifneq ($(FIRRTL_COVER),)
MILL_ARGS += COVER=$(FIRRTL_COVER)
endif

BOOTROM_DIR = $(abspath ./bootrom)
BOOTROM_SRC = $(BOOTROM_DIR)/bootrom.S
BOOTROM_IMG = $(BOOTROM_DIR)/bootrom.img

$(BOOTROM_IMG): $(BOOTROM_SRC)
	@make -C $(BOOTROM_DIR) all CROSS=riscv64-linux-gnu-

bootrom: $(BOOTROM_IMG)

SCALA_FILE = $(shell find ./generators -name '*.scala')
# $(TOP_V): $(SCALA_FILE) $(BOOTROM_IMG)
$(TOP_V): $(SCALA_FILE)
# FIXME: may be still have to compile BOOTROM_IMG
	mill -i testsoc.runMain $(FUZZ_TOP) $(MILL_ARGS)
	@cp ./resources/vsrc/EICG_wrapper.v $(RTL_DIR)
	@sed -i 's/UNOPTFLAT/LATCH/g' $(RTL_DIR)/EICG_wrapper.v
	cp -r ./build/ ./difftest/
# The following line of code has been temporarily commented out:
# FIXME: Very important, needs fixing later.
# @for file in $(RTL_DIR)/*.$(RTL_SUFFIX); do                                  \
# 	sed -i -e 's/$$fatal/xs_assert_v2(`__FILE__, `__LINE__)/g' "$$file"; \
# 	sed -i -e "s/\$$error(/\$$fwrite(32\'h80000002, /g" "$$file";        \
# done
sim-verilog: $(TOP_V)

emu: sim-verilog
# 先检测sim-verilog的生成情况
	@echo "Chisel Version:$(CHISEL_VERSION)"
	@echo "FuzzTop:  $(FUZZ_TOP)"
	@echo "Mill Args: $(MILL_ARGS)"
	@$(MAKE) -C difftest emu WITH_CHISELDB=0 WITH_CONSTANTIN=0 RTL_SUFFIX=$(RTL_SUFFIX) CPU=ROCKET_CHIP

src: sim-verilog

fuzzer:
	@$(MAKE) -C difftest emu WITH_CHISELDB=0 WITH_CONSTANTIN=0 RTL_SUFFIX=$(RTL_SUFFIX) CPU=ROCKET_CHIP

clean:
	rm -rf $(BUILD_DIR)
idea:
	mill -i mill.idea.GenIdea/idea
init:
	git submodule update --init
# Below is the original rocket-chip Makefile
base_dir=$(abspath ./)

CHISEL_VERSION=3.6.1
MODEL ?= TestHarness
PROJECT ?= freechips.rocketchip.system
CFG_PROJECT ?= $(PROJECT)
CONFIG ?= $(CFG_PROJECT).DefaultConfig
MILL ?= mill

verilog:
	cd $(base_dir) && $(MILL) emulator[freechips.rocketchip.system.TestHarness,$(CONFIG)].mfccompiler.compile

clean-all:
	rm -rf out/
