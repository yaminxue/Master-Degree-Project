package strutsAction;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.Deposition;

public class GetDataAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Deposition depo;

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub

		//List<String> list = new ArrayList<String>();
		
		StringBuffer list = new StringBuffer();
		String[] ls = depo.getPersonEntity().split("~");

		int i;
		for (i = 0; i < (ls.length-1); i++) {
			//list.add(ls[i]);
			list.append(ls[i]);
			list.append("~");
		}
		list.append(ls[i]);

		//System.out.println(list.toString());
		
		//JSONArray jsonArray = JSONArray.fromObject(list);
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(list.toString());

		//System.out.println(jsonArray.toString());

		return null;
	}

	public Deposition getDepo() {
		return depo;
	}

	public void setDeposition(Deposition depo) {
		this.depo = depo;
	}

	
	
	
}
