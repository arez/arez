package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.realityforge.proton.AnnotationsUtil;
import org.realityforge.proton.ProcessorException;

/**
 * Declaration of a component's dependency.
 * This dependency can be an <code>@Observable</code> method, a un-annotated getter method or a field.
 */
final class DependencyDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nullable
  private final ExecutableElement _method;
  @Nullable
  private final VariableElement _field;
  private final boolean _cascade;
  @Nullable
  private ObservableDescriptor _observable;

  DependencyDescriptor( @Nonnull final ComponentDescriptor component,
                        @Nonnull final ExecutableElement method,
                        final boolean cascade )
  {
    _component = Objects.requireNonNull( component );
    _method = Objects.requireNonNull( method );
    _field = null;
    _cascade = cascade;
  }

  DependencyDescriptor( @Nonnull final ComponentDescriptor component, @Nonnull final VariableElement field )
  {
    _component = Objects.requireNonNull( component );
    _method = null;
    _field = Objects.requireNonNull( field );
    _cascade = true;
  }

  @Nonnull
  ComponentDescriptor getComponent()
  {
    return _component;
  }

  boolean needsKey()
  {
    return _component.getDependencies().size() > 1 && hasNoObservable();
  }

  String getKeyName()
  {
    return getElement().getSimpleName().toString();
  }

  boolean shouldCascadeDispose()
  {
    return _cascade;
  }

  boolean isMethodDependency()
  {
    return null != _method;
  }

  @Nonnull
  Element getElement()
  {
    return null != _method ? _method : Objects.requireNonNull( _field );
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    assert null != _method;
    return _method;
  }

  @Nonnull
  VariableElement getField()
  {
    assert null != _field;
    return _field;
  }

  void setObservable( @Nonnull final ObservableDescriptor observable )
  {
    _observable = Objects.requireNonNull( observable );
    _observable.setDependencyDescriptor( this );
  }

  boolean hasNoObservable()
  {
    return null == _observable;
  }

  @Nonnull
  ObservableDescriptor getObservable()
  {
    assert null != _observable;
    return _observable;
  }

  void validate()
  {
    assert hasNoObservable() || isMethodDependency();
    if ( !shouldCascadeDispose() && isMethodDependency() )
    {
      if ( hasNoObservable() )
      {
        throw new ProcessorException( "@ComponentDependency target defined an action of 'SET_NULL' but the " +
                                      "dependency is not an observable so the annotation processor does not " +
                                      "know how to set the value to null.", getMethod() );
      }
      else if ( !getObservable().hasSetter() )
      {
        throw new ProcessorException( "@ComponentDependency target defined an action of 'SET_NULL' but the " +
                                      "dependency is an observable with no setter defined so the annotation " +
                                      "processor does not know how to set the value to null.", getMethod() );
      }
      else if ( AnnotationsUtil.hasNonnullAnnotation( getObservable().getSetter().getParameters().get( 0 ) ) )
      {
        throw new ProcessorException( "@ComponentDependency target defined an action of 'SET_NULL' but the " +
                                      "setter is annotated with @javax.annotation.Nonnull.", getMethod() );
      }
    }
  }
}
