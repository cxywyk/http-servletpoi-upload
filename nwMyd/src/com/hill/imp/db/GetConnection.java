package com.hill.imp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;



public  class GetConnection {
	
	private Connection conn=null;
	
	public	static  String location=ResourceBundle.getBundle("config").getString("service.location");
	private void getConnection() throws SQLException, ClassNotFoundException {
		ResourceBundle resource = ResourceBundle.getBundle("config");
		String userName =resource.getString("oracle.username");
		String passWord = resource.getString("oracle.password");
		String url = resource.getString("oracle.url");
		Class.forName("com.mysql.jdbc.Driver"); //连接驱动 mysql驱动
		conn = DriverManager.getConnection(url,userName,passWord);		
	}
	
	public Connection getCon() throws ClassNotFoundException, SQLException{
		if(this.conn==null){
			this.getConnection();
		}
		return this.conn;
	}
	
	public void closeCon(){
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
