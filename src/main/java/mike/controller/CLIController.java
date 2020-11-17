package mike.controller;

import java.io.IOException;
import java.util.ArrayList;

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

import mike.HelperMethods;
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
import mike.datastructures.Memento;
import mike.datastructures.Model;
import mike.view.CLIView;
import mike.view.ViewTemplate;

public class CLIController extends ControllerType {
    private Model model;
    private CLIView view;
    private boolean prompt;
    private Terminal terminal;
    private History history;
    private DefaultParser parser;
    private LineReader savePromptReader;
    private LineReader reader;
    private ArrayList<Memento> mementos;
    protected int currMeme;

    public CLIController(Model model, ViewTemplate view) throws IOException {
	super();
	this.model = model;
	this.view = (CLIView) view.getViewinterface();
	currMeme = 0;
	mementos = new ArrayList<Memento>();
	mementos.add(new Memento(this.model));

	terminal = TerminalBuilder.builder().system(true).build();

	AggregateCompleter completer = new TabCompleter().updateCompleter(model);

	StringsCompleter savePromptCompleter = new StringsCompleter("yes", "no");

	history = new DefaultHistory();

	parser = new DefaultParser();
	parser.setEscapeChars(new char[] {});

	reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).history(history)
		.variable(LineReader.MENU_COMPLETE, true).parser(parser).build();

	savePromptReader = LineReaderBuilder.builder().terminal(terminal).completer(savePromptCompleter)
		.variable(LineReader.MENU_COMPLETE, true).parser(parser).build();
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
	Memento meme = new Memento(new Model(this.model));
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
	    LoadCommand load = new LoadCommand(meme.getModel(), view, commands, prompt, savePromptReader);
	    prompt = load.execute();
	    newMeme(meme);
	    break;
	// Call create class, field, method, or relationship based on length and user
	// input
	case "create":
	    CreateCommand create = new CreateCommand(meme.getModel(), view, commands, prompt);
	    prompt = create.execute();
	    newMeme(meme);
	    break;
	// Call delete class, field, method, or relationship based on length and user
	// input
	case "delete":
	    DeleteCommand delete = new DeleteCommand(meme.getModel(), view, commands, prompt);
	    prompt = delete.execute();
	    newMeme(meme);
	    break;
	// Call rename class, field, or method depending on user input and length
	case "rename":
	    RenameCommand rename = new RenameCommand(meme.getModel(), view, commands, prompt);
	    prompt = rename.execute();
	    newMeme(meme);
	    break;
	case "settype":
	    SettypeCommand settype = new SettypeCommand(meme.getModel(), view, commands, prompt);
	    prompt = settype.execute();
	    newMeme(meme);
	    break;
	case "setvis":
	    SetvisCommand setvis = new SetvisCommand(meme.getModel(), view, commands, prompt);
	    prompt = setvis.execute();
	    newMeme(meme);
	    break;
	// Call list class or relationship based on length and user input
	case "list":
	    ListCommand list = new ListCommand(model, view, commands, prompt);
	    prompt = list.execute();
	    break;
	// Calls clear
	case "clear":
	    MiscCommand clear = new MiscCommand(meme.getModel(), view, commands, prompt, savePromptReader);
	    prompt = clear.execute();
	    newMeme(meme);
	    break;
	// Mostly for testing. Undocumented addition to allow for doing things without
	// prompting.
	case "sudo":
	    MiscCommand sudo = new MiscCommand(model, view, commands, prompt, savePromptReader);
	    prompt = sudo.execute();
	    break;
	// Proper command not detected, print an error
	case "undo":
	    undo();
	    break;
	case "redo":
	    redo();
	    break;
	default:
	    view.printInvalidCommand();
	}
    }

    private void undo() {
	if (currMeme > 0) {
	    --currMeme;
	    this.model = mementos.get(currMeme).getModel();
	    prompt = true;
	} else {
	    System.out.println("No actions to undo.");

	}
    }

    private void redo() {
	if (currMeme < mementos.size() - 1) {
	    ++currMeme;
	    this.model = mementos.get(currMeme).getModel();
	    prompt = true;
	} else {
	    System.out.println("No actions to redo.");
	}
    }

    private void truncateMemes() {
	if (currMeme < mementos.size() - 1) {
	    for (int i = mementos.size() - 1; i > currMeme; --i) {
		mementos.remove(i);
	    }
	}
    }

    private void newMeme(Memento meme) {
	truncateMemes();
	mementos.add(meme);
	this.model = meme.getModel();
	++currMeme;
    }

    public Model getModel() {
	return model;
    }
}
