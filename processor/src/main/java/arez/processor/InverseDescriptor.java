package arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import static arez.processor.ProcessorUtil.*;

/**
 * Declaration of an inverse.
 */
@SuppressWarnings( "Duplicates" )
final class InverseDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final ObservableDescriptor _observable;
  @Nonnull
  private final String _referenceName;
  @Nonnull
  private final Multiplicity _multiplicity;
  @Nonnull
  private final TypeElement _targetType;
  @Nonnull
  private final String _otherName;

  InverseDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                     @Nonnull final ObservableDescriptor observable,
                     @Nonnull final String referenceName,
                     @Nonnull final Multiplicity multiplicity,
                     @Nonnull final TypeElement targetType )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _observable = Objects.requireNonNull( observable );
    _referenceName = Objects.requireNonNull( referenceName );
    _multiplicity = Objects.requireNonNull( multiplicity );
    _targetType = Objects.requireNonNull( targetType );
    _observable.setInverseDescriptor( this );
    final String targetName = _targetType.getSimpleName().toString();
    _otherName = ProcessorUtil.firstCharacterToLowerCase( targetName );
  }

  @Nonnull
  ObservableDescriptor getObservable()
  {
    return _observable;
  }

  @Nonnull
  String getReferenceName()
  {
    return _referenceName;
  }

  @Nonnull
  Multiplicity getMultiplicity()
  {
    return _multiplicity;
  }

  @Nonnull
  TypeElement getTargetType()
  {
    return _targetType;
  }

  void validate()
  {
    if ( _observable.requireInitializer() )
    {
      throw new ArezProcessorException( "@Inverse target also specifies @Observable(initializer=ENABLE) but " +
                                        "it is not valid to define an initializer for an inverse.",
                                        _observable.getGetter() );
    }
  }

  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    if ( Multiplicity.MANY == _multiplicity )
    {
      final ParameterizedTypeName typeName =
        (ParameterizedTypeName) TypeName.get( _observable.getGetter().getReturnType() );
      final boolean isList = List.class.getName().equals( typeName.rawType.toString() );
      builder.addStatement( "this.$N = new $T<>()",
                            _observable.getDataFieldName(),
                            isList ? ArrayList.class : HashSet.class );
      builder.addStatement( "this.$N = null", _observable.getCollectionCacheDataFieldName() );
    }

  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    if ( Multiplicity.MANY == _multiplicity )
    {
      builder.addMethod( buildAddMethod() );
      builder.addMethod( buildRemoveMethod() );
    }
    else
    {
      builder.addMethod( buildSetMethod() );
      builder.addMethod( buildUnsetMethod() );
    }
  }

  @Nonnull
  private MethodSpec buildAddMethod()
    throws ArezProcessorException
  {
    final String methodName = Generator.getInverseAddMethodName( _observable.getName() );
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    if ( isReferenceInDifferentPackage() )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    final ParameterSpec parameter =
      ParameterSpec.builder( TypeName.get( _targetType.asType() ), _otherName, Modifier.FINAL )
        .addAnnotation( Generator.NONNULL_CLASSNAME )
        .build();
    builder.addParameter( parameter );
    Generator.generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "this.$N.preReportChanged()", _observable.getFieldName() );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckInvariants() )", Generator.AREZ_CLASSNAME );
    block.addStatement( "$T.invariant( () -> !this.$N.contains( $N ), " +
                        "() -> \"Attempted to add reference '$N' to inverse '$N' " +
                        "but inverse already contained element. Inverse = \" + $N )",
                        Generator.GUARDS_CLASSNAME,
                        _observable.getDataFieldName(),
                        _otherName,
                        _otherName,
                        _observable.getName(),
                        _observable.getFieldName() );
    block.endControlFlow();
    builder.addCode( block.build() );

    builder.addStatement( "this.$N.add( $N )", _observable.getDataFieldName(), _otherName );
    final CodeBlock.Builder clearCacheBlock = CodeBlock.builder();
    clearCacheBlock.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )",
                                      Generator.AREZ_CLASSNAME );
    clearCacheBlock.addStatement( "this.$N = null", _observable.getCollectionCacheDataFieldName() );
    clearCacheBlock.endControlFlow();
    builder.addCode( clearCacheBlock.build() );
    builder.addStatement( "this.$N.reportChanged()", _observable.getFieldName() );

    return builder.build();
  }

  @Nonnull
  private MethodSpec buildRemoveMethod()
    throws ArezProcessorException
  {
    final String methodName = Generator.getInverseRemoveMethodName( _observable.getName() );
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    if ( isReferenceInDifferentPackage() )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    final ParameterSpec parameter =
      ParameterSpec.builder( TypeName.get( _targetType.asType() ), _otherName, Modifier.FINAL )
        .addAnnotation( Generator.NONNULL_CLASSNAME )
        .build();
    builder.addParameter( parameter );
    Generator.generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "this.$N.preReportChanged()", _observable.getFieldName() );
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckInvariants() )", Generator.AREZ_CLASSNAME );
    block.addStatement( "$T.invariant( () -> this.$N.contains( $N ), " +
                        "() -> \"Attempted to remove reference '$N' from inverse '$N' " +
                        "but inverse does not contain element. Inverse = \" + $N )",
                        Generator.GUARDS_CLASSNAME,
                        _observable.getDataFieldName(),
                        _otherName,
                        _otherName,
                        _observable.getName(),
                        _observable.getFieldName() );
    block.endControlFlow();
    builder.addCode( block.build() );

    builder.addStatement( "this.$N.remove( $N )", _observable.getDataFieldName(), _otherName );
    final CodeBlock.Builder clearCacheBlock = CodeBlock.builder();
    clearCacheBlock.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )",
                                      Generator.AREZ_CLASSNAME );
    clearCacheBlock.addStatement( "this.$N = null", _observable.getCollectionCacheDataFieldName() );
    clearCacheBlock.endControlFlow();
    builder.addCode( clearCacheBlock.build() );
    builder.addStatement( "this.$N.reportChanged()", _observable.getFieldName() );

    return builder.build();
  }

  @Nonnull
  private MethodSpec buildSetMethod()
    throws ArezProcessorException
  {
    final String methodName =
      Multiplicity.ONE == _multiplicity ?
      Generator.getInverseSetMethodName( _observable.getName() ) :
      Generator.getInverseZSetMethodName( _observable.getName() );
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    if ( isReferenceInDifferentPackage() )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    final ParameterSpec.Builder parameter =
      ParameterSpec.builder( TypeName.get( _targetType.asType() ), _otherName, Modifier.FINAL );
    if ( Multiplicity.ONE == _multiplicity )
    {
      parameter.addAnnotation( Generator.NONNULL_CLASSNAME );
    }
    else
    {
      parameter.addAnnotation( Generator.NULLABLE_CLASSNAME );
    }
    builder.addParameter( parameter.build() );
    Generator.generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "this.$N.preReportChanged()", _observable.getFieldName() );
    builder.addStatement( "this.$N = $N", _observable.getDataFieldName(), _otherName );
    builder.addStatement( "this.$N.reportChanged()", _observable.getFieldName() );

    return builder.build();
  }

  @Nonnull
  private MethodSpec buildUnsetMethod()
    throws ArezProcessorException
  {
    final String methodName =
      Multiplicity.ONE == _multiplicity ?
      Generator.getInverseUnsetMethodName( _observable.getName() ) :
      Generator.getInverseZUnsetMethodName( _observable.getName() );
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    if ( isReferenceInDifferentPackage() )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    final ParameterSpec parameter =
      ParameterSpec.builder( TypeName.get( _targetType.asType() ), _otherName, Modifier.FINAL )
        .addAnnotation( Generator.NONNULL_CLASSNAME )
        .build();
    builder.addParameter( parameter );
    Generator.generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "this.$N.preReportChanged()", _observable.getFieldName() );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( this.$N == $N )", _observable.getDataFieldName(), _otherName );
    block.addStatement( "this.$N = null", _observable.getDataFieldName() );
    block.addStatement( "this.$N.reportChanged()", _observable.getFieldName() );
    block.endControlFlow();

    builder.addCode( block.build() );

    return builder.build();
  }

  private boolean isReferenceInDifferentPackage()
  {
    final PackageElement targetPackageElement = ProcessorUtil.getPackageElement( _targetType );
    final PackageElement selfPackageElement = getPackageElement( _componentDescriptor.getElement() );
    return !Objects.equals( targetPackageElement.getQualifiedName(), selfPackageElement.getQualifiedName() );
  }

  void buildVerify( @Nonnull final CodeBlock.Builder code )
  {
    if ( Multiplicity.MANY == _multiplicity )
    {
      buildManyVerify( code );
    }
    else
    {
      buildSingularVerify( code );
    }
  }

  private void buildSingularVerify( @Nonnull final CodeBlock.Builder code )
  {
    final CodeBlock.Builder builder = CodeBlock.builder();
    builder.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", Generator.AREZ_CLASSNAME );
    builder.addStatement( "$T.apiInvariant( () -> $T.isNotDisposed( this.$N ), () -> \"Inverse relationship " +
                          "named '$N' on component named '\" + this.$N.getName() + \"' contains disposed element " +
                          "'\" + this.$N + \"'\" )",
                          Generator.GUARDS_CLASSNAME,
                          Generator.DISPOSABLE_CLASSNAME,
                          _observable.getDataFieldName(),
                          _observable.getName(),
                          Generator.KERNEL_FIELD_NAME,
                          _observable.getDataFieldName() );
    builder.endControlFlow();
    code.add( builder.build() );
  }

  private void buildManyVerify( @Nonnull final CodeBlock.Builder code )
  {
    final CodeBlock.Builder builder = CodeBlock.builder();
    builder.beginControlFlow( "for( final $T element : this.$N )", _targetType, _observable.getDataFieldName() );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", Generator.AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.isNotDisposed( element ), () -> \"Inverse relationship " +
                        "named '$N' on component named '\" + this.$N.getName() + \"' contains disposed element " +
                        "'\" + element + \"'\" )",
                        Generator.GUARDS_CLASSNAME,
                        Generator.DISPOSABLE_CLASSNAME,
                        _observable.getName(),
                        Generator.KERNEL_FIELD_NAME );
    block.endControlFlow();
    builder.add( block.build() );

    builder.endControlFlow();
    code.add( builder.build() );
  }
}
