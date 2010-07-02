package eddie.wu.question;

public class QuestionType {
	private int type=0;
	private int typeName=0;
	private static QuestionType GuanZi=new QuestionType(1);
	private static QuestionType SiHuo=new QuestionType(2);
	private static QuestionType DuiSha=new QuestionType(3);
	private static QuestionType BuJu=new QuestionType(4);
	private static QuestionType DingShi=new QuestionType(5);
	
	private QuestionType(){
		
	}
	private QuestionType(int type){
		this.type=type;
	}
	public String toString(){
		StringBuffer buf=new StringBuffer("QuestionType[type:");
		buf.append(type);
		buf.append(","+"typeName:");
		buf.append(typeName);
		buf.append("]");
		return buf.toString();
	}
}
