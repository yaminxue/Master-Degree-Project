package strutsAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.Deposition;

public class RegisterAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Deposition deposition;
	private String email;
	private String pwd;
	private String pwd1;
	public Deposition getDeposition() {
		return deposition;
	}
	public void setDeposition(Deposition deposition) {
		this.deposition = deposition;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getPwd1() {
		return pwd1;
	}
	public void setPwd1(String pwd1) {
		this.pwd1 = pwd1;
	}
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		if(pwd.equals(pwd1)){
			int id = deposition.register(email, pwd);
			//System.out.println(id);
			
			if(id!=0){
				ActionContext.getContext().getSession().put("id", id);
				return SUCCESS;
			}
			else
				return ERROR;
		}
		else {
			return ERROR;
		}
		
	}
	
	
	
	
	

}
