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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
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
  private boolean _readOutsideTransaction;
  private Boolean _initializer;
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
  @Nullable
  private DependencyDescriptor _dependencyDescriptor;
  @Nullable
  private ReferenceDescriptor _referenceDescriptor;
  @Nullable
  private InverseDescriptor _inverseDescriptor;

  ObservableDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                        @Nonnull final String name )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    setExpectSetter( true );
    setReadOutsideTransaction( false );
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

  void setReadOutsideTransaction( final boolean readOutsideTransaction )
  {
    _readOutsideTransaction = readOutsideTransaction;
  }

  boolean canReadOutsideTransaction()
  {
    return _readOutsideTransaction;
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
    assert null == _referenceDescriptor;
    _inverseDescriptor = inverseDescriptor;
    setExpectSetter( false );
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
    if ( shouldGenerateUnmodifiableCollectionVariant() )
    {
      final TypeName type = TypeName.get( _getterType.getReturnType() );
      final FieldSpec.Builder dataField =
        FieldSpec.builder( type,
                           getCollectionCacheDataFieldName(),
                           Modifier.PRIVATE );
      builder.addField( dataField.build() );
    }
  }

  @Nonnull
  String getDataFieldName()
  {
    return GeneratorUtil.OBSERVABLE_DATA_FIELD_PREFIX + getName();
  }

  @Nonnull
  String getCollectionCacheDataFieldName()
  {
    return GeneratorUtil.OBSERVABLE_DATA_FIELD_PREFIX + "$$cache$$_" + getName();
  }

  @Nonnull
  String getFieldName()
  {
    return GeneratorUtil.FIELD_PREFIX + getName();
  }

  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N().observable( " +
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
    final String methodName = _refMethod.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _refMethod, builder );
    ProcessorUtil.copyTypeParameters( _refMethodType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _refMethod, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _refMethodType.getReturnType() ) );

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );

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
    final String methodName = _setter.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _setter, builder );
    ProcessorUtil.copyExceptions( _setterType, builder );
    ProcessorUtil.copyTypeParameters( _setterType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _setter, builder );

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
    ProcessorUtil.copyWhitelistedAnnotations( element, param );
    builder.addParameter( param.build() );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );
    builder.addStatement( "this.$N.preReportChanged()", getFieldName() );

    final String varName = GeneratorUtil.VARIABLE_PREFIX + "currentValue";

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    final boolean abstractObservables = getGetter().getModifiers().contains( Modifier.ABSTRACT );
    if ( abstractObservables )
    {
      builder.addStatement( "final $T $N = this.$N", type, varName, getDataFieldName() );
    }
    else
    {
      builder.addStatement( "final $T $N = super.$N()", type, varName, _getter.getSimpleName() );
    }
    if ( type.isPrimitive() )
    {
      codeBlock.beginControlFlow( "if ( $N != $N )", paramName, varName );
    }
    else
    {
      // We have a nonnull setter so lets enforce it
      if ( isSetterNonnull() )
      {
        builder.addStatement( "assert null != $N", paramName );
      }
      codeBlock.beginControlFlow( "if ( !$T.equals( $N, $N ) )", Objects.class, paramName, varName );
    }
    if ( shouldGenerateUnmodifiableCollectionVariant() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );
      block.addStatement( "this.$N = null", getCollectionCacheDataFieldName() );
      block.endControlFlow();

      builder.addCode( block.build() );
    }
    if ( abstractObservables )
    {
      if ( null != _dependencyDescriptor )
      {
        if ( isGetterNonnull() )
        {
          codeBlock.addStatement( "$T.asDisposeTrackable( $N ).getNotifier().removeOnDisposeListener( this )",
                                  GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                                  varName );
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          listenerBlock.addStatement( "$T.asDisposeTrackable( $N ).getNotifier().removeOnDisposeListener( this )",
                                      GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                                      varName );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
      codeBlock.addStatement( "this.$N = $N", getDataFieldName(), paramName );
      if ( null != _dependencyDescriptor )
      {
        if ( _dependencyDescriptor.shouldCascadeDispose() )
        {
          if ( isGetterNonnull() )
          {
            codeBlock
              .addStatement( "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, this::dispose )",
                             GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                             paramName );
          }
          else
          {
            final CodeBlock.Builder listenerBlock = CodeBlock.builder();
            listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
            listenerBlock
              .addStatement( "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, this::dispose )",
                             GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                             paramName );
            listenerBlock.endControlFlow();
            codeBlock.add( listenerBlock.build() );
          }
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
          listenerBlock
            .addStatement( "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, () -> $N( null ) )",
                           GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                           paramName,
                           _setter.getSimpleName().toString() );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
    }
    else
    {
      if ( null != _dependencyDescriptor )
      {
        if ( isGetterNonnull() )
        {
          codeBlock.addStatement( "$T.asDisposeTrackable( $N ).getNotifier().removeOnDisposeListener( this )",
                                  GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                                  varName );
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          listenerBlock.addStatement( "$T.asDisposeTrackable( $N ).getNotifier().removeOnDisposeListener( this )",
                                      GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                                      varName );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
      codeBlock.addStatement( "super.$N( $N )", _setter.getSimpleName(), paramName );
      if ( null != _dependencyDescriptor )
      {
        if ( _dependencyDescriptor.shouldCascadeDispose() )
        {
          if ( isGetterNonnull() )
          {
            codeBlock
              .addStatement( "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, this::dispose )",
                             GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                             paramName );
          }
          else
          {
            final CodeBlock.Builder listenerBlock = CodeBlock.builder();
            listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
            listenerBlock
              .addStatement( "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, this::dispose )",
                             GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                             paramName );
            listenerBlock.endControlFlow();
            codeBlock.add( listenerBlock.build() );
          }
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
          listenerBlock
            .addStatement( "$T.asDisposeTrackable( $N ).getNotifier().addOnDisposeListener( this, () -> $N( null ) )",
                           GeneratorUtil.DISPOSE_TRACKABLE_CLASSNAME,
                           paramName,
                           _setter.getSimpleName().toString() );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
    }
    codeBlock.addStatement( "this.$N.reportChanged()", getFieldName() );
    if ( null != _referenceDescriptor )
    {
      if ( _referenceDescriptor.hasInverse() )
      {
        codeBlock.addStatement( "this.$N()", _referenceDescriptor.getDelinkMethodName() );
      }
      if ( "EAGER".equals( _referenceDescriptor.getLinkType() ) )
      {
        codeBlock.addStatement( "this.$N()", _referenceDescriptor.getLinkMethodName() );
      }
      else
      {
        codeBlock.addStatement( "this.$N = null", _referenceDescriptor.getFieldName() );
      }
    }

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
    final String methodName = _getter.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _getter, builder );
    ProcessorUtil.copyExceptions( _getterType, builder );
    ProcessorUtil.copyTypeParameters( _getterType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _getter, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _getterType.getReturnType() ) );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );

    if ( _readOutsideTransaction )
    {
      builder.addStatement( "this.$N.reportObservedIfTrackingTransactionActive()", getFieldName() );
    }
    else
    {
      builder.addStatement( "this.$N.reportObserved()", getFieldName() );
    }

    if ( getGetter().getModifiers().contains( Modifier.ABSTRACT ) )
    {
      if ( shouldGenerateUnmodifiableCollectionVariant() )
      {
        if ( isGetterNonnull() )
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );

          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N )", getCollectionCacheDataFieldName() );
          guard.addStatement( "this.$N = $T.wrap( this.$N )",
                              getCollectionCacheDataFieldName(),
                              GeneratorUtil.COLLECTIONS_UTIL_CLASSNAME,
                              getDataFieldName() );
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );

          block.addStatement( "return this.$N", getDataFieldName() );
          block.endControlFlow();

          builder.addCode( block.build() );
        }
        else
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );

          final String result = "$$ar$$_result";
          block.addStatement( "final $T $N = this.$N",
                              TypeName.get( getGetterType().getReturnType() ),
                              result,
                              getDataFieldName() );
          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N && null != $N )",
                                  getCollectionCacheDataFieldName(),
                                  result );
          guard.addStatement( "this.$N = $T.wrap( $N )",
                              getCollectionCacheDataFieldName(),
                              GeneratorUtil.COLLECTIONS_UTIL_CLASSNAME,
                              result );
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );

          block.addStatement( "return this.$N", getDataFieldName() );
          block.endControlFlow();

          builder.addCode( block.build() );
        }
      }
      else
      {
        builder.addStatement( "return this.$N", getDataFieldName() );
      }
    }
    else
    {
      if ( shouldGenerateUnmodifiableCollectionVariant() )
      {
        if ( isGetterNonnull() )
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );

          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N )", getCollectionCacheDataFieldName() );
          guard.addStatement( "this.$N = $T.wrap( super.$N() )",
                              getCollectionCacheDataFieldName(),
                              GeneratorUtil.COLLECTIONS_UTIL_CLASSNAME,
                              _getter.getSimpleName() );
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );

          block.addStatement( "return super.$N()", _getter.getSimpleName() );
          block.endControlFlow();

          builder.addCode( block.build() );
        }
        else
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );

          final String result = "$$ar$$_result";
          block.addStatement( "final $T $N = super.$N()",
                              TypeName.get( getGetterType().getReturnType() ),
                              result,
                              _getter.getSimpleName() );
          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N && null != $N )",
                                  getCollectionCacheDataFieldName(),
                                  result );
          guard.addStatement( "this.$N = $T.wrap( $N )",
                              getCollectionCacheDataFieldName(),
                              GeneratorUtil.COLLECTIONS_UTIL_CLASSNAME,
                              result );
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );

          block.addStatement( "return super.$N()", _getter.getSimpleName() );
          block.endControlFlow();

          builder.addCode( block.build() );
        }
      }
      else
      {
        builder.addStatement( "return super.$N()", _getter.getSimpleName() );
      }
    }
    return builder.build();
  }

  private boolean shouldGenerateUnmodifiableCollectionVariant()
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

  private boolean isGetterNonnull()
  {
    return null != ProcessorUtil.findAnnotationByType( getGetter(), Constants.NONNULL_ANNOTATION_CLASSNAME );
  }

  private boolean isSetterNonnull()
  {
    return hasSetter() &&
           null != ProcessorUtil.findAnnotationByType( getSetter().getParameters().get( 0 ),
                                                       Constants.NONNULL_ANNOTATION_CLASSNAME );
  }

  void validate()
  {
    if ( !expectSetter() && !hasRefMethod() && null == _inverseDescriptor )
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
        if ( null == _inverseDescriptor )
        {
          throw new ArezProcessorException( "@Observable target defines expectSetter = false but is abstract. This " +
                                            "is not compatible as there is no opportunity for the processor to " +
                                            "generate the setter.", getGetter() );
        }
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
