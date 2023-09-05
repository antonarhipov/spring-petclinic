package org.springframework.samples.petclinic;

import org.springframework.lang.Nullable;

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

}
