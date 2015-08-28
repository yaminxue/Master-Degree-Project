package dao;

import java.util.ArrayList;

public interface Deposition {

	public String getPersonEntity();
	public ArrayList<String> getDeppositionID(String name);
	public ArrayList<String> getDeppositionIDbyAddress(String address);
	public ArrayList<String> getPlaceEntityByDepoID(String depoID);
	public ArrayList<String> getPersonEntityByDepoID(String depoID);
	public String getDepoIDbyOriginal(String oID);
	public String getOriginDepoIDbyDepoID(String dID);
	public int login(String email, String pwd);
	public int register(String email, String pwd);
	public boolean saveDoc(int id, String depoid);
	public boolean deleteDoc(int id, String depoid);
	public String getSavedDoc(int id);
	public boolean checkFileSatus(int userid, String depoid);
}
