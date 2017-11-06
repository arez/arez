package org.realityforge.arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of @Autorun methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class AutorunDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nonnull
  private final ExecutableElement _autorun;
  @Nonnull
  private final ExecutableType _autorunType;
  @Nullable
  private ExecutableElement _refMethod;
  @Nullable
  private ExecutableType _refMethodType;

  AutorunDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                     @Nonnull final String name,
                     final boolean mutation,
                     @Nonnull final ExecutableElement autorun,
                     @Nonnull final ExecutableType autorunType )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
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

  void setRefMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    _refMethod = Objects.requireNonNull( method );
    _refMethodType = Objects.requireNonNull( methodType );
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
    sb.append( "this.$N = this.$N.autorun( $T.areNamesEnabled() ? " );
    parameters.add( GeneratorUtil.FIELD_PREFIX + getName() );
    parameters.add( GeneratorUtil.CONTEXT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    sb.append( "$N() + $S" );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    sb.append( " : null, " );
    sb.append( _mutation );
    sb.append( ", () -> super.$N(), false )" );
    parameters.add( getAutorun().getSimpleName().toString() );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", GeneratorUtil.FIELD_PREFIX + getName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildAutorun() );
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
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _refMethod.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _refMethod, builder );
    ProcessorUtil.copyTypeParameters( _refMethodType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _refMethod, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _refMethodType.getReturnType() ) );

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

    builder.addStatement( "return $N", GeneratorUtil.FIELD_PREFIX + getName() );

    return builder.build();
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

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

    statement.append( "this.$N." );
    parameterNames.add( GeneratorUtil.CONTEXT_FIELD_NAME );

    statement.append( "safeAction( $T.areNamesEnabled() ? " );
    parameterNames.add( GeneratorUtil.AREZ_CLASSNAME );

    statement.append( "$N() + $S" );
    parameterNames.add( _componentDescriptor.getComponentNameMethodName() );
    parameterNames.add( "." + getName() );
    statement.append( " : null, " );
    statement.append( _mutation );
    statement.append( ", () -> super." );
    statement.append( _autorun.getSimpleName() );
    statement.append( "() )" );

    builder.addStatement( statement.toString(), parameterNames.toArray() );

    return builder.build();
  }
}
