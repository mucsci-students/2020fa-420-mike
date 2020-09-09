import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class list {

	public list(String listType) throws FileNotFoundException, IOException, ParseException {
		//Create list of files in saves folder
		File f = new File(System.getProperty("user.dir") + "\\save");
		String[] contents = f.list();
		String saveFile = null;

		// Find umlSave.json file
		for (int x = 0; x < contents.length; ++x) {
			if (contents[x].equals("umlSave.json")){
				saveFile = contents[x];
				break;
			}
		}

		//Decode json file into a JSONArray
		Object obj = new JSONParser().parse(new FileReader(saveFile));
		JSONObject javaObj = (JSONObject) obj;
		JSONArray list = (JSONArray) javaObj.get(listType);
	
		// Print out list
		System.out.println(listType + ":");
		for (int x = 0; x < list.size(); x++) {
			System.out.println("   -- " + list.get(x));
		}
	}
	
}