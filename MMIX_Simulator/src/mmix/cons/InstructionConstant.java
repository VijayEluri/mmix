package mmix.cons;

public class InstructionConstant {
	public static final int TRAP = 0x00;
	// floating point
	public static final int FCMP = 0x01;
	public static final int FUN = 0x02;
	public static final int FEQL = 0x03;
	public static final int FADD = 0x04;
	public static final int FIX = 0x05;
	public static final int FSUB = 0x06;
	public static final int FIXU = 0x07;

	public static final int FLOT = 0x08;
	public static final int FLOTI = 0x09;
	public static final int FLOTU = 0x0a;
	public static final int FLOTUI = 0x0b;
	public static final int SFLOT = 0x0c;
	public static final int SFLOTI = 0x0d;
	public static final int SFLOTU = 0x0e;
	public static final int SFLOTUI = 0x0f;

	public static final int FMUL = 0x10;
	public static final int FCMPE = 0x11;
	public static final int FUNE = 0x12;
	public static final int FEQLE = 0x13;
	public static final int FDIV = 0x14;
	public static final int FSQRT = 0x15;
	public static final int FREM = 0x16;
	public static final int FINT = 0x17;
	// arithmetic
	public static final int MUL = 0X18;

	public static final int MULI = 0X19;

	public static final int MULU = 0X1a;

	public static final int MULUI = 0X1b;

	public static final int DIV = 0X1c;

	public static final int DIVI = 0X1d;

	public static final int DIVU = 0X1e;

	public static final int DIVUI = 0X1f;

	public static final int ADD = 0X20;

	public static final int ADDI = 0X21;

	public static final int ADDU = 0X22;

	public static final int ADDUI = 0X23;

	public static final int SUB = 0X24;

	public static final int SUBI = 0X25;

	public static final int SUBU = 0X26;

	public static final int SUBUI = 0X27;

	public static final int _2ADDU = 0X28;

	public static final int _2ADDUI = 0X29;

	public static final int _4ADDU = 0X2a;

	public static final int _4ADDUI = 0X2b;

	public static final int _8ADDU = 0X2c;

	public static final int _8ADDUI = 0X2d;

	public static final int _16ADDU = 0X2e;

	public static final int _16ADDUI = 0X2f;

	public static final int CMP = 0X30;

	public static final int CMPI = 0X31;

	public static final int CMPU = 0X32;

	public static final int CMPUI = 0X33;

	public static final int NEG = 0X34;

	public static final int NEGI = 0X35;

	public static final int NEGU = 0X36;

	public static final int NEGUI = 0X37;

	public static final int SL = 0X38;

	public static final int SLI = 0X39;

	public static final int SLU = 0X3a;

	public static final int SLUI = 0X3b;

	public static final int SR = 0X3c;

	public static final int SRI = 0X3d;

	public static final int SRU = 0X3e;

	public static final int SRUI = 0X3f;
	// branch
	public static final int BN = 0x40;

	public static final int BNB = 0x41;

	public static final int BZ = 0x42;

	public static final int BZB = 0x43;

	public static final int BP = 0x44;

	public static final int BPB = 0x45;

	public static final int BOD = 0x46;

	public static final int BODB = 0x47;

	public static final int BNN = 0x48;

	public static final int BNNB = 0x49;

	public static final int BNZ = 0x4a;

	public static final int BNZB = 0x4b;

	public static final int BNP = 0x4c;

	public static final int BNPB = 0x4d;

	public static final int BEV = 0x4e;

	public static final int BEVB = 0x4f;

	public static final int PBN = 0x50;

	public static final int PBNB = 0x51;

	public static final int PBZ = 0x52;

	public static final int PBZB = 0x53;

	public static final int PBP = 0x54;

	public static final int PBPB = 0x55;

	public static final int PBOD = 0x56;

	public static final int PBODB = 0x57;

	public static final int PBNN = 0x58;

	public static final int PBNNB = 0x59;

	public static final int PBNZ = 0x5a;

	public static final int PBNZB = 0x5b;

	public static final int PBNP = 0x5c;

	public static final int PBNPB = 0x5d;

	public static final int PBEV = 0x5e;

	public static final int PBEVB = 0x5f;
	// conditional setting
	public static final int CSN = 0X60;

