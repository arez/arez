package org.realityforge.arez.processor;

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
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

/**
 * The class that represents the parsed state of @Tracked methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class TrackedDescriptor
{
  static final Pattern ON_DEPS_UPDATED_PATTERN = Pattern.compile( "^on([A-Z].*)DepsUpdated" );

  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  private boolean _mutation;
  @Nullable
  private ExecutableElement _trackedMethod;
  @Nullable
  private ExecutableType _trackedMethodType;
  @Nullable
  private ExecutableElement _onDepsUpdatedMethod;

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

  @Nonnull
  ExecutableElement getTrackedMethod()
  {
    assert null != _trackedMethod;
    return _trackedMethod;
  }

  @Nonnull
  ExecutableElement getOnDepsUpdatedMethod()
  {
    assert null != _onDepsUpdatedMethod;
    return _onDepsUpdatedMethod;
  }

  boolean hasTrackedMethod()
  {
    return null != _trackedMethod;
  }

  void setTrackedMethod( final boolean mutation,
                         @Nonnull final ExecutableElement method,
                         @Nonnull final ExecutableType trackedMethodType )
  {
    MethodChecks.mustBeOverridable( Tracked.class, method );
    if ( null != _trackedMethod )
    {
      throw new ArezProcessorException( "@Tracked target duplicates existing method named " +
                                        _trackedMethod.getSimpleName(), method );

    }
    else
    {
      _mutation = mutation;
      _trackedMethod = Objects.requireNonNull( method );
      _trackedMethodType = Objects.requireNonNull( trackedMethodType );
    }
  }

  void setOnDepsUpdatedMethod( @Nonnull final ExecutableElement method )
  {
    MethodChecks.mustBeLifecycleHook( OnDepsUpdated.class, method );
    if ( null != _onDepsUpdatedMethod )
    {
      throw new ArezProcessorException( "@OnDepsUpdated target duplicates existing method named " +
                                        _onDepsUpdatedMethod.getSimpleName(), method );

    }
    else
    {
      _onDepsUpdatedMethod = Objects.requireNonNull( method );
    }
  }

  boolean hasOnDepsUpdatedMethod()
  {
    return null != _onDepsUpdatedMethod;
  }

  /**
   * Build any fields required by
   */
  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    final FieldSpec.Builder field =
      FieldSpec.builder( GeneratorUtil.OBSERVER_CLASSNAME,
                         GeneratorUtil.FIELD_PREFIX + getName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( Nonnull.class );
    builder.addField( field.build() );
  }

  /**
   * Setup initial state of tracked in constructor.
   */
  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    assert null != _onDepsUpdatedMethod;
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = this.$N.tracker( this.$N.areNamesEnabled() ? " );
    parameters.add( GeneratorUtil.FIELD_PREFIX + getName() );
    parameters.add( GeneratorUtil.CONTEXT_FIELD_NAME );
    parameters.add( GeneratorUtil.CONTEXT_FIELD_NAME );
    if ( _componentDescriptor.isSingleton() )
    {
      sb.append( "$S" );
      parameters.add( _componentDescriptor.getNamePrefix() + getName() );
    }
    else
    {
      sb.append( "$N() + $S" );
      parameters.add( _componentDescriptor.getComponentNameMethodName() );
      parameters.add( "." + getName() );
    }
    sb.append( " : null, " );
    sb.append( _mutation );
    sb.append( ", super::$N )" );
    parameters.add( _onDepsUpdatedMethod.getSimpleName().toString() );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "$N.dispose()", GeneratorUtil.FIELD_PREFIX + getName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildTracked() );
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

    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _trackedMethod.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _trackedMethod, builder );
    ProcessorUtil.copyExceptions( _trackedMethodType, builder );
    ProcessorUtil.copyTypeParameters( _trackedMethodType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _trackedMethod, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _trackedMethodType.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final boolean isProcedure = returnType.getKind() == TypeKind.VOID;
    final boolean isSafe = _trackedMethod.getThrownTypes().isEmpty();

    final StringBuilder statement = new StringBuilder();
    final ArrayList<Object> parameterNames = new ArrayList<>();

    if ( !isProcedure )
    {
      statement.append( "final $T $N = " );
      parameterNames.add( TypeName.get( returnType ) );
      parameterNames.add( GeneratorUtil.RESULT_VARIABLE_NAME );
    }
    statement.append( "this.$N." );
    parameterNames.add( GeneratorUtil.CONTEXT_FIELD_NAME );

    if ( isProcedure && isSafe )
    {
      statement.append( "safeProcedure" );
    }
    else if ( isProcedure )
    {
      statement.append( "procedure" );
    }
    else if ( isSafe )
    {
      statement.append( "safeFunction" );
    }
    else
    {
      statement.append( "function" );
    }

    statement.append( "( this.$N, " );
    parameterNames.add( GeneratorUtil.FIELD_PREFIX + getName() );

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

    statement.append( ") )" );

    if ( _componentDescriptor.isDisposable() )
    {
      builder.addStatement( "assert !$N", GeneratorUtil.DISPOSED_FIELD_NAME );
    }

    builder.addStatement( "$T $N = null", Throwable.class, GeneratorUtil.THROWABLE_VARIABLE_NAME );
    builder.addStatement( "$T $N = false", boolean.class, GeneratorUtil.COMPLETED_VARIABLE_NAME );
    builder.addStatement( "$T $N = 0L", long.class, GeneratorUtil.STARTED_AT_VARIABLE_NAME );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "try" );

    GeneratorUtil.actionStartedSpyEvent( _componentDescriptor, _name, true, _trackedMethod, codeBlock );

    codeBlock.addStatement( statement.toString(), parameterNames.toArray() );
    codeBlock.addStatement( "$N = true", GeneratorUtil.COMPLETED_VARIABLE_NAME );
    GeneratorUtil.actionCompletedSpyEvent( _componentDescriptor, _name, true, _trackedMethod, isProcedure, codeBlock );
    if ( !isProcedure )
    {
      codeBlock.addStatement( "return $N", GeneratorUtil.RESULT_VARIABLE_NAME );
    }

    for ( final TypeMirror exception : _trackedMethod.getThrownTypes() )
    {
      codeBlock.nextControlFlow( "catch( final $T $N )", exception, GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.addStatement( "throw $N", GeneratorUtil.CAUGHT_THROWABLE_NAME );
    }

    if ( _trackedMethod.getThrownTypes().stream().noneMatch( t -> t.toString().equals( "java.lang.Throwable" ) ) )
    {
      if ( _trackedMethod.getThrownTypes().stream().noneMatch( t -> t.toString().equals( "java.lang.Exception" ) ) )
      {
        if ( _trackedMethod.getThrownTypes()
          .stream()
          .noneMatch( t -> t.toString().equals( "java.lang.RuntimeException" ) ) )
        {
          codeBlock.nextControlFlow( "catch( final $T $N )",
                                     RuntimeException.class,
                                     GeneratorUtil.CAUGHT_THROWABLE_NAME );
          codeBlock.addStatement( "$N = $N",
                                  GeneratorUtil.THROWABLE_VARIABLE_NAME,
                                  GeneratorUtil.CAUGHT_THROWABLE_NAME );
          codeBlock.addStatement( "throw $N", GeneratorUtil.CAUGHT_THROWABLE_NAME );
        }
        codeBlock.nextControlFlow( "catch( final $T $N )", Exception.class, GeneratorUtil.CAUGHT_THROWABLE_NAME );
        codeBlock.addStatement( "$N = $N", GeneratorUtil.THROWABLE_VARIABLE_NAME, GeneratorUtil.CAUGHT_THROWABLE_NAME );
        codeBlock.addStatement( "throw new $T( $N )",
                                IllegalStateException.class,
                                GeneratorUtil.CAUGHT_THROWABLE_NAME );
      }
      codeBlock.nextControlFlow( "catch( final $T $N )", Error.class, GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.addStatement( "$N = $N", GeneratorUtil.THROWABLE_VARIABLE_NAME, GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.addStatement( "throw $N", GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.nextControlFlow( "catch( final $T $N )", Throwable.class, GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.addStatement( "$N = $N", GeneratorUtil.THROWABLE_VARIABLE_NAME, GeneratorUtil.CAUGHT_THROWABLE_NAME );
      codeBlock.addStatement( "throw new $T( $N )", IllegalStateException.class, GeneratorUtil.CAUGHT_THROWABLE_NAME );
    }
    codeBlock.nextControlFlow( "finally" );

    // Send completed spy event if necessary
    codeBlock.beginControlFlow( "if ( !$N )", GeneratorUtil.COMPLETED_VARIABLE_NAME );
    if ( !isProcedure )
    {
      codeBlock.addStatement( "final $T $N = null",
                              TypeName.get( returnType ).box(),
                              GeneratorUtil.RESULT_VARIABLE_NAME );
    }
    GeneratorUtil.actionCompletedSpyEvent( _componentDescriptor, _name, true, _trackedMethod, isProcedure, codeBlock );
    codeBlock.endControlFlow();

    codeBlock.endControlFlow();
    builder.addCode( codeBlock.build() );

    return builder.build();
  }
}
