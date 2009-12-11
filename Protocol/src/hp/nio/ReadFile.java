package hp.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ReadFile {
	static final String inputFile = "C:/Java/eclipse-java-galileo-win32.zip";

	static final String outputFile = "C:/Java/eclipse-java-galileo-win32_copy.zip";
	static int bufferSize = 1024;

	public static void main(String[] args) throws Exception {
		readWithByteBuffer();
		readWithDirectByteBuffer();
		readWithMappedByteBuffer();
		readWithNormalBuffer();
		bufferSize = 10240;
		readWithByteBuffer();
		readWithDirectByteBuffer();
		readWithMappedByteBuffer();
		readWithNormalBuffer();
	}

	public static void readWithByteBuffer() throws Exception {
		FileInputStream fis = new FileInputStream(inputFile);
		FileOutputStream fos = new FileOutputStream(outputFile);

		long start = System.nanoTime();

		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fos.getChannel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		while (true) {
			byteBuffer.clear();
			int cnt = inChannel.read(byteBuffer);
			if (cnt < 0)
				break;
			byteBuffer.flip();
			outChannel.write(byteBuffer);
		}
		System.out.println("task time(ms) = " + (System.nanoTime() - start)
				/ 1000000);
		fis.close();
		fos.close();

	}

	public static void readWithDirectByteBuffer() throws Exception {
		FileInputStream fis = new FileInputStream(inputFile);
		FileOutputStream fos = new FileOutputStream(outputFile);

		long start = System.nanoTime();

		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fos.getChannel();
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
		while (true) {
			byteBuffer.clear();
			int cnt = inChannel.read(byteBuffer);
			if (cnt < 0)
				break;
			byteBuffer.flip();
			outChannel.write(byteBuffer);
		}
		System.out
				.println("task time=" + (System.nanoTime() - start) / 1000000);
		fis.close();
		fos.close();

	}

	public static void readWithMappedByteBuffer() throws Exception {
		
		
		RandomAccessFile rafin = new RandomAccessFile(inputFile,"rw");
		RandomAccessFile rafout = new RandomAccessFile(inputFile,"rw");
		
		long start = System.nanoTime();

		FileChannel inChannel = rafin.getChannel();
		FileChannel outChannel = rafout.getChannel();
		
		MappedByteBuffer mbbi = inChannel.map(FileChannel.MapMode.READ_WRITE, 0,
				inChannel.size());
		MappedByteBuffer mbbo = inChannel.map(FileChannel.MapMode.READ_WRITE, 0,
				inChannel.size());
		while (true) {
			mbbi.clear();
			int cnt = inChannel.read(mbbi);
			if (cnt < 0)
				break;
			mbbi.flip();
			mbbo.put(mbbi);
			outChannel.write(mbbo);
		}
		System.out
				.println("task time=" + (System.nanoTime() - start) / 1000000);
		rafin.close();
		rafout.close();

	}

	public static void readWithNormalBuffer() throws Exception {
		FileInputStream fis = new FileInputStream(inputFile);
		FileOutputStream fos = new FileOutputStream(outputFile);

		long start = System.nanoTime();
		byte[] buffer = new byte[1024];
		while (true) {
			int cnt = fis.read(buffer);
			if (cnt < 0)
				break;
			fos.write(buffer, 0, cnt);
		}
		System.out
				.println("task time=" + (System.nanoTime() - start) / 1000000);
		fis.close();
		fos.close();
	}
}
