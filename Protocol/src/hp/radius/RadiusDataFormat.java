package hp.radius;

public class RadiusDataFormat {
	// code decide the packet type, such as Access Accept, Access Challenge.
	byte code;
	byte identifier;
	byte[] length = new byte[2];
	//short length;
	byte[] authenticator = new byte[16];
	RadiusAttribute[] attributes; 
}

enum RadiusCode {
	Access_Request(1),
    Access_Accept(2),//
    Access_Reject(3),
    Accounting_Request(4),
    Accounting_Response(5),
          Access_Challenge(11),
          Status_Server (12),// experimental
          Status_Client (13),// experimental
         Reserved(255);
	private final int value;

	RadiusCode(int value) {
		this.value = value;
	}
}

