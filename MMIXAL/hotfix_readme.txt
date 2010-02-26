just a pseudo readme file to test branch merging.

some lessons learned in writting mmix program.

LDA means load Address, since it require Y be a register,
we usually let Y be a base register. and write something like
ptop 	GREG @
BUF	
	LDA BUF

Another way is to use GETA. which works like branch instr.
in the inst. there is only relative addr.
such as GETA $255,String
String "agoo"
but when it is executed, Machine will add @ to String and set
it inot $255.