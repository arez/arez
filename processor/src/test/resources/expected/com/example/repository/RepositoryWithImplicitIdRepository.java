package com.example.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Arez;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PreDispose;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@ArezComponent(
    singleton = true
)
public class RepositoryWithImplicitIdRepository implements RepositoryWithImplicitIdBaseRepositoryExtension {
  private static final boolean $$arez$$_IMMUTABLE_RESULTS = "true".equals( System.getProperty( "arez.repositories_return_immutables", String.valueOf( System.getProperty( "arez.environment", "production" ).equals( "development" ) ) ) );
  ;

  private final Observable $$arez$$_observable = Arez.context().createObservable( Arez.context().areNamesEnabled() ? "RepositoryWithImplicitIdRepository.entities" : null );
  ;

  private final HashMap<Long, RepositoryWithImplicitId> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<RepositoryWithImplicitId> $$arez$$_entityList = Collections.unmodifiableCollection( $$arez$$_entities.values() );
  ;

  RepositoryWithImplicitIdRepository() {
  }

  @Nonnull
  public static RepositoryWithImplicitIdRepository newRepository() {
    return new Arez_RepositoryWithImplicitIdRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  public RepositoryWithImplicitId create(@Nonnull final String packageName, @Nonnull final String name) {
    final Arez_RepositoryWithImplicitId entity = new Arez_RepositoryWithImplicitId(packageName,name);
    $$arez$$_entities.put( entity.$$arez$$_id(), entity );
    $$arez$$_observable.reportChanged();
    return entity;
  }

  @PreDispose
  final void preDispose() {
    $$arez$$_entityList.forEach( e -> Disposable.dispose( e ) );
    $$arez$$_entities.clear();
    $$arez$$_observable.reportChanged();
  }

  public boolean contains(@Nonnull final RepositoryWithImplicitId entity) {
    return entity instanceof Arez_RepositoryWithImplicitId && $$arez$$_entities.containsKey( ((Arez_RepositoryWithImplicitId) entity).$$arez$$_id() );
  }

  public void destroy(@Nonnull final RepositoryWithImplicitId entity) {
    assert null != entity;
    if ( entity instanceof Arez_RepositoryWithImplicitId && null != $$arez$$_entities.remove( ((Arez_RepositoryWithImplicitId) entity).$$arez$$_id() ) ) {
      Disposable.dispose( entity );
      $$arez$$_observable.reportChanged();
    } else {
      Guards.fail( () -> "Called destroy() passing an entity that was not in the repository. Entity: " + entity );
    }
  }

  /**
   * Return the raw collection of entities in the repository.
   * This collection should not be exposed to the user but may be used be repository extensions when
   * they define custom queries. NOTE: use of this method marks the list as observed.
   */
  @Nonnull
  protected final Collection<RepositoryWithImplicitId> entities() {
    $$arez$$_observable.reportObserved();
    return $$arez$$_entityList;
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link toList(List)}.
   */
  @Nonnull
  protected final List<RepositoryWithImplicitId> wrap(@Nonnull final List<RepositoryWithImplicitId> list) {
    return $$arez$$_IMMUTABLE_RESULTS ? Collections.unmodifiableList( list ) : list;
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   */
  @Nonnull
  protected final List<RepositoryWithImplicitId> toList(@Nonnull final Stream<RepositoryWithImplicitId> stream) {
    return wrap( stream.collect( Collectors.toList() ) );
  }

  @Nonnull
  public final List<RepositoryWithImplicitId> findAll() {
    return toList( entities().stream() );
  }

  @Nonnull
  public final List<RepositoryWithImplicitId> findAll(@Nonnull final Comparator<RepositoryWithImplicitId> sorter) {
    return toList( entities().stream().sorted( sorter ) );
  }

  @Nonnull
  public final List<RepositoryWithImplicitId> findAllByQuery(@Nonnull final Predicate<RepositoryWithImplicitId> query) {
    return toList( entities().stream().filter( query ) );
  }

  @Nonnull
  public final List<RepositoryWithImplicitId> findAllByQuery(@Nonnull final Predicate<RepositoryWithImplicitId> query, @Nonnull final Comparator<RepositoryWithImplicitId> sorter) {
    return toList( entities().stream().filter( query ).sorted( sorter ) );
  }

  @Nullable
  public final RepositoryWithImplicitId findByQuery(@Nonnull final Predicate<RepositoryWithImplicitId> query) {
    return entities().stream().filter( query ).findFirst().orElse( null );
  }

  @Override
  @Nonnull
  public final RepositoryWithImplicitIdRepository self() {
    return this;
  }
}
