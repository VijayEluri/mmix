package common.beanutil;

import junit.framework.TestCase;

/**
 * could be used to somplify the code.
 * @author Wu Jianfeng
 *
 */
public class TestPropertyUtils extends TestCase{
	public void test() throws Exception {
		People bean = new People();
		String name = "age";
		Object object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);
		Object value = 23456;
		PropertyUtils.setSimpleProperty(bean, name, value);
		object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);

		name = "name";
		object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);
		value = "Eddie set";
		PropertyUtils.setSimpleProperty(bean, name, value);
		object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);
		
		name = "friends";
		object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);
		
		//=== parent field
		name = "nickName1";
		object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);
		value = "Eddie set - Parent";
		PropertyUtils.setSimpleProperty(bean, name, value);
		object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);
		
		name = "nickName2";
		object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);
		value = "Eddie set-Parent";
		PropertyUtils.setSimpleProperty(bean, name, value);
		object = PropertyUtils.getProperty(bean, name);
		System.out.println(object);
		
	}
}
