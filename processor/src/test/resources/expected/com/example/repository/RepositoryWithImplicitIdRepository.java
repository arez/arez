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
public class RepositoryWithImplicitIdRepository implements RepositoryWithImplicitIdBaseRepositoryExtension {
  private final HashMap<Long, RepositoryWithImplicitId> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<RepositoryWithImplicitId> $$arez$$_entityList = Collections.unmodifiableCollection( this.$$arez$$_entities.values() );
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
  RepositoryWithImplicitId create(@Nonnull final String packageName, @Nonnull final String name) {
    final Arez_RepositoryWithImplicitId entity = new Arez_RepositoryWithImplicitId(packageName,name);
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

  public boolean contains(@Nonnull final RepositoryWithImplicitId entity) {
    getEntitiesObservable().reportObserved();
    return entity instanceof Arez_RepositoryWithImplicitId && this.$$arez$$_entities.containsKey( ((Arez_RepositoryWithImplicitId) entity).$$arez$$_id() );
  }

  @Action
  public void destroy(@Nonnull final RepositoryWithImplicitId entity) {
    assert null != entity;
    if ( entity instanceof Arez_RepositoryWithImplicitId && null != this.$$arez$$_entities.remove( ((Arez_RepositoryWithImplicitId) entity).$$arez$$_id() ) ) {
      ((Arez_RepositoryWithImplicitId) entity).$$arez$$_setOnDispose( null );
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
  protected Collection<RepositoryWithImplicitId> entities() {
    return $$arez$$_entityList;
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link #toList(List)}.
   */
  @Nonnull
  protected final List<RepositoryWithImplicitId> wrap(@Nonnull final List<RepositoryWithImplicitId> list) {
    return Arez.areRepositoryResultsModifiable() ? Collections.unmodifiableList( list ) : list;
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
  public final List<RepositoryWithImplicitId> findAllByQuery(@Nonnull final Predicate<RepositoryWithImplicitId> query,
      @Nonnull final Comparator<RepositoryWithImplicitId> sorter) {
    return toList( entities().stream().filter( query ).sorted( sorter ) );
  }

  @Nullable
  public final RepositoryWithImplicitId findByQuery(@Nonnull final Predicate<RepositoryWithImplicitId> query) {
    return entities().stream().filter( query ).findFirst().orElse( null );
  }

  @Nonnull
  public final RepositoryWithImplicitId getByQuery(@Nonnull final Predicate<RepositoryWithImplicitId> query) {
    final RepositoryWithImplicitId entity = findByQuery( query );
    if ( null == entity ) {
      throw new NoResultException();
    }
    return entity;
  }

  @Override
  @Nonnull
  public final RepositoryWithImplicitIdRepository self() {
    return this;
  }
}
