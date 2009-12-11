package xiao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * How to expand the directory hierarchy to flat file sequences.
 * @author wueddie-wym-wrz
 *
 */
public class Replace {
	
	public static void main(String [] args){
		
		File file = new File("E:\\MyDocuments\\Download\\SCHROD");
		if(file.isDirectory()){
			File [] files= file.listFiles();
			List<File> list =  new ArrayList<File> (files.length );
			Collections.addAll(list, files);
			System.out.println(list);
		}
		//file.
		file = new File("D:\\workspace\\algorithm\\algorithm\\src");
		List<File> list =  new ArrayList<File> (100);
		if(file.isDirectory()){
			expandDirectory(file, list);
		}
		for(File filetemp : list){
			System.out.println(filetemp.getAbsolutePath());
		}
		
	}
	/**
	 * 
	 * @param file should be a directory,
	 * @param list global list of real files (not a directory) 
	 */
	private static void expandDirectory(File directory, List<File> realFileList){		
			File [] files= directory.listFiles();
			for(File file : files){
				if(file.isDirectory()){
					expandDirectory(file, realFileList);
				}else{
					realFileList.add(file);
				}
			}
	}
}
