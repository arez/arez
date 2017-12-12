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
import javax.inject.Singleton;
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
@Singleton
public class InjectEnabledRepositoryRepository implements InjectEnabledRepositoryBaseRepositoryExtension {
  private final HashMap<Long, InjectEnabledRepository> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<InjectEnabledRepository> $$arez$$_entityList = Collections.unmodifiableCollection( this.$$arez$$_entities.values() );
  ;

  InjectEnabledRepositoryRepository() {
  }

  @Nonnull
  public static InjectEnabledRepositoryRepository newRepository() {
    return new Arez_InjectEnabledRepositoryRepository();
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  InjectEnabledRepository create(@Nonnull final String name) {
    final Arez_InjectEnabledRepository entity = new Arez_InjectEnabledRepository(name);
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

  public boolean contains(@Nonnull final InjectEnabledRepository entity) {
    getEntitiesObservable().reportObserved();
    return entity instanceof Arez_InjectEnabledRepository && this.$$arez$$_entities.containsKey( ((Arez_InjectEnabledRepository) entity).$$arez$$_id() );
  }

  @Action
  public void destroy(@Nonnull final InjectEnabledRepository entity) {
    assert null != entity;
    if ( entity instanceof Arez_InjectEnabledRepository && null != this.$$arez$$_entities.remove( ((Arez_InjectEnabledRepository) entity).$$arez$$_id() ) ) {
      ((Arez_InjectEnabledRepository) entity).$$arez$$_setOnDispose( null );
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
  protected Collection<InjectEnabledRepository> entities() {
    return $$arez$$_entityList;
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link #toList(List)}.
   */
  @Nonnull
  protected final List<InjectEnabledRepository> wrap(@Nonnull final List<InjectEnabledRepository> list) {
    return Arez.areRepositoryResultsModifiable() ? Collections.unmodifiableList( list ) : list;
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   */
  @Nonnull
  protected final List<InjectEnabledRepository> toList(@Nonnull final Stream<InjectEnabledRepository> stream) {
    return wrap( stream.collect( Collectors.toList() ) );
  }

  @Nonnull
  public final List<InjectEnabledRepository> findAll() {
    return toList( entities().stream() );
  }

  @Nonnull
  public final List<InjectEnabledRepository> findAll(@Nonnull final Comparator<InjectEnabledRepository> sorter) {
    return toList( entities().stream().sorted( sorter ) );
  }

  @Nonnull
  public final List<InjectEnabledRepository> findAllByQuery(@Nonnull final Predicate<InjectEnabledRepository> query) {
    return toList( entities().stream().filter( query ) );
  }

  @Nonnull
  public final List<InjectEnabledRepository> findAllByQuery(@Nonnull final Predicate<InjectEnabledRepository> query,
      @Nonnull final Comparator<InjectEnabledRepository> sorter) {
    return toList( entities().stream().filter( query ).sorted( sorter ) );
  }

  @Nullable
  public final InjectEnabledRepository findByQuery(@Nonnull final Predicate<InjectEnabledRepository> query) {
    return entities().stream().filter( query ).findFirst().orElse( null );
  }

  @Nonnull
  public final InjectEnabledRepository getByQuery(@Nonnull final Predicate<InjectEnabledRepository> query) {
    final InjectEnabledRepository entity = findByQuery( query );
    if ( null == entity ) {
      throw new NoResultException();
    }
    return entity;
  }

  @Override
  @Nonnull
  public final InjectEnabledRepositoryRepository self() {
    return this;
  }
}
