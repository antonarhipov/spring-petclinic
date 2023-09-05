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
		// x *= 3 / 2; // doesn't change x because of the integer division result

		// while (true) {
		// empty body
		// }
	}

	@Retention(RetentionPolicy.SOURCE)
	@interface SourceAnnotation {

	}

}
