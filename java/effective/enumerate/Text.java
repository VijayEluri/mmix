package effective.enumerate;

import java.util.EnumSet;
import java.util.Set;

public class Text {
	public enum Style {
		BOLD, ITALIC, UNDERLINE, STRIKETHROUGH
	}

	// Any Set could be passed in, but EnumSet is clearly best
	public void applyStyles(Set<Style> styles) {
		for (Style style : styles) {
			System.out.println("YOu have chosen: " + style);
			System.out.println("YOu have chosen: " + style.name());
			System.out.println("YOu have chosen: " + style.ordinal());
		}
	}
	
	public static void main(String[] args){
		Text text = new Text();
		text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
	}
}