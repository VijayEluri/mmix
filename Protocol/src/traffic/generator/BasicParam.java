package traffic.generator;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class BasicParam {
	/**
	 * environment specific - maybe overwrite when we execute all the test cases.
	 */
	private String hostName = "localhost";

	private int port = 8080;
	
	/**
	 * Protocol specific 
	 */

	private String socketType = "TCP";

	private String protocol = "GTPP";

	private String version = "1.0";

	private int callTimeout = 2000; // in milliseconds

	/**
	 * load specific 
	 */
	private int messagesPerRound = 1;

	private int intervalBetweenRound = 1000;

	/**
	 * statistics specific
	 */
	private int[] responseTimePartition = new int[] { 5, 10, 20, 50, 100, 200,
			500, 1000 };

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSocketType() {
		return socketType;
	}

	public void setSocketType(String socketType) {
		this.socketType = socketType;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getCallTimeout() {
		return callTimeout;
	}

	public void setCallTimeout(int callTimeout) {
		this.callTimeout = callTimeout;
	}

	public int getSmssagesPerRound() {
		return messagesPerRound;
	}

	public void setSmssagesPerRound(int smssagesPerRound) {
		this.messagesPerRound = smssagesPerRound;
	}

	public int getIntervalBetweenRound() {
		return intervalBetweenRound;
	}

	public void setIntervalBetweenRound(int intervalBetweenRound) {
		this.intervalBetweenRound = intervalBetweenRound;
	}

	public int[] getResponseTimePartition() {
		return responseTimePartition;
	}

	public void setResponseTimePartition(int[] responseTimePartition) {
		this.responseTimePartition = responseTimePartition;
	}

	public String toString() {

		ReflectionToStringBuilder rsb = new ReflectionToStringBuilder(this);
		rsb.append(hostName);
		rsb.append(port);
		rsb.append(socketType);
		rsb.append(protocol);
		rsb.append(version);
		rsb.append(callTimeout);
		rsb.append(messagesPerRound);
		rsb.append(intervalBetweenRound);
		rsb.append(responseTimePartition);
		return rsb.toString();

	}
}
