module Core(
    input wire clock,        
    input wire reset,
    input         auto_buffer_out_a_ready,
    output        auto_buffer_out_a_valid,
    output [2:0]  auto_buffer_out_a_bits_opcode,
    output [2:0]  auto_buffer_out_a_bits_param,
    output [2:0]  auto_buffer_out_a_bits_size,
    output [2:0]  auto_buffer_out_a_bits_source,
    output [31:0] auto_buffer_out_a_bits_address,
    output [7:0]  auto_buffer_out_a_bits_mask,
    output [63:0] auto_buffer_out_a_bits_data,
    output        auto_buffer_out_b_ready,
    input         auto_buffer_out_b_valid,
    input  [1:0]  auto_buffer_out_b_bits_param,
    input  [2:0]  auto_buffer_out_b_bits_size,
    input  [2:0]  auto_buffer_out_b_bits_source,
    input  [31:0] auto_buffer_out_b_bits_address,
    input         auto_buffer_out_c_ready,
    output        auto_buffer_out_c_valid,
    output [2:0]  auto_buffer_out_c_bits_opcode,
    output [2:0]  auto_buffer_out_c_bits_param,
    output [2:0]  auto_buffer_out_c_bits_size,
    output [2:0]  auto_buffer_out_c_bits_source,
    output [31:0] auto_buffer_out_c_bits_address,
    output [63:0] auto_buffer_out_c_bits_data,
    output        auto_buffer_out_d_ready,
    input         auto_buffer_out_d_valid,
    input  [2:0]  auto_buffer_out_d_bits_opcode,
    input  [1:0]  auto_buffer_out_d_bits_param,
    input  [2:0]  auto_buffer_out_d_bits_size,
    input  [2:0]  auto_buffer_out_d_bits_source,
    input  [1:0]  auto_buffer_out_d_bits_sink,
    input  [63:0] auto_buffer_out_d_bits_data,
    input         auto_buffer_out_e_ready,
    output        auto_buffer_out_e_valid,
    output [1:0]  auto_buffer_out_e_bits_sink,
    input         auto_int_local_in_3_0,
    input         auto_int_local_in_2_0,
    input         auto_int_local_in_1_0,
    input         auto_int_local_in_1_1,
    input         auto_int_local_in_0_0,
    input         auto_hartid_in
);

    wire flying_select_0 = 1'b0;
    reg  [1:0] counter = 0; 
    reg  core_reset = 1'b1;

    always @(posedge clk) begin
        if (counter == 2'd0) begin  
            counter <= counter + 1'b1;
        end else begin
            core_reset <= 1'b0; 
        end
    end

    BoomTile uut(
		.clock     (clock),
		.reset     (core_reset),
		.auto_buffer_out_a_ready(auto_buffer_out_a_ready),
		.auto_buffer_out_a_ready(auto_buffer_out_a_ready),
		.auto_buffer_out_b_valid(auto_buffer_out_b_valid),
		.auto_buffer_out_b_bits_param(auto_buffer_out_b_bits_param),
		.auto_buffer_out_b_bits_size(auto_buffer_out_b_bits_size),
		.auto_buffer_out_b_bits_source(auto_buffer_out_b_bits_source),
		.auto_buffer_out_b_bits_address(auto_buffer_out_b_bits_address),
		.auto_buffer_out_c_ready(auto_buffer_out_c_ready),
		.auto_buffer_out_d_valid(auto_buffer_out_d_valid),
		.auto_buffer_out_d_bits_opcode(auto_buffer_out_d_bits_opcode),
		.auto_buffer_out_d_bits_param(auto_buffer_out_d_bits_param),
		.auto_buffer_out_d_bits_size(auto_buffer_out_d_bits_size),
		.auto_buffer_out_d_bits_source(auto_buffer_out_d_bits_source),
		.auto_buffer_out_d_bits_sink(auto_buffer_out_d_bits_sink),
		.auto_buffer_out_d_bits_data(auto_buffer_out_d_bits_data),
		.auto_buffer_out_e_ready(auto_buffer_out_e_ready),
		.auto_int_local_in_3_0(auto_int_local_in_3_0),
		.auto_int_local_in_2_0(auto_int_local_in_2_0),
		.auto_int_local_in_1_0(auto_int_local_in_1_0),
		.auto_int_local_in_1_1(auto_int_local_in_1_1),
		.auto_int_local_in_0_0(auto_int_local_in_0_0),
		.auto_hartid_in(auto_hartid_in),
		.auto_buffer_out_a_valid(auto_buffer_out_a_valid),
		.auto_buffer_out_a_bits_opcode(auto_buffer_out_a_bits_opcode),
		.auto_buffer_out_a_bits_param(auto_buffer_out_a_bits_param),
		.auto_buffer_out_a_bits_size(auto_buffer_out_a_bits_size),
		.auto_buffer_out_a_bits_source(auto_buffer_out_a_bits_source),
		.auto_buffer_out_a_bits_address(auto_buffer_out_a_bits_address),
		.auto_buffer_out_a_bits_mask(auto_buffer_out_a_bits_mask),
		.auto_buffer_out_a_bits_data(auto_buffer_out_a_bits_data),
		.auto_buffer_out_b_ready(auto_buffer_out_b_ready),
		.auto_buffer_out_c_valid(auto_buffer_out_c_valid),
		.auto_buffer_out_c_bits_opcode(auto_buffer_out_c_bits_opcode),
		.auto_buffer_out_c_bits_param(auto_buffer_out_c_bits_param),
		.auto_buffer_out_c_bits_size(auto_buffer_out_c_bits_size),
		.auto_buffer_out_c_bits_source(auto_buffer_out_c_bits_source),
		.auto_buffer_out_c_bits_address(auto_buffer_out_c_bits_address),
		.auto_buffer_out_c_bits_data(auto_buffer_out_c_bits_data),
		.auto_buffer_out_d_ready(auto_buffer_out_d_ready),
		.auto_buffer_out_e_valid(auto_buffer_out_e_valid),
		.auto_buffer_out_e_bits_sink(auto_buffer_out_e_bits_sink),
		.flying_select_0(flying_select_0)
    );

endmodule