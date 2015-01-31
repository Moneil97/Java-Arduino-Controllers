package arduino;

import java.util.regex.Pattern;

public class testing {

	public testing() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		System.out.println(Pattern.matches("\\d{1,3} \\d{1,3} \\d{1,3}", "255 255 255"));
	}

}
