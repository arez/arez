package org.realityforge.arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of @Autorun methods on a @Container annotated class.
 */
final class AutorunDescriptor
{
  @Nonnull
  private final ContainerDescriptor _containerDescriptor;
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nonnull
  private final ExecutableElement _autorun;
  @Nonnull
  private final ExecutableType _autorunType;

  AutorunDescriptor( @Nonnull final ContainerDescriptor containerDescriptor,
                     @Nonnull final String name,
                     final boolean mutation,
                     @Nonnull final ExecutableElement autorun,
                     @Nonnull final ExecutableType autorunType)
  {
    _containerDescriptor = Objects.requireNonNull( containerDescriptor );
    _name = Objects.requireNonNull( name );
    _mutation = mutation;
    _autorun = Objects.requireNonNull( autorun );
    _autorunType = Objects.requireNonNull( autorunType );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  ExecutableElement getAutorun()
  {
    return _autorun;
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
   * Setup initial state of autorun in constructor.
   */
  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = this.$N.autorun( this.$N.areNamesEnabled() ? " );
    parameters.add( GeneratorUtil.FIELD_PREFIX + getName() );
    parameters.add( GeneratorUtil.CONTEXT_FIELD_NAME );
    parameters.add( GeneratorUtil.CONTEXT_FIELD_NAME );
    if ( _containerDescriptor.isSingleton() )
    {
      sb.append( "$S" );
      parameters.add( _containerDescriptor.getNamePrefix() + getName() );
    }
    else
    {
      sb.append( "$N() + $S" );
      parameters.add( _containerDescriptor.getContainerNameMethodName() );
      parameters.add( "." + getName() );
    }
    sb.append( " : null, " );
    sb.append( _mutation );
    sb.append( ", () -> super.$N(), false )" );
    parameters.add( getAutorun().getSimpleName().toString() );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }
  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "$N.dispose()", GeneratorUtil.FIELD_PREFIX + getName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildAutorun() );
  }

  /**
   * Generate the autorun wrapper.
   * This is wrapped in case the user ever wants to explicitly call method
   */
  @Nonnull
  private MethodSpec buildAutorun()
    throws ArezProcessorException
  {
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _autorun.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _autorun, builder );
    ProcessorUtil.copyExceptions( _autorunType, builder );
    ProcessorUtil.copyTypeParameters( _autorunType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _autorun, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _autorun.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final StringBuilder statement = new StringBuilder();
    final ArrayList<Object> parameterNames = new ArrayList<>();

    statement.append( "this.$N." );
    parameterNames.add( GeneratorUtil.CONTEXT_FIELD_NAME );

    statement.append( "safeProcedure(this.$N.areNamesEnabled() ? " );
    parameterNames.add( GeneratorUtil.CONTEXT_FIELD_NAME );

    if ( _containerDescriptor.isSingleton() )
    {
      statement.append( "$S" );
      parameterNames.add( _containerDescriptor.getNamePrefix() + getName() );
    }
    else
    {
      statement.append( "$N() + $S" );
      parameterNames.add( _containerDescriptor.getContainerNameMethodName() );
      parameterNames.add( "." + getName() );
    }
    statement.append( " : null, " );
    statement.append( _mutation );
    statement.append( ", () -> super." );
    statement.append( _autorun.getSimpleName() );
    statement.append( "() )" );

    builder.addStatement( statement.toString(), parameterNames.toArray() );

    return builder.build();
  }
}
