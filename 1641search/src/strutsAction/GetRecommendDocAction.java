package strutsAction;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import service.SuggestionService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetRecommendDocAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String d;
	private String fd;
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
	
	public String getFd() {
		return fd;
	}

	public void setFd(String fd) {
		this.fd = fd;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		
		String dString = this.d;
		String fdString = this.fd;
		ArrayList<String> res = suggestionService.getRecommendDoc(dString);
		ArrayList<String> tempArrayList = new ArrayList<String>();
		String[] temp = fdString.split("~");
		for(int i=0; i<temp.length; i++)
			tempArrayList.add(temp[i]);
		res.removeAll(tempArrayList);
		
		String s1 ="";
		
		if(res.size()!=0){
			for(int i=0; i<res.size()-1; i++){
				s1 = s1+res.get(i)+"~";
			}
			s1 = s1+res.get(res.size()-1);
			//System.out.println(s1);
		}
		
		
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(s1);
		
		return null;
	}

}
