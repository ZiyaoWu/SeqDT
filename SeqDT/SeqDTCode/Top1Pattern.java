package SeqDTCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Top1Pattern {
	public float maxGini;
	public Inverted it;
	public int Num;
	public int maxL;
	public int g;
	public ArrayList<Sequence> dataBase;
	public Map<String, Integer>  label;
	public float Gini;
	public Top1Pattern( int g, int maxL) {
		this.dataBase=new ArrayList<Sequence>();
		this.it = new Inverted();
		this.maxGini=0;
		this.Num=0;
		this.maxL=maxL;
		this.g=g;
		this.label = new HashMap<String,Integer>();
	}

	public void read(ArrayList<String> trainList,Dataset dataset)throws Exception {
		int i = 0;
		for(String str : trainList){
			Sequence s = new Sequence();
			String[] s1 = str.split("\t");
			String[] sequence = s1[1].split(" ");	
			s.data = sequence;
			s.label = s1[0];
			s.id = i;
			dataBase.add(s);
			dataset.dataId.add(i);
			dataset.indexList.put(i, 0);
			it.buildInvertedIndex(i,sequence);	
			LinkedHashSet<Integer> indexList = new LinkedHashSet<Integer>();
			indexList.add(-1);
			dataset.indexes.put(i, indexList);
			i++;
			if(label.containsKey(s.label)){
				int number=label.get(s.label);
				number++;
				label.put(s.label, number);
			}else{
				label.put(s.label, 1);
			}
		}
		dataset.len=i;
		Num=i;
		dataset.label=label;
		Gini=dataset.MultiGini();
		dataset.label=label;
		GiniInverted(it);
	}
	
	public LinkedHashSet<String> GiniInverted(Inverted itver){
		long time=0;
		long startTime = System.currentTimeMillis();
		Map<String, Map<Integer, ArrayList<Integer>>> temp=itver.word;	
		Map<String, Float> GiniList = new HashMap<String, Float>();
		for(String item: temp.keySet()){
			Map<String, Integer> Tlabel = new HashMap<String,Integer>();
			for(int id:temp.get(item).keySet()){
				String T=dataBase.get(id).label;
				if(Tlabel.containsKey(T)){
					int number=Tlabel.get(T);
					number++;
					Tlabel.put(T, number);
				}else{
					Tlabel.put(T, 1);
				}
			}
			float val=calculateGini(Tlabel);
			GiniList.put(item, val);
		}
		LinkedHashSet<String> GiniOrder=MapOrder(GiniList);		
		return GiniOrder;
	}
	
	public float calculateGini(Map<String, Integer>Tlabel) {
		int temp1=0,temp2=0;
		int len1=0;
		for(String key:label.keySet()){
			int value=0;			
			if(Tlabel.containsKey(key)){
				value=Tlabel.get(key);
			}
			len1=len1+value;
			int diffValue=label.get(key)-value;
			temp1=temp1+value*value;
			temp2=temp2+diffValue*diffValue;	
		}
		float Stemp1=0,Stemp2=0;
		if(len1==0){
			Stemp1=0;
			Stemp2=(float)temp2/(Num*(Num-len1));
		}else if((Num-len1)==0){
			Stemp1=(float)temp1/(len1*Num);
			Stemp2=0;
		}else{
			Stemp1=(float)temp1/(len1*Num);
			Stemp2=(float)temp2/(Num*(Num-len1));
		}
		return (Stemp1+Stemp2);
	}
	
	public float calculUpper(Map< String, Integer>Tlabel) {
		float max=0;
		for(String Tkey:Tlabel.keySet()){
			Map<String, Integer>la=new HashMap<String, Integer>();
			for(String key:label.keySet()){
				if(Tkey.equals(key)){
					la.put(key, Tlabel.get(key));
				}else{
					la.put(key, 0);
				}
			}
			float temp=calculateGini(la);
			if(max<temp){
				max=temp;
			}
		}		
		max=-1*Gini+max;
		return max;
	}
	
	static class MyComp implements Comparator<Dataset>{
        @Override
        public int compare(Dataset d1, Dataset d2) {
        	if(d1.TGini>d2.TGini){
        		return -1;
        	}
        	else if(d1.TGini<d2.TGini){
        		return 1;
        	}else{
        		return 0;
        	}
        }
    }

	public LinkedHashSet<String> MapOrder(Map<String, Float> GiniList){
		ArrayList <Map.Entry<String, Float>> infoIds = new ArrayList<Map.Entry<String, Float>>(GiniList.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Float>>() {
			public int compare(Map.Entry<String, Float> o1,
					Map.Entry<String, Float> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		int order=0;
		LinkedHashSet<String> GiniOrder=new LinkedHashSet<String>();
		for(Map.Entry<String, Float> mapping : infoIds){
			GiniOrder.add(mapping.getKey());
		}
		return GiniOrder;
	}

	public String top1patternBre(Dataset D) {
		String prefix=null;
		String maxSeq="";
		String seq="";
		float ub=(float)1.1;
		float maxVal=-1;
		D.pre=seq;
		D.ub=ub;
		Queue<Dataset> que = new PriorityQueue(new MyComp());
		que.add(D);
		while(!que.isEmpty()){
			Dataset dataset=new Dataset();
			dataset=que.poll();
			prefix=dataset.pre;
			ub=dataset.ub;
			if(ub<=maxVal)
				break;
			HashSet<String> alphabet=new HashSet<String>();
			Inverted ited=new Inverted();
			for(int i=0;i<dataset.len;i++){
				int k=dataset.dataId.get(i);
				String[] itemSet = dataBase.get(k).data;
				for (int startIndex :dataset.indexes.get(k)) {
					int endIndex = 0;
					if (startIndex == -1) {
						startIndex = 0;
						endIndex = itemSet.length;
					} else if (startIndex + g + 1 > itemSet.length) {
						endIndex = itemSet.length;
					} else {
						endIndex = startIndex + g + 1;
					}
					for (int iter = startIndex; iter < endIndex; iter++) {
						alphabet.add(itemSet[iter]);
						ited.buildSubInvertedIndex(k, itemSet[iter], iter);
					}
				}					
			}
			LinkedHashSet<String> AlphabetSort = GiniInverted(ited);
			for(String item:AlphabetSort){
				seq=prefix+" "+item;
				Dataset subdataset=new Dataset();
				ArrayList<Integer> subDataId = subdataset.dataId;
				int num = 0;
				Map<Integer, ArrayList<Integer>> temp = ited.word.get(item);
				Map<Integer, HashSet<Integer>> newIndeces = subdataset.indexes;
				for(int i:temp.keySet()){
					int flag=0;
					ArrayList<Integer> indexList;
					LinkedHashSet<Integer> indexSet = new LinkedHashSet<Integer>();
					if((indexList = temp.get(i)) != null) {
						for (int startIndex : dataset.indexes.get(i)) {
							if (startIndex == -1) {
								flag = 1;
								for (int index : indexList) {
									indexSet.add(index + 1);
								}
							}else{
								for (int index : indexList) {
								if((index >= startIndex) && (index - startIndex) <= g) {
									indexSet.add(index + 1);
									flag = 1;
									break;
									}
								}
							}	
						}		
					}
					if (flag == 1) {
						newIndeces.put(i, indexSet);
						
						subDataId.add(i);
						num++;
						String TempLabel=dataBase.get(i).label;
						if(subdataset.label.containsKey(TempLabel)){
							int number=subdataset.label.get(TempLabel);
							number++;
							subdataset.label.put(TempLabel, number);
						}else{
							subdataset.label.put(TempLabel, 1);
						}
									
					}	
				}
				subdataset.len = num;
				float val = -1*Gini +calculateGini(subdataset.label);
				if(val>=maxVal){
					maxVal=val;
					maxSeq=seq;
				}
				subdataset.ub=calculUpper(subdataset.label);
				subdataset.pre=seq;
				
				if(subdataset.ub>maxVal){
					int SeqLen=seq.split(" ").length-1;
					if(SeqLen<maxL){
						//System.out.println("227 "+seq+" "+maxVal+ " "+subdataset.ub);
						que.add(subdataset);
					}
				}
			}
		}
		maxGini=maxVal;
		return maxSeq.substring(1);
	}
}
