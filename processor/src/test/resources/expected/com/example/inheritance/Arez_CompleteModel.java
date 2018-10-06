package com.example.inheritance;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Flags;
import arez.Locator;
import arez.ObservableValue;
import arez.Observer;
import arez.component.CollectionsUtil;
import arez.component.ComponentState;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import arez.component.Verifiable;
import com.example.inheritance.other.Arez_Element;
import com.example.inheritance.other.BaseCompleteModel;
import com.example.inheritance.other.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
public final class Arez_CompleteModel extends CompleteModel implements Disposable, Identifiable<Byte>, Verifiable, DisposeTrackable {
  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nonnull
  private final ObservableValue<String> $$arez$$_myValue;

  private String $$arezd$$_myValue;

  @Nonnull
  private final ObservableValue<List<Element>> $$arez$$_elements;

  private List<Element> $$arezd$$_elements;

  private List<Element> $$arezd$$_$$cache$$_elements;

  @Nonnull
  private final ObservableValue<Element> $$arez$$_parentGeneralisation;

  private Element $$arezd$$_parentGeneralisation;

  @Nonnull
  private final ComputedValue<Long> $$arez$$_time;

  @Nonnull
  private final Observer $$arez$$_myWatcher;

  @Nonnull
  private final Observer $$arez$$_render;

  @Nullable
  private BaseCompleteModel.MyEntity $$arezr$$_myEntity;

