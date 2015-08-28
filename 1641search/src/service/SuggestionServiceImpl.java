package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.springframework.transaction.annotation.Transactional;

import dao.Deposition;

public class SuggestionServiceImpl implements SuggestionService{
	
	private Deposition depo;
	public void setDeposition (Deposition depo) {
        this.depo = depo;
    }

	@Override
	@Transactional
	public ArrayList<String> getSuggestionsByDepo(String depoString) {
		// TODO Auto-generated method stub
		String[] temp = depoString.split("~");
		ArrayList<String> res = new ArrayList<String>();
		res.addAll(depo.getPersonEntityByDepoID(temp[0]));
		res.addAll(depo.getPlaceEntityByDepoID(temp[0]));
		if( temp.length>=2) {
			res.addAll(depo.getPersonEntityByDepoID(temp[1]));
			res.addAll(depo.getPlaceEntityByDepoID(temp[1]));
		}
		
		// filter repeated name
		HashSet<String> set = new HashSet<String>();
		ArrayList<String> res1 = new ArrayList<String>();
					
		for (Iterator iter = res.iterator(); iter.hasNext(); ) {
			String element = (String) iter.next();
			if (set.add(element))
				res1.add(element);
		}
		res.clear();
		res.addAll(res1);
		
		return res;
	}

	@Override
	public ArrayList<String> getRecommendDoc(String enString) {
		ArrayList<String> res = new ArrayList<String>();
		String[] tempStrings = enString.split("~");
		int count = tempStrings.length;
		int i = 3;
		if(count<15)
			i=count/5;
		ArrayList<String> depoid = new ArrayList<String>();
		String[] depoStrings = new String[count];
		
		for (int j = 0; j < count; j++) {
			if(tempStrings[j].indexOf("Ã´")!=-1){
				int x = tempStrings[j].indexOf("Ã´");
				tempStrings[j] = tempStrings[j].substring(0,x)+"ô"+tempStrings[j].substring(x+2,tempStrings[j].length());
			}
			ArrayList<String> tempList = depo.getDeppositionID(tempStrings[j]);
			String string = "";
			if(tempList.size()==0)
				tempList=depo.getDeppositionIDbyAddress(tempStrings[j]);
			//System.out.println(tempList);
			depoid.addAll(tempList);
//			if(tempList.size()==0)
//				System.out.println(tempStrings[j]);
			if(tempList.size()>0){
				for (int k = 0; k < tempList.size()-1; k++) {
					string+=tempList.get(k)+"~";
				}
				string+=tempList.get(tempList.size()-1);
			}
			
			depoStrings[j]=string;
			//System.out.println(depoStrings[j]);
		}
		
		// filter repeated name
		HashSet<String> set = new HashSet<String>();
		ArrayList<String> res1 = new ArrayList<String>();
							
		for (Iterator iter = depoid.iterator(); iter.hasNext(); ) {
					String element = (String) iter.next();
					if (set.add(element))
						res1.add(element);
				}
		depoid.clear();
		depoid.addAll(res1);
		
		int y=0;
		for (int x = 0; x < depoid.size(); x++) {
			y=0;
			for (int j = 0; j < count; j++) {
				String[] tempstr = {depoStrings[j]};
				if(depoStrings[j].contains("~")){
					tempstr = depoStrings[j].split("~");
				}
				
				ArrayList<String> tempList = new ArrayList<String>();
				for (int k = 0; k < tempstr.length; k++) {
					tempList.add(tempstr[k]);
				}
				
				if(tempList.contains(depoid.get(x)))
					y++;
			}
			if(y>=i)
				res.add(depoid.get(x));
		}
			
		return res;
	}
	
	
	

}
