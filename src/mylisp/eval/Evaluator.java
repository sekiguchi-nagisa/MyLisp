package mylisp.eval;

import java.util.HashMap;
import java.util.Stack;

import mylisp.ast.BasicArithmeticOpNode;
import mylisp.ast.BinaryOpNode;
import mylisp.ast.BuiltinSymbolNode;
import mylisp.ast.DefineFunctionNode;
import mylisp.ast.FuncCallNode;
import mylisp.ast.IfNode;
import mylisp.ast.ListNode;
import mylisp.ast.Node;
import mylisp.ast.NumberNode;
import mylisp.ast.SymbolNode;
import mylisp.ast.TopLevelListNode;
import mylisp.ast.VariableDeclNode;

public class Evaluator implements Visitor {
	private Object evaluatedValue;
	private final GlobalContext context;
	private final Stack<Context> contextStack;

	public Evaluator() {
		this.context = new GlobalContext();
		this.contextStack = new Stack<Context>();
		this.contextStack.push(this.context);
	}

	private Context getLocalContext() {
		return this.contextStack.peek();
	}

	private LocalContext createLocalContext() {
		Context parentContext = this.getLocalContext();
		LocalContext localContext = new LocalContext(parentContext);
		this.contextStack.push(localContext);
		return localContext;
	}

	private void deleteLocalContext() {
		this.contextStack.pop();
		if(this.contextStack.isEmpty()) {
			throw new RuntimeError("invalid pop operation");
		}
	}

	public boolean isFuncCallNode(SymbolNode node) {
		return this.context.containFunction(node.getSymbol());
	}

	@Override
	public void visitNumberNode(NumberNode node) {
		this.evaluatedValue = node.getValue();
	}

	@Override
	public void visitBinaryOpNode(BinaryOpNode node) {
		long leftValue = this.checkTypeRequirement(this.evaluate(node.getLeftNode()), Long.class);
		long rightValue = this.checkTypeRequirement(this.evaluate(node.getRightNode()), Long.class);
		int binaryOp = node.getOp();
		switch(binaryOp) {
		case BinaryOpNode.MOD_OP:
			this.evaluatedValue = (leftValue % rightValue);
			break;
		case BinaryOpNode.EQ_OP:
			this.evaluatedValue = (leftValue == rightValue);
			break;
		case BinaryOpNode.NE_OP:
			this.evaluatedValue = (leftValue != rightValue);
			break;
		case BinaryOpNode.LT_OP:
			this.evaluatedValue = (leftValue < rightValue);
			break;
		case BinaryOpNode.LE_OP:
			this.evaluatedValue = (leftValue <= rightValue);
			break;
		case BinaryOpNode.GT_OP:
			this.evaluatedValue = (leftValue > rightValue);
			break;
		case BinaryOpNode.GE_OP:
			this.evaluatedValue = (leftValue >= rightValue);
			break;
		default:
			this.reportError("undefined binary op: " + node.getSymbol());
			break;
		}
	}

	@Override
	public void visitBasicArithmeticOpNode(BasicArithmeticOpNode node) {
		int size = node.getParamSize();
		long[] values = new long[size];
		for(int i = 0; i < size; i++) {
			values[i] = this.checkTypeRequirement(this.evaluate(node.getParamAt(i)), Long.class);
		}
		int basicOp = node.getOp();
		long result = values[0];
		for(int i = 1; i < size; i++) {
			switch(basicOp) {
			case BasicArithmeticOpNode.ADD_OP:
				result += values[i];
				break;
			case BasicArithmeticOpNode.SUB_OP:
				result -= values[i];
				break;
			case BasicArithmeticOpNode.MUL_OP:
				result *= values[i];
				break;
			case BasicArithmeticOpNode.DIV_OP:
				result /= values[i];
				break;
			default:
				this.reportError("undefined basic op: " + node.getSymbol());
				break;
			}
		}
		this.evaluatedValue = result;
	}

	@SuppressWarnings("unchecked")
	private <T> T checkTypeRequirement(Object value, Class<T> requireClass) {
		if(!requireClass.isInstance(value)) {
			this.reportError("reuqires " + requireClass.getSimpleName() +" value: " + value.getClass().getSimpleName());
		}
		return (T) value;
	}

