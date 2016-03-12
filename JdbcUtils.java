package com.jdbc.dbutils;

import java.sql.Connection;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.ResultSetMetaData;  
import java.sql.SQLException;  
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
import java.util.Map; 

public class JdbcUtils {
    private Connection conn;  
    private PreparedStatement pstmt;  
    private ResultSet rs;  
    
    /**
     * 构造函数
     */
    public JdbcUtils() { 
    	//从连接池里获得一个连接
    	conn = JdbcManager.getConnection(); 
    }  
    
    /** 
     * 从C3P0连接池中获取数据源链接
     * @return Connection 数据源链接 
     */  
    public Connection getConnection() {        
        return conn;  
    }  
    
    /** 
     * 关闭数据库连接 
     */  
    public void close(){  
        if(rs != null){  
            try{  
                rs.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        } 
        if(pstmt != null){  
            try{  
            	pstmt.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        } 
        if(conn != null){  
            try{  
            	conn.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        } 
    }  
    
    /** 
     * 查询单条记录
     * @param String table 
     * @param String cols
     * @param String where
     * @param Object[] params
     * @return Map<String, Object>
     * @throws SQLException 
     */  
    public Map<String, Object> select(String table, String cols, String where, Object[] params) throws SQLException{  
        Map<String, Object> map = new HashMap<String, Object>();  
        int index  = 1;  
        pstmt = conn.prepareStatement("select "+cols+" from "+table+" where "+where);  
        if(params != null && params.length!=0){  
            for(int i=0; i<params.length; i++){  
                pstmt.setObject(index++, params[i]);  
            }  
        }  
        rs = pstmt.executeQuery();//返回查询结果  
        ResultSetMetaData metaData = rs.getMetaData();  
        int col_len = metaData.getColumnCount();  
        while(rs.next()){  
            for(int i=0; i<col_len; i++ ){  
                String cols_name = metaData.getColumnName(i+1);  
                Object cols_value = rs.getObject(cols_name);  
                if(cols_value == null){  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
        }  
        return map; 
    } 
    
    /** 
     * 查询单条记录 - 重载1
     * @param String table 
     * @param String where
     * @param Object[] params
     * @return Map<String, Object>
     * @throws SQLException 
     */  
    public Map<String, Object> select(String table, String where, Object[] params) throws SQLException{  
    	String cols="*"; 
        return select(table, cols, where, params); 
    } 
    
    /** 
     * 查询单条记录 - 重载2
     * @param String table 
     * @param String where
     * @return Map<String, Object>
     * @throws SQLException 
     */  
    public Map<String, Object> select(String table, String where) throws SQLException{  
    	String cols="*"; 
    	Object[] params=null;
        return select(table, cols, where, params); 
    } 
    
    /** 
     * 查询单条记录 - 重载3
     * @param String table 
     * @param String cols
     * @param String where
     * @return Map<String, Object>
     * @throws SQLException 
     */  
    public Map<String, Object> select(String table, String cols, String where) throws SQLException{  
    	Object[] params=null;
        return select(table, cols, where, params); 
    } 
    
    /**
     * 查询多条记录 
     * @param String table 
     * @param String cols
     * @param String where
     * @param Object[] params
     * @return List<Map<String, Object>>
     * @throws SQLException 
     */  
    public List<Map<String, Object>> select_all(String table, String cols, String where, Object[] params) throws SQLException{  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        int index = 1;  
        pstmt = conn.prepareStatement("select "+cols+" from "+table+" where "+where);  
        if(params != null && params.length!=0){  
            for(int i = 0; i<params.length; i++){  
                pstmt.setObject(index++, params[i]);  
            }  
        }  
        rs = pstmt.executeQuery();  
        ResultSetMetaData metaData = rs.getMetaData();  
        int cols_len = metaData.getColumnCount();  
        while(rs.next()){  
            Map<String, Object> map = new HashMap<String, Object>();  
            for(int i=0; i<cols_len; i++){  
                String cols_name = metaData.getColumnName(i+1);  
                Object cols_value = rs.getObject(cols_name);  
                if(cols_value == null){  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
            list.add(map);  
        }  
        return list;  
    }  
    
    /**
     * 查询多条记录 - 重载1
     * @param String table 
     * @param String cols
     * @param String where
     * @param Object[] params
     * @return List<Map<String, Object>>
     * @throws SQLException 
     */  
    public List<Map<String, Object>> select_all(String table, String where, Object[] params) throws SQLException{  
    	String cols="*";
    	return select_all(table, cols, where, params);
    }
    
    /**
     * 查询多条记录 - 重载2
     * @param String table 
     * @param String where
     * @return List<Map<String, Object>>
     * @throws SQLException 
     */  
    public List<Map<String, Object>> select_all(String table, String where) throws SQLException{  
    	String cols="*";
    	Object[] params=null;
    	return select_all(table, cols, where, params);
    }
    
    /**
     * 查询多条记录 - 重载3
     * @param String table 
     * @param String cols
     * @param String where
     * @return List<Map<String, Object>>
     * @throws SQLException 
     */  
    public List<Map<String, Object>> select_all(String table, String cols, String where) throws SQLException{  
    	Object[] params=null;
    	return select_all(table, cols, where, params);
    }
    
    
    /** 
     * 增加
     * @param String table 
     * @param Map<String, Object> map
     * @return int
     * @throws SQLException 
     */  
    public int insert(String table, Map<String, Object> map) throws SQLException{  
        int result = -1;  
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) { //遍历
        	String key = entry.getKey();
        	sb.append("`"+key+"`=?,");
        }
        String str = sb.toString();
        if(!str.equals("")){
        	str = str.substring(0, str.length()-1);//删除最后一个字符
        }        	
        //预查询
        pstmt = conn.prepareStatement("insert into `"+table+"` set "+str);  
        int index = 1;  
        for (Map.Entry<String, Object> entry : map.entrySet()) { //遍历
        	Object value = entry.getValue();
        	pstmt.setObject(index++, value);  
        }
        result = pstmt.executeUpdate();   
        return result;  
    } 
    
    /** 
     * 修改
     * @param String table 
     * @param Map<String, Object> map
     * @param String where
     * @param Object[] params
     * @return int
     * @throws SQLException 
     */  
    public int update(String table, Map<String, Object> map, String where, Object[] params) throws SQLException{  
        int result = -1;  
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) { //遍历
        	String key = entry.getKey();
        	sb.append("`"+key+"`=?,");
        }
        String str = sb.toString();
        if(!str.equals("")){
        	str = str.substring(0, str.length()-1);//删除最后一个字符
        }        	
        //预查询
        pstmt = conn.prepareStatement("update `"+table+"` set "+str+" where "+where);  
        int index = 1;  
        for (Map.Entry<String, Object> entry : map.entrySet()) { //遍历
        	Object value = entry.getValue();
        	pstmt.setObject(index++, value);  
        }
        if(params != null && params.length!=0){ 
	        for (int i = 0; i < params.length; i++) {
				Object object = params[i];
				pstmt.setObject(index++, object); 
			}
        }
        result = pstmt.executeUpdate();   
        return result;  
    } 
    
    /** 
     * 修改 - 重载
     * @param String table 
     * @param Map<String, Object> map
     * @param String where
     * @return int
     * @throws SQLException 
     */  
    public int update(String table, Map<String, Object> map, String where) throws SQLException{  
    	Object[] params=null;
        return update(table, map, where, params);
    }
    
    /** 
     * 删除
     * @param String table 
     * @param String where
     * @param Object[] params
     * @return int
     * @throws SQLException 
     */  
    public int delete(String table, String where, Object[] params) throws SQLException{  
        int result = -1;      	
        //预查询
        pstmt = conn.prepareStatement("delete from `"+table+"` where "+where); 
        if(params != null && params.length!=0){ 
        	int index = 1; 
	        for (int i = 0; i < params.length; i++) {
				Object object = params[i];
				pstmt.setObject(index++, object); 
			}
        }
        result = pstmt.executeUpdate();   
        return result;  
    } 
    
    /** 
     * 删除 - 重载
     * @param String table 
     * @param String where
     * @return int
     * @throws SQLException 
     */  
    public int delete(String table, String where) throws SQLException{  
    	Object[] params=null;      	
        return delete(table, where, params);
    } 
    
    //--------------------------------------------------------------
    //我是分隔线
    //--------------------------------------------------------------
      
    /** 
     * 增、删、改 - 集成方法
     * @param String sql 
     * @param Object[] params 
     * @return boolean
     * @throws SQLException 
     */  
    public boolean execute(String sql, Object[] params) throws SQLException{  
        boolean flag = false;  
        int result = -1;  
        pstmt = conn.prepareStatement(sql);           
        if(params != null && params.length!=0){ 
        	int index = 1; 
            for(int i=0; i<params.length; i++){  
                pstmt.setObject(index++, params[i]);  
            }  
        }  
        result = pstmt.executeUpdate();  
        flag = result > 0 ? true : false;  
        return flag;  
    }  
  
    /** 
     * 查询单条记录 
     * @param String sql 
     * @param Object[] params 
     * @return Map<String, Object>
     * @throws SQLException 
     */  
    public Map<String, Object> query(String sql, Object[] params) throws SQLException{  
        Map<String, Object> map = new HashMap<String, Object>();  
        int index  = 1;  
        pstmt = conn.prepareStatement(sql);  
        if(params != null && params.length!=0){  
            for(int i=0; i<params.length; i++){  
                pstmt.setObject(index++, params[i]);  
            }  
        }  
        rs = pstmt.executeQuery();//返回查询结果  
        ResultSetMetaData metaData = rs.getMetaData();  
        int col_len = metaData.getColumnCount();  
        while(rs.next()){  
            for(int i=0; i<col_len; i++ ){  
                String cols_name = metaData.getColumnName(i+1);  
                Object cols_value = rs.getObject(cols_name);  
                if(cols_value == null){  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
        }  
        return map;  
    }  
  
    /**查询多条记录 
     * @param String sql 
     * @param Object[] params 
     * @return List<Map<String, Object>>
     * @throws SQLException 
     */  
    public List<Map<String, Object>> queryMulti(String sql, Object[] params) throws SQLException{  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        int index = 1;  
        pstmt = conn.prepareStatement(sql);  
        if(params != null && params.length!=0){  
            for(int i = 0; i<params.length; i++){  
                pstmt.setObject(index++, params[i]);  
            }  
        }  
        rs = pstmt.executeQuery();  
        ResultSetMetaData metaData = rs.getMetaData();  
        int cols_len = metaData.getColumnCount();  
        while(rs.next()){  
            Map<String, Object> map = new HashMap<String, Object>();  
            for(int i=0; i<cols_len; i++){  
                String cols_name = metaData.getColumnName(i+1);  
                Object cols_value = rs.getObject(cols_name);  
                if(cols_value == null){  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
            list.add(map);  
        }  
        return list;  
    }  
    
    /**
     * 开始事务
     */
    public void beginTransaction() {    	
        try {
			conn.setAutoCommit(false);//设置事务为非自动提交
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 事务提交
     */
    public void commit() {
        try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 事务回滚
     */
    public void rollback() {
        try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 结束事务
     */
    public void endTransaction() { 
        try {
			conn.setAutoCommit(true);//设置事务为自动提交
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
