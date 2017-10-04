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
public class NestedModel$BasicActionModelRepository implements NestedModel$BasicActionModelBaseRepositoryExtension {
  private final Observable $$arez$$_observable = Arez.context().createObservable( Arez.context().areNamesEnabled() ? "BasicActionModelRepository.entities" : null );
  ;

  private final HashMap<Long, NestedModel.BasicActionModel> $$arez$$_entities = new HashMap<>();
  ;

  private final Collection<NestedModel.BasicActionModel> $$arez$$_entityList = Collections.unmodifiableCollection( $$arez$$_entities.values() );
  ;

  NestedModel$BasicActionModelRepository() {
  }

  @Nonnull
  public static NestedModel$BasicActionModelRepository newRepository() {
    return new Arez_NestedModel$BasicActionModelRepository();
  }

  @Action(
      name = "create_"
  )
  @Nonnull
  public NestedModel.BasicActionModel create() {
    final NestedModel$Arez_BasicActionModel entity = new NestedModel$Arez_BasicActionModel();
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

  public boolean contains(@Nonnull final NestedModel.BasicActionModel entity) {
    return entity instanceof NestedModel$Arez_BasicActionModel && $$arez$$_entities.containsKey( ((NestedModel$Arez_BasicActionModel) entity).$$arez$$_id() );
  }

  public void destroy(@Nonnull final NestedModel.BasicActionModel entity) {
    assert null != entity;
    if ( entity instanceof NestedModel$Arez_BasicActionModel && null != $$arez$$_entities.remove( ((NestedModel$Arez_BasicActionModel) entity).$$arez$$_id() ) ) {
      Disposable.dispose( entity );
      $$arez$$_observable.reportChanged();
    } else {
      Guards.fail( () -> "Called destroy() passing an entity that was not in the repository. Entity: " + entity );
    }
  }

  @Nonnull
  public final Collection<NestedModel.BasicActionModel> findAll() {
    $$arez$$_observable.reportObserved();
    return $$arez$$_entityList;
  }

  @Nonnull
  public final List<NestedModel.BasicActionModel> findAll(@Nonnull final Comparator<NestedModel.BasicActionModel> sorter) {
    return findAll().stream().sorted( sorter ).collect( Collectors.toList() );
  }

  @Nonnull
  public final List<NestedModel.BasicActionModel> findAllByQuery(@Nonnull final Predicate<NestedModel.BasicActionModel> query) {
    return findAll().stream().filter( query ).collect( Collectors.toList() );
  }

  @Nonnull
  public final List<NestedModel.BasicActionModel> findAllByQuery(@Nonnull final Predicate<NestedModel.BasicActionModel> query, @Nonnull final Comparator<NestedModel.BasicActionModel> sorter) {
    return findAll().stream().filter( query ).sorted( sorter ).collect( Collectors.toList() );
  }

  @Nullable
  public final NestedModel.BasicActionModel findByQuery(@Nonnull final Predicate<NestedModel.BasicActionModel> query) {
    return findAll().stream().filter( query ).findFirst().orElse( null );
  }

  @Override
  @Nonnull
  public final NestedModel$BasicActionModelRepository self() {
    return this;
  }
}
