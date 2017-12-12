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
import org.realityforge.arez.annotations.Injectible;
import org.realityforge.arez.annotations.ObservableRef;
import org.realityforge.arez.annotations.PreDispose;
import org.realityforge.arez.component.NoResultException;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@ArezComponent(
    nameIncludesId = false,
    inject = Injectible.TRUE
)
public class RepositoryWithMultipleCtorsRepository implements RepositoryWithMultipleCtorsBaseRepositoryExtension {
  private final HashMap<Long, RepositoryWithMultipleCtors> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<RepositoryWithMultipleCtors> $$arez$$_entityList = Collections.unmodifiableCollection( this.$$arez$$_entities.values() );
  ;

  RepositoryWithMultipleCtorsRepository() {
  }

  @Nonnull
  public static RepositoryWithMultipleCtorsRepository newRepository() {
    return new Arez_RepositoryWithMultipleCtorsRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  RepositoryWithMultipleCtors create(@Nonnull final String packageName,
      @Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(packageName,name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    this.$$arez$$_entities.put( entity.$$arez$$_id(), entity );
    getEntitiesObservable().reportChanged();
    return entity;
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  RepositoryWithMultipleCtors create(@Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(name);
    entity.$$arez$$_setOnDispose( e -> destroy( e ) );
    this.$$arez$$_entities.put( entity.$$arez$$_id(), entity );
    getEntitiesObservable().reportChanged();
    return entity;
  }

  @Action(
      name = "create"
  )
  @Nonnull
  RepositoryWithMultipleCtors create() {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors();
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

  public boolean contains(@Nonnull final RepositoryWithMultipleCtors entity) {
    getEntitiesObservable().reportObserved();
    return entity instanceof Arez_RepositoryWithMultipleCtors && this.$$arez$$_entities.containsKey( ((Arez_RepositoryWithMultipleCtors) entity).$$arez$$_id() );
  }

  @Action
  public void destroy(@Nonnull final RepositoryWithMultipleCtors entity) {
    assert null != entity;
    if ( entity instanceof Arez_RepositoryWithMultipleCtors && null != this.$$arez$$_entities.remove( ((Arez_RepositoryWithMultipleCtors) entity).$$arez$$_id() ) ) {
      ((Arez_RepositoryWithMultipleCtors) entity).$$arez$$_setOnDispose( null );
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
  @org.realityforge.arez.annotations.Observable(
      expectSetter = false
  )
  @Nonnull
  protected Collection<RepositoryWithMultipleCtors> entities() {
    return $$arez$$_entityList;
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link #toList(List)}.
   */
  @Nonnull
  protected final List<RepositoryWithMultipleCtors> wrap(@Nonnull final List<RepositoryWithMultipleCtors> list) {
    return Arez.areRepositoryResultsModifiable() ? Collections.unmodifiableList( list ) : list;
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   */
  @Nonnull
  protected final List<RepositoryWithMultipleCtors> toList(@Nonnull final Stream<RepositoryWithMultipleCtors> stream) {
    return wrap( stream.collect( Collectors.toList() ) );
  }

  @Nonnull
  public final List<RepositoryWithMultipleCtors> findAll() {
    return toList( entities().stream() );
  }

  @Nonnull
  public final List<RepositoryWithMultipleCtors> findAll(@Nonnull final Comparator<RepositoryWithMultipleCtors> sorter) {
    return toList( entities().stream().sorted( sorter ) );
  }

  @Nonnull
  public final List<RepositoryWithMultipleCtors> findAllByQuery(@Nonnull final Predicate<RepositoryWithMultipleCtors> query) {
    return toList( entities().stream().filter( query ) );
  }

  @Nonnull
  public final List<RepositoryWithMultipleCtors> findAllByQuery(@Nonnull final Predicate<RepositoryWithMultipleCtors> query,
      @Nonnull final Comparator<RepositoryWithMultipleCtors> sorter) {
    return toList( entities().stream().filter( query ).sorted( sorter ) );
  }

  @Nullable
  public final RepositoryWithMultipleCtors findByQuery(@Nonnull final Predicate<RepositoryWithMultipleCtors> query) {
    return entities().stream().filter( query ).findFirst().orElse( null );
  }

  @Nonnull
  public final RepositoryWithMultipleCtors getByQuery(@Nonnull final Predicate<RepositoryWithMultipleCtors> query) {
    final RepositoryWithMultipleCtors entity = findByQuery( query );
    if ( null == entity ) {
      throw new NoResultException();
    }
    return entity;
  }

  @Override
  @Nonnull
  public final RepositoryWithMultipleCtorsRepository self() {
    return this;
  }
}
