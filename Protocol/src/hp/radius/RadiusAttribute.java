package hp.radius;

public class RadiusAttribute {
	byte type;
	byte length;
	byte[] value;
}

enum RadiusAttributeType{
    UserName(1),
    UserPassword(2),
    CHAPPassword(3),
    NASIPAddress(4),
    NASPort(5),
    ServiceType(6),
    FramedProtocol(7),
    FramedIPAddress(8),
    FramedIPNetmask(9),
    FramedRouting(10),
    FilterId(11),
    FramedMTU(12),
    FramedCompression(13),
    LoginIPHost(14),
    LoginService(15),
    LoginTCPPort(16),
    //(unassigned)(17),
    ReplyMessage(18),
    CallbackNumber(19),
    CallbackId(20),
    //(unassigned)(21),
    FramedRoute(22),
    FramedIPXNetwork(23),
    State(24),
    Class(25),
    VendorSpecific(26),
    SessionTimeout(27),
    IdleTimeout(28),
    TerminationAction(29),
    CalledStationId(30),
    CallingStationId(31),
    NASIdentifier(32),
    ProxyState(33),
    LoginLATService(34),
    LoginLATNode(35),
    LoginLATGroup(36),
    FramedAppleTalkLink(37),
    FramedAppleTalkNetwork(38),
    FramedAppleTalkZone(39),
    //40-59   (reserved for accounting)
    CHAPChallenge(60),
    NASPortType(61),
    PortLimit(62),
    LoginLATPort(63);
    
    private final int value;

	RadiusAttributeType(int value) {
		this.value = value;
	}
	
}

