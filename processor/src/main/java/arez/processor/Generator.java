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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@SuppressWarnings( "Duplicates" )
final class Generator
{
  static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );
  static final ClassName INJECT_CLASSNAME = ClassName.get( "javax.inject", "Inject" );
  static final ClassName SINGLETON_CLASSNAME = ClassName.get( "javax.inject", "Singleton" );
  static final ClassName DAGGER_BINDS_CLASSNAME = ClassName.get( "dagger", "Binds" );
  static final ClassName DAGGER_MODULE_CLASSNAME = ClassName.get( "dagger", "Module" );
  private static final ClassName DAGGER_SUBCOMPONENT_CLASSNAME = ClassName.get( "dagger", "Subcomponent" );
  static final ClassName GUARDS_CLASSNAME = ClassName.get( "org.realityforge.braincheck", "Guards" );
  static final ClassName AREZ_CLASSNAME = ClassName.get( "arez", "Arez" );
  static final ClassName ACTION_FLAGS_CLASSNAME = ClassName.get( "arez", "ActionFlags" );
  static final ClassName OBSERVER_FLAGS_CLASSNAME = ClassName.get( "arez", "Observer", "Flags" );
  static final ClassName COMPUTABLE_VALUE_FLAGS_CLASSNAME = ClassName.get( "arez", "ComputableValue", "Flags" );
  static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "arez", "ArezContext" );
  static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "arez", "ObservableValue" );
  static final ClassName OBSERVER_CLASSNAME = ClassName.get( "arez", "Observer" );
  static final ClassName COMPUTABLE_VALUE_CLASSNAME = ClassName.get( "arez", "ComputableValue" );
  static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "arez", "Disposable" );
  static final ClassName COMPONENT_CLASSNAME = ClassName.get( "arez", "Component" );
  static final ClassName SAFE_PROCEDURE_CLASSNAME = ClassName.get( "arez", "SafeProcedure" );
  static final ClassName FEATURE_CLASSNAME = ClassName.get( "arez.annotations", "Feature" );
  static final ClassName INJECT_MODE_CLASSNAME = ClassName.get( "arez.annotations", "InjectMode" );
  static final ClassName ACTION_CLASSNAME = ClassName.get( "arez.annotations", "Action" );
  static final ClassName ABSTRACT_REPOSITORY_CLASSNAME =
    ClassName.get( "arez.component.internal", "AbstractRepository" );
  static final ClassName KERNEL_CLASSNAME = ClassName.get( "arez.component.internal", "ComponentKernel" );
  static final ClassName MEMOIZE_CACHE_CLASSNAME = ClassName.get( "arez.component.internal", "MemoizeCache" );
  static final ClassName IDENTIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Identifiable" );
  static final ClassName COMPONENT_OBSERVABLE_CLASSNAME = ClassName.get( "arez.component", "ComponentObservable" );
  static final ClassName DISPOSE_TRACKABLE_CLASSNAME = ClassName.get( "arez.component", "DisposeNotifier" );
  static final ClassName COLLECTIONS_UTIL_CLASSNAME = ClassName.get( "arez.component", "CollectionsUtil" );
  static final ClassName LOCATOR_CLASSNAME = ClassName.get( "arez", "Locator" );
  static final ClassName LINKABLE_CLASSNAME = ClassName.get( "arez.component", "Linkable" );
  static final ClassName VERIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Verifiable" );
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
  static final String INTERNAL_DISPOSE_METHOD_NAME = FRAMEWORK_PREFIX + "dispose";
  static final String INTERNAL_NATIVE_COMPONENT_PRE_DISPOSE_METHOD_NAME =
    FRAMEWORK_PREFIX + "nativeComponentPreDispose";
  static final String INTERNAL_PRE_DISPOSE_METHOD_NAME = FRAMEWORK_PREFIX + "preDispose";
  static final String NEXT_ID_FIELD_NAME = FRAMEWORK_PREFIX + "nextId";
  static final String KERNEL_FIELD_NAME = FRAMEWORK_PREFIX + "kernel";
  static final String ID_FIELD_NAME = FRAMEWORK_PREFIX + "id";
  static final String LOCATOR_METHOD_NAME = FRAMEWORK_PREFIX + "locator";
  /**
   * For constructor initializer args where it collides with existing name.
   */
  static final String INITIALIZER_PREFIX = "$$arezip$$_";
  /**
   * For variables used within generated methods that need a unique name.
   */
  static final String VARIABLE_PREFIX = "$$arezv$$_";
  static final String COMPONENT_VAR_NAME = VARIABLE_PREFIX + "component";
  static final String NAME_VAR_NAME = VARIABLE_PREFIX + "name";
  static final String ID_VAR_NAME = VARIABLE_PREFIX + "id";
  static final String CONTEXT_VAR_NAME = VARIABLE_PREFIX + "context";
  static final TypeKind DEFAULT_ID_KIND = TypeKind.INT;
  static final TypeName DEFAULT_ID_TYPE = TypeName.INT;
  /**
   * For fields that are synthesized to hold resolved references.
   */
  private static final String INVERSE_REFERENCE_METHOD_PREFIX = "$$arezir$$_";
  /**
   * The name of exceptions when caught by Arez infrastructure.
   */
  private static final String CAUGHT_THROWABLE_NAME = "$$arez_exception$$";
  @Nonnull
  private static final List<String> ANNOTATION_WHITELIST =
    Arrays.asList( Constants.NONNULL_ANNOTATION_CLASSNAME,
                   Constants.NULLABLE_ANNOTATION_CLASSNAME,
                   SuppressWarnings.class.getName(),
                   Deprecated.class.getName() );

  private Generator()
  {
  }

  @Nonnull
  static TypeSpec buildConsumerDaggerComponentExtension( @Nonnull final ProcessingEnvironment processingEnv,
                                                         @Nonnull final ComponentDescriptor descriptor )
  {
    assert descriptor.shouldGenerateFactory();
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( descriptor.getDaggerComponentExtensionClassName() );
    copyWhitelistedAnnotations( descriptor.getElement(), builder );
    addGeneratedAnnotation( processingEnv, builder );
    GeneratorUtil.addOriginatingTypes( descriptor.getElement(), builder );

    builder.addModifiers( Modifier.PUBLIC );

    {
      final MethodSpec.Builder method =
        MethodSpec.methodBuilder( "get" + descriptor.getType() + "DaggerSubcomponent" ).
          addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT ).
          returns( ClassName.bestGuess( "DaggerSubcomponent" ) );
      builder.addMethod( method.build() );
    }
    builder.addType( buildFactoryBasedDaggerSubcomponent( descriptor ) );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildFactoryBasedDaggerSubcomponent( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( "DaggerSubcomponent" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    final AnnotationSpec.Builder annotation = AnnotationSpec.builder( DAGGER_SUBCOMPONENT_CLASSNAME );
    builder.addAnnotation( annotation.build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "createFactory" )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .returns( descriptor.getEnhancedClassName().nestedClass( "Factory" ) )
                         .build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "inject" )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .addParameter( ParameterSpec
                                          .builder( descriptor.getEnhancedClassName(), "component" )
                                          .addAnnotation( NONNULL_CLASSNAME )
                                          .build() )
                         .build() );

    return builder.build();
  }

  static void addGeneratedAnnotation( @Nonnull final ProcessingEnvironment processingEnv,
                                      @Nonnull final TypeSpec.Builder builder )
  {
    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, ArezProcessor.class.getName() );
  }

  @Nonnull
  static String getInverseAddMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_add";
  }

  @Nonnull
  static String getInverseRemoveMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_remove";
  }

  @Nonnull
  static String getInverseSetMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_set";
  }

  @Nonnull
  static String getInverseUnsetMethodName( @Nonnull final String name )
  {
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_unset";
  }

  @Nonnull
  static String getInverseZSetMethodName( @Nonnull final String name )
  {
    // Use different names for linking/unlinking if there is different multiplicities to
    // avoid scenario where classes that are not consistent will be able to be loaded
    // by the jvm
    return INVERSE_REFERENCE_METHOD_PREFIX + name + "_zset";
  }

  @Nonnull
  static String getInverseZUnsetMethodName( @Nonnull final String name )
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

  static void generateNotInitializedInvariant( @Nonnull final ComponentDescriptor descriptor,
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
                        descriptor.getType() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateNotConstructedInvariant( @Nonnull final MethodSpec.Builder builder,
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

  static void generateNotCompleteInvariant( @Nonnull final MethodSpec.Builder builder,
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

  static void generateNotDisposedInvariant( @Nonnull final MethodSpec.Builder builder,
                                            @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != this.$N && this.$N.isActive(), " +
                        "() -> \"Method named '$N' invoked on \" + this.$N.describeState() + \" component " +
                        "named '\" + ( null == this.$N ? \"?\" : this.$N.getName() ) + \"'\" )",
                        GUARDS_CLASSNAME,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME,
                        methodName,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME,
                        KERNEL_FIELD_NAME );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateTryBlock( @Nonnull final MethodSpec.Builder builder,
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
    final ArrayList<Object> args = new ArrayList<>( expectedThrowTypes );
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

    args.add( CAUGHT_THROWABLE_NAME );

    final String code =
      "catch( final " +
      IntStream.range( 0, thrownCount ).mapToObj( t -> "$T" ).collect( Collectors.joining( " | " ) ) +
      " $N )";
    codeBlock.nextControlFlow( code, args.toArray() );
    codeBlock.addStatement( "throw $N", CAUGHT_THROWABLE_NAME );

    if ( !catchThrowable )
    {
      codeBlock.nextControlFlow( "catch( final $T $N )", Throwable.class, CAUGHT_THROWABLE_NAME );
      codeBlock.addStatement( "throw new $T( $N )", IllegalStateException.class, CAUGHT_THROWABLE_NAME );
    }
    codeBlock.endControlFlow();
    builder.addCode( codeBlock.build() );
  }

  static void buildInverseDisposer( @Nonnull final InverseDescriptor inverse,
                                    @Nonnull final MethodSpec.Builder builder )
  {
    final ObservableDescriptor observable = inverse.getObservable();
    final String delinkMethodName = getDelinkMethodName( inverse.getReferenceName() );
    final ClassName arezClassName = getArezClassName( inverse );
    if ( Multiplicity.MANY == inverse.getMultiplicity() )
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "for ( final $T other : new $T<>( $N ) )",
                              inverse.getTargetType(),
                              TypeName.get( ArrayList.class ),
                              observable.getDataFieldName() );
      block.addStatement( "( ($T) other ).$N()", arezClassName, delinkMethodName );
      block.endControlFlow();
      builder.addCode( block.build() );
    }
    else
    {
      final CodeBlock.Builder block = CodeBlock.builder();
      block.beginControlFlow( "if ( null != $N )", observable.getDataFieldName() );
      block.addStatement( "( ($T) $N ).$N()",
                          arezClassName,
                          observable.getDataFieldName(),
                          delinkMethodName );
      block.endControlFlow();
      builder.addCode( block.build() );
    }
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

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final TypeSpec.Builder builder )
  {
    GeneratorUtil.copyWhitelistedAnnotations( element, builder, ANNOTATION_WHITELIST );
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final MethodSpec.Builder builder )
  {
    GeneratorUtil.copyWhitelistedAnnotations( element, builder, ANNOTATION_WHITELIST );
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final ParameterSpec.Builder builder )
  {
    GeneratorUtil.copyWhitelistedAnnotations( element, builder, ANNOTATION_WHITELIST );
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final FieldSpec.Builder builder )
  {
    GeneratorUtil.copyWhitelistedAnnotations( element, builder, ANNOTATION_WHITELIST );
  }
}
