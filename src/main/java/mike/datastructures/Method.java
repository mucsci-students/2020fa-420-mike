package mike.datastructures;

import java.util.ArrayList;

public class Method extends Formal{
    private ArrayList<Parameter> parameters;

    public Method(String name, String type){
        super(name, type);
        parameters = new ArrayList<Parameter>();
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    /* CREATE, RENAME, DELETE parameter methods */

    public boolean createParameter(String name, String type){
        if(searchParameter(name)){
            return false; //parameter already exists
        }
        return parameters.add(new Parameter(name, type));
    }

    public boolean renameParameter(String name, String newname){
        if(searchParameter(newname)) {
            //parameter with new name already exists
            return false;
        }
        Parameter p;
        for (int index = 0; index < parameters.size(); ++index) {
            p = parameters.get(index);
            // If target found.
            if (p.getName().equals(name)) {
                //rename method.
                p.setName(newname);
                return true;
            }
        }
        // Target not found.
        return false;
    }

    public boolean deleteParameter(String name){
        return parameters.remove(copyParameter(name));
    }

    /* helper functions */
    public boolean searchParameter(String name){
        for (Parameter p : parameters){
            if(p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /* copy a parameter and its properties */
    public Parameter copyParameter(String name){
        for(Parameter p : parameters){
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

}
