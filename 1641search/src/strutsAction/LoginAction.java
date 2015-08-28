package strutsAction;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.Deposition;

public class LoginAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
		
		private Deposition deposition;
		private String email;
		private String pwd;
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
		
		@Override
		public String execute() throws Exception {
			// TODO Auto-generated method stub
			
			int id = deposition.login(email, pwd);
			
			if(id!=0){
				ActionContext.getContext().getSession().put("id", id);
				return SUCCESS;
			}
			else
				return ERROR;
			
		}
		
}
