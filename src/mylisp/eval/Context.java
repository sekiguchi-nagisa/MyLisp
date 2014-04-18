package mylisp.eval;
import java.util.HashMap;

import mylisp.ast.DefineFunctionNode;

public interface Context {
	public Context getParentContext();
	public Object getVariable(String symbol);
	public void addVariable(String symbol, Object value);
	public void updateVariable(String symbol, Object value);
	public boolean containVariable(String symbol);
}

class GlobalContext implements Context {
	private final HashMap<String, DefineFunctionNode> funcMap;
	private final HashMap<String, Object> globalVarMap;

	public GlobalContext() {
		this.funcMap = new HashMap<String, DefineFunctionNode>();
		this.globalVarMap = new HashMap<String, Object>();
	}

	public void setFuncNode(String symbol, DefineFunctionNode funcNode) {
		if(this.funcMap.containsKey(symbol)) {
			throw new RuntimeError("duplicated function: " + symbol);
		}
		this.funcMap.put(symbol, funcNode);
	}

	public DefineFunctionNode getFuncNode(String symbol) {
		DefineFunctionNode node = this.funcMap.get(symbol);
		if(node == null) {
			throw new RuntimeError("undefined function: " + symbol);
		}
		return node;
	}

	public boolean containFunction(String symbol) {
		return this.funcMap.get(symbol) != null;
	}

	@Override
	public Context getParentContext() {
		return null;
	}

	@Override
	public Object getVariable(String symbol) {
		Object value = this.globalVarMap.get(symbol);
		if(value == null) {
			throw new RuntimeError("undefined variable: " + symbol);
		}
		return value;
	}

	@Override
	public void addVariable(String symbol, Object value) {
		this.globalVarMap.put(symbol, value);
	}

	@Override
	public void updateVariable(String symbol, Object value) {
		if(!this.containVariable(symbol)) {
			throw new RuntimeError("undefined variable: " + symbol);
		}
		this.globalVarMap.put(symbol, value);
	}

	@Override
	public boolean containVariable(String symbol) {
		return this.globalVarMap.get(symbol) != null;
	}
}