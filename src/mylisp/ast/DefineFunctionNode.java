package mylisp.ast;

import mylisp.eval.Visitor;

public class DefineFunctionNode extends BuiltinSymbolNode {
	private static int funcName = 0;
	private static int argList = 1;
	private static int body = 2;

	public DefineFunctionNode(String representSymbol) {
		super(representSymbol);
	}

	public String getFuncName() {
		return this.getParamAt(funcName).getSymbol();
	}

	public ListNode getArgList() {
		return (ListNode) this.getParamAt(argList);
	}

	public ListNode getFuncBody() {
		return (ListNode) this.getParamAt(body);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitFunctionNode(this);
	}
}
