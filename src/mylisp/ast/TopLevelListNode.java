package mylisp.ast;

import mylisp.eval.Visitor;

public class TopLevelListNode extends ListNode {
	@Override
	public void accept(Visitor visitor) {
		visitor.visitTopLevelListNode(this);
	}
}
