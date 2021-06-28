package jobs.services;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.ReservedInfo;
import beans.RestaurantInfo;

class DataAccessObject extends controller.DataAccessObject{
	DataAccessObject(){
		super();
	}
	
	ArrayList<RestaurantInfo> getRestaurantInfo(RestaurantInfo ri) {
		ArrayList<RestaurantInfo> restaurantList = new ArrayList<RestaurantInfo>();
		String query = "SELECT * FROM SEARCHLIST WHERE SWORD LIKE '%' || ? || '%'";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getWord());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RestaurantInfo record = new RestaurantInfo();
				record.setReCode(rs.getNString("RECODE"));
				record.setRestaurant(rs.getNString("RESTAURANT"));
				record.setLocation(rs.getNString("LOCATION"));
				record.setCatagory(rs.getNString("CATEGORY"));
				record.setMenuCode(rs.getNString("MENUCODE"));
				record.setMenu(rs.getNString("MENU"));
				record.setPrice(rs.getInt("PRICE"));
				record.setGpa(rs.getInt("GPA"));
				record.setCount(rs.getInt("COUNT"));
				record.setComment(rs.getNString("COMMENTS"));
				restaurantList.add(record);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return restaurantList;
	}
	
	public ArrayList<ReservedInfo> getWaitingInfo(ReservedInfo ri){
		ArrayList<ReservedInfo> list = new ArrayList<ReservedInfo>();
		String query = "SELECT  RECODE, RE.RE_NAME AS RNAME, CUID, CNAME, TO_CHAR(RL.RDATE, 'YYYYMMDDHH24MISS') AS DBDATE, "
				+ "TO_CHAR(RDATE, \'YYYY-MM-DD HH24:MI:SS\') AS RDATE, COUNT AS MCOUNT "
						+ " FROM RESERVELIST RL INNER JOIN RE ON RL.RECODE = RE.RE_CODE "
						+ " WHERE RECODE = ? AND PROCESS = ?";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getProcess());
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservedInfo rInfo = new ReservedInfo();
				rInfo.setReCode(rs.getNString("RECODE"));
				rInfo.setReName(rs.getNString("RNAME"));
				rInfo.setCuId(rs.getNString("CUID"));
				rInfo.setCuName(rs.getNString("CNAME"));
				rInfo.setrDate(rs.getNString("RDATE"));
				rInfo.setDbDate(rs.getNString("DBDATE"));
				rInfo.setmCount(rs.getInt("MCOUNT"));
				
				list.add(rInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<ReservedInfo> getTodayReservedInfo(ReservedInfo ri){
		ArrayList<ReservedInfo> list = new ArrayList<ReservedInfo>();
		String query = "SELECT * FROM RLISTBYDATE WHERE RECODE = ? AND TO_CHAR(ODDATE, 'YYYYMMDD') = TO_CHAR(SYSDATE, 'YYYYMMDD')";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReservedInfo rInfo = new ReservedInfo();
				rInfo.setReCode(rs.getNString("RECODE"));
				rInfo.setReName(rs.getNString("RENAME"));
				rInfo.setCuId(rs.getNString("CUID"));
				rInfo.setCuName(rs.getNString("CUNAME"));
				rInfo.setPhone(rs.getNString("PHONE"));
				rInfo.setmCode(rs.getNString("MNCODE"));
				rInfo.setmName(rs.getNString("MENU"));
				rInfo.setQuantity(rs.getInt("QUANTITY"));
				rInfo.setrDate(rs.getNString("ODDATE"));
				
				list.add(rInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	int confirmReserve(ReservedInfo ri) {
		int result = 0;
		String query = "UPDATE OS SET OS_PROCESS = 'C' WHERE OS_RECODE = ? AND OS_CUID = ? AND TO_CHAR(OS_DATE, 'YYYYMMDDHH24MISS') = ?";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			pstmt.setNString(2, ri.getCuId());
			pstmt.setNString(3, ri.getDbDate());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	 ArrayList<String> getReservedList(ReservedInfo ri) {
		ArrayList<String> reservedList = new ArrayList<String>();
		
		 String query = "SELECT TO_CHAR(OS_DATE,'YYYYMMDD') AS \"RESERVED\""
		 		+ "FROM OS WHERE OS_RECODE = ? AND OS_PROCESS=? AND "
		 		+ "            (TO_CHAR(OS_DATE,'YYYYMMDD') > TO_CHAR(SYSDATE,'YYYYMMDD') AND"
		 		+ "            TO_CHAR(OS_DATE,'YYYYMMDD') <= TO_CHAR(SYSDATE+7,'YYYYMMDD'))";
		 
		 try {
			pstmt=connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			 pstmt.setNString(2, ri.getProcess());
			 rs = pstmt.executeQuery();
			 
			 
			 while(rs.next()) {
				 reservedList.add(rs.getNString("RESERVED"));
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		return reservedList;
	}

	ArrayList<RestaurantInfo> getMenu(ReservedInfo ri){
		ArrayList<RestaurantInfo> list = new ArrayList<RestaurantInfo>();
		
		String query = "SELECT * FROM MENUINFO WHERE RECODE = ?";
		
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.setNString(1, ri.getReCode());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				RestaurantInfo menu = new RestaurantInfo();
				menu.setReCode(rs.getNString("RECODE"));
				menu.setRestaurant(rs.getNString("RESTAURANT"));
				menu.setMenuCode(rs.getNString("MNCODE"));
				menu.setMenu(rs.getNString("MENUNAME"));
				menu.setPrice(rs.getInt("PRICE"));
				
				list.add(menu);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;

		
	}
}
