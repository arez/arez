package com.example.inheritance;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Locator;
import arez.Observable;
import arez.Observer;
import arez.component.ComponentState;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import arez.component.Identifiable;
import com.example.inheritance.other.BaseCompleteModel;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
@SuppressWarnings("unchecked")
final class Arez_CompleteModel extends CompleteModel implements Disposable, Identifiable<Byte>, DisposeTrackable {
  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final DisposeNotifier $$arezi$$_disposeNotifier;

  @Nonnull
  private final Observable<String> $$arez$$_myValue;

  private String $$arezd$$_myValue;

  @Nonnull
  private final ComputedValue<Long> $$arez$$_time;

  @Nonnull
  private final Observer $$arez$$_myAutorun;

  @Nonnull
  private final Observer $$arez$$_render;

  @Nullable
  private BaseCompleteModel.MyEntity $$arezr$$_myEntity;

  Arez_CompleteModel() {
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
    this.$$arez$$_time = getContext().computedValue( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".time" : null, () -> super.getTime(), this::onTimeActivate, this::onTimeDeactivate, this::onTimeStale, this::onTimeDispose );
    this.$$arez$$_myAutorun = getContext().autorun( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".myAutorun" : null, false, () -> super.myAutorun() );
    this.$$arez$$_render = getContext().tracker( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? getComponentName() + ".render" : null, false, () -> super.onRenderDepsChanged() );
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
        getContext().safeAction( Arez.areNamesEnabled() ? getComponentName() + ".dispose" : null, true, false, () -> { {
          this.$$arezi$$_preDispose();
          this.$$arez$$_myAutorun.dispose();
          this.$$arez$$_render.dispose();
          this.$$arez$$_time.dispose();
          this.$$arez$$_myValue.dispose();
        } } );
      }
      if ( Arez.shouldCheckApiInvariants() ) {
        this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
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
  protected Observable<String> getMyValueObservable() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getMyValueObservable' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    return $$arez$$_myValue;
  }

  @Override
  protected void myAutorun() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.fail( () -> "Autorun method named 'myAutorun' invoked but @Autorun annotated methods should only be invoked by the runtime." );
    }
    super.myAutorun();
  }

  @Override
  protected Observer getMyAutorunObserver() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getMyAutorunObserver' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    return $$arez$$_myAutorun;
  }

  @Override
  public void myAction() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'myAction' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    try {
      getContext().safeAction(Arez.areNamesEnabled() ? getComponentName() + ".myAction" : null, true, false, false, () -> super.myAction() );
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
  public void render(final long time, final float someOtherParameter) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'render' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + getComponentName() + "'" );
    }
    try {
      getContext().safeTrack( this.$$arez$$_render, () -> super.render(time,someOtherParameter), time, someOtherParameter );
    } catch( final RuntimeException | Error $$arez_exception$$ ) {
      throw $$arez_exception$$;
    } catch( final Throwable $$arez_exception$$ ) {
      throw new IllegalStateException( $$arez_exception$$ );
    }
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
    if ( null == this.$$arezr$$_myEntity ) {
      final int id = this.getMyEntityId();
      this.$$arezr$$_myEntity = this.$$arezi$$_locator().findById( BaseCompleteModel.MyEntity.class, id );
      if ( Arez.shouldCheckApiInvariants() ) {
        Guards.apiInvariant( () -> null != $$arezr$$_myEntity, () -> "Reference method named 'getMyEntity' invoked on component named '" + getComponentName() + "' missing related entity. Id = " + getMyEntityId() );
      }
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
        final Arez_CompleteModel that = (Arez_CompleteModel) o;;
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
