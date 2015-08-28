package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.sql.DataSource;



public class DepositionImpl implements Deposition{
	
	 
	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		}

	   @Override
	public String getPersonEntity() {
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		PreparedStatement ps =null;
		ArrayList<String> resArrayList = new ArrayList<String>();
		
		try {
			
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select forename, surname from person");
			while (rs.next()) {
				String firstname = rs.getString("forename");
				String lastname = rs.getString("surname");
//				if(lastname.length()>1){
//					//System.out.println(firstname);
//					if(lastname.substring(0, 1).equals(" ")){
//						System.out.println(lastname);
//						lastname = lastname.substring(1,lastname.length());
//						System.out.println(lastname);
//						ps = conn.prepareStatement("update person set surname=? where surname=?");
//						ps.setString(1, lastname);
//						ps.setString(2, " "+lastname);
//						ps.executeUpdate();	
//					}
//				}
				String name = firstname + " " + lastname;
				resArrayList.add(name);
				
			}
			// filter repeated name
			HashSet<String> set = new HashSet<String>();
			ArrayList<String> res = new ArrayList<String>();
			
			for (Iterator iter = resArrayList.iterator(); iter.hasNext(); ) {
			String element = (String) iter.next();
			if (set.add(element))
				res.add(element);
			}
			resArrayList.clear();
			resArrayList.addAll(res);	
			
			for (int i = 0; i < resArrayList.size()-1; i++) {
				sb.append(resArrayList.get(i) +"(Entity)"+ "~");
			}
			sb.append(resArrayList.get(resArrayList.size()-1) +"(Entity)");
			//System.out.println(sb.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	   @Override
	public ArrayList<String> getDeppositionID(String name) {
		ArrayList<String> odID = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String forename ="";
		String surname = name;
		int i=0;
		
		while(true){
			
			int index=surname.indexOf(" ");
			if(index==-1)
				break;
			if(i==0)
				forename = forename + surname.substring(0,index);
			else 
				forename = forename +" "+ surname.substring(0,index);
			surname = surname.substring(index+1,surname.length());
//			System.out.println(forename);
//			System.out.println(surname);
			
			
			try {
				
				conn = dataSource.getConnection();
				ps = conn.prepareStatement("select deposition_id from person where forename = ? and surname = ?");
				ps.setString(1, forename);
				ps.setString(2, surname);
				rs = ps.executeQuery();
				while (rs.next()) {
					String depositionID = rs.getString("deposition_id");
					odID.add(getOriginDepoIDbyDepoID(depositionID));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if (rs != null)
						rs.close();
					if (ps != null)
						ps.close();
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			i++;
			if(odID.size()!=0)
				break;
		}
//		System.out.println(odID);
		return odID;
		
	}  
	   
	@Override
	public ArrayList<String> getDeppositionIDbyAddress(String address) {
		// TODO Auto-generated method stub
		ArrayList<String> place_id = new ArrayList<String>();
		ArrayList<String> oid = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String temp ="";
		try {
			
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("select place_id from place where address_1 = ?");
			ps.setString(1, address);
			rs = ps.executeQuery();
			while (rs.next()) {
				int placeID = rs.getInt("place_id");
				
				ps1 = conn.prepareStatement("select deposition_id from place_link where place_id = ?");
				ps1.setInt(1, placeID);
				rs1 = ps1.executeQuery();
				if(rs1.next()){
					temp = rs1.getString("deposition_id");
					oid.add(getOriginDepoIDbyDepoID(temp));
				}
				else 
					;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs1 != null)
					rs1.close();
				if (rs != null)
					rs.close();
				if (ps1 != null)
					ps1.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return oid;
	}

	@Override
	public String getOriginDepoIDbyDepoID(String dID) {
		// TODO Auto-generated method stub
		String depoID ="";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("select original_deposition_id from original_deposition where deposition_id = ?");
			ps.setString(1, dID);
			rs = ps.executeQuery();
			if(rs.next())
				depoID=rs.getString("original_deposition_id");
			else 
				depoID=dID;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
//		System.out.println(oID);
//		System.out.println(depoID);
		return depoID;
	}

	@Override
	public String getDepoIDbyOriginal(String oID) {
		// TODO Auto-generated method stub
		
		String depoID ="";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("select deposition_id from original_deposition where original_deposition_id = ?");
			ps.setString(1, oID);
			rs = ps.executeQuery();
			if(rs.next())
				depoID=rs.getString("deposition_id");
			else 
				depoID=oID;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
//		System.out.println(oID);
//		System.out.println(depoID);
		return depoID;
		
	}

	@Override
	public ArrayList<String> getPlaceEntityByDepoID(String depoID) {
		// TODO Auto-generated method stub
		
//		String dID = getDepoIDbyOriginal(depoID);
		String dID = depoID;
		ArrayList<String> place = new ArrayList<String>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("select place_id from place_link where deposition_id = ?");
			ps.setString(1, dID);
			rs = ps.executeQuery();
			while (rs.next()) {
				int placeID = rs.getInt("place_id");
				
				ps1 = conn.prepareStatement("select address_1 from place where place_id = ?");
				ps1.setInt(1, placeID);
				rs1 = ps1.executeQuery();
				if(rs1.next())
					place.add(rs1.getString("address_1"));
				else 
					;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs1 != null)
					rs1.close();
				if (rs != null)
					rs.close();
				if (ps1 != null)
					ps1.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
//		System.out.println(place);
		return place;
	}

	@Override
	public ArrayList<String> getPersonEntityByDepoID(String depoID) {
		// TODO Auto-generated method stub
		
//		String dID = getDepoIDbyOriginal(depoID);
		String dID = depoID;
		ArrayList<String> person = new ArrayList<String>();
		Connection conn = null;
		Statement stmt =null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select forename, surname from person where deposition_id = \""+dID+"\"");
			while (rs.next()) {
				String firstname = rs.getString("forename");
				String lastname = rs.getString("surname");
				String name = firstname + " " + lastname;
				person.add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}catch (SQLException se) {
				se.printStackTrace();
			}
		}
//		System.out.println(person);
		return person;
	}

	@Override
	public int login(String email, String pwd) {
		// TODO Auto-generated method stub
		
		int id =0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("select user_id from user where email=? and pwd=?");
			ps.setString(1, email);
			ps.setString(2, pwd);
			rs = ps.executeQuery();
			if(rs.next())
				id=rs.getInt("user_id");
			else 
				;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
		return id;
	}

	@Override
	public int register(String email, String pwd) {
		// TODO Auto-generated method stub
		int id =0;
		Connection conn = null;
		PreparedStatement ps = null;
		int b;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("insert into user (email,pwd) values (?,?)");
			ps.setString(1, email);
			ps.setString(2, pwd);
			b = ps.executeUpdate();
			if(b==1)
				id = login(email, pwd);
			else 
				;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
		return id;
	}

	@Override
	public boolean saveDoc(int id, String depoid) {
		// TODO Auto-generated method stub
		
		boolean b = false;
		Connection conn = null;
		PreparedStatement ps = null;
		int i;
		
		
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("insert into deposition (user_id,deposition_id) values (?,?)");
			ps.setInt(1, id);
			ps.setString(2, depoid);
			i = ps.executeUpdate();
			if(i>0)
				b = true;
			else {
				;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return b;
	}

	@Override
	public boolean deleteDoc(int id, String depoid) {
		// TODO Auto-generated method stub
		boolean b = false;
		Connection conn = null;
		PreparedStatement ps = null;
		int i;
		
		
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("delete from deposition where user_id=? and deposition_id=?");
			ps.setInt(1, id);
			ps.setString(2, depoid);
			i = ps.executeUpdate();
			if(i>0)
				b = true;
			else {
				;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return b;
	}

	@Override
	public String getSavedDoc(int id) {
		// TODO Auto-generated method stub
		
		String str ="";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("select deposition_id from deposition where user_id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while(rs.next())
				str=str+rs.getString("deposition_id")+"~";
			if(!str.equals(""))
				str=str.substring(0,str.length()-1);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		//System.out.println(str);
		return str;
	}

	@Override
	public boolean checkFileSatus(int userid, String depoid) {
		// TODO Auto-generated method stub

		boolean b = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("select * from deposition where user_id =? and deposition_id =?");
			ps.setInt(1, userid);
			ps.setString(2, depoid);
			rs = ps.executeQuery();
			
			if(rs.next())
				b=true;
			else 
				;
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return b;
	}
	
	
	   
}
