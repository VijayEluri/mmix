all the instruction executor refer to the same Machine State, which maitain the status of the machine.

10/26/2008
At this weekend, I setup Eclipse 3.4.1 in Ubuntu 7.10.

how to display a binary file in Unix:

od --format=x1 test.mmo

0000000 98 09 01 01 49 06 9e 19 98 01 20 01 00 00 00 00
0000020 00 00 00 00 00 00 00 00 61 62 00 00 98 01 00 02
0000040 00 00 00 01 23 45 67 8c 98 06 00 02 74 65 73 74
0000060 2e 6d 6d 73 98 07 00 07 f0 00 00 00 98 02 40 00
0000100 98 07 00 09 81 03 fe 01 42 03 00 00 98 07 00 0a
0000120 00 00 00 00 98 01 00 02 00 00 00 01 23 45 a7 68
0000140 98 05 00 10 01 00 ff f5 98 04 0f f7 98 03 20 01
0000160 00 00 00 00 98 06 01 02 66 6f 6f 2e 6d 6d 73 00
0000200 98 07 00 04 f0 00 00 0a 98 08 00 05 00 00 02 00
0000220 00 fe 00 00 98 01 20 01 00 00 00 0a 00 00 63 64
0000240 98 00 00 01 98 00 00 00 98 0a 00 fe 20 00 00 00
0000260 00 00 00 08 00 00 00 01 23 45 67 8c 98 0b 00 00

0000300 20 3a 50 40 50 40 40 20 41 20 42 20 43 09 44 08
0000320 83 40 40 20 4d 20 61 20 69 05 6e 01 23 45 67 8c
0000340 81 40 0f 61 fe 82 00 00 98 0c 00 0a
0000354

local java output:
98090101	49069e19
98012001	00000000
00000000	00000000
61620000	98010002
00000001	2345678c
98060002	74657374
2e6d6d73	98070007
f0000000	98024000
98070009	8103fe01
42030000	9807000a
00000000	98010002
00000001	2345a768
98050010	0100fff5
98040ff7	98032001
00000000	98060102
666f6f2e	6d6d7300
98070004	f000000a
98080005	00000200
00fe0000	98012001
0000000a	00006364
98000001	98000000
980a00fe	20000000
00000008	00000001
2345678c	980b0000
203a5040	50404020
41204220	43094408
83404020	4d206120
69056e01	2345678c
81400f61	fe820000
980c000a	

[oracle@ora10gapp1 mmix]$ od --format=x1 hello.mmo
0000000 98 09 01 01 49 06 7a 74 
		98 02 01 00 
		98 06 00 03
0000020 68 65 6c 6c 6f 2e 6d 6d 73 00 00 00 
		98 07 00 03
0000040 8f ff 01 00 00 00 07 01 f4 ff 00 00 00 00 07 01
0000060 00 00 00 00 
		98 04 00 03 2c 20 77 6f 
		98 07 00 08
0000100 72 6c 64 0a 
		98 07 00 08 00 00 00 00 
		98 0a 00 ff
0000120 00 00 00 00 00 00 01 00 
		98 0b 00 00 20 3a 50 50
0000140 10 40 40 20 4d 20 61 20 69 02 6e 01 00 81 20 53
0000160 20 74 10 10 20 72 20 69 20 6e 02 67 01 14 83 40
0000200 20 61 20 72 20 67 0f 76 01 82 00 00 
		98 0c 00 0c
0000220

local output:
98090101	49067a74
98020100	98060003
68656c6c	6f2e6d6d
73000000	98070003
8fff0100	00000701
f4ff0000	00000701
00000000	98040003
2c20776f	98070008
726c640a	98070008
00000000	980a00ff
00000000	00000100
980b0000	203a5050
10404020	4d206120
69026e01	00812053
20741010	20722069
206e0267	01148340
20612072	20670f76
01820000	980c000c


code snippet:
	8fff0100
	00000701
	f4ff0003
	00000701
	00000000
	
	2c20776f
	
	726c640a
	00

	
	file --
39020003
8c01fe02
f0000006
8c03fe02
30ff0301
5cff0003
c1010300
3d000203
25020208
5502fffa
f8020000
	
	
	
	loading--
