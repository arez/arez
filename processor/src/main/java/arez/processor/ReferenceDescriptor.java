package arez.processor;

import arez.processor.support.AnnotationsUtil;
import arez.processor.support.GeneratorUtil;
import arez.processor.support.ProcessorException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;

/**
 * Declaration of a reference.
 */
@SuppressWarnings( "Duplicates" )
final class ReferenceDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _method;
  @Nullable
  private ExecutableType _methodType;
  @Nullable
  private String _linkType;
  @Nullable
  private ObservableDescriptor _observable;
  @Nullable
  private ExecutableElement _idMethod;
  @Nullable
  private String _inverseName;
  @Nullable
  private Multiplicity _inverseMultiplicity;
  @Nullable
  private CascadeDisposableDescriptor _cascadeDisposableDescriptor;

  ReferenceDescriptor( @Nonnull final ComponentDescriptor component, @Nonnull final String name )
  {
    _component = Objects.requireNonNull( component );
    _name = Objects.requireNonNull( name );
  }

  void setIdMethod( @Nonnull final ExecutableElement method )
  {
    assert null == _idMethod;
    _idMethod = Objects.requireNonNull( method );
  }

  void setObservable( @Nonnull final ObservableDescriptor observable )
  {
    setIdMethod( observable.getGetter() );
    assert null == _observable;
    _observable = observable;
    _observable.setReferenceDescriptor( this );
  }

  void setMethod( @Nonnull final ExecutableElement method,
                  @Nonnull final ExecutableType methodType,
                  @Nonnull final String linkType,
                  @Nullable final String inverseName,
                  @Nullable final Multiplicity inverseMultiplicity )
  {
    assert null == _method;
    assert null == _methodType;
    assert null == _linkType;
    assert null == _inverseName;
    assert null == _inverseMultiplicity;
    assert ( null == inverseName && null == inverseMultiplicity ) ||
           ( null != inverseName && null != inverseMultiplicity );
    _method = Objects.requireNonNull( method );
    _methodType = Objects.requireNonNull( methodType );
    _linkType = Objects.requireNonNull( linkType );
    _inverseName = inverseName;
    _inverseMultiplicity = inverseMultiplicity;
  }

  @Nonnull
  String getLinkType()
  {
    assert null != _linkType;
    return _linkType;
  }

  @Nullable
  CascadeDisposableDescriptor getCascadeDisposableDescriptor()
  {
    return _cascadeDisposableDescriptor;
  }

  void setCascadeDisposableDescriptor( @Nonnull final CascadeDisposableDescriptor cascadeDisposableDescriptor )
  {
    _cascadeDisposableDescriptor = Objects.requireNonNull( cascadeDisposableDescriptor );
  }

  @Nonnull
  ComponentDescriptor getComponent()
  {
    return _component;
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  ExecutableType getMethodType()
  {
    assert null != _methodType;
    return _methodType;
  }

  @Nullable
  ObservableDescriptor getObservable()
  {
    return _observable;
  }

  boolean hasMethod()
  {
    return null != _method;
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    assert null != _method;
    return _method;
  }

  @Nonnull
  ExecutableElement getIdMethod()
  {
    assert null != _idMethod;
    return _idMethod;
  }

  @Nonnull
  String getLinkMethodName()
  {
    return ComponentGenerator.getLinkMethodName( getName() );
  }

  @Nonnull
  String getDelinkMethodName()
  {
    return ComponentGenerator.getDelinkMethodName( getName() );
  }

  @Nonnull
  String getFieldName()
  {
    return ComponentGenerator.REFERENCE_FIELD_PREFIX + _name;
  }

  boolean hasInverse()
  {
    return null != _inverseName;
  }

  @Nonnull
  String getInverseName()
  {
    assert null != _inverseName;
    return _inverseName;
  }

  @Nonnull
  Multiplicity getInverseMultiplicity()
  {
    assert null != _inverseMultiplicity;
    return _inverseMultiplicity;
  }

  boolean isNullable()
  {
    return !getIdMethod().getReturnType().getKind().isPrimitive() &&
           !AnnotationsUtil.hasNonnullAnnotation( getIdMethod() );
  }

  @Nonnull
  ClassName getArezClassName()
  {
    final ClassName other = (ClassName) TypeName.get( getMethod().getReturnType() );
    return GeneratorUtil.getGeneratedClassName( other, "Arez_", "" );
  }

  void validate()
    throws ProcessorException
  {
    if ( null == _idMethod )
    {
      assert null != _method;
      throw new ProcessorException( "@Reference exists but there is no corresponding @ReferenceId", _method );
    }
    else if ( null == _method )
    {
      throw new ProcessorException( "@ReferenceId exists but there is no corresponding @Reference", _idMethod );
    }
    else if ( null != _observable && !_observable.hasSetter() )
    {
      throw new ProcessorException( "@ReferenceId added to @Observable method but expectSetter = false on " +
                                    "property which is not compatible with @ReferenceId", _idMethod );
    }
  }
}
