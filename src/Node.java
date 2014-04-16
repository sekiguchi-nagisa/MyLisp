import java.util.ArrayList;
import java.util.HashMap;

interface Visitor {
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

class ListNode extends Node {
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

class TopLevelListNode extends ListNode {
	@Override
	public void accept(Visitor visitor) {
		visitor.visitTopLevelListNode(this);
	}
}

class NumberNode extends Node {
	private final long value;

	public static boolean isNumber(String symbol) {
		try {
			Long.parseLong(symbol);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	public NumberNode(String symbol) {
		super(symbol);
		this.value = Long.parseLong(this.getSymbol());
	}

	public long getValue() {
		return this.value;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitNumberNode(this);
	}
}

abstract class BuiltinSymbolNode extends Node {
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

class BinaryOpNode extends BuiltinSymbolNode {
	private final static int leftNodeIndex = 1;
	private final static int rightNodeIndex = 2;
	
	public final static int MOD_OP = 4;
	public final static int EQ_OP  = 5;
	public final static int NE_OP  = 6;
	public final static int LT_OP  = 7;
	public final static int LE_OP  = 8;
	public final static int GT_OP  = 9;
	public final static int GE_OP  = 10;

	private final static HashMap<String, Integer> binaryOpMap = new HashMap<String, Integer>();
	static {
		binaryOpMap.put("%",  MOD_OP);
		binaryOpMap.put("=",  EQ_OP);
		binaryOpMap.put("!=", NE_OP);
		binaryOpMap.put("<",  LT_OP);
		binaryOpMap.put("<=", LE_OP);
		binaryOpMap.put(">",  GT_OP);
		binaryOpMap.put(">=", GE_OP);
	}

	public static boolean isBinaryOp(String symbol) {
		return binaryOpMap.containsKey(symbol);
	}

	private final int binaryOP;

	public BinaryOpNode(String symbol) {
		super(symbol);
		this.binaryOP = binaryOpMap.get(this.getSymbol());
	}

	public int getOp() {
		return this.binaryOP;
	}

	public Node getLeftNode() {
		return this.getParent().getNodeAt(leftNodeIndex);
	}

	public Node getRightNode() {
		return this.getParent().getNodeAt(rightNodeIndex);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitBinaryOpNode(this);
	}
}

class BasicArithmeticOpNode extends BuiltinSymbolNode {	public final static int ADD_OP = 0;
	public final static int SUB_OP = 1;
	public final static int MUL_OP = 2;
	public final static int DIV_OP = 3;

	private final static HashMap<String, Integer> basicOpMap = new HashMap<String, Integer>();
	static {
		basicOpMap.put("+",  ADD_OP);
		basicOpMap.put("-",  SUB_OP);
		basicOpMap.put("*",  MUL_OP);
		basicOpMap.put("/",  DIV_OP);
	}

	public static boolean isBasicOp(String symbol) {
		return basicOpMap.containsKey(symbol);
	}

	private final int basicOp;

	public BasicArithmeticOpNode(String symbol) {
		super(symbol);
		this.basicOp = basicOpMap.get(this.getSymbol());
	}

	public int getOp() {
		return this.basicOp;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitBasicArithmeticOpNode(this);
	}
}

class DefineFunctionNode extends BuiltinSymbolNode {
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

class SymbolNode extends Node {
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

class FuncCallNode extends BuiltinSymbolNode {
	public FuncCallNode(String representSymbol) {
		super(representSymbol);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitFuncCallNode(this);
	}
}

class VariableDeclNode extends BuiltinSymbolNode {
	private final static int varName = 0;
	private final static int varValue = 1;
	public VariableDeclNode(String representSymbol) {
		super(representSymbol);
	}

	public String getVariableName() {
		return this.getParamAt(varName).getSymbol();
	}

	public Node getVarValueNode() {
		return this.getParamAt(varValue);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitVariableDeclNode(this);
	}
}

class IfNode extends BuiltinSymbolNode {
	private final static int cond = 0;
	private final static int then = 1;
	private final static int otherwise = 2;
	public IfNode(String representSymbol) {
		super(representSymbol);
	}

	public Node getConditionNode() {
		return this.getParamAt(cond);
	}

	public Node getThenNode() {
		return this.getParamAt(then);
	}

	public Node getElseNode() {
		return this.getParamAt(otherwise);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitIfNode(this);
	}
}

class EndOfListNode extends Node {
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

class RuntimeErrorNode extends Node {
	private final RuntimeError error;
	public RuntimeErrorNode(String message) {
		super("$Error$");
		this.error = new RuntimeError(message);
	}

	@Override
	public void accept(Visitor visitor) {
		throw this.error;
	}
}

class RuntimeError extends Error {
	private static final long serialVersionUID = -3376961271132050213L;
	public RuntimeError(String message) {
		super(message);
	}
}