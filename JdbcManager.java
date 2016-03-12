package com.jdbc.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JdbcManager {
    //数据库用户名  
    private static final String USERNAME = "root";  
    //数据库密码  
    private static final String PASSWORD = "root";  
    //驱动信息   
    private static final String DRIVER = "com.mysql.jdbc.Driver";  
    //数据库地址  
    private static final String JDBCURL = "jdbc:mysql://localhost:3306/dataname";      
	  //C3P0连接池数据源
    public static ComboPooledDataSource dataSource;  
    static {  
        try {  
            dataSource = new ComboPooledDataSource();  
            dataSource.setUser(USERNAME);  
            dataSource.setPassword(PASSWORD);  
            dataSource.setJdbcUrl(JDBCURL);  
            dataSource.setDriverClass(DRIVER);  
            dataSource.setInitialPoolSize(10);  
            dataSource.setMinPoolSize(5);  
            dataSource.setMaxPoolSize(50);  
            dataSource.setMaxStatements(100);  
            dataSource.setMaxIdleTime(60);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
	
    /*
	  //C3P0连接池 ,也可以采用c3p0-config.xml配置方式
    //可放置在: WebContent/WEB-INF/classes/
    //且在eclipse里把上述路径配置到：运行->运行配置->Apache Tomcat->类路径CLASSPATH下
    public static ComboPooledDataSource dataSource;  
    static {  
        try {  
        	  dataSource = new ComboPooledDataSource();//默认的缺省配置
            //dataSource = new ComboPooledDataSource("mysql2");//别名方式
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }*/
    
    /** 
     * 从C3P0连接池中获取数据源链接
     * @return Connection 数据源链接 
     */  
    public static Connection getConnection() {
    	Connection conn = null;  
        try {  
        	conn = dataSource.getConnection();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } 
        return conn;  
    }      
    
}
