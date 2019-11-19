package arez.processor;

import com.squareup.javapoet.TypeSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

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

  void buildMethods( @Nonnull final ProcessingEnvironment processingEnv,
                     @Nonnull final TypeElement typeElement,
                     @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    final String stateMethodName =
      State.READY == _state ? "isReady" :
      State.CONSTRUCTED == _state ? "isConstructed" :
      State.COMPLETE == _state ? "isComplete" :
      "isDisposing";

    builder.addMethod( Generator
                         .refMethod( processingEnv, typeElement, _method )
                         .addStatement( "return this.$N.$N()", Generator.KERNEL_FIELD_NAME, stateMethodName )
                         .build() );
  }
}
