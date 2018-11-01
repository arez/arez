package arez.processor;

import com.squareup.javapoet.MethodSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * Declaration of a field that must be cascade disposed.
 */
final class CascadeDisposableDescriptor
{
  @Nullable
  private final ExecutableElement _method;
  @Nullable
  private final VariableElement _field;

  CascadeDisposableDescriptor( @Nonnull final VariableElement field )
  {
    _method = null;
    _field = Objects.requireNonNull( field );
  }

  CascadeDisposableDescriptor( @Nonnull final ExecutableElement method )
  {
    _method = Objects.requireNonNull( method );
    _field = null;
  }

  void buildDisposer( @Nonnull final MethodSpec.Builder builder )
  {
    if ( null != _field )
    {
      builder.addStatement( "$T.dispose( $N )", GeneratorUtil.DISPOSABLE_CLASSNAME, _field.getSimpleName().toString() );
    }
    else
    {
      assert null != _method;
      builder.addStatement( "$T.dispose( $N() )",
                            GeneratorUtil.DISPOSABLE_CLASSNAME,
                            _method.getSimpleName().toString() );
    }
  }
}
