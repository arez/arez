package com.example.repository;

import arez.Arez;
import arez.Disposable;
import arez.Observable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ObservableRef;
import arez.annotations.PreDispose;
import arez.component.NoResultException;
import arez.component.NoSuchEntityException;
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
import javax.inject.Singleton;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@ArezComponent(
    nameIncludesId = false
)
@Singleton
public abstract class RepositoryWithExplicitIdRepository {
  private final HashMap<Integer, RepositoryWithExplicitId> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<RepositoryWithExplicitId> $$arez$$_entityList = Collections.unmodifiableCollection( this.$$arez$$_entities.values() );
  ;

  RepositoryWithExplicitIdRepository() {
  }

  @Nonnull
  public static RepositoryWithExplicitIdRepository newRepository() {
    return new Arez_RepositoryWithExplicitIdRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  RepositoryWithExplicitId create(@Nonnull final String packageName, @Nonnull final String name) {
    final Arez_RepositoryWithExplicitId entity = new Arez_RepositoryWithExplicitId(packageName,name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    this.$$arez$$_entities.put( entity.getId(), entity );
    getEntitiesObservable().reportChanged();
    return entity;
  }

  @PreDispose
  final void preDispose() {
    this.$$arez$$_entityList.forEach( e -> Disposable.dispose( e ) );
    this.$$arez$$_entities.clear();
    getEntitiesObservable().reportChanged();
  }

  public boolean contains(@Nonnull final RepositoryWithExplicitId entity) {
    getEntitiesObservable().reportObserved();
    return this.$$arez$$_entities.containsKey( entity.getId() );
  }

  @Action
  public void destroy(@Nonnull final RepositoryWithExplicitId entity) {
    assert null != entity;
    if ( null != this.$$arez$$_entities.remove( entity.getId() ) ) {
      ((Arez_RepositoryWithExplicitId) entity).$$arez$$_setOnDispose( null );
      Disposable.dispose( entity );
      getEntitiesObservable().reportChanged();
    } else {
      Guards.fail( () -> "Called destroy() passing an entity that was not in the repository. Entity: " + entity );
    }
  }

  @Nullable
  public RepositoryWithExplicitId findById(final int id) {
    getEntitiesObservable().reportObserved();
    return this.$$arez$$_entities.get( id );
  }

  @Nonnull
  public final RepositoryWithExplicitId getById(final int id) {
    final RepositoryWithExplicitId entity = findById( id );
    if ( null == entity ) {
      throw new NoSuchEntityException( RepositoryWithExplicitId.class, id );
    }
    return entity;
  }

  @ObservableRef
  abstract Observable getEntitiesObservable();

  /**
   * Return the raw collection of entities in the repository.
   * This collection should not be exposed to the user but may be used be repository extensions when
   * they define custom queries. NOTE: use of this method marks the list as observed.
   */
  @arez.annotations.Observable(
      expectSetter = false
  )
  @Nonnull
  protected Collection<RepositoryWithExplicitId> entities() {
    return $$arez$$_entityList;
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link #toList(List)}.
   */
  @Nonnull
  protected final List<RepositoryWithExplicitId> wrap(@Nonnull final List<RepositoryWithExplicitId> list) {
    return Arez.areRepositoryResultsModifiable() ? Collections.unmodifiableList( list ) : list;
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   */
  @Nonnull
  protected final List<RepositoryWithExplicitId> toList(@Nonnull final Stream<RepositoryWithExplicitId> stream) {
    return wrap( stream.collect( Collectors.toList() ) );
  }

  @Nonnull
  public final List<RepositoryWithExplicitId> findAll() {
    return toList( entities().stream() );
  }

  @Nonnull
  public final List<RepositoryWithExplicitId> findAll(@Nonnull final Comparator<RepositoryWithExplicitId> sorter) {
    return toList( entities().stream().sorted( sorter ) );
  }

  @Nonnull
  public final List<RepositoryWithExplicitId> findAllByQuery(@Nonnull final Predicate<RepositoryWithExplicitId> query) {
    return toList( entities().stream().filter( query ) );
  }

  @Nonnull
  public final List<RepositoryWithExplicitId> findAllByQuery(@Nonnull final Predicate<RepositoryWithExplicitId> query,
      @Nonnull final Comparator<RepositoryWithExplicitId> sorter) {
    return toList( entities().stream().filter( query ).sorted( sorter ) );
  }

  @Nullable
  public final RepositoryWithExplicitId findByQuery(@Nonnull final Predicate<RepositoryWithExplicitId> query) {
    return entities().stream().filter( query ).findFirst().orElse( null );
  }

  @Nonnull
  public final RepositoryWithExplicitId getByQuery(@Nonnull final Predicate<RepositoryWithExplicitId> query) {
    final RepositoryWithExplicitId entity = findByQuery( query );
    if ( null == entity ) {
      throw new NoResultException();
    }
    return entity;
  }

  @Nonnull
  public final RepositoryWithExplicitIdRepository self() {
    return this;
  }
}
