package arez.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of Observable properties on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class ObservableDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  private boolean _expectSetter;
  @Nullable
  private ExecutableElement _getter;
  @Nullable
  private ExecutableType _getterType;
  @Nullable
  private ExecutableElement _setter;
  @Nullable
  private ExecutableType _setterType;
  @Nullable
  private ExecutableElement _refMethod;
  @Nullable
  private ExecutableType _refMethodType;

  ObservableDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                        @Nonnull final String name )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    setExpectSetter( true );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  void setExpectSetter( final boolean expectSetter )
  {
    _expectSetter = expectSetter;
  }

  boolean expectSetter()
  {
    return _expectSetter;
  }

  boolean hasRefMethod()
  {
    return null != _refMethod;
  }

  @Nonnull
  ExecutableElement getRefMethod()
    throws ArezProcessorException
  {
    assert null != _refMethod;
    return _refMethod;
  }

  void setRefMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    _refMethod = Objects.requireNonNull( method );
    _refMethodType = Objects.requireNonNull( methodType );
  }

  boolean hasGetter()
  {
    return null != _getter;
  }

  @Nonnull
  ExecutableElement getGetter()
    throws ArezProcessorException
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
    throws ArezProcessorException
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

  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    assert null != _getterType;
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( GeneratorUtil.OBSERVABLE_CLASSNAME,
                                 TypeName.get( _getterType.getReturnType() ).box() );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName,
                         getFieldName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    builder.addField( field.build() );
    if ( getGetter().getModifiers().contains( Modifier.ABSTRACT ) )
    {
      final TypeName type = TypeName.get( _getterType.getReturnType() );
      final FieldSpec.Builder dataField =
        FieldSpec.builder( type,
                           getDataFieldName(),
                           Modifier.PRIVATE );
      builder.addField( dataField.build() );
    }
  }

  @Nonnull
  private String getDataFieldName()
  {
    return GeneratorUtil.OBSERVABLE_DATA_FIELD_PREFIX + getName();
  }

  @Nonnull
  private String getFieldName()
  {
    return GeneratorUtil.FIELD_PREFIX + getName();
  }

  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N().createObservable( " +
               "$T.areNativeComponentsEnabled() ? this.$N : null, " +
               "$T.areNamesEnabled() ? $N() + $S : null, " +
               "$T.arePropertyIntrospectorsEnabled() ? () -> " );
    parameters.add( getFieldName() );
    parameters.add( _componentDescriptor.getContextMethodName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );

    final boolean abstractObservables = getGetter().getModifiers().contains( Modifier.ABSTRACT );
    if ( abstractObservables )
    {
      sb.append( "this.$N" );
      parameters.add( getDataFieldName() );
    }
    else
    {
      sb.append( "super.$N()" );
      parameters.add( getGetter().getSimpleName() );
    }
    sb.append( " : null" );

    if ( hasSetter() )
    {
      //setter
      sb.append( ", $T.arePropertyIntrospectorsEnabled() ? v -> " );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      if ( abstractObservables )
      {
        sb.append( "this.$N = v" );
        parameters.add( getDataFieldName() );
      }
      else
      {
        sb.append( "super.$N( v )" );
        parameters.add( getSetter().getSimpleName() );
      }
      sb.append( " : null" );
    }
    else
    {
      sb.append( ", null" );
    }

    sb.append( " )" );
    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", getFieldName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildObservableGetter() );
    if ( expectSetter() )
    {
      builder.addMethod( buildObservableSetter() );
    }
    if ( hasRefMethod() )
    {
      builder.addMethod( buildObservableRefMethod() );
    }
  }

  /**
   * Generate the accessor for ref method.
   */
  @Nonnull
  private MethodSpec buildObservableRefMethod()
    throws ArezProcessorException
  {
    assert null != _refMethod;
    assert null != _refMethodType;
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _refMethod.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _refMethod, builder );
    ProcessorUtil.copyTypeParameters( _refMethodType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _refMethod, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _refMethodType.getReturnType() ) );

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

    builder.addStatement( "return $N", getFieldName() );

    return builder.build();
  }

  /**
   * Generate the setter that reports that ensures that the access is reported as Observable.
   */
  @Nonnull
  private MethodSpec buildObservableSetter()
    throws ArezProcessorException
  {
    assert null != _setter;
    assert null != _setterType;
    assert null != _getter;
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _setter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _setter, builder );
    ProcessorUtil.copyExceptions( _setterType, builder );
    ProcessorUtil.copyTypeParameters( _setterType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _setter, builder );

    builder.addAnnotation( Override.class );

    // If the getter is deprecated but the setter is not
    // then we need to suppress deprecation warnings on setter
    // as we invoked getter from within it to verify value is
    // actually changed
    if ( null == _setter.getAnnotation( Deprecated.class ) &&
         null != _getter.getAnnotation( Deprecated.class ) )
    {
      builder.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                               .addMember( "value", "$S", "deprecation" )
                               .build() );
    }

    final TypeMirror parameterType = _setterType.getParameterTypes().get( 0 );
    final VariableElement element = _setter.getParameters().get( 0 );
    final String paramName = element.getSimpleName().toString();
    final TypeName type = TypeName.get( parameterType );
    final ParameterSpec.Builder param =
      ParameterSpec.builder( type, paramName, Modifier.FINAL );
    ProcessorUtil.copyDocumentedAnnotations( element, param );
    builder.addParameter( param.build() );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    final boolean abstractObservables = getGetter().getModifiers().contains( Modifier.ABSTRACT );
    if ( type.isPrimitive() )
    {
      if ( abstractObservables )
      {
        codeBlock.beginControlFlow( "if ( $N != this.$N )",
                                    paramName,
                                    getDataFieldName() );
      }
      else
      {
        codeBlock.beginControlFlow( "if ( $N != super.$N() )",
                                    paramName,
                                    _getter.getSimpleName() );
      }
    }
    else
    {
      if ( abstractObservables )
      {
        codeBlock.beginControlFlow( "if ( !$T.equals( $N, this.$N ) )",
                                    Objects.class,
                                    paramName,
                                    getDataFieldName() );
      }
      else
      {
        codeBlock.beginControlFlow( "if ( !$T.equals( $N, super.$N() ) )",
                                    Objects.class,
                                    paramName,
                                    _getter.getSimpleName() );
      }
    }
    codeBlock.addStatement( "this.$N.preReportChanged()", getFieldName() );
    if ( abstractObservables )
    {
      codeBlock.addStatement( "this.$N = $N", getDataFieldName(), paramName );
    }
    else
    {
      codeBlock.addStatement( "super.$N($N)", _setter.getSimpleName(), paramName );
    }
    codeBlock.addStatement( "this.$N.reportChanged()", getFieldName() );
    codeBlock.endControlFlow();
    builder.addCode( codeBlock.build() );

    return builder.build();
  }

  /**
   * Generate the getter that ensures that the access is reported.
   */
  @Nonnull
  private MethodSpec buildObservableGetter()
    throws ArezProcessorException
  {
    assert null != _getter;
    assert null != _getterType;
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _getter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _getter, builder );
    ProcessorUtil.copyExceptions( _getterType, builder );
    ProcessorUtil.copyTypeParameters( _getterType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _getter, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _getterType.getReturnType() ) );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

    builder.addStatement( "this.$N.reportObserved()", getFieldName() );

    if ( getGetter().getModifiers().contains( Modifier.ABSTRACT ) )
    {
      builder.addStatement( "return this.$N", getDataFieldName() );
    }
    else
    {
      builder.addStatement( "return super.$N()", _getter.getSimpleName() );
    }
    return builder.build();
  }

  void validate()
  {
    if ( !expectSetter() && !hasRefMethod() )
    {
      throw new ArezProcessorException( "@Observable target defines expectSetter = false but there is no ref " +
                                        "method for observable and thus never possible to report it as changed " +
                                        "and thus should not be observable.", getGetter() );
    }

    if ( null != _refMethodType )
    {
      final TypeName typeName = TypeName.get( _refMethodType.getReturnType() );
      if ( typeName instanceof ParameterizedTypeName )
      {
        final ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
        final TypeName expectedType = parameterizedTypeName.typeArguments.get( 0 );
        assert null != _getterType;
        final TypeName actual = TypeName.get( _getterType.getReturnType() );
        if ( !actual.box().toString().equals( expectedType.toString() ) )
        {
          assert null != _refMethod;
          throw new ArezProcessorException( "@ObservableRef target has a type parameter of " + expectedType +
                                            " but @Observable method returns type of " + actual, _refMethod );
        }
      }
    }
    if ( getGetter().getModifiers().contains( Modifier.ABSTRACT ) )
    {
      if ( !hasSetter() )
      {
        throw new ArezProcessorException( "@Observable target defines expectSetter = false but is abstract. This " +
                                          "is not compatible as there is no opportunity for the processor to " +
                                          "generate the setter.", getGetter() );
      }
      else
      {
        final ExecutableElement setter = getSetter();
        if ( !setter.getModifiers().contains( Modifier.ABSTRACT ) )
        {
          throw new ArezProcessorException( "@Observable property defines an abstract getter but a concrete setter. " +
                                            "Both getter and setter must be concrete or both must be abstract.",
                                            getGetter() );
        }
      }
    }
    else if ( hasSetter() && getSetter().getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@Observable property defines an abstract setter but a concrete getter. " +
                                        "Both getter and setter must be concrete or both must be abstract.",
                                        getSetter() );
    }
    if ( expectSetter() && !getSetterType().getTypeVariables().isEmpty() )
    {
      throw new ArezProcessorException( "@Observable target defines type variables. Method level type parameters " +
                                        "are not supported for observable values.", getSetter() );
    }
    if ( !getGetterType().getTypeVariables().isEmpty() )
    {
      throw new ArezProcessorException( "@Observable target defines type variables. Method level type parameters " +
                                        "are not supported for observable values.", getGetter() );
    }
  }
}
