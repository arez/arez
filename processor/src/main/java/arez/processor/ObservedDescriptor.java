package arez.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * The class that represents the parsed state of @Observed methods on a @ArezComponent annotated class.
 */
@SuppressWarnings( "Duplicates" )
final class ObservedDescriptor
{
  @Nonnull
  private final ComponentDescriptor _componentDescriptor;
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nonnull
  private final String _priority;
  private final boolean _observeLowerPriorityDependencies;
  private final boolean _nestedActionsAllowed;
  @Nonnull
  private final ExecutableElement _observed;
  @Nonnull
  private final ExecutableType _observedType;
  @Nullable
  private ExecutableElement _refMethod;
  @Nullable
  private ExecutableType _refMethodType;

  ObservedDescriptor( @Nonnull final ComponentDescriptor componentDescriptor,
                      @Nonnull final String name,
                      final boolean mutation,
                      final String priority,
                      final boolean observeLowerPriorityDependencies,
                      final boolean nestedActionsAllowed,
                      @Nonnull final ExecutableElement observed,
                      @Nonnull final ExecutableType observedType )
  {
    _componentDescriptor = Objects.requireNonNull( componentDescriptor );
    _name = Objects.requireNonNull( name );
    _mutation = mutation;
    _priority = Objects.requireNonNull( priority );
    _observeLowerPriorityDependencies = observeLowerPriorityDependencies;
    _nestedActionsAllowed = nestedActionsAllowed;
    _observed = Objects.requireNonNull( observed );
    _observedType = Objects.requireNonNull( observedType );
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
  ExecutableElement getObserved()
  {
    return _observed;
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
   * Setup initial state of observed in constructor.
   */
  void buildInitializer( @Nonnull final MethodSpec.Builder builder )
  {
    final ArrayList<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N().observer( $T.areNativeComponentsEnabled() ? this.$N : null, " +
               "$T.areNamesEnabled() ? $N() + $S : null, () -> super.$N(), " );
    parameters.add( getFieldName() );
    parameters.add( _componentDescriptor.getContextMethodName() );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( GeneratorUtil.COMPONENT_FIELD_NAME );
    parameters.add( GeneratorUtil.AREZ_CLASSNAME );
    parameters.add( _componentDescriptor.getComponentNameMethodName() );
    parameters.add( "." + getName() );
    parameters.add( getObserved().getSimpleName().toString() );

    final ArrayList<String> flags = new ArrayList<>();
    flags.add( "RUN_LATER" );

    if ( _observeLowerPriorityDependencies )
    {
      flags.add( "OBSERVE_LOWER_PRIORITY_DEPENDENCIES" );
    }
    if ( _nestedActionsAllowed )
    {
      flags.add( "NESTED_ACTIONS_ALLOWED" );
    }
    if ( _mutation )
    {
      flags.add( "READ_WRITE" );
    }
    if ( !"NORMAL".equals( _priority ) )
    {
      flags.add( "PRIORITY_" + _priority );
    }

    sb.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( GeneratorUtil.FLAGS_CLASSNAME );
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
    builder.addMethod( buildObserved() );
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
    ProcessorUtil.copyWhitelistedAnnotations( _refMethod, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( _refMethodType.getReturnType() ) );

    GeneratorUtil.generateNotDisposedInvariant( _componentDescriptor, builder, methodName );

    builder.addStatement( "return $N", getFieldName() );

    return builder.build();
  }

  /**
   * Generate the observed wrapper.
   * This is wrapped to block user from directly invoking observed method.
   */
  @Nonnull
  private MethodSpec buildObserved()
    throws ArezProcessorException
  {
    final String methodName = _observed.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    ProcessorUtil.copyAccessModifiers( _observed, builder );
    ProcessorUtil.copyExceptions( _observedType, builder );
    ProcessorUtil.copyTypeParameters( _observedType, builder );
    ProcessorUtil.copyWhitelistedAnnotations( _observed, builder );
    builder.addAnnotation( Override.class );
    final TypeMirror returnType = _observed.getReturnType();
    builder.returns( TypeName.get( returnType ) );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", GeneratorUtil.AREZ_CLASSNAME );
    block.addStatement( "$T.fail( () -> \"Observed method named '$N' invoked but @Observed annotated " +
                        "methods should only be invoked by the runtime.\" )",
                        GeneratorUtil.GUARDS_CLASSNAME,
                        methodName );
    block.endControlFlow();

    builder.addCode( block.build() );
    // This super is generated so that the GWT compiler in production model will identify this as a method
    // that only contains a super invocation and will thus inline it. If the body is left empty then the
    // GWT compiler will be required to keep the empty method present because it can not determine that the
    // empty method will never be invoked.
    builder.addStatement( "super.$N()", _observed.getSimpleName() );

    return builder.build();
  }
}
