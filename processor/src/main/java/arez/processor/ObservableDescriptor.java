package arez.processor;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.realityforge.proton.AnnotationsUtil;
import org.realityforge.proton.ProcessorException;

/**
 * The class that represents the parsed state of ObservableValue properties on a @ArezComponent annotated class.
 */
final class ObservableDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final String _name;
  private boolean _expectSetter;
  private String _readOutsideTransaction = "AUTODETECT";
  private String _writeOutsideTransaction = "AUTODETECT";
  private boolean _setterAlwaysMutates;
  private Boolean _initializer;
  @Nullable
  private ExecutableElement _getter;
  @Nullable
  private ExecutableType _getterType;
  @Nullable
  private ExecutableElement _setter;
  @Nullable
  private ExecutableType _setterType;
  @Nonnull
  private final List<CandidateMethod> _refMethods = new ArrayList<>();
  @Nullable
  private DependencyDescriptor _dependencyDescriptor;
  @Nullable
  private ReferenceDescriptor _referenceDescriptor;
  @Nullable
  private InverseDescriptor _inverseDescriptor;
  @Nullable
  private CascadeDisposeDescriptor _cascadeDisposeDescriptor;

  ObservableDescriptor( @Nonnull final ComponentDescriptor component, @Nonnull final String name )
  {
    _component = Objects.requireNonNull( component );
    _name = Objects.requireNonNull( name );
    setExpectSetter( true );
    setSetterAlwaysMutates( true );
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

  boolean requireInitializer()
  {
    assert null != _initializer;
    return _initializer;
  }

  @Nullable
  Boolean getInitializer()
  {
    return _initializer;
  }

  void setInitializer( @Nonnull final Boolean initializer )
  {
    _initializer = Objects.requireNonNull( initializer );
  }

  void setSetterAlwaysMutates( final boolean setterAlwaysMutates )
  {
    _setterAlwaysMutates = setterAlwaysMutates;
  }

  void setReadOutsideTransaction( final String readOutsideTransaction )
  {
    _readOutsideTransaction = readOutsideTransaction;
  }

  boolean canReadOutsideTransaction()
  {
    return "ENABLE".equals( _readOutsideTransaction ) ||
           ( "AUTODETECT".equals( _readOutsideTransaction ) && getComponent().defaultReadOutsideTransaction() );
  }

  boolean canWriteOutsideTransaction()
  {
    return "ENABLE".equals( _writeOutsideTransaction ) ||
           ( "AUTODETECT".equals( _writeOutsideTransaction ) && getComponent().defaultWriteOutsideTransaction() );
  }

  void setWriteOutsideTransaction( @Nonnull final String writeOutsideTransaction )
  {
    _writeOutsideTransaction = writeOutsideTransaction;
  }

  void setExpectSetter( final boolean expectSetter )
  {
    _expectSetter = expectSetter;
  }

  boolean expectSetter()
  {
    return _expectSetter;
  }

  @Nonnull
  List<CandidateMethod> getRefMethods()
  {
    return _refMethods;
  }

  void addRefMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    _refMethods.add( new CandidateMethod( method, methodType ) );
  }

  boolean hasGetter()
  {
    return null != _getter;
  }

  @Nonnull
  ExecutableElement getGetter()
    throws ProcessorException
  {
    assert null != _getter;
    return _getter;
  }

  @Nonnull
  ExecutableType getGetterType()
  {
    assert null != _getterType;
    return _getterType;
  }

  void setGetter( @Nonnull final ExecutableElement getter, @Nonnull final ExecutableType methodType )
  {
    _getter = Objects.requireNonNull( getter );
    _getterType = Objects.requireNonNull( methodType );
  }

  boolean hasSetter()
  {
    return null != _setter;
  }

  @Nonnull
  ExecutableElement getSetter()
    throws ProcessorException
  {
    assert null != _setter;
    return _setter;
  }

  @Nonnull
  ExecutableType getSetterType()
  {
    assert null != _setterType;
    return _setterType;
  }

  void setSetter( @Nonnull final ExecutableElement setter, @Nonnull final ExecutableType methodType )
  {
    assert _expectSetter;
    _setter = Objects.requireNonNull( setter );
    _setterType = Objects.requireNonNull( methodType );
  }

  @Nonnull
  ExecutableElement getDefiner()
  {
    if ( null != _getter )
    {
      return _getter;
    }
    else
    {
      return Objects.requireNonNull( _setter );
    }
  }

  void setDependencyDescriptor( @Nullable final DependencyDescriptor dependencyDescriptor )
  {
    _dependencyDescriptor = dependencyDescriptor;
  }

  void setReferenceDescriptor( @Nullable final ReferenceDescriptor referenceDescriptor )
  {
    assert null == _inverseDescriptor;
    _referenceDescriptor = referenceDescriptor;
  }

  void setInverseDescriptor( @Nullable final InverseDescriptor inverseDescriptor )
  {
    assert null == getReferenceDescriptor();
    _inverseDescriptor = inverseDescriptor;
    setExpectSetter( false );
  }

  @Nullable
  CascadeDisposeDescriptor getCascadeDisposeDescriptor()
  {
    return _cascadeDisposeDescriptor;
  }

  void setCascadeDisposeDescriptor( @Nonnull final CascadeDisposeDescriptor cascadeDisposeDescriptor )
  {
    _cascadeDisposeDescriptor = Objects.requireNonNull( cascadeDisposeDescriptor );
  }

  @Nonnull
  String getDataFieldName()
  {
    return ComponentGenerator.OBSERVABLE_DATA_FIELD_PREFIX + getName();
  }

  @Nonnull
  String getCollectionCacheDataFieldName()
  {
    return ComponentGenerator.OBSERVABLE_DATA_FIELD_PREFIX + "$$cache$$_" + getName();
  }

  @Nonnull
  String getFieldName()
  {
    return ComponentGenerator.FIELD_PREFIX + getName();
  }

  boolean isAbstract()
  {
    return getGetter().getModifiers().contains( Modifier.ABSTRACT );
  }

  boolean shouldGenerateUnmodifiableCollectionVariant()
  {
    return ( hasSetter() || null != _inverseDescriptor ) && isCollectionType();
  }

  private boolean isCollectionType()
  {
    return isGetterUnparameterizedReturnType( Collection.class ) ||
           isGetterUnparameterizedReturnType( Set.class ) ||
           isGetterUnparameterizedReturnType( List.class ) ||
           isGetterUnparameterizedReturnType( Map.class );
  }

  private boolean isGetterUnparameterizedReturnType( @Nonnull final Class<?> type )
  {
    final TypeMirror returnType = getGetterType().getReturnType();
    final TypeKind kind = returnType.getKind();
    if ( TypeKind.DECLARED != kind )
    {
      return false;
    }
    else
    {
      final DeclaredType declaredType = (DeclaredType) returnType;
      final TypeElement element = (TypeElement) declaredType.asElement();
      return element.getQualifiedName().toString().equals( type.getName() );
    }
  }

  boolean isGetterNonnull()
  {
    return AnnotationsUtil.hasNonnullAnnotation( getGetter() );
  }

  boolean isSetterNonnull()
  {
    return hasSetter() && AnnotationsUtil.hasNonnullAnnotation( getSetter().getParameters().get( 0 ) );
  }

  void validate()
  {
    if ( !expectSetter() )
    {
      if ( !doesSetterAlwaysMutate() )
      {
        throw new ProcessorException( "@Observable target defines expectSetter = false " +
                                      "setterAlwaysMutates = false but this is an invalid configuration.",
                                      getGetter() );
      }
      if ( _refMethods.isEmpty() && null == _inverseDescriptor )
      {
        throw new ProcessorException( "@Observable target defines expectSetter = false but there is no ref " +
                                      "method for observable and thus never possible to report it as changed " +
                                      "and thus should not be observable.", getGetter() );
      }
    }

    for ( final CandidateMethod refMethod : _refMethods )
    {
      final TypeName typeName = TypeName.get( refMethod.getMethodType().getReturnType() );
      if ( typeName instanceof final ParameterizedTypeName parameterizedTypeName )
      {
        final TypeName expectedType = parameterizedTypeName.typeArguments.get( 0 );
        if ( !( expectedType instanceof WildcardTypeName ) )
        {
          assert null != _getterType;
          final TypeName actual = TypeName.get( _getterType.getReturnType() );
          if ( !actual.box().toString().equals( expectedType.toString() ) )
          {
            throw new ProcessorException( "@ObservableValueRef target has a type parameter of " + expectedType +
                                          " but @Observable method returns type of " + actual, refMethod.getMethod() );
          }
        }
      }
    }
    if ( isAbstract() )
    {
      if ( !getGetter().getThrownTypes().isEmpty() )
      {
        throw new ProcessorException( "@Observable property is abstract but the getter declares an exception.",
                                      getSetter() );
      }
      if ( !hasSetter() )
      {
        if ( null == _inverseDescriptor )
        {
          throw new ProcessorException( "@Observable target defines expectSetter = false but is abstract. This " +
                                        "is not compatible as there is no opportunity for the processor to " +
                                        "generate the setter.", getGetter() );
        }
      }
      else
      {
        final ExecutableElement setter = getSetter();
        if ( !setter.getModifiers().contains( Modifier.ABSTRACT ) )
        {
          throw new ProcessorException( "@Observable property defines an abstract getter but a concrete setter. " +
                                        "Both getter and setter must be concrete or both must be abstract.",
                                        getGetter() );
        }

        if ( !setter.getThrownTypes().isEmpty() )
        {
          throw new ProcessorException( "@Observable property is abstract but the setter declares an exception.",
                                        getSetter() );
        }
      }
      if ( !doesSetterAlwaysMutate() )
      {
        throw new ProcessorException( "@Observable target defines setterAlwaysMutates = false but but has " +
                                      "defined abstract getters and setters.", getGetter() );
      }
    }
    else if ( hasSetter() && getSetter().getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ProcessorException( "@Observable property defines an abstract setter but a concrete getter. " +
                                    "Both getter and setter must be concrete or both must be abstract.",
                                    getSetter() );
    }
    if ( expectSetter() && !getSetterType().getTypeVariables().isEmpty() )
    {
      throw new ProcessorException( "@Observable target defines type variables. Method level type parameters " +
                                    "are not supported for observable values.", getSetter() );
    }
    if ( !getGetterType().getTypeVariables().isEmpty() )
    {
      throw new ProcessorException( "@Observable target defines type variables. Method level type parameters " +
                                    "are not supported for observable values.", getGetter() );
    }
  }

  @Nullable
  DependencyDescriptor getDependencyDescriptor()
  {
    return _dependencyDescriptor;
  }

  @Nullable
  ReferenceDescriptor getReferenceDescriptor()
  {
    return _referenceDescriptor;
  }

  boolean doesSetterAlwaysMutate()
  {
    return _setterAlwaysMutates;
  }
}
