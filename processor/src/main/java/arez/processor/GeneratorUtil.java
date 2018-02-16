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
  static final ClassName DISPOSABLE_CLASSNAME = ClassName.get( "arez", "Disposable" );
  static final ClassName COMPONENT_CLASSNAME = ClassName.get( "arez", "Component" );
  static final ClassName INJECTIBLE_CLASSNAME = ClassName.get( "arez.annotations", "Feature" );
  static final ClassName ABSTRACT_REPOSITORY_CLASSNAME = ClassName.get( "arez.component", "AbstractRepository" );
  static final ClassName IDENTIFIABLE_CLASSNAME = ClassName.get( "arez.component", "Identifiable" );
  static final ClassName MEMOIZE_CACHE_CLASSNAME = ClassName.get( "arez.component", "MemoizeCache" );

  static final String FIELD_PREFIX = "$$arez$$_";
  static final String OBSERVABLE_FIELD_PREFIX = FIELD_PREFIX + "$$data$$_";
  private static final String CAUGHT_THROWABLE_NAME = FIELD_PREFIX + "e";
  /**
   * State field holds the state of entity.
   *  0 = initial value,
   *  1 = component constructor called, ArezContext assigned, synthetic Id generated (if required)
   *  2 = arez elements created, postConstruct called
   *  3 = scheduler triggered (if required), ready for operation
   * -2 = component dispose started
   * -1 = component dispose completed
   */
  static final String STATE_FIELD_NAME = FIELD_PREFIX + "state";
  static final String DISPOSED_OBSERVABLE_FIELD_NAME = FIELD_PREFIX + "disposedObservable";
  static final String ID_FIELD_NAME = FIELD_PREFIX + "id";
  static final String NAME_METHOD_NAME = FIELD_PREFIX + "name";
  static final String NEXT_ID_FIELD_NAME = FIELD_PREFIX + "nextId";
  static final String CONTEXT_FIELD_NAME = FIELD_PREFIX + "context";
  static final String COMPONENT_FIELD_NAME = FIELD_PREFIX + "component";

  private GeneratorUtil()
  {
  }

  static void generateNotInitializedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> this.$N == 0, () -> \"Method invoked on uninitialized component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        STATE_FIELD_NAME,
                        descriptor.getComponentNameMethodName() );
    block.endControlFlow();

    builder.addCode( block.build() );
  }

  static void generateNotConstructedInvariant( @Nonnull final ComponentDescriptor descriptor,
                                               @Nonnull final MethodSpec.Builder builder )
  {
    final CodeBlock.Builder block = CodeBlock.builder();
    block.beginControlFlow( "if ( $T.shouldCheckApiInvariants() )", AREZ_CLASSNAME );
    block.addStatement( "$T.apiInvariant( () -> this.$N == 0 || this.$N == 1, () -> \"Method invoked on un-constructed component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        STATE_FIELD_NAME,
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
    block.addStatement( "$T.apiInvariant( () -> this.$N >= 2, () -> \"Method invoked on dispos\" + (this.$N == -2 ? \"ing\" : \"ed\" ) + \" component named '\" + $N() + \"'\" )",
                        GUARDS_CLASSNAME,
                        STATE_FIELD_NAME,
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
