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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of @Track methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class TrackedDescriptor
{
  static final Pattern ON_DEPS_CHANGED_PATTERN = Pattern.compile( "^on([A-Z].*)DepsChanged" );
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  private boolean _mutation;
  private String _priority;
  private boolean _reportParameters;
  private boolean _observeLowerPriorityDependencies;
  @Nullable
  private ExecutableElement _trackedMethod;
  @Nullable
  private ExecutableType _trackedMethodType;
  @Nullable
  private ExecutableElement _onDepsChangedMethod;
  @Nullable
  private ExecutableElement _refMethod;
  @Nullable
  private ExecutableType _refMethodType;

  TrackedDescriptor( @Nonnull final ComponentDescriptor componentDescriptor, @Nonnull final String name )
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
  ExecutableElement getTrackedMethod()
  {
    assert null != _trackedMethod;
    return _trackedMethod;
  }

  @Nonnull
  ExecutableElement getOnDepsChangedMethod()
  {
    assert null != _onDepsChangedMethod;
    return _onDepsChangedMethod;
  }

  boolean hasTrackedMethod()
  {
    return null != _trackedMethod;
  }

  void setTrackedMethod( final boolean mutation,
                         @Nonnull final String priority,
                         final boolean reportParameters,
                         final boolean observeLowerPriorityDependencies,
                         @Nonnull final ExecutableElement method,
                         @Nonnull final ExecutableType trackedMethodType )
  {
    MethodChecks.mustBeWrappable( _componentDescriptor.getElement(), Constants.TRACK_ANNOTATION_CLASSNAME, method );
    if ( null != _trackedMethod )
    {
      throw new ArezProcessorException( "@Track target duplicates existing method named " +
                                        _trackedMethod.getSimpleName(), method );

    }
    else
    {
      _mutation = mutation;
      _priority = Objects.requireNonNull( priority );
      _reportParameters = reportParameters;
      _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
      _trackedMethod = Objects.requireNonNull( method );
      _trackedMethodType = Objects.requireNonNull( trackedMethodType );
    }
  }

  void setOnDepsChangedMethod( @Nonnull final ExecutableElement method )
  {
    MethodChecks.mustBeLifecycleHook( _componentDescriptor.getElement(),
                                      Constants.ON_DEPS_CHANGED_ANNOTATION_CLASSNAME,
                                      method );
    if ( null != _onDepsChangedMethod )
    {
      throw new ArezProcessorException( "@OnDepsChanged target duplicates existing method named " +
                                        _onDepsChangedMethod.getSimpleName(), method );

    }
    else
    {
      _onDepsChangedMethod = Objects.requireNonNull( method );
    }
  }

  boolean hasOnDepsChangedMethod()
  {
    return null != _onDepsChangedMethod;
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

  /**
   * Setup initial state of tracked in constructor.
   */
  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    assert null != _onDepsChangedMethod;
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N().tracker( " +
               "$T.areNativeComponentsEnabled() ? this.$N : null, " +
               "$T.areNamesEnabled() ? $N() + $S : null, " );
    parameters.add( getFieldName() );
    parameters.add( _componentDescriptor.getContextMethodName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    sb.append( _mutation );
    sb.append( ", () -> super.$N()" );
    parameters.add( _onDepsChangedMethod.getSimpleName().toString() );

    if ( !"NORMAL".equals( _priority ) || _observeLowerPriorityDependencies )
    {
      sb.append( ", $T.$N" );
      parameters.add( GeneratorUtil.PRIORITY_CLASSNAME );
      parameters.add( _priority );
      if ( _observeLowerPriorityDependencies )
      {
        sb.append( ", true" );
      }
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
    builder.addMethod( buildTracked() );
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
    ProcessorUtil.copyDocumentedAnnotations( _refMethod, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _refMethodType.getReturnType() ) );

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );

    builder.addStatement( "return $N", getFieldName() );

    return builder.build();
  }

  /**
   * Generate the tracked wrapper.
   * This is wrapped in case the user ever wants to explicitly call method
   */
  @Nonnull
  private MethodSpec buildTracked()
    throws ArezProcessorException
  {
    assert null != _trackedMethod;
    assert null != _trackedMethodType;

    final String methodName = _trackedMethod.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _trackedMethod, builder );
    ProcessorUtil.copyExceptions( _trackedMethodType, builder );
    ProcessorUtil.copyTypeParameters( _trackedMethodType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _trackedMethod, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _trackedMethodType.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final boolean isProcedure = returnType.getKind() == TypeKind.VOID;
    final List<? extends TypeMirror> thrownTypes = _trackedMethod.getThrownTypes();
    final boolean isSafe = thrownTypes.isEmpty();

    final StringBuilder statement = new StringBuilder();
    final ArrayList<Object> parameterNames = new ArrayList<>();

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );
    if ( !isProcedure )
    {
      statement.append( "return " );
    }
    statement.append( "$N()." );
    parameterNames.add( _componentDescriptor.getContextMethodName() );

    if ( isProcedure && isSafe )
    {
      statement.append( "safeTrack" );
    }
    else if ( isProcedure )
    {
      statement.append( "track" );
    }
    else if ( isSafe )
    {
      statement.append( "safeTrack" );
    }
    else
    {
      statement.append( "track" );
    }

    statement.append( "( this.$N, " );
    parameterNames.add( getFieldName() );

    statement.append( "() -> super." );
    statement.append( _trackedMethod.getSimpleName() );
    statement.append( "(" );

    boolean firstParam = true;
    final List<? extends VariableElement> parameters = _trackedMethod.getParameters();
    final int paramCount = parameters.size();
    for ( int i = 0; i < paramCount; i++ )
    {
      final VariableElement element = parameters.get( i );
      final TypeName parameterType = TypeName.get( _trackedMethodType.getParameterTypes().get( i ) );
      final ParameterSpec.Builder param =
        ParameterSpec.builder( parameterType, element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyDocumentedAnnotations( element, param );
      builder.addParameter( param.build() );
      parameterNames.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        statement.append( "," );
      }
      firstParam = false;
      statement.append( "$N" );
    }

    statement.append( ")" );
    if ( _reportParameters )
    {
      for ( final VariableElement parameter : parameters )
      {
        parameterNames.add( parameter.getSimpleName().toString() );
        statement.append( ", $N" );
      }
    }
    statement.append( " )" );

    GeneratorUtil.generateTryBlock( builder,
                                    thrownTypes,
                                    b -> b.addStatement( statement.toString(), parameterNames.toArray() ) );

    return builder.build();
  }
}
