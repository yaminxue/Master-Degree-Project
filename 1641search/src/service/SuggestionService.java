package service;

import java.util.ArrayList;


public interface SuggestionService {

	public ArrayList<String> getSuggestionsByDepo(String depoString);
	public ArrayList<String> getRecommendDoc(String enString);
}
