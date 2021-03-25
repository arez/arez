package com.example.deprecated;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.Observer;
import arez.SafeProcedure;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.internal.CollectionsUtil;
import arez.component.internal.ComponentKernel;
import arez.component.internal.MemoizeCache;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
final class Arez_DeprecatedUsageModel extends DeprecatedUsageModel implements Disposable, Identifiable<Integer>, DisposeNotifier {
  private static volatile int $$arezi$$_nextId;

  private final ComponentKernel $$arezi$$_kernel;

  @Nonnull
  @SuppressWarnings("deprecation")
  private final ObservableValue<MyDeprecatedEntity> $$arez$$_myEntity;

  @SuppressWarnings("deprecation")
  private MyDeprecatedEntity $$arezd$$_myEntity;

  @Nonnull
  @SuppressWarnings("deprecation")
  private final ObservableValue<MyDeprecatedEntity> $$arez$$_myEntity2;

  @Nonnull
  @SuppressWarnings("deprecation")
  private final ObservableValue<List<MyDeprecatedEntity>> $$arez$$_myEntityList;

  @SuppressWarnings("deprecation")
  private List<MyDeprecatedEntity> $$arezd$$_myEntityList;

  @SuppressWarnings("deprecation")
  private List<MyDeprecatedEntity> $$arezd$$_$$cache$$_myEntityList;

  @Nonnull
  @SuppressWarnings("deprecation")
  private final ComputableValue<MyDeprecatedEntity> $$arez$$_genEntity;

  @Nonnull
  private final MemoizeCache<Integer> $$arez$$_genEntityStat1;

  @Nonnull
  private final MemoizeCache<Integer> $$arez$$_genEntityStat2;

  @Nonnull
  private final Observer $$arez$$_render;

  @SuppressWarnings("deprecation")
  Arez_DeprecatedUsageModel(final MyDeprecatedEntity myEntity,
      final List<MyDeprecatedEntity> myEntityList) {
    super();
    final ArezContext $$arezv$$_context = Arez.context();
    final int $$arezv$$_id = ++$$arezi$$_nextId;
    final String $$arezv$$_name = Arez.areNamesEnabled() ? "com_example_deprecated_DeprecatedUsageModel." + $$arezv$$_id : null;
    final Component $$arezv$$_component = Arez.areNativeComponentsEnabled() ? $$arezv$$_context.component( "com_example_deprecated_DeprecatedUsageModel", $$arezv$$_id, $$arezv$$_name, this::$$arezi$$_nativeComponentPreDispose ) : null;
    this.$$arezi$$_kernel = new ComponentKernel( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNamesEnabled() ? $$arezv$$_name : null, $$arezv$$_id, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, null, Arez.areNativeComponentsEnabled() ? null : this::$$arezi$$_dispose, null, true, false, false );
    this.$$arezd$$_myEntity = myEntity;
    this.$$arezd$$_myEntityList = myEntityList;
    this.$$arez$$_myEntity = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myEntity" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_myEntity : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_myEntity = v : null );
    this.$$arez$$_myEntity2 = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myEntity2" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getMyEntity2() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setMyEntity2( v ) : null );
    this.$$arez$$_myEntityList = $$arezv$$_context.observable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".myEntityList" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_myEntityList : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_myEntityList = v : null );
    this.$$arez$$_genEntity = $$arezv$$_context.computable( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".genEntity" : null, () -> super.genEntity(), ComputableValue.Flags.AREZ_DEPENDENCIES | ComputableValue.Flags.RUN_LATER );
    this.$$arez$$_genEntityStat1 = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".genEntityStat1" : null, args -> super.genEntityStat1((MyDeprecatedEntity) args[ 0 ]), 1, ComputableValue.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_genEntityStat2 = new MemoizeCache<>( Arez.areZonesEnabled() ? $$arezv$$_context : null, Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".genEntityStat2" : null, args -> super.genEntityStat2((List<Consumer<MyDeprecatedEntity>>) args[ 0 ]), 1, ComputableValue.Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render = $$arezv$$_context.tracker( Arez.areNativeComponentsEnabled() ? $$arezv$$_component : null, Arez.areNamesEnabled() ? $$arezv$$_name + ".render" : null, () -> super.onRenderDepsChange(), Observer.Flags.RUN_LATER | Observer.Flags.NESTED_ACTIONS_DISALLOWED | Observer.Flags.AREZ_DEPENDENCIES );
    this.$$arezi$$_kernel.componentConstructed();
    this.$$arezi$$_kernel.componentReady();
  }

  private int $$arezi$$_id() {
    return this.$$arezi$$_kernel.getId();
  }

