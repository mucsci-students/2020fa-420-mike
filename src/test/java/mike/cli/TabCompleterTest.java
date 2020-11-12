package mike.cli;

import org.junit.Test;

import mike.datastructures.Model;
import mike.datastructures.Relationship.Type;

public class TabCompleterTest {
    
    @Test
    public void updateTest() {
	Model model = new Model();
	TabCompleter tab = new TabCompleter();
	
	model.createClass("c1");
	model.createClass("c2");
	model.createField("c1", "f1", "int", "private");
	model.createMethod("c1", "m1", "int", "public");
	model.createParameter("c1", "m1", "int", "p1");
	model.createRelationship(Type.AGGREGATION, "c1", "c2");
	tab.updateCompleter(model);
    }
}
