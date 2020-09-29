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
        if(containsParameter(name)){
            return false; //parameter already exists
        }
        return parameters.add(new Parameter(name, type));
    }

    public boolean renameParameter(String name, String newname){
        if(containsParameter(newname)) {
            //parameter with new name already exists
            return false;
        }

        for (Parameter p : parameters) {
            // If target found.
            if (p.getName().equals(name)) {
                //rename parameter.
                p.setName(newname);
                return true;
            }
        }
        // Target not found.
        return false;
    }

    public boolean deleteParameter(String name){
        return parameters.remove(new Parameter(name, "int"));
    }

    /* helper functions */
    public boolean containsParameter(String name){
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