	public static final int CSNI = 0X61;

	public static final int CSZ = 0X62;

	public static final int CSZI = 0X63;

	public static final int CSP = 0X64;

	public static final int CSPI = 0X65;

	public static final int CSOD = 0X66;

	public static final int CSODI = 0X67;

	public static final int CSNN = 0X68;

	public static final int CSNNI = 0X69;

	public static final int CSNZ = 0X6A;

	public static final int CSNZI = 0X6B;

	public static final int CSNP = 0X6C;

	public static final int CSNPI = 0X6D;

	public static final int CSEV = 0X6E;

	public static final int CSEVI = 0X6F;

	/**
	 * ZERO OR Conditional Set
	 */
	public static final int ZSN = 0X70;

	public static final int ZSNI = 0X71;

	public static final int ZSZ = 0X72;

	public static final int ZSZI = 0X73;

	public static final int ZSP = 0X74;

	public static final int ZSPI = 0X75;

	public static final int ZSOD = 0X76;

	public static final int ZSODI = 0X77;

	public static final int ZSNN = 0X78;

	public static final int ZSNNI = 0X79;

	public static final int ZSNZ = 0X7A;

	public static final int ZSNZI = 0X7B;

	public static final int ZSNP = 0X7C;

	public static final int ZSNPI = 0X7D;

	public static final int ZSEV = 0X7E;

	public static final int ZSEVI = 0X7F;

	// loading
	public static final int LDB = 0X80;

	public static final int LDBI = 0X81;

	public static final int LDBU = 0X82;

	public static final int LDBUI = 0X83;

	public static final int LDW = 0X84;

	public static final int LDWI = 0X85;

	public static final int LDWU = 0X86;

	public static final int LDWUI = 0X87;

	public static final int LDT = 0X88;

	public static final int LDTI = 0X89;

	public static final int LDTU = 0X8a;

	public static final int LDTUI = 0X8b;

	public static final int LDO = 0X8c;

	public static final int LDOI = 0X8d;

	public static final int LDOU = 0X8e;

	public static final int LDOUI = 0X8f;

	// special loading
	public static final int LDSF = 0X90;

	public static final int LDSFI = 0X91;

	public static final int LDHT = 0X92;

	public static final int LDHTI = 0X93;

	public static final int CSWAP = 0X94;

	public static final int CSWAPI = 0X95;

	public static final int LDUNC = 0X96;

	public static final int LDUNCI = 0X97;

	// special
	public static final int LDVTS = 0X98;

	public static final int LDVTSI = 0X99;

	public static final int PRELD = 0X9a;

	public static final int PRELDI = 0X9b;

	public static final int PREGO = 0X9c;

	public static final int REGOI = 0X9d;

	public static final int GO = 0X9e;

	public static final int GOI = 0X9f;

	// storing
	public static final int STB = 0Xa0;

	public static final int STBI = 0Xa1;

	public static final int STBU = 0Xa2;

	public static final int STBUI = 0Xa3;

	public static final int STW = 0Xa4;

	public static final int STWI = 0Xa5;

	public static final int STWU = 0Xa6;

	public static final int STWUI = 0Xa7;

	public static final int STT = 0Xa8;

	public static final int STTI = 0Xa9;

	public static final int STTU = 0Xaa;

	public static final int STTUI = 0Xab;

	public static final int STO = 0Xac;

	public static final int STOI = 0Xad;

	public static final int STOU = 0Xae;

	public static final int STOUI = 0Xaf;

	// =========special store
	public static final int STSF = 0Xb0;

	public static final int STSFI = 0Xb1;

	public static final int STHT = 0Xb2;

	public static final int STHTI = 0Xb3;

	// public static final int STCO = 0Xbe; --bug 6745
	public static final int STCO = 0Xb4;

	public static final int STCOI = 0Xb5;

	public static final int STUNC = 0Xb6;

	public static final int STUNCI = 0Xb7;

	// special

	public static final int SYNCD = 0Xb8;

	public static final int SYNCDI = 0Xb9;

	public static final int PREST = 0Xba;

	public static final int PRESTI = 0Xbb;

	public static final int SYNCID = 0Xbc;

	public static final int SYNCIDI = 0Xbd;

	public static final int PUSHGO = 0Xbe;

