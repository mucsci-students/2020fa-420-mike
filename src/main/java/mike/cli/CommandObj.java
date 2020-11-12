package mike.cli;

import mike.datastructures.Model;
import mike.view.CLIView;

public abstract class CommandObj {
    
    protected Model model;
    protected CLIView view;
    protected String[] commands;
    protected String errorMessage = "Error in parsing command. Proper command usage is: ";
    protected String[] commandUsage;
    protected static boolean prompt;

    CommandObj(Model m, CLIView view, String[] com, boolean p) {
	this.model = m;
	this.view = view;
	this.commands = com;
	commandUsage = getCommandUsage();
	prompt = p;
    }
    
    private static String[] getCommandUsage() {
	String[] commandUsage = { "\n  save <name>.json (optional <path>)", "\n  load <path>",
		"\n  create class <name>", "\n  create field <class name> <field visibility> <field type> <field name>",
		"\n  create method <class name> <method visibility> <method type> <method name>",
		"\n  create relationship <type> <class name1> <class name2>",
		"\n  create parameter <class name> <method> <parameter type> <parameter name>",
		"\n  delete class <name>", "\n  delete field <class name> <field name>",
		"\n  delete method <class name> <method name>",
		"\n  delete relationship <type> <class name1> <class name2>",
		"\n  delete parameter <class name> <method name>, <parameter name>",
		"\n  rename class <name> <newname>", "\n  rename field <class name> <field name> <newname>",
		"\n  rename method <class name> <method name> <newname>",
		"\n  rename parameter <class name> <method name> <parameter name> <parameter newname>",
		"\n  settype field <class name> <field name> <newtype>",
		"\n  settype method <class name> <method name> <newtype>",
		"\n  settype parameter <class name> <method name> <parameter name> <newtype>",
		"\n  setvis field <class name> <field name> <visibility>",
		"\n  setvis method <class name> <method name> <visibility>", "\n  list classes",
		"\n  list relationships", "\n  list all", "\n  clear" };
	return commandUsage;
    }
    
    protected abstract boolean execute();
}
