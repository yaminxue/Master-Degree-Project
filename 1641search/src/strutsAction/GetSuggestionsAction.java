package strutsAction;



import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import service.SuggestionService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetSuggestionsAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String d;
	private SuggestionService suggestionService;
	
	

	public SuggestionService getSuggestionService() {
		return suggestionService;
	}

	public void setSuggestionService(SuggestionService suggestionService) {
		this.suggestionService = suggestionService;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}
	
	

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		
		String dString = this.d;
		ArrayList<String> res = suggestionService.getSuggestionsByDepo(dString);
		if(QueryAction.andEntity!=null||QueryAction.andFT!=null||QueryAction.orEntity!=null||QueryAction.orFT!=null){
			res.removeAll(QueryAction.andEntity);
			res.removeAll(QueryAction.andFT);
			res.removeAll(QueryAction.orEntity);
			res.removeAll(QueryAction.orFT);
		}
		
		
		
		String s1 ="";
		
		for(int i=0; i<res.size()-1; i++){
			s1 = s1+res.get(i)+"~";
		}
		s1 = s1+res.get(res.size()-1);
		//System.out.println(s1);
		
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(s1);
		
		return null;
	}
	
	

}
