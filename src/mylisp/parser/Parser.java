package mylisp.parser;

import java.util.ArrayList;

import mylisp.ast.BasicArithmeticOpNode;
import mylisp.ast.BinaryOpNode;
import mylisp.ast.DefineFunctionNode;
import mylisp.ast.EndOfListNode;
import mylisp.ast.IfNode;
import mylisp.ast.ListNode;
import mylisp.ast.Node;
import mylisp.ast.NumberNode;
import mylisp.ast.SymbolNode;
import mylisp.ast.TopLevelListNode;
import mylisp.ast.VariableDeclNode;
import mylisp.eval.RuntimeError;


public class Parser {
	private final ArrayList<String> tokenList;
	private final int size;
	private int index;

	public Parser(ArrayList<String> tokenList) {
		this.tokenList = tokenList;
		this.size = this.tokenList.size();
		this.index = -1;
	}

	public Node parse() {
		if(this.index == this.size) {
			throw new RuntimeError("parsing error, index == size");
		}
		if(this.index == -1) {
			this.index++;
			TopLevelListNode listNode = new TopLevelListNode();
			while(this.index < this.size) {
				listNode.appendNode(this.parse());
			}
			return listNode;
		}
		String token = this.tokenList.get(this.index++);
		if(token.equals("(")) {
			ListNode node = new ListNode();
			while(true) {
				Node parsedNode = this.parse();
				if(parsedNode instanceof EndOfListNode) {
					break;
				}
				node.appendNode(parsedNode);
			}
			return node;
		} else if(token.equals(")")) {
			return new EndOfListNode();
		} else if(NumberNode.isNumber(token)) {
			return new NumberNode(token);
		} else if(BinaryOpNode.isBinaryOp(token)) {
			return new BinaryOpNode(token);
		} else if(BasicArithmeticOpNode.isBasicOp(token)) {
			return new BasicArithmeticOpNode(token);
		} else if(token.equals("defun")) {
			return new DefineFunctionNode(token);
		} else if(token.equals("setq")) {
			return new VariableDeclNode(token);
		} else if(token.equals("if")) {
			return new IfNode(token);
		}
		return new SymbolNode(token);
	}

	public static void main(String[] args) {
		//String ex1 = "(+ 1 (* 13 3))";
		String ex1 = "(+ 1 2 3 4 5 6 (* 1 3))";
		Tokenizer tokenizer = new Tokenizer();
		Node node = new Parser(tokenizer.tokenize(ex1)).parse();
		System.out.println(node);
	}
}

