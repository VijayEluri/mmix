
package api.reg;


public class FakePO {
	String goodMan;
	public String getGoodMan() {
		return goodMan;
	}
	public void setGoodMan(String goodMan) {
		this.goodMan = goodMan;
	}
	public boolean isGoodMan(){
		return "Y".equalsIgnoreCase(goodMan);
	}
}
