package mike;

import mike.gui.Controller;

import java.io.IOException;

import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal;

public class Main {
	static Controller controller;

	public static void main(String[] args) throws IOException {

		Terminal terminal = TerminalBuilder.builder().system(true).build();

		Completer completer = new StringsCompleter("create", "rename", "delete", "save", "load");

		DefaultParser parser = new DefaultParser();
		parser.setEscapeChars(new char[] {});

		LineReader reader = LineReaderBuilder.builder().terminal(terminal).completer(completer)
				.variable(LineReader.MENU_COMPLETE, true).parser(parser).build();

		while (true) {
			String line = null;
			try {
				line = reader.readLine("UML> ", "", (MaskingCallback) null, null);
				line = line.trim();
				terminal.writer().println("  You entered: " + line);
				terminal.flush();
				if (line.equals("quit")) {
					break;
				}
			} catch (Exception e) {

			}
		}

		/*
		 * 
		 * 
		 * 
		 * if (args.length == 1 && args[0].equals("cli")) { controller = new
		 * Controller(View.InterfaceType.CLI); } else if (args.length == 0) { controller
		 * = new Controller(View.InterfaceType.GUI); } else { System.out.println(
		 * "Invalid input. Enter 'cli' for the command line interface, or enter nothing for the gui."
		 * ); }
		 * 
		 */
	}
}
