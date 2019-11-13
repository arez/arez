package arez.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of ObservableValue properties on a @ArezComponent annotated class.
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
  private boolean _writeOutsideTransaction;
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
  @Nullable
  private CascadeDisposableDescriptor _cascadeDisposableDescriptor;

  ObservableDescriptor( @Nonnull final ComponentDescriptor componentDescriptor, @Nonnull final String name )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    setExpectSetter( true );
    setReadOutsideTransaction( false );
    setWriteOutsideTransaction( false );
    setSetterAlwaysMutates( true );
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

  void setReadOutsideTransaction( final boolean readOutsideTransaction )
  {
    _readOutsideTransaction = readOutsideTransaction;
  }

  boolean canReadOutsideTransaction()
  {
    return _readOutsideTransaction;
  }

  private boolean canWriteOutsideTransaction()
  {
    return _writeOutsideTransaction;
  }

  void setWriteOutsideTransaction( final boolean writeOutsideTransaction )
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

  boolean hasRefMethod()
  {
    return null != _refMethod;
  }

  @Nonnull
  ExecutableElement getRefMethod()
    throws ProcessorException
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
    assert null == _referenceDescriptor;
    _inverseDescriptor = inverseDescriptor;
    setExpectSetter( false );
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

  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    assert null != _getterType;
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( Generator.OBSERVABLE_CLASSNAME,
                                 TypeName.get( _getterType.getReturnType() ).box() );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName,
                         getFieldName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( Generator.NONNULL_CLASSNAME );
    builder.addField( field.build() );
    if ( isAbstract() )
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
    return Generator.OBSERVABLE_DATA_FIELD_PREFIX + getName();
  }

  @Nonnull
  String getCollectionCacheDataFieldName()
  {
    return Generator.OBSERVABLE_DATA_FIELD_PREFIX + "$$cache$$_" + getName();
  }

  @Nonnull
  String getFieldName()
  {
    return Generator.FIELD_PREFIX + getName();
  }

  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N.observable( " +
               "$T.areNativeComponentsEnabled() ? $N : null, " +
               "$T.areNamesEnabled() ? $N + $S : null, " +
               "$T.arePropertyIntrospectorsEnabled() ? () -> " );
    parameters.add( getFieldName() );
    parameters.add( Generator.CONTEXT_VAR_NAME );
    parameters.add( Generator.AREZ_CLASSNAME );
    parameters.add( Generator.COMPONENT_VAR_NAME );
    parameters.add( Generator.AREZ_CLASSNAME );
    parameters.add( Generator.NAME_VAR_NAME );
    parameters.add( "." + getName() );
    parameters.add( Generator.AREZ_CLASSNAME );

    final boolean abstractObservables = isAbstract();
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
      parameters.add( Generator.AREZ_CLASSNAME );
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

  boolean isAbstract()
  {
    return getGetter().getModifiers().contains( Modifier.ABSTRACT );
  }

  void buildDisposer( @Nonnull final MethodSpec.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", getFieldName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    builder.addMethod( buildObservableGetter() );
    if ( expectSetter() )
    {
      builder.addMethod( buildObservableSetter() );
      if ( canWriteOutsideTransaction() )
      {
        builder.addMethod( buildObservableInternalSetter() );
      }
    }
    if ( hasRefMethod() )
    {
      builder.addMethod( buildRefMethod() );
    }
  }

  /**
   * Generate the accessor for ref method.
   */
  @Nonnull
  private MethodSpec buildRefMethod()
    throws ProcessorException
  {
    assert null != _refMethod;
    assert null != _refMethodType;
    final String methodName = _refMethod.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    GeneratorUtil.copyAccessModifiers( _refMethod, builder );
    GeneratorUtil.copyTypeParameters( _refMethodType, builder );
    Generator.copyWhitelistedAnnotations( _refMethod, builder );

    final TypeName typeName = TypeName.get( _refMethod.getReturnType() );
    if ( !( typeName instanceof ParameterizedTypeName ) &&
         !ProcessorUtil.hasAnnotationOfType( _refMethod, SuppressWarnings.class.getName() ) )
    {
      builder.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class ).
        addMember( "value", "$S", "rawtypes" ).
        build() );
    }

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _refMethodType.getReturnType() ) );

    Generator.generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "return $N", getFieldName() );

    return builder.build();
  }

  @Nonnull
  private MethodSpec buildObservableSetter()
    throws ProcessorException
  {
    assert null != _setter;
    assert null != _setterType;
    assert null != _getter;
    final String methodName = _setter.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    GeneratorUtil.copyAccessModifiers( _setter, builder );
    GeneratorUtil.copyExceptions( _setterType, builder );
    GeneratorUtil.copyTypeParameters( _setterType, builder );
    Generator.copyWhitelistedAnnotations( _setter, builder );

    builder.addAnnotation( Override.class );

    if ( canWriteOutsideTransaction() )
    {
      final VariableElement element = _setter.getParameters().get( 0 );
      final String paramName = element.getSimpleName().toString();
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( _setterType.getParameterTypes().get( 0 ) ), paramName, Modifier.FINAL );
      Generator.copyWhitelistedAnnotations( element, param );
      builder.addParameter( param.build() );

      if ( _setterType.getThrownTypes().isEmpty() )
      {
        builder.addStatement( "this.$N.safeSetObservable( $T.areNamesEnabled() ? this.$N.getName() + $S : null, " +
                              "() -> this.$N( $N ) )",
                              Generator.KERNEL_FIELD_NAME,
                              Generator.AREZ_CLASSNAME,
                              Generator.KERNEL_FIELD_NAME,
                              "." + methodName,
                              Generator.FRAMEWORK_PREFIX + methodName,
                              paramName );
      }
      else
      {
        //noinspection CodeBlock2Expr
        Generator.generateTryBlock( builder, _setterType.getThrownTypes(), b -> {
          b.addStatement( "this.$N.setObservable( $T.areNamesEnabled() ? this.$N.getName() + $S : null, " +
                          "() -> this.$N( $N ) )",
                          Generator.KERNEL_FIELD_NAME,
                          Generator.AREZ_CLASSNAME,
                          Generator.KERNEL_FIELD_NAME,
                          "." + methodName,
                          Generator.FRAMEWORK_PREFIX + methodName,
                          paramName );
        } );
      }
    }
    else
    {
      addDeprecationIfRequired( builder );
      buildSetterImpl( builder );
    }

    return builder.build();
  }

  /**
   * Generate the internal setter.
   */
  @Nonnull
  private MethodSpec buildObservableInternalSetter()
    throws ProcessorException
  {
    assert null != _setter;
    assert null != _setterType;
    assert null != _getter;
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( Generator.FRAMEWORK_PREFIX + _setter.getSimpleName().toString() );
    builder.addModifiers( Modifier.PRIVATE );
    GeneratorUtil.copyExceptions( _setterType, builder );
    GeneratorUtil.copyTypeParameters( _setterType, builder );
    Generator.copyWhitelistedAnnotations( _setter, builder );
    addDeprecationIfRequired( builder );

    buildSetterImpl( builder );

    return builder.build();
  }

  private void buildSetterImpl( @Nonnull final MethodSpec.Builder builder )
  {
    assert null != _setter;
    assert null != _setterType;
    assert null != _getter;
    final String methodName = _setter.getSimpleName().toString();

    final TypeMirror parameterType = _setterType.getParameterTypes().get( 0 );
    final VariableElement element = _setter.getParameters().get( 0 );
    final String paramName = element.getSimpleName().toString();
    final TypeName type = TypeName.get( parameterType );
    final ParameterSpec.Builder param =
      ParameterSpec.builder( type, paramName, Modifier.FINAL );
    Generator.copyWhitelistedAnnotations( element, param );
    builder.addParameter( param.build() );
    Generator.generateNotDisposedInvariant( builder, methodName );
    builder.addStatement( "this.$N.preReportChanged()", getFieldName() );

    final String varName = Generator.VARIABLE_PREFIX + "currentValue";

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    final boolean abstractObservables = isAbstract();
    if ( abstractObservables )
    {
      builder.addStatement( "final $T $N = this.$N", type, varName, getDataFieldName() );
    }
    else
    {
      if ( _componentDescriptor.isClassType() )
      {
        builder.addStatement( "final $T $N = super.$N()", type, varName, _getter.getSimpleName() );
      }
      else
      {
        builder.addStatement( "final $T $N = $T.super.$N()",
                              type,
                              varName,
                              _componentDescriptor.getClassName(),
                              _getter.getSimpleName() );
      }
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
      block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );
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
          codeBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( this )",
                                  Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                  varName );
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          listenerBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( this )",
                                      Generator.DISPOSE_TRACKABLE_CLASSNAME,
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
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, this::dispose )",
                             Generator.DISPOSE_TRACKABLE_CLASSNAME,
                             paramName );
          }
          else
          {
            final CodeBlock.Builder listenerBlock = CodeBlock.builder();
            listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
            listenerBlock
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, this::dispose )",
                             Generator.DISPOSE_TRACKABLE_CLASSNAME,
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
            .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, () -> $N( null ) )",
                           Generator.DISPOSE_TRACKABLE_CLASSNAME,
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
          codeBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( this )",
                                  Generator.DISPOSE_TRACKABLE_CLASSNAME,
                                  varName );
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          listenerBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( this )",
                                      Generator.DISPOSE_TRACKABLE_CLASSNAME,
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
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, this::dispose )",
                             Generator.DISPOSE_TRACKABLE_CLASSNAME,
                             paramName );
          }
          else
          {
            final CodeBlock.Builder listenerBlock = CodeBlock.builder();
            listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
            listenerBlock
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, this::dispose )",
                             Generator.DISPOSE_TRACKABLE_CLASSNAME,
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
            .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( this, () -> $N( null ) )",
                           Generator.DISPOSE_TRACKABLE_CLASSNAME,
                           paramName,
                           _setter.getSimpleName().toString() );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
    }
    if ( _setterAlwaysMutates )
    {
      codeBlock.addStatement( "this.$N.reportChanged()", getFieldName() );
    }
    else
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      if ( type.isPrimitive() )
      {
        if ( _componentDescriptor.isClassType() )
        {
          block.beginControlFlow( "if ( $N != super.$N() )", varName, _getter.getSimpleName() );
        }
        else
        {
          block.beginControlFlow( "if ( $N != $T.super.$N() )",
                                  varName,
                                  _componentDescriptor.getClassName(),
                                  _getter.getSimpleName() );
        }
      }
      else
      {
        if ( _componentDescriptor.isClassType() )
        {
          block.beginControlFlow( "if ( !$T.equals( $N, super.$N() ) )",
                                  Objects.class,
                                  varName,
                                  _getter.getSimpleName() );
        }
        else
        {
          block.beginControlFlow( "if ( !$T.equals( $N, $T.super.$N() ) )",
                                  Objects.class,
                                  varName,
                                  _componentDescriptor.getClassName(),
                                  _getter.getSimpleName() );
        }
      }
      block.addStatement( "this.$N.reportChanged()", getFieldName() );
      block.endControlFlow();
      codeBlock.add( block.build() );
    }
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
  }

  private void addDeprecationIfRequired( final MethodSpec.Builder builder )
  {
    assert null != _setter;
    assert null != _getter;
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
  }

  /**
   * Generate the getter that ensures that the access is reported.
   */
  @Nonnull
  private MethodSpec buildObservableGetter()
    throws ProcessorException
  {
    assert null != _getter;
    assert null != _getterType;
    final String methodName = _getter.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    GeneratorUtil.copyAccessModifiers( _getter, builder );
    GeneratorUtil.copyExceptions( _getterType, builder );
    GeneratorUtil.copyTypeParameters( _getterType, builder );
    Generator.copyWhitelistedAnnotations( _getter, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _getterType.getReturnType() ) );
    Generator.generateNotDisposedInvariant( builder, methodName );

    if ( _readOutsideTransaction )
    {
      builder.addStatement( "this.$N.reportObservedIfTrackingTransactionActive()", getFieldName() );
    }
    else
    {
      builder.addStatement( "this.$N.reportObserved()", getFieldName() );
    }

    if ( isAbstract() )
    {
      if ( shouldGenerateUnmodifiableCollectionVariant() )
      {
        if ( isGetterNonnull() )
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );

          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N )", getCollectionCacheDataFieldName() );
          guard.addStatement( "this.$N = $T.wrap( this.$N )",
                              getCollectionCacheDataFieldName(),
                              Generator.COLLECTIONS_UTIL_CLASSNAME,
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
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );

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
                              Generator.COLLECTIONS_UTIL_CLASSNAME,
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
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );

          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N )", getCollectionCacheDataFieldName() );
          if ( _componentDescriptor.isClassType() )
          {
            guard.addStatement( "this.$N = $T.wrap( super.$N() )",
                                getCollectionCacheDataFieldName(),
                                Generator.COLLECTIONS_UTIL_CLASSNAME,
                                _getter.getSimpleName() );
          }
          else
          {
            guard.addStatement( "this.$N = $T.wrap( $T.super.$N() )",
                                getCollectionCacheDataFieldName(),
                                Generator.COLLECTIONS_UTIL_CLASSNAME,
                                _componentDescriptor.getClassName(),
                                _getter.getSimpleName() );
          }
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );
          if ( _componentDescriptor.isClassType() )
          {
            block.addStatement( "return super.$N()", _getter.getSimpleName() );
          }
          else
          {
            block.addStatement( "return $T.super.$N()", _componentDescriptor.getClassName(), _getter.getSimpleName() );
          }
          block.endControlFlow();

          builder.addCode( block.build() );
        }
        else
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );

          final String result = "$$ar$$_result";
          if ( _componentDescriptor.isClassType() )
          {
            block.addStatement( "final $T $N = super.$N()",
                                TypeName.get( getGetterType().getReturnType() ),
                                result,
                                _getter.getSimpleName() );
          }
          else
          {
            block.addStatement( "final $T $N = $T.super.$N()",
                                TypeName.get( getGetterType().getReturnType() ),
                                result,
                                _componentDescriptor.getClassName(),
                                _getter.getSimpleName() );
          }
          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N && null != $N )",
                                  getCollectionCacheDataFieldName(),
                                  result );
          guard.addStatement( "this.$N = $T.wrap( $N )",
                              getCollectionCacheDataFieldName(),
                              Generator.COLLECTIONS_UTIL_CLASSNAME,
                              result );
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );

          if ( _componentDescriptor.isClassType() )
          {
            block.addStatement( "return super.$N()", _getter.getSimpleName() );
          }
          else
          {
            block.addStatement( "return $T.super.$N()", _componentDescriptor.getClassName(), _getter.getSimpleName() );
          }
          block.endControlFlow();

          builder.addCode( block.build() );
        }
      }
      else
      {
        if ( _componentDescriptor.isClassType() )
        {
          builder.addStatement( "return super.$N()", _getter.getSimpleName() );
        }
        else
        {
          builder.addStatement( "return $T.super.$N()", _componentDescriptor.getClassName(), _getter.getSimpleName() );
        }
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

  boolean isGetterNonnull()
  {
    return ProcessorUtil.hasNonnullAnnotation( getGetter() );
  }

  private boolean isSetterNonnull()
  {
    return hasSetter() && ProcessorUtil.hasNonnullAnnotation( getSetter().getParameters().get( 0 ) );
  }

  void validate()
  {
    if ( !expectSetter() )
    {
      if ( !_setterAlwaysMutates )
      {
        throw new ProcessorException( "@Observable target defines expectSetter = false " +
                                      "setterAlwaysMutates = false but this is an invalid configuration.",
                                      getGetter() );
      }
      if ( !hasRefMethod() && null == _inverseDescriptor )
      {
        throw new ProcessorException( "@Observable target defines expectSetter = false but there is no ref " +
                                      "method for observable and thus never possible to report it as changed " +
                                      "and thus should not be observable.", getGetter() );
      }
    }

    if ( null != _refMethodType )
    {
      final TypeName typeName = TypeName.get( _refMethodType.getReturnType() );
      if ( typeName instanceof ParameterizedTypeName )
      {
        final ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
        final TypeName expectedType = parameterizedTypeName.typeArguments.get( 0 );
        if ( !( expectedType instanceof WildcardTypeName ) )
        {
          assert null != _getterType;
          final TypeName actual = TypeName.get( _getterType.getReturnType() );
          if ( !actual.box().toString().equals( expectedType.toString() ) )
          {
            assert null != _refMethod;
            throw new ProcessorException( "@ObservableValueRef target has a type parameter of " + expectedType +
                                          " but @Observable method returns type of " + actual, _refMethod );
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
      if ( !_setterAlwaysMutates )
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
}
