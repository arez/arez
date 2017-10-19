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
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ObservableRef;
import org.realityforge.arez.annotations.PreDispose;
import org.realityforge.arez.component.NoResultException;
import org.realityforge.arez.component.NoSuchEntityException;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
@ArezComponent(
    singleton = true
)
public class CompleteRepositoryExampleRepository implements CompleteRepositoryExampleBaseRepositoryExtension, CompleteRepositoryExample.FooEx {
  private static final boolean $$arez$$_IMMUTABLE_RESULTS = "true".equals( System.getProperty( "arez.repositories_return_immutables", String.valueOf( System.getProperty( "arez.environment", "production" ).equals( "development" ) ) ) );
  ;

  private final HashMap<Integer, CompleteRepositoryExample> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<CompleteRepositoryExample> $$arez$$_entityList = Collections.unmodifiableCollection( $$arez$$_entities.values() );
  ;

  CompleteRepositoryExampleRepository() {
  }

  @Nonnull
  public static CompleteRepositoryExampleRepository newRepository() {
    return new Arez_CompleteRepositoryExampleRepository();
  }

  @Action(
      name = "create_packageName_name"
  )
  @Nonnull
  CompleteRepositoryExample create(@Nonnull final String packageName, @Nonnull final String name) {
    final Arez_CompleteRepositoryExample entity = new Arez_CompleteRepositoryExample(packageName,name);
    $$arez$$_entities.put( entity.getId(), entity );
    getEntitiesObservable().reportChanged();
    return entity;
  }

  @PreDispose
  final void preDispose() {
    $$arez$$_entityList.forEach( e -> Disposable.dispose( e ) );
    $$arez$$_entities.clear();
    getEntitiesObservable().reportChanged();
  }

  public boolean contains(@Nonnull final CompleteRepositoryExample entity) {
    getEntitiesObservable().reportObserved();
    return $$arez$$_entities.containsKey( entity.getId() );
  }

  @Action
  public void destroy(@Nonnull final CompleteRepositoryExample entity) {
    assert null != entity;
    if ( null != $$arez$$_entities.remove( entity.getId() ) ) {
      Disposable.dispose( entity );
      getEntitiesObservable().reportChanged();
    } else {
      Guards.fail( () -> "Called destroy() passing an entity that was not in the repository. Entity: " + entity );
    }
  }

  @Nullable
  public CompleteRepositoryExample findById(final int id) {
    getEntitiesObservable().reportObserved();
    return $$arez$$_entities.get( id );
  }

  @Nonnull
  public final CompleteRepositoryExample getById(final int id) {
    final CompleteRepositoryExample entity = findById( id );
    if ( null == entity ) {
      throw new NoSuchEntityException( CompleteRepositoryExample.class, id );
    }
    return entity;
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
  protected Collection<CompleteRepositoryExample> entities() {
    return $$arez$$_entityList;
  }

  /**
   * If config option enabled, wrap the specified list in an immutable list and return it.
   * This method should be called by repository extensions when returning list results when not using {@link toList(List)}.
   */
  @Nonnull
  protected final List<CompleteRepositoryExample> wrap(@Nonnull final List<CompleteRepositoryExample> list) {
    return $$arez$$_IMMUTABLE_RESULTS ? Collections.unmodifiableList( list ) : list;
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   */
  @Nonnull
  protected final List<CompleteRepositoryExample> toList(@Nonnull final Stream<CompleteRepositoryExample> stream) {
    return wrap( stream.collect( Collectors.toList() ) );
  }

  @Nonnull
  public final List<CompleteRepositoryExample> findAll() {
    return toList( entities().stream() );
  }

  @Nonnull
  public final List<CompleteRepositoryExample> findAll(@Nonnull final Comparator<CompleteRepositoryExample> sorter) {
    return toList( entities().stream().sorted( sorter ) );
  }

  @Nonnull
  public final List<CompleteRepositoryExample> findAllByQuery(@Nonnull final Predicate<CompleteRepositoryExample> query) {
    return toList( entities().stream().filter( query ) );
  }

  @Nonnull
  public final List<CompleteRepositoryExample> findAllByQuery(@Nonnull final Predicate<CompleteRepositoryExample> query, @Nonnull final Comparator<CompleteRepositoryExample> sorter) {
    return toList( entities().stream().filter( query ).sorted( sorter ) );
  }

  @Nullable
  public final CompleteRepositoryExample findByQuery(@Nonnull final Predicate<CompleteRepositoryExample> query) {
    return entities().stream().filter( query ).findFirst().orElse( null );
  }

  @Nonnull
  public final CompleteRepositoryExample getByQuery(@Nonnull final Predicate<CompleteRepositoryExample> query) {
    final CompleteRepositoryExample entity = findByQuery( query );
    if ( null == entity ) {
      throw new NoResultException();
    }
    return entity;
  }

  @Override
  @Nonnull
  public final CompleteRepositoryExampleRepository self() {
    return this;
  }
}
