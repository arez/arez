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
import javax.lang.model.type.ExecutableType;

/**
 * The class that represents the parsed state of Observable properties on a @ArezComponent annotated class.
 */
final class ObservableDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _getter;
  @Nullable
  private ExecutableType _getterType;
  @Nullable
  private ExecutableElement _setter;
  @Nullable
  private ExecutableType _setterType;

  ObservableDescriptor( @Nonnull final ComponentDescriptor componentDescriptor, @Nonnull final String name )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
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
    assert null != _getter;
    return _getter;
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
    throws ArezProcessorException
  {
    assert null != _setter;
    return _setter;
  }

  void setSetter( @Nonnull final ExecutableElement setter, @Nonnull final ExecutableType methodType )
  {
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
    if ( _componentDescriptor.isSingleton() )
    {
      builder.addStatement( "this.$N = this.$N.createObservable( this.$N.areNamesEnabled() ? $S : null )",
                            GeneratorUtil.FIELD_PREFIX + getName(),
                            GeneratorUtil.CONTEXT_FIELD_NAME,
                            GeneratorUtil.CONTEXT_FIELD_NAME,
                            _componentDescriptor.getNamePrefix() + getName() );
    }
    else
    {
      builder.addStatement( "this.$N = this.$N.createObservable( this.$N.areNamesEnabled() ? $N() + $S : null )",
                            GeneratorUtil.FIELD_PREFIX + getName(),
                            GeneratorUtil.CONTEXT_FIELD_NAME,
                            GeneratorUtil.CONTEXT_FIELD_NAME,
                            _componentDescriptor.getComponentNameMethodName(),
                            "." + getName() );
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
    assert null != _setter;
    assert null != _setterType;
    assert null != _getter;
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _setter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _setter, builder );
    ProcessorUtil.copyExceptions( _setterType, builder );
    ProcessorUtil.copyTypeParameters( _setterType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _setter, builder );

    builder.addAnnotation( Override.class );

    final VariableElement element = _setter.getParameters().get( 0 );
    final String paramName = element.getSimpleName().toString();
    final TypeName type = TypeName.get( element.asType() );
    final ParameterSpec.Builder param =
      ParameterSpec.builder( type, paramName, Modifier.FINAL );
    ProcessorUtil.copyDocumentedAnnotations( element, param );
    builder.addParameter( param.build() );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    final String accessor = "super." + _getter.getSimpleName() + "()";
    final String mutator = "super." + _setter.getSimpleName() + "($N)";
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
    assert null != _getter;
    assert null != _getterType;
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( _getter.getSimpleName().toString() );
    ProcessorUtil.copyAccessModifiers( _getter, builder );
    ProcessorUtil.copyExceptions( _getterType, builder );
    ProcessorUtil.copyTypeParameters( _getterType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _getter, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _getter.getReturnType() ) );
    builder.addStatement( "this.$N.reportObserved()", GeneratorUtil.FIELD_PREFIX + getName() );
    builder.addStatement( "return super." + _getter.getSimpleName() + "()" );
    return builder.build();
  }
}
