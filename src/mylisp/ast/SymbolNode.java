package mylisp.ast;

import mylisp.eval.Visitor;

public class SymbolNode extends Node {
	public SymbolNode(String representSymbol) {
		super(representSymbol);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitSymbolNode(this);
	}

	public FuncCallNode toFuncCallNode() {
		FuncCallNode node =  new FuncCallNode(this.getParent().getNodeAt(0).getSymbol());
		node.setParent(this.getParent());
		return node;
	}
}
