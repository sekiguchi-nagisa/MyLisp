package mylisp.ast;

import mylisp.eval.Visitor;

public class NumberNode extends Node {
	private final long value;

	public static boolean isNumber(String symbol) {
		try {
			Long.parseLong(symbol);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public NumberNode(String symbol) {
		super(symbol);
		this.value = Long.parseLong(this.getSymbol());
	}

	public long getValue() {
		return this.value;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitNumberNode(this);
	}
}
