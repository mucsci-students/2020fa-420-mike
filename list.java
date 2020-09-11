public class list {
	
	// Lists all of the classes and their respective attributes
	public static void listClasses()
	{
		System.out.println("Classes:");
		for(Entity entity : Classes.entities)
		{			
			System.out.print("   -- " + entity.name + ": [ ");
			
			//Prints out all of the attributes
			for(int x = 0; x < entity.attributes.size(); x++)
			{
				System.out.print(entity.attributes.get(x));
				if(x != entity.attributes.size()-1)
				{
					System.out.print(", ");
				}
			}
			System.out.println(" ]");
		}
	}
	
	// Lists all of the relationships and the classes they are pointing to
	public static void listRelationships()
	{
		System.out.println("Relationships:");
		for(Relationship relation : Classes.relationships)
		{
			System.out.println("   -- " + relation.name + ": " + relation.class1 + "--" + relation.class2);
		}
	}
	
} 