  @Override
  @Nonnull
  public Integer getArezId() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'getArezId' invoked on uninitialized component of type 'com_example_deprecated_DeprecatedUsageModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'getArezId' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return $$arezi$$_id();
  }

  private void $$arezi$$_nativeComponentPreDispose() {
    this.$$arezi$$_kernel.notifyOnDisposeListeners();
  }

  @Override
  public void addOnDisposeListener(@Nonnull final Object key, @Nonnull final SafeProcedure action) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'addOnDisposeListener' invoked on uninitialized component of type 'com_example_deprecated_DeprecatedUsageModel'" );
    }
    this.$$arezi$$_kernel.addOnDisposeListener( key, action );
  }

  @Override
  public void removeOnDisposeListener(@Nonnull final Object key) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'removeOnDisposeListener' invoked on uninitialized component of type 'com_example_deprecated_DeprecatedUsageModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'removeOnDisposeListener' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.removeOnDisposeListener( key );
  }

  @Override
  public boolean isDisposed() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'isDisposed' invoked on uninitialized component of type 'com_example_deprecated_DeprecatedUsageModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'isDisposed' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    return this.$$arezi$$_kernel.isDisposed();
  }

  @Override
  public void dispose() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenInitialized(), () -> "Method named 'dispose' invoked on uninitialized component of type 'com_example_deprecated_DeprecatedUsageModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.hasBeenConstructed(), () -> "Method named 'dispose' invoked on un-constructed component named '" + ( null == this.$$arezi$$_kernel ? "?" : this.$$arezi$$_kernel.getName() ) + "'" );
    }
    this.$$arezi$$_kernel.dispose();
  }

  private void $$arezi$$_dispose() {
    this.$$arez$$_render.dispose();
    this.$$arez$$_genEntity.dispose();
    this.$$arez$$_genEntityStat1.dispose();
    this.$$arez$$_genEntityStat2.dispose();
    this.$$arez$$_myEntity.dispose();
    this.$$arez$$_myEntity2.dispose();
    this.$$arez$$_myEntityList.dispose();
  }

  @Override
  @SuppressWarnings("deprecation")
  MyDeprecatedEntity getMyEntity() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyEntity' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myEntity.reportObserved();
    return this.$$arezd$$_myEntity;
  }

  @Override
  @SuppressWarnings("deprecation")
  void setMyEntity(final MyDeprecatedEntity entity) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyEntity' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myEntity.preReportChanged();
    final MyDeprecatedEntity $$arezv$$_currentValue = this.$$arezd$$_myEntity;
    if ( !Objects.equals( entity, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_myEntity = entity;
      this.$$arez$$_myEntity.reportChanged();
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  MyDeprecatedEntity getMyEntity2() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyEntity2' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myEntity2.reportObserved();
    return super.getMyEntity2();
  }

  @Override
  @SuppressWarnings("deprecation")
  void setMyEntity2(final MyDeprecatedEntity entity) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyEntity2' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myEntity2.preReportChanged();
    final MyDeprecatedEntity $$arezv$$_currentValue = super.getMyEntity2();
    if ( !Objects.equals( entity, $$arezv$$_currentValue ) ) {
      super.setMyEntity2( entity );
      this.$$arez$$_myEntity2.reportChanged();
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  List<MyDeprecatedEntity> getMyEntityList() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'getMyEntityList' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myEntityList.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<MyDeprecatedEntity> $$ar$$_result = this.$$arezd$$_myEntityList;
      if ( null == this.$$arezd$$_$$cache$$_myEntityList && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_myEntityList = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_myEntityList;
    } else {
      return this.$$arezd$$_myEntityList;
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  void setMyEntityList(final List<MyDeprecatedEntity> entity) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'setMyEntityList' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arez$$_myEntityList.preReportChanged();
    final List<MyDeprecatedEntity> $$arezv$$_currentValue = this.$$arezd$$_myEntityList;
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_myEntityList = null;
    }
    if ( !Objects.equals( entity, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_myEntityList = entity;
      this.$$arez$$_myEntityList.reportChanged();
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public void render(@Nonnull final MyDeprecatedEntity entity) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'render' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeObserve( this.$$arez$$_render, () -> super.render( entity ), Arez.areSpiesEnabled() ? new Object[] { entity } : null );
  }

  @Override
  @SuppressWarnings("deprecation")
  public void doStuff(@Nonnull final MyDeprecatedEntity entity) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'doStuff' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    this.$$arezi$$_kernel.getContext().safeAction( Arez.areNamesEnabled() ? this.$$arezi$$_kernel.getName() + ".doStuff" : null, () -> super.doStuff( entity ), ActionFlags.READ_WRITE | ActionFlags.VERIFY_ACTION_REQUIRED, Arez.areSpiesEnabled() ? new Object[] { entity } : null );
  }

  @Override
  @SuppressWarnings("deprecation")
  MyDeprecatedEntity genEntity() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'genEntity' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_genEntity.get();
  }

  @Override
  @SuppressWarnings("deprecation")
  int genEntityStat1(final MyDeprecatedEntity entity) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'genEntityStat1' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_genEntityStat1.get( entity );
  }

  @Override
  @SuppressWarnings("deprecation")
  int genEntityStat2(final List<Consumer<MyDeprecatedEntity>> other) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != this.$$arezi$$_kernel && this.$$arezi$$_kernel.isActive(), () -> "Method named 'genEntityStat2' invoked on " + this.$$arezi$$_kernel.describeState() + " component named '" + this.$$arezi$$_kernel.getName() + "'" );
    }
    return this.$$arez$$_genEntityStat2.get( other );
  }

  @Override
  public String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + this.$$arezi$$_kernel.getName() + "]";
    } else {
      return super.toString();
    }
  }
}
