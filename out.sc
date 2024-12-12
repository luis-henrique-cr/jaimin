#
		ORG
		LOAD #2
		SUBD #3
		JPOS label1
		LOAD #5
		SUBD #2
		JPOS label3
		JUMP label2
label3  
		ECHO "Certo!"
		JUMP label2
label1  
		ECHO "Errado!"
label2  
		ECHO "Teste fora da estrutura de decisão"
		RETN
#