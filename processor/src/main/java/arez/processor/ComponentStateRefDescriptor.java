package arez.processor;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

/**
 * Declaration of a method that used to access component state.
 */
final class ComponentStateRefDescriptor
{
  enum State
  {
    CONSTRUCTED,
    COMPLETE,
    READY,
    DISPOSING
  }

  @Nonnull
  private final ExecutableElement _method;
  @Nonnull
  private final State _state;

  ComponentStateRefDescriptor( @Nonnull final ExecutableElement method,
                               @Nonnull final State state )
  {
    _method = Objects.requireNonNull( method );
    _state = Objects.requireNonNull( state );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    builder.addMethod( buildComponentStateRefMethod() );
  }

  @Nonnull
  private MethodSpec buildComponentStateRefMethod()
    throws ProcessorException
  {
    final String methodName = _method.getSimpleName().toString();
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName ).
      addModifiers( Modifier.FINAL ).
      addAnnotation( Override.class ).
      addAnnotation( Generator.NONNULL_CLASSNAME ).
      returns( TypeName.BOOLEAN );

    GeneratorUtil.copyAccessModifiers( _method, method );

    final String stateMethodName =
      State.READY == _state ? "isReady" :
      State.CONSTRUCTED == _state ? "isConstructed" :
      State.COMPLETE == _state ? "isComplete" :
      "isDisposing";

    method.addStatement( "return this.$N.$N()", Generator.KERNEL_FIELD_NAME, stateMethodName );
    return method.build();
  }
}
