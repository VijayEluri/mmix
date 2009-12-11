
package api.reg;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Longest {
	public static void main(String args[]) {
		if (args.length != 1) {
			System.err.println("Provide a filename");
			return;
		}

		try {
			// Map File from filename to byte buffer
			FileInputStream input = new FileInputStream(args[0]);
			FileChannel channel = input.getChannel();
			int fileLength = (int) channel.size();
			MappedByteBuffer buffer = channel.map(
					FileChannel.MapMode.READ_ONLY, 0, fileLength);

			// Convert to character buffer
			Charset charset = Charset.forName("ISO-8859-1");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(buffer);

			// Create line pattern
			//Pattern linePattern = Pattern.compile(".*$", Pattern.MULTILINE);
			Pattern linePattern = Pattern.compile(".*$");
			// Create word pattern
			Pattern wordBreakPattern = Pattern.compile("[\\p{Punct}\\s}]");
			System.out.println(wordBreakPattern);

			// Match line pattern to buffer
			Matcher lineMatcher = linePattern.matcher(charBuffer);

			// Holder for longest word
			String longest = "";
			int lineCount = 0;
			// For each line
			while (lineMatcher.find()) {
				lineCount++;
				// Get line
				String line = lineMatcher.group();
				System.out.println(line);
				// Get array of words on line
				String words[] = wordBreakPattern.split(line);
				for (String temp : words) {

					System.out.println(temp);
				}
				// Look for longest word
				for (int i = 0, n = words.length; i < n; i++) {
					if (words[i].length() > longest.length()) {
						longest = words[i];
					}
				}
			}
			// Report
			System.out.println("Longest word: " + longest);
			System.out.println("Total: " + lineCount + " lines.");
			// Close
			input.close();
		} catch (IOException e) {
			System.err.println("Error processing");
		}
	}
}
