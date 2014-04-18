package mylisp.eval.vm;

import mylisp.eval.RuntimeError;

public class ByteCodeInterpreter {
	private final static int defaultStackSize = 1024 * 1024;

	public static void startInterpreter(OpCode[] codes, VMContext vContext) {
		int[] operandStack = new int[defaultStackSize];
		int stackTop = -1;
		int pc = 0;
		while(stackTop < defaultStackSize) {
			OpCode code = codes[pc];
			switch(code) {
			case PUSH:
				break;
			case POP:
				break;
			case ADD:
				break;
			case SUB:
				break;
			case MUL:
				break;
			case DIV:
				break;
			case MOD:
				break;
			case EQ:
				break;
			case NE:
				break;
			case LT:
				break;
			case LE:
				break;
			case GT:
				break;
			case GE:
				break;
			case IF_JMP:
				break;
			case RET:
				break;
			case CALL:
				break;
			case LOAD:
				break;
			case STORE:
				break;
			case LOAD_G:
				break;
			case STORE_G:
				break;
			default:
				break;
			}
		}
		throw new RuntimeError("stack overflow");
	}
}

