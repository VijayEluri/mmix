package effective.serialize.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MyMap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Map<Integer, String> map = new HashMap<Integer, String>();

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		// Manually serialize superclass state
		s.writeInt(map.size());
		for (Entry<Integer, String> entry : map.entrySet()) {
			s.writeInt(entry.getKey());
			s.writeUTF(entry.getValue());
		}
	}

	private void readObject(ObjectInputStream s) throws IOException,
			ClassNotFoundException {
		s.defaultReadObject();
		// Manually deserialize and initialize superclass state

		int size = s.readInt();
		for (int i = 0; i < size; i++) {
			int readInt = s.readInt();
			String readUTF = s.readUTF();
			map.put(readInt, readUTF);
			//System.out.println(readInt + " : " + readUTF);
		}

	}
	
	public String toString(){
		return super.toString()+map.toString();
		
	}
}
