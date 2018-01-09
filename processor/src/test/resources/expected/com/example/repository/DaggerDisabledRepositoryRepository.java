package com.example.repository;

import arez.Arez;
import arez.Disposable;
import arez.Observable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Injectible;
import arez.annotations.ObservableRef;
import arez.annotations.PreDispose;
import arez.component.NoResultException;
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
    nameIncludesId = false,
    dagger = Injectible.FALSE
)
@Singleton
public class DaggerDisabledRepositoryRepository {
  private final HashMap<Long, DaggerDisabledRepository> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<DaggerDisabledRepository> $$arez$$_entityList = Collections.unmodifiableCollection( this.$$arez$$_entities.values() );
  ;

  DaggerDisabledRepositoryRepository() {
  }

  @Nonnull
  public static DaggerDisabledRepositoryRepository newRepository() {
    return new Arez_DaggerDisabledRepositoryRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  DaggerDisabledRepository create(@Nonnull final String name) {
    final Arez_DaggerDisabledRepository entity = new Arez_DaggerDisabledRepository(name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    this.$$arez$$_entities.put( entity.$$arez$$_id(), entity );
    getEntitiesObservable().reportChanged();
    return entity;
  }

  @PreDispose
  final void preDispose() {
    this.$$arez$$_entityList.forEach( e -> Disposable.dispose( e ) );
    this.$$arez$$_entities.clear();
    getEntitiesObservable().reportChanged();
  }

  public boolean contains(@Nonnull final DaggerDisabledRepository entity) {
    getEntitiesObservable().reportObserved();
    return entity instanceof Arez_DaggerDisabledRepository && this.$$arez$$_entities.containsKey( ((Arez_DaggerDisabledRepository) entity).$$arez$$_id() );
  }

  @Action
  public void destroy(@Nonnull final DaggerDisabledRepository entity) {
    assert null != entity;
    if ( entity instanceof Arez_DaggerDisabledRepository && null != this.$$arez$$_entities.remove( ((Arez_DaggerDisabledRepository) entity).$$arez$$_id() ) ) {
      ((Arez_DaggerDisabledRepository) entity).$$arez$$_setOnDispose( null );
      Disposable.dispose( entity );
      getEntitiesObservable().reportChanged();
    } else {
      Guards.fail( () -> "Called destroy() passing an entity that was not in the repository. Entity: " + entity );
    }
  }

  @ObservableRef
  Observable getEntitiesObservable() {
    throw new IllegalStateException();
  }

  /**
   * Return the raw collection of entities in the repository.
   * This collection should not be exposed to the user but may be used be repository extensions when
   * they define custom queries. NOTE: use of this method marks the list as observed.
   */
  @arez.annotations.Observable(
      expectSetter = false
  )
  @Nonnull
  protected Collection<DaggerDisabledRepository> entities() {
    return $$arez$$_entityList;
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link #toList(List)}.
   */
  @Nonnull
  protected final List<DaggerDisabledRepository> wrap(@Nonnull final List<DaggerDisabledRepository> list) {
    return Arez.areRepositoryResultsModifiable() ? Collections.unmodifiableList( list ) : list;
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   */
  @Nonnull
  protected final List<DaggerDisabledRepository> toList(@Nonnull final Stream<DaggerDisabledRepository> stream) {
    return wrap( stream.collect( Collectors.toList() ) );
  }

  @Nonnull
  public final List<DaggerDisabledRepository> findAll() {
    return toList( entities().stream() );
  }

  @Nonnull
  public final List<DaggerDisabledRepository> findAll(@Nonnull final Comparator<DaggerDisabledRepository> sorter) {
    return toList( entities().stream().sorted( sorter ) );
  }

  @Nonnull
  public final List<DaggerDisabledRepository> findAllByQuery(@Nonnull final Predicate<DaggerDisabledRepository> query) {
    return toList( entities().stream().filter( query ) );
  }

  @Nonnull
  public final List<DaggerDisabledRepository> findAllByQuery(@Nonnull final Predicate<DaggerDisabledRepository> query,
      @Nonnull final Comparator<DaggerDisabledRepository> sorter) {
    return toList( entities().stream().filter( query ).sorted( sorter ) );
  }

  @Nullable
  public final DaggerDisabledRepository findByQuery(@Nonnull final Predicate<DaggerDisabledRepository> query) {
    return entities().stream().filter( query ).findFirst().orElse( null );
  }

  @Nonnull
  public final DaggerDisabledRepository getByQuery(@Nonnull final Predicate<DaggerDisabledRepository> query) {
    final DaggerDisabledRepository entity = findByQuery( query );
    if ( null == entity ) {
      throw new NoResultException();
    }
    return entity;
  }

  @Nonnull
  public final DaggerDisabledRepositoryRepository self() {
    return this;
  }
}