39020003	8c01fe02
f0000006	8c03fe02
30ff0301	5cff0003
c1010300	3d000203
25020208	5502fffa
f8020000	
	
	
machine.getGeneralRegister(0)= 4
machine.getGeneralRegister(1)= 0
2008-11-04 16:05:19,622 DEBUG [mmix.Machine] Instruction = 1
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] virtualAt = 0x100
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] at = 256
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] opCode = 57 (0x39)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] z = 3 (0x03)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] executors[57] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 3
machine.specialRegister[L]= 3
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] Instruction = 2
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] virtualAt = 0x104
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] at = 260
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] opCode = 140 (0x8c)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] x = 1 (0x01)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] y = 254 (0xfe)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] z = 2 (0x02)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] executors[140] = mmix.LoadingInstructionExecutor
machine.specialRegister[L]= 3
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] Instruction = 3
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] virtualAt = 0x108
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] at = 264
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] opCode = 240 (0xf0)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] x = 0 (0x00)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] z = 6 (0x06)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] executors[240] = mmix.JumpInstructionExecutor
setThreeByteRA_Forward()
108
18
120
2008-11-04 16:05:19,668 DEBUG [mmix.JumpInstructionExecutor] jump to 0x120
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] Instruction = 4
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] virtualAt = 0x120
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] at = 288
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] opCode = 37 (0x25)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] y = 2 (0x02)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] z = 8 (0x08)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] executors[37] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 3
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] Instruction = 5
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] virtualAt = 0x124
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] at = 292
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] opCode = 85 (0x55)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] y = 255 (0xff)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] z = 250 (0xfa)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] executors[85] = mmix.BranchInstructionExecutor
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] Instruction = 6
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] virtualAt = 0x10c
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] at = 268
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] opCode = 140 (0x8c)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] x = 3 (0x03)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] y = 254 (0xfe)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] z = 2 (0x02)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] executors[140] = mmix.LoadingInstructionExecutor
machine.specialRegister[L]= 4
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] Instruction = 7
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] virtualAt = 0x110
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] at = 272
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] opCode = 48 (0x30)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] x = 255 (0xff)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] y = 3 (0x03)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] z = 1 (0x01)
2008-11-04 16:05:19,668 DEBUG [mmix.Machine] executors[48] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] Instruction = 8
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] virtualAt = 0x114
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] at = 276
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] opCode = 92 (0x5c)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] x = 255 (0xff)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] z = 3 (0x03)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] executors[92] = mmix.BranchInstructionExecutor
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] Instruction = 9
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] virtualAt = 0x118
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] at = 280
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] opCode = 193 (0xc1)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] x = 1 (0x01)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] y = 3 (0x03)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] z = 0 (0x00)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] executors[193] = mmix.BitwiseInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] Instruction = 10
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] virtualAt = 0x11c
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] at = 284
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] opCode = 61 (0x3d)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] x = 0 (0x00)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] y = 2 (0x02)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] z = 3 (0x03)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] executors[61] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 256
machine.specialRegister[L]= 256
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] Instruction = 11
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] virtualAt = 0x120
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] at = 288
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] opCode = 37 (0x25)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] y = 2 (0x02)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] z = 8 (0x08)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] executors[37] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] Instruction = 12
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] virtualAt = 0x124
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] at = 292
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] opCode = 85 (0x55)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] y = 255 (0xff)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] z = 250 (0xfa)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] executors[85] = mmix.BranchInstructionExecutor
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] Instruction = 13
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] virtualAt = 0x10c
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] at = 268
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] opCode = 140 (0x8c)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] x = 3 (0x03)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] y = 254 (0xfe)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] z = 2 (0x02)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] executors[140] = mmix.LoadingInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] Instruction = 14
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] virtualAt = 0x110
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] at = 272
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] opCode = 48 (0x30)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] x = 255 (0xff)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] y = 3 (0x03)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] z = 1 (0x01)
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] executors[48] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] Instruction = 15
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] virtualAt = 0x114
2008-11-04 16:05:19,684 DEBUG [mmix.Machine] at = 276
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 92 (0x5c)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 255 (0xff)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 3 (0x03)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[92] = mmix.BranchInstructionExecutor
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] Instruction = 16
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] virtualAt = 0x120
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] at = 288
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 37 (0x25)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 2 (0x02)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 8 (0x08)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[37] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] Instruction = 17
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] virtualAt = 0x124
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] at = 292
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 85 (0x55)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 255 (0xff)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 250 (0xfa)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[85] = mmix.BranchInstructionExecutor
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] Instruction = 18
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] virtualAt = 0x10c
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] at = 268
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 140 (0x8c)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 3 (0x03)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 254 (0xfe)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 2 (0x02)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[140] = mmix.LoadingInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] Instruction = 19
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] virtualAt = 0x110
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] at = 272
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 48 (0x30)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 255 (0xff)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 3 (0x03)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 1 (0x01)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[48] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] Instruction = 20
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] virtualAt = 0x114
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] at = 276
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 92 (0x5c)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 255 (0xff)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 3 (0x03)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[92] = mmix.BranchInstructionExecutor
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] Instruction = 21
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] virtualAt = 0x120
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] at = 288
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 37 (0x25)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 2 (0x02)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 8 (0x08)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[37] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 256
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] Instruction = 22
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] virtualAt = 0x124
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] at = 292
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 85 (0x55)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 255 (0xff)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 250 (0xfa)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[85] = mmix.BranchInstructionExecutor
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] Instruction = 23
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] virtualAt = 0x128
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] at = 296
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] opCode = 0 (0x0)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] x = 0 (0x00)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] z = 0 (0x00)
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] executors[0] = mmix.InterruptInstructionExecutor
2008-11-04 16:05:19,699 DEBUG [mmix.InterruptInstructionExecutor] machine.getGeneralRegister(255)=-1
machine.specialRegister[L]= 256
2008-11-04 16:05:19,699 DEBUG [mmix.InterruptInstructionExecutor] TRAP to OS: HALT
2008-11-04 16:05:19,699 DEBUG [mmix.Machine] TRAP to OS: HALT
machine.getGeneralRegister(0)= 3
machine.getGeneralRegister(1)= 4000000000000101

