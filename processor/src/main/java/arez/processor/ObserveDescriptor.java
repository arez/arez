package arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of @Observe methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class ObserveDescriptor
{
  static final Pattern ON_DEPS_CHANGE_PATTERN = Pattern.compile( "^on([A-Z].*)DepsChange" );
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  private boolean _mutation;
  private String _priority;
  private boolean _arezExecutor;
  private boolean _reportParameters;
  private boolean _reportResult;
  private String _depType;
  private boolean _observeLowerPriorityDependencies;
  private boolean _nestedActionsAllowed;
  private boolean _requireEnvironment;
  @Nullable
  private ExecutableElement _observed;
  @Nullable
  private ExecutableType _observedType;
  @Nullable
  private ExecutableElement _onDepsChange;
  @Nullable
  private ExecutableElement _refMethod;
  @Nullable
  private ExecutableType _refMethodType;

  ObserveDescriptor( @Nonnull final ComponentDescriptor componentDescriptor, @Nonnull final String name )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  void setRefMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    _refMethod = Objects.requireNonNull( method );
    _refMethodType = Objects.requireNonNull( methodType );
  }

  @Nonnull
  ExecutableElement getObserved()
  {
    assert null != _observed;
    return _observed;
  }

  boolean hasObserved()
  {
    return null != _observed;
  }

  void setObservedMethod( final boolean mutation,
                          @Nonnull final String priority,
                          final boolean arezExecutor,
                          final boolean reportParameters,
                          final boolean reportResult,
                          @Nonnull final String depType,
                          final boolean observeLowerPriorityDependencies,
                          final boolean nestedActionsAllowed,
                          final boolean requireEnvironment,
                          @Nonnull final ExecutableElement method,
                          @Nonnull final ExecutableType trackedMethodType )
  {
    MethodChecks.mustBeWrappable( _componentDescriptor.getElement(), Constants.OBSERVED_ANNOTATION_CLASSNAME, method );

    if ( arezExecutor )
    {
      if ( !method.getParameters().isEmpty() )
      {
        throw new ArezProcessorException( "@Observe target must not have any parameters when executor=AREZ", method );
      }
      if ( !method.getThrownTypes().isEmpty() )
      {
        throw new ArezProcessorException( "@Observe target must not throw any exceptions when executor=AREZ", method );
      }
      if ( TypeKind.VOID != method.getReturnType().getKind() )
      {
        throw new ArezProcessorException( "@Observe target must not return a value when executor=AREZ", method );
      }
      if ( method.getModifiers().contains( Modifier.PUBLIC ) )
      {
        throw new ArezProcessorException( "@Observe target must not be public when executor=AREZ", method );
      }
      if ( !reportParameters )
      {
        throw new ArezProcessorException( "@Observe target must not specify reportParameters parameter " +
                                          "when executor=AREZ", method );
      }
      if ( !reportResult )
      {
        throw new ArezProcessorException( "@Observe target must not specify reportResult parameter " +
                                          "when executor=AREZ", method );
      }
    }

    if ( null != _observed )
    {
      throw new ArezProcessorException( "@Observe target duplicates existing method named " +
                                        _observed.getSimpleName(), method );
    }
    else
    {
      _mutation = mutation;
      _priority = Objects.requireNonNull( priority );
      _arezExecutor = arezExecutor;
      _reportParameters = reportParameters;
      _reportResult = reportResult;
      _depType = Objects.requireNonNull( depType );
      _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
      _nestedActionsAllowed = nestedActionsAllowed;
      _requireEnvironment = requireEnvironment;
      _observed = Objects.requireNonNull( method );
      _observedType = Objects.requireNonNull( trackedMethodType );
    }
  }

  @Nonnull
  ExecutableElement getOnDepsChange()
  {
    assert null != _onDepsChange;
    return _onDepsChange;
  }

  boolean hasOnDepsChange()
  {
    return null != _onDepsChange;
  }

  void setOnDepsChange( @Nonnull final ExecutableElement method )
  {
    MethodChecks.mustBeLifecycleHook( _componentDescriptor.getElement(),
                                      Constants.ON_DEPS_CHANGE_ANNOTATION_CLASSNAME,
                                      method );
    if ( null != _onDepsChange )
    {
      throw new ArezProcessorException( "@OnDepsChange target duplicates existing method named " +
                                        _onDepsChange.getSimpleName(), method );

    }
    else
    {
      _onDepsChange = Objects.requireNonNull( method );
    }
  }

  /**
   * Build any fields required by
   */
  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    final FieldSpec.Builder field =
      FieldSpec.builder( GeneratorUtil.OBSERVER_CLASSNAME, getFieldName(), Modifier.FINAL, Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    builder.addField( field.build() );
  }

  @Nonnull
  private String getFieldName()
  {
    return GeneratorUtil.FIELD_PREFIX + getName();
  }

  void validate()
  {
    if ( _arezExecutor && null != _onDepsChange && null == _refMethod )
    {
      assert null != _observed;
      throw new ArezProcessorException( "@Observe target with parameter executor=AREZ defined an @OnDepsChange " +
                                        "method but has not defined an @ObserverRef method and thus can never" +
                                        "schedule observer.", _observed );
    }
    if ( !_arezExecutor && null == _onDepsChange )
    {
      assert null != _observed;
      throw new ArezProcessorException( "@Observe target defined parameter executor=APPLICATION but does not " +
                                        "specify an @OnDepsChange method.", _observed );
    }
    if ( "AREZ_OR_EXTERNAL".equals( _depType ) && null == _refMethod )
    {
      assert null != _observed;
      throw new ArezProcessorException( "@Observe target with parameter depType=AREZ_OR_EXTERNAL has not " +
                                        "defined an @ObserverRef method and thus can not invoke reportStale().",
                                        _observed );
    }
    if ( _arezExecutor )
    {
      _requireEnvironment = true;
    }
  }

  /**
   * Setup initial state of observed in constructor.
   */
  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    if ( _arezExecutor )
    {
      buildObserverInitializer( builder );
    }
    else
    {
      buildTrackerInitializer( builder );
    }
  }

  private void buildObserverInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N().observer( $T.areNativeComponentsEnabled() ? this.$N : null, " +
               "$T.areNamesEnabled() ? $N() + $S : null, () -> super.$N(), " );
    parameters.add( getFieldName() );
    parameters.add( _componentDescriptor.getContextMethodName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    parameters.add( getObserved().getSimpleName().toString() );
    if ( null != _onDepsChange )
    {
      sb.append( "() -> super.$N(), " );
      parameters.add( _onDepsChange.getSimpleName().toString() );
    }

    appendFlags( parameters, sb );

    sb.append( " )" );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private void buildTrackerInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    assert null != _onDepsChange;
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N().tracker( " +
               "$T.areNativeComponentsEnabled() ? this.$N : null, " +
               "$T.areNamesEnabled() ? $N() + $S : null, () -> super.$N(), " );
    parameters.add( getFieldName() );
    parameters.add( _componentDescriptor.getContextMethodName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    parameters.add( _onDepsChange.getSimpleName().toString() );

    appendFlags( parameters, sb );

    sb.append( " )" );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private void appendFlags( @Nonnull final ArrayList<Object> parameters, @Nonnull final StringBuilder expression )
  {
    final ArrayList<String> flags = new ArrayList<>();
    flags.add( "RUN_LATER" );

    if ( _observeLowerPriorityDependencies )
    {
      flags.add( "OBSERVE_LOWER_PRIORITY_DEPENDENCIES" );
    }
    if ( !_reportResult )
    {
      flags.add( "NO_REPORT_RESULT" );
    }
    if ( _nestedActionsAllowed )
    {
      flags.add( "NESTED_ACTIONS_ALLOWED" );
    }
    else
    {
      flags.add( "NESTED_ACTIONS_DISALLOWED" );
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
    if ( _requireEnvironment )
    {
      flags.add( "ENVIRONMENT_REQUIRED" );
    }
    else
    {
      flags.add( "ENVIRONMENT_NOT_REQUIRED" );
    }
    if ( _mutation )
    {
      flags.add( "READ_WRITE" );
    }
    if ( !"NORMAL".equals( _priority ) )
    {
      flags.add( "PRIORITY_" + _priority );
    }

    expression.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( GeneratorUtil.FLAGS_CLASSNAME );
    }
  }

  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", getFieldName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    if ( _arezExecutor )
    {
      builder.addMethod( buildObserved() );
    }
    else
    {
      builder.addMethod( buildTracked() );
    }
    if ( null != _refMethod )
    {
      builder.addMethod( buildObserverRefMethod() );
    }
  }

  /**
   * Generate the accessor for ref method.
   */
  @Nonnull
  private MethodSpec buildObserverRefMethod()
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
   * Generate the tracked wrapper.
   */
  @Nonnull
  private MethodSpec buildTracked()
    throws ArezProcessorException
  {
    assert null != _observed;
    assert null != _observedType;

    final String methodName = _observed.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _observed, builder );
    ProcessorUtil.copyExceptions( _observedType, builder );
    ProcessorUtil.copyTypeParameters( _observedType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _observed, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _observedType.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final boolean isProcedure = returnType.getKind() == TypeKind.VOID;
    final List<? extends TypeMirror> thrownTypes = _observed.getThrownTypes();
    final boolean isSafe = thrownTypes.isEmpty();

    final StringBuilder statement = new StringBuilder();
    final ArrayList<Object> params = new ArrayList<>();

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );
    if ( !isProcedure )
    {
      statement.append( "return " );
    }
    statement.append( "$N()." );
    params.add( _componentDescriptor.getContextMethodName() );

    if ( isProcedure && isSafe )
    {
      statement.append( "safeObserve" );
    }
    else if ( isProcedure )
    {
      statement.append( "observe" );
    }
    else if ( isSafe )
    {
      statement.append( "safeObserve" );
    }
    else
    {
      statement.append( "observe" );
    }

    statement.append( "( this.$N, " );
    params.add( getFieldName() );

    statement.append( "() -> super." );
    statement.append( _observed.getSimpleName() );
    statement.append( "(" );

    final List<? extends VariableElement> parameters = _observed.getParameters();
    final int paramCount = parameters.size();
    if ( 0 != paramCount )
    {
      statement.append( " " );
    }

    boolean firstParam = true;
    for ( int i = 0; i < paramCount; i++ )
    {
      final VariableElement element = parameters.get( i );
      final TypeName parameterType = TypeName.get( _observedType.getParameterTypes().get( i ) );
      final ParameterSpec.Builder param =
        ParameterSpec.builder( parameterType, element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyWhitelistedAnnotations( element, param );
      builder.addParameter( param.build() );
      params.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        statement.append( ", " );
      }
      firstParam = false;
      statement.append( "$N" );
    }
    if ( 0 != paramCount )
    {
      statement.append( " " );
    }

    statement.append( "), " );
    if ( _reportParameters && !parameters.isEmpty() )
    {
      statement.append( "$T.areSpiesEnabled() ? new $T[] { " );
      params.add( GeneratorUtil.AREZ_CLASSNAME );
      params.add( Object.class );
      firstParam = true;
      for ( final VariableElement parameter : parameters )
      {
        if ( !firstParam )
        {
          statement.append( ", " );
        }
        firstParam = false;
        params.add( parameter.getSimpleName().toString() );
        statement.append( "$N" );
      }
      statement.append( " } : null" );
    }
    else
    {
      statement.append( "null" );
    }
    statement.append( " )" );

    GeneratorUtil.generateTryBlock( builder,
                                    thrownTypes,
                                    b -> b.addStatement( statement.toString(), params.toArray() ) );

    return builder.build();
  }

  /**
   * Generate the observed wrapper.
   * This is wrapped to block user from directly invoking observed method.
   */
  @Nonnull
  private MethodSpec buildObserved()
    throws ArezProcessorException
  {
    assert null != _observed;
    assert null != _observedType;
    final String methodName = _observed.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _observed, builder );
    ProcessorUtil.copyExceptions( _observedType, builder );
    ProcessorUtil.copyTypeParameters( _observedType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _observed, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _observed.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", GeneratorUtil.AREZ_CLASSNAME );
    block.addStatement( "$T.fail( () -> \"Observe method named '$N' invoked but @Observe(executor=AREZ) " +
                        "annotated methods should only be invoked by the runtime.\" )",
                        GeneratorUtil.GUARDS_CLASSNAME,
                        methodName );
    block.endControlFlow();

    builder.addCode( block.build() );
    // This super is generated so that the GWT compiler in production model will identify this as a method
    // that only contains a super invocation and will thus inline it. If the body is left empty then the
    // GWT compiler will be required to keep the empty method present because it can not determine that the
    // empty method will never be invoked.
    builder.addStatement( "super.$N()", _observed.getSimpleName() );

    return builder.build();
  }
}
