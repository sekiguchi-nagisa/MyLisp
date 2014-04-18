package mylisp.ast;

import java.util.HashMap;

import mylisp.eval.Visitor;

public class BinaryOpNode extends BuiltinSymbolNode {
	private final static int leftNodeIndex = 1;
	private final static int rightNodeIndex = 2;
	
	public final static int MOD_OP = 4;
	public final static int EQ_OP  = 5;
	public final static int NE_OP  = 6;
	public final static int LT_OP  = 7;
	public final static int LE_OP  = 8;
	public final static int GT_OP  = 9;
	public final static int GE_OP  = 10;

	private final static HashMap<String, Integer> binaryOpMap = new HashMap<String, Integer>();
	static {
		binaryOpMap.put("%",  MOD_OP);
		binaryOpMap.put("=",  EQ_OP);
		binaryOpMap.put("!=", NE_OP);
		binaryOpMap.put("<",  LT_OP);
		binaryOpMap.put("<=", LE_OP);
		binaryOpMap.put(">",  GT_OP);
		binaryOpMap.put(">=", GE_OP);
	}

	public static boolean isBinaryOp(String symbol) {
		return binaryOpMap.containsKey(symbol);
	}

	private final int binaryOP;

	public BinaryOpNode(String symbol) {
		super(symbol);
		this.binaryOP = binaryOpMap.get(this.getSymbol());
	}

	public int getOp() {
		return this.binaryOP;
	}

	public Node getLeftNode() {
		return this.getParent().getNodeAt(leftNodeIndex);
	}

	public Node getRightNode() {
		return this.getParent().getNodeAt(rightNodeIndex);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitBinaryOpNode(this);
	}
}
