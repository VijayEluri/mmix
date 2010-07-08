package tupianxiangqi;
import java.net.*;
import java.io.*;
import java.util.Observable;
import java.util.zip.*;
public class ClsTCPBase extends Observable implements Runnable
{
	public final static int cstBufferSize=1024;
	public final static int cstTypeNormal=(int)'B';
	public final static int cstTypeExtra=(int)'A';
	public final static int cstMAXLENG=2*1024;

	Socket mySock;
	Thread myThread;

	DataInputStream in;
	DataOutputStream out;

	boolean isStop=false;

	public ClsTCPBase(Socket sock)
	{
		mySock=sock;
	}

	public void start()
	{
		if(myThread!=null)
			stop();
		try
		{
			in=new DataInputStream(mySock.getInputStream());
			out=new DataOutputStream(mySock.getOutputStream());
		}
		catch(Exception e)
		{
			isStop=true;
			return;
		}
		myThread=new Thread(this);
		isStop=false;
		myThread.start();
	}

	public void stop()
	{
		isStop=true;
		myThread=null;

		try
		{
			if(mySock!=null)
			{
				System.out.println( mySock.getInetAddress().getHostAddress()+"退出");
				in.close();
				in=null;
				out.close();
				out=null;
				mySock.close();
				mySock=null;
			}
		}
		catch(Exception e)
		{
		}
	}

	private byte[] doZip(byte [] data,int zip)
	{
		if(zip<=0)
			return data;
		ByteArrayOutputStream fou=new ByteArrayOutputStream();
		//建立gzip解压工作流
		try
		{
			GZIPOutputStream gzou=new GZIPOutputStream(fou);

			gzou.write(data);
			gzou.flush();
			gzou.close();
			byte[] data2=fou.toByteArray();
			fou.close();
			return data2;
		}
		catch(IOException e)
		{	return data;
		}
	}

	private byte[] disGZip(byte[] data,int zip)
	{
		if(zip<=0)
			return data;

		ByteArrayInputStream fin=new ByteArrayInputStream(data);
		//建立gzip解压工作流
		try
		{
			GZIPInputStream gzin=new GZIPInputStream(fin);

			byte[] temp=new byte[this.cstMAXLENG];
			int leng=gzin.read(temp,0,cstMAXLENG);	//数据长度必须小于1024

			if(leng==cstMAXLENG)
			{
				int leng2=0;
				byte[] temp2=new byte[this.cstMAXLENG];

				while((leng2=gzin.read(temp2,0,cstMAXLENG))>0)
				{
					byte[] ttemp=temp;
					temp=new byte[leng+leng2];
					System.arraycopy(ttemp,0,temp,0,leng);
					System.arraycopy(temp2,0,temp,leng,leng2);
					leng+=leng2;
				}
			}
			gzin.close();
			fin.close();
			if(leng>0)
			{	byte[] data2=new byte[leng];
				System.arraycopy(temp,0,data2,0,leng);
				return data2;
			}
			else
			{	return data;
			}
		}
		catch(IOException e)
		{	return data;
		}
	}

	private byte[] doEncry(byte[] data,int encry)
	{
		if(encry>0)
		{
			for(int i=0;i<data.length;i++)
			{	data[i]=(byte)(~data[i]);
				data[i]=(byte)(((data[i]&0x0f)<<4)|((data[i]&0xf0)>>4));
			}
		}
		return data;
	}

	private void disEncrypt(byte[] data,int encry)
	{
		if(encry>0)
		{
			for(int i=0;i<data.length;i++)
			{	data[i]=(byte)(((data[i]&0x0f)<<4)|((data[i]&0xf0)>>4));
				data[i]=(byte)(~data[i]);
			}
		}
	}

	public boolean sendAbsoluteData(byte[] data)
	{
		if(data==null||data.length<=8) return false;

		if(out!=null)
		{	try
			{
				out.write(data);
				out.flush();
			}
			catch(Exception e)
			{	return false;
			}
		}
		return true;
	}

	public byte[] sendData(byte[] data,int zip,int encry)
	{
		if(data==null||data.length<=0) return null;

		byte[] temp;
		byte[] data2;
		int count;

		temp=doZip(data,zip);
		temp=doEncry(temp,encry);
		count=temp.length+8;
		data2=new byte[count];

		data2[0]=(byte)cstTypeExtra;
		data2[1]=(byte)((count&0xff000000)>>24);
		data2[2]=(byte)((count&0x00ff0000)>>16);
		data2[3]=(byte)((count&0x00ff00)>>8);
		data2[4]=(byte)(count&0xff);
		data2[5]=(byte)(zip&0xff);
		data2[6]=(byte)(encry&0xff);
		System.arraycopy(temp,0,data2,8,temp.length);

		if(out!=null)
		{	try
			{
				out.write(data2);
				out.flush();
			}
			catch(Exception e)
			{	return null;
			}
		}
		return data2;
	}

