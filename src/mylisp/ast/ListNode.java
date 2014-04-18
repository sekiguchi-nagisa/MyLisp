package mylisp.ast;

import java.util.ArrayList;

import mylisp.eval.Visitor;

public class ListNode extends Node {
	private final ArrayList<Node> nodeList;

	public ListNode() {
		super("$List$");
		this.nodeList = new ArrayList<Node>();
	}

	public void appendNode(Node node) {
		node.setIndex(this.nodeList.size());
		node.setParent(this);
		this.nodeList.add(node);
	}

	public Node getNodeAt(int index) {
		return this.nodeList.get(index);
	}

	public int getListSize() {
		return this.nodeList.size();
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitListNode(this);
	}
}
