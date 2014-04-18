package mylisp.eval;

public class RuntimeError extends Error {
	private static final long serialVersionUID = -3376961271132050213L;
	public RuntimeError(String message) {
		super(message);
	}
}
