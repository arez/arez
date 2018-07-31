package arez.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of @Memoize methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class MemoizeDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  @Nonnull
  private final String _priority;
  private final boolean _observeLowerPriorityDependencies;
  @Nullable
  private ExecutableElement _memoize;
  @Nullable
  private ExecutableType _memoizeType;

  MemoizeDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                     @Nonnull final String name,
                     @Nonnull final String priority,
                     final boolean observeLowerPriorityDependencies )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    _priority = Objects.requireNonNull( priority );
    _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  ExecutableElement getMemoize()
  {
    return Objects.requireNonNull( _memoize );
  }

  void setMemoize( @Nonnull final ExecutableElement memoize, @Nonnull final ExecutableType memoizeType )
    throws ArezProcessorException
  {
    //The caller already verified that no duplicate computed have been defined
    assert null == _memoize;
    MethodChecks.mustBeWrappable( _componentDescriptor.getElement(), Constants.MEMOIZE_ANNOTATION_CLASSNAME, memoize );
    MethodChecks.mustHaveParameters( Constants.MEMOIZE_ANNOTATION_CLASSNAME, memoize );
    MethodChecks.mustReturnAValue( Constants.MEMOIZE_ANNOTATION_CLASSNAME, memoize );
    MethodChecks.mustNotThrowAnyExceptions( Constants.MEMOIZE_ANNOTATION_CLASSNAME, memoize );

    _memoize = Objects.requireNonNull( memoize );
    _memoizeType = Objects.requireNonNull( memoizeType );
  }

  void buildFields( @Nonnull final TypeSpec.Builder builder )
  {
    assert null != _memoize;
    assert null != _memoizeType;
    final TypeName parameterType =
      _memoize.getTypeParameters().isEmpty() ? TypeName.get( _memoizeType.getReturnType() ).box() :
      WildcardTypeName.subtypeOf( TypeName.OBJECT );
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( GeneratorUtil.MEMOIZE_CACHE_CLASSNAME, parameterType );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName,
                         getFieldName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    builder.addField( field.build() );
  }

  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    assert null != _memoize;
    assert null != _memoizeType;
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append(
      "this.$N = new $T<>( $T.areZonesEnabled() ? $N() : null, $T.areNativeComponentsEnabled() ? this.$N : null, " +
      "$T.areNamesEnabled() ? $N() + $S : null, args -> super.$N(" );
    parameters.add( getFieldName() );
    parameters.add( GeneratorUtil.MEMOIZE_CACHE_CLASSNAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getContextMethodName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    parameters.add( _memoize.getSimpleName().toString() );

    int index = 0;
    for ( final TypeMirror arg : _memoizeType.getParameterTypes() )
    {
      if ( 0 != index )
      {
        sb.append( ", " );
      }
      sb.append( "($T) args[ " ).append( index ).append( " ]" );
      parameters.add( arg );
      index++;
    }

    sb.append( "), " );
    sb.append( _memoize.getParameters().size() );
    sb.append( ", $T.$N," );
    sb.append( _observeLowerPriorityDependencies );
    sb.append( " )" );
    parameters.add( GeneratorUtil.PRIORITY_CLASSNAME );
    parameters.add( _priority );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", getFieldName() );
  }

  @Nonnull
  private String getFieldName()
  {
    return GeneratorUtil.FIELD_PREFIX + getName();
  }

  void buildMethods( @Nonnull final TypeSpec.Builder builder )
    throws ArezProcessorException
  {
    builder.addMethod( buildMemoize() );
  }

  @Nonnull
  private MethodSpec buildMemoize()
    throws ArezProcessorException
  {
    assert null != _memoize;
    assert null != _memoizeType;
    final String methodName = _memoize.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _memoize, builder );
    ProcessorUtil.copyExceptions( _memoizeType, builder );
    ProcessorUtil.copyTypeParameters( _memoizeType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _memoize, builder );
    builder.addAnnotation( Override.class );
    final TypeName returnType = TypeName.get( _memoizeType.getReturnType() );
    builder.returns( returnType );

    final boolean hasTypeParameters = !_memoize.getTypeParameters().isEmpty();
    if ( hasTypeParameters )
    {
      builder.addAnnotation( AnnotationSpec.builder( SuppressWarnings.class )
                               .addMember( "value", "$S", "unchecked" )
                               .build() );
    }

    {
      final List<? extends VariableElement> parameters = _memoize.getParameters();
      final int paramCount = parameters.size();
      for ( int i = 0; i < paramCount; i++ )
      {
        final VariableElement element = parameters.get( i );
        final TypeName parameterType = TypeName.get( _memoizeType.getParameterTypes().get( i ) );
        final ParameterSpec.Builder param =
          ParameterSpec.builder( parameterType, element.getSimpleName().toString(), Modifier.FINAL );
        ProcessorUtil.copyDocumentedAnnotations( element, param );
        builder.addParameter( param.build() );
      }
    }

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );

    final StringBuilder sb = new StringBuilder();
    final ArrayList<Object> parameters = new ArrayList<>();
    sb.append( "return " );
    if ( hasTypeParameters )
    {
      sb.append( "($T) " );
      parameters.add( returnType.box() );
    }
    sb.append( "this.$N.get( " );
    parameters.add( getFieldName() );

    boolean first = true;
    for ( final VariableElement element : _memoize.getParameters() )
    {
      if ( !first )
      {
        sb.append( ", " );
      }
      first = false;
      sb.append( "$N" );
      parameters.add( element.getSimpleName().toString() );
    }
    sb.append( " )" );

    builder.addStatement( sb.toString(), parameters.toArray() );

    return builder.build();
  }
}
