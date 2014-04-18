package mylisp.ast;

import java.util.HashMap;

import mylisp.eval.Visitor;

public class BasicArithmeticOpNode extends BuiltinSymbolNode {
	public final static int ADD_OP = 0;
	public final static int SUB_OP = 1;
	public final static int MUL_OP = 2;
	public final static int DIV_OP = 3;

	private final static HashMap<String, Integer> basicOpMap = new HashMap<String, Integer>();
	static {
		basicOpMap.put("+",  ADD_OP);
		basicOpMap.put("-",  SUB_OP);
		basicOpMap.put("*",  MUL_OP);
		basicOpMap.put("/",  DIV_OP);
	}

	public static boolean isBasicOp(String symbol) {
		return basicOpMap.containsKey(symbol);
	}

	private final int basicOp;

	public BasicArithmeticOpNode(String symbol) {
		super(symbol);
		this.basicOp = basicOpMap.get(this.getSymbol());
	}

	public int getOp() {
		return this.basicOp;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitBasicArithmeticOpNode(this);
	}
}
