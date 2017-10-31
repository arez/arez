package org.realityforge.arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnActivate;
import org.realityforge.arez.annotations.OnDeactivate;
import org.realityforge.arez.annotations.OnDispose;
import org.realityforge.arez.annotations.OnStale;

/**
 * The class that represents the parsed state of @Computed methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class ComputedDescriptor
{
  static final Pattern ON_ACTIVATE_PATTERN = Pattern.compile( "^on([A-Z].*)Activate$" );
  static final Pattern ON_DEACTIVATE_PATTERN = Pattern.compile( "^on([A-Z].*)Deactivate$" );
  static final Pattern ON_STALE_PATTERN = Pattern.compile( "^on([A-Z].*)Stale$" );
  static final Pattern ON_DISPOSE_PATTERN = Pattern.compile( "^on([A-Z].*)Dispose$" );

  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _computed;
  @Nullable
  private ExecutableType _computedType;
  @Nullable
  private ExecutableElement _onActivate;
  @Nullable
  private ExecutableElement _onDeactivate;
  @Nullable
  private ExecutableElement _onStale;
  @Nullable
  private ExecutableElement _onDispose;

  ComputedDescriptor( @Nonnull final ComponentDescriptor componentDescriptor, @Nonnull final String name )
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
  ExecutableElement getComputed()
  {
    return Objects.requireNonNull( _computed );
  }

  void setComputed( @Nonnull final ExecutableElement computed, @Nonnull final ExecutableType computedType )
    throws ArezProcessorException
  {
    MethodChecks.mustBeOverridable( Computed.class, computed );
    MethodChecks.mustNotHaveAnyParameters( Computed.class, computed );
    MethodChecks.mustReturnAValue( Computed.class, computed );
    MethodChecks.mustNotThrowAnyExceptions( Computed.class, computed );

    if ( null != _computed )
    {
      throw new ArezProcessorException( "Method annotated with @Computed specified name " + getName() +
                                        " that duplicates computed defined by method " +
                                        _computed.getSimpleName(), computed );
    }
    else
    {
      _computed = Objects.requireNonNull( computed );
      _computedType = Objects.requireNonNull( computedType );
    }
  }

  void setOnActivate( @Nonnull final ExecutableElement onActivate )
    throws ArezProcessorException
  {
    MethodChecks.mustBeLifecycleHook( OnActivate.class, onActivate );

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
    MethodChecks.mustBeLifecycleHook( OnDeactivate.class, onDeactivate );
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
    MethodChecks.mustBeLifecycleHook( OnStale.class, onStale );
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

  void setOnDispose( @Nonnull final ExecutableElement onDispose )
    throws ArezProcessorException
  {
    MethodChecks.mustBeLifecycleHook( OnDispose.class, onDispose );

    if ( null != _onDispose )
    {
      throw new ArezProcessorException( "@OnDispose target duplicates existing method named " +
                                        _onDispose.getSimpleName(), onDispose );
    }
    else
    {
      _onDispose = Objects.requireNonNull( onDispose );
    }
  }

  void validate()
    throws ArezProcessorException
  {
    if ( null == _computed )
    {
      if ( null != _onActivate )
      {
        throw new ArezProcessorException( "@OnActivate exists but there is no corresponding @Computed",
                                          _onActivate );
      }
      else if ( null != _onDeactivate )
      {
        throw new ArezProcessorException( "@OnDeactivate exists but there is no corresponding @Computed",
                                          _onDeactivate );
      }
      else if ( null != _onDispose )
      {
        throw new ArezProcessorException( "@OnDispose exists but there is no corresponding @Computed",
                                          _onDispose );
      }
      else
      {
        final ExecutableElement onStale = _onStale;
        assert null != onStale;
        throw new ArezProcessorException( "@OnStale exists but there is no corresponding @Computed", onStale );
      }
    }
  }

  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    assert null != _computed;
    assert null != _computedType;
    final TypeName parameterType =
      _computed.getTypeParameters().isEmpty() ? TypeName.get( _computedType.getReturnType() ).box() :
      WildcardTypeName.subtypeOf( TypeName.OBJECT );
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( GeneratorUtil.COMPUTED_VALUE_CLASSNAME, parameterType );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName,
                         GeneratorUtil.FIELD_PREFIX + getName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( Nonnull.class );
    builder.addField( field.build() );
  }

  void buildInitializer( @Nonnull MethodSpec.Builder builder )
  {
    assert null != _computed;
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = this.$N.createComputedValue( $T.areNamesEnabled() ? " );
    parameters.add( GeneratorUtil.FIELD_PREFIX + getName() );
    parameters.add( GeneratorUtil.CONTEXT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
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
    sb.append( " : null, super::$N, $T::equals, " );
    parameters.add( _computed.getSimpleName().toString() );
    parameters.add( Objects.class );

    if ( null != _onActivate )
    {
      sb.append( "this::$N" );
      parameters.add( _onActivate.getSimpleName().toString() );
    }
    else
    {
      sb.append( "null" );
    }
    sb.append( ", " );

    if ( null != _onDeactivate )
    {
      sb.append( "this::$N" );
      parameters.add( _onDeactivate.getSimpleName().toString() );
    }
    else
    {
      sb.append( "null" );
    }
    sb.append( ", " );

    if ( null != _onStale )
    {
      sb.append( "this::$N" );
      parameters.add( _onStale.getSimpleName().toString() );
    }
    else
    {
      sb.append( "null" );
    }
    sb.append( ", " );

    if ( null != _onDispose )
    {
      sb.append( "this::$N" );
      parameters.add( _onDispose.getSimpleName().toString() );
    }
    else
    {
      sb.append( "null" );
    }

    sb.append( " )" );
    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", GeneratorUtil.FIELD_PREFIX + getName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildComputed() );
  }

  /**
   * Generate the wrapper around Computed method.
   */
  @Nonnull
  private MethodSpec buildComputed()
    throws ArezProcessorException
  {
    assert null != _computed;
    assert null != _computedType;
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _computed.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _computed, builder );
    ProcessorUtil.copyExceptions( _computedType, builder );
    ProcessorUtil.copyTypeParameters( _computedType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _computed, builder );
    builder.addAnnotation( Override.class );
    final TypeName returnType = TypeName.get( _computedType.getReturnType() );
    builder.returns( returnType );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

    if ( _computed.getTypeParameters().isEmpty() )
    {
      builder.addStatement( "return this.$N.get()", GeneratorUtil.FIELD_PREFIX + getName() );
    }
    else
    {
      builder.addStatement( "return ($T) this.$N.get()",
                            returnType.box(),
                            GeneratorUtil.FIELD_PREFIX + getName() );
    }

    return builder.build();
  }
}