	public static final int PUSHGOI = 0Xbf;

	public static final int OR = 0Xc0;

	public static final int ORI = 0Xc1;

	public static final int ORN = 0Xc2;

	public static final int ORNI = 0Xc3;

	public static final int NOR = 0Xc4;

	public static final int NORI = 0Xc5;

	public static final int XOR = 0Xc6;

	public static final int XORI = 0Xc7;

	// special

	public static final int AND = 0Xc8;

	public static final int ANDI = 0Xc9;

	public static final int ANDN = 0Xca;

	public static final int ANDNI = 0Xcb;

	public static final int NAND = 0Xcc;

	public static final int NANDI = 0Xcd;

	public static final int NXOR = 0Xce;

	public static final int NXORI = 0Xcf;

	public static final int BDIF = 0Xd0;

	public static final int BDIFI = 0Xd1;

	public static final int WDIF = 0Xd2;

	public static final int WDIFI = 0Xd3;

	public static final int TDIF = 0Xd4;

	public static final int TDIFI = 0Xd5;

	public static final int ODIF = 0Xd6;

	public static final int ODIFI = 0Xd7;

	// special

	public static final int MUX = 0Xd8;

	public static final int MUXI = 0Xd9;

	public static final int SADD = 0Xda;

	public static final int SADDI = 0Xdb;

	// bytewise
	public static final int MOR = 0Xdc;

	public static final int MORI = 0Xdd;

	public static final int MXOR = 0Xde;

	public static final int MXORI = 0Xdf;

	public static final int SETH = 0Xe0;

	public static final int SETMH = 0Xe1;

	public static final int SETML = 0Xe2;

	public static final int SETL = 0Xe3;

	public static final int INCH = 0Xe4;

	public static final int INCMH = 0Xe5;

	public static final int INCML = 0Xe6;

	public static final int INCL = 0Xe7;

	// special
	// Wyde Immediate
	public static final int ORH = 0Xe8;

	public static final int ORMH = 0Xe9;

	public static final int ORML = 0Xea;

	public static final int ORL = 0Xeb;

	public static final int ANDNH = 0Xec;

	public static final int ANDNMH = 0Xed;

	public static final int ANDNML = 0Xee;

	public static final int ANDNL = 0Xef;

	//

	public static final int JMP = 0Xf0;

	public static final int JMPB = 0Xf1;

	public static final int PUSHJ = 0Xf2;

	public static final int PUSHJB = 0Xf3;

	public static final int GETA = 0Xf4;

	public static final int GETAB = 0Xf5;

	public static final int PUT = 0Xf6;

	public static final int PUTI = 0Xf7;

	// special

	public static final int POP = 0Xf8;

	public static final int RESUME = 0Xf9;

	public static final int UNSAVE = 0Xfa;

	public static final int SAVE = 0Xfb;

	public static final int SYNC = 0Xfc;

	public static final int SWYM = 0Xfd;

	public static final int GET = 0Xfe;

	public static final int TRIP = 0Xff;

