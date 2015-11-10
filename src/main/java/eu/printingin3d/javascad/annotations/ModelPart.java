package eu.printingin3d.javascad.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used by the 
 * {@link eu.printingin3d.javascad.utils.AnnotatedModelProvider AnnotatedModelProvider} class to identify
 * the methods which returns with a printable part.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModelPart {
	/**
	 * Returns with the name of the output file. The filename will be the methods name
	 * if this value is omitted.
	 * @return the name of the output file
	 */
	String value() default "";
}
