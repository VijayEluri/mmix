package common.beanutil;

public class Animal {
	private int id;
	private String _nickName1;
	private String nickName2_;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickName1() {
		return _nickName1;
	}
	public void setNickName1(String nickName1) {
		this._nickName1 = nickName1;
	}
	public String getNickName2() {
		return nickName2_;
	}
	public void setNickName2(String nickName2) {
		this.nickName2_ = nickName2;
	}
	@Override
	public String toString() {
		return "Annimal [id=" + id + ", nickName1=" + _nickName1
				+ ", nickName2=" + nickName2_ + "]";
	}
	
}
