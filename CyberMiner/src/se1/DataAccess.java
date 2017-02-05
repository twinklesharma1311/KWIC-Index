package se1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataAccess {
	Connection conn = null;

	// Constructor
	public DataAccess() {

	}

	// To input url and original line
	public void insertURL(String url, String orgLine, String searchTerms,
			int status) throws Exception {
		Connection conn = null;
		java.sql.PreparedStatement ps = null;
		ResultSet rs = null;
		int urlId = 0;

		String insertUrl = "Insert into urlTable (urlId, url, orgLine, active) VALUES (DEFAULT, ?, ?, ?)";

		try {
			conn = dbConn();
			ps = conn.prepareStatement(insertUrl,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, url);
			ps.setString(2, orgLine);
			ps.setInt(3, status);
			ps.executeUpdate();
			System.out
					.println("************** URL is inserted ******************");

			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				urlId = rs.getInt(1);
				System.out.println("Generated urlID is :" + urlId);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// Getting individual shifted lines
		System.out.println("SearchTerms: " + searchTerms);
		String lines[] = searchTerms.split("/");
		for (String s : lines)
			System.out.println(s);

		
		String insertShiftedLines = "Insert into shiftlines (lineId, line, urlId) VALUES (DEFAULT, ?, ?)";

		try {
			ps = conn.prepareStatement(insertShiftedLines);

			for (String s : lines) {
				ps.setString(1, s);
				ps.setInt(2, urlId);
				ps.executeUpdate();
			}
			System.out
					.println("************** Shifted Line is inserted ******************");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		}
	}

	// Insert the shifted lines
	public void addUrl(String url, String orgLine, String searchTerms,int status) throws SQLException {
		Connection conn = null;
		java.sql.PreparedStatement ps = null;
		int urlId = 0;

		String selectSQL = "SELECT urlId FROM urlTable WHERE orgLine = ? LIMIT 0,1";

		try {
			conn = dbConn();
			ps = conn.prepareStatement(selectSQL);
			ps.setString(1, orgLine);

			// execute select SQL statement
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				urlId = rs.getInt("urlId");
				System.out.println("urlId : " + urlId);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
		
		// Getting individual shifted lines
		System.out.println("SearchTerms: " + searchTerms);
		String lines[] = searchTerms.split("/");
		for (String s : lines)
			System.out.println(s);
				
		String insertShiftedLines = "Insert into shiftlines (lineId, line, urlId) VALUES (DEFAULT, ?, ?)";
		
		try {
			ps = conn.prepareStatement(insertShiftedLines);

			for (String s : lines) {
				ps.setString(1, s);
				ps.setInt(2, urlId);
				ps.executeUpdate();
			}
			System.out.println("************** Shifted Line is inserted ******************");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		}
	}

	//Default search
	public String defaultSearch(String words[]) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		String result = "";
		
		String selectSQL = "SELECT orgLine, url FROM urlTable WHERE active = 1 and urlId IN (";
		for(int i = 0 ; i < words.length ; i++){
			if(i == 0)
				selectSQL += " SELECT DISTINCT urlId FROM shiftlines WHERE line LIKE '%" + words[i] + "%' ";
			else
				selectSQL += "UNION SELECT DISTINCT urlId FROM shiftlines WHERE line LIKE '%" + words[i] + "%' ";
		}
		selectSQL += ")";
		System.out.println("\n QueryString: " + selectSQL);

		try {
			conn = dbConn();
			stmt = conn.createStatement();
 
			System.out.println(selectSQL);
			ResultSet rs = stmt.executeQuery(selectSQL);

			int i = 0;
			while (rs.next()) {
				String url = rs.getString("url");
				String orgLine = rs.getString("orgLine");
 
				//System.out.println("URL : " + url);
				//System.out.println("OrgLine : " + orgLine);
				if(i == 0)
					result += orgLine + "," + url;
				else
					result += ";" + orgLine + "," + url;
				i++;
			}
			System.out.println("Final Result: " + result);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
		
		return result;
	}
	
	//Multiple ands
	public String multipleAnds(String words[]) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		String result = "";
		
		String selectSQL = "SELECT orgLine, url FROM urlTable WHERE active = 1 and urlId IN " +
				"(SELECT DISTINCT urlId FROM shiftlines WHERE line LIKE ";
		for(int i = 0 ; i < words.length ; i++){
			if(i == 0)
				selectSQL += "'%" + words[i] + "%" + words[i+1] + "%'";
			else
				selectSQL += " or '%" + words[i] + "%" + words[i-1] + "%'";
		}
		selectSQL +=")";
		System.out.println("\n QueryString: " + selectSQL);

		try {
			conn = dbConn();
			stmt = conn.createStatement();
 
			System.out.println(selectSQL);
			ResultSet rs = stmt.executeQuery(selectSQL);
 
			int i = 0;
			while (rs.next()) {
				String url = rs.getString("url");
				String orgLine = rs.getString("orgLine");
 
				//System.out.println("URL : " + url);
				//System.out.println("OrgLine : " + orgLine);
				if(i == 0)
					result += orgLine + "," + url;
				else
					result += ";" + orgLine + "," + url;
				i++;
			}
			System.out.println("Final Result: " + result);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
		
		return result;
	}
	
	//Multiple NOTs
		public String  multipleNots(String words[]) throws SQLException{
			Connection conn = null;
			Statement stmt = null;
			String result = "";
			
			String selectSQL = "SELECT orgLine, url FROM urlTable WHERE active = 1 and urlId IN " +
					"(SELECT DISTINCT urlId FROM shiftlines WHERE line LIKE '%" + words[0] + "%' " +
					"AND urlId NOT IN (SELECT DISTINCT urlId FROM shiftlines WHERE line LIKE "; 
			for(int i = 0 ; i < words.length ; i++){
				if(i == 0)
					selectSQL += "'%" + words[i] + "%" + words[i+1] + "%'";
				else
					selectSQL += " or '%" + words[i] + "%" + words[i-1] + "%'";
			}
			selectSQL +="))";
			System.out.println("\n QueryString: " + selectSQL);

			try {
				conn = dbConn();
				stmt = conn.createStatement();
	 
				System.out.println(selectSQL);
				ResultSet rs = stmt.executeQuery(selectSQL);
	 
				int i = 0;
				while (rs.next()) {
					String url = rs.getString("url");
					String orgLine = rs.getString("orgLine");
	 
					//System.out.println("URL : " + url);
					//System.out.println("OrgLine : " + orgLine);
					if(i == 0)
						result += orgLine + "," + url;
					else
						result += ";" + orgLine + "," + url;
					i++;
				}
				System.out.println("Final Result: " + result);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			}
			
			return result;
		}
	
	/*private void permutation(String prefix, List<String> words){
		int length = words.size();
		
		if(length == 0)
			System.out.println(prefix);
		else{
			for(int i = 0 ; i < length ; i++){
				List<String> newList = words.subList(0, i-1);
				newList.addAll(words.subList(i+1, length));
				permutation(prefix + words.get(i), newList);
			}
		}
	}*/
	
	//Delete url
	public void deleteURL(String url) throws SQLException{
	
		Connection conn = null;
		Statement stmt = null;
		int urlId = 0;//String result = "";
		
		String getUrlId = "SELECT urlId FROM urltable WHERE url = '" + url + "'";
		
		try {
			conn = dbConn();
			stmt = conn.createStatement();
 
			System.out.println(getUrlId);
			ResultSet rs = stmt.executeQuery(getUrlId);
			while (rs.next()) {
				urlId = rs.getInt("urlId");
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		
		String selectSQL = "UPDATE urlTable SET active = 0 where urlId = '" + urlId + "'";
		
		try {
			conn = dbConn();
			stmt = conn.createStatement();
 
			System.out.println(selectSQL);
			stmt.executeUpdate(selectSQL);
			
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} 
	}
	
	
	// Get connection function
	public static Connection dbConn() {
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/kwic", "root", "sid91phe");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}

		if (conn != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

		return conn;
	}
}
