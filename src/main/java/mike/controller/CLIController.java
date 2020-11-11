package mike.controller;

import java.nio.file.Path;

import javax.swing.JLabel;

import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.terminal.Terminal;

import mike.cli.CreateCommand;
import mike.cli.DeleteCommand;
import mike.cli.ListCommand;
import mike.cli.LoadCommand;
import mike.cli.MiscCommand;
import mike.cli.RenameCommand;
import mike.cli.SaveCommand;
import mike.cli.SettypeCommand;
import mike.cli.SetvisCommand;
import mike.cli.TabCompleter;
import mike.datastructures.Model;
import mike.view.CLIView;
import mike.view.ViewTemplate;

public class CLIController extends ControllerType {
    Model model;
    CLIView view;
    private boolean prompt;
    private Terminal terminal;
    private History history;
    private DefaultParser parser;
    private LineReader savePromptReader;
    private LineReader reader;

    public CLIController(Model model, ViewTemplate view) {
	super();
	this.model = model;
	this.view = (CLIView) view.getViewinterface();
    }

    public void init() {
	view.printIntro();

	while (true) {
	    String line = null;

	    line = reader.readLine("Enter a command: ", "", (MaskingCallback) null, null);
	    line = line.trim();

	    String[] commands = line.split(" ");

	    evaluateCommand(commands);

	    // update completer
	    AggregateCompleter completer = new TabCompleter().updateCompleter(model);

	    // rebuild reader
	    reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).history(history)
		    .variable(LineReader.MENU_COMPLETE, true).parser(parser).build();
	}
    }

    public void evaluateCommand(String[] commands) {
	switch (commands[0]) {
	case "quit":
	    MiscCommand quit = new MiscCommand(model, view, commands, prompt, savePromptReader);
	    prompt = quit.execute();
	    break;
	// Call help
	case "help":
	    MiscCommand help = new MiscCommand(model, view, commands, prompt, savePromptReader);
	    prompt = help.execute();
	    break;
	// Call save depending on if pathname was specified or not
	case "save":
	    SaveCommand save = new SaveCommand(model, view, commands, prompt);
	    prompt = save.execute();
	    break;
	// Call load given a directory+filename
	case "load":
	    LoadCommand load = new LoadCommand(model, view, commands, prompt, savePromptReader);
	    prompt = load.execute();
	    break;
	// Call create class, field, method, or relationship based on length and user
	// input
	case "create":
	    CreateCommand create = new CreateCommand(model, view, commands, prompt);
	    prompt = create.execute();
	    break;
	// Call delete class, field, method, or relationship based on length and user
	// input
	case "delete":
	    DeleteCommand delete = new DeleteCommand(model, view, commands, prompt);
	    prompt = delete.execute();
	    break;
	// Call rename class, field, or method depending on user input and length
	case "rename":
	    RenameCommand rename = new RenameCommand(model, view, commands, prompt);
	    prompt = rename.execute();
	    break;
	case "settype":
	    SettypeCommand settype = new SettypeCommand(model, view, commands, prompt);
	    prompt = settype.execute();
	    break;
	case "setvis":
	    SetvisCommand setvis = new SetvisCommand(model, view, commands, prompt);
	    prompt = setvis.execute();
	    break;
	// Call list class or relationship based on length and user input
	case "list":
	    ListCommand list = new ListCommand(model, view, commands, prompt);
	    prompt = list.execute();
	    break;
	// Calls clear
	case "clear":
	    MiscCommand clear = new MiscCommand(model, view, commands, prompt, savePromptReader);
	    prompt = clear.execute();
	    break;
	// Mostly for testing. Undocumented addition to allow for doing things without
	// prompting.
	case "sudo":
	    MiscCommand sudo = new MiscCommand(model, view, commands, prompt, savePromptReader);
	    prompt = sudo.execute();
	    break;
	// Proper command not detected, print an error
	default:
	    view.printInvalidCommand();
	}
    }

    public Model getModel() {
	return model;
    }
}
