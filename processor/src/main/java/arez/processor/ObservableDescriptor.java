package arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of Observable properties on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class ObservableDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  private boolean _expectSetter;
  @Nullable
  private ExecutableElement _getter;
  @Nullable
  private ExecutableType _getterType;
  @Nullable
  private ExecutableElement _setter;
  @Nullable
  private ExecutableType _setterType;
  @Nullable
  private ExecutableElement _refMethod;
  @Nullable
  private ExecutableType _refMethodType;

  ObservableDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                        @Nonnull final String name )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    setExpectSetter( true );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  void setExpectSetter( final boolean expectSetter )
  {
    _expectSetter = expectSetter;
  }

  boolean expectSetter()
  {
    return _expectSetter;
  }

  boolean hasRefMethod()
  {
    return null != _refMethod;
  }

  @Nonnull
  ExecutableElement getRefMethod()
    throws ArezProcessorException
  {
    assert null != _refMethod;
    return _refMethod;
  }

  void setRefMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    _refMethod = Objects.requireNonNull( method );
    _refMethodType = Objects.requireNonNull( methodType );
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

  @Nonnull
  ExecutableType getGetterType()
  {
    assert null != _getterType;
    return _getterType;
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

  @Nonnull
  ExecutableType getSetterType()
  {
    assert null != _setterType;
    return _setterType;
  }

  void setSetter( @Nonnull final ExecutableElement setter, @Nonnull final ExecutableType methodType )
  {
    assert _expectSetter;
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
    assert null != _getterType;
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( GeneratorUtil.OBSERVABLE_CLASSNAME, TypeName.get( _getterType.getReturnType() ).box() );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName,
                         GeneratorUtil.FIELD_PREFIX + getName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    builder.addField( field.build() );
  }

  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N().createObservable( " +
               "$T.areNativeComponentsEnabled() ? this.$N : null, " +
               "$T.areNamesEnabled() ? $N() + $S : null, " +
               "$T.arePropertyIntrospectorsEnabled() ? () -> super.$N() : null" );
    parameters.add( GeneratorUtil.FIELD_PREFIX + getName() );
    parameters.add( _componentDescriptor.getContextMethodName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( getGetter().getSimpleName().toString() );

    if ( hasSetter() )
    {
      //setter
      sb.append( ", $T.arePropertyIntrospectorsEnabled() ? v -> super.$N( v ) : null" );
      parameters.add( GeneratorUtil.AREZ_CLASSNAME );
      parameters.add( getSetter().getSimpleName().toString() );
    }
    else
    {
      sb.append( ", null" );
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
    builder.addMethod( buildObservableGetter() );
    if ( expectSetter() )
    {
      builder.addMethod( buildObservableSetter() );
    }
    if ( hasRefMethod() )
    {
      builder.addMethod( buildObservableRefMethod() );
    }
  }

  /**
   * Generate the accessor for ref method.
   */
  @Nonnull
  private MethodSpec buildObservableRefMethod()
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

    final TypeMirror parameterType = _setterType.getParameterTypes().get( 0 );
    final VariableElement element = _setter.getParameters().get( 0 );
    final String paramName = element.getSimpleName().toString();
    final TypeName type = TypeName.get( parameterType );
    final ParameterSpec.Builder param =
      ParameterSpec.builder( type, paramName, Modifier.FINAL );
    ProcessorUtil.copyDocumentedAnnotations( element, param );
    builder.addParameter( param.build() );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

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
    builder.returns( TypeName.get( _getterType.getReturnType() ) );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder );

    builder.addStatement( "this.$N.reportObserved()", GeneratorUtil.FIELD_PREFIX + getName() );
    builder.addStatement( "return super." + _getter.getSimpleName() + "()" );
    return builder.build();
  }

  void validate()
  {
    if ( !expectSetter() && !hasRefMethod() )
    {
      throw new ArezProcessorException( "@Observable target defines expectSetter = false but there is no ref " +
                                        "method for observable and thus never possible to report it as changed " +
                                        "and thus should not be observable.", getGetter() );
    }

    if ( null != _refMethodType )
    {
      final TypeName typeName = TypeName.get( _refMethodType.getReturnType() );
      if ( typeName instanceof ParameterizedTypeName )
      {
        final ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
        final TypeName expectedType = parameterizedTypeName.typeArguments.get( 0 );
        assert null != _getterType;
        final TypeName actual = TypeName.get( _getterType.getReturnType() );
        if ( !actual.box().toString().equals( expectedType.toString() ) )
        {
          assert null != _refMethod;
          throw new ArezProcessorException( "@ObservableRef target has a type parameter of " + expectedType +
                                            " but @Observable method returns type of " + actual, _refMethod );
        }
      }
    }
  }
}
