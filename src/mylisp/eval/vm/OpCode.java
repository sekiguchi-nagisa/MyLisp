package mylisp.eval.vm;

public enum OpCode {
	// stack op
	PUSH,	// PUSH const_pool_index
	POP,	// POP
	// arithmetic op
	ADD,	// ADD arg_size
	SUB,	// SUB arg_size
	MUL,	// MUL arg_size
	DIV,	// DIV arg_size
	// binary op
	MOD,	// MOD
	EQ,		// EQ
	NE,		// NE
	LT,		// LT
	LE,		// LE
	GT,		// GT
	GE,		// GE
	// jmp op
	IF_JMP,	// IF_JMP jmp_addr_if_true
	// c
	RET,
	CALL,	// arg_size const_pool_index(function)
	// local variable op
	LOAD,	// LOAD arg_index
	STORE,	// STORE arg_index
	// gobal variable op
	LOAD_G,	// LOAD_G g_arg_index
	STORE_G;	// STORE_G g_arg_index

	private final static int arg_size = 3;
	private int[] args = new int[arg_size];

	public void appendArg(int index, int arg) {
		this.args[index] = arg;
	}

	public int getArg(int index) {
		return this.args[index];
	}
}
