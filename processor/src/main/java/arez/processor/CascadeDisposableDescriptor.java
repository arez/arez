package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import org.realityforge.proton.ProcessorException;

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
  @Nullable
  private ReferenceDescriptor _reference;

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

  @Nonnull
  Element getElement()
  {
    final ExecutableElement method = getMethod();
    return null != method ? method : Objects.requireNonNull( getField() );
  }

  void setObservable( @Nonnull final ObservableDescriptor observable )
  {
    _observable = Objects.requireNonNull( observable );
    observable.setCascadeDisposableDescriptor( this );
  }

  void setReference( @Nullable final ReferenceDescriptor reference )
  {
    _reference = Objects.requireNonNull( reference );
    reference.setCascadeDisposableDescriptor( this );
  }

  void validate()
  {
    if ( null != getMethod() )
    {
      if ( null == getObservable() && null == getReference() )
      {
        if ( getMethod().getModifiers().contains( Modifier.ABSTRACT ) )
        {
          throw new ProcessorException( "@CascadeDispose target must not be abstract unless the method is " +
                                        "also annotated with the @Observable or @Reference annotation.",
                                        getMethod() );
        }
      }
      else
      {
        if ( !getMethod().getModifiers().contains( Modifier.ABSTRACT ) )
        {
          throw new ProcessorException( "@CascadeDispose target must be abstract if the method is " +
                                        "also annotated with the @Observable or @Reference annotation.",
                                        getMethod() );
        }
      }
    }
  }

  @Nullable
  ExecutableElement getMethod()
  {
    return _method;
  }

  @Nullable
  VariableElement getField()
  {
    return _field;
  }

  @Nullable
  ObservableDescriptor getObservable()
  {
    return _observable;
  }

  @Nullable
  ReferenceDescriptor getReference()
  {
    return _reference;
  }
}
