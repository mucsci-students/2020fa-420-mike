package mike.cli;

import mike.HelperMethods;
import mike.datastructures.Model;
import mike.view.CLIView;

public class ListCommand extends CommandObj {

    public ListCommand(Model m, CLIView v, String[] com, boolean p) {
        super(m, v, com, p);
    }

    public boolean execute() {
        if (commands.length < 2) {
            view.printError(errorMessage + commandUsage[21] + commandUsage[22] + commandUsage[23] + "\n");
            return prompt;
        }
        if (commands[1].equals("classes")) {
            if (commands.length != 2) {
                view.printError(errorMessage + commandUsage[21] + "\n");
                return prompt;
            } else {
                System.out.println();
                HelperMethods.listClasses(model);
                System.out.println();
                return prompt;
            }
        } else if (commands[1].equals("relationships")) {
            if (commands.length != 2) {
                view.printError(errorMessage + commandUsage[22] + "\n");
                return prompt;
            } else {
                System.out.println();
                HelperMethods.listRelationships(model);
                System.out.println();
                return prompt;
            }
        } else if (commands[1].equals("all")) {
            if (commands.length != 2) {
                view.printError(errorMessage + commandUsage[23] + "\n");
                return prompt;
            } else {
                System.out.println();
                HelperMethods.listClasses(model);
                System.out.println();
                HelperMethods.listRelationships(model);
                System.out.println();
                return prompt;
            }
        } else {
            view.printError(errorMessage + commandUsage[21] + commandUsage[22] + commandUsage[23] + "\n");
        }
        return prompt;
    }
}
