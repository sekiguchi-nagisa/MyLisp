package mylisp.parser;
import java.util.ArrayList;


public class Tokenizer {
	public ArrayList<String> tokenize(String expression) {
		ArrayList<String> tokenList = new ArrayList<String>();
		int size = expression.length();
		int parenthesisCount = 0;
		TokenBuffer buffer = new TokenBuffer();
		for(int i = 0; i < size; i++) {
			char ch = expression.charAt(i);
			switch(ch) {
			case '\n':
			case '\t':
			case ' ':
				buffer.flush(tokenList);
				break;
			case '(':
				parenthesisCount++;
				buffer.flush(tokenList);
				tokenList.add(Character.toString(ch));
				break;
			case ')':
				parenthesisCount--;
				buffer.flush(tokenList);
				tokenList.add(Character.toString(ch));
				break;
			default:
				buffer.append(ch);
				break;
			}
		}
		if(parenthesisCount != 0) {
			throw new IllegalArgumentException(expression);
		}
		return tokenList;
	}

	private static class TokenBuffer {
		private StringBuilder sBuilder;
		public TokenBuffer() {
			this.sBuilder = null;
		}

		public void append(char ch) {
			if(this.isEmpty()) {
				this.sBuilder = new StringBuilder();
			}
			this.sBuilder.append(ch);
		}

		public void flush(ArrayList<String> tokenList) {
			if(!this.isEmpty()) {
				tokenList.add(this.sBuilder.toString());
				this.sBuilder = null;
			}
		}

		private boolean isEmpty() {
			return this.sBuilder == null;
		}
	}

	public static void main(String[] args) {
		String expression = "(defun fib (n) (if (< n 3) 1 (+ (fib (- n 1)) (fib (- n 2)))))";
		Tokenizer tokennizer = new Tokenizer();
		ArrayList<String> tokenList = tokennizer.tokenize(expression);
		int size = tokenList.size();
		System.out.print("[");
		for(int i = 0; i < size; i++) {
			if(i != 0) {
				System.out.print(", ");
			}
			System.out.print(tokenList.get(i));
		}
		System.out.print("]\n");
	}
}

