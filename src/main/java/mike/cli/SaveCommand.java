package mike.cli;

import java.io.File;
import java.io.IOException;

import mike.HelperMethods;
import mike.datastructures.Model;
import mike.view.CLIView;

public class SaveCommand extends CommandObj {

    private File file;

    public SaveCommand(Model m, CLIView v, String[] com, boolean p, File file) {
        super(m, v, com, p);
        this.file = file;
    }

    public boolean execute() {
        if (commands.length == 2) {
        	//save to new specified file path
            try {
                file = new File(commands[1]);
                HelperMethods.save(file, model);
                System.out.println("File saved at: " + file.getAbsolutePath());
            } catch (IOException e) {
                view.printError("Failed to parse directory. Exiting.");
                return prompt;
            }
        } else if(commands.length == 1) {
        	if(file == null) {
        		System.out.println("\nSpecify a file path to save to. Proper command usage is: " + commandUsage[0] + "\n");
        		return prompt;
			}
        	//save to current file
			try {
				HelperMethods.save(file, model);
				System.out.println("File saved at: " + file.getAbsolutePath());
			} catch (IOException e) {
				view.printError("Failed to parse directory. Exiting.");
				return prompt;
			}
		} else {
            view.printError(errorMessage + commandUsage[0] + "\n");
            return prompt;
        }
        return false;
    }

    public File getFile() {
        return file;
    }

}
