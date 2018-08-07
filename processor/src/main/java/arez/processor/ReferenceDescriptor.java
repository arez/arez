package arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;

/**
 * Declaration of a reference.
 */
final class ReferenceDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _method;
  @Nullable
  private ExecutableType _methodType;
  @Nullable
  private String _linkType;
  @Nullable
  private ObservableDescriptor _observable;
  @Nullable
  private ExecutableElement _idMethod;
  @Nullable
  private ExecutableType _idMethodType;

  ReferenceDescriptor( @Nonnull final ComponentDescriptor componentDescriptor, @Nonnull final String name )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
  }

  void setIdMethod( @Nonnull final ExecutableElement method, @Nonnull final ExecutableType methodType )
  {
    assert null == _idMethod;
    assert null == _idMethodType;
    _idMethod = Objects.requireNonNull( method );
    _idMethodType = Objects.requireNonNull( methodType );
  }

  void setObservable( @Nonnull final ObservableDescriptor observable )
  {
    setIdMethod( observable.getGetter(), observable.getGetterType() );
    assert null == _observable;
    _observable = observable;
    _observable.setReferenceDescriptor( this );
  }

  void setMethod( @Nonnull final ExecutableElement method,
                  @Nonnull final ExecutableType methodType,
                  @Nonnull final String linkType )
  {
    assert null == _method;
    assert null == _methodType;
    _method = Objects.requireNonNull( method );
    _methodType = Objects.requireNonNull( methodType );
    _linkType = Objects.requireNonNull( linkType );
  }

  @Nonnull
  String getLinkType()
  {
    assert null != _linkType;
    return _linkType;
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    assert null != _method;
    return _method;
  }

  @Nonnull
  private ExecutableElement getIdMethod()
  {
    assert null != _idMethod;
    return _idMethod;
  }

  @Nonnull
  String getLinkMethodName()
  {
    return GeneratorUtil.FRAMEWORK_PREFIX + "link_" + _name;
  }

  @Nonnull
  String getFieldName()
  {
    return GeneratorUtil.REFERENCE_FIELD_PREFIX + _name;
  }

  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    assert null != _method;
    final FieldSpec.Builder field =
      FieldSpec.builder( TypeName.get( _method.getReturnType() ),
                         getFieldName(),
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NULLABLE_CLASSNAME );
    builder.addField( field.build() );
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildReferenceMethod() );
    builder.addMethod( buildLinkMethod() );
  }

  @Nonnull
  private MethodSpec buildReferenceMethod()
    throws ArezProcessorException
  {
    assert null != _method;
    assert null != _methodType;
    assert null != _idMethod;
    assert null != _idMethodType;
    assert null != _linkType;

    final String methodName = _method.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _method, builder );
    ProcessorUtil.copyTypeParameters( _methodType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _method, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _method.getReturnType() ) );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );

    final boolean isNullable =
      !getIdMethod().getReturnType().getKind().isPrimitive() &&
      null == ProcessorUtil.findAnnotationByType( getIdMethod(), Constants.NONNULL_ANNOTATION_CLASSNAME );

    if ( !"LAZY".equals( _linkType ) )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", GeneratorUtil.AREZ_CLASSNAME );
      if ( isNullable )
      {
        block.addStatement( "$T.apiInvariant( () -> null != $N && null != $N(), () -> \"Nullable reference method " +
                            "named '$N' invoked on component named '\" + $N() + \"' and reference has not been " +
                            "resolved yet is not lazy. Id = \" + $N() )",
                            GeneratorUtil.GUARDS_CLASSNAME,
                            getFieldName(),
                            _idMethod.getSimpleName(),
                            _method.getSimpleName(),
                            _componentDescriptor.getComponentNameMethodName(),
                            _idMethod.getSimpleName() );
      }
      else
      {
        block.addStatement( "$T.apiInvariant( () -> null != $N, () -> \"Nonnull reference method named '$N' " +
                            "invoked on component named '\" + $N() + \"' but reference has not been resolved yet " +
                            "is not lazy. Id = \" + $N() )",
                            GeneratorUtil.GUARDS_CLASSNAME,
                            getFieldName(),
                            _method.getSimpleName(),
                            _componentDescriptor.getComponentNameMethodName(),
                            _idMethod.getSimpleName() );
      }
      block.endControlFlow();

      builder.addCode( block.build() );
    }
    else
    {
      if ( null == _observable )
      {
        builder.addStatement( "this.$N()", getLinkMethodName() );
      }
      else
      {
        final CodeBlock.Builder block = CodeBlock.builder();
        block.beginControlFlow( "if ( null == this.$N )", getFieldName() );
        block.addStatement( "this.$N()", getLinkMethodName() );
        block.nextControlFlow( "else" );
        if ( _observable.canReadOutsideTransaction() )
        {
          block.addStatement( "this.$N.reportObservedIfTrackingTransactionActive()", _observable.getFieldName() );
        }
        else
        {
          block.addStatement( "this.$N.reportObserved()", _observable.getFieldName() );
        }
        block.endControlFlow();
        builder.addCode( block.build() );
      }
    }

    builder.addStatement( "return this.$N", getFieldName() );
    return builder.build();
  }

  @Nonnull
  private MethodSpec buildLinkMethod()
    throws ArezProcessorException
  {
    final String methodName = getLinkMethodName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    builder.addModifiers( Modifier.PRIVATE );
    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );

    final boolean isNullable =
      !getIdMethod().getReturnType().getKind().isPrimitive() &&
      null == ProcessorUtil.findAnnotationByType( getIdMethod(), Constants.NONNULL_ANNOTATION_CLASSNAME );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( null == this.$N )", getFieldName() );
    block.addStatement( "final $T id = this.$N()", getIdMethod().getReturnType(), getIdMethod().getSimpleName() );
    if ( isNullable )
    {
      final CodeBlock.Builder nestedBlock = CodeBlock.builder();
      nestedBlock.beginControlFlow( "if ( null != id )" );
      buildLookup( nestedBlock );
      nestedBlock.endControlFlow();
      block.add( nestedBlock.build() );
    }
    else
    {
      buildLookup( block );
    }
    block.endControlFlow();
    builder.addCode( block.build() );

    return builder.build();
  }

  private void buildLookup( @Nonnull final CodeBlock.Builder builder )
  {
    builder.addStatement( "this.$N = this.$N().findById( $T.class, id )",
                          getFieldName(),
                          GeneratorUtil.LOCATOR_METHOD_NAME,
                          getMethod().getReturnType() );
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", GeneratorUtil.AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != $N, () -> \"Reference method named '$N' " +
                        "invoked on component named '\" + $N() + \"' missing related entity. Id = \" + $N() )",
                        GeneratorUtil.GUARDS_CLASSNAME,
                        getFieldName(),
                        getMethod().getSimpleName(),
                        _componentDescriptor.getComponentNameMethodName(),
                        getIdMethod().getSimpleName() );
    block.endControlFlow();
    builder.add( block.build() );
  }

  void validate()
    throws ArezProcessorException
  {
    if ( null == _idMethod )
    {
      assert null != _method;
      throw new ArezProcessorException( "@Reference exists but there is no corresponding @ReferenceId", _method );
    }
    else if ( null == _method )
    {
      throw new ArezProcessorException( "@ReferenceId exists but there is no corresponding @Reference", _idMethod );
    }
    else if ( null != _observable && !_observable.hasSetter() )
    {
      throw new ArezProcessorException( "@ReferenceId added to @Observable method but expectSetter = false on " +
                                        "property which is not compatible with @ReferenceId", _idMethod );
    }
  }
}
