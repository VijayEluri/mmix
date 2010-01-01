	LOC	Data_Segment
X0	IS	@
N	IS	128
x0	GREG	X0
j 	IS	$0
m	IS	$1
kk	IS	$2
xk	IS 	$3
t	IS 	$255
	LOC	#100
Maximum	SL 	kk,$0,3
	LDO 	m,x0,kk
	JMP	DecrK
LOOP	LDO	xk,x0,kk
	CMP	t,xk,m
	PBNP	t,DecrK
ChangeM	SET 	m,xk
	SR 	j,kk,3
DecrK	SUB 	kk,kk,8
	PBP	kk,LOOP
	POP 	2,0
	GETA	t,9F;
	TRAP 	0,Fread,StdIn
Main	SETL	$0,N<<3
	SETML	$0,0
	PUSHJ	$1,INIT
1H	SR	$2,$0,3;
	PUSHJ	$1,Maximum
	LDO	$3,x0,$0
	SL 	$2,$2,3
	STO  	$1,x0,$0;
	STO 	$3,x0,$2
	SUB	$0,$0,1<<3;
	PBNZ	$0,1B
	GETA	t,9F; TRAP 0,Fwrite,StdOut
	TRAP	0,Halt,0
INIT	SETL 	$3,N;
Loop2	CMPU	$1,$0,$3	//i for $0
	BNN	$1,2F;
	8ADDU	$2,$0,x0;//addr.
	NEGU	$4,0,$0
	ADDU	$4,$4,$3
	STO	$4,$2,0
	INCL	$0,1;
	JMP	Loop2
2H	POP 0,0
9H	OCTA	X0+1<<3,N<<3
	LOC	Data_Segment
	OCTA	#303030303030303030,#3131313131313131,#3232323232323232;
	OCTA	15,18,3,89,67,45,56,67
	OCTA	#333333333333333313

