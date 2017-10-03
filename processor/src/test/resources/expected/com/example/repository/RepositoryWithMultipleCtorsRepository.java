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
public class RepositoryWithMultipleCtorsRepository implements RepositoryWithMultipleCtorsBaseRepositoryExtension {
  private final Observable $$arez$$_observable = Arez.context().createObservable( Arez.context().areNamesEnabled() ? "RepositoryWithMultipleCtorsRepository.entities" : null );
  ;

  private final HashMap<Long, RepositoryWithMultipleCtors> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<RepositoryWithMultipleCtors> $$arez$$_entityList = Collections.unmodifiableCollection( $$arez$$_entities.values() );
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
  public RepositoryWithMultipleCtors create(@Nonnull final String packageName, @Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(packageName,name);
    $$arez$$_entities.put( entity.$$arez$$_id(), entity );
    $$arez$$_observable.reportChanged();
    return entity;
  }

  @Action(
      name = "create_name"
  )
  @Nonnull
  public RepositoryWithMultipleCtors create(@Nonnull final String name) {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors(name);
    $$arez$$_entities.put( entity.$$arez$$_id(), entity );
    $$arez$$_observable.reportChanged();
    return entity;
  }

  @Action(
      name = "create_"
  )
  @Nonnull
  public RepositoryWithMultipleCtors create() {
    final Arez_RepositoryWithMultipleCtors entity = new Arez_RepositoryWithMultipleCtors();
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

  public boolean contains(@Nonnull final RepositoryWithMultipleCtors entity) {
    return entity instanceof Arez_RepositoryWithMultipleCtors && $$arez$$_entities.containsKey( ((Arez_RepositoryWithMultipleCtors) entity).$$arez$$_id() );
  }

  public void destroy(@Nonnull final RepositoryWithMultipleCtors entity) {
    assert null != entity;
    if ( entity instanceof Arez_RepositoryWithMultipleCtors && null != $$arez$$_entities.remove( ((Arez_RepositoryWithMultipleCtors) entity).$$arez$$_id() ) ) {
      Disposable.dispose( entity );
      $$arez$$_observable.reportChanged();
    } else {
      Guards.fail( () -> "Called destroy() passing aentity that was not in the repository. Entity: " + entity );
    }
  }

  @Nonnull
  public final Collection<RepositoryWithMultipleCtors> findAll() {
    $$arez$$_observable.reportObserved();
    return $$arez$$_entityList;
  }

  @Nonnull
  public final List<RepositoryWithMultipleCtors> findAll(@Nonnull final Comparator<RepositoryWithMultipleCtors> sorter) {
    return findAll().stream().sorted( sorter ).collect( Collectors.toList() );
  }

  @Nonnull
  public final List<RepositoryWithMultipleCtors> findAllByQuery(@Nonnull final Predicate<RepositoryWithMultipleCtors> query) {
    return findAll().stream().filter( query ).collect( Collectors.toList() );
  }

  @Nonnull
  public final List<RepositoryWithMultipleCtors> findAllByQuery(@Nonnull final Predicate<RepositoryWithMultipleCtors> query, @Nonnull final Comparator<RepositoryWithMultipleCtors> sorter) {
    return findAll().stream().filter( query ).sorted( sorter ).collect( Collectors.toList() );
  }

  @Nullable
  public final RepositoryWithMultipleCtors findByQuery(@Nonnull final Predicate<RepositoryWithMultipleCtors> query) {
    return findAll().stream().filter( query ).findFirst().orElse( null );
  }

  @Override
  @Nonnull
  public final RepositoryWithMultipleCtorsRepository self() {
    return this;
  }
}
