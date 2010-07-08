package tupianxiangqi;
import java.net.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

public class ClsClient extends Observable implements Observer
{
	Socket mySock;
	ClsTCPBase myBase;
	String IP;
	int port;
	public int WaitTime;

	ChkLink myChkLink;

	class ChkLink extends Thread
	{
		ClsClient sender;
		long sleepTime;
                boolean running=true;
		public ChkLink(ClsClient d,long sleepTime)
		{
			sender=d;
			this.sleepTime=sleepTime;

		}

                public void stopRun()
                {
                    running=false;
                    try
                    {
                        this.interrupt();
                    }
                    catch(Exception e)
                    {
                    }
                }

		public void run()
		{
			try
			{
				if(sender!=null)
					while(running)
					{
						this.sleep(sleepTime);
						byte[] data=new byte[3];
						data[2]=(byte)1;
						sender.sendData(data);
					}
			}
			catch(Exception e)
			{
			}
		}
	}

        public void login(int roomID,int userID,String user,String pass)
        {

        }

        public void logout()
        {

        }

        public void setAutoSentdata(boolean data)
        {
            if(WaitTime>1000)
            {
                if(myChkLink!=myChkLink)
                    myChkLink.stopRun();
                if(data)
                {
                    myChkLink=new ChkLink(this,WaitTime*2/3);
                    myChkLink.start();
                }
            }
        }
	public ClsClient(String ip,int port,int time)
	{
		IP=ip;
		this.port=port;
		WaitTime=time;
	}

	private void translateData(ClsTCPBaseData data)
	{
		if(data.data[0]==0)
		{
			if(data.data[1]==0)		//测试断线  获取网路速度
			{
				if(data.data[2]==1)
				{
					data.data[2]=(byte)2;
					sendData(data.data);
				}
			}
		}
		else
		{
			this.setChanged();
			this.notifyObservers(data);
			this.clearChanged();
		}
	}

	public void update(Observable obs,Object obj)
	{
		if(obj instanceof ClsTCPBaseData)
		{
			ClsTCPBaseData data=(ClsTCPBaseData)obj;
			//增加代码解释模块
			if(data.err==0)
			{
				if(data.data==null||data.data.length<3)
					return;
				translateData(data);
			}
			else
			{
				this.setChanged();
				this.notifyObservers(data);
				this.clearChanged();
			}
		}
	}

	public void sendData(byte[] data)
	{
		if(data.length>=ClsTCPBase.cstBufferSize)
			sendData(data,1,0);
		else
			sendData(data,0,0);
	}


	public synchronized void sendData(byte[] data,int zip,int cry)
	{
		if(myBase!=null)
			myBase.sendData(data,zip,cry);
	}

	public synchronized String  start()
	{
		if(myBase!=null)
		{	myBase.stop();
			myBase.deleteObserver(this);
		}
		try
		{
			mySock=new Socket(IP,port);
			myBase=new ClsTCPBase(mySock);
			myBase.addObserver(this);
			myBase.start();
			/*
			if(WaitTime<1000)
				WaitTime=0;
			else
			{
				try
				{
					mySock.setSoTimeout(WaitTime);
				}
				catch(SocketException e2)
				{
				}
			}*/
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
	   return null;
	}

	public synchronized void stop()
	{
		if(myChkLink!=null)
		{	myChkLink.stopRun();
			myChkLink=null;
		}
		if(myBase!=null)
		{	myBase.stop();
			myBase.deleteObserver(this);
			myBase=null;
			this.deleteObservers();
		}
	}

	public void finalize()
	{
		stop();
	}
}
