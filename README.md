# jdbc-dbutils
JAVA WEB Development: Jdbc dbUtils for MYSQL.

//------------------------- demo 1: How to use -------------------------
		JdbcUtils jdbc = new JdbcUtils();  

		//initialize map
		Map<String, Object> map = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
		        put("title", request.getParameter("title"));  
		        put("content", request.getParameter("content")); 
			}
		};
		int res = -1;
		try {
			res = jdbc.insert("msg", map);//add
		} catch (SQLException e) {
			e.printStackTrace();
		}
		jdbc.close();//close database connection
		if(res>0){
			//Congratulations, add successful
		}
		else{
			//sorry, add failed
		}
