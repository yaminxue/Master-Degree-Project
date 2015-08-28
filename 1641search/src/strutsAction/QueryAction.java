package strutsAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import service.QueryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class QueryAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private QueryService qService;

	public static ArrayList<String> andEntity;
	public static ArrayList<String> orEntity;
	public static ArrayList<String> andFT;
	public static ArrayList<String> orFT;

	@Override
	public String execute() throws Exception {

		andFT = new ArrayList<String>();
		orFT = new ArrayList<String>();
		andEntity = new ArrayList<String>();
		orEntity = new ArrayList<String>();

		HttpServletRequest request = ServletActionContext.getRequest();

		String andquery = java.net.URLDecoder.decode(
				request.getParameter("andquery"), "UTF-8");

		String orquery = java.net.URLDecoder.decode(
				request.getParameter("orquery"), "UTF-8");
		String[] andqueries = andquery.split("\",\"");
		String[] orqueries = orquery.split("\",\"");


		CharSequence cs = "Entity";
		// split entities and free texts
		for (int x = 0; x < andqueries.length; x++) {
			if (andqueries[x].contains(cs)) {
				String[] temp1 = andqueries[x].split("\\(");
				andEntity.add(temp1[0]);
			} else {
				andFT.add(andqueries[x]);
			}
		}

		for (int y = 0; y < orqueries.length; y++) {
			if (orqueries[y].contains(cs)) {
				String[] temp2 = orqueries[y].split("\\(");
				orEntity.add(temp2[0]);
			} else {
				orFT.add(orqueries[y]);
			}
		}

		

		// organize the query that will be sent to solr
		String solrquery = "";

		for (int i = 0; i < andFT.size(); i++) {
			if (i < (andFT.size() - 1))
				solrquery += andFT.get(i) + " " + "AND" + " ";
			else
				solrquery += andFT.get(i);
		}

		if (andFT.size() != 0 && orFT.size() != 0) {
			if (andFT.get(0).equals("") || orFT.get(0).equals(""))
				;
			else
				solrquery += " " + "OR" + " ";

		}
		for (int j = 0; j < orFT.size(); j++) {
			if (j < (orFT.size() - 1))
				solrquery += orFT.get(j) + " " + "OR" + " ";
			else
				solrquery += orFT.get(j);
		}
		
		ArrayList<String> solrList = new ArrayList<String>();
		if (solrquery.equals("") || solrquery.equals(" ")
				|| solrquery.equals("  ") || solrquery.equals("   "))
			;
		else
			solrList = qService.getResponseListfromSolr(solrquery);
		ArrayList<String> nameList = qService
				.getfilenamefromDeposition(andEntity);
		ArrayList<String> resList = new ArrayList<String>();
		if (nameList.size() == 0 && solrList.size() != 0)
			resList = solrList;
		else if (nameList.size() != 0 && solrList.size() == 0)
			resList = nameList;
		else if (nameList.size() != 0 && solrList.size() != 0) {
			solrList.retainAll(nameList);
			resList = solrList;
		} else
			;
		if (nameList.size() == 0 && orEntity.size() != 0) {
			resList.addAll(qService.getOrfilenamefromDeposition(orEntity));
			// filter repeated name
			HashSet<String> set = new HashSet<String>();
			ArrayList<String> res = new ArrayList<String>();

			for (Iterator iter = resList.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				if (set.add(element))
					res.add(element);
			}
			resList.clear();
			resList.addAll(res);
		}

		String str = "";

		if (resList.size() == 0)
			str = "No results containing all your search terms were found.";
		else {
			int templen = 0;
			if (resList.size() > 15)
				templen = 10;
			else
				templen = resList.size();
			for (int i = 0; i < templen - 1; i++) {
				str = str + resList.get(i) + "~";
			}
			str = str + resList.get(templen - 1);
		}

		HttpServletResponse response = (HttpServletResponse) ActionContext
				.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(str);

		return null;
	}

	public QueryService getQueryService() {
		return qService;
	}

	public void setQueryService(QueryService qService) {
		this.qService = qService;
	}

}
