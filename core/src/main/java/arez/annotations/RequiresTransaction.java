package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation that marks a method as requiring an existing Arez transaction.
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 *
 * <p>This annotation is only supported on methods contained within a type annotated by
 * {@link ArezComponent} or {@link ArezComponentLike}. Other usages will fail compilation.</p>
 *
 * <p>Unlike {@link Action}, this annotation never creates or wraps a transaction and instead verifies
 * that the caller has already established a transaction with the required characteristics.</p>
 */
@Documented
@Target( ElementType.METHOD )
public @interface RequiresTransaction
{
  /**
   * Return the required mode of the existing transaction.
   *
   * @return the required mode of the existing transaction.
   */
  @Nonnull
  TransactionMode mode() default TransactionMode.ANY;

  /**
   * Return the required tracking state of the existing transaction.
   *
   * @return the required tracking state of the existing transaction.
   */
  @Nonnull
  TrackingMode tracking() default TrackingMode.ANY;
}