	private void notifyData(byte[] data,int length)
	{
		//System.out.println("接收到"+mySock.getInetAddress().getHostAddress()+"	"+tempData.length+"字节长数据");
		ClsTCPBaseData baseData=null;

		if(tempData[0]==cstTypeExtra)
		{
			byte[] tdata=new byte[length-8];
			System.arraycopy(data,8,tdata,0,tdata.length);
			int zip=data[5];
			int cry=data[6];

			byte[] bdata;
			this.disEncrypt(tdata,cry);

			bdata=this.disGZip(tdata,zip);
			baseData=new ClsTCPBaseData();
			baseData.encry=cry;
			baseData.zip=zip;
			baseData.data=bdata;
			baseData.err=0;
			//sendData((int)(tempData[0]),data,zip,cry);
		}
		else
		{
			baseData=new ClsTCPBaseData();
			baseData.err=ClsTCPBaseData.cstDataError;
			baseData.errStr="数据错误";
		}

		tempData=null;
		isNew=true;
		corLength=0;
		tempLength=0;

		this.setChanged();
		this.notifyObservers(baseData);
		this.clearChanged();
	}

	private void notifyError(int error,String str)
	{
		ClsTCPBaseData baseData=new ClsTCPBaseData();
		baseData.err=error;
		baseData.errStr=str;

		tempData=null;
		isNew=true;
		corLength=0;
		tempLength=0;

		this.setChanged();
		this.notifyObservers(baseData);
		this.clearChanged();
	}

	boolean isNew=true;
	byte[] tempData;
	int corLength=0;
	int tempLength=0;

	private void readData(byte[] data,int beg,int length)	//初步解释数据返回false表示一次数据结束
	{
		int error=0;

		if(isNew)
		{
			if(data[beg]==(byte)cstTypeExtra)
			{
				if(length<=8)
					error=ClsTCPBaseData.cstDataError;
				else
				{
					ByteArrayInputStream d=new ByteArrayInputStream(data,beg,length);
					DataInputStream dd=new DataInputStream(d);
					try
					{
						dd.skip(1);
						corLength=dd.readInt();
						tempData=new byte[corLength];
						if(corLength<=length)
						{
							int cLength=corLength;
							System.arraycopy(data,beg,tempData,0,corLength);
							notifyData(tempData,corLength);
							if(cLength<length)
							{
								readData(data,beg+cLength,length-cLength);
							}
						}
						else
						{
							System.arraycopy(data,beg,tempData,0,length);
							tempLength=length;
							isNew=false;
						}
					}
					catch(Exception e)
					{
					}
				}
			}
			else
			{
				error=ClsTCPBaseData.cstDataError;
			}
			if(error!=0)
			{
				notifyError(error,"数据错误");
			}
		}
		else
		{
			if(tempData==null)
				error=ClsTCPBaseData.cstDataError;
			else
			{

				if(corLength>tempLength+length)
				{	System.arraycopy(data,0,tempData,tempLength,length);
					tempLength+=length;
				}
				else if(corLength==tempLength+length)
				{
					System.arraycopy(data,0,tempData,tempLength,length);
					tempLength+=length;
					notifyData(tempData,corLength);
				}
				else
				{
					System.arraycopy(data,0,tempData,tempLength,corLength-tempLength);

					int l1=corLength;
					int l2=tempLength;
					tempLength=corLength;
					notifyData(tempData,corLength);
					readData(data,l1-l2,length-(l1-l2));
				}
			}
		}
	}

	public void run()
	{
		//接收数据
		byte[] data=new byte[cstBufferSize];
		int count=-1;

			try
			{
				while(!isStop&&(count=in.read(data))>0)
				{
					readData(data,0,count);
				}
				if(count<0)
					notifyError(ClsTCPBaseData.cstOutLineError,"服务复位");
			}
			catch(SocketException e1)
			{
				notifyError(ClsTCPBaseData.cstOutLineError,"断线错误");
			}
			catch(IOException e3)
			{
				notifyError(ClsTCPBaseData.cstTimeError,"超时错误");
			}
			catch(Exception e2)
			{
				notifyError(ClsTCPBaseData.cstOtherError,"其他错误");
			}
			stop();
	}

	public void finalize()
	{
		stop();
	}
}
