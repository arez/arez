package arez.processor;

import com.squareup.javapoet.MethodSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Declaration of a method or field that must be cascade disposed.
 */
final class CascadeDisposableDescriptor
{
  @Nullable
  private final ExecutableElement _method;
  @Nullable
  private final VariableElement _field;
  @Nullable
  private ObservableDescriptor _observable;

  CascadeDisposableDescriptor( @Nonnull final VariableElement field )
  {
    _method = null;
    _field = Objects.requireNonNull( field );
  }

  CascadeDisposableDescriptor( @Nonnull final ExecutableElement method,
                               @Nullable final ObservableDescriptor observable )
  {
    _method = Objects.requireNonNull( method );
    _field = null;
    if ( null != observable )
    {
      setObservable( observable );
    }
  }

  void setObservable( @Nonnull final ObservableDescriptor observable )
  {
    _observable = Objects.requireNonNull( observable );
    observable.setCascadeDisposableDescriptor( this );
  }

  void buildDisposer( @Nonnull final MethodSpec.Builder builder )
  {
    if ( null != _field )
    {
      builder.addStatement( "$T.dispose( $N )", Generator.DISPOSABLE_CLASSNAME, _field.getSimpleName().toString() );
    }
    else
    {
      assert null != _method;
      if ( null == _observable )
      {
        builder.addStatement( "$T.dispose( $N() )",
                              Generator.DISPOSABLE_CLASSNAME,
                              _method.getSimpleName().toString() );
      }
      else
      {
        builder.addStatement( "$T.dispose( $N )", Generator.DISPOSABLE_CLASSNAME, _observable.getDataFieldName() );

      }
    }
  }

  void validate()
  {
    if ( null != _method )
    {
      if ( null == _observable )
      {
        if ( _method.getModifiers().contains( Modifier.ABSTRACT ) )
        {
          throw new ArezProcessorException( "@CascadeDispose target must not be abstract unless " +
                                            "the method is also annotated with the @Observable annotation.",
                                            _method );
        }
        if ( ElementKind.CLASS == _method.getEnclosingElement().getKind() )
        {
          MethodChecks.mustBeFinal( Constants.CASCADE_DISPOSE_ANNOTATION_CLASSNAME, _method );
        }
      }
      else
      {
        if ( !_method.getModifiers().contains( Modifier.ABSTRACT ) )
        {
          throw new ArezProcessorException( "@CascadeDispose target must be abstract if " +
                                            "the method is also annotated with the @Observable annotation.",
                                            _method );
        }
      }
    }
  }
}
