package hp.gtp;

/**
 * over all structure of GTP packet
 * 
 * @author wujianfe
 * 
 */
class GTPPacket {
	byte[] ipHeader = new byte[20];
	byte[] udpHeader = new byte[8];
	byte[] gtpHeader = new byte[6];
	byte[] gtpInforElements;
}

/**
 * overall structure of packet header IP header: 20 bytes; UDP header: 8 bytes;
 * GTP' header: 6 bytes; GTP information elements;
 * 
 * @author wujianfe
 * 
 */
public class GTPDataFormat {
	public static final byte V0_6 = 0x0F;
	public static final byte V0_20 = 0x0E;
	// byte[] gtpHeader = new byte[6];//it may be 20 bytes.
	/**
	 * version: 3 bits Protocol Type: 1 bit Sparse: 3 bits (111) header length
	 * in v0: 1 bit (1:6 bytes header, 0:20 bytes header)
	 */
	byte version; // [1] decide length of head
	// header part has 6 bytes for version 0;
	byte messageType; // [2] enum
	short length; // [3-4]length of information elements (IE); pay-load length.
	short sequenceNumber;// [5-6]
	// body
	InfoElement[] infoElements;

}

class InfoElement {
	/* bit 1 = 0, TV ; bit 1=1, TLV */
	byte type; // IEType
	/*
	 * one IE is usually a TV or TLV just like an attribute is a TLV. in this
	 * case, length is the pay-load, type and length itself is not counted.
	 */
	short length; // IELength
	byte[] content; // = new byte[length];

}

/**
 * extension of GTP, based on two InfoElement Type
 * 
 * @author wujianfe
 * 
 */
class QMInfoElement {
	byte type = (byte) 252;// 0xfc
	short length; // IELength

	byte numOfDataRecord;
	short dataRecordFormat;
	short dataRecordFormatVersion;

	byte lengthOfDataRecord1;
	byte[] dataRecord1;
	// ...
	// byte lengthOfDataRecordN;
	// byte[] dataRecordN;
}

class UserProfileRequestMessage {

}

// The extend mechanism is like the following,
// the IE Type is 255. then use TLV series.

class CSGQuotaManagementMessage {
	short messageType;
	short length;
	TLV[] tlvs;

}

class TLV {
}

class QSQuotaMessage {
	short length;
	short messageType;
	TLV[] tlvs;
}

/**
 * common structure in packet.
 * 
 * @author wujianfe
 * 
 */
class TLV_Byte extends TLV {
	byte type;
	byte length;// including the header (type and length)
	byte[] value;
}

class TLV_Short extends TLV {
	short type;
	short length;// pay load: net length of value;
	byte[] value;
}
