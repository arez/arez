package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;

/**
 * Declaration of a component's dependency.
 * This dependency can be an {@link arez.annotations.Observable} or just a vanilla getter method.
 */
final class DependencyDescriptor
{
  @Nonnull
  private final ExecutableElement _method;
  private final boolean _cascade;
  @Nullable
  private ObservableDescriptor _observable;

  DependencyDescriptor( @Nonnull final ExecutableElement method,
                        final boolean cascade )
  {
    _method = Objects.requireNonNull( method );
    _cascade = cascade;
  }

  boolean shouldCascadeDispose()
  {
    return _cascade;
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    return _method;
  }

  void setObservable( @Nonnull final ObservableDescriptor observable )
  {
    _observable = Objects.requireNonNull( observable );
  }

  @Nonnull
  ObservableDescriptor getObservable()
  {
    assert null != _observable;
    return _observable;
  }

  void validate()
  {
    if ( !shouldCascadeDispose() )
    {
      if ( null == _observable )
      {
        throw new ArezProcessorException( "@Dependency target defined an action of 'SET_NULL' but the dependency is " +
                                          "not an observable so the annotation processor does not know how to set " +
                                          "the value to null.", _method );
      }
      else if ( !_observable.hasSetter() )
      {
        throw new ArezProcessorException( "@Dependency target defined an action of 'SET_NULL' but the dependency is " +
                                          "an observable with no setter defined so the annotation processor does " +
                                          "not know how to set the value to null.", _method );
      }
      else if ( null != ProcessorUtil.findAnnotationByType( _observable.getSetter().getParameters().get( 0 ),
                                                            Constants.NONNULL_ANNOTATION_CLASSNAME ) )
      {
        throw new ArezProcessorException( "@Dependency target defined an action of 'SET_NULL' but the setter is " +
                                          "annotated with @javax.annotation.Nonnull.", _method );
      }
    }
  }
}
