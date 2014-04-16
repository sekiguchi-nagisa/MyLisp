import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class Lisp {
	public static void main(String[] args) {
		Tokenizer tokenizer = new Tokenizer();
		if(args.length == 0) {
			Console console = new Console();
			while(true) {
				String line = console.readLine();
				if(line.equals("")) {
					continue;
				}
				if(line.equals("exit")) {
					break;
				}
				Node node = new Parser(tokenizer.tokenize(line)).parse();
				Evaluator evaluator = new Evaluator();
				evaluator.evaluate(node);
			}
		} else {
			Node node = new Parser(tokenizer.tokenize(readFile(args[0]))).parse();
			Evaluator evaluator = new Evaluator();
			evaluator.evaluate(node);
		}
	}

	private static String readFile(String fileName) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("file not found: " + fileName);
			System.exit(1);
		}
		ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[256];
			int read;
			while((read = fileInputStream.read(buffer, 0, buffer.length)) > -1) {
				bufferStream.write(buffer, 0, read);
			}
			fileInputStream.close();
			return bufferStream.toString();
		} catch (IOException e) {
			System.err.println("io problem");
			System.exit(1);
		}
		return null;
	}

	private static class Console {
		private final BufferedReader bReader;

		public Console() {
			this.bReader = new BufferedReader(new InputStreamReader(System.in));
		}

		public String readLine() {
			StringBuilder lineBuilder = new StringBuilder();
			String line = this.readLine(">>> ");
			lineBuilder.append(line);
			int level = 0;
			while((level = this.checkBraceLevel(line, level)) > 0) {
				line = this.readLine("    ");
				lineBuilder.append("\n");
				lineBuilder.append(line);
			}
			if(level < 0) {
				System.err.println("... canceled");
				return "";
			}
			return lineBuilder.toString().trim();
		}

		private String readLine(String prompt) {
			System.out.print(prompt);
			try {
				String line = this.bReader.readLine();
				return line;
			} catch (IOException e) {
				System.err.println("io problem");
				System.exit(1);
			}
			return null;
		}

		private int checkBraceLevel(String line, int level) {
			if(line == null) {
				return -1;
			}
			int size = line.length();
			for(int i = 0;i < size; i++) {
				char ch = line.charAt(i);
				if(ch == '(') {
					level++;
				} else if(ch == ')') {
					level--;
				}
			}
			return level;
		}
	}
}
