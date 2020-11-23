package mike.datastructures;

public class Memento {
    private Model m;

    public Memento() {
    }

    public Memento(Model m) {
	set(m);
    }

    public void set(Model m) {
	this.m = m;
    }

    public Model getModel() {
	return this.m;
    }
}