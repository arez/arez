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
  private Priority _priority;
  private boolean _reportResult;
  private boolean _observeLowerPriorityDependencies;
  private boolean _readOutsideTransaction;
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
  private String getName()
  {
    return _name;
  }

  @Nonnull
  private String getCollectionCacheDataActiveFieldName()
  {
    return Generator.OBSERVABLE_DATA_FIELD_PREFIX + "$$cache_active$$_" + getName();
  }

  @Nonnull
  private String getCollectionCacheDataFieldName()
  {
    return Generator.OBSERVABLE_DATA_FIELD_PREFIX + "$$cache$$_" + getName();
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
                   @Nonnull final Priority priority,
                   final boolean reportResult,
                   final boolean observeLowerPriorityDependencies,
                   final boolean readOutsideTransaction,
                   @Nonnull final String depType )
    throws ProcessorException
  {
    //The caller already verified that no duplicate computable have been defined
    assert null == _method;
    MemberChecks.mustBeWrappable( _componentDescriptor.getElement(),
                                  Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                  Constants.MEMOIZE_ANNOTATION_CLASSNAME,
                                  method );
    MemberChecks.mustReturnAValue( Constants.MEMOIZE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.MEMOIZE_ANNOTATION_CLASSNAME, method );

    _method = Objects.requireNonNull( method );
    _methodType = Objects.requireNonNull( methodType );
    _keepAlive = keepAlive;
    _priority = Objects.requireNonNull( priority );
    _reportResult = reportResult;
    _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
    _readOutsideTransaction = readOutsideTransaction;
    _depType = Objects.requireNonNull( depType );

    if ( isMethodReturnType( Stream.class ) )
    {
      throw new ProcessorException( "@Memoize target must not return a value of type java.util.stream.Stream " +
                                    "as the type is single use and thus does not make sense to cache as a " +
                                    "computable value", method );
    }
  }

  void setRefMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
    throws ProcessorException
  {
    MemberChecks.mustBeSubclassCallable( _componentDescriptor.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME,
                                         method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME, method );

    if ( null != _refMethod )
    {
      throw new ProcessorException( "@ComputableValueRef target duplicates existing method named " +
                                    _refMethod.getSimpleName(), method );
    }
    else
    {
      _refMethod = Objects.requireNonNull( method );
      _refMethodType = Objects.requireNonNull( methodType );
    }
  }

  void setOnActivate( @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    MemberChecks.mustNotBeAbstract( Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustBeSubclassCallable( _componentDescriptor.getElement(),
                                         Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                         Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME,
                                         method );
    final List<? extends VariableElement> parameters = method.getParameters();

    if (
      !(
        parameters.isEmpty() ||
        ( 1 == parameters.size() &&
          Constants.COMPUTABLE_VALUE_CLASSNAME.equals( toRawType( parameters.get( 0 ).asType() ).toString() ) )
      )
    )
    {
      throw new ProcessorException( "@OnActivate target must not have any parameters or must have a single " +
                                    "parameter of type arez.ComputableValue", method );
    }

    MemberChecks.mustNotReturnAnyValue( Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME, method );
    MemberChecks.mustNotThrowAnyExceptions( Constants.ON_ACTIVATE_ANNOTATION_CLASSNAME, method );

    if ( null != _onActivate )
    {
      throw new ProcessorException( "@OnActivate target duplicates existing method named " +
                                    _onActivate.getSimpleName(), method );
    }
    else
    {
      _onActivate = Objects.requireNonNull( method );
    }
  }

  @Nonnull
  private TypeName toRawType( @Nonnull final TypeMirror type )
  {
    final TypeName typeName = TypeName.get( type );
    if ( typeName instanceof ParameterizedTypeName )
    {
      return ( (ParameterizedTypeName) typeName ).rawType;
    }
    else
    {
      return typeName;
    }
  }

  void setOnDeactivate( @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    MemberChecks.mustBeLifecycleHook( _componentDescriptor.getElement(),
                                      Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                      Constants.ON_DEACTIVATE_ANNOTATION_CLASSNAME,
                                      method );
    if ( null != _onDeactivate )
    {
      throw new ProcessorException( "@OnDeactivate target duplicates existing method named " +
                                    _onDeactivate.getSimpleName(),
                                    method );
    }
    else
    {
      _onDeactivate = Objects.requireNonNull( method );
    }
  }

  void setOnStale( @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    MemberChecks.mustBeLifecycleHook( _componentDescriptor.getElement(),
                                      Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                      Constants.ON_STALE_ANNOTATION_CLASSNAME,
                                      method );
    if ( null != _onStale )
    {
      throw new ProcessorException( "@OnStale target duplicates existing method named " +
                                    _onStale.getSimpleName(),
                                    method );
    }
    else
    {
      _onStale = Objects.requireNonNull( method );
    }
  }

  void validate()
    throws ProcessorException
  {
    if ( null == _method )
    {
      if ( null != _onActivate )
      {
        throw new ProcessorException( "@OnActivate exists but there is no corresponding @Memoize",
                                      _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ProcessorException( "@OnDeactivate exists but there is no corresponding @Memoize",
                                      _onDeactivate );
      }
      else if ( null != _refMethod )
      {
        throw new ProcessorException( "@ComputableValueRef exists but there is no corresponding @Memoize",
                                      _refMethod );
      }
      else
      {
        final ExecutableElement onStale = _onStale;
        assert null != onStale;
        throw new ProcessorException( "@OnStale exists but there is no corresponding @Memoize", onStale );
      }
    }
    if ( _keepAlive )
    {
      if ( !_method.getParameters().isEmpty() )
      {
        throw new ProcessorException( "@Memoize target specified parameter keepAlive as true but has parameters.",
                                      _method );
      }
      else if ( null != _onActivate )
      {
        throw new ProcessorException( "@OnActivate exists for @Memoize property that specified parameter " +
                                      "keepAlive as true.", _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ProcessorException( "@OnDeactivate exists for @Memoize property that specified parameter " +
                                      "keepAlive as true.", _onDeactivate );
      }
    }
    if ( !_method.getParameters().isEmpty() )
    {
      if ( null != _onActivate )
      {
        throw new ProcessorException( "@OnActivate target associated with @Memoize method that has parameters.",
                                      _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ProcessorException( "@OnDeactivate target associated with @Memoize method that has parameters.",
                                      _onDeactivate );
      }
      else if ( null != _onStale )
      {
        throw new ProcessorException( "@OnStale target associated with @Memoize method that has parameters.",
                                      _onStale );
      }
    }

    if ( null != _onActivate && null != _method )
    {
      final List<? extends VariableElement> parameters = _onActivate.getParameters();
      if ( 1 == parameters.size() )
      {
        final TypeName typeName = TypeName.get( parameters.get( 0 ).asType() );
        if ( typeName instanceof ParameterizedTypeName )
        {
          final ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
          final TypeName paramType = parameterizedTypeName.typeArguments.get( 0 );
          if ( !( paramType instanceof WildcardTypeName ) )
          {
            final TypeName actual = TypeName.get( _method.getReturnType() );
            if ( !actual.box().toString().equals( paramType.toString() ) )
            {
              throw new ProcessorException( "@OnActivate target has a parameter of type ComputableValue with a " +
                                            "type parameter of " + paramType + " but the @Memoize method " +
                                            "returns a type of " + actual, _onActivate );
            }
          }
        }
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
          throw new ProcessorException( "@ComputableValueRef target has a type parameter of " + expectedType +
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
        throw new ProcessorException( "@ComputableValueRef target and the associated @Memoize " +
                                      "target do not have the same parameters.", _method );
      }
    }
    else if ( _depType.equals( "AREZ_OR_EXTERNAL" ) )
    {
      assert null != _method;
      throw new ProcessorException( "@Memoize target specified depType = AREZ_OR_EXTERNAL but " +
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
      ParameterizedTypeName.get( Generator.COMPUTABLE_VALUE_CLASSNAME, parameterType );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName, getFieldName(), Modifier.FINAL, Modifier.PRIVATE ).
        addAnnotation( Generator.NONNULL_CLASSNAME );
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
      ParameterizedTypeName.get( Generator.MEMOIZE_CACHE_CLASSNAME, parameterType );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName,
                         getFieldName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( Generator.NONNULL_CLASSNAME );
    builder.addField( field.build() );
  }

  @Nonnull
  private String getFieldName()
  {
    return Generator.FIELD_PREFIX + getName();
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
                 "$T.areNamesEnabled() ? $N + $S : null, " );
      parameters.add( getFieldName() );
      parameters.add( Generator.AREZ_CLASSNAME );
      parameters.add( Generator.CONTEXT_VAR_NAME );
      parameters.add( Generator.AREZ_CLASSNAME );
      parameters.add( Generator.COMPONENT_VAR_NAME );
      parameters.add( Generator.AREZ_CLASSNAME );
      parameters.add( Generator.NAME_VAR_NAME );
      parameters.add( "." + getName() );

      if ( _componentDescriptor.isClassType() )
      {
        sb.append( "() -> super.$N(), " );
        parameters.add( _method.getSimpleName().toString() );
      }
      else
      {
        sb.append( "() -> $T.super.$N(), " );
        parameters.add( _componentDescriptor.getClassName() );
        parameters.add( _method.getSimpleName().toString() );
      }
      appendInitializerSuffix( parameters, sb, true );

      // Else part of ternary
      sb.append( " : $N.computable( " +
                 "$T.areNativeComponentsEnabled() ? $N : null, " +
                 "$T.areNamesEnabled() ? $N + $S : null, " );
      parameters.add( Generator.CONTEXT_VAR_NAME );
      parameters.add( Generator.AREZ_CLASSNAME );
      parameters.add( Generator.COMPONENT_VAR_NAME );
      parameters.add( Generator.AREZ_CLASSNAME );
      parameters.add( Generator.NAME_VAR_NAME );
      parameters.add( "." + getName() );
      if ( _componentDescriptor.isClassType() )
      {
        sb.append( "() -> super.$N(), " );
        parameters.add( _method.getSimpleName().toString() );
      }
      else
      {
        sb.append( "() -> $T.super.$N(), " );
        parameters.add( _componentDescriptor.getClassName() );
        parameters.add( _method.getSimpleName().toString() );
      }
      appendInitializerSuffix( parameters, sb, false );
    }
    else // hasHooks()
    {
      sb.append( "this.$N = $N.computable( " +
                 "$T.areNativeComponentsEnabled() ? $N : null, " +
                 "$T.areNamesEnabled() ? $N + $S : null, " );
      parameters.add( getFieldName() );
      parameters.add( Generator.CONTEXT_VAR_NAME );
      parameters.add( Generator.AREZ_CLASSNAME );
      parameters.add( Generator.COMPONENT_VAR_NAME );
      parameters.add( Generator.AREZ_CLASSNAME );
      parameters.add( Generator.NAME_VAR_NAME );
      parameters.add( "." + getName() );

      if ( _componentDescriptor.isClassType() )
      {
        sb.append( "() -> super.$N(), " );
        parameters.add( _method.getSimpleName().toString() );
      }
      else
      {
        sb.append( "() -> $T.super.$N(), " );
        parameters.add( _componentDescriptor.getClassName() );
        parameters.add( _method.getSimpleName().toString() );
      }
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
               "$T.areNamesEnabled() ? $N + $S : null, " );
    parameters.add( getFieldName() );
    parameters.add( Generator.MEMOIZE_CACHE_CLASSNAME );
    parameters.add( Generator.AREZ_CLASSNAME );
    parameters.add( Generator.CONTEXT_VAR_NAME );
    parameters.add( Generator.AREZ_CLASSNAME );
    parameters.add( Generator.COMPONENT_VAR_NAME );
    parameters.add( Generator.AREZ_CLASSNAME );
    parameters.add( Generator.NAME_VAR_NAME );
    parameters.add( "." + getName() );

    if ( _componentDescriptor.isClassType() )
    {
      sb.append( "args -> super.$N(" );
      parameters.add( _method.getSimpleName().toString() );
    }
    else
    {
      sb.append( "args -> $T.super.$N(" );
      parameters.add( _componentDescriptor.getClassName() );
      parameters.add( _method.getSimpleName().toString() );
    }

    int index = 0;
    for ( final TypeMirror arg : _methodType.getParameterTypes() )
    {
      if ( 0 != index )
      {
        sb.append( ", " );
      }
      if ( TypeName.get( arg ).equals( TypeName.OBJECT ) )
      {
        sb.append( "args[ " ).append( index ).append( " ]" );
      }
      else
      {
        sb.append( "($T) args[ " ).append( index ).append( " ]" );
        parameters.add( arg );
      }
      index++;
    }

    sb.append( "), " );
    sb.append( _method.getParameters().size() );
    sb.append( ", " );

    final ArrayList<String> flags = generateFlags();

    sb.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( Generator.COMPUTABLE_VALUE_FLAGS_CLASSNAME );
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
        if ( _onActivate.getParameters().isEmpty() )
        {
          sb.append( "this::$N" );
          parameters.add( _onActivate.getSimpleName().toString() );
        }
        else
        {
          sb.append( "this::$N" );
          parameters.add( getOnActivateHookMethodName() );
        }
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
      parameters.add( Generator.COMPUTABLE_VALUE_FLAGS_CLASSNAME );
    }

    sb.append( " )" );
  }

  @Nonnull
  private ArrayList<String> generateFlags()
  {
    final ArrayList<String> flags = new ArrayList<>();
    if ( Priority.NORMAL != _priority )
    {
      flags.add( "PRIORITY_" + _priority.name() );
    }

    if ( !_reportResult )
    {
      flags.add( "NO_REPORT_RESULT" );
    }
    if ( _observeLowerPriorityDependencies )
    {
      flags.add( "OBSERVE_LOWER_PRIORITY_DEPENDENCIES" );
    }
    if ( _readOutsideTransaction )
    {
      flags.add( "READ_OUTSIDE_TRANSACTION" );
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
    return Generator.FRAMEWORK_PREFIX + "onActivate_" + getName();
  }

  @Nonnull
  private String getOnDeactivateHookMethodName()
  {
    return Generator.FRAMEWORK_PREFIX + "onDeactivate_" + getName();
  }

  @Nonnull
  private String getOnStaleHookMethodName()
  {
    return Generator.FRAMEWORK_PREFIX + "onStale_" + getName();
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
    throws ProcessorException
  {
    assert null != _method;
    if ( _method.getParameters().isEmpty() )
    {
      builder.addMethod( buildNoParamsMemoize() );
      if ( ( null != _onActivate && !_onActivate.getParameters().isEmpty() ) || isCollectionType() )
      {
        builder.addMethod( buildOnActivateWrapperHook() );
      }

      if ( isCollectionType() )
      {
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
    throws ProcessorException
  {
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getOnActivateHookMethodName() );
    builder.addModifiers( Modifier.PRIVATE );

    if ( isCollectionType() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );
      block.addStatement( "this.$N = true", getCollectionCacheDataActiveFieldName() );
      block.addStatement( "this.$N = null", getCollectionCacheDataFieldName() );
      block.endControlFlow();
      builder.addCode( block.build() );
    }

    if ( null != _onActivate )
    {
      if ( _onActivate.getParameters().isEmpty() )
      {
        builder.addStatement( "$N()", _onActivate.getSimpleName().toString() );
      }
      else
      {
        builder.addStatement( "$N( $N )", _onActivate.getSimpleName().toString(), getFieldName() );
      }
    }
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildOnDeactivateWrapperHook()
    throws ProcessorException
  {
    assert isCollectionType();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getOnDeactivateHookMethodName() );
    builder.addModifiers( Modifier.PRIVATE );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );
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
    throws ProcessorException
  {
    assert isCollectionType();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getOnStaleHookMethodName() );
    builder.addModifiers( Modifier.PRIVATE );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() && this.$N )",
                            Generator.AREZ_CLASSNAME,
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
    throws ProcessorException
  {
    assert null != _method;
    assert null != _methodType;
    final String methodName = _method.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    GeneratorUtil.copyAccessModifiers( _method, builder );
    GeneratorUtil.copyExceptions( _methodType, builder );
    GeneratorUtil.copyTypeParameters( _methodType, builder );
    Generator.copyWhitelistedAnnotations( _method, builder );
    builder.addAnnotation( Override.class );
    final TypeName returnType = TypeName.get( _methodType.getReturnType() );
    builder.returns( returnType );
    Generator.generateNotDisposedInvariant( builder, methodName );

    if ( isCollectionType() )
    {
      if ( ProcessorUtil.hasNonnullAnnotation( getMethod() ) )
      {
        final CodeBlock.Builder block = CodeBlock.builder();
        block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );

        final CodeBlock.Builder guard = CodeBlock.builder();
        guard.beginControlFlow( "if ( null == this.$N )", getCollectionCacheDataFieldName() );
        guard.addStatement( "this.$N = $T.wrap( this.$N.get() )",
                            getCollectionCacheDataFieldName(),
                            Generator.COLLECTIONS_UTIL_CLASSNAME,
                            getFieldName() );
        guard.nextControlFlow( "else" );
        guard.add( "// Make sure that we are observing computable value\n" );
        guard.addStatement( "this.$N.get()", getFieldName() );
        guard.endControlFlow();
        block.add( guard.build() );
        block.addStatement( "return $N", getCollectionCacheDataFieldName() );

        block.nextControlFlow( "else" );

        block.addStatement( "return this.$N.get()", getFieldName() );
        block.endControlFlow();

        builder.addCode( block.build() );
      }
      else
      {
        final CodeBlock.Builder block = CodeBlock.builder();
        block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", Generator.AREZ_CLASSNAME );

        final String result = "$$ar$$_result";
        if ( returnType.isPrimitive() )
        {
          block.addStatement( "final $T $N = ($T) this.$N.get()",
                              returnType,
                              result,
                              returnType.box(),
                              getFieldName() );
        }
        else
        {
          block.addStatement( "final $T $N = this.$N.get()", returnType, result, getFieldName() );
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

        block.addStatement( "return this.$N.get()", getFieldName() );
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
    throws ProcessorException
  {
    assert null != _refMethod;
    assert null != _refMethodType;
    final MethodSpec.Builder method =
      GeneratorUtil.refMethod( _componentDescriptor.getProcessingEnv(), _componentDescriptor.getElement(), _refMethod );
    Generator.generateNotDisposedInvariant( method, _refMethod.getSimpleName().toString() );

    final List<? extends VariableElement> parameters = _refMethod.getParameters();
    final int paramCount = parameters.size();
    for ( int i = 0; i < paramCount; i++ )
    {
      final VariableElement element = parameters.get( i );
      final TypeName parameterType = TypeName.get( _refMethodType.getParameterTypes().get( i ) );
      final ParameterSpec.Builder param =
        ParameterSpec.builder( parameterType, element.getSimpleName().toString(), Modifier.FINAL );
      Generator.copyWhitelistedAnnotations( element, param );
      method.addParameter( param.build() );
    }

    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> params = new ArrayList<>();
    sb.append( "return this.$N.getComputableValue( " );
    params.add( getFieldName() );

    boolean first = true;
    for ( final VariableElement element : _refMethod.getParameters() )
    {
      if ( !first )
      {
        sb.append( ", " );
      }
      first = false;
      sb.append( "$N" );
      params.add( element.getSimpleName().toString() );
    }
    sb.append( " )" );

    method.addStatement( sb.toString(), params.toArray() );
    return method.build();
  }

  /**
   * Generate the accessor for ref method.
   */
  @Nonnull
  private MethodSpec buildNoParamsRefMethod()
    throws ProcessorException
  {
    assert null != _refMethod;
    final MethodSpec.Builder method =
      GeneratorUtil.refMethod( _componentDescriptor.getProcessingEnv(), _componentDescriptor.getElement(), _refMethod );
    Generator.generateNotDisposedInvariant( method, _refMethod.getSimpleName().toString() );
    return method
      .addStatement( "return $N", getFieldName() )
      .build();
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
    throws ProcessorException
  {
    assert null != _method;
    assert null != _methodType;
    final String methodName = _method.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    GeneratorUtil.copyAccessModifiers( _method, builder );
    GeneratorUtil.copyExceptions( _methodType, builder );
    GeneratorUtil.copyTypeParameters( _methodType, builder );
    Generator.copyWhitelistedAnnotations( _method, builder );
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
        Generator.copyWhitelistedAnnotations( element, param );
        builder.addParameter( param.build() );
      }
    }

    Generator.generateNotDisposedInvariant( builder, methodName );

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
