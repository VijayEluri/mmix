package toy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Util {
	public static void storeList(String fileName, List<BasicMove> obj){
		ObjectOutputStream fos;
		try {
			fos = new ObjectOutputStream(new FileOutputStream(fileName));
			fos.writeObject(obj);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static List<BasicMove>  getList(String fileName ){
		ObjectInputStream fos;
		try {
			fos = new ObjectInputStream(new FileInputStream(fileName));
			Object obj = fos.readObject();
			return (List<BasicMove>)obj;
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
