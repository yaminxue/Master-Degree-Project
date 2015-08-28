package strutsAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.Deposition;

public class GetDepoListAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Deposition deposition;

	public Deposition getDeposition() {
		return deposition;
	}

	public void setDeposition(Deposition deposition) {
		this.deposition = deposition;
	}
	
	private String n;
	

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		
		String nString = this.n;
		
		if(nString.indexOf("Ã´")!=-1){
			int x = n.indexOf("Ã´");
			nString = nString.substring(0,x)+"ô"+n.substring(x+2,nString.length());
		}
		//System.out.println(nString);
		ArrayList<String> res = deposition.getDeppositionID(nString);
		if(res.size()==0)
			res=deposition.getDeppositionIDbyAddress(nString);
		
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
		
		String str = "";
		//System.out.println(res);
		
		for (int i = 0; i < res.size()-1; i++) {
			str = str + res.get(i)+"~";
		}
		str = str +  res.get(res.size()-1);
		
		
		HttpServletResponse response = (HttpServletResponse) ActionContext
					.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(str);
			
		
		return null;
	}
	
	

}
