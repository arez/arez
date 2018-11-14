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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
 * The class that represents the parsed state of @Memoize methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class MemoizeDescriptor
{
  static final Pattern ON_ACTIVATE_PATTERN = Pattern.compile( "^on([A-Z].*)Activate$" );
  static final Pattern ON_DEACTIVATE_PATTERN = Pattern.compile( "^on([A-Z].*)Deactivate$" );
  static final Pattern ON_STALE_PATTERN = Pattern.compile( "^on([A-Z].*)Stale$" );
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _method;
  @Nullable
  private ExecutableType _methodType;
  private boolean _keepAlive;
  private boolean _requireEnvironment;
  private String _priority;
  private boolean _reportResult;
  private boolean _observeLowerPriorityDependencies;
  private String _depType;
  @Nullable
  private ExecutableElement _onActivate;
  @Nullable
  private ExecutableElement _onDeactivate;
  @Nullable
  private ExecutableElement _onStale;
  @Nullable
  private ExecutableElement _refMethod;
  @Nullable
  private ExecutableType _refMethodType;

  MemoizeDescriptor( @Nonnull final ComponentDescriptor componentDescriptor, @Nonnull final String name )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  private String getCollectionCacheDataActiveFieldName()
  {
    return GeneratorUtil.OBSERVABLE_DATA_FIELD_PREFIX + "$$cache_active$$_" + getName();
  }

  @Nonnull
  private String getCollectionCacheDataFieldName()
  {
    return GeneratorUtil.OBSERVABLE_DATA_FIELD_PREFIX + "$$cache$$_" + getName();
  }

  boolean hasMemoize()
  {
    return null != _method;
  }

  boolean isKeepAlive()
  {
    return _keepAlive;
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    return Objects.requireNonNull( _method );
  }

  @Nullable
  ExecutableElement getOnActivate()
  {
    return _onActivate;
  }

  @Nullable
  ExecutableElement getOnDeactivate()
  {
    return _onDeactivate;
  }

  @Nullable
  ExecutableElement getOnStale()
  {
    return _onStale;
  }

  void setMemoize( @Nonnull final ExecutableElement method,
                   @Nonnull final ExecutableType methodType,
                   final boolean keepAlive,
                   final boolean requireEnvironment,
                   @Nonnull final String priority,
                   final boolean reportResult,
                   final boolean observeLowerPriorityDependencies,
                   @Nonnull final String depType )
    throws ArezProcessorException
  {
    //The caller already verified that no duplicate computable have been defined
    assert null == _method;
    MethodChecks.mustBeWrappable( _componentDescriptor.getElement(),
                                  Constants.MEMOIZE_ANNOTATION_CLASSNAME,
                                  method );
    MethodChecks.mustReturnAValue( Constants.MEMOIZE_ANNOTATION_CLASSNAME, method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.MEMOIZE_ANNOTATION_CLASSNAME, method );

    _method = Objects.requireNonNull( method );
    _methodType = Objects.requireNonNull( methodType );
    _keepAlive = keepAlive;
    _requireEnvironment = requireEnvironment;
    _priority = Objects.requireNonNull( priority );
    _reportResult = reportResult;
    _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
    _depType = Objects.requireNonNull( depType );

    if ( isMethodReturnType( Stream.class ) )
    {
      throw new ArezProcessorException( "@Memoize target must not return a value of type java.util.stream.Stream " +
                                        "as the type is single use and thus does not make sense to cache as a " +
                                        "computable value", method );
    }
  }

  void setRefMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeSubclassCallable( _componentDescriptor.getElement(),
                                         Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME,
                                         method );
    MethodChecks.mustNotThrowAnyExceptions( Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME, method );

    if ( null != _refMethod )
    {
      throw new ArezProcessorException( "@ComputableValueRef target duplicates existing method named " +
                                        _refMethod.getSimpleName(), method );
    }
    else
    {
      _refMethod = Objects.requireNonNull( method );
      _refMethodType = Objects.requireNonNull( methodType );
    }
  }

  void setOnActivate( @Nonnull final ExecutableElement onActivate )
    throws ArezProcessorException
  {
    MethodChecks.mustBeLifecycleHook( _componentDescriptor.getElement(),
                                      Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME,
                                      onActivate );

    if ( null != _onActivate )
    {
      throw new ArezProcessorException( "@OnActivate target duplicates existing method named " +
                                        _onActivate.getSimpleName(), onActivate );
    }
    else
    {
      _onActivate = Objects.requireNonNull( onActivate );
    }
  }

  void setOnDeactivate( @Nonnull final ExecutableElement onDeactivate )
    throws ArezProcessorException
  {
    MethodChecks.mustBeLifecycleHook( _componentDescriptor.getElement(),
                                      Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME,
                                      onDeactivate );
    if ( null != _onDeactivate )
    {
      throw new ArezProcessorException( "@OnDeactivate target duplicates existing method named " +
                                        _onDeactivate.getSimpleName(),
                                        onDeactivate );
    }
    else
    {
      _onDeactivate = Objects.requireNonNull( onDeactivate );
    }
  }

  void setOnStale( @Nonnull final ExecutableElement onStale )
    throws ArezProcessorException
  {
    MethodChecks.mustBeLifecycleHook( _componentDescriptor.getElement(),
                                      Constants.ON_STALE_ANNOTATION_CLASSNAME,
                                      onStale );
    if ( null != _onStale )
    {
      throw new ArezProcessorException( "@OnStale target duplicates existing method named " +
                                        _onStale.getSimpleName(),
                                        onStale );
    }
    else
    {
      _onStale = Objects.requireNonNull( onStale );
    }
  }

  void validate()
    throws ArezProcessorException
  {
    if ( null == _method )
    {
      if ( null != _onActivate )
      {
        throw new ArezProcessorException( "@OnActivate exists but there is no corresponding @Memoize",
                                          _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ArezProcessorException( "@OnDeactivate exists but there is no corresponding @Memoize",
                                          _onDeactivate );
      }
      else if ( null != _refMethod )
      {
        throw new ArezProcessorException( "@ComputableValueRef exists but there is no corresponding @Memoize",
                                          _refMethod );
      }
      else
      {
        final ExecutableElement onStale = _onStale;
        assert null != onStale;
        throw new ArezProcessorException( "@OnStale exists but there is no corresponding @Memoize", onStale );
      }
    }
    if ( _keepAlive )
    {
      if ( !_method.getParameters().isEmpty() )
      {
        throw new ArezProcessorException( "@Memoize target specified parameter keepAlive as true but has parameters.",
                                          _method );
      }
      else if ( null != _onActivate )
      {
        throw new ArezProcessorException( "@OnActivate exists for @Memoize property that specified parameter " +
                                          "keepAlive as true.", _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ArezProcessorException( "@OnDeactivate exists for @Memoize property that specified parameter " +
                                          "keepAlive as true.", _onDeactivate );
      }
    }
    if ( !_method.getParameters().isEmpty() )
    {
      if ( null != _onActivate )
      {
        throw new ArezProcessorException( "@OnActivate target associated with @Memoize method that has parameters.",
                                          _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ArezProcessorException( "@OnDeactivate target associated with @Memoize method that has parameters.",
                                          _onDeactivate );
      }
      else if ( null != _onStale )
      {
        throw new ArezProcessorException( "@OnStale target associated with @Memoize method that has parameters.",
                                          _onStale );
      }
    }

    if ( null != _refMethod )
    {
      final TypeName typeName = TypeName.get( _refMethod.getReturnType() );
      if ( typeName instanceof ParameterizedTypeName )
      {
        final ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
        final TypeName expectedType = parameterizedTypeName.typeArguments.get( 0 );
        final TypeName actual = TypeName.get( _method.getReturnType() );
        if ( !actual.box().toString().equals( expectedType.toString() ) )
        {
          throw new ArezProcessorException( "@ComputableValueRef target has a type parameter of " + expectedType +
                                            " but @Memoize method returns type of " + actual, _refMethod );
        }
      }

      assert null != _methodType;
      assert null != _refMethodType;
      final List<? extends TypeMirror> parameterTypes = _methodType.getParameterTypes();
      final List<? extends TypeMirror> refParameterTypes = _refMethodType.getParameterTypes();

      final boolean sizeMatch = parameterTypes.size() == refParameterTypes.size();
      boolean typesMatch = true;
      if ( sizeMatch )
      {
        for ( int i = 0; i < parameterTypes.size(); i++ )
        {
          final TypeMirror typeMirror = parameterTypes.get( i );
          final TypeMirror typeMirror2 = refParameterTypes.get( i );
          if ( !_componentDescriptor.getTypeUtils().isSameType( typeMirror, typeMirror2 ) )
          {
            typesMatch = false;
            break;
          }
        }
      }
      if ( !sizeMatch || !typesMatch )
      {
        throw new ArezProcessorException( "@ComputableValueRef target and the associated @Memoize " +
                                          "target do not have the same parameters.", _method );
      }
    }
    else if ( _depType.equals( "AREZ_OR_EXTERNAL" ) )
    {
      throw new ArezProcessorException( "@Memoize target specified depType = AREZ_OR_EXTERNAL but " +
                                        "there is no associated @ComputableValueRef method.", _method );
    }
  }

  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    assert null != _method;
    if ( _method.getParameters().isEmpty() )
    {
      buildFieldsForNoParamVariant( builder );
    }
    else
    {
      buildFieldsForParamVariant( builder );
    }
  }

  private void buildFieldsForNoParamVariant( @Nonnull final TypeSpec.Builder builder )
  {
    assert null != _method;
    assert null != _methodType;
    final TypeName parameterType =
      _method.getTypeParameters().isEmpty() ? TypeName.get( _methodType.getReturnType() ).box() :
      WildcardTypeName.subtypeOf( TypeName.OBJECT );
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( GeneratorUtil.COMPUTABLE_VALUE_CLASSNAME, parameterType );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName, getFieldName(), Modifier.FINAL, Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    builder.addField( field.build() );
    if ( isCollectionType() )
    {
      builder.addField( FieldSpec.builder( TypeName.get( _methodType.getReturnType() ),
                                           getCollectionCacheDataFieldName(),
                                           Modifier.PRIVATE ).build() );
      builder.addField( FieldSpec.builder( TypeName.BOOLEAN,
                                           getCollectionCacheDataActiveFieldName(),
                                           Modifier.PRIVATE ).build() );
    }
  }

  private void buildFieldsForParamVariant( @Nonnull final TypeSpec.Builder builder )
  {
    assert null != _method;
    assert null != _methodType;
    final TypeName parameterType =
      _method.getTypeParameters().isEmpty() ? TypeName.get( _methodType.getReturnType() ).box() :
      WildcardTypeName.subtypeOf( TypeName.OBJECT );
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( GeneratorUtil.MEMOIZE_CACHE_CLASSNAME, parameterType );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName,
                         getFieldName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    builder.addField( field.build() );
  }

  @Nonnull
  private String getFieldName()
  {
    return GeneratorUtil.FIELD_PREFIX + getName();
  }

  void buildInitializer( @Nonnull MethodSpec.Builder builder )
  {
    assert null != _method;
    if ( _method.getParameters().isEmpty() )
    {
      buildInitializerForNoParamVariant( builder );
    }
    else
    {
      buildInitializerForParamVariant( builder );
    }
  }

  private void buildInitializerForNoParamVariant( @Nonnull final MethodSpec.Builder builder )
  {
    assert null != _method;
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();

    if ( isCollectionType() && !hasHooks() )
    {
      sb.append( "this.$N = $T.areCollectionsPropertiesUnmodifiable() ? " +
                 "$N.computable( " +
                 "$T.areNativeComponentsEnabled() ? $N : null, " +
                 "$T.areNamesEnabled() ? $N + $S : null, " +
                 "() -> super.$N(), " );
      parameters.add( getFieldName() );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      parameters.add( GeneratorUtil.CONTEXT_VAR_NAME );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      parameters.add( GeneratorUtil.COMPONENT_VAR_NAME );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      parameters.add( GeneratorUtil.NAME_VAR_NAME );
      parameters.add( "." + getName() );
      parameters.add( _method.getSimpleName().toString() );
      appendInitializerSuffix( parameters, sb, true );

      // Else part of ternary
      sb.append( " : $N.computable( " +
                 "$T.areNativeComponentsEnabled() ? $N : null, " +
                 "$T.areNamesEnabled() ? $N + $S : null, " +
                 "() -> super.$N(), " );
      parameters.add( GeneratorUtil.CONTEXT_VAR_NAME );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      parameters.add( GeneratorUtil.COMPONENT_VAR_NAME );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      parameters.add( GeneratorUtil.NAME_VAR_NAME );
      parameters.add( "." + getName() );
      parameters.add( _method.getSimpleName().toString() );
      appendInitializerSuffix( parameters, sb, false );
    }
    else // hasHooks()
    {
      sb.append( "this.$N = $N.computable( " +
                 "$T.areNativeComponentsEnabled() ? $N : null, " +
                 "$T.areNamesEnabled() ? $N + $S : null, " +
                 "() -> super.$N(), " );
      parameters.add( getFieldName() );
      parameters.add( GeneratorUtil.CONTEXT_VAR_NAME );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      parameters.add( GeneratorUtil.COMPONENT_VAR_NAME );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      parameters.add( GeneratorUtil.NAME_VAR_NAME );
      parameters.add( "." + getName() );
      parameters.add( _method.getSimpleName().toString() );
      appendInitializerSuffix( parameters, sb, true );
    }
    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private void buildInitializerForParamVariant( @Nonnull final MethodSpec.Builder builder )
  {
    assert null != _method;
    assert null != _methodType;
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = new $T<>( $T.areZonesEnabled() ? $N : null, " +
               "$T.areNativeComponentsEnabled() ? $N : null, " +
               "$T.areNamesEnabled() ? $N + $S : null, args -> super.$N(" );
    parameters.add( getFieldName() );
    parameters.add( GeneratorUtil.MEMOIZE_CACHE_CLASSNAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.CONTEXT_VAR_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_VAR_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.NAME_VAR_NAME );
    parameters.add( "." + getName() );
    parameters.add( _method.getSimpleName().toString() );

    int index = 0;
    for ( final TypeMirror arg : _methodType.getParameterTypes() )
    {
      if ( 0 != index )
      {
        sb.append( ", " );
      }
      sb.append( "($T) args[ " ).append( index ).append( " ]" );
      parameters.add( arg );
      index++;
    }

    sb.append( "), " );
    sb.append( _method.getParameters().size() );
    sb.append( ", " );

    final ArrayList<String> flags = generateFlags();

    sb.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( GeneratorUtil.FLAGS_CLASSNAME );
    }

    sb.append( " )" );
    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private void appendInitializerSuffix( @Nonnull final ArrayList<Object> parameters,
                                        @Nonnull final StringBuilder sb,
                                        final boolean areCollectionsPropertiesUnmodifiable )
  {
    final boolean isCollectionType = isCollectionType();
    if ( hasHooks() || ( isCollectionType && areCollectionsPropertiesUnmodifiable ) )
    {
      if ( isCollectionType )
      {
        sb.append( "this::$N" );
        parameters.add( getOnActivateHookMethodName() );
      }
      else if ( null != _onActivate )
      {
        sb.append( "this::$N" );
        parameters.add( _onActivate.getSimpleName().toString() );
      }
      else
      {
        sb.append( "null" );
      }
      sb.append( ", " );

      if ( isCollectionType )
      {
        sb.append( "this::$N" );
        parameters.add( getOnDeactivateHookMethodName() );
      }
      else if ( null != _onDeactivate )
      {
        sb.append( "this::$N" );
        parameters.add( _onDeactivate.getSimpleName().toString() );
      }
      else
      {
        sb.append( "null" );
      }
      sb.append( ", " );

      if ( isCollectionType )
      {
        sb.append( "this::$N" );
        parameters.add( getOnStaleHookMethodName() );
      }
      else if ( null != _onStale )
      {
        sb.append( "this::$N" );
        parameters.add( _onStale.getSimpleName().toString() );
      }
      else
      {
        sb.append( "null" );
      }

      sb.append( ", " );
    }

    final ArrayList<String> flags = generateFlags();
    flags.add( "RUN_LATER" );
    if ( _keepAlive )
    {
      flags.add( "KEEPALIVE" );
    }

    sb.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( GeneratorUtil.FLAGS_CLASSNAME );
    }

    sb.append( " )" );
  }

  @Nonnull
  private ArrayList<String> generateFlags()
  {
    final ArrayList<String> flags = new ArrayList<>();
    flags.add( "PRIORITY_" + _priority );

    if ( !_reportResult )
    {
      flags.add( "NO_REPORT_RESULT" );
    }
    if ( _observeLowerPriorityDependencies )
    {
      flags.add( "OBSERVE_LOWER_PRIORITY_DEPENDENCIES" );
    }
    if ( _requireEnvironment )
    {
      flags.add( "ENVIRONMENT_REQUIRED" );
    }
    else
    {
      flags.add( "ENVIRONMENT_NOT_REQUIRED" );
    }
    switch ( _depType )
    {
      case "AREZ":
        flags.add( "AREZ_DEPENDENCIES" );
        break;
      case "AREZ_OR_NONE":
        flags.add( "AREZ_OR_NO_DEPENDENCIES" );
        break;
      default:
        flags.add( "AREZ_OR_EXTERNAL_DEPENDENCIES" );
        break;
    }
    return flags;
  }

  @Nonnull
  private String getOnActivateHookMethodName()
  {
    return GeneratorUtil.FRAMEWORK_PREFIX + "onActivate_" + getName();
  }

  @Nonnull
  private String getOnDeactivateHookMethodName()
  {
    return GeneratorUtil.FRAMEWORK_PREFIX + "onDeactivate_" + getName();
  }

  @Nonnull
  private String getOnStaleHookMethodName()
  {
    return GeneratorUtil.FRAMEWORK_PREFIX + "onStale_" + getName();
  }

  private boolean hasHooks()
  {
    return null != _onActivate || null != _onDeactivate || null != _onStale;
  }

  void buildDisposer( @Nonnull final MethodSpec.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", getFieldName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    assert null != _method;
    if ( _method.getParameters().isEmpty() )
    {
      builder.addMethod( buildNoParamsMemoize() );
      if ( isCollectionType() )
      {
        builder.addMethod( buildOnActivateWrapperHook() );
        builder.addMethod( buildOnDeactivateWrapperHook() );
        builder.addMethod( buildOnStaleWrapperHook() );
      }
      if ( null != _refMethod )
      {
        builder.addMethod( buildNoParamsRefMethod() );
      }
    }
    else
    {
      builder.addMethod( buildParamsMemoize() );
      if ( null != _refMethod )
      {
        builder.addMethod( buildParamsRefMethod() );
      }
    }
  }

  @Nonnull
  private MethodSpec buildOnActivateWrapperHook()
    throws ArezProcessorException
  {
    assert isCollectionType();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getOnActivateHookMethodName() );
    builder.addModifiers( Modifier.PRIVATE );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );
    block.addStatement( "this.$N = true", getCollectionCacheDataActiveFieldName() );
    block.addStatement( "this.$N = null", getCollectionCacheDataFieldName() );
    block.endControlFlow();
    builder.addCode( block.build() );

    if ( null != _onActivate )
    {
      builder.addStatement( "$N()", _onActivate.getSimpleName().toString() );
    }
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildOnDeactivateWrapperHook()
    throws ArezProcessorException
  {
    assert isCollectionType();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getOnDeactivateHookMethodName() );
    builder.addModifiers( Modifier.PRIVATE );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );
    block.addStatement( "this.$N = false", getCollectionCacheDataActiveFieldName() );
    block.addStatement( "this.$N = null", getCollectionCacheDataFieldName() );
    block.endControlFlow();
    builder.addCode( block.build() );

    if ( null != _onDeactivate )
    {
      builder.addStatement( "$N()", _onDeactivate.getSimpleName().toString() );
    }
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildOnStaleWrapperHook()
    throws ArezProcessorException
  {
    assert isCollectionType();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getOnStaleHookMethodName() );
    builder.addModifiers( Modifier.PRIVATE );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() && this.$N )",
                            GeneratorUtil.AREZ_CLASSNAME,
                            getCollectionCacheDataActiveFieldName() );
    block.addStatement( "this.$N = null", getCollectionCacheDataFieldName() );
    block.endControlFlow();
    builder.addCode( block.build() );

    if ( null != _onStale )
    {
      builder.addStatement( "$N()", _onStale.getSimpleName().toString() );
    }
    return builder.build();
  }

  /**
   * Generate the wrapper around Memoize method.
   */
  @Nonnull
  private MethodSpec buildNoParamsMemoize()
    throws ArezProcessorException
  {
    assert null != _method;
    assert null != _methodType;
    final String methodName = _method.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _method, builder );
    ProcessorUtil.copyExceptions( _methodType, builder );
    ProcessorUtil.copyTypeParameters( _methodType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _method, builder );
    builder.addAnnotation( Override.class );
    final TypeName returnType = TypeName.get( _methodType.getReturnType() );
    builder.returns( returnType );
    GeneratorUtil.generateNotDisposedInvariant( builder, methodName );

    if ( isCollectionType() )
    {
      if ( isMemoizeNonnull() )
      {
        final CodeBlock.Builder block = CodeBlock.builder();
        block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );

        final CodeBlock.Builder guard = CodeBlock.builder();
        guard.beginControlFlow( "if ( null == this.$N )", getCollectionCacheDataFieldName() );
        guard.addStatement( "this.$N = $T.wrap( ($T) this.$N.get() )",
                            getCollectionCacheDataFieldName(),
                            GeneratorUtil.COLLECTIONS_UTIL_CLASSNAME,
                            returnType.box(),
                            getFieldName() );
        guard.nextControlFlow( "else" );
        guard.add( "// Make sure that we are observing computable value\n" );
        guard.addStatement( "this.$N.get()", getFieldName() );
        guard.endControlFlow();
        block.add( guard.build() );
        block.addStatement( "return $N", getCollectionCacheDataFieldName() );

        block.nextControlFlow( "else" );

        block.addStatement( "return ($T) this.$N.get()", returnType.box(), getFieldName() );
        block.endControlFlow();

        builder.addCode( block.build() );
      }
      else
      {
        final CodeBlock.Builder block = CodeBlock.builder();
        block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", GeneratorUtil.AREZ_CLASSNAME );

        final String result = "$$ar$$_result";
        block.addStatement( "final $T $N = ($T) this.$N.get()", returnType, result, returnType.box(), getFieldName() );
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

        block.addStatement( "return ($T) this.$N.get()", returnType.box(), getFieldName() );
        block.endControlFlow();

        builder.addCode( block.build() );
      }
    }
    else if ( _method.getTypeParameters().isEmpty() )
    {
      builder.addStatement( "return this.$N.get()", getFieldName() );
    }
    else
    {
      builder.addStatement( "return ($T) this.$N.get()", returnType.box(), getFieldName() );
    }
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildParamsRefMethod()
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

    {
      final List<? extends VariableElement> parameters = _refMethod.getParameters();
      final int paramCount = parameters.size();
      for ( int i = 0; i < paramCount; i++ )
      {
        final VariableElement element = parameters.get( i );
        final TypeName parameterType = TypeName.get( _refMethodType.getParameterTypes().get( i ) );
        final ParameterSpec.Builder param =
          ParameterSpec.builder( parameterType, element.getSimpleName().toString(), Modifier.FINAL );
        ProcessorUtil.copyWhitelistedAnnotations( element, param );
        builder.addParameter( param.build() );
      }
    }

    GeneratorUtil.generateNotDisposedInvariant( builder, methodName );

    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> parameters = new ArrayList<>();
    sb.append( "return this.$N.getComputableValue( " );
    parameters.add( getFieldName() );

    boolean first = true;
    for ( final VariableElement element : _refMethod.getParameters() )
    {
      if ( !first )
      {
        sb.append( ", " );
      }
      first = false;
      sb.append( "$N" );
      parameters.add( element.getSimpleName().toString() );
    }
    sb.append( " )" );

    builder.addStatement( sb.toString(), parameters.toArray() );
    return builder.build();
  }

  /**
   * Generate the accessor for ref method.
   */
  @Nonnull
  private MethodSpec buildNoParamsRefMethod()
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

    GeneratorUtil.generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "return $N", getFieldName() );

    return builder.build();
  }

  private boolean isMemoizeNonnull()
  {
    return null != ProcessorUtil.findAnnotationByType( getMethod(), Constants.NONNULL_ANNOTATION_CLASSNAME );
  }

  private boolean isCollectionType()
  {
    return isMethodReturnType( Collection.class ) ||
           isMethodReturnType( Set.class ) ||
           isMethodReturnType( List.class ) ||
           isMethodReturnType( Map.class );
  }

  private boolean isMethodReturnType( @Nonnull final Class<?> type )
  {
    final TypeMirror returnType = getMethod().getReturnType();
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

  @Nonnull
  private MethodSpec buildParamsMemoize()
    throws ArezProcessorException
  {
    assert null != _method;
    assert null != _methodType;
    final String methodName = _method.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _method, builder );
    ProcessorUtil.copyExceptions( _methodType, builder );
    ProcessorUtil.copyTypeParameters( _methodType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _method, builder );
    builder.addAnnotation( Override.class );
    final TypeName returnType = TypeName.get( _methodType.getReturnType() );
    builder.returns( returnType );

    final boolean hasTypeParameters = !_method.getTypeParameters().isEmpty();
    if ( hasTypeParameters )
    {
      builder.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                               .addMember( "value", "$S", "unchecked" )
                               .build() );
    }

    {
      final List<? extends VariableElement> parameters = _method.getParameters();
      final int paramCount = parameters.size();
      for ( int i = 0; i < paramCount; i++ )
      {
        final VariableElement element = parameters.get( i );
        final TypeName parameterType = TypeName.get( _methodType.getParameterTypes().get( i ) );
        final ParameterSpec.Builder param =
          ParameterSpec.builder( parameterType, element.getSimpleName().toString(), Modifier.FINAL );
        ProcessorUtil.copyWhitelistedAnnotations( element, param );
        builder.addParameter( param.build() );
      }
    }

    GeneratorUtil.generateNotDisposedInvariant( builder, methodName );

    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> parameters = new ArrayList<>();
    sb.append( "return " );
    if ( hasTypeParameters )
    {
      sb.append( "($T) " );
      parameters.add( returnType.box() );
    }
    sb.append( "this.$N.get( " );
    parameters.add( getFieldName() );

    boolean first = true;
    for ( final VariableElement element : _method.getParameters() )
    {
      if ( !first )
      {
        sb.append( ", " );
      }
      first = false;
      sb.append( "$N" );
      parameters.add( element.getSimpleName().toString() );
    }
    sb.append( " )" );

    builder.addStatement( sb.toString(), parameters.toArray() );

    return builder.build();
  }
}
