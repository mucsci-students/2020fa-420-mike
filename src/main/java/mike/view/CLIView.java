package mike.view;

public class CLIView implements ViewInterface {

    public CLIView(){
    }
    
    public void printIntro()
    {
	System.out.println("Hello, and welcome to Team mike's UML editor.");
	System.out.println("To exit the program, type 'quit'.");
	System.out.println("To see all the commands available, type 'help'.\n");
    }
    
    public void printInvalidCommand() {
	 System.out.println("\nInvalid command.\nType help to see a list of all commands.\n");
    }
    
    public void printError(String e)
    {
	System.out.println("\nERROR: " + e);
    }
}
