package mylisp.ast;

public abstract class BuiltinSymbolNode extends Node {
	public BuiltinSymbolNode(String representSymbol) {
		super(representSymbol);
	}

	public int getParamSize() {
		return this.getParent().getListSize() - 1;
	}

	public Node getParamAt(int index) {
		return this.getParent().getNodeAt(index + 1);
	}
}