after pushl


virtualAddr=6000000000000000
i= 2 ; 2000
machine.getGeneralRegister(0)= 0
machine.getGeneralRegister(1)= 0
machine.getGeneralRegister(2)= 0
2008-11-04 16:08:54,708 DEBUG [mmix.Machine] Instruction = 1
2008-11-04 16:08:54,740 DEBUG [mmix.Machine] virtualAt = 0x100
2008-11-04 16:08:54,740 DEBUG [mmix.Machine] at = 256
2008-11-04 16:08:54,740 DEBUG [mmix.Machine] opCode = 227 (0xe3)
2008-11-04 16:08:54,740 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:08:54,740 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:08:54,740 DEBUG [mmix.Machine] z = 4 (0x04)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] executors[227] = mmix.WydeImmediateInstructionExecutor
machine.specialRegister[L]= 3
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] Instruction = 2
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] virtualAt = 0x104
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] at = 260
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] opCode = 242 (0xf2)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] x = 1 (0x01)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] z = 127 (0x7f)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] executors[242] = mmix.SubroutineInstructionExecutor
machine.getGeneralRegister(0)= 0
machine.getGeneralRegister(1)= 0
machine.getGeneralRegister(2)= 4
machine.getGeneralRegister(0)= 4
machine.getGeneralRegister(1)= 0
machine.getGeneralRegister(2)= 0
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] Instruction = 3
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] virtualAt = 0x300
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] at = 768
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] opCode = 57 (0x39)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] x = 2 (0x02)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] y = 0 (0x00)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] z = 3 (0x03)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] executors[57] = mmix.ArithmeticInstructionExecutor
machine.specialRegister[L]= 3
machine.specialRegister[L]= 3
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] Instruction = 4
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] virtualAt = 0x304
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] at = 772
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] opCode = 140 (0x8c)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] x = 1 (0x01)
2008-11-04 16:08:54,771 DEBUG [mmix.Machine] y = 254 (0xfe)
2008-11-04 16:08:54,786 DEBUG [mmix.Machine] z = 2 (0x02)
2008-11-04 16:08:54,786 DEBUG [mmix.Machine] executors[140] = mmix.LoadingInstructionExecutor

