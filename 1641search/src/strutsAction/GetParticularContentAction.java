package strutsAction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetParticularContentAction extends ActionSupport {

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

		ArrayList<String> andqueries = QueryAction.andEntity;
		ArrayList<String> orqueries = QueryAction.orEntity;

		String s1 = null;
		
		if(andqueries!=null||orqueries!=null){
			FileReader reader = new FileReader(ServletActionContext.getRequest()
					.getRealPath("/") + "1641-original/" + qString + ".txt");
			BufferedReader br = new BufferedReader(reader);
			
			int key = 0;
			while ((s1 = br.readLine()) != null) {
				// System.out.println(s1);
				for (int i = 0; i < andqueries.size(); i++) {
					CharSequence cs = andqueries.get(i);
					// System.out.println("sdf");
					// System.out.println(s1.indexOf(andqueries[i]));
					if (s1.contains(cs)) {
						key = 1;
						// System.out.println(s1.indexOf(andqueries[i]));
						break;
					}
				}

				if (key == 1)
					break;

				for (int j = 0; j < orqueries.size(); j++) {

					CharSequence cs1 = orqueries.get(j);
					if (s1.contains(cs1)) {
						key = 1;
						break;
					}
				}

				if (key == 1)
					break;
			}

			br.close();
			reader.close();
		}
		

		//System.out.println(s1);
		
		
		if(s1==null){

			FileReader reader1 = new FileReader(ServletActionContext.getRequest()
					.getRealPath("/") + "1641-original/" + qString + ".txt");
			BufferedReader br1 = new BufferedReader(reader1);
			while ((s1 = br1.readLine()) != null) {
				if(s1.length()>10)
					break;
			}
			br1.close();
			reader1.close();
		}
		
		
		
		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(s1);

		return null;
	}

}
