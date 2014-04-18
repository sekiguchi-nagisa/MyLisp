package mylisp.ast;

import mylisp.eval.Visitor;

public class VariableDeclNode extends BuiltinSymbolNode {
	private final static int varName = 0;
	private final static int varValue = 1;
	public VariableDeclNode(String representSymbol) {
		super(representSymbol);
	}

	public String getVariableName() {
		return this.getParamAt(varName).getSymbol();
	}

	public Node getVarValueNode() {
		return this.getParamAt(varValue);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitVariableDeclNode(this);
	}
}
