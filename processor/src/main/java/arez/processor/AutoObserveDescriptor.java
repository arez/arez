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
 * Declaration of a method or field that must be observed for the lifetime of the component.
 */
final class AutoObserveDescriptor
{
  private final boolean _validateTypeAtRuntime;
  @Nullable
  private final ExecutableElement _method;
  @Nullable
  private final VariableElement _field;
  @Nullable
  private ObservableDescriptor _observable;
  @Nullable
  private ReferenceDescriptor _reference;

  AutoObserveDescriptor( final boolean validateTypeAtRuntime, @Nonnull final VariableElement field )
  {
    _validateTypeAtRuntime = validateTypeAtRuntime;
    _method = null;
    _field = Objects.requireNonNull( field );
  }

  AutoObserveDescriptor( final boolean validateTypeAtRuntime,
                         @Nonnull final ExecutableElement method,
                         @Nullable final ObservableDescriptor observable )
  {
    _validateTypeAtRuntime = validateTypeAtRuntime;
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
    observable.setAutoObserveDescriptor( this );
  }

  void setReference( @Nonnull final ReferenceDescriptor reference )
  {
    _reference = Objects.requireNonNull( reference );
    reference.setAutoObserveDescriptor( this );
  }

  void validate()
  {
    final ExecutableElement method = getMethod();
    if ( null != method )
    {
      if ( null == getObservable() && null == getReference() )
      {
        if ( method.getModifiers().contains( Modifier.ABSTRACT ) )
        {
          throw new ProcessorException( "@AutoObserve target must not be abstract unless the method is " +
                                        "also annotated with the @Observable or @Reference annotation.",
                                        method );
        }
      }
      else if ( !method.getModifiers().contains( Modifier.ABSTRACT ) )
      {
        throw new ProcessorException( "@AutoObserve target must be abstract if the method is " +
                                      "also annotated with the @Observable or @Reference annotation.",
                                      method );
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

  boolean isValidateTypeAtRuntime()
  {
    return _validateTypeAtRuntime;
  }
}
