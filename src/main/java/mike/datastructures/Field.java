package mike.datastructures;

import mike.datastructures.Entity.visibility;

public class Field extends Formal{
    private visibility visType;

    public Field(String name, String type, visibility visType){
        super(name, type);
	this.setVisibility(visType);
    }

    public visibility getVisibility() {
	return visType;
    }

    public void setVisibility(visibility newVisType) {
	this.visType = newVisType;
    }
}
