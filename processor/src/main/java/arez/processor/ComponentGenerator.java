package arez.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.realityforge.proton.AnnotationsUtil;
import org.realityforge.proton.ElementsUtil;
import org.realityforge.proton.GeneratorUtil;
import org.realityforge.proton.ProcessorException;
import org.realityforge.proton.SuppressWarningsUtil;
import org.realityforge.proton.TypesUtil;

final class ComponentGenerator
{
  private static final ClassName GUARDS_CLASSNAME = ClassName.get( "org.realityforge.braincheck", "Guards" );
  private static final ClassName AREZ_CLASSNAME = ClassName.get( "arez", "Arez" );
  private static final ClassName ACTION_FLAGS_CLASSNAME = ClassName.get( "arez", "ActionFlags" );
  private static final ClassName ACTION_SKIPPED_EVENT_CLASSNAME = ClassName.get( "arez.spy", "ActionSkippedEvent" );
  private static final ClassName OBSERVER_FLAGS_CLASSNAME = ClassName.get( "arez", "Observer", "Flags" );
  private static final ClassName COMPUTABLE_VALUE_FLAGS_CLASSNAME = ClassName.get( "arez", "ComputableValue", "Flags" );
  private static final ClassName OBJECTS_DEEP_EQUALS_COMPARATOR_CLASSNAME = ClassName.get( "arez", "ObjectsDeepEqualsComparator" );
  private static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "arez", "ArezContext" );
  private static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "arez", "ObservableValue" );
  private static final ClassName OBSERVER_CLASSNAME = ClassName.get( "arez", "Observer" );
  private static final ClassName COMPUTABLE_VALUE_CLASSNAME = ClassName.get( "arez", "ComputableValue" );
  private static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "arez", "Disposable" );
  private static final ClassName COMPONENT_CLASSNAME = ClassName.get( "arez", "Component" );
  private static final ClassName SAFE_PROCEDURE_CLASSNAME = ClassName.get( "arez", "SafeProcedure" );
  private static final ClassName KERNEL_CLASSNAME = ClassName.get( "arez.component.internal", "ComponentKernel" );
  private static final ClassName MEMOIZE_CACHE_CLASSNAME = ClassName.get( "arez.component.internal", "MemoizeCache" );
  private static final ClassName IDENTIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Identifiable" );
  private static final ClassName COMPONENT_OBSERVABLE_CLASSNAME =
    ClassName.get( "arez.component", "ComponentObservable" );
  private static final ClassName DISPOSE_TRACKABLE_CLASSNAME = ClassName.get( "arez.component", "DisposeNotifier" );
  private static final ClassName COLLECTIONS_UTIL_CLASSNAME =
    ClassName.get( "arez.component.internal", "CollectionsUtil" );
  private static final ClassName LOCATOR_CLASSNAME = ClassName.get( "arez", "Locator" );
  private static final ClassName LINKABLE_CLASSNAME = ClassName.get( "arez.component", "Linkable" );
  private static final ClassName VERIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Verifiable" );
  private static final ClassName STING_INJECTABLE = ClassName.get( "sting", "Injectable" );
  /**
   * Prefix for fields that are used to generate Arez elements.
   */
  static final String FIELD_PREFIX = "$$arez$$_";
  /**
   * For fields that are synthesized to hold data for abstract observable properties.
   */
  static final String OBSERVABLE_DATA_FIELD_PREFIX = "$$arezd$$_";
  /**
   * For fields that are synthesized to hold resolved references.
   */
  static final String REFERENCE_FIELD_PREFIX = "$$arezr$$_";
  /**
   * For methods/fields used internally for the component to manage lifecycle or implement support functionality.
   */
  static final String FRAMEWORK_PREFIX = "$$arezi$$_";
  private static final String INTERNAL_DISPOSE_METHOD_NAME = FRAMEWORK_PREFIX + "dispose";
  private static final String INTERNAL_NATIVE_COMPONENT_PRE_DISPOSE_METHOD_NAME =
    FRAMEWORK_PREFIX + "nativeComponentPreDispose";
  private static final String INTERNAL_PRE_DISPOSE_METHOD_NAME = FRAMEWORK_PREFIX + "preDispose";
  private static final String INTERNAL_POST_DISPOSE_METHOD_NAME = FRAMEWORK_PREFIX + "postDispose";
  private static final String NEXT_ID_FIELD_NAME = FRAMEWORK_PREFIX + "nextId";
  private static final String KERNEL_FIELD_NAME = FRAMEWORK_PREFIX + "kernel";
  static final String ID_FIELD_NAME = FRAMEWORK_PREFIX + "id";
  private static final String LOCATOR_METHOD_NAME = FRAMEWORK_PREFIX + "locator";
  /**
   * For constructor initializer args where it collides with existing name.
   */
  private static final String INITIALIZER_PREFIX = "$$arezip$$_";
  /**
   * For variables used within generated methods that need a unique name.
   */
  private static final String VARIABLE_PREFIX = "$$arezv$$_";
  private static final String DEPENDENCY_KEY_PREFIX = "$$arez_dk$$_";
  private static final String COMPONENT_VAR_NAME = VARIABLE_PREFIX + "component";
  private static final String NAME_VAR_NAME = VARIABLE_PREFIX + "name";
  private static final String ID_VAR_NAME = VARIABLE_PREFIX + "id";
  private static final String CONTEXT_VAR_NAME = VARIABLE_PREFIX + "context";
  private static final TypeKind DEFAULT_ID_KIND = TypeKind.INT;
  static final TypeName DEFAULT_ID_TYPE = TypeName.INT;
  /**
   * For fields that are synthesized to hold resolved references.
   */
  private static final String INVERSE_REFERENCE_METHOD_PREFIX = "$$arezir$$_";

  private ComponentGenerator()
  {
  }

  /**
   * Build the enhanced class for the component.
   */
  @SuppressWarnings( "unchecked" )
  @Nonnull
  static TypeSpec buildType( @Nonnull final ProcessingEnvironment processingEnv,
                             @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( component.getEnhancedClassName().simpleName() ).
      addTypeVariables( GeneratorUtil.getTypeArgumentsAsNames( component.asDeclaredType() ) ).
      addModifiers( Modifier.FINAL );
    GeneratorUtil.addOriginatingTypes( component.getElement(), builder );
    if ( component.isStingEnabled() )
    {
      builder.addAnnotation( STING_INJECTABLE );
    }

    final List<String> whitelist = new ArrayList<>( GeneratorUtil.ANNOTATION_WHITELIST );

    if ( component.isStingEnabled() )
    {
      whitelist.add( Constants.STING_NAMED );
      whitelist.add( Constants.STING_EAGER );
      whitelist.add( Constants.STING_CONTRIBUTE_TO );
    }

    GeneratorUtil.copyWhitelistedAnnotations( component.getElement(), builder, whitelist );

    if ( component.isStingEnabled() )
    {
      final AnnotationMirror typed =
        AnnotationsUtil.findAnnotationByType( component.getElement(), Constants.STING_TYPED );
      if ( null == typed )
      {
        builder.addAnnotation( AnnotationSpec
                                 .builder( ClassName.get( "sting", "Typed" ) )
                                 .addMember( "value", "$T.class", component.getClassName() )
                                 .build() );
      }
      else if ( ( (List<AnnotationValue>) AnnotationsUtil.getAnnotationValue( typed, "value" ).getValue() ).isEmpty() )
      {
        // This is only needed because javapoet has a bug and does not correctly the scenario
        // where the attribute is an array, and it is empty and there is no default value
        builder.addAnnotation( AnnotationSpec
                                 .builder( ClassName.get( "sting", "Typed" ) )
                                 .addMember( "value", "{}" )
                                 .build() );
      }
      else
      {
        builder.addAnnotation( AnnotationSpec.get( typed ) );
      }
    }

    if ( component.isClassType() )
    {
      builder.superclass( TypeName.get( component.getElement().asType() ) );
    }
    else
    {
      builder.addSuperinterface( TypeName.get( component.getElement().asType() ) );
    }

    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, ArezProcessor.class.getName() );
    final List<String> additionalSuppressions =
      new ArrayList<>( component.getMemoizes().isEmpty() ?
                       Collections.emptyList() :
                       Collections.singletonList( "unchecked" ) );
    additionalSuppressions.addAll( getAdditionalSuppressions( component.asDeclaredType().asElement() ) );
    final List<TypeMirror> types = new ArrayList<>();
    final ExecutableElement componentId = component.getComponentId();
    if ( component.isIdRequired() && null != componentId )
    {
      types.add( processingEnv.getTypeUtils().asMemberOf( component.asDeclaredType(), componentId ) );
    }
    types.add( component.asDeclaredType() );
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, builder, additionalSuppressions, types );

    if ( component.shouldGeneratedClassBePublic( processingEnv ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }

    builder.addSuperinterface( DISPOSABLE_CLASSNAME );
    if ( component.isIdRequired() )
    {
      builder.addSuperinterface( ParameterizedTypeName.get( IDENTIFIABLE_CLASSNAME, component.getIdType().box() ) );
    }
    if ( component.isObservable() )
    {
      builder.addSuperinterface( COMPONENT_OBSERVABLE_CLASSNAME );
    }
    if ( component.shouldVerify() )
    {
      builder.addSuperinterface( VERIFIABLE_CLASSNAME );
    }
    if ( component.isDisposeNotifier() )
    {
      builder.addSuperinterface( DISPOSE_TRACKABLE_CLASSNAME );
    }
    if ( component.needsExplicitLink() )
    {
      builder.addSuperinterface( LINKABLE_CLASSNAME );
    }

    buildFields( processingEnv, component, builder );

    if ( component.isClassType() )
    {
      buildConstructors( processingEnv, component, builder );
    }
    else
    {
      builder.addMethod( buildConstructor( processingEnv, component, null, null ) );
    }

    for ( final ExecutableElement contextRef : component.getContextRefs() )
    {
      final MethodSpec.Builder method = refMethod( processingEnv, component.getElement(), contextRef );
      generateNotInitializedInvariant( component, method, contextRef.getSimpleName().toString() );
      method.addStatement( "return this.$N.getContext()", KERNEL_FIELD_NAME );
      builder.addMethod( method.build() );
    }
    for ( final ExecutableElement componentRef : component.getComponentRefs() )
    {
      builder.addMethod( buildComponentRefMethod( processingEnv, component, componentRef ) );
    }
    for ( final ExecutableElement componentIdRef : component.getComponentIdRefs() )
    {
      builder.addMethod( refMethod( processingEnv, component.getElement(), componentIdRef )
                           .addStatement( "return this.$N()", component.getIdMethodName() )
                           .build() );
    }
    if ( !component.getReferences().isEmpty() || component.hasInverses() )
    {
      builder.addMethod( buildLocatorRefMethod( component ) );
    }
    if ( null == component.getComponentId() )
    {
      builder.addMethod( buildComponentIdMethod( component ) );
    }
    if ( component.isIdRequired() )
    {
      builder.addMethod( buildArezIdMethod( processingEnv, component ) );
    }
    for ( final ExecutableElement componentNameRef : component.getComponentNameRefs() )
    {
      final MethodSpec.Builder method = refMethod( processingEnv, component.getElement(), componentNameRef );
      generateNotInitializedInvariant( component, method, componentNameRef.getSimpleName().toString() );
      method.addStatement( "return this.$N.getName()", KERNEL_FIELD_NAME );
      builder.addMethod( method.build() );
    }
    for ( final ExecutableElement componentTypeNameRef : component.getComponentTypeNameRefs() )
    {
      builder.addMethod( refMethod( processingEnv, component.getElement(), componentTypeNameRef )
                           .addStatement( "return $S", component.getName() )
                           .build() );
    }

    if ( component.isObservable() )
    {
      builder.addMethod( buildObserve( component ) );
    }
    if ( hasInternalPreDispose( component ) )
    {
      builder.addMethod( buildInternalPreDispose( component ) );
    }
    if ( hasInternalPreDispose( component ) || component.isDisposeNotifier() )
    {
      builder.addMethod( buildNativeComponentPreDispose( component ) );
    }
    if ( component.isDisposeNotifier() )
    {
      builder.addMethod( buildAddOnDisposeListener( component ) );
      builder.addMethod( buildRemoveOnDisposeListener( component ) );
    }
    if ( component.hasInternalPostDispose() )
    {
      builder.addMethod( buildInternalPostDispose( component ) );
    }
    builder.addMethod( buildIsDisposed( component ) );
    builder.addMethod( buildDispose( component ) );
    if ( component.needsInternalDispose() )
    {
      builder.addMethod( buildInternalDispose( component ) );
    }
    if ( component.shouldVerify() )
    {
      builder.addMethod( buildVerify( component ) );
    }

    if ( component.needsExplicitLink() )
    {
      builder.addMethod( buildLink( component ) );
    }

    component.getComponentStateRefs().forEach( e -> buildComponentStateRefMethods( processingEnv,
                                                                                   e,
                                                                                   component.getElement(),
                                                                                   builder ) );
    component.getObservables().values().forEach( e -> buildObservableMethods( processingEnv, e, builder ) );
    component.getObserves().values().forEach( e -> buildObserveMethods( processingEnv, e, builder ) );
    component.getActions().values().forEach( e -> builder.addMethod( buildAction( processingEnv, e ) ) );
    component.getMemoizes().values().forEach( e -> buildMemoizeMethods( processingEnv, e, builder ) );
    component.getReferences().values().forEach( e -> buildReferenceMethods( processingEnv, e, builder ) );
    component.getInverses().values().forEach( e -> buildInverseMethods( e, builder ) );

    if ( component.isRequireEquals() )
    {
      builder.addMethod( buildHashcodeMethod( component ) );
      builder.addMethod( buildEqualsMethod( component ) );
    }

    if ( component.isGenerateToString() )
    {
      builder.addMethod( buildToStringMethod( component ) );
    }

    return builder.build();
  }

  @Nonnull
  private static String getInverseAddMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_add";
  }

  @Nonnull
  private static String getInverseRemoveMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_remove";
  }

  @Nonnull
  private static String getInverseSetMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_set";
  }

  @Nonnull
  private static String getInverseUnsetMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_unset";
  }

  @Nonnull
  private static String getInverseZSetMethodName( @Nonnull final String name )
  {
    // Use different names for linking/unlinking if there is different multiplicities to
    // avoid scenario where classes that are not consistent will be able to be loaded
    // by the jvm
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_zset";
  }

  @Nonnull
  private static String getInverseZUnsetMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_zunset";
  }

  @Nonnull
  static String getLinkMethodName( @Nonnull final String name )
  {
    return FRAMEWORK_PREFIX + "link_" + name;
  }

  @Nonnull
  static String getDelinkMethodName( @Nonnull final String name )
  {
    return FRAMEWORK_PREFIX + "delink_" + name;
  }

  private static void generateNotInitializedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                                       @Nonnull final MethodSpec.Builder builder,
                                                       @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != this.$N && this.$N.hasBeenInitialized(), " +
                        "() -> \"Method named '$N' invoked on uninitialized component of type '$N'\" )",
                        GUARDS_CLASSNAME,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME,
                        methodName,
                        descriptor.getName() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  private static void generateNotConstructedInvariant( @Nonnull final MethodSpec.Builder builder,
                                                       @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != this.$N && this.$N.hasBeenConstructed(), " +
                        "() -> \"Method named '$N' invoked on un-constructed component named '\" + " +
                        "( null == this.$N ? \"?\" : this.$N.getName() ) + \"'\" )",
                        GUARDS_CLASSNAME,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME,
                        methodName,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  private static void generateNotCompleteInvariant( @Nonnull final MethodSpec.Builder builder,
                                                    @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != this.$N && this.$N.hasBeenCompleted(), " +
                        "() -> \"Method named '$N' invoked on incomplete component named '\" + " +
                        "( null == this.$N ? \"?\" : this.$N.getName() ) + \"'\" )",
                        GUARDS_CLASSNAME,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME,
                        methodName,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  private static void generateNotDisposedInvariant( @Nonnull final MethodSpec.Builder builder,
                                                    @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != this.$N && this.$N.isActive(), " +
                        "() -> \"Method named '$N' invoked on \" + this.$N.describeState() + \" component " +
                        "named '\" + this.$N.getName() + \"'\" )",
                        GUARDS_CLASSNAME,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME,
                        methodName,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  private static void generateTryBlock( @Nonnull final MethodSpec.Builder builder,
                                        @Nonnull final List<? extends TypeMirror> expectedThrowTypes,
                                        @Nonnull final Consumer<CodeBlock.Builder> action )
  {
    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "try" );

    action.accept( codeBlock );

    final boolean catchThrowable =
      expectedThrowTypes.stream().anyMatch( t -> t.toString().equals( "java.lang.Throwable" ) );
    final boolean catchException =
      expectedThrowTypes.stream().anyMatch( t -> t.toString().equals( "java.lang.Exception" ) );
    final boolean catchRuntimeException =
      expectedThrowTypes.stream().anyMatch( t -> t.toString().equals( "java.lang.RuntimeException" ) );
    int thrownCount = expectedThrowTypes.size();
    final List<Object> args = new ArrayList<>( expectedThrowTypes );
    if ( !catchThrowable && !catchRuntimeException && !catchException )
    {
      thrownCount++;
      args.add( TypeName.get( RuntimeException.class ) );
    }
    if ( !catchThrowable )
    {
      thrownCount++;
      args.add( TypeName.get( Error.class ) );
    }

    final String caughtThrowableName = "$$arez_exception$$";
    args.add( caughtThrowableName );

    final String code =
      "catch( final " +
      IntStream.range( 0, thrownCount ).mapToObj( t -> "$T" ).collect( Collectors.joining( " | " ) ) +
      " $N )";
    codeBlock.nextControlFlow( code, args.toArray() );
    codeBlock.addStatement( "throw $N", caughtThrowableName );

    if ( !catchThrowable )
    {
      codeBlock.nextControlFlow( "catch( final $T $N )", Throwable.class, caughtThrowableName );
      codeBlock.addStatement( "throw new $T( $N )", IllegalStateException.class, caughtThrowableName );
    }
    codeBlock.endControlFlow();
    builder.addCode( codeBlock.build() );
  }

  private static void buildInverseDisposer( @Nonnull final InverseDescriptor inverse,
                                            @Nonnull final MethodSpec.Builder builder )
  {
    final ObservableDescriptor observable = inverse.getObservable();
    final String delinkMethodName = getDelinkMethodName( inverse.getReferenceName() );
    final ClassName arezClassName = getArezClassName( inverse );
    final CodeBlock.Builder block = CodeBlock.builder();
    if ( Multiplicity.MANY == inverse.getMultiplicity() )
    {
      block.beginControlFlow( "for ( final $T other : new $T<>( $N ) )",
                              inverse.getTargetType(),
                              TypeName.get( ArrayList.class ),
                              observable.getDataFieldName() );
      block.addStatement( "( ($T) other ).$N()", arezClassName, delinkMethodName );
    }
    else
    {
      block.beginControlFlow( "if ( null != $N )", observable.getDataFieldName() );
      block.addStatement( "( ($T) $N ).$N()",
                          arezClassName,
                          observable.getDataFieldName(),
                          delinkMethodName );
    }
    block.endControlFlow();
    builder.addCode( block.build() );
  }

  @Nonnull
  private static ClassName getArezClassName( @Nonnull final InverseDescriptor inverseDescriptor )
  {
    final TypeName typeName = TypeName.get( inverseDescriptor.getObservable().getGetter().getReturnType() );
    final ClassName other = typeName instanceof ClassName ?
                            (ClassName) typeName :
                            (ClassName) ( (ParameterizedTypeName) typeName ).typeArguments.get( 0 );
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

  @Nonnull
  private static MethodSpec buildAction( @Nonnull final ProcessingEnvironment processingEnv,
                                         @Nonnull final ActionDescriptor action )
    throws ProcessorException
  {
    final String methodName = action.getAction().getSimpleName().toString();
    final ComponentDescriptor component = action.getComponent();
    final MethodSpec.Builder method = overrideMethod( processingEnv, component.getElement(), action.getAction() );

    final boolean isProcedure = action.getActionType().getReturnType().getKind() == TypeKind.VOID;
    final List<? extends TypeMirror> thrownTypes = action.getAction().getThrownTypes();
    final boolean isSafe = thrownTypes.isEmpty();

    final StringBuilder statement = new StringBuilder();
    final List<Object> params = new ArrayList<>();

    if ( !isProcedure )
    {
      statement.append( "return " );
    }

    statement.append( "this.$N.getContext()." );
    params.add( KERNEL_FIELD_NAME );

    if ( isProcedure && isSafe )
    {
      statement.append( "safeAction" );
    }
    else if ( isProcedure )
    {
      statement.append( "action" );
    }
    else if ( isSafe )
    {
      statement.append( "safeAction" );
    }
    else
    {
      statement.append( "action" );
    }

    statement.append( "( " );

    statement.append( "$T.areNamesEnabled() ? this.$N.getName() + $S : null" );
    params.add( AREZ_CLASSNAME );
    params.add( KERNEL_FIELD_NAME );
    params.add( "." + action.getName() );

    statement.append( ", () -> " );
    if ( component.isInterfaceType() )
    {
      statement.append( "$T." );
      params.add( component.getClassName() );
    }
    statement.append( "super.$N(" );
    params.add( action.getAction().getSimpleName().toString() );

    final List<? extends VariableElement> parameters = action.getAction().getParameters();
    final int paramCount = parameters.size();

    boolean firstParam = true;
    if ( 0 != paramCount )
    {
      statement.append( " " );
    }
    for ( final VariableElement element : parameters )
    {
      params.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        statement.append( ", " );
      }
      firstParam = false;
      statement.append( "$N" );
    }
    if ( 0 != paramCount )
    {
      statement.append( " " );
    }

    statement.append( "), " );

    appendActionFlags( action, statement, params );

    statement.append( ", " );

    if ( action.isReportParameters() && !parameters.isEmpty() )
    {
      statement.append( "$T.areSpiesEnabled() ? new $T[] { " );
      params.add( AREZ_CLASSNAME );
      params.add( Object.class );
      firstParam = true;
      for ( final VariableElement parameter : parameters )
      {
        if ( !firstParam )
        {
          statement.append( ", " );
        }
        firstParam = false;
        params.add( parameter.getSimpleName().toString() );
        statement.append( "$N" );
      }
      statement.append( " } : null" );
    }
    else
    {
      statement.append( "null" );
    }
    statement.append( " )" );

    if ( action.isSkipIfDisposed() )
    {
      final CodeBlock nameExpr =
        CodeBlock.of( "$T.areNamesEnabled() ? this.$N.getName() + $S : null",
                      AREZ_CLASSNAME,
                      KERNEL_FIELD_NAME,
                      "." + action.getName() );
      final CodeBlock parametersExpr = buildActionParametersExpression( action, parameters );
      final CodeBlock.Builder guardBlock = CodeBlock.builder();
      guardBlock.beginControlFlow( "if ( this.$N.isDisposed() )", KERNEL_FIELD_NAME );
      guardBlock
        .beginControlFlow( "if ( $T.areSpiesEnabled() && this.$N.getContext().getSpy().willPropagateSpyEvents() )",
                           AREZ_CLASSNAME,
                           KERNEL_FIELD_NAME );
      guardBlock.addStatement( "this.$N.getContext().getSpy().reportSpyEvent( new $T( $L, false, $L ) )",
                               KERNEL_FIELD_NAME,
                               ACTION_SKIPPED_EVENT_CLASSNAME,
                               nameExpr,
                               parametersExpr );
      guardBlock.endControlFlow();
      guardBlock.addStatement( "return" );
      guardBlock.endControlFlow();
      method.addCode( guardBlock.build() );
    }
    else
    {
      generateNotDisposedInvariant( method, methodName );
    }
    if ( isSafe )
    {
      method.addStatement( statement.toString(), params.toArray() );
    }
    else
    {
      generateTryBlock( method,
                        thrownTypes,
                        b -> b.addStatement( statement.toString(), params.toArray() ) );
    }

    return method.build();
  }

  @Nonnull
  private static CodeBlock buildActionParametersExpression( @Nonnull final ActionDescriptor action,
                                                            @Nonnull final List<? extends VariableElement> parameters )
  {
    if ( action.isReportParameters() && !parameters.isEmpty() )
    {
      final CodeBlock.Builder builder = CodeBlock.builder();
      builder.add( "new $T[] { ", Object.class );
      boolean firstParam = true;
      for ( final VariableElement parameter : parameters )
      {
        if ( !firstParam )
        {
          builder.add( ", " );
        }
        firstParam = false;
        builder.add( "$N", parameter.getSimpleName().toString() );
      }
      builder.add( " }" );
      return builder.build();
    }
    else
    {
      return CodeBlock.of( "new $T[ 0 ]", Object.class );
    }
  }

  private static void appendActionFlags( @Nonnull final ActionDescriptor action,
                                         @Nonnull final StringBuilder expression,
                                         @Nonnull final List<Object> parameters )
  {
    final List<String> flags = new ArrayList<>();

    if ( action.isRequireNewTransaction() )
    {
      flags.add( "REQUIRE_NEW_TRANSACTION" );
    }
    if ( action.isMutation() )
    {
      flags.add( "READ_WRITE" );
    }
    else
    {
      flags.add( "READ_ONLY" );
    }
    if ( action.isVerifyRequired() )
    {
      flags.add( "VERIFY_ACTION_REQUIRED" );
    }
    else
    {
      flags.add( "NO_VERIFY_ACTION_REQUIRED" );
    }
    if ( !action.isReportResult() )
    {
      flags.add( "NO_REPORT_RESULT" );
    }

    expression.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( ACTION_FLAGS_CLASSNAME );
    }
  }

  private static void buildCascadeDisposeDisposer( @Nonnull final CascadeDisposeDescriptor cascadeDisposable,
                                                   @Nonnull final MethodSpec.Builder builder )
  {
    if ( null != cascadeDisposable.getField() )
    {
      builder.addStatement( "$T.dispose( $N )",
                            DISPOSABLE_CLASSNAME,
                            cascadeDisposable.getField().getSimpleName().toString() );
    }
    else
    {
      assert null != cascadeDisposable.getMethod();
      if ( null != cascadeDisposable.getObservable() )
      {
        builder.addStatement( "$T.dispose( $N )",
                              DISPOSABLE_CLASSNAME,
                              cascadeDisposable.getObservable().getDataFieldName() );
      }
      else if ( null != cascadeDisposable.getReference() )
      {
        builder.addStatement( "$T.dispose( $N )",
                              DISPOSABLE_CLASSNAME,
                              cascadeDisposable.getReference().getFieldName() );
      }
      else
      {
        builder.addStatement( "$T.dispose( $N() )",
                              DISPOSABLE_CLASSNAME,
                              cascadeDisposable.getMethod().getSimpleName().toString() );
      }
    }
  }

  @Nonnull
  private static String getDependencyKey( @Nonnull final DependencyDescriptor dependency )
  {
    return dependency.getComponent().getDependencies().size() > 1 ?
           dependency.hasNoObservable() ? DEPENDENCY_KEY_PREFIX + dependency.getKeyName() :
           dependency.getObservable().getFieldName() :
           "this";
  }

  private static void buildDependencyInitializer( @Nonnull final DependencyDescriptor dependency,
                                                  @Nonnull final MethodSpec.Builder builder )
  {
    if ( !dependency.isMethodDependency() )
    {
      assert dependency.shouldCascadeDispose();
      final String fieldName = dependency.getField().getSimpleName().toString();
      if ( AnnotationsUtil.hasNonnullAnnotation( dependency.getField() ) )
      {
        builder.addStatement( "$T.asDisposeNotifier( this.$N ).addOnDisposeListener( $N, this::dispose, true )",
                              DISPOSE_TRACKABLE_CLASSNAME,
                              fieldName,
                              getDependencyKey( dependency ) );
      }
      else
      {
        final CodeBlock.Builder listenerBlock = CodeBlock.builder();
        listenerBlock.beginControlFlow( "if ( null != this.$N )", fieldName );
        listenerBlock.addStatement( "$T.asDisposeNotifier( this.$N ).addOnDisposeListener( $N, this::dispose, true )",
                                    DISPOSE_TRACKABLE_CLASSNAME,
                                    dependency.getField().getSimpleName(),
                                    getDependencyKey( dependency ) );
        listenerBlock.endControlFlow();
        builder.addCode( listenerBlock.build() );
      }
    }
    else
    {
      final ExecutableElement method = dependency.getMethod();
      final String methodName = method.getSimpleName().toString();
      final boolean abstractObservables = method.getModifiers().contains( Modifier.ABSTRACT );
      if ( abstractObservables )
      {
        final ObservableDescriptor observable = dependency.getObservable();
        if ( AnnotationsUtil.hasNonnullAnnotation( method ) )
        {
          assert dependency.shouldCascadeDispose();
          builder.addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, this::dispose, true )",
                                DISPOSE_TRACKABLE_CLASSNAME,
                                observable.getDataFieldName(),
                                getDependencyKey( dependency ) );
        }
        // Abstract methods that do not require initializer have no chance to be non-null in the constructor
        // so there is no need to try and add listener as this can not occur
        else if ( observable.requireInitializer() )
        {
          final String varName = VARIABLE_PREFIX + methodName + "_dependency";
          builder.addStatement( "final $T $N = this.$N",
                                dependency.getMethod().getReturnType(),
                                varName,
                                observable.getDataFieldName() );
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          if ( dependency.shouldCascadeDispose() )
          {
            listenerBlock.addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, this::dispose, true )",
                                        DISPOSE_TRACKABLE_CLASSNAME,
                                        observable.getDataFieldName(),
                                        getDependencyKey( dependency ) );
          }
          else
          {
            listenerBlock
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, () -> $N( null ), true )",
                             DISPOSE_TRACKABLE_CLASSNAME,
                             observable.getDataFieldName(),
                             getDependencyKey( dependency ),
                             observable.getSetter().getSimpleName().toString() );
          }
          listenerBlock.endControlFlow();
          builder.addCode( listenerBlock.build() );
        }
      }
      else
      {
        if ( AnnotationsUtil.hasNonnullAnnotation( method ) )
        {
          assert dependency.shouldCascadeDispose();
          if ( dependency.getComponent().isClassType() )
          {
            builder.addStatement( "$T.asDisposeNotifier( super.$N() ).addOnDisposeListener( $N, this::dispose, true )",
                                  DISPOSE_TRACKABLE_CLASSNAME,
                                  method.getSimpleName().toString(),
                                  getDependencyKey( dependency ) );
          }
          else
          {
            builder
              .addStatement( "$T.asDisposeNotifier( $T.super.$N() ).addOnDisposeListener( $N, this::dispose, true )",
                             DISPOSE_TRACKABLE_CLASSNAME,
                             dependency.getComponent().getClassName(),
                             method.getSimpleName().toString(),
                             getDependencyKey( dependency ) );
          }
        }
        else
        {
          final String varName = VARIABLE_PREFIX + methodName + "_dependency";
          if ( dependency.getComponent().isClassType() )
          {
            builder.addStatement( "final $T $N = super.$N()",
                                  dependency.getMethod().getReturnType(),
                                  varName,
                                  method.getSimpleName().toString() );
          }
          else
          {
            builder.addStatement( "final $T $N = $T.super.$N()",
                                  dependency.getMethod().getReturnType(),
                                  varName,
                                  dependency.getComponent().getClassName(),
                                  method.getSimpleName().toString() );
          }
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          if ( dependency.shouldCascadeDispose() )
          {
            if ( dependency.getComponent().isClassType() )
            {
              listenerBlock.addStatement( "$T.asDisposeNotifier( super.$N() )." +
                                          "addOnDisposeListener( $N, this::dispose, true )",
                                          DISPOSE_TRACKABLE_CLASSNAME,
                                          method.getSimpleName(),
                                          getDependencyKey( dependency ) );
            }
            else
            {
              listenerBlock.addStatement( "$T.asDisposeNotifier( $T.super.$N() )." +
                                          "addOnDisposeListener( $N, this::dispose, true )",
                                          DISPOSE_TRACKABLE_CLASSNAME,
                                          dependency.getComponent().getClassName(),
                                          method.getSimpleName(),
                                          getDependencyKey( dependency ) );
            }
          }
          else
          {
            final ObservableDescriptor observable = dependency.getObservable();
            if ( dependency.getComponent().isClassType() )
            {
              listenerBlock.addStatement( "$T.asDisposeNotifier( super.$N() )." +
                                          "addOnDisposeListener( $N, () -> $N( null ), true )",
                                          DISPOSE_TRACKABLE_CLASSNAME,
                                          method.getSimpleName(),
                                          getDependencyKey( dependency ),
                                          observable.getSetter().getSimpleName().toString() );
            }
            else
            {
              listenerBlock.addStatement( "$T.asDisposeNotifier( $T.super.$N() )." +
                                          "addOnDisposeListener( $N, () -> $N( null ), true )",
                                          DISPOSE_TRACKABLE_CLASSNAME,
                                          dependency.getComponent().getClassName(),
                                          method.getSimpleName(),
                                          getDependencyKey( dependency ),
                                          observable.getSetter().getSimpleName().toString() );
            }
          }
          listenerBlock.endControlFlow();
          builder.addCode( listenerBlock.build() );
        }
      }
    }
  }

  @Nonnull
  private static MethodSpec buildToStringMethod( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    assert component.isGenerateToString();

    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( "toString" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        returns( TypeName.get( String.class ) );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "if ( $T.areNamesEnabled() )", AREZ_CLASSNAME );
    codeBlock.addStatement( "return $S + this.$N.getName() + $S",
                            "ArezComponent[",
                            KERNEL_FIELD_NAME,
                            "]" );
    codeBlock.nextControlFlow( "else" );
    if ( component.isInterfaceType() )
    {
      codeBlock.addStatement( "return $T.super.toString()", component.getClassName() );
    }
    else
    {
      codeBlock.addStatement( "return super.toString()" );
    }
    codeBlock.endControlFlow();
    method.addCode( codeBlock.build() );
    return method.build();
  }

  @Nonnull
  private static MethodSpec buildHashcodeMethod( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String idMethod = component.getIdMethodName();

    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( "hashCode" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        returns( TypeName.INT );
    final TypeKind kind =
      null != component.getComponentId() ? component.getComponentId().getReturnType().getKind() : DEFAULT_ID_KIND;
    if ( kind == TypeKind.DECLARED || kind == TypeKind.TYPEVAR )
    {
      method.addStatement( "return null != $N() ? $N().hashCode() : $T.identityHashCode( this )",
                           idMethod,
                           idMethod,
                           System.class );
    }
    else if ( kind == TypeKind.BYTE )
    {
      method.addStatement( "return $T.hashCode( $N() )", Byte.class, idMethod );
    }
    else if ( kind == TypeKind.CHAR )
    {
      method.addStatement( "return $T.hashCode( $N() )", Character.class, idMethod );
    }
    else if ( kind == TypeKind.SHORT )
    {
      method.addStatement( "return $T.hashCode( $N() )", Short.class, idMethod );
    }
    else if ( kind == TypeKind.INT )
    {
      method.addStatement( "return $T.hashCode( $N() )", Integer.class, idMethod );
    }
    else if ( kind == TypeKind.LONG )
    {
      method.addStatement( "return $T.hashCode( $N() )", Long.class, idMethod );
    }
    else if ( kind == TypeKind.FLOAT )
    {
      method.addStatement( "return $T.hashCode( $N() )", Float.class, idMethod );
    }
    else if ( kind == TypeKind.DOUBLE )
    {
      method.addStatement( "return $T.hashCode( $N() )", Double.class, idMethod );
    }
    else
    {
      // So very unlikely but will cover it for completeness
      assert kind == TypeKind.BOOLEAN;
      method.addStatement( "return $T.hashCode( $N() )", Boolean.class, idMethod );
    }

    return method.build();
  }

  @Nonnull
  private static MethodSpec buildLocatorRefMethod( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String methodName = LOCATOR_METHOD_NAME;
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      addModifiers( Modifier.PRIVATE ).
      returns( LOCATOR_CLASSNAME );

    generateNotInitializedInvariant( component, method, methodName );

    method.addStatement( "return this.$N.getContext().locator()", KERNEL_FIELD_NAME );
    return method.build();
  }

  @Nonnull
  private static MethodSpec buildComponentRefMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                     @Nonnull final ComponentDescriptor component,
                                                     @Nonnull final ExecutableElement componentRef )
    throws ProcessorException
  {
    final MethodSpec.Builder method = refMethod( processingEnv, component.getElement(), componentRef );

    final String methodName = componentRef.getSimpleName().toString();
    generateNotInitializedInvariant( component, method, methodName );
    generateNotConstructedInvariant( method, methodName );
    generateNotCompleteInvariant( method, methodName );
    generateNotDisposedInvariant( method, methodName );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.invariant( $T::areNativeComponentsEnabled, () -> \"Invoked @ComponentRef " +
                        "method '$N' but Arez.areNativeComponentsEnabled() returned false.\" )",
                        GUARDS_CLASSNAME,
                        AREZ_CLASSNAME,
                        methodName );
    block.endControlFlow();

    method.addCode( block.build() );

    method.addStatement( "return this.$N.getComponent()", KERNEL_FIELD_NAME );
    return method.build();
  }

  @Nonnull
  private static MethodSpec buildArezIdMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                               @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String methodName = "getArezId";
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName ).
      addAnnotation( Override.class ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      addModifiers( Modifier.PUBLIC ).
      returns( component.getIdType().box() );
    generateNotInitializedInvariant( component, method, methodName );
    generateNotConstructedInvariant( method, methodName );
    method.addStatement( "return $N()", component.getIdMethodName() );

    final ExecutableElement componentId = component.getComponentId();
    if ( null != componentId )
    {
      final TypeMirror componentIdType =
        processingEnv.getTypeUtils().asMemberOf( component.asDeclaredType(), componentId );
      SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, method, componentIdType );
    }
    return method.build();
  }

  @Nonnull
  private static MethodSpec buildComponentIdMethod( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    assert null == component.getComponentId();

    return MethodSpec
      .methodBuilder( ID_FIELD_NAME )
      .addModifiers( Modifier.PRIVATE )
      .returns( DEFAULT_ID_TYPE )
      .addStatement( "assert null != this.$N", KERNEL_FIELD_NAME )
      .addStatement( "return this.$N.getId()", KERNEL_FIELD_NAME )
      .build();
  }

  @Nonnull
  private static MethodSpec buildVerify( @Nonnull final ComponentDescriptor component )
  {
    final String methodName = "verify";
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( methodName ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class );

    generateNotInitializedInvariant( component, method, methodName );
    generateNotConstructedInvariant( method, methodName );
    generateNotDisposedInvariant( method, methodName );

    if ( !component.getReferences().isEmpty() || !component.getInverses().isEmpty() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() && $T.isVerifyEnabled() )",
                              AREZ_CLASSNAME,
                              AREZ_CLASSNAME );

      block.addStatement( "$T.apiInvariant( () -> this == $N().findById( $T.class, $N() ), () -> \"Attempted to " +
                          "lookup self in Locator with type $T and id '\" + $N() + \"' but unable to locate " +
                          "self. Actual value: \" + $N().findById( $T.class, $N() ) )",
                          GUARDS_CLASSNAME,
                          LOCATOR_METHOD_NAME,
                          component.getElement(),
                          component.getIdMethodName(),
                          component.getElement(),
                          component.getIdMethodName(),
                          LOCATOR_METHOD_NAME,
                          component.getElement(),
                          component.getIdMethodName() );
      for ( final ReferenceDescriptor reference : component.getReferences().values() )
      {
        buildReferenceVerify( reference, block );
      }

      for ( final InverseDescriptor inverse : component.getInverses().values() )
      {
        buildInverseVerify( inverse, block );
      }

      block.endControlFlow();
      method.addCode( block.build() );
    }
    return method.build();
  }

  @Nonnull
  private static MethodSpec buildLink( @Nonnull final ComponentDescriptor component )
  {
    final String methodName = "link";
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( methodName ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class );

    generateNotInitializedInvariant( component, method, methodName );
    generateNotConstructedInvariant( method, methodName );

    generateNotDisposedInvariant( method, methodName );

    final List<ReferenceDescriptor> explicitReferences =
      component.getReferences().values()
        .stream()
        .filter( r -> r.getLinkType().equals( "EXPLICIT" ) )
        .toList();
    for ( final ReferenceDescriptor reference : explicitReferences )
    {
      method.addStatement( "this.$N()", reference.getLinkMethodName() );
    }
    return method.build();
  }

  /**
   * Generate the dispose method.
   */
  @Nonnull
  private static MethodSpec buildDispose( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String methodName = "dispose";
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( methodName ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class );

    generateNotInitializedInvariant( component, method, methodName );
    generateNotConstructedInvariant( method, methodName );

    method.addStatement( "this.$N.dispose()", KERNEL_FIELD_NAME );

    return method.build();
  }

  @Nonnull
  private static MethodSpec buildInternalDispose( @Nonnull final ComponentDescriptor componentDescriptor )
    throws ProcessorException
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( INTERNAL_DISPOSE_METHOD_NAME ).
        addModifiers( Modifier.PRIVATE );

    componentDescriptor.getObserves().values().forEach( observe -> buildObserveDisposer( observe,
                                                                                         builder ) );
    componentDescriptor.getMemoizes().values().forEach( memoize -> buildMemoizeDisposer( memoize,
                                                                                         builder ) );
    componentDescriptor.getObservables().values().forEach( observable -> buildObservableDisposer( observable,
                                                                                                  builder ) );

    return builder.build();
  }

  private static boolean hasInternalPreDispose( @Nonnull final ComponentDescriptor component )
  {
    return component.getPreDisposes().size() > 1 ||
           !component.getInverses().isEmpty() ||
           !component.getCascadeDisposes().isEmpty() ||
           ( component.isDisposeNotifier() && !component.getDependencies().isEmpty() ) ||
           component.getReferences().values().stream().anyMatch( ReferenceDescriptor::hasInverse );
  }

  /**
   * Generate the isDisposed method.
   */
  @Nonnull
  private static MethodSpec buildIsDisposed( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String methodName = "isDisposed";
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( methodName ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        returns( TypeName.BOOLEAN );

    generateNotInitializedInvariant( component, method, methodName );
    generateNotConstructedInvariant( method, methodName );

    method.addStatement( "return this.$N.isDisposed()", KERNEL_FIELD_NAME );
    return method.build();
  }

  /**
   * Generate the observe method.
   */
  @Nonnull
  private static MethodSpec buildObserve( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String methodName = "observe";
    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( methodName ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        returns( TypeName.BOOLEAN );
    generateNotInitializedInvariant( component, method, methodName );
    generateNotConstructedInvariant( method, methodName );

    method.addStatement( "return this.$N.observe()", KERNEL_FIELD_NAME );
    return method.build();
  }

  /**
   * Generate the preDispose method only used when native components are enabled.
   */
  @Nonnull
  private static MethodSpec buildNativeComponentPreDispose( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( INTERNAL_NATIVE_COMPONENT_PRE_DISPOSE_METHOD_NAME ).
        addModifiers( Modifier.PRIVATE );

    if ( hasInternalPreDispose( component ) )
    {
      builder.addStatement( "this.$N()", INTERNAL_PRE_DISPOSE_METHOD_NAME );
    }
    else
    {
      final List<ExecutableElement> preDisposes = new ArrayList<>( component.getPreDisposes() );
      Collections.reverse( preDisposes );
      for ( final ExecutableElement preDispose : preDisposes )
      {
        if ( component.isClassType() )
        {
          builder.addStatement( "super.$N()", preDispose.getSimpleName() );
        }
        else
        {
          builder.addStatement( "$T.super.$N()", component.getClassName(), preDispose.getSimpleName() );
        }
      }
    }
    if ( component.isDisposeNotifier() )
    {
      builder.addStatement( "this.$N.notifyOnDisposeListeners()", KERNEL_FIELD_NAME );
    }

    return builder.build();
  }

  /**
   * Generate the preDispose method.
   */
  @Nonnull
  private static MethodSpec buildInternalPreDispose( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    assert hasInternalPreDispose( component );
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( INTERNAL_PRE_DISPOSE_METHOD_NAME ).
        addModifiers( Modifier.PRIVATE );

    final List<ExecutableElement> preDisposes = new ArrayList<>( component.getPreDisposes() );
    Collections.reverse( preDisposes );
    for ( final ExecutableElement preDispose : preDisposes )
    {
      if ( component.isClassType() )
      {
        builder.addStatement( "super.$N()", preDispose.getSimpleName() );
      }
      else
      {
        builder.addStatement( "$T.super.$N()", component.getClassName(), preDispose.getSimpleName() );
      }
    }
    component.getCascadeDisposes().values().forEach( r -> buildCascadeDisposeDisposer( r, builder ) );
    component.getReferences().values().forEach( r -> buildReferenceDisposer( r, builder ) );
    component.getInverses().values().forEach( r -> buildInverseDisposer( r, builder ) );
    if ( component.isDisposeNotifier() )
    {
      for ( final DependencyDescriptor dependency : component.getDependencies().values() )
      {
        final Element element = dependency.getElement();

        if ( dependency.isMethodDependency() )
        {
          final ExecutableElement method = dependency.getMethod();
          final String methodName = method.getSimpleName().toString();
          if ( AnnotationsUtil.hasNonnullAnnotation( element ) )
          {
            final boolean abstractObservables = method.getModifiers().contains( Modifier.ABSTRACT );
            if ( abstractObservables )
            {
              builder.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( $N, true )",
                                    DISPOSE_TRACKABLE_CLASSNAME,
                                    dependency.getObservable().getDataFieldName(),
                                    getDependencyKey( dependency ) );
            }
            else
            {
              builder.addStatement( "$T.asDisposeNotifier( $N() ).removeOnDisposeListener( $N, true )",
                                    DISPOSE_TRACKABLE_CLASSNAME,
                                    methodName,
                                    getDependencyKey( dependency ) );
            }
          }
          else
          {
            final String varName = VARIABLE_PREFIX + methodName + "_dependency";
            final boolean abstractObservables = method.getModifiers().contains( Modifier.ABSTRACT );
            if ( abstractObservables )
            {
              builder.addStatement( "final $T $N = this.$N",
                                    method.getReturnType(),
                                    varName,
                                    dependency.getObservable().getDataFieldName() );
            }
            else
            {
              if ( component.isClassType() )
              {
                builder.addStatement( "final $T $N = super.$N()", method.getReturnType(), varName, methodName );
              }
              else
              {
                builder.addStatement( "final $T $N = $T.super.$N()",
                                      method.getReturnType(),
                                      varName,
                                      component.getClassName(),
                                      methodName );
              }
            }
            final CodeBlock.Builder listenerBlock = CodeBlock.builder();
            listenerBlock.beginControlFlow( "if ( null != $N )", varName );
            listenerBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( $N, true )",
                                        DISPOSE_TRACKABLE_CLASSNAME,
                                        varName,
                                        getDependencyKey( dependency ) );
            listenerBlock.endControlFlow();
            builder.addCode( listenerBlock.build() );
          }
        }
        else
        {
          final VariableElement field = dependency.getField();
          final String fieldName = field.getSimpleName().toString();
          if ( AnnotationsUtil.hasNonnullAnnotation( element ) )
          {
            builder.addStatement( "$T.asDisposeNotifier( this.$N ).removeOnDisposeListener( $N, true )",
                                  DISPOSE_TRACKABLE_CLASSNAME,
                                  fieldName,
                                  getDependencyKey( dependency ) );
          }
          else
          {
            final CodeBlock.Builder listenerBlock = CodeBlock.builder();
            listenerBlock.beginControlFlow( "if ( null != this.$N )", fieldName );
            listenerBlock.addStatement( "$T.asDisposeNotifier( this.$N ).removeOnDisposeListener( $N, true )",
                                        DISPOSE_TRACKABLE_CLASSNAME,
                                        fieldName,
                                        getDependencyKey( dependency ) );
            listenerBlock.endControlFlow();
            builder.addCode( listenerBlock.build() );
          }
        }
      }
    }

    return builder.build();
  }

  @Nonnull
  private static MethodSpec buildInternalPostDispose( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    assert component.hasInternalPostDispose();
    final MethodSpec.Builder builder =
      MethodSpec
        .methodBuilder( INTERNAL_POST_DISPOSE_METHOD_NAME )
        .addModifiers( Modifier.PRIVATE );

    final List<ExecutableElement> postDisposes = new ArrayList<>( component.getPostDisposes() );
    Collections.reverse( postDisposes );
    for ( final ExecutableElement postDispose : postDisposes )
    {
      if ( component.isClassType() )
      {
        builder.addStatement( "super.$N()", postDispose.getSimpleName() );
      }
      else
      {
        builder.addStatement( "$T.super.$N()", component.getClassName(), postDispose.getSimpleName() );
      }
    }
    return builder.build();
  }

  /**
   * Generate the addOnDisposeListener method.
   */
  @Nonnull
  private static MethodSpec buildAddOnDisposeListener( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String methodName = "addOnDisposeListener";
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName ).
      addModifiers( Modifier.PUBLIC ).
      addAnnotation( Override.class ).
      addParameter( ParameterSpec.builder( TypeName.OBJECT, "key", Modifier.FINAL )
                      .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                      .build() ).
      addParameter( ParameterSpec.builder( SAFE_PROCEDURE_CLASSNAME, "action", Modifier.FINAL )
                      .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                      .build() ).
      addParameter( ParameterSpec.builder( TypeName.BOOLEAN, "errorIfDuplicate", Modifier.FINAL ).build() );
    generateNotInitializedInvariant( component, method, methodName );
    method.addStatement( "this.$N.addOnDisposeListener( key, action, errorIfDuplicate )", KERNEL_FIELD_NAME );
    return method.build();
  }

  /**
   * Generate the removeOnDisposeListener method.
   */
  @Nonnull
  private static MethodSpec buildRemoveOnDisposeListener( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String methodName = "removeOnDisposeListener";
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName ).
      addModifiers( Modifier.PUBLIC ).
      addAnnotation( Override.class ).
      addParameter( ParameterSpec.builder( TypeName.OBJECT, "key", Modifier.FINAL )
                      .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                      .build() ).
      addParameter( ParameterSpec.builder( TypeName.BOOLEAN, "errorIfMissing", Modifier.FINAL ).build() );
    generateNotInitializedInvariant( component, method, methodName );
    generateNotConstructedInvariant( method, methodName );
    method.addStatement( "this.$N.removeOnDisposeListener( key, errorIfMissing )", KERNEL_FIELD_NAME );
    return method.build();
  }

  private static void buildComponentKernel( @Nonnull final ProcessingEnvironment processingEnv,
                                            @Nonnull final ComponentDescriptor component,
                                            @Nonnull final MethodSpec.Builder builder )
  {
    buildContextVar( builder );
    buildSyntheticIdVarIfRequired( processingEnv, component, builder );
    buildNameVar( component, builder );
    buildNativeComponentVar( component, builder );

    final StringBuilder sb = new StringBuilder();
    final List<Object> params = new ArrayList<>();

    sb.append( "this.$N = new $T( $T.areZonesEnabled() ? $N : null, $T.areNamesEnabled() ? $N : null, " );
    params.add( KERNEL_FIELD_NAME );
    params.add( KERNEL_CLASSNAME );
    params.add( AREZ_CLASSNAME );
    params.add( CONTEXT_VAR_NAME );
    params.add( AREZ_CLASSNAME );
    params.add( NAME_VAR_NAME );
    if ( null == component.getComponentId() )
    {
      sb.append( "$N, " );
      params.add( ID_VAR_NAME );
    }
    else
    {
      sb.append( "0, " );
    }
    sb.append( "$T.areNativeComponentsEnabled() ? $N : null, " );
    params.add( AREZ_CLASSNAME );
    params.add( COMPONENT_VAR_NAME );

    if ( hasInternalPreDispose( component ) )
    {
      sb.append( "$T.areNativeComponentsEnabled() ? null : this::$N, " );
      params.add( AREZ_CLASSNAME );
      params.add( INTERNAL_PRE_DISPOSE_METHOD_NAME );
    }
    else if ( !component.getPreDisposes().isEmpty() )
    {
      if ( component.isClassType() )
      {
        sb.append( "$T.areNativeComponentsEnabled() ? null : () -> super.$N(), " );
        params.add( AREZ_CLASSNAME );
        params.add( component.getPreDisposes().get( 0 ).getSimpleName() );
      }
      else
      {
        sb.append( "$T.areNativeComponentsEnabled() ? null : () -> $T.super.$N(), " );
        params.add( AREZ_CLASSNAME );
        params.add( component.getClassName() );
        params.add( component.getPreDisposes().get( 0 ).getSimpleName() );
      }
    }
    else
    {
      sb.append( "null, " );
    }
    if ( component.needsInternalDispose() )
    {
      sb.append( "$T.areNativeComponentsEnabled() ? null : this::$N, " );
      params.add( AREZ_CLASSNAME );
      params.add( INTERNAL_DISPOSE_METHOD_NAME );
    }
    else
    {
      sb.append( "null, " );
    }

    if ( component.getPostDisposes().isEmpty() )
    {
      sb.append( "null, " );
    }
    else if ( 1 == component.getPostDisposes().size() )
    {
      if ( component.isClassType() )
      {
        sb.append( "$T.areNativeComponentsEnabled() ? null : () -> super.$N(), " );
        params.add( AREZ_CLASSNAME );
        params.add( component.getPostDisposes().get( 0 ).getSimpleName() );
      }
      else
      {
        sb.append( "$T.areNativeComponentsEnabled() ? null : () -> $T.super.$N(), " );
        params.add( AREZ_CLASSNAME );
        params.add( component.getClassName() );
        params.add( component.getPostDisposes().get( 0 ).getSimpleName() );
      }
    }
    else
    {
      sb.append( "$T.areNativeComponentsEnabled() ? null : this::$N, " );
      params.add( AREZ_CLASSNAME );
      params.add( INTERNAL_POST_DISPOSE_METHOD_NAME );
    }

    sb.append( component.isDisposeNotifier() );
    sb.append( ", " );
    sb.append( component.isObservable() );
    sb.append( ", " );
    sb.append( component.isDisposeOnDeactivate() );
    sb.append( " )" );

    builder.addStatement( sb.toString(), params.toArray() );
  }

  private static void buildContextVar( @Nonnull final MethodSpec.Builder builder )
  {
    builder.addStatement( "final $T $N = $T.context()",
                          AREZ_CONTEXT_CLASSNAME,
                          CONTEXT_VAR_NAME,
                          AREZ_CLASSNAME );
  }

  private static void buildNameVar( @Nonnull final ComponentDescriptor component,
                                    @Nonnull final MethodSpec.Builder builder )
  {
    builder.addStatement( "final String $N = $T.areNamesEnabled() ? $S + $N : null",
                          NAME_VAR_NAME,
                          AREZ_CLASSNAME,
                          component.getName() + ".",
                          ID_VAR_NAME );
  }

  private static void buildSyntheticIdVarIfRequired( @Nonnull final ProcessingEnvironment processingEnv,
                                                     @Nonnull final ComponentDescriptor component,
                                                     @Nonnull final MethodSpec.Builder builder )
  {
    if ( null == component.getComponentId() )
    {
      if ( component.isIdRequired() )
      {
        builder.addStatement( "final int $N = ++$N", ID_VAR_NAME, NEXT_ID_FIELD_NAME );
      }
      else
      {
        builder.addStatement( "final int $N = ( $T.areNamesEnabled() || $T.areRegistriesEnabled() || " +
                              "$T.areNativeComponentsEnabled() ) ? ++$N : 0",
                              ID_VAR_NAME,
                              AREZ_CLASSNAME,
                              AREZ_CLASSNAME,
                              AREZ_CLASSNAME,
                              NEXT_ID_FIELD_NAME );
      }
    }
    else
    {
      final ExecutableType methodType =
        (ExecutableType) processingEnv.getTypeUtils().asMemberOf( (DeclaredType) component.getElement().asType(),
                                                                  component.getComponentId() );
      builder.addStatement( "final $T $N = $N()",
                            methodType.getReturnType(),
                            ID_VAR_NAME,
                            component.getComponentId().getSimpleName() );
    }
  }

  private static void buildNativeComponentVar( @Nonnull final ComponentDescriptor component,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    final StringBuilder sb = new StringBuilder();
    final List<Object> params = new ArrayList<>();
    sb.append( "final $T $N = $T.areNativeComponentsEnabled() ? $N.component( $S, $N, $N" );
    params.add( COMPONENT_CLASSNAME );
    params.add( COMPONENT_VAR_NAME );
    params.add( AREZ_CLASSNAME );
    params.add( CONTEXT_VAR_NAME );
    params.add( component.getName() );
    params.add( ID_VAR_NAME );
    params.add( NAME_VAR_NAME );
    final boolean hasInternalPreDispose = hasInternalPreDispose( component );
    if ( hasInternalPreDispose ||
         component.isDisposeNotifier() ||
         !component.getPreDisposes().isEmpty() ||
         !component.getPostDisposes().isEmpty() )
    {
      sb.append( ", " );
      if ( hasInternalPreDispose || component.isDisposeNotifier() )
      {
        sb.append( "this::$N" );
        params.add( INTERNAL_NATIVE_COMPONENT_PRE_DISPOSE_METHOD_NAME );
      }
      else if ( 1 == component.getPreDisposes().size() )
      {
        if ( component.isClassType() )
        {
          sb.append( "() -> super.$N()" );
          params.add( component.getPreDisposes().get( 0 ).getSimpleName().toString() );
        }
        else
        {
          sb.append( "() -> $T.super.$N()" );
          params.add( component.getClassName() );
          params.add( component.getPreDisposes().get( 0 ).getSimpleName().toString() );
        }
      }
      else
      {
        sb.append( "null" );
      }
      if ( component.getPostDisposes().size() > 1 )
      {
        sb.append( ",  this::$N" );
        params.add( INTERNAL_POST_DISPOSE_METHOD_NAME );
      }
      else if ( 1 == component.getPostDisposes().size() )
      {
        if ( component.isClassType() )
        {
          sb.append( ",  () -> super.$N()" );
          params.add( component.getPostDisposes().get( 0 ).getSimpleName().toString() );
        }
        else
        {
          sb.append( ",  () -> $T.super.$N()" );
          params.add( component.getClassName() );
          params.add( component.getPostDisposes().get( 0 ).getSimpleName().toString() );
        }
      }
    }
    sb.append( " ) : null" );
    builder.addStatement( sb.toString(), params.toArray() );
  }

  /**
   * Build all constructors as they appear on the ArezComponent class.
   * Arez Observable fields are populated as required and parameters are passed up to superclass.
   */
  private static void buildConstructors( @Nonnull final ProcessingEnvironment processingEnv,
                                         @Nonnull final ComponentDescriptor component,
                                         @Nonnull final TypeSpec.Builder builder )
  {
    for ( final ExecutableElement constructor : ElementsUtil.getConstructors( component.getElement() ) )
    {
      final ExecutableType methodType =
        (ExecutableType) processingEnv.getTypeUtils()
          .asMemberOf( (DeclaredType) component.getElement().asType(), constructor );
      builder.addMethod( buildConstructor( processingEnv, component, constructor, methodType ) );
    }
  }

  /**
   * Build a constructor based on the supplied constructor.
   */
  @Nonnull
  private static MethodSpec buildConstructor( @Nonnull final ProcessingEnvironment processingEnv,
                                              @Nonnull final ComponentDescriptor component,
                                              @Nullable final ExecutableElement constructor,
                                              @Nullable final ExecutableType constructorType )
  {
    final MethodSpec.Builder builder = MethodSpec.constructorBuilder();
    if ( component.shouldGeneratedClassBePublic( processingEnv ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    if ( null != constructorType )
    {
      GeneratorUtil.copyExceptions( constructorType, builder );
      GeneratorUtil.copyTypeParameters( constructorType, builder );
    }
    final List<ObservableDescriptor> initializers = component.getInitializers();

    final List<String> additionalSuppressions =
      component.hasDeprecatedElements() ? Collections.singletonList( "deprecation" ) : Collections.emptyList();
    final List<TypeMirror> types = new ArrayList<>();
    final ExecutableElement componentId = component.getComponentId();
    if ( null != componentId )
    {
      types.add( processingEnv.getTypeUtils().asMemberOf( component.asDeclaredType(), componentId ) );
    }
    if ( null != constructor )
    {
      types.add( processingEnv.getTypeUtils().asMemberOf( component.asDeclaredType(), constructor ) );
    }
    for ( final ObservableDescriptor initializer : initializers )
    {
      types.add( initializer.getGetterType() );
    }
    for ( final MemoizeDescriptor memoize : component.getMemoizes().values() )
    {
      types.addAll( memoize.getMethodType().getParameterTypes() );
    }
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, builder, additionalSuppressions, types );

    final StringBuilder superCall = new StringBuilder();
    superCall.append( "super(" );
    final List<String> parameterNames = new ArrayList<>();

    boolean firstParam = true;
    if ( null != constructor )
    {
      for ( final VariableElement element : constructor.getParameters() )
      {
        final ParameterSpec.Builder param =
          ParameterSpec.builder( TypeName.get( element.asType() ), element.getSimpleName().toString(), Modifier.FINAL );
        final ArrayList<String> whitelist = new ArrayList<>( GeneratorUtil.ANNOTATION_WHITELIST );
        whitelist.add( Constants.STING_NAMED );
        GeneratorUtil.copyWhitelistedAnnotations( element, param, whitelist );
        builder.addParameter( param.build() );
        parameterNames.add( element.getSimpleName().toString() );
        if ( !firstParam )
        {
          superCall.append( "," );
        }
        firstParam = false;
        superCall.append( "$N" );
      }
    }

    superCall.append( ")" );
    builder.addStatement( superCall.toString(), parameterNames.toArray() );

    if ( !component.getReferences().isEmpty() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
      block.addStatement( "$T.apiInvariant( $T::areReferencesEnabled, () -> \"Attempted to create instance " +
                          "of component of type '$N' that contains references but Arez.areReferencesEnabled() " +
                          "returns false. References need to be enabled to use this component\" )",
                          GUARDS_CLASSNAME,
                          AREZ_CLASSNAME,
                          component.getName() );
      block.endControlFlow();
      builder.addCode( block.build() );
    }

    buildComponentKernel( processingEnv, component, builder );

    for ( final ObservableDescriptor observable : initializers )
    {
      final String candidateName = observable.getName();
      final String name =
        null != constructor && anyParametersNamed( constructor, candidateName ) ?
        INITIALIZER_PREFIX + candidateName :
        candidateName;
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( observable.getGetterType().getReturnType() ),
                               name,
                               Modifier.FINAL );
      GeneratorUtil.copyWhitelistedAnnotations( observable.getGetter(), param );
      builder.addParameter( param.build() );
      final boolean isPrimitive = TypeName.get( observable.getGetterType().getReturnType() ).isPrimitive();
      if ( isPrimitive )
      {
        builder.addStatement( "this.$N = $N", observable.getDataFieldName(), name );
      }
      else if ( observable.isGetterNonnull() )
      {
        builder.addStatement( "this.$N = $T.requireNonNull( $N )",
                              observable.getDataFieldName(),
                              Objects.class,
                              name );
      }
      else
      {
        builder.addStatement( "this.$N = $N", observable.getDataFieldName(), name );
      }
    }

    for ( final ObservableDescriptor observable : component.getObservables().values() )
    {
      if ( observable.hasObservableInitial() )
      {
        final ObservableInitialDescriptor observableInitial = observable.getObservableInitial();
        final CodeBlock initializer =
          observableInitial.isField() ?
          CodeBlock.of( "$T.$N", component.getClassName(), observableInitial.getField().getSimpleName() ) :
          CodeBlock.of( "$T.$N()", component.getClassName(), observableInitial.getMethod().getSimpleName() );
        final boolean isPrimitive = TypeName.get( observable.getGetterType().getReturnType() ).isPrimitive();
        if ( isPrimitive )
        {
          builder.addStatement( "this.$N = $L", observable.getDataFieldName(), initializer );
        }
        else if ( observable.isGetterNonnull() )
        {
          builder.addStatement( "this.$N = $T.requireNonNull( $L )",
                                observable.getDataFieldName(),
                                Objects.class,
                                initializer );
        }
        else
        {
          builder.addStatement( "this.$N = $L", observable.getDataFieldName(), initializer );
        }
      }
    }

    component.getObservables().values().forEach( observable -> buildObservableInitializer( observable, builder ) );
    component.getMemoizes().values().forEach( memoize -> buildMemoizeInitializer( memoize, builder ) );
    component.getObserves().values().forEach( observe -> buildObserveInitializer( observe, builder ) );
    component.getInverses().values().forEach( e -> buildInverseInitializer( e, builder ) );
    component.getDependencies().values().forEach( e -> buildDependencyKeyInitializer( e, builder ) );
    component.getDependencies().values().forEach( e -> buildDependencyInitializer( e, builder ) );

    builder.addStatement( "this.$N.componentConstructed()", KERNEL_FIELD_NAME );

    final List<ReferenceDescriptor> eagerReferences =
      component
        .getReferences()
        .values()
        .stream()
        .filter( r -> r.getLinkType().equals( "EAGER" ) )
        .toList();
    for ( final ReferenceDescriptor reference : eagerReferences )
    {
      builder.addStatement( "this.$N()", reference.getLinkMethodName() );
    }

    if ( !component.getPostConstructs().isEmpty() )
    {
      for ( final ExecutableElement postConstruct : component.getPostConstructs() )
      {
        if ( component.isClassType() )
        {
          if ( AnnotationsUtil.hasAnnotationOfType( postConstruct, Constants.ACTION_CLASSNAME ) )
          {
            builder.addStatement( "this.$N()", postConstruct.getSimpleName().toString() );
          }
          else
          {
            builder.addStatement( "super.$N()", postConstruct.getSimpleName().toString() );
          }
        }
        else
        {
          builder.addStatement( "$T.super.$N()", component.getClassName(), postConstruct.getSimpleName().toString() );
        }
      }
    }

    if ( component.requiresSchedule() )
    {
      builder.addStatement( "this.$N.componentComplete()", KERNEL_FIELD_NAME );
    }
    else
    {
      builder.addStatement( "this.$N.componentReady()", KERNEL_FIELD_NAME );
    }
    return builder.build();
  }

  /**
   * Build the fields required to make class Observable. This involves;
   * <ul>
   * <li>the context field if there is any @Action methods.</li>
   * <li>the observable object for every @Observable.</li>
   * <li>the ComputableValue object for every @Memoize method.</li>
   * </ul>
   */
  private static void buildFields( @Nonnull final ProcessingEnvironment processingEnv,
                                   @Nonnull final ComponentDescriptor component,
                                   @Nonnull final TypeSpec.Builder builder )
  {

    final FieldSpec.Builder idField =
      FieldSpec.builder( KERNEL_CLASSNAME,
                         KERNEL_FIELD_NAME,
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NULLABLE_CLASSNAME );
    builder.addField( idField.build() );

    // If we don't have a method for object id but we need one then synthesize it
    if ( null == component.getComponentId() )
    {
      final FieldSpec.Builder nextIdField =
        FieldSpec.builder( DEFAULT_ID_TYPE,
                           NEXT_ID_FIELD_NAME,
                           Modifier.VOLATILE,
                           Modifier.STATIC,
                           Modifier.PRIVATE );
      builder.addField( nextIdField.build() );
    }

    component.getObservables()
      .values()
      .forEach( observable -> buildObservableFields( processingEnv, observable, builder ) );
    component.getMemoizes().values().forEach( memoize -> buildMemoizeFields( processingEnv, memoize, builder ) );
    component.getObserves().values().forEach( observe -> buildObserveFields( observe, builder ) );
    component.getReferences().values().forEach( r -> buildReferenceFields( r, builder ) );
    component.getDependencies().values().forEach( e -> buildDependencyKeyField( e, builder ) );
  }

  @Nonnull
  private static MethodSpec buildEqualsMethod( @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    final String idMethod = component.getIdMethodName();

    final MethodSpec.Builder method =
      MethodSpec.methodBuilder( "equals" ).
        addModifiers( Modifier.PUBLIC ).
        addAnnotation( Override.class ).
        addParameter( Object.class, "o", Modifier.FINAL ).
        returns( TypeName.BOOLEAN );

    final ClassName generatedClass = component.getEnhancedClassName();
    final List<? extends TypeParameterElement> typeParameters = component.getElement().getTypeParameters();

    if ( !typeParameters.isEmpty() )
    {
      method.addAnnotation( SuppressWarningsUtil.suppressWarningsAnnotation( "unchecked" ) );
    }

    final TypeName typeName =
      typeParameters.isEmpty() ?
      generatedClass :
      ParameterizedTypeName.get( generatedClass,
                                 typeParameters.stream().map( TypeVariableName::get ).toArray( TypeName[]::new ) );

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    codeBlock.beginControlFlow( "if ( o instanceof $T )", generatedClass );
    codeBlock.addStatement( "final $T that = ($T) o", typeName, typeName );
    /*
     * If componentId is null then it is using synthetic id which is monotonically increasing and
     * thus if the id matches then the instance match. As a result no need to check isDisposed as
     * they will always match. Whereas if componentId is not null then the application controls the
     * id and there maybe be multiple entities with the same id where one has been disposed. They
     * should not match.
     */
    final String prefix = null != component.getComponentId() ? "isDisposed() == that.isDisposed() && " : "";
    final TypeKind kind =
      null != component.getComponentId() ? component.getComponentId().getReturnType().getKind() : DEFAULT_ID_KIND;
    if ( kind == TypeKind.DECLARED || kind == TypeKind.TYPEVAR )
    {
      codeBlock.addStatement( "return " + prefix + "null != $N() && $N().equals( that.$N() )",
                              idMethod,
                              idMethod,
                              idMethod );
    }
    else
    {
      codeBlock.addStatement( "return " + prefix + "$N() == that.$N()",
                              idMethod,
                              idMethod );
    }
    codeBlock.nextControlFlow( "else" );
    codeBlock.addStatement( "return false" );
    codeBlock.endControlFlow();

    method.addCode( codeBlock.build() );
    return method.build();
  }

  /**
   * Build any fields required by ObserveDescriptor.
   */
  private static void buildObserveFields( @Nonnull final ObserveDescriptor observe,
                                          @Nonnull final TypeSpec.Builder builder )
  {
    final FieldSpec.Builder field =
      FieldSpec.builder( OBSERVER_CLASSNAME, observe.getFieldName(), Modifier.FINAL, Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    builder.addField( field.build() );
  }

  /**
   * Setup initial state of observed in constructor.
   */
  private static void buildObserveInitializer( @Nonnull final ObserveDescriptor observe,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    if ( observe.isInternalExecutor() )
    {
      buildObserverInitializer( observe, builder );
    }
    else
    {
      buildTrackerInitializer( observe, builder );
    }
  }

  private static void buildObserverInitializer( @Nonnull final ObserveDescriptor observe,
                                                @Nonnull final MethodSpec.Builder builder )
  {
    final ComponentDescriptor component = observe.getComponent();
    final List<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N.observer( $T.areNativeComponentsEnabled() ? $N : null, " +
               "$T.areNamesEnabled() ? $N + $S : null, " );
    parameters.add( observe.getFieldName() );
    parameters.add( CONTEXT_VAR_NAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( COMPONENT_VAR_NAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( NAME_VAR_NAME );
    parameters.add( "." + observe.getName() );
    if ( component.isClassType() )
    {
      sb.append( "() -> super.$N(), " );
      parameters.add( observe.getMethod().getSimpleName().toString() );
    }
    else
    {
      sb.append( "() -> $T.super.$N(), " );
      parameters.add( component.getClassName() );
      parameters.add( observe.getMethod().getSimpleName().toString() );
    }
    if ( observe.hasOnDepsChange() )
    {
      final ExecutableElement onDepsChange = observe.getOnDepsChange();
      if ( !onDepsChange.getParameters().isEmpty() )
      {
        sb.append( "this::$N, " );
        parameters.add( FRAMEWORK_PREFIX + onDepsChange.getSimpleName() );
      }
      else if ( component.isClassType() )
      {
        sb.append( "() -> super.$N(), " );
        parameters.add( onDepsChange.getSimpleName().toString() );
      }
      else
      {
        sb.append( "() -> $T.super.$N(), " );
        parameters.add( component.getClassName() );
        parameters.add( onDepsChange.getSimpleName().toString() );
      }
    }

    appendFlags( observe, parameters, sb );

    sb.append( " )" );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private static void buildTrackerInitializer( @Nonnull final ObserveDescriptor observe,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    assert observe.hasOnDepsChange();
    final ComponentDescriptor component = observe.getComponent();
    final List<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N.tracker( " +
               "$T.areNativeComponentsEnabled() ? $N : null, " +
               "$T.areNamesEnabled() ? $N + $S : null, " );
    parameters.add( observe.getFieldName() );
    parameters.add( CONTEXT_VAR_NAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( COMPONENT_VAR_NAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( NAME_VAR_NAME );
    parameters.add( "." + observe.getName() );

    final ExecutableElement onDepsChange = observe.getOnDepsChange();
    if ( !onDepsChange.getParameters().isEmpty() )
    {
      sb.append( "this::$N, " );
      parameters.add( FRAMEWORK_PREFIX + onDepsChange.getSimpleName() );
    }
    else if ( component.isClassType() )
    {
      sb.append( "() -> super.$N(), " );
      parameters.add( onDepsChange.getSimpleName().toString() );
    }
    else
    {
      sb.append( "() -> $T.super.$N(), " );
      parameters.add( component.getClassName() );
      parameters.add( onDepsChange.getSimpleName().toString() );
    }
    appendFlags( observe, parameters, sb );

    sb.append( " )" );

    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private static void appendFlags( @Nonnull final ObserveDescriptor observe,
                                   @Nonnull final List<Object> parameters,
                                   @Nonnull final StringBuilder expression )
  {
    final List<String> flags = new ArrayList<>();
    flags.add( "RUN_LATER" );

    if ( observe.isObserveLowerPriorityDependencies() )
    {
      flags.add( "OBSERVE_LOWER_PRIORITY_DEPENDENCIES" );
    }
    if ( !observe.isReportResult() )
    {
      flags.add( "NO_REPORT_RESULT" );
    }
    if ( observe.isNestedActionsAllowed() )
    {
      flags.add( "NESTED_ACTIONS_ALLOWED" );
    }
    else
    {
      flags.add( "NESTED_ACTIONS_DISALLOWED" );
    }
    switch ( observe.getDepType() )
    {
      case "AREZ":
        flags.add( "AREZ_DEPENDENCIES" );
        break;
      case "AREZ_OR_NONE":
        flags.add( "AREZ_OR_NO_DEPENDENCIES" );
        break;
      default:
        flags.add( "AREZ_OR_EXTERNAL_DEPENDENCIES" );
        break;
    }
    if ( observe.isMutation() )
    {
      flags.add( "READ_WRITE" );
    }
    if ( Priority.NORMAL != observe.getPriority() )
    {
      flags.add( "PRIORITY_" + observe.getPriority().name() );
    }

    expression.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( OBSERVER_FLAGS_CLASSNAME );
    }
  }

  private static void buildMemoizeDisposer( @Nonnull final MemoizeDescriptor memoize,
                                            @Nonnull final MethodSpec.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", getMemoizeFieldName( memoize ) );
  }

  private static void buildMemoizeMethods( @Nonnull final ProcessingEnvironment processingEnv,
                                           @Nonnull final MemoizeDescriptor memoize,
                                           @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    if ( memoize.shouldGenerateMemoizeWrapper() )
    {
      builder.addMethod( buildMemoizeAdapterMethod( processingEnv, memoize ) );
    }
    if ( memoize.shouldGenerateDeactivateWrapperHook() )
    {
      builder.addMethod( buildOnDeactivateWrapperHook( memoize ) );
    }

    if ( memoize.hasNoParameters() )
    {
      builder.addMethod( buildMemoizeWithoutParams( processingEnv, memoize ) );
      final ExecutableElement onActivate = memoize.getOnActivate();
      if ( ( null != onActivate && !onActivate.getParameters().isEmpty() ) || memoize.isCollectionType() )
      {
        builder.addMethod( buildOnActivateWrapperHook( memoize ) );
      }

      for ( final CandidateMethod refMethod : memoize.getRefMethods() )
      {
        final MethodSpec.Builder method =
          refMethod( processingEnv, memoize.getComponent().getElement(), refMethod.getMethod() );
        generateNotDisposedInvariant( method, refMethod.getMethod().getSimpleName().toString() );
        builder.addMethod( method
                             .addStatement( "return $N", getMemoizeFieldName( memoize ) )
                             .build() );
      }
    }
    else
    {
      builder.addMethod( buildMemoizeWithParams( processingEnv, memoize ) );
      for ( final CandidateMethod refMethod : memoize.getRefMethods() )
      {
        builder.addMethod( buildMemoizeWithParamsComputableValueRef( processingEnv, memoize, refMethod ) );
      }
    }
  }

  @Nonnull
  private static MethodSpec buildOnActivateWrapperHook( @Nonnull final MemoizeDescriptor memoize )
    throws ProcessorException
  {
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getOnActivateHookMethodName( memoize ) );
    builder.addModifiers( Modifier.PRIVATE );

    if ( memoize.isCollectionType() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", AREZ_CLASSNAME );
      block.addStatement( "this.$N = true", getMemoizeCollectionCacheDataActiveFieldName( memoize ) );
      block.addStatement( "this.$N = null", getMemoizeCollectionCacheDataFieldName( memoize ) );
      block.addStatement( "this.$N = null", getMemoizeCollectionUnmodifiableCacheDataFieldName( memoize ) );
      block.endControlFlow();
      builder.addCode( block.build() );
    }

    final ExecutableElement onActivate = memoize.getOnActivate();
    if ( null != onActivate )
    {
      if ( onActivate.getParameters().isEmpty() )
      {
        builder.addStatement( "$N()", onActivate.getSimpleName().toString() );
      }
      else
      {
        builder.addStatement( "$N( $N )", onActivate.getSimpleName().toString(), getMemoizeFieldName( memoize ) );
      }
    }
    return builder.build();
  }

  @Nonnull
  private static MethodSpec buildOnDeactivateWrapperHook( @Nonnull final MemoizeDescriptor memoize )
    throws ProcessorException
  {
    assert memoize.shouldGenerateDeactivateWrapperHook();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( getOnDeactivateHookMethodName( memoize ) );
    builder.addModifiers( Modifier.PRIVATE );

    if ( memoize.isCollectionType() && memoize.hasNoParameters() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", AREZ_CLASSNAME );
      block.addStatement( "this.$N = false", getMemoizeCollectionCacheDataActiveFieldName( memoize ) );
      block.addStatement( "this.$N = null", getMemoizeCollectionCacheDataFieldName( memoize ) );
      block.addStatement( "this.$N = null", getMemoizeCollectionUnmodifiableCacheDataFieldName( memoize ) );
      block.endControlFlow();
      builder.addCode( block.build() );
    }

    final ExecutableElement onDeactivate = memoize.getOnDeactivate();
    if ( null != onDeactivate )
    {
      builder.addStatement( "$N()", onDeactivate.getSimpleName().toString() );
    }
    return builder.build();
  }

  /**
   * Generate a wrapper around the Memoize method the setups up all the context parameters.
   */
  @Nonnull
  private static MethodSpec buildMemoizeAdapterMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                       @Nonnull final MemoizeDescriptor memoize )
    throws ProcessorException
  {
    assert memoize.shouldGenerateMemoizeWrapper();

    final ExecutableElement onActivate = memoize.getOnActivate();
    final ExecutableElement onDeactivate = memoize.getOnDeactivate();

    final boolean onActivateRequiresWrapper = memoize.shouldGenerateActivateWrapperHook();
    final boolean onDeactivateRequiresWrapper = memoize.shouldGenerateDeactivateWrapperHook();

    final ExecutableElement executableElement = memoize.getMethod();
    final ComponentDescriptor component = memoize.getComponent();
    final TypeElement typeElement = component.getElement();

    final DeclaredType declaredType = (DeclaredType) typeElement.asType();
    final ExecutableType executableType =
      (ExecutableType) processingEnv.getTypeUtils().asMemberOf( declaredType, executableElement );

    final MethodSpec.Builder method = MethodSpec.methodBuilder( getMemoizeWrapperMethodName( memoize ) );

    final List<String> additionalSuppressions = new ArrayList<>();

    if ( null != onActivate && null != onActivate.getAnnotation( Deprecated.class ) )
    {
      if ( !onActivateRequiresWrapper )
      {
        additionalSuppressions.add( "deprecation" );
      }
    }
    if ( null != onDeactivate && null != onDeactivate.getAnnotation( Deprecated.class ) )
    {
      if ( !onDeactivateRequiresWrapper && !additionalSuppressions.contains( "deprecation" ) )
      {
        additionalSuppressions.add( "deprecation" );
      }
    }
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv,
                                                        method,
                                                        additionalSuppressions,
                                                        Collections.singletonList( executableType ) );
    method.addModifiers( Modifier.PRIVATE );
    GeneratorUtil.copyTypeParameters( executableType, method );
    GeneratorUtil.copyWhitelistedAnnotations( executableElement, method );

    method.varargs( executableElement.isVarArgs() );

    // Add context parameters
    for ( final MemoizeContextParameterDescriptor contextParameter : memoize.getContextParameters() )
    {
      final String contextParameterName = FRAMEWORK_PREFIX + contextParameter.getName();
      final TypeName typeName = TypeName.get( contextParameter.initialValueType() );
      final ParameterSpec.Builder parameter = ParameterSpec.builder( typeName, contextParameterName, Modifier.FINAL );
      GeneratorUtil.copyWhitelistedAnnotations( contextParameter.getCapture(), parameter );
      method.addParameter( parameter.build() );
    }

    // Copy all the parameters across
    GeneratorUtil.copyParameters( executableElement, executableType, method );
    GeneratorUtil.copyExceptions( executableType, method );

    // Copy return type
    method.returns( TypeName.get( executableType.getReturnType() ) );

    // Register Hook
    if ( memoize.hasHooks() || onActivateRequiresWrapper || onDeactivateRequiresWrapper )
    {
      final StringBuilder sb = new StringBuilder();
      final List<Object> parameters = new ArrayList<>();
      sb.append( "this.$N.getContext().registerHook( $S, " );
      parameters.add( KERNEL_FIELD_NAME );
      parameters.add( "$H" );

      if ( null != onActivate || onActivateRequiresWrapper )
      {
        if ( onActivateRequiresWrapper )
        {
          sb.append( "this::$N" );
          parameters.add( getOnActivateHookMethodName( memoize ) );
        }
        else if ( component.isClassType() )
        {
          if ( onActivate.getParameters().isEmpty() )
          {
            sb.append( "() -> super.$N()" );
            parameters.add( onActivate.getSimpleName() );
          }
          else
          {
            sb.append( "() -> super.$N( this )" );
            parameters.add( onActivate.getSimpleName() );
          }
        }
        else
        {
          sb.append( onActivate.getParameters().isEmpty() ? "() -> $T.super.$N()" : "() -> $T.super.$N( this )" );
          parameters.add( component.getClassName() );
          parameters.add( onActivate.getSimpleName() );
        }
      }
      else
      {
        sb.append( "null" );
      }

      sb.append( ", " );

      if ( null != onDeactivate || onDeactivateRequiresWrapper )
      {
        if ( onDeactivateRequiresWrapper )
        {
          sb.append( "this::$N" );
          parameters.add( getOnDeactivateHookMethodName( memoize ) );
        }
        else if ( component.isClassType() )
        {
          if ( onDeactivate.getParameters().isEmpty() )
          {
            sb.append( "() -> super.$N()" );
            parameters.add( onDeactivate.getSimpleName() );
          }
          else
          {
            sb.append( "() -> super.$N( this )" );
            parameters.add( onDeactivate.getSimpleName() );
          }
        }
        else
        {
          sb.append( onDeactivate.getParameters().isEmpty() ? "() -> $T.super.$N()" : "() -> $T.super.$N( this )" );
          parameters.add( component.getClassName() );
          parameters.add( onDeactivate.getSimpleName() );
        }
      }
      else
      {
        sb.append( "null" );
      }
      sb.append( " )" );
      method.addStatement( sb.toString(), parameters.toArray() );
    }

    for ( final MemoizeContextParameterDescriptor contextParameter : memoize.getContextParameters() )
    {
      method.beginControlFlow( "try" );
      method.addStatement( "$N( $N )",
                           contextParameter.getPush().getSimpleName().toString(),
                           FRAMEWORK_PREFIX + contextParameter.getName() );
    }
    final StringBuilder sb = new StringBuilder();
    final List<Object> parameters = new ArrayList<>();
    sb.append( "return " );
    if ( !executableElement.getTypeParameters().isEmpty() )
    {
      sb.append( "($T) " );
      parameters.add( TypeName.get( memoize.getMethodType().getReturnType() ).box() );
    }

    else if ( component.isClassType() )
    {
      sb.append( "super.$N(" );
      parameters.add( memoize.getMethod().getSimpleName().toString() );
    }
    else
    {
      sb.append( "$T.super.$N(" );
      parameters.add( component.getClassName() );
      parameters.add( memoize.getMethod().getSimpleName().toString() );
    }

    boolean first = true;
    for ( final VariableElement element : executableElement.getParameters() )
    {
      if ( !first )
      {
        sb.append( "," );
      }
      first = false;
      sb.append( " $N" );
      parameters.add( element.getSimpleName().toString() );
    }
    if ( !first )
    {
      sb.append( " " );
    }
    sb.append( ")" );

    method.addStatement( sb.toString(), parameters.toArray() );

    for ( final MemoizeContextParameterDescriptor contextParameter : memoize.getContextParameters() )
    {
      method.nextControlFlow( "finally" );
      method.addStatement( "$N( $N )",
                           contextParameter.getPop().getSimpleName().toString(),
                           FRAMEWORK_PREFIX + contextParameter.getName() );
      method.endControlFlow();
    }

    return method.build();
  }

  @Nonnull
  private static MethodSpec.Builder refMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                               @Nonnull final TypeElement typeElement,
                                               @Nonnull final ExecutableElement executableElement )
  {
    final MethodSpec.Builder method =
      GeneratorUtil.overrideMethod( processingEnv,
                                    typeElement,
                                    executableElement,
                                    getAdditionalSuppressions( executableElement ),
                                    false );
    if ( !executableElement.getReturnType().getKind().isPrimitive() )
    {
      method.addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    }
    return method;
  }

  @Nonnull
  private static MethodSpec.Builder overrideMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                    @Nonnull final TypeElement typeElement,
                                                    @Nonnull final ExecutableElement executableElement )
  {
    return GeneratorUtil.overrideMethod( processingEnv,
                                         typeElement,
                                         executableElement,
                                         getAdditionalSuppressions( executableElement ),
                                         true );
  }

  @Nonnull
  private static List<String> getAdditionalSuppressions( @Nonnull final AnnotatedConstruct annotatedConstruct )
  {
    return hasSuppressWarningsDeprecation( annotatedConstruct ) ?
           Arrays.asList( "RedundantSuppression", "deprecation" ) :
           Collections.emptyList();
  }

  private static boolean hasSuppressWarningsDeprecation( @Nonnull final AnnotatedConstruct annotatedConstruct )
  {
    for ( var annotationMirror : annotatedConstruct.getAnnotationMirrors() )
    {
      if ( annotationMirror.getAnnotationType().toString().equals( SuppressWarnings.class.getCanonicalName() ) )
      {
        for ( var entry : annotationMirror.getElementValues().entrySet() )
        {
          if ( entry.getKey().getSimpleName().toString().equals( "value" ) )
          {
            // Get the list of values (e.g., ["unchecked", "deprecation"])
            @SuppressWarnings( "unchecked" )
            var values = (List<? extends AnnotationValue>) entry.getValue().getValue();
            for ( var value : values )
            {
              if ( "deprecation".equals( value.getValue().toString() ) )
              {
                return true;
              }
            }
          }
        }
      }
    }

    return false;
  }

  /**
   * Generate the wrapper around Memoize method.
   */
  @Nonnull
  private static MethodSpec buildMemoizeWithoutParams( @Nonnull final ProcessingEnvironment processingEnv,
                                                       @Nonnull final MemoizeDescriptor memoize )
    throws ProcessorException
  {
    final ExecutableElement executableElement = memoize.getMethod();
    final ComponentDescriptor component = memoize.getComponent();
    final MethodSpec.Builder method = overrideMethod( processingEnv, component.getElement(), executableElement );

    final TypeName returnType = TypeName.get( memoize.getMethodType().getReturnType() );
    generateNotDisposedInvariant( method, executableElement.getSimpleName().toString() );

    if ( memoize.isCollectionType() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", AREZ_CLASSNAME );

      final String result = "$$ar$$_result";
      block.addStatement( "final $T $N = this.$N.get()", returnType, result, getMemoizeFieldName( memoize ) );
      final CodeBlock.Builder guard = CodeBlock.builder();
      guard.beginControlFlow( "if ( this.$N != $N )",
                              getMemoizeCollectionCacheDataFieldName( memoize ),
                              result );
      guard.addStatement( "this.$N = $N", getMemoizeCollectionCacheDataFieldName( memoize ), result );

      if ( AnnotationsUtil.hasNonnullAnnotation( memoize.getMethod() ) )
      {
        guard.addStatement( "this.$N = $T.wrap( $N )",
                            getMemoizeCollectionUnmodifiableCacheDataFieldName( memoize ),
                            COLLECTIONS_UTIL_CLASSNAME,
                            result );
      }
      else
      {
        guard.addStatement( "this.$N = null == $N ? null : $T.wrap( $N )",
                            getMemoizeCollectionUnmodifiableCacheDataFieldName( memoize ),
                            result,
                            COLLECTIONS_UTIL_CLASSNAME,
                            result );
      }
      guard.endControlFlow();
      block.add( guard.build() );
      block.addStatement( "return $N", getMemoizeCollectionUnmodifiableCacheDataFieldName( memoize ) );

      block.nextControlFlow( "else" );

      block.addStatement( "return this.$N.get()", getMemoizeFieldName( memoize ) );
      block.endControlFlow();

      method.addCode( block.build() );
    }
    else if ( executableElement.getTypeParameters().isEmpty() )
    {
      method.addStatement( "return this.$N.get()", getMemoizeFieldName( memoize ) );
    }
    else
    {
      method.addStatement( "return ($T) this.$N.get()", returnType.box(), getMemoizeFieldName( memoize ) );
    }
    return method.build();
  }

  @Nonnull
  private static MethodSpec buildMemoizeWithParamsComputableValueRef( @Nonnull final ProcessingEnvironment processingEnv,
                                                                      @Nonnull final MemoizeDescriptor memoize,
                                                                      @Nonnull final CandidateMethod refMethod )
    throws ProcessorException
  {
    final MethodSpec.Builder method =
      refMethod( processingEnv, memoize.getComponent().getElement(), refMethod.getMethod() );
    generateNotDisposedInvariant( method, refMethod.getMethod().getSimpleName().toString() );

    final StringBuilder sb = new StringBuilder();
    final List<Object> params = new ArrayList<>();
    sb.append( "return this.$N.getComputableValue( " );
    params.add( getMemoizeFieldName( memoize ) );

    boolean first = true;
    for ( final VariableElement element : refMethod.getMethod().getParameters() )
    {
      if ( !first )
      {
        sb.append( ", " );
      }
      first = false;
      sb.append( "$N" );
      params.add( element.getSimpleName().toString() );
    }
    sb.append( " )" );

    method.addStatement( sb.toString(), params.toArray() );
    return method.build();
  }

  @Nonnull
  private static MethodSpec buildMemoizeWithParams( @Nonnull final ProcessingEnvironment processingEnv,
                                                    @Nonnull final MemoizeDescriptor memoize )
    throws ProcessorException
  {
    final ExecutableElement executableElement = memoize.getMethod();

    final boolean hasTypeParameters = !executableElement.getTypeParameters().isEmpty();
    final List<String> additionalSuppressions =
      new ArrayList<>( hasTypeParameters ? Collections.singletonList( "unchecked" ) : Collections.emptyList() );
    additionalSuppressions.addAll( getAdditionalSuppressions( executableElement ) );
    final MethodSpec.Builder builder =
      GeneratorUtil.overrideMethod( processingEnv,
                                    memoize.getComponent().getElement(),
                                    executableElement,
                                    additionalSuppressions,
                                    true );

    generateNotDisposedInvariant( builder, executableElement.getSimpleName().toString() );

    final StringBuilder sb = new StringBuilder();
    final List<Object> parameters = new ArrayList<>();
    sb.append( "return " );
    if ( hasTypeParameters )
    {
      sb.append( "($T) " );
      parameters.add( TypeName.get( memoize.getMethodType().getReturnType() ).box() );
    }
    sb.append( "this.$N.get( " );
    parameters.add( getMemoizeFieldName( memoize ) );

    boolean first = true;
    for ( final MemoizeContextParameterDescriptor contextParameter : memoize.getContextParameters() )
    {
      if ( !first )
      {
        sb.append( ", " );
      }
      first = false;
      sb.append( "$N()" );
      parameters.add( contextParameter.getCapture().getSimpleName().toString() );
    }

    for ( final VariableElement element : executableElement.getParameters() )
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

  private static void buildMemoizeFields( @Nonnull final ProcessingEnvironment processingEnv,
                                          @Nonnull final MemoizeDescriptor memoize,
                                          @Nonnull final TypeSpec.Builder builder )
  {
    if ( memoize.hasNoParameters() )
    {
      buildMemoizeWithNoParametersFields( processingEnv, memoize, builder );
    }
    else
    {
      buildMemoizeWithParametersFields( processingEnv, memoize, builder );
    }
  }

  private static void buildMemoizeWithNoParametersFields( @Nonnull final ProcessingEnvironment processingEnv,
                                                          @Nonnull final MemoizeDescriptor memoize,
                                                          @Nonnull final TypeSpec.Builder builder )
  {
    final ExecutableElement method = memoize.getMethod();
    final ExecutableType methodType = memoize.getMethodType();
    final TypeName parameterType =
      method.getTypeParameters().isEmpty() ? TypeName.get( methodType.getReturnType() ).box() :
      WildcardTypeName.subtypeOf( TypeName.OBJECT );
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( COMPUTABLE_VALUE_CLASSNAME, parameterType );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName, getMemoizeFieldName( memoize ), Modifier.FINAL, Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, field, methodType.getReturnType() );
    builder.addField( field.build() );
    if ( memoize.isCollectionType() )
    {
      final FieldSpec.Builder cacheField =
        FieldSpec.builder( TypeName.get( methodType.getReturnType() ),
                           getMemoizeCollectionCacheDataFieldName( memoize ),
                           Modifier.PRIVATE );
      SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, cacheField, methodType.getReturnType() );
      builder.addField( cacheField.build() );

      final FieldSpec.Builder unmodifiableCacheField =
        FieldSpec.builder( TypeName.get( methodType.getReturnType() ),
                           getMemoizeCollectionUnmodifiableCacheDataFieldName( memoize ),
                           Modifier.PRIVATE );
      SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv,
                                                          unmodifiableCacheField,
                                                          methodType.getReturnType() );
      builder.addField( unmodifiableCacheField.build() );

      builder.addField( FieldSpec.builder( TypeName.BOOLEAN,
                                           getMemoizeCollectionCacheDataActiveFieldName( memoize ),
                                           Modifier.PRIVATE ).build() );
    }
  }

  private static void buildMemoizeWithParametersFields( @Nonnull final ProcessingEnvironment processingEnv,
                                                        @Nonnull final MemoizeDescriptor memoize,
                                                        @Nonnull final TypeSpec.Builder builder )
  {
    final TypeName parameterType =
      memoize.getMethod().getTypeParameters().isEmpty() ?
      TypeName.get( memoize.getMethodType().getReturnType() ).box() :
      WildcardTypeName.subtypeOf( TypeName.OBJECT );
    final FieldSpec.Builder field =
      FieldSpec.builder( ParameterizedTypeName.get( MEMOIZE_CACHE_CLASSNAME, parameterType ),
                         getMemoizeFieldName( memoize ),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, field, memoize.getMethodType().getReturnType() );
    builder.addField( field.build() );
  }

  @Nonnull
  private static String getMemoizeFieldName( @Nonnull final MemoizeDescriptor memoize )
  {
    return FIELD_PREFIX + memoize.getName();
  }

  private static void buildMemoizeInitializer( @Nonnull final MemoizeDescriptor memoize,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    if ( memoize.hasNoParameters() )
    {
      buildMemoizeWithNoParameters( memoize, builder );
    }
    else
    {
      buildMemoizeWithParametersInitializer( memoize, builder );
    }
  }

  private static void buildMemoizeWithNoParameters( @Nonnull final MemoizeDescriptor memoize,
                                                    @Nonnull final MethodSpec.Builder builder )
  {
    final List<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();

    final ComponentDescriptor component = memoize.getComponent();
    final ExecutableElement method = memoize.getMethod();
    if ( memoize.isCollectionType() && !memoize.hasHooks() )
    {
      sb.append( "this.$N = $T.areCollectionsPropertiesUnmodifiable() ? " +
                 "$N.computable( " +
                 "$T.areNativeComponentsEnabled() ? $N : null, " +
                 "$T.areNamesEnabled() ? $N + $S : null, " );
      parameters.add( getMemoizeFieldName( memoize ) );
      parameters.add( AREZ_CLASSNAME );
      parameters.add( CONTEXT_VAR_NAME );
      parameters.add( AREZ_CLASSNAME );
      parameters.add( COMPONENT_VAR_NAME );
      parameters.add( AREZ_CLASSNAME );
      parameters.add( NAME_VAR_NAME );
      parameters.add( "." + memoize.getName() );

      if ( memoize.shouldGenerateMemoizeWrapper() )
      {
        sb.append( "() -> $N(), " );
        parameters.add( getMemoizeWrapperMethodName( memoize ) );
      }
      else if ( component.isClassType() )
      {
        sb.append( "() -> super.$N(), " );
        parameters.add( method.getSimpleName().toString() );
      }
      else
      {
        sb.append( "() -> $T.super.$N(), " );
        parameters.add( component.getClassName() );
        parameters.add( method.getSimpleName().toString() );
      }
      appendInitializerSuffix( memoize, parameters, sb );

      // Else part of ternary
      sb.append( " : $N.computable( " +
                 "$T.areNativeComponentsEnabled() ? $N : null, " +
                 "$T.areNamesEnabled() ? $N + $S : null, " );
      parameters.add( CONTEXT_VAR_NAME );
      parameters.add( AREZ_CLASSNAME );
      parameters.add( COMPONENT_VAR_NAME );
      parameters.add( AREZ_CLASSNAME );
      parameters.add( NAME_VAR_NAME );
      parameters.add( "." + memoize.getName() );
      if ( component.isClassType() )
      {
        sb.append( "() -> super.$N(), " );
        parameters.add( method.getSimpleName().toString() );
      }
      else
      {
        sb.append( "() -> $T.super.$N(), " );
        parameters.add( component.getClassName() );
        parameters.add( method.getSimpleName().toString() );
      }
      appendInitializerSuffix( memoize, parameters, sb );
    }
    else // hasHooks()
    {
      sb.append( "this.$N = $N.computable( " +
                 "$T.areNativeComponentsEnabled() ? $N : null, " +
                 "$T.areNamesEnabled() ? $N + $S : null, " );
      parameters.add( getMemoizeFieldName( memoize ) );
      parameters.add( CONTEXT_VAR_NAME );
      parameters.add( AREZ_CLASSNAME );
      parameters.add( COMPONENT_VAR_NAME );
      parameters.add( AREZ_CLASSNAME );
      parameters.add( NAME_VAR_NAME );
      parameters.add( "." + memoize.getName() );

      if ( memoize.shouldGenerateMemoizeWrapper() )
      {
        sb.append( "() -> $N(), " );
        parameters.add( getMemoizeWrapperMethodName( memoize ) );
      }
      else if ( component.isClassType() )
      {
        sb.append( "() -> super.$N(), " );
        parameters.add( method.getSimpleName().toString() );
      }
      else
      {
        sb.append( "() -> $T.super.$N(), " );
        parameters.add( component.getClassName() );
        parameters.add( method.getSimpleName().toString() );
      }
      appendInitializerSuffix( memoize, parameters, sb );
    }
    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private static void buildMemoizeWithParametersInitializer( @Nonnull final MemoizeDescriptor memoize,
                                                             @Nonnull final MethodSpec.Builder builder )
  {
    final List<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = new $T<>( $T.areZonesEnabled() ? $N : null, " +
               "$T.areNativeComponentsEnabled() ? $N : null, " +
               "$T.areNamesEnabled() ? $N + $S : null, " );
    parameters.add( getMemoizeFieldName( memoize ) );
    parameters.add( MEMOIZE_CACHE_CLASSNAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( CONTEXT_VAR_NAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( COMPONENT_VAR_NAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( NAME_VAR_NAME );
    parameters.add( "." + memoize.getName() );

    final ComponentDescriptor component = memoize.getComponent();
    final ExecutableElement method = memoize.getMethod();
    if ( memoize.shouldGenerateMemoizeWrapper() )
    {
      sb.append( "args -> $N(" );
      parameters.add( getMemoizeWrapperMethodName( memoize ) );
    }
    else if ( component.isClassType() )
    {
      sb.append( "args -> super.$N(" );
      parameters.add( method.getSimpleName().toString() );
    }
    else
    {
      sb.append( "args -> $T.super.$N(" );
      parameters.add( component.getClassName() );
      parameters.add( method.getSimpleName().toString() );
    }

    boolean first = true;
    int index = 0;
    for ( final var contextParameter : memoize.getContextParameters() )
    {
      if ( !first )
      {
        sb.append( ", " );
      }
      first = false;
      final TypeName contextParameterJavaType = TypeName.get( contextParameter.initialValueType() );
      if ( contextParameterJavaType.equals( TypeName.OBJECT ) )
      {
        sb.append( "args[ " ).append( index ).append( " ]" );
      }
      else
      {
        sb.append( "($T) args[ " ).append( index ).append( " ]" );
        parameters.add( contextParameterJavaType );
      }
      index++;
    }
    for ( final TypeMirror arg : memoize.getMethodType().getParameterTypes() )
    {
      if ( !first )
      {
        sb.append( ", " );
      }
      first = false;
      if ( TypeName.get( arg ).equals( TypeName.OBJECT ) )
      {
        sb.append( "args[ " ).append( index ).append( " ]" );
      }
      else
      {
        sb.append( "($T) args[ " ).append( index ).append( " ]" );
        parameters.add( arg );
      }
      index++;
    }

    sb.append( "), " );
    sb.append( index );
    sb.append( ", " );

    final List<String> flags = generateMemoizeFlags( memoize );

    sb.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( COMPUTABLE_VALUE_FLAGS_CLASSNAME );
    }

    appendMemoizeEqualityComparatorArg( memoize, parameters, sb );

    sb.append( " )" );
    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private static void appendInitializerSuffix( @Nonnull final MemoizeDescriptor memoize,
                                               @Nonnull final List<Object> parameters,
                                               @Nonnull final StringBuilder sb )
  {
    final List<String> flags = generateMemoizeFlags( memoize );
    flags.add( "RUN_LATER" );
    if ( memoize.isKeepAlive() )
    {
      flags.add( "KEEPALIVE" );
    }

    sb.append( flags.stream().map( flag -> "$T." + flag ).collect( Collectors.joining( " | " ) ) );
    for ( int i = 0; i < flags.size(); i++ )
    {
      parameters.add( COMPUTABLE_VALUE_FLAGS_CLASSNAME );
    }

    appendMemoizeEqualityComparatorArg( memoize, parameters, sb );

    sb.append( " )" );
  }

  private static void appendMemoizeEqualityComparatorArg( @Nonnull final MemoizeDescriptor memoize,
                                                          @Nonnull final List<Object> parameters,
                                                          @Nonnull final StringBuilder sb )
  {
    if ( !memoize.hasObjectsEqualsComparator() )
    {
      sb.append( ", " );
      if ( memoize.hasObjectsDeepEqualsComparator() )
      {
        sb.append( "$T.INSTANCE" );
        parameters.add( OBJECTS_DEEP_EQUALS_COMPARATOR_CLASSNAME );
      }
      else
      {
        final ClassName comparatorClassName = ClassName.bestGuess( memoize.getEqualityComparator() );
        sb.append( "new $T()" );
        parameters.add( comparatorClassName );
      }
    }
  }

  @Nonnull
  private static List<String> generateMemoizeFlags( @Nonnull final MemoizeDescriptor memoize )
  {
    final List<String> flags = new ArrayList<>();
    final Priority priority = memoize.getPriority();
    if ( Priority.NORMAL != priority )
    {
      flags.add( "PRIORITY_" + priority.name() );
    }

    if ( !memoize.isReportResult() )
    {
      flags.add( "NO_REPORT_RESULT" );
    }
    if ( memoize.isObserveLowerPriorityDependencies() )
    {
      flags.add( "OBSERVE_LOWER_PRIORITY_DEPENDENCIES" );
    }
    if ( memoize.canReadOutsideTransaction() )
    {
      flags.add( "READ_OUTSIDE_TRANSACTION" );
    }
    switch ( memoize.getDepType() )
    {
      case "AREZ":
        flags.add( "AREZ_DEPENDENCIES" );
        break;
      case "AREZ_OR_NONE":
        flags.add( "AREZ_OR_NO_DEPENDENCIES" );
        break;
      default:
        flags.add( "AREZ_OR_EXTERNAL_DEPENDENCIES" );
        break;
    }
    return flags;
  }

  static boolean isCollectionType( @Nonnull final ExecutableElement method )
  {
    return isMethodReturnType( method, Collection.class ) ||
           isMethodReturnType( method, Set.class ) ||
           isMethodReturnType( method, List.class ) ||
           isMethodReturnType( method, Map.class );
  }

  static boolean isMethodReturnType( @Nonnull final ExecutableElement method, @Nonnull final Class<?> type )
  {
    final TypeMirror returnType = method.getReturnType();
    final TypeKind kind = returnType.getKind();
    if ( TypeKind.DECLARED != kind )
    {
      return false;
    }
    else
    {
      final DeclaredType declaredType = (DeclaredType) returnType;
      final TypeElement element = (TypeElement) declaredType.asElement();
      return element.getQualifiedName().toString().equals( type.getName() );
    }
  }

  @Nonnull
  private static String getMemoizeWrapperMethodName( @Nonnull final MemoizeDescriptor memoize )
  {
    return FRAMEWORK_PREFIX + "memoize_" + memoize.getName();
  }

  @Nonnull
  private static String getOnActivateHookMethodName( @Nonnull final MemoizeDescriptor memoize )
  {
    return FRAMEWORK_PREFIX + "onActivate_" + memoize.getName();
  }

  @Nonnull
  private static String getOnDeactivateHookMethodName( @Nonnull final MemoizeDescriptor memoize )
  {
    return FRAMEWORK_PREFIX + "onDeactivate_" + memoize.getName();
  }

  @Nonnull
  private static String getMemoizeCollectionCacheDataActiveFieldName( @Nonnull final MemoizeDescriptor memoize )
  {
    return OBSERVABLE_DATA_FIELD_PREFIX + "$$cache_active$$_" + memoize.getName();
  }

  @Nonnull
  private static String getMemoizeCollectionCacheDataFieldName( @Nonnull final MemoizeDescriptor memoize )
  {
    return OBSERVABLE_DATA_FIELD_PREFIX + "$$cache$$_" + memoize.getName();
  }

  @Nonnull
  private static String getMemoizeCollectionUnmodifiableCacheDataFieldName( @Nonnull final MemoizeDescriptor memoize )
  {
    return OBSERVABLE_DATA_FIELD_PREFIX + "$$unmodifiable_cache$$_" + memoize.getName();
  }

  private static void buildObservableFields( @Nonnull final ProcessingEnvironment processingEnv,
                                             @Nonnull final ObservableDescriptor observable,
                                             @Nonnull final TypeSpec.Builder builder )
  {
    final ExecutableType getterType = observable.getGetterType();
    final ParameterizedTypeName typeName =
      ParameterizedTypeName.get( OBSERVABLE_CLASSNAME,
                                 TypeName.get( getterType.getReturnType() ).box() );
    final FieldSpec.Builder field =
      FieldSpec.builder( typeName,
                         observable.getFieldName(),
                         Modifier.FINAL,
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, field, getterType.getReturnType() );
    builder.addField( field.build() );
    if ( observable.hasCustomEqualityComparator() )
    {
      final ClassName equalityComparatorClassName = ClassName.bestGuess( observable.getEqualityComparator() );
      builder.addField( FieldSpec.builder( equalityComparatorClassName,
                                           observable.getEqualityComparatorFieldName(),
                                           Modifier.FINAL,
                                           Modifier.PRIVATE )
                          .initializer( "new $T()", equalityComparatorClassName )
                          .build() );
    }
    if ( observable.isAbstract() )
    {
      final FieldSpec.Builder dataField =
        FieldSpec.builder( TypeName.get( getterType.getReturnType() ),
                           observable.getDataFieldName(),
                           Modifier.PRIVATE );
      if ( observable.isGetterNonnull() && observable.isSetterNonnull() && observable.requireInitializer() )
      {
        dataField.addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
      }
      else if ( AnnotationsUtil.hasNullableAnnotation( observable.getGetter() ) )
      {
        dataField.addAnnotation( GeneratorUtil.NULLABLE_CLASSNAME );
      }
      SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, dataField, getterType.getReturnType() );
      builder.addField( dataField.build() );
    }
    if ( observable.shouldGenerateUnmodifiableCollectionVariant() )
    {
      final FieldSpec.Builder dataField =
        FieldSpec.builder( TypeName.get( getterType.getReturnType() ),
                           observable.getCollectionCacheDataFieldName(),
                           Modifier.PRIVATE );
      SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, dataField, getterType.getReturnType() );
      builder.addField( dataField.build() );
    }
  }

  private static void buildObservableInitializer( @Nonnull final ObservableDescriptor observable,
                                                  @Nonnull final MethodSpec.Builder builder )
  {
    final List<Object> parameters = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "this.$N = $N.observable( " +
               "$T.areNativeComponentsEnabled() ? $N : null, " +
               "$T.areNamesEnabled() ? $N + $S : null, " +
               "$T.arePropertyIntrospectorsEnabled() ? () -> ( this.$N.isNotReady() ? null : " );
    parameters.add( observable.getFieldName() );
    parameters.add( CONTEXT_VAR_NAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( COMPONENT_VAR_NAME );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( NAME_VAR_NAME );
    parameters.add( "." + observable.getName() );
    parameters.add( AREZ_CLASSNAME );
    parameters.add( KERNEL_FIELD_NAME );

    final boolean abstractObservables = observable.isAbstract();
    if ( abstractObservables )
    {
      sb.append( "this.$N" );
      parameters.add( observable.getDataFieldName() );
    }
    else if ( observable.getComponent().isClassType() )
    {
      sb.append( "super.$N()" );
      parameters.add( observable.getGetter().getSimpleName() );
    }
    else
    {
      sb.append( "$T.super.$N()" );
      parameters.add( observable.getComponent().getClassName() );
      parameters.add( observable.getGetter().getSimpleName() );
    }
    sb.append( " ) : null" );

    if ( observable.hasSetter() )
    {
      //setter
      sb.append( ", $T.arePropertyIntrospectorsEnabled() ? v -> " );
      parameters.add( AREZ_CLASSNAME );
      if ( abstractObservables )
      {
        sb.append( "this.$N = v" );
        parameters.add( observable.getDataFieldName() );
      }
      else if ( observable.getComponent().isClassType() )
      {
        sb.append( "super.$N( v )" );
        parameters.add( observable.getSetter().getSimpleName() );
      }
      else
      {
        sb.append( "$T.super.$N( v )" );
        parameters.add( observable.getComponent().getClassName() );
        parameters.add( observable.getSetter().getSimpleName() );
      }
      sb.append( " : null" );
    }
    else
    {
      sb.append( ", null" );
    }

    sb.append( " )" );
    builder.addStatement( sb.toString(), parameters.toArray() );
  }

  private static void buildObservableDisposer( @Nonnull final ObservableDescriptor observable,
                                               @Nonnull final MethodSpec.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", observable.getFieldName() );
  }

  private static void buildObservableMethods( @Nonnull final ProcessingEnvironment processingEnv,
                                              @Nonnull final ObservableDescriptor observable,
                                              @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    builder.addMethod( buildObservableGetter( processingEnv, observable ) );
    if ( observable.expectSetter() )
    {
      builder.addMethod( buildObservableSetter( processingEnv, observable ) );
      if ( observable.canWriteOutsideTransaction() )
      {
        builder.addMethod( buildObservableInternalSetter( processingEnv, observable ) );
      }
    }
    for ( final CandidateMethod refMethod : observable.getRefMethods() )
    {
      final MethodSpec.Builder method =
        refMethod( processingEnv, observable.getComponent().getElement(), refMethod.getMethod() );

      generateNotDisposedInvariant( method, refMethod.getMethod().getSimpleName().toString() );

      builder.addMethod( method.addStatement( "return $N", observable.getFieldName() ).build() );
    }
  }

  @Nonnull
  private static MethodSpec buildObservableSetter( @Nonnull final ProcessingEnvironment processingEnv,
                                                   @Nonnull final ObservableDescriptor observable )
    throws ProcessorException
  {
    final ExecutableElement setter = observable.getSetter();
    final ExecutableType setterType = observable.getSetterType();
    final String methodName = setter.getSimpleName().toString();
    final ComponentDescriptor component = observable.getComponent();
    final MethodSpec.Builder method = overrideMethod( processingEnv, component.getElement(), setter );

    if ( observable.canWriteOutsideTransaction() )
    {
      final VariableElement element = setter.getParameters().get( 0 );
      final String paramName = element.getSimpleName().toString();

      if ( setterType.getThrownTypes().isEmpty() )
      {
        method.addStatement( "this.$N.safeSetObservable( $T.areNamesEnabled() ? this.$N.getName() + $S : null, " +
                             "() -> this.$N( $N ) )",
                             KERNEL_FIELD_NAME,
                             AREZ_CLASSNAME,
                             KERNEL_FIELD_NAME,
                             "." + methodName,
                             FRAMEWORK_PREFIX + methodName,
                             paramName );
      }
      else
      {
        //noinspection CodeBlock2Expr
        generateTryBlock( method, setterType.getThrownTypes(), b -> {
          b.addStatement( "this.$N.setObservable( $T.areNamesEnabled() ? this.$N.getName() + $S : null, " +
                          "() -> this.$N( $N ) )",
                          KERNEL_FIELD_NAME,
                          AREZ_CLASSNAME,
                          KERNEL_FIELD_NAME,
                          "." + methodName,
                          FRAMEWORK_PREFIX + methodName,
                          paramName );
        } );
      }
    }
    else
    {
      buildObservableSetterImpl( observable, method );
    }

    return method.build();
  }

  /**
   * Generate the internal setter.
   */
  @Nonnull
  private static MethodSpec buildObservableInternalSetter( @Nonnull final ProcessingEnvironment processingEnv,
                                                           @Nonnull final ObservableDescriptor observable )
    throws ProcessorException
  {
    final ExecutableElement setter = observable.getSetter();
    final ExecutableType setterType = observable.getSetterType();
    final MethodSpec.Builder builder =
      MethodSpec.methodBuilder( FRAMEWORK_PREFIX + setter.getSimpleName() );
    builder.addModifiers( Modifier.PRIVATE );
    GeneratorUtil.copyExceptions( setterType, builder );
    GeneratorUtil.copyTypeParameters( setterType, builder );
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, builder, setterType );
    GeneratorUtil.copyWhitelistedAnnotations( setter, builder );
    buildObservableSetterImpl( observable, builder );
    final VariableElement element = setter.getParameters().get( 0 );
    final ParameterSpec.Builder param =
      ParameterSpec.builder( TypeName.get( observable.getSetterType().getParameterTypes().get( 0 ) ),
                             element.getSimpleName().toString(),
                             Modifier.FINAL );
    GeneratorUtil.copyWhitelistedAnnotations( element, param );
    builder.addParameter( param.build() );

    return builder.build();
  }

  private static void buildObservableSetterImpl( @Nonnull final ObservableDescriptor observable,
                                                 @Nonnull final MethodSpec.Builder builder )
  {
    final ExecutableElement setter = observable.getSetter();
    final ExecutableElement getter = observable.getGetter();
    final String methodName = setter.getSimpleName().toString();

    // If the getter is deprecated but the setter is not
    // then we need to suppress deprecation warnings on setter
    // as we invoked getter from within it to verify value is
    // actually changed
    if ( null == setter.getAnnotation( Deprecated.class ) && null != getter.getAnnotation( Deprecated.class ) )
    {
      builder.addAnnotation( SuppressWarningsUtil.suppressWarningsAnnotation( "deprecation" ) );
    }

    final TypeMirror parameterType = observable.getSetterType().getParameterTypes().get( 0 );
    final VariableElement element = setter.getParameters().get( 0 );
    final String paramName = element.getSimpleName().toString();
    final TypeName type = TypeName.get( parameterType );
    generateNotDisposedInvariant( builder, methodName );
    builder.addStatement( "this.$N.preReportChanged()", observable.getFieldName() );

    final String varName = VARIABLE_PREFIX + "currentValue";

    final CodeBlock.Builder codeBlock = CodeBlock.builder();
    final boolean abstractObservables = observable.isAbstract();
    final ComponentDescriptor component = observable.getComponent();
    if ( abstractObservables )
    {
      builder.addStatement( "final $T $N = this.$N", type, varName, observable.getDataFieldName() );
    }
    else
    {
      if ( component.isClassType() )
      {
        builder.addStatement( "final $T $N = super.$N()", type, varName, getter.getSimpleName() );
      }
      else
      {
        builder.addStatement( "final $T $N = $T.super.$N()",
                              type,
                              varName,
                              component.getClassName(),
                              getter.getSimpleName() );
      }
    }
    if ( type.isPrimitive() )
    {
      codeBlock.beginControlFlow( "if ( $N != $N )", paramName, varName );
    }
    else
    {
      // We have a nonnull setter so lets enforce it
      if ( observable.isSetterNonnull() )
      {
        builder.addStatement( "assert null != $N", paramName );
      }
      if ( observable.hasObjectsDeepEqualsComparator() )
      {
        codeBlock.beginControlFlow( "if ( !$T.deepEquals( $N, $N ) )", Objects.class, paramName, varName );
      }
      else if ( observable.hasCustomEqualityComparator() )
      {
        codeBlock.beginControlFlow( "if ( !this.$N.areEqual( $N, $N ) )",
                                    observable.getEqualityComparatorFieldName(),
                                    paramName,
                                    varName );
      }
      else
      {
        codeBlock.beginControlFlow( "if ( !$T.equals( $N, $N ) )", Objects.class, paramName, varName );
      }
    }
    if ( observable.shouldGenerateUnmodifiableCollectionVariant() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", AREZ_CLASSNAME );
      block.addStatement( "this.$N = null", observable.getCollectionCacheDataFieldName() );
      block.endControlFlow();

      builder.addCode( block.build() );
    }
    final DependencyDescriptor dependency = observable.getDependencyDescriptor();
    if ( abstractObservables )
    {
      if ( null != dependency )
      {
        if ( observable.isGetterNonnull() )
        {
          codeBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( $N, true )",
                                  DISPOSE_TRACKABLE_CLASSNAME,
                                  varName,
                                  getDependencyKey( dependency ) );
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          listenerBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( $N, true )",
                                      DISPOSE_TRACKABLE_CLASSNAME,
                                      varName,
                                      getDependencyKey( dependency ) );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
      codeBlock.addStatement( "this.$N = $N", observable.getDataFieldName(), paramName );
      if ( null != dependency )
      {
        if ( dependency.shouldCascadeDispose() )
        {
          if ( observable.isGetterNonnull() )
          {
            codeBlock
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, this::dispose, true )",
                             DISPOSE_TRACKABLE_CLASSNAME,
                             paramName,
                             getDependencyKey( dependency ) );
          }
          else
          {
            final CodeBlock.Builder listenerBlock = CodeBlock.builder();
            listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
            listenerBlock
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, this::dispose, true )",
                             DISPOSE_TRACKABLE_CLASSNAME,
                             paramName,
                             getDependencyKey( dependency ) );
            listenerBlock.endControlFlow();
            codeBlock.add( listenerBlock.build() );
          }
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
          listenerBlock
            .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, () -> $N( null ), true )",
                           DISPOSE_TRACKABLE_CLASSNAME,
                           paramName,
                           getDependencyKey( dependency ),
                           setter.getSimpleName().toString() );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
    }
    else
    {
      if ( null != dependency )
      {
        if ( observable.isGetterNonnull() )
        {
          codeBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( $N, true )",
                                  DISPOSE_TRACKABLE_CLASSNAME,
                                  varName,
                                  getDependencyKey( dependency ) );
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", varName );
          listenerBlock.addStatement( "$T.asDisposeNotifier( $N ).removeOnDisposeListener( $N, true )",
                                      DISPOSE_TRACKABLE_CLASSNAME,
                                      varName,
                                      getDependencyKey( dependency ) );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
      if ( component.isClassType() )
      {
        codeBlock.addStatement( "super.$N( $N )", setter.getSimpleName(), paramName );
      }
      else
      {
        codeBlock.addStatement( "$T.super.$N( $N )", component.getClassName(), setter.getSimpleName(), paramName );
      }
      if ( null != dependency )
      {
        if ( dependency.shouldCascadeDispose() )
        {
          if ( observable.isGetterNonnull() )
          {
            codeBlock
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, this::dispose, true )",
                             DISPOSE_TRACKABLE_CLASSNAME,
                             paramName,
                             getDependencyKey( dependency ) );
          }
          else
          {
            final CodeBlock.Builder listenerBlock = CodeBlock.builder();
            listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
            listenerBlock
              .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, this::dispose, true )",
                             DISPOSE_TRACKABLE_CLASSNAME,
                             paramName,
                             getDependencyKey( dependency ) );
            listenerBlock.endControlFlow();
            codeBlock.add( listenerBlock.build() );
          }
        }
        else
        {
          final CodeBlock.Builder listenerBlock = CodeBlock.builder();
          listenerBlock.beginControlFlow( "if ( null != $N )", paramName );
          listenerBlock
            .addStatement( "$T.asDisposeNotifier( $N ).addOnDisposeListener( $N, () -> $N( null ), true )",
                           DISPOSE_TRACKABLE_CLASSNAME,
                           paramName,
                           getDependencyKey( dependency ),
                           setter.getSimpleName().toString() );
          listenerBlock.endControlFlow();
          codeBlock.add( listenerBlock.build() );
        }
      }
    }
    if ( observable.doesSetterAlwaysMutate() )
    {
      codeBlock.addStatement( "this.$N.reportChanged()", observable.getFieldName() );
    }
    else
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      if ( type.isPrimitive() )
      {
        if ( component.isClassType() )
        {
          block.beginControlFlow( "if ( $N != super.$N() )", varName, getter.getSimpleName() );
        }
        else
        {
          block.beginControlFlow( "if ( $N != $T.super.$N() )",
                                  varName,
                                  component.getClassName(),
                                  getter.getSimpleName() );
        }
      }
      else
      {
        if ( observable.hasObjectsDeepEqualsComparator() )
        {
          if ( component.isClassType() )
          {
            block.beginControlFlow( "if ( !$T.deepEquals( $N, super.$N() ) )",
                                    Objects.class,
                                    varName,
                                    getter.getSimpleName() );
          }
          else
          {
            block.beginControlFlow( "if ( !$T.deepEquals( $N, $T.super.$N() ) )",
                                    Objects.class,
                                    varName,
                                    component.getClassName(),
                                    getter.getSimpleName() );
          }
        }
        else if ( observable.hasCustomEqualityComparator() )
        {
          if ( component.isClassType() )
          {
            block.beginControlFlow( "if ( !this.$N.areEqual( $N, super.$N() ) )",
                                    observable.getEqualityComparatorFieldName(),
                                    varName,
                                    getter.getSimpleName() );
          }
          else
          {
            block.beginControlFlow( "if ( !this.$N.areEqual( $N, $T.super.$N() ) )",
                                    observable.getEqualityComparatorFieldName(),
                                    varName,
                                    component.getClassName(),
                                    getter.getSimpleName() );
          }
        }
        else
        {
          if ( component.isClassType() )
          {
            block.beginControlFlow( "if ( !$T.equals( $N, super.$N() ) )",
                                    Objects.class,
                                    varName,
                                    getter.getSimpleName() );
          }
          else
          {
            block.beginControlFlow( "if ( !$T.equals( $N, $T.super.$N() ) )",
                                    Objects.class,
                                    varName,
                                    component.getClassName(),
                                    getter.getSimpleName() );
          }
        }
      }
      block.addStatement( "this.$N.reportChanged()", observable.getFieldName() );
      block.endControlFlow();
      codeBlock.add( block.build() );
    }
    if ( null != observable.getReferenceDescriptor() )
    {
      if ( observable.getReferenceDescriptor().hasInverse() )
      {
        codeBlock.addStatement( "this.$N()", observable.getReferenceDescriptor().getDelinkMethodName() );
      }
      if ( "EAGER".equals( observable.getReferenceDescriptor().getLinkType() ) )
      {
        codeBlock.addStatement( "this.$N()", observable.getReferenceDescriptor().getLinkMethodName() );
      }
      else
      {
        codeBlock.addStatement( "this.$N = null", observable.getReferenceDescriptor().getFieldName() );
      }
    }

    codeBlock.endControlFlow();
    builder.addCode( codeBlock.build() );
  }

  /**
   * Generate the getter that ensures that the access is reported.
   */
  @Nonnull
  private static MethodSpec buildObservableGetter( @Nonnull final ProcessingEnvironment processingEnv,
                                                   @Nonnull final ObservableDescriptor observable )
    throws ProcessorException
  {
    final ComponentDescriptor component = observable.getComponent();
    final ExecutableElement getter = observable.getGetter();
    final List<String> additionalSuppressions = new ArrayList<>();
    if ( TypesUtil.hasRawTypes( processingEnv, observable.getGetterType().getReturnType() ) &&
         isCollectionType( getter ) )
    {
      additionalSuppressions.add( "unchecked" );
    }
    additionalSuppressions.addAll( getAdditionalSuppressions( getter ) );
    final MethodSpec.Builder method =
      GeneratorUtil.overrideMethod( processingEnv, component.getElement(), getter, additionalSuppressions, true );

    generateNotDisposedInvariant( method, getter.getSimpleName().toString() );

    if ( observable.canReadOutsideTransaction() )
    {
      method.addStatement( "this.$N.reportObservedIfTrackingTransactionActive()", observable.getFieldName() );
    }
    else
    {
      method.addStatement( "this.$N.reportObserved()", observable.getFieldName() );
    }

    if ( observable.isAbstract() )
    {
      if ( observable.shouldGenerateUnmodifiableCollectionVariant() )
      {
        if ( observable.isGetterNonnull() )
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", AREZ_CLASSNAME );

          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N )", observable.getCollectionCacheDataFieldName() );
          guard.addStatement( "this.$N = $T.wrap( this.$N )",
                              observable.getCollectionCacheDataFieldName(),
                              COLLECTIONS_UTIL_CLASSNAME,
                              observable.getDataFieldName() );
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", observable.getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );

          block.addStatement( "return this.$N", observable.getDataFieldName() );
          block.endControlFlow();

          method.addCode( block.build() );
        }
        else
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", AREZ_CLASSNAME );

          final String result = "$$ar$$_result";
          block.addStatement( "final $T $N = this.$N",
                              TypeName.get( observable.getGetterType().getReturnType() ),
                              result,
                              observable.getDataFieldName() );
          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N && null != $N )",
                                  observable.getCollectionCacheDataFieldName(),
                                  result );
          guard.addStatement( "this.$N = $T.wrap( $N )",
                              observable.getCollectionCacheDataFieldName(),
                              COLLECTIONS_UTIL_CLASSNAME,
                              result );
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", observable.getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );

          block.addStatement( "return this.$N", observable.getDataFieldName() );
          block.endControlFlow();

          method.addCode( block.build() );
        }
      }
      else
      {
        method.addStatement( "return this.$N", observable.getDataFieldName() );
      }
    }
    else
    {
      if ( observable.shouldGenerateUnmodifiableCollectionVariant() )
      {
        if ( observable.isGetterNonnull() )
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", AREZ_CLASSNAME );

          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N )", observable.getCollectionCacheDataFieldName() );
          if ( component.isClassType() )
          {
            guard.addStatement( "this.$N = $T.wrap( super.$N() )",
                                observable.getCollectionCacheDataFieldName(),
                                COLLECTIONS_UTIL_CLASSNAME,
                                getter.getSimpleName() );
          }
          else
          {
            guard.addStatement( "this.$N = $T.wrap( $T.super.$N() )",
                                observable.getCollectionCacheDataFieldName(),
                                COLLECTIONS_UTIL_CLASSNAME,
                                component.getClassName(),
                                getter.getSimpleName() );
          }
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", observable.getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );
          if ( component.isClassType() )
          {
            block.addStatement( "return super.$N()", getter.getSimpleName() );
          }
          else
          {
            block.addStatement( "return $T.super.$N()", component.getClassName(), getter.getSimpleName() );
          }
          block.endControlFlow();

          method.addCode( block.build() );
        }
        else
        {
          final CodeBlock.Builder block = CodeBlock.builder();
          block.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )", AREZ_CLASSNAME );

          final String result = "$$ar$$_result";
          if ( component.isClassType() )
          {
            block.addStatement( "final $T $N = super.$N()",
                                TypeName.get( observable.getGetterType().getReturnType() ),
                                result,
                                getter.getSimpleName() );
          }
          else
          {
            block.addStatement( "final $T $N = $T.super.$N()",
                                TypeName.get( observable.getGetterType().getReturnType() ),
                                result,
                                component.getClassName(),
                                getter.getSimpleName() );
          }
          final CodeBlock.Builder guard = CodeBlock.builder();
          guard.beginControlFlow( "if ( null == this.$N && null != $N )",
                                  observable.getCollectionCacheDataFieldName(),
                                  result );
          guard.addStatement( "this.$N = $T.wrap( $N )",
                              observable.getCollectionCacheDataFieldName(),
                              COLLECTIONS_UTIL_CLASSNAME,
                              result );
          guard.endControlFlow();
          block.add( guard.build() );
          block.addStatement( "return $N", observable.getCollectionCacheDataFieldName() );

          block.nextControlFlow( "else" );

          if ( component.isClassType() )
          {
            block.addStatement( "return super.$N()", getter.getSimpleName() );
          }
          else
          {
            block.addStatement( "return $T.super.$N()", component.getClassName(), getter.getSimpleName() );
          }
          block.endControlFlow();

          method.addCode( block.build() );
        }
      }
      else
      {
        if ( component.isClassType() )
        {
          method.addStatement( "return super.$N()", getter.getSimpleName() );
        }
        else
        {
          method.addStatement( "return $T.super.$N()", component.getClassName(), getter.getSimpleName() );
        }
      }
    }
    return method.build();
  }

  private static void buildComponentStateRefMethods( @Nonnull final ProcessingEnvironment processingEnv,
                                                     @Nonnull final ComponentStateRefDescriptor componentStateRef,
                                                     @Nonnull final TypeElement typeElement,
                                                     @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    final ComponentStateRefDescriptor.State state = componentStateRef.getState();
    final String stateMethodName =
      ComponentStateRefDescriptor.State.READY == state ? "isReady" :
      ComponentStateRefDescriptor.State.CONSTRUCTED == state ? "isConstructed" :
      ComponentStateRefDescriptor.State.COMPLETE == state ? "isComplete" :
      "isDisposing";

    builder.addMethod( GeneratorUtil
                         .refMethod( processingEnv, typeElement, componentStateRef.getMethod() )
                         .addStatement( "return this.$N.$N()", KERNEL_FIELD_NAME, stateMethodName )
                         .build() );
  }

  @Nonnull
  private static MethodSpec buildInverseAddMethod( @Nonnull final InverseDescriptor inverse )
    throws ProcessorException
  {
    final ObservableDescriptor observable = inverse.getObservable();
    final String methodName = getInverseAddMethodName( observable.getName() );
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    if ( ElementsUtil.areTypesInDifferentPackage( inverse.getTargetType(), inverse.getComponent().getElement() ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    final String otherName = inverse.getOtherName();
    final ParameterSpec parameter =
      ParameterSpec.builder( TypeName.get( inverse.getTargetType().asType() ), otherName, Modifier.FINAL )
        .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
        .build();
    builder.addParameter( parameter );
    generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "this.$N.preReportChanged()", observable.getFieldName() );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.invariant( () -> !this.$N.contains( $N ), " +
                        "() -> \"Attempted to add reference '$N' to inverse '$N' " +
                        "but inverse already contained element. Inverse = \" + $N )",
                        GUARDS_CLASSNAME,
                        observable.getDataFieldName(),
                        otherName,
                        otherName,
                        observable.getName(),
                        observable.getFieldName() );
    block.endControlFlow();
    builder.addCode( block.build() );

    builder.addStatement( "this.$N.add( $N )", observable.getDataFieldName(), otherName );
    final CodeBlock.Builder clearCacheBlock = CodeBlock.builder();
    clearCacheBlock.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )",
                                      AREZ_CLASSNAME );
    clearCacheBlock.addStatement( "this.$N = null", observable.getCollectionCacheDataFieldName() );
    clearCacheBlock.endControlFlow();
    builder.addCode( clearCacheBlock.build() );
    builder.addStatement( "this.$N.reportChanged()", observable.getFieldName() );

    for ( final ExecutableElement hook : inverse.getPostInverseAddHooks() )
    {
      builder.addStatement( "$N( $N )", hook.getSimpleName(), otherName );
    }

    return builder.build();
  }

  @Nonnull
  private static MethodSpec buildInverseRemoveMethod( @Nonnull final InverseDescriptor inverse )
    throws ProcessorException
  {
    final ObservableDescriptor observable = inverse.getObservable();
    final String methodName = getInverseRemoveMethodName( observable.getName() );
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    if ( ElementsUtil.areTypesInDifferentPackage( inverse.getTargetType(), inverse.getComponent().getElement() ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    final String otherName = inverse.getOtherName();
    final ParameterSpec parameter =
      ParameterSpec.builder( TypeName.get( inverse.getTargetType().asType() ), otherName, Modifier.FINAL )
        .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
        .build();
    builder.addParameter( parameter );
    generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "this.$N.preReportChanged()", observable.getFieldName() );
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.invariant( () -> this.$N.contains( $N ), " +
                        "() -> \"Attempted to remove reference '$N' from inverse '$N' " +
                        "but inverse does not contain element. Inverse = \" + $N )",
                        GUARDS_CLASSNAME,
                        observable.getDataFieldName(),
                        otherName,
                        otherName,
                        observable.getName(),
                        observable.getFieldName() );
    block.endControlFlow();
    builder.addCode( block.build() );

    for ( final ExecutableElement hook : inverse.getPreInverseRemoveHooks() )
    {
      builder.addStatement( "$N( $N )", hook.getSimpleName(), otherName );
    }

    builder.addStatement( "this.$N.remove( $N )", observable.getDataFieldName(), otherName );
    final CodeBlock.Builder clearCacheBlock = CodeBlock.builder();
    clearCacheBlock.beginControlFlow( "if ( $T.areCollectionsPropertiesUnmodifiable() )",
                                      AREZ_CLASSNAME );
    clearCacheBlock.addStatement( "this.$N = null", observable.getCollectionCacheDataFieldName() );
    clearCacheBlock.endControlFlow();
    builder.addCode( clearCacheBlock.build() );
    builder.addStatement( "this.$N.reportChanged()", observable.getFieldName() );

    return builder.build();
  }

  @Nonnull
  private static MethodSpec buildInverseSetMethod( @Nonnull final InverseDescriptor inverse )
    throws ProcessorException
  {
    final Multiplicity multiplicity = inverse.getMultiplicity();
    final ObservableDescriptor observable = inverse.getObservable();
    final String methodName =
      Multiplicity.ONE == multiplicity ?
      getInverseSetMethodName( observable.getName() ) :
      getInverseZSetMethodName( observable.getName() );
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    if ( ElementsUtil.areTypesInDifferentPackage( inverse.getTargetType(), inverse.getComponent().getElement() ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    final String otherName = inverse.getOtherName();
    final ParameterSpec.Builder parameter =
      ParameterSpec.builder( TypeName.get( inverse.getTargetType().asType() ), otherName, Modifier.FINAL );
    if ( Multiplicity.ONE == multiplicity )
    {
      parameter.addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
    }
    else
    {
      parameter.addAnnotation( GeneratorUtil.NULLABLE_CLASSNAME );
    }
    builder.addParameter( parameter.build() );
    generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "this.$N.preReportChanged()", observable.getFieldName() );
    builder.addStatement( "this.$N = $N", observable.getDataFieldName(), otherName );
    builder.addStatement( "this.$N.reportChanged()", observable.getFieldName() );

    for ( final ExecutableElement hook : inverse.getPostInverseAddHooks() )
    {
      builder.addStatement( "$N( $N )", hook.getSimpleName(), otherName );
    }

    return builder.build();
  }

  @Nonnull
  private static MethodSpec buildInverseUnsetMethod( @Nonnull final InverseDescriptor inverse )
    throws ProcessorException
  {
    final ObservableDescriptor observable = inverse.getObservable();
    final String methodName =
      Multiplicity.ONE == inverse.getMultiplicity() ?
      getInverseUnsetMethodName( observable.getName() ) :
      getInverseZUnsetMethodName( observable.getName() );
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    if ( ElementsUtil.areTypesInDifferentPackage( inverse.getTargetType(), inverse.getComponent().getElement() ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    final String otherName = inverse.getOtherName();
    final ParameterSpec parameter =
      ParameterSpec.builder( TypeName.get( inverse.getTargetType().asType() ), otherName, Modifier.FINAL )
        .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
        .build();
    builder.addParameter( parameter );
    generateNotDisposedInvariant( builder, methodName );

    builder.addStatement( "this.$N.preReportChanged()", observable.getFieldName() );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( this.$N == $N )", observable.getDataFieldName(), otherName );
    for ( final ExecutableElement hook : inverse.getPreInverseRemoveHooks() )
    {
      block.addStatement( "$N( $N )", hook.getSimpleName(), otherName );
    }

    block.addStatement( "this.$N = null", observable.getDataFieldName() );
    block.addStatement( "this.$N.reportChanged()", observable.getFieldName() );
    block.endControlFlow();

    builder.addCode( block.build() );

    return builder.build();
  }

  private static void buildInverseVerify( @Nonnull final InverseDescriptor inverse,
                                          @Nonnull final CodeBlock.Builder code )
  {
    if ( Multiplicity.MANY == inverse.getMultiplicity() )
    {
      buildInverseManyVerify( inverse, code );
    }
    else
    {
      buildInverseSingularVerify( inverse, code );
    }
  }

  private static void buildInverseSingularVerify( @Nonnull final InverseDescriptor inverse,
                                                  @Nonnull final CodeBlock.Builder code )
  {
    final CodeBlock.Builder builder = CodeBlock.builder();
    builder.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    final ObservableDescriptor observable = inverse.getObservable();
    builder.addStatement( "$T.apiInvariant( () -> $T.isNotDisposed( this.$N ), () -> \"Inverse relationship " +
                          "named '$N' on component named '\" + this.$N.getName() + \"' contains disposed element " +
                          "'\" + this.$N + \"'\" )",
                          GUARDS_CLASSNAME,
                          DISPOSABLE_CLASSNAME,
                          observable.getDataFieldName(),
                          observable.getName(),
                          KERNEL_FIELD_NAME,
                          observable.getDataFieldName() );
    builder.endControlFlow();
    code.add( builder.build() );
  }

  private static void buildInverseManyVerify( @Nonnull final InverseDescriptor inverse,
                                              @Nonnull final CodeBlock.Builder code )
  {
    final CodeBlock.Builder builder = CodeBlock.builder();
    final ObservableDescriptor observable = inverse.getObservable();
    builder.beginControlFlow( "for( final $T element : this.$N )",
                              inverse.getTargetType(),
                              observable.getDataFieldName() );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.isNotDisposed( element ), () -> \"Inverse relationship " +
                        "named '$N' on component named '\" + this.$N.getName() + \"' contains disposed element " +
                        "'\" + element + \"'\" )",
                        GUARDS_CLASSNAME,
                        DISPOSABLE_CLASSNAME,
                        observable.getName(),
                        KERNEL_FIELD_NAME );
    block.endControlFlow();
    builder.add( block.build() );

    builder.endControlFlow();
    code.add( builder.build() );
  }

  private static void buildInverseInitializer( @Nonnull final InverseDescriptor inverse,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    if ( Multiplicity.MANY == inverse.getMultiplicity() )
    {
      final ObservableDescriptor observable = inverse.getObservable();
      final ParameterizedTypeName typeName =
        (ParameterizedTypeName) TypeName.get( observable.getGetter().getReturnType() );
      final boolean isList = List.class.getName().equals( typeName.rawType.toString() );
      builder.addStatement( "this.$N = new $T<>()",
                            observable.getDataFieldName(),
                            isList ? ArrayList.class : HashSet.class );
      builder.addStatement( "this.$N = null", observable.getCollectionCacheDataFieldName() );
    }
  }

  private static void buildInverseMethods( @Nonnull final InverseDescriptor inverse,
                                           @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    if ( Multiplicity.MANY == inverse.getMultiplicity() )
    {
      builder.addMethod( buildInverseAddMethod( inverse ) );
      builder.addMethod( buildInverseRemoveMethod( inverse ) );
    }
    else
    {
      builder.addMethod( buildInverseSetMethod( inverse ) );
      builder.addMethod( buildInverseUnsetMethod( inverse ) );
    }
  }

  private static void buildDependencyKeyField( @Nonnull final DependencyDescriptor dependency,
                                               @Nonnull final TypeSpec.Builder builder )
  {
    if ( dependency.needsKey() )
    {
      final FieldSpec.Builder field =
        FieldSpec.builder( TypeName.get( String.class ),
                           DEPENDENCY_KEY_PREFIX + dependency.getKeyName(),
                           Modifier.FINAL,
                           Modifier.PRIVATE ).
          addAnnotation( GeneratorUtil.NONNULL_CLASSNAME );
      builder.addField( field.build() );
    }
  }

  private static void buildDependencyKeyInitializer( @Nonnull final DependencyDescriptor dependency,
                                                     @Nonnull final MethodSpec.Builder builder )
  {
    if ( dependency.needsKey() )
    {
      final ComponentDescriptor component = dependency.getComponent();
      builder.addStatement( "this.$N = $N.class.getName() + $N + '.' + $S",
                            DEPENDENCY_KEY_PREFIX + dependency.getKeyName(),
                            component.getEnhancedClassName().simpleName(),
                            null == component.getComponentId() && !component.isIdRequired() ? "this" : ID_VAR_NAME,
                            dependency.getElement().getSimpleName() );
    }
  }

  private static void buildReferenceFields( @Nonnull final ReferenceDescriptor reference,
                                            @Nonnull final TypeSpec.Builder builder )
  {
    final FieldSpec.Builder field =
      FieldSpec.builder( TypeName.get( reference.getMethod().getReturnType() ),
                         reference.getFieldName(),
                         Modifier.PRIVATE ).
        addAnnotation( GeneratorUtil.NULLABLE_CLASSNAME );
    builder.addField( field.build() );
  }

  private static void buildReferenceMethods( @Nonnull final ProcessingEnvironment processingEnv,
                                             @Nonnull final ReferenceDescriptor reference,
                                             @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    builder.addMethod( buildReferenceMethod( processingEnv, reference ) );
    builder.addMethod( buildReferenceLinkMethod( reference ) );
    if ( reference.hasInverse() || reference.getComponent().shouldVerify() )
    {
      builder.addMethod( buildReferenceDelinkMethod( processingEnv, reference ) );
    }
  }

  @Nonnull
  private static MethodSpec buildReferenceMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                  @Nonnull final ReferenceDescriptor reference )
    throws ProcessorException
  {
    final ExecutableElement method = reference.getMethod();
    final ExecutableElement idMethod = reference.getIdMethod();

    final String methodName = method.getSimpleName().toString();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    GeneratorUtil.copyAccessModifiers( method, builder );
    GeneratorUtil.copyTypeParameters( reference.getMethodType(), builder );
    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv,
                                                        builder,
                                                        getAdditionalSuppressions( method ),
                                                        Collections.singletonList( method.asType() ) );
    GeneratorUtil.copyWhitelistedAnnotations( method, builder );

    builder.addAnnotation( Override.class );
    builder.returns( TypeName.get( method.getReturnType() ) );
    generateNotDisposedInvariant( builder, methodName );

    final ObservableDescriptor observable = reference.getObservable();
    if ( !"LAZY".equals( reference.getLinkType() ) )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
      if ( reference.isNullable() )
      {
        block.addStatement( "$T.apiInvariant( () -> null != $N || null == $N(), () -> \"Nullable reference method " +
                            "named '$N' invoked on component named '\" + this.$N.getName() + \"' and reference has not been " +
                            "resolved yet is not lazy. Id = \" + $N() )",
                            GUARDS_CLASSNAME,
                            reference.getFieldName(),
                            idMethod.getSimpleName(),
                            method.getSimpleName(),
                            KERNEL_FIELD_NAME,
                            idMethod.getSimpleName() );
      }
      else
      {
        block.addStatement( "$T.apiInvariant( () -> null != $N, () -> \"Nonnull reference method named '$N' " +
                            "invoked on component named '\" + this.$N.getName() + \"' but reference has not been resolved yet " +
                            "is not lazy. Id = \" + $N() )",
                            GUARDS_CLASSNAME,
                            reference.getFieldName(),
                            method.getSimpleName(),
                            KERNEL_FIELD_NAME,
                            idMethod.getSimpleName() );
      }
      block.endControlFlow();

      builder.addCode( block.build() );

      if ( null != observable )
      {
        if ( observable.canReadOutsideTransaction() )
        {
          builder.addStatement( "this.$N.reportObservedIfTrackingTransactionActive()", observable.getFieldName() );
        }
        else
        {
          builder.addStatement( "this.$N.reportObserved()", observable.getFieldName() );
        }
      }
    }
    else
    {
      if ( null == observable )
      {
        builder.addStatement( "this.$N()", reference.getLinkMethodName() );
      }
      else
      {
        final CodeBlock.Builder block = CodeBlock.builder();
        block.beginControlFlow( "if ( null == this.$N )", reference.getFieldName() );
        block.addStatement( "this.$N()", reference.getLinkMethodName() );
        block.nextControlFlow( "else" );
        if ( observable.canReadOutsideTransaction() )
        {
          block.addStatement( "this.$N.reportObservedIfTrackingTransactionActive()", observable.getFieldName() );
        }
        else
        {
          block.addStatement( "this.$N.reportObserved()", observable.getFieldName() );
        }
        block.endControlFlow();
        builder.addCode( block.build() );
      }
    }

    builder.addStatement( "return this.$N", reference.getFieldName() );
    return builder.build();
  }

  @Nonnull
  private static MethodSpec buildReferenceLinkMethod( @Nonnull final ReferenceDescriptor reference )
    throws ProcessorException
  {
    final String methodName = reference.getLinkMethodName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    builder.addModifiers( Modifier.PRIVATE );
    generateNotDisposedInvariant( builder, methodName );

    if ( "EAGER".equals( reference.getLinkType() ) )
    {
      /*
       * Linking under eager should always proceed and does not need a null check
       * as the link method only called when a link is required.
       */
      builder.addStatement( "final $T id = this.$N()",
                            reference.getIdMethod().getReturnType(),
                            reference.getIdMethod().getSimpleName() );
      if ( reference.isNullable() )
      {
        final CodeBlock.Builder nestedBlock = CodeBlock.builder();
        nestedBlock.beginControlFlow( "if ( null != id )" );
        buildReferenceLookup( reference, nestedBlock );
        nestedBlock.nextControlFlow( "else" );
        nestedBlock.addStatement( "this.$N = null", reference.getFieldName() );
        nestedBlock.endControlFlow();
        builder.addCode( nestedBlock.build() );
      }
      else
      {
        buildReferenceLookup( reference, builder );
      }
    }
    else
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( null == this.$N )", reference.getFieldName() );
      block.addStatement( "final $T id = this.$N()",
                          reference.getIdMethod().getReturnType(),
                          reference.getIdMethod().getSimpleName() );
      if ( reference.isNullable() )
      {
        final CodeBlock.Builder nestedBlock = CodeBlock.builder();
        nestedBlock.beginControlFlow( "if ( null != id )" );
        buildReferenceLookup( reference, nestedBlock );
        nestedBlock.endControlFlow();
        block.add( nestedBlock.build() );
      }
      else
      {
        buildReferenceLookup( reference, block );
      }
      block.endControlFlow();
      builder.addCode( block.build() );
    }
    return builder.build();
  }

  private static void buildReferenceLookup( @Nonnull final ReferenceDescriptor reference,
                                            @Nonnull final MethodSpec.Builder builder )
  {
    builder.addStatement( "this.$N = this.$N().findById( $T.class, id )",
                          reference.getFieldName(),
                          LOCATOR_METHOD_NAME,
                          reference.getMethod().getReturnType() );
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != $N, () -> \"Reference named '$N' " +
                        "on component named '\" + this.$N.getName() + \"' is unable to resolve entity of type $N " +
                        "and id = \" + $N() )",
                        GUARDS_CLASSNAME,
                        reference.getFieldName(),
                        reference.getName(),
                        KERNEL_FIELD_NAME,
                        reference.getMethod().getReturnType().toString(),
                        reference.getIdMethod().getSimpleName() );
    block.endControlFlow();
    builder.addCode( block.build() );
    if ( reference.hasInverse() )
    {
      final Multiplicity inverseMultiplicity = reference.getInverseMultiplicity();
      final String inverseName = reference.getInverseName();
      final String linkMethodName =
        Multiplicity.MANY == inverseMultiplicity ? getInverseAddMethodName( inverseName ) :
        Multiplicity.ONE == inverseMultiplicity ? getInverseSetMethodName( inverseName ) :
        getInverseZSetMethodName( inverseName );
      builder.addStatement( "( ($T) this.$N ).$N( this )",
                            reference.getArezClassName(),
                            reference.getFieldName(),
                            linkMethodName );
    }
  }

  private static void buildReferenceLookup( @Nonnull final ReferenceDescriptor reference,
                                            @Nonnull final CodeBlock.Builder builder )
  {
    builder.addStatement( "this.$N = this.$N().findById( $T.class, id )",
                          reference.getFieldName(),
                          LOCATOR_METHOD_NAME,
                          reference.getMethod().getReturnType() );
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != $N, () -> \"Reference named '$N' " +
                        "on component named '\" + this.$N.getName() + \"' is unable to resolve entity of type $N " +
                        "and id = \" + $N() )",
                        GUARDS_CLASSNAME,
                        reference.getFieldName(),
                        reference.getName(),
                        KERNEL_FIELD_NAME,
                        reference.getMethod().getReturnType().toString(),
                        reference.getIdMethod().getSimpleName() );
    block.endControlFlow();
    builder.add( block.build() );
    if ( reference.hasInverse() )
    {
      final Multiplicity inverseMultiplicity = reference.getInverseMultiplicity();
      final String inverseName = reference.getInverseName();
      final String linkMethodName =
        Multiplicity.MANY == inverseMultiplicity ? getInverseAddMethodName( inverseName ) :
        Multiplicity.ONE == inverseMultiplicity ? getInverseSetMethodName( inverseName ) :
        getInverseZSetMethodName( inverseName );
      builder.addStatement( "( ($T) this.$N ).$N( this )",
                            reference.getArezClassName(),
                            reference.getFieldName(),
                            linkMethodName );
    }
  }

  private static void buildReferenceDisposer( @Nonnull final ReferenceDescriptor reference,
                                              @Nonnull final MethodSpec.Builder builder )
  {
    if ( reference.hasInverse() )
    {
      builder.addStatement( "this.$N()", reference.getDelinkMethodName() );
    }
  }

  @Nonnull
  private static MethodSpec buildReferenceDelinkMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                        @Nonnull final ReferenceDescriptor reference )
    throws ProcessorException
  {
    final String methodName = reference.getDelinkMethodName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );

    final TypeElement typeElement =
      (TypeElement) processingEnv.getTypeUtils().asElement( reference.getMethod().getReturnType() );
    if ( ElementsUtil.areTypesInDifferentPackage( typeElement, reference.getComponent().getElement() ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    else if ( !reference.hasInverse() )
    {
      builder.addModifiers( Modifier.PRIVATE );
    }

    if ( reference.hasInverse() )
    {
      final String inverseName = reference.getInverseName();
      final CodeBlock.Builder nestedBlock = CodeBlock.builder();
      nestedBlock.beginControlFlow( "if ( null != $N && $T.isNotDisposed( $N ) )",
                                    reference.getFieldName(),
                                    DISPOSABLE_CLASSNAME,
                                    reference.getFieldName() );
      final Multiplicity inverseMultiplicity = reference.getInverseMultiplicity();
      final String delinkMethodName =
        Multiplicity.MANY == inverseMultiplicity ?
        getInverseRemoveMethodName( inverseName ) :
        Multiplicity.ONE == inverseMultiplicity ?
        getInverseUnsetMethodName( inverseName ) :
        getInverseZUnsetMethodName( inverseName );
      nestedBlock.addStatement( "( ($T) this.$N ).$N( this )",
                                reference.getArezClassName(),
                                reference.getFieldName(),
                                delinkMethodName );
      nestedBlock.endControlFlow();
      builder.addCode( nestedBlock.build() );
    }
    builder.addStatement( "this.$N = null", reference.getFieldName() );

    return builder.build();
  }

  private static void buildReferenceVerify( @Nonnull final ReferenceDescriptor reference,
                                            @Nonnull final CodeBlock.Builder builder )
  {
    final String name = reference.getName();
    final String idName = VARIABLE_PREFIX + name + "Id";
    final String refName = VARIABLE_PREFIX + name;

    final ExecutableElement idMethod = reference.getIdMethod();
    builder.addStatement( "final $T $N = this.$N()", idMethod.getReturnType(), idName, idMethod.getSimpleName() );
    if ( reference.isNullable() )
    {
      final CodeBlock.Builder nestedBlock = CodeBlock.builder();
      nestedBlock.beginControlFlow( "if ( null != $N )", idName );
      buildReferenceVerify( reference, nestedBlock, idName, refName );
      nestedBlock.endControlFlow();
      builder.add( nestedBlock.build() );
    }
    else
    {
      buildReferenceVerify( reference, builder, idName, refName );
    }
  }

  private static void buildReferenceVerify( @Nonnull final ReferenceDescriptor reference,
                                            @Nonnull final CodeBlock.Builder builder,
                                            @Nonnull final String idName,
                                            @Nonnull final String refName )
  {
    builder.addStatement( "final $T $N = this.$N().findById( $T.class, $N )",
                          reference.getMethod().getReturnType(),
                          refName,
                          LOCATOR_METHOD_NAME,
                          reference.getMethod().getReturnType(),
                          idName );
    builder.addStatement( "$T.apiInvariant( () -> null != $N, () -> \"Reference named '$N' " +
                          "on component named '\" + this.$N.getName() + \"' is unable to resolve entity of type $N " +
                          "and id = \" + $N() )",
                          GUARDS_CLASSNAME,
                          refName,
                          reference.getName(),
                          KERNEL_FIELD_NAME,
                          reference.getMethod().getReturnType().toString(),
                          reference.getIdMethod().getSimpleName() );
  }

  private static void buildObserveDisposer( @Nonnull final ObserveDescriptor observe,
                                            @Nonnull final MethodSpec.Builder codeBlock )
  {
    codeBlock.addStatement( "this.$N.dispose()", observe.getFieldName() );
  }

  private static void buildObserveMethods( @Nonnull final ProcessingEnvironment processingEnv,
                                           @Nonnull final ObserveDescriptor observe,
                                           @Nonnull final TypeSpec.Builder builder )
    throws ProcessorException
  {
    if ( observe.isInternalExecutor() )
    {
      builder.addMethod( buildObserve( processingEnv, observe ) );
    }
    else
    {
      builder.addMethod( buildObserveTrackWrapper( processingEnv, observe ) );
    }
    for ( final ExecutableElement refMethod : observe.getRefMethods() )
    {
      final ComponentDescriptor component = observe.getComponent();
      final MethodSpec.Builder method = refMethod( processingEnv, component.getElement(), refMethod );
      generateNotDisposedInvariant( method, refMethod.getSimpleName().toString() );
      builder.addMethod( method.addStatement( "return $N", observe.getFieldName() ).build() );
    }
    if ( observe.hasOnDepsChange() && !observe.getOnDepsChange().getParameters().isEmpty() )
    {
      builder.addMethod( buildNativeOnDepsChangeMethod( observe ) );
    }
  }

  @Nonnull
  private static MethodSpec buildNativeOnDepsChangeMethod( @Nonnull final ObserveDescriptor observe )
    throws ProcessorException
  {
    final ExecutableElement onDepsChange = observe.getOnDepsChange();
    final String methodName = FRAMEWORK_PREFIX + onDepsChange.getSimpleName();
    final MethodSpec.Builder builder = MethodSpec.methodBuilder( methodName );
    builder.addModifiers( Modifier.PRIVATE );

    generateNotDisposedInvariant( builder, methodName );
    final ComponentDescriptor component = observe.getComponent();
    if ( component.isClassType() )
    {
      builder.addStatement( "super.$N( $N )", onDepsChange.getSimpleName().toString(), observe.getFieldName() );
    }
    else
    {
      builder.addStatement( "$T.super.$N( $N )",
                            component.getClassName(),
                            onDepsChange.getSimpleName().toString(),
                            observe.getFieldName() );
    }

    return builder.build();
  }

  @Nonnull
  private static MethodSpec buildObserveTrackWrapper( @Nonnull final ProcessingEnvironment processingEnv,
                                                      @Nonnull final ObserveDescriptor observe )
    throws ProcessorException
  {
    assert observe.hasObserve();
    final ExecutableType methodType = observe.getMethodType();
    final ExecutableElement method = observe.getMethod();
    final String methodName = method.getSimpleName().toString();
    final ComponentDescriptor component = observe.getComponent();
    final MethodSpec.Builder builder = overrideMethod( processingEnv, component.getElement(), observe.getMethod() );

    final TypeMirror returnType = methodType.getReturnType();

    final boolean isProcedure = returnType.getKind() == TypeKind.VOID;
    final List<? extends TypeMirror> thrownTypes = method.getThrownTypes();
    final boolean isSafe = thrownTypes.isEmpty();

    final StringBuilder statement = new StringBuilder();
    final List<Object> params = new ArrayList<>();

    generateNotDisposedInvariant( builder, methodName );
    if ( !isProcedure )
    {
      statement.append( "return " );
    }
    statement.append( "this.$N.getContext()." );
    params.add( KERNEL_FIELD_NAME );

    if ( isProcedure && isSafe )
    {
      statement.append( "safeObserve" );
    }
    else if ( isProcedure )
    {
      statement.append( "observe" );
    }
    else if ( isSafe )
    {
      statement.append( "safeObserve" );
    }
    else
    {
      statement.append( "observe" );
    }

    statement.append( "( this.$N, " );
    params.add( observe.getFieldName() );

    if ( component.isClassType() )
    {
      statement.append( "() -> super.$N(" );
      params.add( method.getSimpleName() );
    }
    else
    {
      statement.append( "() -> $T.super.$N(" );
      params.add( component.getClassName() );
      params.add( method.getSimpleName() );
    }

    final List<? extends VariableElement> parameters = method.getParameters();
    final int paramCount = parameters.size();
    if ( 0 != paramCount )
    {
      statement.append( " " );
    }

    boolean firstParam = true;
    for ( final VariableElement element : parameters )
    {
      params.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        statement.append( ", " );
      }
      firstParam = false;
      statement.append( "$N" );
    }
    if ( 0 != paramCount )
    {
      statement.append( " " );
    }

    statement.append( "), " );
    if ( observe.isReportParameters() && !parameters.isEmpty() )
    {
      statement.append( "$T.areSpiesEnabled() ? new $T[] { " );
      params.add( AREZ_CLASSNAME );
      params.add( Object.class );
      firstParam = true;
      for ( final VariableElement parameter : parameters )
      {
        if ( !firstParam )
        {
          statement.append( ", " );
        }
        firstParam = false;
        params.add( parameter.getSimpleName().toString() );
        statement.append( "$N" );
      }
      statement.append( " } : null" );
    }
    else
    {
      statement.append( "null" );
    }
    statement.append( " )" );

    if ( isSafe )
    {
      builder.addStatement( statement.toString(), params.toArray() );
    }
    else
    {
      generateTryBlock( builder,
                        thrownTypes,
                        b -> b.addStatement( statement.toString(), params.toArray() ) );
    }

    return builder.build();
  }

  /**
   * Generate the observed wrapper.
   * This is wrapped to block user from directly invoking observed method.
   */
  @Nonnull
  private static MethodSpec buildObserve( @Nonnull final ProcessingEnvironment processingEnv,
                                          @Nonnull final ObserveDescriptor observe )
    throws ProcessorException
  {
    assert observe.hasObserve();
    final ComponentDescriptor component = observe.getComponent();
    final MethodSpec.Builder method = overrideMethod( processingEnv, component.getElement(), observe.getMethod() );

    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.fail( () -> \"Observe method named '$N' invoked but @Observe(executor=INTERNAL) " +
                        "annotated methods should only be invoked by the runtime.\" )",
                        GUARDS_CLASSNAME,
                        observe.getMethod().getSimpleName().toString() );
    block.endControlFlow();

    method.addCode( block.build() );
    // This super is generated so that the GWT compiler in production model will identify this as a method
    // that only contains a super invocation and will thus inline it. If the body is left empty then the
    // GWT compiler will be required to keep the empty method present because it can not determine that the
    // empty method will never be invoked.
    if ( component.isClassType() )
    {
      method.addStatement( "super.$N()", observe.getMethod().getSimpleName() );
    }
    else
    {
      method.addStatement( "$T.super.$N()", component.getClassName(), observe.getMethod().getSimpleName() );
    }

    return method.build();
  }

  private static boolean anyParametersNamed( @Nonnull final ExecutableElement element, @Nonnull final String name )
  {
    return element.getParameters().stream().anyMatch( p -> p.getSimpleName().toString().equals( name ) );
  }
}
