//辅助操作类:字节数据流比较
package tupianxiangqi;
import java.util.Vector;
public final class ClsByte
{
	public static byte cstWordFlag=(byte)'\r';
	public static byte cstLineFlag=(byte)'\n';

	public static boolean byteEqual(byte byte1[],byte byte2[])
	{  int i,leng1,leng2;
	   leng1=byte1.length;
	   leng2=byte2.length;
	   for(i=0;i<leng1;i++)
	   {  if(i>=leng2) return false;
		  if(byte1[i]!=byte2[i]) return false;
		  if(byte1[i]==0) return true;
	   }

       if(i<leng2 && byte2[i]!=0) return false;
       return true;
	}//判相等

	public static byte[] byteUnion(byte byte1[],byte byte2[])
	{  int length1=byte1.length;
	   int length2=byte2.length;

	   byte[] data=new byte[length1+length2];

		System.arraycopy(byte1,0,data,0,length1);
		System.arraycopy(byte2,0,data,length1,length2);
	   return data;
	}

	public static boolean byteBeginWith(byte byte1[],byte byte2[])
	{
	   int i,leng1,leng2;
	   leng1=byte1.length;
	   leng2=byte2.length;

	   if(leng1<leng2) return false;

	   for(i=0;i<leng2;i++)
	   { if(byte1[i]!=byte2[i]) return false;
	   }
	   return true;
	}//判断特征吗

	public static int indexOf(byte byte1[],byte data,int index)
	{	if(index<0) index=0;
		if(byte1==null||byte1.length<=index) return -1;

		for(int i=index;i<byte1.length;i++)
		{	if(byte1[i]==data)	return i;
		}
		return -1;
	}

	/**p1 表示单词，p2表示分段
	 */
	public static String[][] translate(byte[] data,byte p1,byte p2)
	{
		if(data==null||data.length<1)
			return null;
		//遍历数据
		int count1=0,count2=0;

		for(int i=0;i<data.length;i++)
		{
			if(data[i]==p1)
			{	count1++;
			   if(data.length>i+1&& data[i+1]==p2)
			   { count2++;
				 i++;
			   }
			}
		}
		if(count1==0||count2==0)
			return null;
		String[][] retData=new String[count2][];

		byte[][] data1=new byte[count1][];

		int indexi=0;
		int index1l=0,index1r=0;
		int index2=0;


		for(int i=0;i<data.length;i++)
		{
			if(data[i]==p1)
			{
				data1[index1r]=new byte[i-indexi];
				System.arraycopy(data,indexi,data1[index1r],0,i-indexi);
				indexi=i+1;
				index1r++;

				if(data.length>i+1&& data[i+1]==p2)
				{
					retData[index2]=new String[index1r-index1l];
					for(int j=index1l;j<index1r;j++)
					{
						retData[index2][j-index1l]=new String(data1[j]);
					}
					index2++;
					index1l=index1r;
					i++;
                                        indexi=i+1;
				}
			}
		}
		return retData;
	}
        public static byte[] tranToByte(byte layer,byte cmd,byte para,String str)
	{
		if(str==null) return null;
		byte[] data=str.getBytes();
		byte[] ret=new byte[3+data.length];
		ret[0]=layer;
		ret[1]=cmd;
		ret[2]=para;
		System.arraycopy(data,0,ret,3,data.length );
		return ret;
	}
}
