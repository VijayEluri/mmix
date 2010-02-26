//1/21/2010 10:05:15 PM
C0	GREG #30
C1	GREG #31
Cons	GREG #3030303030303030
Addr	GREG Data_Segment
	LOC #100
Main	ADD $1,Cons,0 
	GETA $2,1F	
	PUSHJ 0,core
	GETA $255,0F
	TRAP Fwrite,StdOut
	
value 	IS $0 	//value to print
string	IS $1   //the addr. of out put buffer	
core	BEV even
	STO C1,string,0
inc	INCL string,string,1
	SR value,value,1
	JMP core
even	BZ exit
	STO C0,string,0
	JMP inc
exit	POP 0,0

	LOC Data_Segment
0H	OCTA 1F,65			
1H	OCTA #0