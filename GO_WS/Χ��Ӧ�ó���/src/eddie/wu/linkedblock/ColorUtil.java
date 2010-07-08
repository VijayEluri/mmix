/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.linkedblock;

/**
 * @author eddie
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ColorUtil {

	/**
	 * color for single point 
	 */
	public final static byte BLANK_POINT = 0; //0��ʾ�հ׵�.

	public final static byte BLACK = 1; //1��ʾ����;

	public final static byte WHITE = 2; //2��ʾ����;

	public final static byte Mixture = 3; //3��ʾ��ͨ�Ŀհٿ�.��������;

	//������û�м���ȷ��.�൱��UNKnown
	public final static byte OutOfBound = 10; //2��ʾ����;

	/**
	 * ��shoushu����֮ǰ���ã�yise��tongse�ļ���������ͬ��
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getNextStepColor(short shoushu) {

		byte tongse = (byte) (shoushu % 2 + 1);
		return tongse;
	}

	/**
	 * ��shoushu����֮ǰ���ã�yise��tongse�ļ���������ͬ��
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getNextStepEnemyColor(short shoushu) {
		byte yise = (byte) ((1 + shoushu) % 2 + 1);
		return yise;
	}

	/**
	 * ���������ж����ӵ����ɫ�������Ѿ����ӡ�
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getCurrentStepColor(short shoushu) {

		byte tongse = (byte) ((1 + shoushu) % 2 + 1); //tong se=1��2,�׺���Ϊż��
		return tongse;
	}

	/**
	 * ���������ж����ӵ����ɫ�������Ѿ����ӡ�
	 * 
	 * @param shoushu
	 * @return
	 */
	public static byte getCurrentStepEnemyColor(short shoushu) {
		byte yise = (byte) (shoushu % 2 + 1); //yi se=1��2,������Ϊ����
		return yise;
	}
}