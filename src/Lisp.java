
public class Lisp {
	public static void main(String[] args) {
		//String ex1 = "(+ 1 2 3 4 5 6 (* 1 3))";
		//String ex1 = "(+ 1 2 3 (setq x 4) x x)";
		//String ex1 = "(defun sum (x y)(+ x y)) (sum 1 (sum 1 3))";
		String ex1 = "(defun fib (n) (if (< n 3) 1 (+ (fib (- n 1)) (fib (- n 2))))) (fib 36)";
		Tokenizer tokenizer = new Tokenizer();
		Node node = new Parser(tokenizer.tokenize(ex1)).parse();
		Evaluator evaluator = new Evaluator();
		evaluator.evaluate(node);
	}
}

