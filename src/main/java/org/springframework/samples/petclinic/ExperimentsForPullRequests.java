package org.springframework.samples.petclinic;

import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

public class ExperimentsForPullRequests {

	public static void foo(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo2(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo3(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo4(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo5(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo6(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo7(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo8(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo9(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo10(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo11(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo12(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo13(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo14(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo15(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo16(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo17(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo18(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo19(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo20(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo21(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo22(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo23(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo24(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo25(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo26(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo27(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo28(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo29(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo30(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo31(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo32(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo33(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo34(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo35(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo36(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo37(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void foo38(@Nullable String s) {
		System.out.println("String length: " + s.length());
	}

	public static void bar() {
		while (true) {
			System.out.println("test");
		}
	}

	public static void baz(Optional<String> os) {
		System.out.println(os.get());
		boo();
	}

	@Deprecated(forRemoval = true)
	public static String boo() {
		System.out.println();
		return "";
	}

	public void catchAndPrint() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("file"));
			String string = reader.readLine();
			System.out.println(string);
		}
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}

		int x = 18;
		x *= 3 / 2; // doesn't change x because of the integer division result

		while (true) {
		}
	}

}
