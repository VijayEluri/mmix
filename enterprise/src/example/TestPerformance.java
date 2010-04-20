package example;

import java.io.Reader;

import junit.framework.TestCase;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 100 object creation: 229,638 nanos <br/>
 * 100 records insertion: 21,560,184,605 nanos <br/>
 * 100 records insertion (reuse session): 10,314,272,065 nanos <br/>
 * 200 records Batch insertion: 460,152,617 nanos
 * 
 * @author Wu Jianfeng
 * 
 */
public class TestPerformance extends TestCase {
	private AccountDAO dao;
	TestAccountDAO testAccountDAO = new TestAccountDAO();
	long start, end;

	public void setUp() {
		try {
			String resource = "sql-map-config.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			dao = new AccountDAO(new SqlSessionFactoryBuilder().build(reader));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		testAccountDAO.setUp();
		testAccountDAO._testDeleteAll();

	}

	int n = 100;
	Account[] accounts = new Account[n];

	/**
	 * 100 object creation: 202,540 nanos<br>
	 * 100records insertion: 41,244,239,980 nanos
	 */
	public void testBatchInsertWithoutBatch() {
		createObjects();
		
		compareInsert();

		for (Account account : accounts) {
			account.setPassword("changed");
		}
		compareUpdate();

		start = end;
	}

//	public void testCompareUpdate() {
//		compareUpdate();
//	}

	private void compareUpdate() {
		int insertBatch;
		start = System.nanoTime();
		insertBatch = dao.updateBatch(accounts);
		end = System.nanoTime();
		System.out.printf(insertBatch + " records Batch update: %,d nanos %n",
				end - start);
	}

	private void compareInsert() {
		 int insertBatch;
//		start = end;
//		for (int i = 0; i < n; i++) {
//			int count = dao.insert(accounts[i]);
//			System.out.println("i=" + i + "; count=" + count);
//		}
//		end = System.nanoTime();
//		System.out.printf(n + " records insertion: %,d nanos %n", end - start);
//
//		accountDAO._testDeleteAll();
//
//		start = end;
//		 insertBatch = dao.insertReuseSession(accounts);
//		end = System.nanoTime();
//		System.out.printf(insertBatch
//				+ " records insertion (reuse session): %,d nanos %n", end
//				- start);

		testAccountDAO._testDeleteAll();

		start = end;
		insertBatch = dao.insertBatch(accounts);
		end = System.nanoTime();
		System.out.printf(insertBatch
				+ " records Batch insertion: %,d nanos %n", end - start);
	}

	private void createObjects() {
		start = System.nanoTime();

		for (int i = 0; i < n; i++) {
			Account account = new Account();
			account.setId(10000 + i);
			account.setUsername("eddie" + i);
			account.setPassword("pass" + i);
			accounts[i] = account;
		}
		end = System.nanoTime();
		System.out.printf(n + " object creation: %,d nanos %n", end - start);
	}

}
