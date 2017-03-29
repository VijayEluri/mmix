package eod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.Datum;

class IndexBy {

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
		OracleCallableStatement cstmt = (OracleCallableStatement) conn
				.prepareCall("begin demo_pkg.index_by(?,?,?,?); end;");

		showElapsed("Prepared, going to bind");
		int maxl = 20000;
		int elemSqlType = OracleTypes.VARCHAR;
		int elemMaxLen = 30;

		cstmt.setString(1, "SYS");
		cstmt.registerIndexTableOutParameter(2, maxl, elemSqlType, elemMaxLen);
		cstmt.registerIndexTableOutParameter(3, maxl, elemSqlType, elemMaxLen);
		cstmt.registerIndexTableOutParameter(4, maxl, elemSqlType, elemMaxLen);
		showElapsed("Bound, going to execute");
		cstmt.execute();

		Datum[] object_name = cstmt.getOraclePlsqlIndexTable(2);
		Datum[] object_type = cstmt.getOraclePlsqlIndexTable(3);
		Datum[] timestamp = cstmt.getOraclePlsqlIndexTable(4);
		showElapsed("First Row " + object_name.length);
		String data;
		int i;
		for (i = 0; i < object_name.length; i++) {
			data = object_name[i].stringValue();
			data = object_type[i].stringValue();
			data = timestamp[i].stringValue();
		}
		showElapsed("Last Row " + i);
		
		Statement stmt = conn.createStatement();
		String sql = FileUtil.toString("C:/Users/think/Program/SCM_GIT/mmix/java/eod/stats.sql","UTF-8");//.replace("\r\n"," ");
		System.out.println(sql);
		stmt.execute(sql);
	}
}
