package mylisp.ast;

import mylisp.eval.Visitor;

public abstract class Node {
	private ListNode parentNode;
	private int indexOfList;
	private final String symbol;
	private String representString;

	public Node(String representSymbol) {
		this.symbol =  representSymbol;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setParent(ListNode parentNode) {
		this.parentNode = parentNode;
	}

	public ListNode getParent() {
		return this.parentNode;
	}

	public void setIndex(int index) {
		this.indexOfList = index;
	}

	public int getIndex() {
		return this.indexOfList;
	}

	@Override
	public String toString() {
		if(this.representString == null) {
			this.representString = this.getClass().getSimpleName() + "(" + this.symbol + ")";
		}
		return this.representString;
	}

	public abstract void accept(Visitor visitor);
}
