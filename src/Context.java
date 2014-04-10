import java.util.HashMap;

public interface Context {
	public Context getParentContext();
}


class GlobalContext implements Context {
	private final HashMap<String, FunctionNode> funcMap;
	private final HashMap<String, Object> globalVarMap;

	public GlobalContext() {
		this.funcMap = new HashMap<String, FunctionNode>();
		this.globalVarMap = new HashMap<String, Object>();
	}

	public void setFuncNode(String symbol, FunctionNode funcNode) {
		if(this.funcMap.containsKey(symbol)) {
			throw new RuntimeError("duplicated function: " + symbol);
		}
		this.funcMap.put(symbol, funcNode);
	}

	public FunctionNode getFuncNode(String symbol) {
		FunctionNode node = this.funcMap.get(symbol);
		if(node == null) {
			throw new RuntimeError("undefined function: " + symbol);
		}
		return node;
	}

	public boolean containFunction(String symbol) {
		return this.funcMap.get(symbol) != null;
	}

	public void setVariable(String symbol, Object value) {
		this.globalVarMap.put(symbol, value);
	}

	public Object getVariable(String symbol) {
		Object value = this.globalVarMap.get(symbol);
		if(value == null) {
			throw new RuntimeError("undefined variable: " + symbol);
		}
		return value;
	}

	public boolean containVariable(String symbol) {
		return this.globalVarMap.get(symbol) != null;
	}
	@Override
	public Context getParentContext() {
		return null;
	}
}