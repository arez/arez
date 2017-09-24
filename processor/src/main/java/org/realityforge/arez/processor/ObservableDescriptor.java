package org.realityforge.arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * The class that represents the parsed state of Observable properties on a @Container annotated class.
 */
final class ObservableDescriptor
{
  @Nonnull
  private final ContainerDescriptor _containerDescriptor;
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _getter;
  @Nullable
  private ExecutableElement _setter;

  ObservableDescriptor( @Nonnull final ContainerDescriptor containerDescriptor, @Nonnull final String name )
  {
    _containerDescriptor = Objects.requireNonNull( containerDescriptor );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  boolean hasGetter()
  {
    return null != _getter;
  }

  @Nonnull
  ExecutableElement getGetter()
    throws ArezProcessorException
  {
    if ( null == _getter )
    {
      throw new ArezProcessorException( String.format( "ObservableDescriptor.getGetter() invoked for observable " +
                                                       "named '%s' on container named '%s' before getter has " +
                                                       "been set", getName(), _containerDescriptor.getName() ),
                                        Objects.requireNonNull( _setter ) );
    }
    return _getter;
  }

  void setGetter( @Nonnull final ExecutableElement getter )
  {
    _getter = Objects.requireNonNull( getter );
  }

  boolean hasSetter()
  {
    return null != _setter;
  }

  @Nonnull
  ExecutableElement getSetter()
    throws ArezProcessorException
  {
    if ( null == _setter )
    {
      throw new ArezProcessorException( String.format( "ObservableDescriptor.getSetter() invoked for observable " +
                                                       "named '%s' on container named '%s' before setter has " +
                                                       "been set", getName(), _containerDescriptor.getName() ),
                                        Objects.requireNonNull( _setter ) );
    }
    return _setter;
  }

  void setSetter( @Nonnull final ExecutableElement setter )
  {
    _setter = Objects.requireNonNull( setter );
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

  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    final FieldSpec.Builder field =
      FieldSpec.builder( GeneratorUtil.OBSERVABLE_CLASSNAME,
                         GeneratorUtil.FIELD_PREFIX + getName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( Nonnull.class );
    builder.addField( field.build() );
  }

  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    if ( _containerDescriptor.isSingleton() )
    {
      builder.addStatement( "this.$N = this.$N.createObservable( this.$N.areNamesEnabled() ? $S : null )",
                            GeneratorUtil.FIELD_PREFIX + getName(),
                            GeneratorUtil.CONTEXT_FIELD_NAME,
                            GeneratorUtil.CONTEXT_FIELD_NAME,
                            _containerDescriptor.getNamePrefix() + getName() );
    }
    else
    {
      builder.addStatement( "this.$N = this.$N.createObservable( this.$N.areNamesEnabled() ? $N() + $S : null )",
                            GeneratorUtil.FIELD_PREFIX + getName(),
                            GeneratorUtil.CONTEXT_FIELD_NAME,
                            GeneratorUtil.CONTEXT_FIELD_NAME,
                            GeneratorUtil.ID_FIELD_NAME,
                            getName() );
    }
  }

  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "$N.dispose()", GeneratorUtil.FIELD_PREFIX + getName() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildObservableGetter() );
    builder.addMethod( buildObservableSetter() );
  }

  /**
   * Generate the setter that reports that ensures that the access is reported as Observable.
   */
  @Nonnull
  private MethodSpec buildObservableSetter()
    throws ArezProcessorException
  {
    final ExecutableElement getter = getGetter();
    final ExecutableElement setter = getSetter();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( setter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( setter, builder );
    ProcessorUtil.copyExceptions( setter, builder );
    ProcessorUtil.copyTypeParameters( setter, builder );
    ProcessorUtil.copyDocumentedAnnotations( setter, builder );

    builder.addAnnotation( Override.class );

    final VariableElement element = setter.getParameters().get( 0 );
    final String paramName = element.getSimpleName().toString();
    final TypeName type = TypeName.get( element.asType() );
    final ParameterSpec.Builder param =
      ParameterSpec.builder( type, paramName, Modifier.FINAL );
    ProcessorUtil.copyDocumentedAnnotations( element, param );
    builder.addParameter( param.build() );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    final String accessor = "super." + getter.getSimpleName() + "()";
    final String mutator = "super." + setter.getSimpleName() + "($N)";
    if ( type.isPrimitive() )
    {
      codeBlock.beginControlFlow( "if ( $N != " + accessor + " )", paramName );
    }
    else
    {
      codeBlock.beginControlFlow( "if ( !$T.equals($N, " + accessor + ") )", Objects.class, paramName );
    }
    codeBlock.addStatement( mutator, paramName );
    codeBlock.addStatement( "this.$N.reportChanged()", GeneratorUtil.FIELD_PREFIX + getName() );
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
    final ExecutableElement getter = getGetter();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( getter, builder );
    ProcessorUtil.copyExceptions( getter, builder );
    ProcessorUtil.copyTypeParameters( getter, builder );
    ProcessorUtil.copyDocumentedAnnotations( getter, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( getter.getReturnType() ) );
    builder.addStatement( "this.$N.reportObserved()", GeneratorUtil.FIELD_PREFIX + getName() );
    builder.addStatement( "return super." + getter.getSimpleName() + "()" );
    return builder.build();
  }
}
