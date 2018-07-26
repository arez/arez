package arez.processor;

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
  private final String _priority;
  private final boolean _observeLowerPriorityDependencies;
  private final boolean _canNestActions;
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
                     final String priority,
                     final boolean observeLowerPriorityDependencies,
                     final boolean canNestActions,
                     @Nonnull final ExecutableElement autorun,
                     @Nonnull final ExecutableType autorunType )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    _mutation = mutation;
    _priority = Objects.requireNonNull( priority );
    _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
    _canNestActions = canNestActions;
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
      FieldSpec.builder( GeneratorUtil.OBSERVER_CLASSNAME, getFieldName(), Modifier.FINAL, Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    builder.addField( field.build() );
  }

  @Nonnull
  private String getFieldName()
  {
    return GeneratorUtil.FIELD_PREFIX + getName();
  }

  /**
   * Setup initial state of autorun in constructor.
   */
  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N().autorun( $T.areNativeComponentsEnabled() ? this.$N : null, " +
               "$T.areNamesEnabled() ? $N() + $S : null, " );
    parameters.add( getFieldName() );
    parameters.add( _componentDescriptor.getContextMethodName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    sb.append( _mutation );
    sb.append( ", () -> super.$N()" );
    parameters.add( getAutorun().getSimpleName().toString() );
    if ( !"NORMAL".equals( _priority ) || _observeLowerPriorityDependencies || _canNestActions )
    {
      sb.append( ", $T.$N, false" );
      parameters.add( GeneratorUtil.PRIORITY_CLASSNAME );
      parameters.add( _priority );

      if ( _observeLowerPriorityDependencies || _canNestActions )
      {
        sb.append( ", " );
        sb.append( _observeLowerPriorityDependencies );
        if ( _canNestActions )
        {
          sb.append( ", true" );
        }
      }
    }
    sb.append( " )" );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  void buildDisposer( @Nonnull final CodeBlock.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", getFieldName() );
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
    final String methodName = _refMethod.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _refMethod, builder );
    ProcessorUtil.copyTypeParameters( _refMethodType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _refMethod, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _refMethodType.getReturnType() ) );

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );

    builder.addStatement( "return $N", getFieldName() );

    return builder.build();
  }

  /**
   * Generate the autorun wrapper.
   * This is wrapped to block user from directly invoking autorun method.
   */
  @Nonnull
  private MethodSpec buildAutorun()
    throws ArezProcessorException
  {
    final String methodName = _autorun.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _autorun, builder );
    ProcessorUtil.copyExceptions( _autorunType, builder );
    ProcessorUtil.copyTypeParameters( _autorunType, builder );
    ProcessorUtil.copyDocumentedAnnotations( _autorun, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _autorun.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", GeneratorUtil.AREZ_CLASSNAME );
    block.addStatement( "$T.fail( () -> \"Autorun method named '$N' invoked but @Autorun annotated " +
                        "methods should only be invoked by the runtime.\" )",
                        GeneratorUtil.GUARDS_CLASSNAME,
                        methodName );
    block.endControlFlow();

    builder.addCode( block.build() );
    // This super is generated so that the GWT compiler in production model will identify this as a method
    // that only contains a super invocation and will thus inline it. If the body is left empty then the
    // GWT compiler will be required to keep the empty method present because it can not determine that the
    // empty method will never be invoked.
    builder.addStatement( "super.$N()", _autorun.getSimpleName() );

    return builder.build();
  }
}
