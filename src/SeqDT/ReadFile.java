package SeqDT;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
public class ReadFile {
	public ReadFile() {
		
	}
	public ArrayList<String> Read(String filepath)throws Exception{
		BufferedReader br = null;
		ArrayList<String> list = new ArrayList<String>();
		br = new BufferedReader(new FileReader(filepath));
		String str = null;
		while ((str = br.readLine()) != null) {
			if(str.equals(""))
				continue;
			list.add(str);
		}
		br.close();
		return list;
	}
	
}
