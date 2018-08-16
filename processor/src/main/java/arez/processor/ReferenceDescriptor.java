package arez.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
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
  @Nullable
  private String _inverseName;
  @Nullable
  private Multiplicity _inverseMultiplicity;

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
                  @Nonnull final String linkType,
                  @Nullable final String inverseName,
                  @Nullable final Multiplicity inverseMultiplicity )
  {
    assert null == _method;
    assert null == _methodType;
    assert null == _linkType;
    assert null == _inverseName;
    assert null == _inverseMultiplicity;
    assert ( null == inverseName && null == inverseMultiplicity ) ||
           ( null != inverseName && null != inverseMultiplicity );
    _method = Objects.requireNonNull( method );
    _methodType = Objects.requireNonNull( methodType );
    _linkType = Objects.requireNonNull( linkType );
    _inverseName = inverseName;
    _inverseMultiplicity = inverseMultiplicity;
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
  String getDelinkMethodName()
  {
    return GeneratorUtil.FRAMEWORK_PREFIX + "delink_" + _name;
  }

  @Nonnull
  String getFieldName()
  {
    return GeneratorUtil.REFERENCE_FIELD_PREFIX + _name;
  }

  boolean hasInverse()
  {
    return null != _inverseName;
  }

  @Nonnull
  String getInverseName()
  {
    assert null != _inverseName;
    return _inverseName;
  }

  @Nonnull
  Multiplicity getInverseMultiplicity()
  {
    assert null != _inverseMultiplicity;
    return _inverseMultiplicity;
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
    if ( hasInverse() )
    {
      builder.addMethod( buildDelinkMethod() );
    }
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
        block.addStatement( "$T.apiInvariant( () -> null != $N || null == $N(), () -> \"Nullable reference method " +
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

      if ( null != _observable )
      {
        if ( _observable.canReadOutsideTransaction() )
        {
          builder.addStatement( "this.$N.reportObservedIfTrackingTransactionActive()", _observable.getFieldName() );
        }
        else
        {
          builder.addStatement( "this.$N.reportObserved()", _observable.getFieldName() );
        }
      }
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

    if ( "EAGER".equals( getLinkType() ) )
    {
      /*
       * Linking under eager should always proceed and does not need a null check
       * as the link method only called when a link is required.
       */
      builder.addStatement( "final $T id = this.$N()", getIdMethod().getReturnType(), getIdMethod().getSimpleName() );
      if ( isNullable )
      {
        final CodeBlock.Builder nestedBlock = CodeBlock.builder();
        nestedBlock.beginControlFlow( "if ( null != id )" );
        buildLookup( nestedBlock );
        nestedBlock.nextControlFlow( "else" );
        nestedBlock.addStatement( "this.$N = null", getFieldName() );
        nestedBlock.endControlFlow();
        builder.addCode( nestedBlock.build() );
      }
      else
      {
        buildLookup( builder );
      }
    }
    else
    {
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
    }
    return builder.build();
  }

  private void buildLookup( @Nonnull final MethodSpec.Builder builder )
  {
    builder.addStatement( "this.$N = this.$N().findById( $T.class, id )",
                          getFieldName(),
                          GeneratorUtil.LOCATOR_METHOD_NAME,
                          getMethod().getReturnType() );
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", GeneratorUtil.AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != $N, () -> \"Reference method named '$N' " +
                        "invoked on component named '\" + $N() + \"' is unable to resolve entity of type $N " +
                        "and id = \" + $N() )",
                        GeneratorUtil.GUARDS_CLASSNAME,
                        getFieldName(),
                        getMethod().getSimpleName(),
                        _componentDescriptor.getComponentNameMethodName(),
                        getMethod().getReturnType().toString(),
                        getIdMethod().getSimpleName() );
    block.endControlFlow();
    builder.addCode( block.build() );
    if ( hasInverse() )
    {
      assert null != _inverseName;
      final String linkMethodName =
        _inverseMultiplicity == Multiplicity.MANY ?
        GeneratorUtil.getInverseAddMethodName( _inverseName ) :
        _inverseMultiplicity == Multiplicity.ONE ?
        GeneratorUtil.getInverseSetMethodName( _inverseName ) :
        GeneratorUtil.getInverseZSetMethodName( _inverseName );
      builder.addStatement( "( ($T) this.$N ).$N( this )", getArezClassName(), getFieldName(), linkMethodName );
    }
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
                        "invoked on component named '\" + $N() + \"' is unable to resolve entity of type $N " +
                        "and id = \" + $N() )",
                        GeneratorUtil.GUARDS_CLASSNAME,
                        getFieldName(),
                        getMethod().getSimpleName(),
                        _componentDescriptor.getComponentNameMethodName(),
                        getMethod().getReturnType().toString(),
                        getIdMethod().getSimpleName() );
    block.endControlFlow();
    builder.add( block.build() );
    if ( hasInverse() )
    {
      assert null != _inverseName;
      final String linkMethodName =
        _inverseMultiplicity == Multiplicity.MANY ?
        GeneratorUtil.getInverseAddMethodName( _inverseName ) :
        _inverseMultiplicity == Multiplicity.ONE ?
        GeneratorUtil.getInverseSetMethodName( _inverseName ) :
        GeneratorUtil.getInverseZSetMethodName( _inverseName );
      assert null != _method;
      builder.addStatement( "( ($T) this.$N ).$N( this )", getArezClassName(), getFieldName(), linkMethodName );
    }
  }

  void buildDisposer( @Nonnull final MethodSpec.Builder builder )
  {
    if ( hasInverse() )
    {
      builder.addStatement( "this.$N()", getDelinkMethodName() );
    }
  }

  @Nonnull
  private MethodSpec buildDelinkMethod()
    throws ArezProcessorException
  {
    assert null != _method;
    assert null != _inverseName;
    final String methodName = getDelinkMethodName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    builder.addModifiers( Modifier.PRIVATE );

    final CodeBlock.Builder nestedBlock = CodeBlock.builder();
    nestedBlock.beginControlFlow( "if ( null != $N )", getFieldName() );
    assert null != _inverseName;
    assert null != _inverseName;
    final String delinkMethodName =
      Multiplicity.MANY == _inverseMultiplicity ?
      GeneratorUtil.getInverseRemoveMethodName( _inverseName ) :
      Multiplicity.ONE == _inverseMultiplicity ?
      GeneratorUtil.getInverseUnsetMethodName( _inverseName ) :
      GeneratorUtil.getInverseZUnsetMethodName( _inverseName );
    nestedBlock.addStatement( "( ($T) this.$N ).$N( this )", getArezClassName(), getFieldName(), delinkMethodName );
    nestedBlock.addStatement( "this.$N = null", getFieldName() );
    nestedBlock.endControlFlow();
    builder.addCode( nestedBlock.build() );

    return builder.build();
  }

  @Nonnull
  private ClassName getArezClassName()
  {
    assert null != _method;
    final ClassName other = (ClassName) TypeName.get( _method.getReturnType() );
    final StringBuilder sb = new StringBuilder();
    final String packageName = other.packageName();
    if ( null != packageName )
    {
      sb.append( packageName );
      sb.append( "." );
    }

    final List<String> simpleNames = other.simpleNames();
    final int end = simpleNames.size() - 1;
    for ( int i = 0; i < end; i++ )
    {
      sb.append( simpleNames.get( i ) );
      sb.append( "_" );
    }
    sb.append( "Arez_" );
    sb.append( simpleNames.get( end ) );
    return ClassName.bestGuess( sb.toString() );
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
