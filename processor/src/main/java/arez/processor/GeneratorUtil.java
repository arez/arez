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
  static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "arez", "ArezContext" );
  static final ClassName OBSERVABLE_CLASSNAME = ClassName.get( "arez", "Observable" );
  static final ClassName OBSERVER_CLASSNAME = ClassName.get( "arez", "Observer" );
  static final ClassName COMPUTED_VALUE_CLASSNAME = ClassName.get( "arez", "ComputedValue" );
  static final ClassName EQUALITY_COMPARATOR_CLASSNAME = ClassName.get( "arez", "EqualityComparator" );
  static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "arez", "Disposable" );
  static final ClassName COMPONENT_CLASSNAME = ClassName.get( "arez", "Component" );
  static final ClassName SAFE_FUNCTION_CLASSNAME = ClassName.get( "arez", "SafeFunction" );
  static final ClassName INJECTIBLE_CLASSNAME = ClassName.get( "arez.annotations", "Feature" );
  static final ClassName ABSTRACT_REPOSITORY_CLASSNAME = ClassName.get( "arez.component", "AbstractRepository" );
  static final ClassName IDENTIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Identifiable" );
  static final ClassName MEMOIZE_CACHE_CLASSNAME = ClassName.get( "arez.component", "MemoizeCache" );
  static final ClassName COMPONENT_STATE_CLASSNAME = ClassName.get( "arez.component", "ComponentState" );
  static final ClassName COMPONENT_OBSERVABLE_CLASSNAME = ClassName.get( "arez.component", "ComponentObservable" );

  /**
   * Prefix for fields that are used to generate Arez elements.
   */
  static final String FIELD_PREFIX = "$$arez$$_";
  /**
   * For fields that are synthesized to hold data for abstract observable properties.
   */
  static final String OBSERVABLE_DATA_FIELD_PREFIX = "$$arezd$$_";
  /**
   * For fields/elements used internally for component to manage lifecycle.
   */
  private static final String FRAMEWORK_PREFIX = "$$arezi$$_";
  /**
   * The name of exceptions when caught by Arez infrastructure.
   */
  private static final String CAUGHT_THROWABLE_NAME = "$$arez_exception$$";
  /**
   * The state of the entity.
   */
  static final String STATE_FIELD_NAME = FRAMEWORK_PREFIX + "state";
  static final String DISPOSED_OBSERVABLE_FIELD_NAME = FRAMEWORK_PREFIX + "disposedObservable";
  static final String DISPOSE_ON_DEACTIVATE_FIELD_NAME = FRAMEWORK_PREFIX + "disposeOnDeactivate";
  static final String ID_FIELD_NAME = FRAMEWORK_PREFIX + "id";
  static final String NAME_METHOD_NAME = FRAMEWORK_PREFIX + "name";
  static final String NEXT_ID_FIELD_NAME = FRAMEWORK_PREFIX + "nextId";
  static final String CONTEXT_FIELD_NAME = FRAMEWORK_PREFIX + "context";
  static final String COMPONENT_FIELD_NAME = FRAMEWORK_PREFIX + "component";
  static final String CASCADE_ON_DISPOSE_FIELD_NAME = FRAMEWORK_PREFIX + "cascadeOnDispose";
  static final String GET_CASCADE_ON_DISPOSE_DEPS_METHOD_NAME = FRAMEWORK_PREFIX + "getCascadeOnDisposeDependencies";
  static final String SET_NULL_ON_DISPOSE_FIELD_NAME = FRAMEWORK_PREFIX + "setNullOnDispose";
  static final String SET_NULL_ON_DISPOSE_METHOD_NAME = FRAMEWORK_PREFIX + "setNullOnDispose";
  static final String INTERNAL_OBSERVE_METHOD_NAME = FRAMEWORK_PREFIX + "observe";

  private GeneratorUtil()
  {
  }

  static void generateNotInitializedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.hasBeenInitialized( this.$N ), " +
                        "() -> \"Method invoked on uninitialized component of type '$N'\" )",
                        GUARDS_CLASSNAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        descriptor.getType() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateNotConstructedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.hasBeenConstructed( this.$N ), () -> \"Method invoked " +
                        "on un-constructed component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        descriptor.getComponentNameMethodName() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateNotCompleteInvariant( @Nonnull final ComponentDescriptor descriptor,
                                            @Nonnull final MethodSpec.Builder builder )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.hasBeenCompleted( this.$N ), () -> \"Method invoked " +
                        "on incomplete component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        descriptor.getComponentNameMethodName() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateNotDisposedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                            @Nonnull final MethodSpec.Builder builder )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> $T.isActive( this.$N ), () -> \"Method invoked " +
                        "on \" + $T.describe( this.$N ) + \" component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        COMPONENT_STATE_CLASSNAME,
                        STATE_FIELD_NAME,
                        descriptor.getComponentNameMethodName() );
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
    final ArrayList<Object> args = new ArrayList<>();
    args.addAll( expectedThrowTypes );
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
}
