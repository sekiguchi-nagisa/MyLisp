package mylisp.ast;

import mylisp.eval.Visitor;

public class EndOfListNode extends Node {
	public static class EndOfListError extends Error {
		private static final long serialVersionUID = 8060527838887003825L;
	}

	public EndOfListNode() {
		super("$EndOfList$");
	}

	@Override
	public void accept(Visitor visitor) {
		throw new EndOfListError();
	}
}
