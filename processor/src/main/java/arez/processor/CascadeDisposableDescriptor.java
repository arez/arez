package arez.processor;

import com.squareup.javapoet.MethodSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.VariableElement;

/**
 * Declaration of a field that must be cascade disposed.
 */
final class CascadeDisposableDescriptor
{
  @Nonnull
  private final VariableElement _field;

  CascadeDisposableDescriptor( @Nonnull final VariableElement field )
  {
    _field = Objects.requireNonNull( field );
  }

  @Nonnull
  VariableElement getField()
  {
    return _field;
  }

  void buildDisposer( @Nonnull final MethodSpec.Builder builder )
  {
    builder.addStatement( "$T.dispose( $N )",
                          GeneratorUtil.DISPOSABLE_CLASSNAME,
                          getField().getSimpleName().toString() );
  }
}