2008-11-04 16:08:54,786 DEBUG [mmix.Machine] 256
machine.getGeneralRegister(0)= 4
machine.getGeneralRegister(1)= 0
machine.getGeneralRegister(2)= 20


int ([^ ]*) = ([^;]*);
	$
	OP_CODE[$2] = "$1";
	
[oracle@ora10gapp1 mmix]$ od --format=x1 cp.mmo
0000000 98 09 01 01 49 11 2b 9f 98 01 20 01 00 00 00 00
0000020 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02
0000040 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01
0000060 98 03 20 01 00 00 00 10 98 03 20 01 00 00 00 00
0000100 98 01 00 01 00 00 01 00 98 06 00 02 63 70 2e 6d
0000120 6d 73 00 00 98 07 00 09 23 ff fe 00 00 00 04 00
0000140 40 ff 00 00 23 ff fe 10 00 00 06 01 f1 ff ff fb
0000160 98 04 00 04 00 00 00 00 98 0a 00 fe 20 00 00 00
0000200 00 00 00 00 00 00 00 00 00 00 01 00 98 0b 00 00
0000220 20 3a 40 40 50 60 60 20 41 20 72 20 67 19 52 00
0000240 83 09 57 10 84 42 10 20 75 09 66 20 82 44 10 20
0000260 6f 20 6e 02 65 01 18 85 40 40 20 4d 20 61 20 69
0000300 02 6e 01 00 81 00 00 00 98 0c 00 0e
0000314


	
set classpath=%classpath%;E:\sourceCode\junit3.8.1\junit3.8.1\junit.jar
set classpath=%classpath%;E:\Jar_Lib\log4j-1.2.8.jar
set classpath=%classpath%;D:\WorkSpace\CS\MMIX_Simulator
set classpath=%classpath%;D:\WorkSpace\CS\MMIX_Simulator\bin
cd D:\WorkSpace\CS\MMIX_Simulator

java mmix.TestCPMMO < .\test\mmix\testcp.mms 

mmo vs mmb
[oracle@ora10gapp1 mmix]$ od --format=x1 hello.mmo
0000000 98 09 01 01 49 06 7a 74 98 02 01 00 98 06 00 03
0000020 68 65 6c 6c 6f 2e 6d 6d 73 00 00 00 
		98 07 00 03
0000040 8f ff 01 00 00 00 07 01 f4 ff 00 00 00 00 07 01
0000060 00 00 00 00 
		98 04 00 03 
		2c 20 77 6f 
		98 07 00 08
0000100 72 6c 64 0a 
		98 07 00 08 00 00 00 00 
		98 0a 00 ff
0000120 00 00 00 00 00 00 01 00 
		98 0b 00 00 20 3a 50 50
0000140 10 40 40 20 4d 20 61 20 69 02 6e 01 00 81 20 53
0000160 20 74 10 10 20 72 20 69 20 6e 02 67 01 14 83 40
0000200 20 61 20 72 20 67 0f 76 01 82 00 00 
		98 0c 00 0c
0000220
[oracle@ora10gapp1 mmix]$ od --format=x1 hello.mmb
0000000 00 00 00 00 00 00 01 00 
		8f ff 01 00 00 00 07 01
0000020 f4 ff 00 03 00 00 07 01 00 00 00 00 
		2c 20 77 6f
0000040 72 6c 64 0a 

		00 00 00 00 00 00 00 00 00 00 00 00
0000060 40 00 00 00 00 00 00 00 
		40 00 00 00 00 00 00 20
0000100 40 00 00 00 00 00 00 18 
		00 00 00 00 00 00 00 00
0000120 40 00 00 00 00 00 00 18 
		68 65 6c 6c 6f 00 00 00
0000140 00 00 00 00 00 00 00 00 
		60 00 00 00 00 00 00 00
0000160 00 00 00 00 00 00 00 01 
		40 00 00 00 00 00 00 08
0000200 00 00 00 00 00 00 00 02 00 00 00 00 00 00 01 00
0000220 00 00 00 00 00 00 00 00 60 00 00 00 00 00 00 80
0000240 ff 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
0000260
