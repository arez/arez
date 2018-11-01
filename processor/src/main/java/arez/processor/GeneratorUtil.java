package arez.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

@SuppressWarnings( "Duplicates" )
final class GeneratorUtil
{
  static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );
  static final ClassName INJECT_CLASSNAME = ClassName.get( "javax.inject", "Inject" );
  static final ClassName SINGLETON_CLASSNAME = ClassName.get( "javax.inject", "Singleton" );
  static final ClassName DAGGER_MODULE_CLASSNAME = ClassName.get( "dagger", "Module" );
  static final ClassName DAGGER_PROVIDES_CLASSNAME = ClassName.get( "dagger", "Provides" );
  static final ClassName GUARDS_CLASSNAME = ClassName.get( "org.realityforge.braincheck", "Guards" );
  static final ClassName AREZ_CLASSNAME = ClassName.get( "arez", "Arez" );
  static final ClassName FLAGS_CLASSNAME = ClassName.get( "arez", "Flags" );
  static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "arez", "ArezContext" );
  static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "arez", "ObservableValue" );
  static final ClassName OBSERVER_CLASSNAME = ClassName.get( "arez", "Observer" );
  static final ClassName COMPUTABLE_VALUE_CLASSNAME = ClassName.get( "arez", "ComputableValue" );
  static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "arez", "Disposable" );
  static final ClassName COMPONENT_CLASSNAME = ClassName.get( "arez", "Component" );
  static final ClassName INJECTIBLE_CLASSNAME = ClassName.get( "arez.annotations", "Feature" );
  static final ClassName ACTION_CLASSNAME = ClassName.get( "arez.annotations", "Action" );
  static final ClassName ABSTRACT_REPOSITORY_CLASSNAME = ClassName.get( "arez.component", "AbstractRepository" );
  static final ClassName IDENTIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Identifiable" );
  static final ClassName MEMOIZE_CACHE_CLASSNAME = ClassName.get( "arez.component", "MemoizeCache" );
  static final ClassName COMPONENT_STATE_CLASSNAME = ClassName.get( "arez.component", "ComponentState" );
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
   * For fields that are synthesized to hold resolved references.
   */
  private static final String INVERSE_REFERENCE_METHOD_PREFIX = "$$arezir$$_";
  /**
   * For methods/fields used internally for the component to manage lifecycle or implement support functionality.
   */
  static final String FRAMEWORK_PREFIX = "$$arezi$$_";
  /**
   * For constructor initializer args where it collides with existing name.
   */
  static final String INITIALIZER_PREFIX = "$$arezip$$_";
  /**
   * For variables used within generated methods that need a unique name.
   */
  static final String VARIABLE_PREFIX = "$$arezv$$_";
  /**
   * The name of exceptions when caught by Arez infrastructure.
   */
  private static final String CAUGHT_THROWABLE_NAME = "$$arez_exception$$";
  /**
   * The state of the entity.
   */
  static final String STATE_FIELD_NAME = FRAMEWORK_PREFIX + "state";
  static final String LOCATOR_METHOD_NAME = FRAMEWORK_PREFIX + "locator";
  static final String DISPOSED_OBSERVABLE_FIELD_NAME = FRAMEWORK_PREFIX + "disposedObservable";
  static final String DISPOSE_NOTIFIER_FIELD_NAME = FRAMEWORK_PREFIX + "disposeNotifier";
  static final String DISPOSE_ON_DEACTIVATE_FIELD_NAME = FRAMEWORK_PREFIX + "disposeOnDeactivate";
  static final String ID_FIELD_NAME = FRAMEWORK_PREFIX + "id";
  static final String NAME_METHOD_NAME = FRAMEWORK_PREFIX + "name";
  static final String NEXT_ID_FIELD_NAME = FRAMEWORK_PREFIX + "nextId";
  static final String CONTEXT_FIELD_NAME = FRAMEWORK_PREFIX + "context";
  static final String COMPONENT_FIELD_NAME = FRAMEWORK_PREFIX + "component";
  static final String INTERNAL_OBSERVE_METHOD_NAME = FRAMEWORK_PREFIX + "observe";
  static final String INTERNAL_PRE_DISPOSE_METHOD_NAME = FRAMEWORK_PREFIX + "preDispose";
  static final TypeKind DEFAULT_ID_KIND = TypeKind.INT;
  static final TypeName DEFAULT_ID_TYPE = TypeName.INT;

  private GeneratorUtil()
  {
  }

  @Nonnull
  static String getInverseAddMethodName( @Nonnull final String name )
  {
    return GeneratorUtil.INVERSE_REFERENCE_METHOD_PREFIX + name + "_add";
  }

  @Nonnull
  static String getInverseRemoveMethodName( @Nonnull final String name )
  {
    return GeneratorUtil.INVERSE_REFERENCE_METHOD_PREFIX + name + "_remove";
  }

  @Nonnull
  static String getInverseSetMethodName( @Nonnull final String name )
  {
    return GeneratorUtil.INVERSE_REFERENCE_METHOD_PREFIX + name + "_set";
  }

  @Nonnull
  static String getInverseUnsetMethodName( @Nonnull final String name )
  {
    return GeneratorUtil.INVERSE_REFERENCE_METHOD_PREFIX + name + "_unset";
  }

  @Nonnull
  static String getInverseZSetMethodName( @Nonnull final String name )
  {
    // Use different names for linking/unlinking if there is different multiplicities to
    // avoid scenario where classes that are not consistent will be able to be loaded
    // by the jvm
    return GeneratorUtil.INVERSE_REFERENCE_METHOD_PREFIX + name + "_zset";
  }

  @Nonnull
  static String getInverseZUnsetMethodName( @Nonnull final String name )
  {
    return GeneratorUtil.INVERSE_REFERENCE_METHOD_PREFIX + name + "_zunset";
  }

  @Nonnull
  static String getLinkMethodName( @Nonnull final String name )
  {
    return GeneratorUtil.FRAMEWORK_PREFIX + "link_" + name;
  }

  @Nonnull
  static String getDelinkMethodName( @Nonnull final String name )
  {
    return GeneratorUtil.FRAMEWORK_PREFIX + "delink_" + name;
  }

  static void generateNotInitializedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                               @Nonnull final MethodSpec.Builder builder,
                                               @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.hasBeenInitialized( this.$N ), " +
                        "() -> \"Method named '$N' invoked on uninitialized component of type '$N'\" )",
                        GUARDS_CLASSNAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        methodName,
                        descriptor.getType() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateNotConstructedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                               @Nonnull final MethodSpec.Builder builder,
                                               @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.hasBeenConstructed( this.$N ), () -> \"Method named '$N' invoked " +
                        "on un-constructed component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        methodName,
                        descriptor.getComponentNameMethodName() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateNotCompleteInvariant( @Nonnull final ComponentDescriptor descriptor,
                                            @Nonnull final MethodSpec.Builder builder,
                                            @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.hasBeenCompleted( this.$N ), () -> \"Method named '$N' invoked " +
                        "on incomplete component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        methodName,
                        descriptor.getComponentNameMethodName() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateNotDisposedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                            @Nonnull final MethodSpec.Builder builder,
                                            @Nonnull final String methodName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.isActive( this.$N ), () -> \"Method named '$N' invoked " +
                        "on \" + $T.describe( this.$N ) + \" component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        methodName,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        descriptor.getComponentNameMethodName() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void setStateForInvariantChecking( @Nonnull final MethodSpec.Builder builder, @Nonnull final String stateName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );

    block.addStatement( "this.$N = $T.$N",
                        GeneratorUtil.STATE_FIELD_NAME,
                        GeneratorUtil.COMPONENT_STATE_CLASSNAME,
                        stateName );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  @SuppressWarnings( "SameParameterValue" )
  static void setStateForInvariantChecking( @Nonnull final CodeBlock.Builder builder, @Nonnull final String stateName )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );

    block.addStatement( "this.$N = $T.$N",
                        GeneratorUtil.STATE_FIELD_NAME,
                        GeneratorUtil.COMPONENT_STATE_CLASSNAME,
                        stateName );
    block.endControlFlow();

    builder.add( block.build() );
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
