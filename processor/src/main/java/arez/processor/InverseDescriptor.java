package arez.processor;

import arez.processor.support.ProcessorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * Declaration of an inverse.
 */
@SuppressWarnings( "Duplicates" )
final class InverseDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final String _name;
  @Nonnull
  private final List<ExecutableElement> _postInverseAddHooks = new ArrayList<>();
  @Nonnull
  private final List<ExecutableElement> _preInverseRemoveHooks = new ArrayList<>();
  @Nullable
  private ObservableDescriptor _observable;
  @Nullable
  private String _referenceName;
  @Nullable
  private Multiplicity _multiplicity;
  @Nullable
  private TypeElement _targetType;
  @Nullable
  private String _otherName;

  InverseDescriptor( @Nonnull final ComponentDescriptor component, @Nonnull final String name )
  {
    _component = Objects.requireNonNull( component );
    _name = Objects.requireNonNull( name );
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

  boolean hasObservable()
  {
    return null != _observable;
  }

  @Nonnull
  ObservableDescriptor getObservable()
  {
    assert null != _observable;
    return _observable;
  }

  @Nonnull
  String getReferenceName()
  {
    assert null != _referenceName;
    return _referenceName;
  }

  @Nonnull
  Multiplicity getMultiplicity()
  {
    assert null != _multiplicity;
    return _multiplicity;
  }

  @Nonnull
  TypeElement getTargetType()
  {
    assert null != _targetType;
    return _targetType;
  }

  @Nonnull
  String getOtherName()
  {
    assert null != _otherName;
    return _otherName;
  }

  void setInverse( @Nonnull final ObservableDescriptor observable,
                   @Nonnull final String referenceName,
                   @Nonnull final Multiplicity multiplicity,
                   @Nonnull final TypeElement targetType )
  {
    assert null == _observable;
    _observable = Objects.requireNonNull( observable );
    _referenceName = Objects.requireNonNull( referenceName );
    _multiplicity = Objects.requireNonNull( multiplicity );
    _targetType = Objects.requireNonNull( targetType );
    _observable.setInverseDescriptor( this );
    _otherName = ArezUtils.firstCharacterToLowerCase( _targetType.getSimpleName().toString() );
  }

  void addPostInverseAddHook( @Nonnull final ExecutableElement method )
  {
    _postInverseAddHooks.add( method );
  }

  void addPreInverseRemoveHook( @Nonnull final ExecutableElement method )
  {
    _preInverseRemoveHooks.add( method );
  }

  @Nonnull
  List<ExecutableElement> getPostInverseAddHooks()
  {
    return _postInverseAddHooks;
  }

  @Nonnull
  List<ExecutableElement> getPreInverseRemoveHooks()
  {
    return _preInverseRemoveHooks;
  }

  void validate( @Nonnull final ProcessingEnvironment processingEnv )
  {
    if ( null == _observable && !getPreInverseRemoveHooks().isEmpty() )
    {
      throw new ProcessorException( "@PreInverseRemove target with name '" + getName() + "' is not associated " +
                                    "an @Inverse annotated method with the same name",
                                    getPreInverseRemoveHooks().get( 0 ) );
    }
    else if ( null == _observable && !getPostInverseAddHooks().isEmpty() )
    {
      throw new ProcessorException( "@PostInverseAdd target with name '" + getName() + "' is not associated " +
                                    "an @Inverse annotated method with the same name",
                                    getPostInverseAddHooks().get( 0 ) );
    }
    else
    {
      final ObservableDescriptor observable = getObservable();
      if ( observable.requireInitializer() )
      {
        throw new ProcessorException( "@Inverse target also specifies @Observable(initializer=ENABLE) but " +
                                      "it is not valid to define an initializer for an inverse.",
                                      observable.getGetter() );
      }
      for ( final ExecutableElement hook : getPostInverseAddHooks() )
      {
        final TypeMirror paramType = ( (ExecutableType) hook.asType() ).getParameterTypes().get( 0 );
        final TypeMirror targetType = getTargetType().asType();
        if ( !processingEnv.getTypeUtils().isSameType( paramType, targetType ) )
        {
          throw new ProcessorException( "@PostInverseAdd target has a parameter that is not the expected type. " +
                                        "Actual type: " + paramType + " Expected Type: " + targetType,
                                        hook );
        }
      }
      for ( final ExecutableElement hook : getPreInverseRemoveHooks() )
      {
        final TypeMirror paramType = ( (ExecutableType) hook.asType() ).getParameterTypes().get( 0 );
        final TypeMirror targetType = getTargetType().asType();
        if ( !processingEnv.getTypeUtils().isSameType( paramType, targetType ) )
        {
          throw new ProcessorException( "@PreInverseRemove target has a parameter that is not the expected type. " +
                                        "Actual type: " + paramType + " Expected Type: " + targetType,
                                        hook );
        }
      }
    }
  }
}
