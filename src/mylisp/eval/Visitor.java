package mylisp.eval;

import mylisp.ast.BasicArithmeticOpNode;
import mylisp.ast.BinaryOpNode;
import mylisp.ast.DefineFunctionNode;
import mylisp.ast.FuncCallNode;
import mylisp.ast.IfNode;
import mylisp.ast.ListNode;
import mylisp.ast.NumberNode;
import mylisp.ast.SymbolNode;
import mylisp.ast.TopLevelListNode;
import mylisp.ast.VariableDeclNode;

public interface Visitor {
	public void visitListNode(ListNode node);
	public void visitTopLevelListNode(TopLevelListNode node);
	public void visitNumberNode(NumberNode node);
	public void visitBinaryOpNode(BinaryOpNode node);
	public void visitBasicArithmeticOpNode(BasicArithmeticOpNode node);
	public void visitFunctionNode(DefineFunctionNode node);
	public void visitSymbolNode(SymbolNode node);
	public void visitFuncCallNode(FuncCallNode node);
	public void visitVariableDeclNode(VariableDeclNode node);
	public void visitIfNode(IfNode node);
}