	public Object evaluate(Node node) {
		node.accept(this);
		return this.evaluatedValue;
	}

	@Override
	public void visitListNode(ListNode node) {
		Node keyNode = node.getNodeAt(0);
		if(keyNode instanceof BuiltinSymbolNode) {
			this.evaluate(node.getNodeAt(0));
		} else if(keyNode instanceof SymbolNode && this.isFuncCallNode((SymbolNode) keyNode)) {
			this.evaluate(keyNode);
		} else {
			int size = node.getListSize();
			for(int i = 0; i < size; i++) {
				this.evaluate(node.getNodeAt(i));
			}
		}
	}

	@Override
	public void visitFunctionNode(DefineFunctionNode node) {
		this.context.setFuncNode(node.getFuncName(), node);
		this.evaluatedValue = node.getFuncName();
	}

	@Override
	public void visitSymbolNode(SymbolNode node) {
		String symbol = node.getSymbol();
		Context localContext = this.getLocalContext();
		if(localContext.containVariable(symbol)) {
			this.evaluatedValue = localContext.getVariable(symbol);
			return;
		}
		if(this.context.containFunction(symbol)) {
			this.evaluatedValue = this.evaluate(node.toFuncCallNode());
			return;
		}
		this.evaluatedValue = this.context.getVariable(symbol);
	}

	@Override
	public void visitFuncCallNode(FuncCallNode node) {
		DefineFunctionNode funcNode = this.context.getFuncNode(node.getSymbol());
		int size = node.getParamSize();
		Object[] values = new Object[size];
		for(int i = 0; i < size; i++) {
			values[i] = this.evaluate(node.getParamAt(i));
		}
		LocalContext localContext = this.createLocalContext();
		for(int i = 0; i < size; i++) {
			String varName = funcNode.getArgList().getNodeAt(i).getSymbol();
			localContext.addVariable(varName, values[i]);
		}
		this.evaluatedValue = this.evaluate(funcNode.getFuncBody());
		this.deleteLocalContext();
	}

	@Override
	public void visitVariableDeclNode(VariableDeclNode node) {
		String symbol = node.getVariableName();
		Object value = this.evaluate(node.getVarValueNode());
		this.getLocalContext().addVariable(symbol, value);
		this.evaluatedValue = value;
	}

	@Override
	public void visitTopLevelListNode(TopLevelListNode node) {
		int size = node.getListSize();
		for(int i = 0; i < size; i++) {
			System.out.println(this.evaluate(node.getNodeAt(i)));
		}
	}

	@Override
	public void visitIfNode(IfNode node) {
		boolean cond = this.checkTypeRequirement(this.evaluate(node.getConditionNode()), Boolean.class);
		this.evaluatedValue = cond ? this.evaluate(node.getThenNode()) : this.evaluate(node.getElseNode());
	}

	public void reportError(String message) {
		throw new RuntimeError(message);
	}
}

class LocalContext implements Context {
	private final Context parentContext;
	private final HashMap<String, Object> variableMap;

	public LocalContext(Context context) {
		this.parentContext = context;
		this.variableMap = new HashMap<String, Object>();
	}

	@Override
	public Context getParentContext() {
		return this.parentContext;
	}

	@Override
	public Object getVariable(String symbol) {
		Object value = this.variableMap.get(symbol);
		if(value == null) {
			return this.getParentContext().getVariable(symbol);
		}
		return value;
	}

	@Override
	public void addVariable(String symbol, Object value) {
		if(this.variableMap.containsKey(symbol)) {
			throw new RuntimeError("duplicated variable: " + symbol);
		}
		this.variableMap.put(symbol, value);
	}

	@Override
	public void updateVariable(String symbol, Object value) {
		if(!this.variableMap.containsKey(symbol)) {
			this.getParentContext().updateVariable(symbol, value);
			return;
		}
		this.variableMap.put(symbol, value);
	}

	@Override
	public boolean containVariable(String symbol) {
		return this.variableMap.containsKey(symbol) ? true : this.getParentContext().containVariable(symbol);
	}
}
