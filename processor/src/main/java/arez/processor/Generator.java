package arez.processor;

import com.google.auto.common.GeneratedAnnotationSpecs;
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
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@SuppressWarnings( "Duplicates" )
final class Generator
{
  static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );
  static final ClassName INJECT_CLASSNAME = ClassName.get( "javax.inject", "Inject" );
  static final ClassName SINGLETON_CLASSNAME = ClassName.get( "javax.inject", "Singleton" );
  private static final ClassName PROVIDER_CLASSNAME = ClassName.get( "javax.inject", "Provider" );
  static final ClassName DAGGER_BINDS_CLASSNAME = ClassName.get( "dagger", "Binds" );
  private static final ClassName DAGGER_PROVIDES_CLASSNAME = ClassName.get( "dagger", "Provides" );
  static final ClassName DAGGER_MODULE_CLASSNAME = ClassName.get( "dagger", "Module" );
  private static final ClassName DAGGER_SUBCOMPONENT_CLASSNAME = ClassName.get( "dagger", "Subcomponent" );
  static final ClassName GUARDS_CLASSNAME = ClassName.get( "org.realityforge.braincheck", "Guards" );
  static final ClassName AREZ_CLASSNAME = ClassName.get( "arez", "Arez" );
  static final ClassName FLAGS_CLASSNAME = ClassName.get( "arez", "Flags" );
  static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "arez", "ArezContext" );
  static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "arez", "ObservableValue" );
  static final ClassName OBSERVER_CLASSNAME = ClassName.get( "arez", "Observer" );
  static final ClassName COMPUTABLE_VALUE_CLASSNAME = ClassName.get( "arez", "ComputableValue" );
  static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "arez", "Disposable" );
  static final ClassName COMPONENT_CLASSNAME = ClassName.get( "arez", "Component" );
  static final ClassName FEATURE_CLASSNAME = ClassName.get( "arez.annotations", "Feature" );
  static final ClassName INJECT_MODE_CLASSNAME = ClassName.get( "arez.annotations", "InjectMode" );
  static final ClassName ACTION_CLASSNAME = ClassName.get( "arez.annotations", "Action" );
  static final ClassName ABSTRACT_REPOSITORY_CLASSNAME =
    ClassName.get( "arez.component.internal", "AbstractRepository" );
  static final ClassName KERNEL_CLASSNAME = ClassName.get( "arez.component.internal", "ComponentKernel" );
  static final ClassName MEMOIZE_CACHE_CLASSNAME = ClassName.get( "arez.component.internal", "MemoizeCache" );
  static final ClassName IDENTIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Identifiable" );
  static final ClassName COMPONENT_OBSERVABLE_CLASSNAME = ClassName.get( "arez.component", "ComponentObservable" );
  static final ClassName DISPOSE_TRACKABLE_CLASSNAME = ClassName.get( "arez.component", "DisposeTrackable" );
  static final ClassName DISPOSE_NOTIFIER_CLASSNAME = ClassName.get( "arez.component", "DisposeNotifier" );
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
  static final String ENHANCER_PARAM_NAME = FRAMEWORK_PREFIX + "enhancer";
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

  private Generator()
  {
  }

  @Nonnull
  static TypeSpec buildProviderDaggerComponentExtension( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( descriptor.getDaggerComponentExtensionClassName() );
    addGeneratedAnnotation( descriptor, builder );
    addOriginatingTypes( descriptor.getElement(), builder );

    builder.addModifiers( Modifier.PUBLIC );

    builder.addMethod( MethodSpec.methodBuilder( "createProvider" ).
      addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC ).
      returns( ParameterizedTypeName.get( PROVIDER_CLASSNAME, descriptor.getClassName() ) ).build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "inject" )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .addParameter( ParameterSpec
                                          .builder( descriptor.getEnhancedClassName(), "component" )
                                          .addAnnotation( NONNULL_CLASSNAME )
                                          .build() )
                         .build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "bind" + descriptor.getType() )
                         .addModifiers( Modifier.PUBLIC, Modifier.DEFAULT )
                         .addStatement( "InjectSupport.c_enhancer = this::inject" )
                         .build() );

    builder.addType( buildInjectSupport( descriptor ) );
    builder.addType( buildDaggerModule( descriptor ) );
    builder.addType( buildEnhancerDaggerModule( descriptor ) );

    return builder.build();
  }

  @Nonnull
  static TypeSpec buildConsumerDaggerComponentExtension( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( descriptor.getDaggerComponentExtensionClassName() );
    addGeneratedAnnotation( descriptor, builder );
    addOriginatingTypes( descriptor.getElement(), builder );

    builder.addModifiers( Modifier.PUBLIC );

    {
      final MethodSpec.Builder method =
        MethodSpec.methodBuilder( "get" + descriptor.getType() + "DaggerSubcomponent" ).
          addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT ).
          returns( ClassName.bestGuess( "DaggerSubcomponent" ) );
      builder.addMethod( method.build() );
    }
    {
      final MethodSpec.Builder method =
        MethodSpec.methodBuilder( "bind" + descriptor.getType() ).
          addModifiers( Modifier.PUBLIC, Modifier.DEFAULT );
      method.addStatement( "InjectSupport.c_enhancer = instance -> $N().inject( instance )",
                           "get" + descriptor.getType() + "DaggerSubcomponent" );
      builder.addMethod( method.build() );
    }

    builder.addType( buildInjectSupport( descriptor ) );
    builder.addType( buildDaggerModule( descriptor ) );
    builder.addType( buildEnhancerDaggerModule( descriptor ) );
    builder.addType( buildConsumerDaggerSubcomponent( descriptor ) );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildConsumerDaggerSubcomponent( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( "DaggerSubcomponent" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    builder.addAnnotation( AnnotationSpec
                             .builder( DAGGER_SUBCOMPONENT_CLASSNAME )
                             .addMember( "modules", "DaggerModule.class" )
                             .build() );

    builder.addMethod( MethodSpec.methodBuilder( "createProvider" ).
      addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC ).
      returns( ParameterizedTypeName.get( PROVIDER_CLASSNAME, descriptor.getClassName() ) ).build() );

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

  @Nonnull
  private static TypeSpec buildDaggerModule( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( "DaggerModule" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    builder.addAnnotation( AnnotationSpec.builder( DAGGER_MODULE_CLASSNAME )
                             .addMember( "includes", "EnhancerDaggerModule.class" )
                             .build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "bindComponent" )
                         .addAnnotation( DAGGER_BINDS_CLASSNAME )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .addParameter( descriptor.getClassNameToConstruct(), "component" )
                         .returns( descriptor.getClassName() )
                         .build() );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildInjectSupport( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "InjectSupport" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL );

    builder.addField( FieldSpec
                        .builder( descriptor.getEnhancedClassName().nestedClass( "Enhancer" ), "c_enhancer" )
                        .addModifiers( Modifier.PRIVATE, Modifier.STATIC ).build() );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildEnhancerDaggerModule( @Nonnull final ComponentDescriptor descriptor )
  {
    // Dues to a bug in javapoet that disallows static methods as part of interface classes
    // we synthesize a separate module and it include it in main component to achieve the same
    // goal. In an ideal world this static method would be merged into above class
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "EnhancerDaggerModule" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    builder.addAnnotation( DAGGER_MODULE_CLASSNAME );

    final MethodSpec.Builder method = MethodSpec
      .methodBuilder( "provideEnhancer" )
      .addAnnotation( DAGGER_PROVIDES_CLASSNAME )
      .addModifiers( Modifier.STATIC )
      .returns( descriptor.getEnhancerClassName() );
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> null != InjectSupport.c_enhancer, " +
                        "() -> \"Attempted to create an instance of the Arez component named '$N' before " +
                        "the dependency injection provider has been initialized. Please see " +
                        "the documentation at https://arez.github.io/docs/dependency_injection.html for " +
                        "directions how to configure dependency injection.\" )",
                        GUARDS_CLASSNAME,
                        descriptor.getType() );
    block.endControlFlow();

    method.addCode( block.build() );

    method.addStatement( "return InjectSupport.c_enhancer" );
    builder.addMethod( method.build() );

    return builder.build();
  }

  static void addOriginatingTypes( @Nonnull final TypeElement element, @Nonnull final TypeSpec.Builder builder )
  {
    builder.addOriginatingElement( element );
    ProcessorUtil.getSuperTypes( element ).forEach( builder::addOriginatingElement );
  }

  static void addGeneratedAnnotation( @Nonnull final ComponentDescriptor descriptor,
                                      @Nonnull final TypeSpec.Builder builder )
  {
    GeneratedAnnotationSpecs
      .generatedAnnotationSpec( descriptor.getElements(), descriptor.getSourceVersion(), ArezProcessor.class )
      .ifPresent( builder::addAnnotation );
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
                        "( null == this.$N ? '?' : this.$N.getName() ) + \"'\" )",
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
                        "( null == this.$N ? '?' : this.$N.getName() ) + \"'\" )",
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
                        "named '\" + ( null == this.$N ? '?' : this.$N.getName() ) + \"'\" )",
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

  static void generateTryBlock( @Nonnull final CodeBlock.Builder builder,
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
    builder.add( codeBlock.build() );
  }
}
