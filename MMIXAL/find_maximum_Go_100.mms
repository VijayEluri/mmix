	LOC	Data_Segment
X0	IS	@
N	IS	100
x0	GREG	X0
j 	GREG	
m	GREG	
kk	GREG	
xk	GREG	
t	IS 	$255
	LOC	#100
	GREG 	@
GoMax100	SETL 	kk,100<<3
	LDO 	m,x0,kk
	JMP	1F	
3H	LDO	xk,x0,kk
	CMP	t,xk,m
	PBNP	t,5F
4H	SET 	m,xk
1H	SR 	j,kk,3
5H	SUB 	kk,kk,8
	PBP	kk,3B
6H	GO	kk,$0,0
Main	GETA	t,9F;
	TRAP 	0,Fread,StdIn
	SET	$0,N<<3
1H	SR	$2,$0,3;
	STO	$0,X0
	GO	$0,GoMax100
	LDO	$0,X0
	LDO	$3,x0,$0
	SL 	$2,$2,3
	STO  	$1,x0,$0;
	STO 	$3,x0,$2
	SUB	$0,$0,1<<3;
	PBNZ	$0,1B
	GETA	t,9F; TRAP 0,Fwrite,StdOut
	TRAP	0,Halt,0
9H	OCTA	X0+1<<3,N<<3

