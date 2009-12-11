package hp.diameter;

public class DiameterDataFormat {
	short version; // 1 byte
	int messageLength; // 3 bytes
	byte commandFlags; // 1 byte. R P E T r r r r
	int commandCode; //3 bytes
	int applicationID; // 4 byte
	
}
