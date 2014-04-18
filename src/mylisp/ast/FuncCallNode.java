package mylisp.ast;

import mylisp.eval.Visitor;

public class FuncCallNode extends BuiltinSymbolNode {
	public FuncCallNode(String representSymbol) {
		super(representSymbol);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitFuncCallNode(this);
	}
}
