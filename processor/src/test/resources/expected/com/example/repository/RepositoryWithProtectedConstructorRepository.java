package com.example.repository;

import arez.Arez;
import arez.Disposable;
import arez.Observable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
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
    nameIncludesId = false
)
@Singleton
public abstract class RepositoryWithProtectedConstructorRepository {
  private final HashMap<Long, RepositoryWithProtectedConstructor> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<RepositoryWithProtectedConstructor> $$arez$$_entityList = Collections.unmodifiableCollection( this.$$arez$$_entities.values() );
  ;

  RepositoryWithProtectedConstructorRepository() {
  }

  @Nonnull
  public static RepositoryWithProtectedConstructorRepository newRepository() {
    return new Arez_RepositoryWithProtectedConstructorRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  protected RepositoryWithProtectedConstructor create(@Nonnull final String name) {
    final Arez_RepositoryWithProtectedConstructor entity = new Arez_RepositoryWithProtectedConstructor(name);
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

  public boolean contains(@Nonnull final RepositoryWithProtectedConstructor entity) {
    getEntitiesObservable().reportObserved();
    return entity instanceof Arez_RepositoryWithProtectedConstructor && this.$$arez$$_entities.containsKey( ((Arez_RepositoryWithProtectedConstructor) entity).$$arez$$_id() );
  }

  @Action
  public void destroy(@Nonnull final RepositoryWithProtectedConstructor entity) {
    assert null != entity;
    if ( entity instanceof Arez_RepositoryWithProtectedConstructor && null != this.$$arez$$_entities.remove( ((Arez_RepositoryWithProtectedConstructor) entity).$$arez$$_id() ) ) {
      ((Arez_RepositoryWithProtectedConstructor) entity).$$arez$$_setOnDispose( null );
      Disposable.dispose( entity );
      getEntitiesObservable().reportChanged();
    } else {
      Guards.fail( () -> "Called destroy() passing an entity that was not in the repository. Entity: " + entity );
    }
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
  protected Collection<RepositoryWithProtectedConstructor> entities() {
    return $$arez$$_entityList;
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link #toList(List)}.
   */
  @Nonnull
  protected final List<RepositoryWithProtectedConstructor> wrap(@Nonnull final List<RepositoryWithProtectedConstructor> list) {
    return Arez.areRepositoryResultsModifiable() ? Collections.unmodifiableList( list ) : list;
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   */
  @Nonnull
  protected final List<RepositoryWithProtectedConstructor> toList(@Nonnull final Stream<RepositoryWithProtectedConstructor> stream) {
    return wrap( stream.collect( Collectors.toList() ) );
  }

  @Nonnull
  public final List<RepositoryWithProtectedConstructor> findAll() {
    return toList( entities().stream() );
  }

  @Nonnull
  public final List<RepositoryWithProtectedConstructor> findAll(@Nonnull final Comparator<RepositoryWithProtectedConstructor> sorter) {
    return toList( entities().stream().sorted( sorter ) );
  }

  @Nonnull
  public final List<RepositoryWithProtectedConstructor> findAllByQuery(@Nonnull final Predicate<RepositoryWithProtectedConstructor> query) {
    return toList( entities().stream().filter( query ) );
  }

  @Nonnull
  public final List<RepositoryWithProtectedConstructor> findAllByQuery(@Nonnull final Predicate<RepositoryWithProtectedConstructor> query,
      @Nonnull final Comparator<RepositoryWithProtectedConstructor> sorter) {
    return toList( entities().stream().filter( query ).sorted( sorter ) );
  }

  @Nullable
  public final RepositoryWithProtectedConstructor findByQuery(@Nonnull final Predicate<RepositoryWithProtectedConstructor> query) {
    return entities().stream().filter( query ).findFirst().orElse( null );
  }

  @Nonnull
  public final RepositoryWithProtectedConstructor getByQuery(@Nonnull final Predicate<RepositoryWithProtectedConstructor> query) {
    final RepositoryWithProtectedConstructor entity = findByQuery( query );
    if ( null == entity ) {
      throw new NoResultException();
    }
    return entity;
  }

  @Nonnull
  public final RepositoryWithProtectedConstructorRepository self() {
    return this;
  }
}
