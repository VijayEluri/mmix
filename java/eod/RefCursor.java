package eod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

class RefCursor {

	static long start = new Date().getTime();

	public static void showElapsed(String msg) {
		long end = new Date().getTime();

		System.out.println(msg + " " + (end - start) + " ms");
		start = end;
	}

	public static void main(String args[]) throws Exception {
		showElapsed("start to register");
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		
		//jdbc:oracle:thin:username/password@//x.x.x.1:1522/ABCD
		Connection conn = DriverManager.getConnection(
				"jdbc:oracle:thin:eddie/eddie@//localhost:1521/XE");

		showElapsed("Connected, going to prepare");
//		(OracleCallableStatement) 
		OracleCallableStatement cstmt = (OracleCallableStatement) conn
				.prepareCall("begin demo_pkg.ref_cursor(?,?); end;");
		cstmt.setFetchSize(100);
		showElapsed("Prepared, going to bind");

		cstmt.setString(1, "SYS");
		cstmt.registerOutParameter(2, OracleTypes.CURSOR);
		showElapsed("Bound, going to execute");
		cstmt.execute();

		ResultSet rset = (ResultSet)cstmt.getObject(2);
		if(rset.next()){
		showElapsed("First Row " );
		}
		String data = null;
		int i;
		for (i = 0; rset.next(); i++) {
			data = rset.getString(1);
			data = rset.getString(2);
			data = rset.getString(3);
		}
		showElapsed("Last Row " + (i+1));
		
		Statement stmt = conn.createStatement();
		String sql = FileUtil.toString("C:/Users/think/Program/SCM_GIT/mmix/java/eod/stats_ref.sql","UTF-8");//.replace("\r\n"," ");
		System.out.println(sql);
		stmt.execute(sql);
	}
}
