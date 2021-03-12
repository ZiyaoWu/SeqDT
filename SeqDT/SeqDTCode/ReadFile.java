package SeqDTCode;
import java.io.BufferedReader;
import java.io.FileReader;
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
			list.add(str);
		}
		br.close();
		return list;
	}
	
	public Map<String, ArrayList<String>> data(ArrayList<String> readData)throws Exception{
		Map<String, ArrayList<String>> classMap = new LinkedHashMap<String, ArrayList<String>>();
		for(String str:readData){
			if (str.equals("") || str.equals(";")) continue;
			String label=str.split("\t")[0];
			if(classMap.containsKey(label)){
				classMap.get(label).add(str);
			}else{
				ArrayList<String> list = new ArrayList<String>();
				list.add(str);
				classMap.put(label, list);
			}
		}
		return classMap;
	}
}
