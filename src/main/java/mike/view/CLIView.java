package mike.view;

import java.io.IOException;

import mike.datastructures.*;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import cli.CreateCommand;
import cli.DeleteCommand;
import cli.ListCommand;
import cli.LoadCommand;
import cli.MiscCommand;
import cli.RenameCommand;
import cli.SaveCommand;
import cli.SettypeCommand;
import cli.SetvisCommand;
import cli.TabCompleter;

public class CLIView implements ViewInterface {
    private Model classes;
    private boolean prompt;
    private Terminal terminal;
    private History history;
    private DefaultParser parser;
    private LineReader savePromptReader;
    private LineReader reader;

    public CLIView(Model classModel) throws IOException {
	// Initialize variables
	this.classes = classModel;
	prompt = false;

	terminal = TerminalBuilder.builder().system(true).build();

	AggregateCompleter completer = new TabCompleter().updateCompleter(classes);

	StringsCompleter savePromptCompleter = new StringsCompleter("yes", "no");

	history = new DefaultHistory();

	parser = new DefaultParser();
	parser.setEscapeChars(new char[] {});

	reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).history(history)
		.variable(LineReader.MENU_COMPLETE, true).parser(parser).build();

	savePromptReader = LineReaderBuilder.builder().terminal(terminal).completer(savePromptCompleter)
		.variable(LineReader.MENU_COMPLETE, true).parser(parser).build();
    }

    public Model getCLIModel() {
	return classes;
    }

    public void begin() {
	System.out.println("Hello, and welcome to Team mike's UML editor.");
	System.out.println("To exit the program, type 'quit'.");
	System.out.println("To see all the commands available, type 'help'.\n");

	while (true) {
	    String line = null;

	    line = reader.readLine("Enter a command: ", "", (MaskingCallback) null, null);
	    line = line.trim();

	    String[] commands = line.split(" ");

	    evaluateCommand(commands);

	    // update completer
	    AggregateCompleter completer = new TabCompleter().updateCompleter(classes);

	    // rebuild reader
	    reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).history(history)
		    .variable(LineReader.MENU_COMPLETE, true).parser(parser).build();
	}
    }

    public void evaluateCommand(String[] commands) {
	switch (commands[0]) {
	case "quit":
	    MiscCommand quit = new MiscCommand(classes, commands, prompt, savePromptReader);
	    prompt = quit.execute();
	    break;
	// Call help
	case "help":
	    MiscCommand help = new MiscCommand(classes, commands, prompt, savePromptReader);
	    prompt = help.execute();
	    break;
	// Call save depending on if pathname was specified or not
	case "save":
	    SaveCommand save = new SaveCommand(classes, commands, prompt);
	    prompt = save.execute();
	    break;
	// Call load given a directory+filename
	case "load":
	    LoadCommand load = new LoadCommand(classes, commands, prompt, savePromptReader);
	    prompt = load.execute();
	    break;
	// Call create class, field, method, or relationship based on length and user
	// input
	case "create":
	    CreateCommand create = new CreateCommand(classes, commands, prompt);
	    prompt = create.execute();
	    break;
	// Call delete class, field, method, or relationship based on length and user
	// input
	case "delete":
	    DeleteCommand delete = new DeleteCommand(classes, commands, prompt);
	    prompt = delete.execute();
	    break;
	// Call rename class, field, or method depending on user input and length
	case "rename":
	    RenameCommand rename = new RenameCommand(classes, commands, prompt);
	    prompt = rename.execute();
	    break;
	case "settype":
	    SettypeCommand settype = new SettypeCommand(classes, commands, prompt);
	    prompt = settype.execute();
	    break;
	case "setvis":
	    SetvisCommand setvis = new SetvisCommand(classes, commands, prompt);
	    prompt = setvis.execute();
	    break;
	// Call list class or relationship based on length and user input
	case "list":
	    ListCommand list = new ListCommand(classes, commands, prompt);
	    prompt = list.execute();
	    break;
	// Calls clear
	case "clear":
	    MiscCommand clear = new MiscCommand(classes, commands, prompt, savePromptReader);
	    prompt = clear.execute();
	    break;
	// Mostly for testing. Undocumented addition to allow for doing things without
	// prompting.
	case "sudo":
	    MiscCommand sudo = new MiscCommand(classes, commands, prompt, savePromptReader);
	    prompt = sudo.execute();
	    break;
	// Proper command not detected, print an error
	default:
	    System.out.println("\nInvalid command.\nType help to see a list of all commands.\n");
	}
    }
    
}
