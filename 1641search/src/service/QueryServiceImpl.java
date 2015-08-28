package service;

import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.transaction.annotation.Transactional;

import dao.Deposition;

public class QueryServiceImpl implements QueryService{
	
	private Deposition depo;
	public void setDeposition (Deposition depo) {
        this.depo = depo;
    }

	@Override
    @Transactional
	public ArrayList<String> getResponseListfromSolr(String myQueryString) throws Exception{

		SolrQuery query = new SolrQuery(myQueryString);
		// set indent == true, so that the xml output is formatted
		query.set("indent", true);
		query.set("fl", "id");
		query.set("start", "0");
		query.set("rows", "2147483646");
				
		// use org.apache.solr.client.solrj.util.ClientUtils 
		// to make a URL compatible query string of your SolrQuery
		String urlQueryString = ClientUtils.toQueryString(query, false);

		String solrURL = "http://localhost:8983/solr/1641depo/select";
		URL url = new URL(solrURL + urlQueryString);

		// use org.apache.commons.io.IOUtils to do the http handling for you
		String xmlResponse = IOUtils.toString(url);
		
		String[] fileStrings = xmlResponse.split("originaltxt/");
		
		String[] temp;
		ArrayList<String> filename = new ArrayList<String>();
		for(int i=1;i<fileStrings.length; i++){
			temp = fileStrings[i].split(".txt");
			filename.add(temp[0]);
		}
		
		return filename;
	}
	
	
	@Override
    @Transactional
	public ArrayList<String> getfilenamefromDeposition(ArrayList<String> andEntity){
		
		ArrayList<String> filename = new ArrayList<String>();
		if(andEntity.size()!=0){
			filename = depo.getDeppositionID(andEntity.get(0));
		}
			
		
		for (int i=1;i<andEntity.size();i++){
			ArrayList<String> temp = depo.getDeppositionID(andEntity.get(i));
			filename.retainAll(temp);
		}
		
		return filename;
	}

	@Override
	public ArrayList<String> getOrfilenamefromDeposition(
			ArrayList<String> orEntity) {
		ArrayList<String> filename = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		
		if(orEntity.size()!=0){
			filename.addAll(depo.getDeppositionID(orEntity.get(0)));
		}
		
		for (int i=1;i<orEntity.size();i++){
			temp = depo.getDeppositionID(orEntity.get(i));
			filename.addAll(temp);
		}
		
		return filename;
	}
	

	
		
//	 
//	 public static void main (String[] args)throws Exception{
//		 String query ="dublin";
//		 QuerySolr qs = new QuerySolr();
//		 ArrayList<String> res = qs.getResponseString(query);
//	 }
}
