package fr.jfbeuve.webdmx.sd10;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Address {
	public String value();
}
