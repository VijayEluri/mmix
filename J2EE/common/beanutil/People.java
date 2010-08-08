package common.beanutil;

import java.util.Arrays;
import java.util.List;

public class People extends Animal{
	int age =18;
	String _name="beauty";
	List<String> friends=Arrays.asList("a","b");
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getname() {
		return _name;
	}
	public void setname(String name) {
		_name = name;
	}
	public List<String> getFriends() {
		return friends;
	}
	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
	@Override
	public String toString() {
		return "People [_name=" + _name + ", age=" + age + ", friends="
				+ friends + "]";
	}
	
}
