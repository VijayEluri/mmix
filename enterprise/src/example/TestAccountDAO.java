package example;

import java.io.Reader;

import junit.framework.TestCase;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class TestAccountDAO extends TestCase {

	AccountDAO dao;

	public void setUp() {
		try {
			String resource = "sql-map-config.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			dao = new AccountDAO(new SqlSessionFactoryBuilder().build(reader));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	public void _testSelect() {
		Account select = dao.select(101);
		super.assertEquals(101, select.getId());
		System.out.println(select);
	}
	
	public void _testDelete() {
		long select = dao.delete(101);
		System.out.println(select + " rows are deleted.");
	}
	
	public void _testDeleteAll() {
		long select = dao.deleteAll();
		System.out.println(select + " rows are deleted.");
	}

	
	public void _testInsert() {
		Account account = new Account();
		account.setId(101);
		account.setUsername("micheal");
		account.setPassword("1234");
		int insert = dao.insert(account);
		System.out.println(insert + "  rows were inserted.");
	}

	public void _testUpdate() {
		Account account = dao.select(101);;
		account.setUsername("eddie");
		account.setPassword("wu");
		int count = dao.update(account);
		System.out.println(count + "  rows were udpated.");
	}
	
	public void testAll(){
		_testDelete();
		_testInsert();
		_testSelect();
		_testUpdate();
		_testSelect();
		_testDelete();
	}
}
