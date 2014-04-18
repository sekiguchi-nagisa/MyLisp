package mylisp.ast;

import mylisp.eval.Visitor;

public class IfNode extends BuiltinSymbolNode {
	private final static int cond = 0;
	private final static int then = 1;
	private final static int otherwise = 2;
	public IfNode(String representSymbol) {
		super(representSymbol);
	}

	public Node getConditionNode() {
		return this.getParamAt(cond);
	}

	public Node getThenNode() {
		return this.getParamAt(then);
	}

	public Node getElseNode() {
		return this.getParamAt(otherwise);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitIfNode(this);
	}
}