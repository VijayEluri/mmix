package tupianxiangqi;
public final class ClsTCPBaseData
{
	public static final int cstNOError=0;
	public static final int cstTimeError=1;
	public static final int cstDataError=2;
	public static final int cstOutLineError=3;
	public static final int cstOtherError=4;

	public byte[] data;
	public int zip;
	public int encry;
	public int err;
	public String errStr;
}
