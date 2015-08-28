package service;

import java.util.ArrayList;

public interface QueryService {

	public ArrayList<String> getResponseListfromSolr(String myQueryString) throws Exception;
	public ArrayList<String> getfilenamefromDeposition(ArrayList<String> andEntity);
	public ArrayList<String> getOrfilenamefromDeposition(ArrayList<String> orEntity);
}
