package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;

/**
 * Declaration of an inverse.
 */
@SuppressWarnings( "Duplicates" )
final class InverseDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final ObservableDescriptor _observable;
  @Nonnull
  private final String _referenceName;
  @Nonnull
  private final Multiplicity _multiplicity;
  @Nonnull
  private final TypeElement _targetType;
  @Nonnull
  private final String _otherName;

  InverseDescriptor( @Nonnull final ComponentDescriptor component,
                     @Nonnull final ObservableDescriptor observable,
                     @Nonnull final String referenceName,
                     @Nonnull final Multiplicity multiplicity,
                     @Nonnull final TypeElement targetType )
  {
    _component = Objects.requireNonNull( component );
    _observable = Objects.requireNonNull( observable );
    _referenceName = Objects.requireNonNull( referenceName );
    _multiplicity = Objects.requireNonNull( multiplicity );
    _targetType = Objects.requireNonNull( targetType );
    _observable.setInverseDescriptor( this );
    _otherName = ProcessorUtil.firstCharacterToLowerCase( _targetType.getSimpleName().toString() );
  }

  @Nonnull
  ComponentDescriptor getComponent()
  {
    return _component;
  }

  @Nonnull
  ObservableDescriptor getObservable()
  {
    return _observable;
  }

  @Nonnull
  String getReferenceName()
  {
    return _referenceName;
  }

  @Nonnull
  Multiplicity getMultiplicity()
  {
    return _multiplicity;
  }

  @Nonnull
  TypeElement getTargetType()
  {
    return _targetType;
  }

  @Nonnull
  String getOtherName()
  {
    return _otherName;
  }

  void validate()
  {
    if ( getObservable().requireInitializer() )
    {
      throw new ProcessorException( "@Inverse target also specifies @Observable(initializer=ENABLE) but " +
                                    "it is not valid to define an initializer for an inverse.",
                                    _observable.getGetter() );
    }
  }

}
