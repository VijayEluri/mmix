package example;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * one connection per session. connection is not reused.
 * 
 * @author Wu Jianfeng
 * 
 */
public class AccountDAO {

	private SqlSessionFactory sqlMapper = null;

	AccountDAO(SqlSessionFactory sqlMapper) {
		this.sqlMapper = sqlMapper;
	}

	public Account select(long id) {
		SqlSession session = getSession();
		try {
			Account account = (Account) session.selectOne(
					"example.Account.getAccount", id);// id is boxed.
			session.commit();
			return account;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}
	}

	private SqlSession getSession() {
		SqlSession session = this.sqlMapper.openSession();
		return session;
	}

	public int insert(Account account) {
		SqlSession session = getSession();
		try {
			int insert = session.insert("createAccount", account);
			session.commit();
			return insert;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}

	}

	public int insertBatch(Account[] account) {
		SqlSession session = this.sqlMapper.openSession(ExecutorType.BATCH,
				false);
		try {
			int n = account.length;
			int insert = 0;
			for (int i = 0; i < n; i++) {
				System.out.println(account[i]);
				insert += session.insert("createAccount", account[i]);
				System.out.println("i=" + i + "; insert=" + insert);
			}
			session.commit();
			return insert;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}

	}

	public int insertReuseSession(Account[] account) {
		SqlSession session = this.sqlMapper.openSession();
		try {
			int n = account.length;
			int insert = 0;
			for (int i = 0; i < n; i++) {
				System.out.println(account[i]);
				insert += session.insert("createAccount", account[i]);
				System.out.println("i=" + i + "; insert=" + insert);
			}
			session.commit();
			return insert;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}

	}

	/**
	 * how to void the plumbing code
	 * 
	 * @param account
	 * @return
	 */
	public int update(Account account) {
		SqlSession session = getSession();
		try {
			int count = session.update("updateAccount", account);
			session.commit();
			return count;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}

	}

	public int updateBatch(Account[] account) {
		SqlSession session = this.sqlMapper.openSession(ExecutorType.BATCH,
				false);
		try {
			int count = 0;
			int n = account.length;
			int ret = 0;
			for (int i = 0; i < n; i++) {
				ret = session.update("updateAccount", account[i]);
				count += ret;
				System.out.println("ret=" + ret +", Hex="+ Integer.toString(ret,16)+",abs"+Math.abs(ret));
			}
			session.commit();
			return count;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}

	}

	public int delete(long accountId) {
		SqlSession session = getSession();
		try {
			int count = session.delete("deleteAccount", accountId);
			session.commit();
			return count;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}

	}

	public int deleteAll() {
		SqlSession session = getSession();
		try {
			int count = session.delete("deleteAllAccount");
			session.commit();
			return count;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
			}
		}

	}

}
