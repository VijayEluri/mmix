package hp.gtp;
public class Parser {
	public static void main(String[] args){
		String[] input = new String[]{
				
				"HTTP-Header",
				"HTTP-Statistics",
				"TCP",
				"UDP",
				"IP", 
				"HTTP-Statistics-Intermediate",
				"TCP-Intermediate",
				"UDP-Intermediate",
				"IP-Intermediate",
				"Quato-Download",
				"Termination-Action",
				"Called-Station-Id",
				"Calling-Station-Id",
				"NAS-Identifier",
				"Proxy-State",
				"Login-LAT-Service",
				"Login-LAT-Node",
				"Login-LAT-Group",
				"Framed-AppleTalk-Link", 
				"Framed-AppleTalk-Network",
				"Framed-AppleTalk-Zone",
				"CHAP-Challenge string",
				"NAS-Port-Type",
				"Port-Limit",
				"Login-LAT-Port"
		};
		for(String t : input){
			String[] temp = t.split("-");
			// <field name="loginTcpPort" type="Login_Tcp_Port_Type" />
			String a="";
			String b="";
			for(int i=0;i<temp.length;i++){
				String tt=temp[i];
				b+=tt;
				b+="_";
				if(i==0){
					tt=tt.toLowerCase();
				}
				a+=tt;
				
			}
			a = a.substring(0, 1).toLowerCase()+a.substring(1);
			//b=b.substring(0, b.length()-1);
			
			System.out.println("<field name=\""+a+"\" type=\""+b+"Type\" />");
			
		}
	}
}