  public Arez_CompleteModel() {
    super();
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> Arez.areReferencesEnabled(), () -> "Attempted to create instance of component of type 'CompleteModel' that contains references but Arez.areReferencesEnabled() returns false. References need to be enabled to use this component" );
    }
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    }
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? getContext().component( "CompleteModel", getId(), Arez.areNamesEnabled() ? getComponentName() : null, () -> $$arezi$$_preDispose() ) : null;
    this.$$arezi$$_disposeNotifier = new DisposeNotifier();
    this.$$arez$$_myValue = getContext().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".myValue" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_myValue : null, Arez.arePropertyIntrospectorsEnabled() ? v -> this.$$arezd$$_myValue = v : null );
    this.$$arez$$_elements = getContext().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".elements" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_elements : null, null );
    this.$$arez$$_parentGeneralisation = getContext().observable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".parentGeneralisation" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezd$$_parentGeneralisation : null, null );
    this.$$arez$$_time = getContext().computed( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".time" : null, () -> super.getTime(), this::onTimeActivate, this::onTimeDeactivate, this::onTimeStale, Flags.RUN_LATER | Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_myWatcher = getContext().observer( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".myWatcher" : null, () -> super.myWatcher(), Flags.RUN_LATER | Flags.NESTED_ACTIONS_DISALLOWED | Flags.AREZ_DEPENDENCIES );
    this.$$arez$$_render = getContext().tracker( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".render" : null, () -> super.onRenderDepsChanged(), Flags.RUN_LATER | Flags.NESTED_ACTIONS_DISALLOWED | Flags.AREZ_DEPENDENCIES );
    this.$$arezd$$_elements = new ArrayList<>();
    this.$$arezd$$_$$cache$$_elements = null;
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    }
    this.$$arezi$$_link_myEntity();
    super.postConstruct();
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_COMPLETE;
    }
    getContext().triggerScheduler();
    if ( Arez.shouldCheckApiInvariants() ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_READY;
    }
  }

  @Override
  protected final ArezContext getContext() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named 'getContext' invoked on uninitialized component of type 'CompleteModel'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  @Nonnull
  protected final Component getComponent() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named 'getComponent' invoked on uninitialized component of type 'CompleteModel'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenConstructed( this.$$arezi$$_state ), () -> "Method named 'getComponent' invoked on un-constructed component named '" + getComponentName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenCompleted( this.$$arezi$$_state ), () -> "Method named 'getComponent' invoked on incomplete component named '" + getComponentName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getComponent' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> Arez.areNativeComponentsEnabled(), () -> "Invoked @ComponentRef method 'getComponent' but Arez.areNativeComponentsEnabled() returned false." );
    }
    return this.$$arezi$$_component;
  }

  final Locator $$arezi$$_locator() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_locator' invoked on uninitialized component of type 'CompleteModel'" );
    }
    return getContext().locator();
  }

  @Override
  @Nonnull
  public final Byte getArezId() {
    return getId();
  }

  protected final String getComponentName() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named 'getComponentName' invoked on uninitialized component of type 'CompleteModel'" );
    }
    return "CompleteModel." + getId();
  }

  private void $$arezi$$_preDispose() {
    Disposable.dispose( _myDisposableField );
    for ( final Element other : new ArrayList<>( $$arezd$$_elements ) ) {
      ( (Arez_Element) other ).$$arezi$$_delink_completeModel();
    }
    if ( null != $$arezd$$_parentGeneralisation ) {
      ( (Arez_Element) $$arezd$$_parentGeneralisation ).$$arezi$$_delink_child();
    }
    $$arezi$$_disposeNotifier.dispose();
  }

  @Override
  @Nonnull
  public DisposeNotifier getNotifier() {
    return $$arezi$$_disposeNotifier;
  }

  @Override
  public boolean isDisposed() {
    return ComponentState.isDisposingOrDisposed( this.$$arezi$$_state );
  }

  @Override
  public void dispose() {
    if ( !ComponentState.isDisposingOrDisposed( this.$$arezi$$_state ) ) {
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSING;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arezi$$_component.dispose();
      } else {
        getContext().safeAction( Arez.areNamesEnabled() ? getComponentName() + ".dispose" : null, () -> { {
          this.$$arezi$$_preDispose();
          this.$$arez$$_myWatcher.dispose();
          this.$$arez$$_render.dispose();
          this.$$arez$$_time.dispose();
          this.$$arez$$_myValue.dispose();
          this.$$arez$$_elements.dispose();
          this.$$arez$$_parentGeneralisation.dispose();
        } }, Flags.NO_VERIFY_ACTION_REQUIRED );
      }
      if ( Arez.shouldCheckApiInvariants() ) {
        this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
      }
    }
  }

  @Override
  public void verify() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'verify' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() && Arez.isVerifyEnabled() ) {
      Guards.apiInvariant( () -> this == $$arezi$$_locator().findById( CompleteModel.class, getId() ), () -> "Attempted to lookup self in Locator with type CompleteModel and id '" + getId() + "' but unable to locate self. Actual value: " + $$arezi$$_locator().findById( CompleteModel.class, getId() ) );
      final int $$arezv$$_myEntityId = this.getMyEntityId();
      final BaseCompleteModel.MyEntity $$arezv$$_myEntity = this.$$arezi$$_locator().findById( BaseCompleteModel.MyEntity.class, $$arezv$$_myEntityId );
      Guards.apiInvariant( () -> null != $$arezv$$_myEntity, () -> "Reference named 'myEntity' on component named '" + getComponentName() + "' is unable to resolve entity of type com.example.inheritance.other.BaseCompleteModel.MyEntity and id = " + getMyEntityId() );
      for( final Element element : this.$$arezd$$_elements ) {
        if ( Arez.shouldCheckApiInvariants() ) {
          Guards.apiInvariant( () -> Disposable.isNotDisposed( element ), () -> "Inverse relationship named 'elements' on component named '" + getComponentName() + "' contains disposed element '" + element + "'" );
        }
      }
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> Disposable.isNotDisposed( this.$$arezd$$_parentGeneralisation ), () -> "Inverse relationship named 'parentGeneralisation' on component named '" + getComponentName() + "' contains disposed element '" + this.$$arezd$$_parentGeneralisation + "'" );
      }
    }
  }

  @Override
  protected String getMyValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getMyValue' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_myValue.reportObserved();
    return this.$$arezd$$_myValue;
  }

  @Override
  public void setMyValue(final String value) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setMyValue' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_myValue.preReportChanged();
    final String $$arezv$$_currentValue = this.$$arezd$$_myValue;
    if ( !Objects.equals( value, $$arezv$$_currentValue ) ) {
      this.$$arezd$$_myValue = value;
      this.$$arez$$_myValue.reportChanged();
    }
  }

  @Nonnull
  @Override
  protected ObservableValue<String> getMyValueObservableValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getMyValueObservableValue' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    return $$arez$$_myValue;
  }

  @Override
  protected List<Element> getElements() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getElements' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_elements.reportObserved();
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      final List<Element> $$ar$$_result = this.$$arezd$$_elements;
      if ( null == this.$$arezd$$_$$cache$$_elements && null != $$ar$$_result ) {
        this.$$arezd$$_$$cache$$_elements = CollectionsUtil.wrap( $$ar$$_result );
      }
      return $$arezd$$_$$cache$$_elements;
    } else {
      return this.$$arezd$$_elements;
    }
  }

  @Nullable
  @Override
  Element getParentGeneralisation() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getParentGeneralisation' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.reportObserved();
    return this.$$arezd$$_parentGeneralisation;
  }

  @Override
  protected void myWatcher() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.fail( () -> "Observe method named 'myWatcher' invoked but @Observe(executor=AREZ) annotated methods should only be invoked by the runtime." );
    }
    super.myWatcher();
  }

  @Override
  protected Observer getMyWatcherObserver() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getMyWatcherObserver' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    return $$arez$$_myWatcher;
  }

  @Override
  public void render(final long time, final float someOtherParameter) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'render' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    try {
      getContext().safeObserve( this.$$arez$$_render, () -> super.render( time, someOtherParameter ), Arez.areSpiesEnabled() ? new Object[] { time, someOtherParameter } : null );
    } catch( final RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
    }
  }

  @Override
  public void myAction() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'myAction' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    try {
      getContext().safeAction(Arez.areNamesEnabled() ? getComponentName() + ".myAction" : null, () -> super.myAction(), Flags.READ_WRITE | Flags.ENVIRONMENT_NOT_REQUIRED | Flags.VERIFY_ACTION_REQUIRED, null );
    } catch( final RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
    }
  }

  @Override
  protected long getTime() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getTime' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    return this.$$arez$$_time.get();
  }

  @Nonnull
  @Override
  protected ComputedValue<Long> getTimeComputedValue() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getTimeComputedValue' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    return $$arez$$_time;
  }

  @Override
  protected BaseCompleteModel.MyEntity getMyEntity() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getMyEntity' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_myEntity, () -> "Nonnull reference method named 'getMyEntity' invoked on component named '" + getComponentName() + "' but reference has not been resolved yet is not lazy. Id = " + getMyEntityId() );
    }
    return this.$$arezr$$_myEntity;
  }

  private void $$arezi$$_link_myEntity() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_link_myEntity' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    final int id = this.getMyEntityId();
    this.$$arezr$$_myEntity = this.$$arezi$$_locator().findById( BaseCompleteModel.MyEntity.class, id );
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> null != $$arezr$$_myEntity, () -> "Reference named 'myEntity' on component named '" + getComponentName() + "' is unable to resolve entity of type com.example.inheritance.other.BaseCompleteModel.MyEntity and id = " + getMyEntityId() );
    }
  }

  public void $$arezi$$_delink_myEntity() {
    this.$$arezr$$_myEntity = null;
  }

  public void $$arezir$$_elements_add(@Nonnull final Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_elements_add' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_elements.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> !this.$$arezd$$_elements.contains( element ), () -> "Attempted to add reference 'element' to inverse 'elements' but inverse already contained element. Inverse = " + $$arez$$_elements );
    }
    this.$$arezd$$_elements.add( element );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_elements = null;
    }
    this.$$arez$$_elements.reportChanged();
  }

  public void $$arezir$$_elements_remove(@Nonnull final Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_elements_remove' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_elements.preReportChanged();
    if ( Arez.shouldCheckInvariants() ) {
      Guards.invariant( () -> this.$$arezd$$_elements.contains( element ), () -> "Attempted to remove reference 'element' from inverse 'elements' but inverse does not contain element. Inverse = " + $$arez$$_elements );
    }
    this.$$arezd$$_elements.remove( element );
    if ( Arez.areCollectionsPropertiesUnmodifiable() ) {
      this.$$arezd$$_$$cache$$_elements = null;
    }
    this.$$arez$$_elements.reportChanged();
  }

  public void $$arezir$$_parentGeneralisation_zset(@Nullable final Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_parentGeneralisation_zset' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    this.$$arezd$$_parentGeneralisation = element;
    this.$$arez$$_parentGeneralisation.reportChanged();
  }

  public void $$arezir$$_parentGeneralisation_zunset(@Nonnull final Element element) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named '$$arezir$$_parentGeneralisation_zunset' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    this.$$arez$$_parentGeneralisation.preReportChanged();
    if ( this.$$arezd$$_parentGeneralisation == element ) {
      this.$$arezd$$_parentGeneralisation = null;
      this.$$arez$$_parentGeneralisation.reportChanged();
    }
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Byte.hashCode( getId() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( this == o ) {
        return true;
      } else if ( null == o || !(o instanceof Arez_CompleteModel) ) {
        return false;
      } else if ( Disposable.isDisposed( this ) != Disposable.isDisposed( o ) ) {
        return false;
      } else {
        final Arez_CompleteModel that = (Arez_CompleteModel) o;
        return getId() == that.getId();
      }
    } else {
      return super.equals( o );
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + getComponentName() + "]";
    } else {
      return super.toString();
    }
  }
}
