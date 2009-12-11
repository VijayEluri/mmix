import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TwoWord
{
 public static void main(String [] args)
 {
   Pattern regex1 = Pattern.compile(
       "\\b([a-z]+)((?:\\s|\\<[^>]+\\>)+)(\\1\\b)",
       Pattern.CASE_INSENSITIVE);
   String replace1 = "\033[7m$1\033[m$2\033[7m$3\033[m";
   Pattern regex2 = Pattern.compile("^(?:[^\\e]*\\n)+", Pattern.MULTILINE);
   Pattern regex3 = Pattern.compile("^([^\\n]+)", Pattern.MULTILINE);

   // For each command-line argument....
   for (int i = 0; i < args.length; i++)
   {
     try {
       BufferedReader in = new BufferedReader(new FileReader(args[i]));
       String text;

       // For each paragraph of each file.....
       while ((text = getPara(in)) != null)
       {
           // Apply the three substitutions
           text = regex1.matcher(text).replaceAll(replace1);
           text = regex2.matcher(text).replaceAll("");
           text = regex3.matcher(text).replaceAll(args[i] + ": $1");

           // Display results
           System.out.print(text);
       }
     } catch (IOException e) {
       System.err.println("can't read ["+args[i]+"]: " + e.getMessage());
     }
   }
 }
  static int count = 0;
 // Routine to read next "paragraph" and return as a string
 static String getPara(BufferedReader in) throws java.io.IOException
 {
   count ++;
   StringBuffer buf = new StringBuffer();
   String line;

   while ((line = in.readLine()) != null &&
          (buf.length() == 0 || line.length() != 0))
   {
       buf.append(line + "\n");
   }
   System.out.print(count + " ");
   System.out.println(buf.toString());
   return  buf.length() == 0 ? null : buf.toString();
 }
}
