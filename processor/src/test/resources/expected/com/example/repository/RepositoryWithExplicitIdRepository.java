package com.example.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
public class RepositoryWithExplicitIdRepository implements RepositoryWithExplicitIdRepositoryExtension {
  private final Observable $$arez$$_observable = Arez.context().createObservable( Arez.context().areNamesEnabled() ? "RepositoryWithExplicitIdRepository.entities" : null );
  ;

  private final HashMap<Integer, RepositoryWithExplicitId> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<RepositoryWithExplicitId> $$arez$$_entityList = Collections.unmodifiableCollection( $$arez$$_entities.values() );
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
  public RepositoryWithExplicitId create(@Nonnull final String packageName, @Nonnull final String name) {
    final Arez_RepositoryWithExplicitId entity = new Arez_RepositoryWithExplicitId(packageName,name);
    $$arez$$_entities.put( entity.getId(), entity );
    $$arez$$_observable.reportChanged();
    return entity;
  }

  @PreDispose
  final void preDispose() {
    $$arez$$_entityList.forEach( e -> Disposable.dispose( e ) );
    $$arez$$_entities.clear();
    $$arez$$_observable.reportChanged();
  }

  public boolean contains(@Nonnull final RepositoryWithExplicitId entity) {
    return $$arez$$_entities.containsKey( entity.getId() );
  }

  public void destroy(@Nonnull final RepositoryWithExplicitId entity) {
    assert null != entity;
    if ( null != $$arez$$_entities.remove( entity.getId() ) ) {
      Disposable.dispose( entity );
      $$arez$$_observable.reportChanged();
    } else {
      Guards.fail( () -> "Called destroy() passing aentity that was not in the repository. Entity: " + entity );
    }
  }

  @Nullable
  public RepositoryWithExplicitId findById(final int id) {
    return $$arez$$_entities.get( id );
  }

  @Nonnull
  public final Collection<RepositoryWithExplicitId> findAll() {
    $$arez$$_observable.reportObserved();
    return $$arez$$_entityList;
  }

  @Nonnull
  public final List<RepositoryWithExplicitId> findAll(@Nonnull final Comparator<RepositoryWithExplicitId> sorter) {
    return findAll().stream().sorted( sorter ).collect( Collectors.toList() );
  }

  @Nonnull
  public final List<RepositoryWithExplicitId> findAllByQuery(@Nonnull final Predicate<RepositoryWithExplicitId> query) {
    return findAll().stream().filter( query ).collect( Collectors.toList() );
  }

  @Nonnull
  public final List<RepositoryWithExplicitId> findAllByQuery(@Nonnull final Predicate<RepositoryWithExplicitId> query, @Nonnull final Comparator<RepositoryWithExplicitId> sorter) {
    return findAll().stream().filter( query ).sorted( sorter ).collect( Collectors.toList() );
  }

  @Nullable
  public final RepositoryWithExplicitId findByQuery(@Nonnull final Predicate<RepositoryWithExplicitId> query) {
    return findAll().stream().filter( query ).findFirst().orElse( null );
  }

  @Override
  @Nonnull
  public final RepositoryWithExplicitIdRepository self() {
    return this;
  }
}