	public static final String[] OP_CODE = new String[256];
	static {
		OP_CODE[0x00] = "TRAP";
		OP_CODE[0x01] = "FCMP";
		OP_CODE[0x02] = "FUN";
		OP_CODE[0x03] = "FEQL";
		OP_CODE[0x04] = "FADD";
		OP_CODE[0x05] = "FIX";
		OP_CODE[0x06] = "FSUB";
		OP_CODE[0x07] = "FIXU";

		OP_CODE[0x08] = "FLOT";
		OP_CODE[0x09] = "FLOTI";
		OP_CODE[0x0a] = "FLOTU";
		OP_CODE[0x0b] = "FLOTUI";
		OP_CODE[0x0c] = "SFLOT";
		OP_CODE[0x0d] = "SFLOTI";
		OP_CODE[0x0e] = "SFLOTU";
		OP_CODE[0x0f] = "SFLOTUI";

		OP_CODE[0x10] = "FMUL";
		OP_CODE[0x11] = "FCMPE";
		OP_CODE[0x12] = "FUNE";
		OP_CODE[0x13] = "FEQLE";
		OP_CODE[0x14] = "FDIV";
		OP_CODE[0x15] = "FSQRT";
		OP_CODE[0x16] = "FREM";
		OP_CODE[0x17] = "FINT";

		OP_CODE[0X18] = "MUL";

		OP_CODE[0X19] = "MULI";

		OP_CODE[0X1a] = "MULU";

		OP_CODE[0X1b] = "MULUI";

		OP_CODE[0X1c] = "DIV";

		OP_CODE[0X1d] = "DIVI";

		OP_CODE[0X1e] = "DIVU";

		OP_CODE[0X1f] = "DIVUI";

		OP_CODE[0X20] = "ADD";

		OP_CODE[0X21] = "ADDI";

		OP_CODE[0X22] = "ADDU";

		OP_CODE[0X23] = "ADDUI";

		OP_CODE[0X24] = "SUB";

		OP_CODE[0X25] = "SUBI";

		OP_CODE[0X26] = "SUBU";

		OP_CODE[0X27] = "SUBUI";

		OP_CODE[0X28] = "_2ADDU";

		OP_CODE[0X29] = "_2ADDUI";

		OP_CODE[0X2a] = "_4ADDU";

		OP_CODE[0X2b] = "_4ADDUI";

		OP_CODE[0X2c] = "_8ADDU";

		OP_CODE[0X2d] = "_8ADDUI";

		OP_CODE[0X2e] = "_16ADDU";

		OP_CODE[0X2f] = "_16ADDUI";

		OP_CODE[0X30] = "CMP";

		OP_CODE[0X31] = "CMPI";

		OP_CODE[0X32] = "CMPU";

		OP_CODE[0X33] = "CMPUI";

		OP_CODE[0X34] = "NEG";

		OP_CODE[0X35] = "NEGI";

		OP_CODE[0X36] = "NEGU";

		OP_CODE[0X37] = "NEGUI";

		OP_CODE[0X38] = "SL";

		OP_CODE[0X39] = "SLI";

		OP_CODE[0X3a] = "SLU";

		OP_CODE[0X3b] = "SLUI";

		OP_CODE[0X3c] = "SR";

		OP_CODE[0X3d] = "SRI";

		OP_CODE[0X3e] = "SRU";

		OP_CODE[0X3f] = "SRUI";
		// branch
		OP_CODE[0x40] = "BN";

		OP_CODE[0x41] = "BNB";

		OP_CODE[0x42] = "BZ";

		OP_CODE[0x43] = "BZB";

		OP_CODE[0x44] = "BP";

		OP_CODE[0x45] = "BPB";

		OP_CODE[0x46] = "BOD";

		OP_CODE[0x47] = "BODB";

		OP_CODE[0x48] = "BNN";

		OP_CODE[0x49] = "BNNB";

		OP_CODE[0x4a] = "BNZ";

		OP_CODE[0x4b] = "BNZB";

		OP_CODE[0x4c] = "BNP";

		OP_CODE[0x4d] = "BNPB";

		OP_CODE[0x4e] = "BEV";

		OP_CODE[0x4f] = "BEVB";

		OP_CODE[0x50] = "PBN";

		OP_CODE[0x51] = "PBNB";

		OP_CODE[0x52] = "PBZ";

		OP_CODE[0x53] = "PBZB";

		OP_CODE[0x54] = "PBP";

		OP_CODE[0x55] = "PBPB";

		OP_CODE[0x56] = "PBOD";

		OP_CODE[0x57] = "PBODB";

		OP_CODE[0x58] = "PBNN";

		OP_CODE[0x59] = "PBNNB";

		OP_CODE[0x5a] = "PBNZ";

		OP_CODE[0x5b] = "PBNZB";

		OP_CODE[0x5c] = "PBNP";

		OP_CODE[0x5d] = "PBNPB";

		OP_CODE[0x5e] = "PBEV";

		OP_CODE[0x5f] = "PBEVB";
		// conditional setting
		OP_CODE[0X60] = "CSN";

		OP_CODE[0X61] = "CSNI";

		OP_CODE[0X62] = "CSZ";

		OP_CODE[0X63] = "CSZI";

		OP_CODE[0X64] = "CSP";

		OP_CODE[0X65] = "CSPI";

		OP_CODE[0X66] = "CSOD";

		OP_CODE[0X67] = "CSODI";

		OP_CODE[0X68] = "CSNN";

		OP_CODE[0X69] = "CSNNI";

		OP_CODE[0X6A] = "CSNZ";

		OP_CODE[0X6B] = "CSNZI";

		OP_CODE[0X6C] = "CSNP";

		OP_CODE[0X6D] = "CSNPI";

		OP_CODE[0X6E] = "CSEV";

		OP_CODE[0X6F] = "CSEVI";

		/**
		 * ZERO OR Conditional Set
		 */
		OP_CODE[0X70] = "ZSN";

		OP_CODE[0X71] = "ZSNI";

		OP_CODE[0X72] = "ZSZ";

		OP_CODE[0X73] = "ZSZI";

		OP_CODE[0X74] = "ZSP";

		OP_CODE[0X75] = "ZSPI";

		OP_CODE[0X76] = "ZSOD";

		OP_CODE[0X77] = "ZSODI";

		OP_CODE[0X78] = "ZSNN";

		OP_CODE[0X79] = "ZSNNI";

		OP_CODE[0X7A] = "ZSNZ";

		OP_CODE[0X7B] = "ZSNZI";

		OP_CODE[0X7C] = "ZSNP";

		OP_CODE[0X7D] = "ZSNPI";

		OP_CODE[0X7E] = "ZSEV";

		OP_CODE[0X7F] = "ZSEVI";

		// loading
		OP_CODE[0X80] = "LDB";

		OP_CODE[0X81] = "LDBI";

		OP_CODE[0X82] = "LDBU";

		OP_CODE[0X83] = "LDBUI";

		OP_CODE[0X84] = "LDW";

		OP_CODE[0X85] = "LDWI";

		OP_CODE[0X86] = "LDWU";

		OP_CODE[0X87] = "LDWUI";

		OP_CODE[0X88] = "LDT";

		OP_CODE[0X89] = "LDTI";

		OP_CODE[0X8a] = "LDTU";

		OP_CODE[0X8b] = "LDTUI";

		OP_CODE[0X8c] = "LDO";

		OP_CODE[0X8d] = "LDOI";

		OP_CODE[0X8e] = "LDOU";

		OP_CODE[0X8f] = "LDOUI";

		// special loading
		OP_CODE[0X90] = "LDSF";

		OP_CODE[0X91] = "LDSFI";

		OP_CODE[0X92] = "LDHT";

		OP_CODE[0X93] = "LDHTI";

		OP_CODE[0X94] = "CSWAP";

		OP_CODE[0X95] = "CSWAPI";

		OP_CODE[0X96] = "LDUNC";

		OP_CODE[0X97] = "LDUNCI";

		// special
		OP_CODE[0X98] = "LDVTS";

		OP_CODE[0X99] = "LDVTSI";

		OP_CODE[0X9a] = "PRELD";

		OP_CODE[0X9b] = "PRELDI";

		OP_CODE[0X9c] = "PREGO";

		OP_CODE[0X9d] = "REGOI";

		OP_CODE[0X9e] = "GO";

		OP_CODE[0X9f] = "GOI";

		// storing
		OP_CODE[0Xa0] = "STB";

		OP_CODE[0Xa1] = "STBI";

		OP_CODE[0Xa2] = "STBU";

		OP_CODE[0Xa3] = "STBUI";

		OP_CODE[0Xa4] = "STW";

		OP_CODE[0Xa5] = "STWI";

		OP_CODE[0Xa6] = "STWU";

		OP_CODE[0Xa7] = "STWUI";

		OP_CODE[0Xa8] = "STT";

		OP_CODE[0Xa9] = "STTI";

		OP_CODE[0Xaa] = "STTU";

		OP_CODE[0Xab] = "STTUI";

		OP_CODE[0Xac] = "STO";

		OP_CODE[0Xad] = "STOI";

		OP_CODE[0Xae] = "STOU";

		OP_CODE[0Xaf] = "STOUI";

		// =========special store
		OP_CODE[0Xb0] = "STSF";

		OP_CODE[0Xb1] = "STSFI";

		OP_CODE[0Xb2] = "STHT";

		OP_CODE[0Xb3] = "STHTI";

		OP_CODE[0Xb4] = "STCO";

		OP_CODE[0Xb5] = "STCOI";

		OP_CODE[0Xb6] = "STUNC";

		OP_CODE[0Xb7] = "STUNCI";

		// special

		OP_CODE[0Xb8] = "SYNCD";

		OP_CODE[0Xb9] = "SYNCDI";

		OP_CODE[0Xba] = "PREST";

		OP_CODE[0Xbb] = "PRESTI";

		OP_CODE[0Xbc] = "SYNCID";

		OP_CODE[0Xbd] = "SYNCIDI";

		OP_CODE[0Xbe] = "PUSHGO";

		OP_CODE[0Xbf] = "PUSHGOI";

		OP_CODE[0Xc0] = "OR";

		OP_CODE[0Xc1] = "ORI";

		OP_CODE[0Xc2] = "ORN";

		OP_CODE[0Xc3] = "ORNI";

		OP_CODE[0Xc4] = "NOR";

		OP_CODE[0Xc5] = "NORI";

		OP_CODE[0Xc6] = "XOR";
		OP_CODE[0Xc7] = "XORI";

		// special

		OP_CODE[0Xc8] = "AND";

		OP_CODE[0Xc9] = "ANDI";
		OP_CODE[0Xca] = "ANDN";

		OP_CODE[0Xcb] = "ANDNI";

		OP_CODE[0Xcc] = "NAND";

		OP_CODE[0Xcd] = "NANDI";

		OP_CODE[0Xce] = "NXOR";

		OP_CODE[0Xcf] = "NXORI";

		OP_CODE[0Xd0] = "BDIF";

		OP_CODE[0Xd1] = "BDIFI";

		OP_CODE[0Xd2] = "WDIF";

		OP_CODE[0Xd3] = "WDIFI";

		OP_CODE[0Xd4] = "TDIF";

		OP_CODE[0Xd5] = "TDIFI";

		OP_CODE[0Xd6] = "ODIF";

		OP_CODE[0Xd7] = "ODIFI";

		// special

		OP_CODE[0Xd8] = "MUX";

		OP_CODE[0Xd9] = "MUXI";
		OP_CODE[0Xda] = "SADD";

		OP_CODE[0Xdb] = "SADDI";

		// bytewise
		OP_CODE[0Xdc] = "MOR";

		OP_CODE[0Xdd] = "MORI";

		OP_CODE[0Xde] = "MXOR";

		OP_CODE[0Xdf] = "MXORI";

		OP_CODE[0Xe0] = "SETH";

		OP_CODE[0Xe1] = "SETMH";

		OP_CODE[0Xe2] = "SETML";

		OP_CODE[0Xe3] = "SETL";

		OP_CODE[0Xe4] = "INCH";

		OP_CODE[0Xe5] = "INCMH";

		OP_CODE[0Xe6] = "INCML";

		OP_CODE[0Xe7] = "INCL";

		// special
		// Wyde Immediate
		OP_CODE[0Xe8] = "ORH";

		OP_CODE[0Xe9] = "ORMH";

		OP_CODE[0Xea] = "ORML";

		OP_CODE[0Xeb] = "ORL";

		OP_CODE[0Xec] = "ANDNH";

		OP_CODE[0Xed] = "ANDNMH";

		OP_CODE[0Xee] = "ANDNML";

		OP_CODE[0Xef] = "ANDNL";

		//

		OP_CODE[0Xf0] = "JMP";

		OP_CODE[0Xf1] = "JMPB";

		OP_CODE[0Xf2] = "PUSHJ";

		OP_CODE[0Xf3] = "PUSHJB";

		OP_CODE[0Xf4] = "GETA";

		OP_CODE[0Xf5] = "GETAB";

		OP_CODE[0Xf6] = "PUT";

		OP_CODE[0Xf7] = "PUTI";

		// special

		OP_CODE[0Xf8] = "POP";

		OP_CODE[0Xf9] = "RESUME";

		OP_CODE[0Xfa] = "UNSAVE";

		OP_CODE[0Xfb] = "SAVE";

		OP_CODE[0Xfc] = "SYNC";

		OP_CODE[0Xfd] = "SWYM";

		OP_CODE[0Xfe] = "GET";

		OP_CODE[0Xff] = "TRIP";
	}

	public static String mapToString(int index) {
		return OP_CODE[index];
	}
}
