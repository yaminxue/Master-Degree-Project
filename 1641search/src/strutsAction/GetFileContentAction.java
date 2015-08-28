package strutsAction;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetFileContentAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String q;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		
		String qString = this.q;
		 
		//System.out.println(qString);
		 FileReader reader = new FileReader(ServletActionContext.getRequest().getRealPath("/")+"1641-original/"+qString+".txt");
		 BufferedReader br = new BufferedReader(reader);
		 String s1 = "";
		 String content = "";
		 int i;
		 for(i=0;i<2;i++){
			 String s2 = br.readLine();
		 }
		 while((s1 = br.readLine())!=null){
			 content += s1 +"~";
		 }
		  
		 br.close();
		 reader.close();
		 
		 //System.out.println(content);
		 HttpServletResponse response = (HttpServletResponse) ActionContext
					.getContext().get(ServletActionContext.HTTP_RESPONSE);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(content);
			
		
		return null;
	}
	
	

}
