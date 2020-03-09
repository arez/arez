package arez.testng;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to test methods that indicate that observer errors should not cause the test to fail.
 * Instead the errors should be collected and added to any fields of type {@link ObserverErrorCollector} that
 * are contained in the test.
 */
@Documented
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface CollectObserverErrors
{
}
