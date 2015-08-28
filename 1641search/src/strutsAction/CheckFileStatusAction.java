package strutsAction;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.Deposition;

public class CheckFileStatusAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Deposition deposition;
	private int userid;
	private String depoid;
	public Deposition getDeposition() {
		return deposition;
	}
	public void setDeposition(Deposition deposition) {
		this.deposition = deposition;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getDepoid() {
		return depoid;
	}
	public void setDepoid(String depoid) {
		this.depoid = depoid;
	}
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.setCharacterEncoding("UTF-8");
		boolean b = deposition.checkFileSatus(userid, depoid);
//		System.out.println(userid);
//		System.out.println(depoid);
//		System.out.println(b);
		if(b){
			response.getWriter().print("saved");
		}
		else {
			response.getWriter().print("not saved");
		}
		return null;
	}
	

}
