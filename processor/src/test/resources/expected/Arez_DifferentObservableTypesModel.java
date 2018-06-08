import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.ComponentObservable;
import arez.component.ComponentState;
import arez.component.Identifiable;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_DifferentObservableTypesModel extends DifferentObservableTypesModel implements Disposable, Identifiable<Integer>, ComponentObservable {
  private static volatile int $$arezi$$_nextId;

  private final int $$arezi$$_id;

  private byte $$arezi$$_state;

  @Nullable
  private final ArezContext $$arezi$$_context;

  private final Component $$arezi$$_component;

  private final Observable<Boolean> $$arezi$$_disposedObservable;

  @Nonnull
  private final Observable<Boolean> $$arez$$_v1;

  @Nonnull
  private final Observable<Byte> $$arez$$_v2;

  @Nonnull
  private final Observable<Character> $$arez$$_v3;

  @Nonnull
  private final Observable<Short> $$arez$$_v4;

  @Nonnull
  private final Observable<Integer> $$arez$$_v5;

  @Nonnull
  private final Observable<Long> $$arez$$_v6;

  @Nonnull
  private final Observable<Float> $$arez$$_v7;

  @Nonnull
  private final Observable<Double> $$arez$$_v8;

  @Nonnull
  private final Observable<Object> $$arez$$_v9;

  public Arez_DifferentObservableTypesModel() {
    super();
    this.$$arezi$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arezi$$_id = ( Arez.areNamesEnabled() || Arez.areNativeComponentsEnabled() ) ? $$arezi$$_nextId++ : 0;
    this.$$arezi$$_state = ComponentState.COMPONENT_INITIALIZED;
    this.$$arezi$$_component = Arez.areNativeComponentsEnabled() ? $$arezi$$_context().createComponent( "DifferentObservableTypesModel", $$arezi$$_id(), Arez.areNamesEnabled() ? $$arezi$$_name() : null ) : null;
    this.$$arezi$$_disposedObservable = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arezi$$_state >= 0 : null );
    this.$$arez$$_v1 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v1" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.isV1() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV1( v ) : null );
    this.$$arez$$_v2 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v2" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV2() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV2( v ) : null );
    this.$$arez$$_v3 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v3" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV3() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV3( v ) : null );
    this.$$arez$$_v4 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v4" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV4() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV4( v ) : null );
    this.$$arez$$_v5 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v5" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV5() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV5( v ) : null );
    this.$$arez$$_v6 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v6" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV6() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV6( v ) : null );
    this.$$arez$$_v7 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v7" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV7() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV7( v ) : null );
    this.$$arez$$_v8 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v8" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV8() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV8( v ) : null );
    this.$$arez$$_v9 = $$arezi$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arezi$$_component : null, Arez.areNamesEnabled() ? $$arezi$$_name() + ".v9" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV9() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV9( v ) : null );
    this.$$arezi$$_state = ComponentState.COMPONENT_CONSTRUCTED;
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arezi$$_component.complete();
    }
    this.$$arezi$$_state = ComponentState.COMPONENT_READY;
  }

  final ArezContext $$arezi$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_context' invoked on uninitialized component of type 'DifferentObservableTypesModel'" );
    }
    return Arez.areZonesEnabled() ? this.$$arezi$$_context : Arez.context();
  }

  final int $$arezi$$_id() {
    if ( Arez.shouldCheckInvariants() && !Arez.areNamesEnabled() && !Arez.areNativeComponentsEnabled() ) {
      Guards.fail( () -> "Method invoked to access id when id not expected." );
    }
    return this.$$arezi$$_id;
  }

  @Override
  @Nonnull
  public final Integer getArezId() {
    return $$arezi$$_id();
  }

  String $$arezi$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.hasBeenInitialized( this.$$arezi$$_state ), () -> "Method named '$$arezi$$_name' invoked on uninitialized component of type 'DifferentObservableTypesModel'" );
    }
    return "DifferentObservableTypesModel." + $$arezi$$_id();
  }

  private boolean $$arezi$$_observe() {
    final boolean isDisposed = isDisposed();
    if ( !isDisposed )  {
      this.$$arezi$$_disposedObservable.reportObserved();
    }
    return !isDisposed;
  }

  @Override
  public boolean observe() {
    return $$arezi$$_observe();
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
        $$arezi$$_context().dispose( Arez.areNamesEnabled() ? $$arezi$$_name() : null, () -> { {
          this.$$arezi$$_disposedObservable.dispose();
          this.$$arez$$_v1.dispose();
          this.$$arez$$_v2.dispose();
          this.$$arez$$_v3.dispose();
          this.$$arez$$_v4.dispose();
          this.$$arez$$_v5.dispose();
          this.$$arez$$_v6.dispose();
          this.$$arez$$_v7.dispose();
          this.$$arez$$_v8.dispose();
          this.$$arez$$_v9.dispose();
        } } );
      }
      this.$$arezi$$_state = ComponentState.COMPONENT_DISPOSED;
    }
  }

  @Override
  public boolean isV1() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'isV1' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v1.reportObserved();
    return super.isV1();
  }

  @Override
  public void setV1(final boolean v1) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV1' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( v1 != super.isV1() ) {
      this.$$arez$$_v1.preReportChanged();
      super.setV1(v1);
      this.$$arez$$_v1.reportChanged();
    }
  }

  @Override
  public byte getV2() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getV2' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v2.reportObserved();
    return super.getV2();
  }

  @Override
  public void setV2(final byte v2) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV2' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( v2 != super.getV2() ) {
      this.$$arez$$_v2.preReportChanged();
      super.setV2(v2);
      this.$$arez$$_v2.reportChanged();
    }
  }

  @Override
  public char getV3() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getV3' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v3.reportObserved();
    return super.getV3();
  }

  @Override
  public void setV3(final char v3) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV3' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( v3 != super.getV3() ) {
      this.$$arez$$_v3.preReportChanged();
      super.setV3(v3);
      this.$$arez$$_v3.reportChanged();
    }
  }

  @Override
  public short getV4() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getV4' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v4.reportObserved();
    return super.getV4();
  }

  @Override
  public void setV4(final short v4) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV4' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( v4 != super.getV4() ) {
      this.$$arez$$_v4.preReportChanged();
      super.setV4(v4);
      this.$$arez$$_v4.reportChanged();
    }
  }

  @Override
  public int getV5() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getV5' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v5.reportObserved();
    return super.getV5();
  }

  @Override
  public void setV5(final int v5) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV5' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( v5 != super.getV5() ) {
      this.$$arez$$_v5.preReportChanged();
      super.setV5(v5);
      this.$$arez$$_v5.reportChanged();
    }
  }

  @Override
  public long getV6() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getV6' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v6.reportObserved();
    return super.getV6();
  }

  @Override
  public void setV6(final long v6) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV6' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( v6 != super.getV6() ) {
      this.$$arez$$_v6.preReportChanged();
      super.setV6(v6);
      this.$$arez$$_v6.reportChanged();
    }
  }

  @Override
  public float getV7() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getV7' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v7.reportObserved();
    return super.getV7();
  }

  @Override
  public void setV7(final float v7) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV7' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( v7 != super.getV7() ) {
      this.$$arez$$_v7.preReportChanged();
      super.setV7(v7);
      this.$$arez$$_v7.reportChanged();
    }
  }

  @Override
  public double getV8() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getV8' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v8.reportObserved();
    return super.getV8();
  }

  @Override
  public void setV8(final double v8) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV8' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( v8 != super.getV8() ) {
      this.$$arez$$_v8.preReportChanged();
      super.setV8(v8);
      this.$$arez$$_v8.reportChanged();
    }
  }

  @Override
  public Object getV9() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'getV9' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    this.$$arez$$_v9.reportObserved();
    return super.getV9();
  }

  @Override
  public void setV9(final Object v9) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> ComponentState.isActive( this.$$arezi$$_state ), () -> "Method named 'setV9' invoked on " + ComponentState.describe( this.$$arezi$$_state ) + " component named '" + $$arezi$$_name() + "'" );
    }
    if ( !Objects.equals( v9, super.getV9() ) ) {
      this.$$arez$$_v9.preReportChanged();
      super.setV9(v9);
      this.$$arez$$_v9.reportChanged();
    }
  }

  @Override
  public final int hashCode() {
    if ( Arez.areNativeComponentsEnabled() ) {
      return Double.hashCode( $$arezi$$_id() );
    } else {
      return super.hashCode();
    }
  }

  @Override
  public final boolean equals(final Object o) {
    if ( Arez.areNativeComponentsEnabled() ) {
      if ( this == o ) {
        return true;
      } else if ( null == o || !(o instanceof Arez_DifferentObservableTypesModel) ) {
        return false;
      } else {
        final Arez_DifferentObservableTypesModel that = (Arez_DifferentObservableTypesModel) o;;
        return $$arezi$$_id() == that.$$arezi$$_id();
      }
    } else {
      return super.equals( o );
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arezi$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
