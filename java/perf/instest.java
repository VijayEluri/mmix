package perf;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class instest {
	static String url = "jdbc:oracle:thin:@localhost:1521:XE";
	static String userName = "eddie";
	static String password = "eddie";
	static int nRec = 3000 * 10; // 2500 5 times fast.

	/**
	 * @param args
	 * @throws Exception
	 */
	static public void main(String args[]) throws Exception {

		if (args.length >= 1) {
			url = args[0];
			if (args.length >= 2) {
				userName = args[2];
				if (args.length >= 3) {
					password = args[2];
				}
			}
		}
		testSingleUser();
	}

	public static void testSingleUser() throws Exception {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		Connection conn = DriverManager.getConnection(url, userName, password);
		conn.setAutoCommit(false);

		cleanupBefore(conn);
		withBindVariable(conn, "T1");
//		cleanupAfter(conn);
//
//		withoutBindVariable(conn, "T1");
//		cleanupAfter(conn);
		conn.close();
	}

	public static void cleanupBefore(Connection conn)
			throws Exception {
		cleanup(conn, true);
	}

	public static void cleanupAfter(Connection conn)
			throws Exception {
		cleanup(conn, false);
	}

	// alter session set nls_language = American
	//@?/rdbms/admin/spreport
	public static void cleanup(Connection conn, boolean before)
			throws Exception {
		CallableStatement call =null;
		String sql=null;
		if(before==false){//get snap first before cleanup
			//BEGIN statspack.snap(); END;
			sql = "{call statspack.snap()}";
			call = conn.prepareCall(sql);
			call.execute();
		}
		sql = "select count(1) from v$sql";
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			int count = resultSet.getInt(1);
			System.out.println("v$sql: " + count);
		}

		sql = "alter system flush shared_pool";
		call = conn.prepareCall(sql);
		call.execute();

		resultSet = statement.executeQuery();
		if (resultSet.next()) {
			int count = resultSet.getInt(1);
			System.out.println("v$sql: " + count);
		}

		if(before){ //get snap after cleanup
			sql = "{call statspack.snap()}";
			call = conn.prepareCall(sql);
			call.execute();
		}
	}

	public static void withBindVariable(Connection conn, String table)
			throws Exception {
		System.out.println("start - with bind variable");
		long start = System.nanoTime();
		PreparedStatement pstmt = conn.prepareStatement("insert /*TAG*/ into "
				+ table + " (x) values(?)");
		long end = System.nanoTime();
		System.out.println("prepare statement: " + (end - start));

		start = System.nanoTime();
		conn.prepareStatement("select * /*TAG*/ from T1 where rownum<1");
		end = System.nanoTime();
		System.out.println("second  statement: " + (end - start));

		start = System.nanoTime();
		for (int i = 0; i < nRec; i++) {
			pstmt.setInt(1, i);
			pstmt.executeUpdate();
		}
		conn.commit();
		end = System.nanoTime();
		System.out.println("done " + (end - start));

		pstmt.close();
	}

	public static void withoutBindVariable(Connection conn, String table)
			throws Exception {
		System.out.println("start - without bind variable");

		long start = System.nanoTime();
		Statement stmt = conn.createStatement();
		long end = System.nanoTime();
		System.out.println("statement: " + (end - start));

		start = System.nanoTime();
		conn.createStatement();
		end = System.nanoTime();
		// NumberFormat.getIntegerInstance().format(number)
		System.out.println("second statement: " + (end - start));

		start = System.nanoTime();
		for (int i = 0; i < nRec; i++) {
			stmt.execute("insert /*TAG*/ into " + table + " (x) values(" + i
					+ ")");
		}
		conn.commit();
		end = System.nanoTime();
		long delta = end - start;
		if (delta > 1000 * 1000 * 1000) {
			System.out.println("done " + (end - start) / 1000 / 1000 / 1000
					+ " second.");
		} else if (delta > 1000 * 1000) {
			System.out.println("done " + (end - start) / 1000 / 1000
					+ " milli-second.");
		}

		stmt.close();
	}
}
