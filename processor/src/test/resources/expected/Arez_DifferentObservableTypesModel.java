import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import arez.component.Identifiable;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.Guards;

@Generated("arez.processor.ArezProcessor")
public final class Arez_DifferentObservableTypesModel extends DifferentObservableTypesModel implements Disposable, Identifiable<Long> {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private byte $$arez$$_state;

  @Nullable
  private final ArezContext $$arez$$_context;

  private final Component $$arez$$_component;

  private final Observable<Boolean> $$arez$$_disposedObservable;

  @Nonnull
  private final Observable<Long> $$arez$$_v6;

  @Nonnull
  private final Observable<Float> $$arez$$_v7;

  @Nonnull
  private final Observable<Double> $$arez$$_v8;

  @Nonnull
  private final Observable<Object> $$arez$$_v9;

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

  public Arez_DifferentObservableTypesModel() {
    super();
    this.$$arez$$_context = Arez.areZonesEnabled() ? Arez.context() : null;
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_state = 1;
    this.$$arez$$_component = Arez.areNativeComponentsEnabled() ? $$arez$$_context().createComponent( "DifferentObservableTypesModel", $$arez$$_id(), $$arez$$_name(), null, null ) : null;
    this.$$arez$$_disposedObservable = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".isDisposed" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> this.$$arez$$_state >= 0 : null, null );
    this.$$arez$$_v6 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v6" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV6() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV6( v ) : null );
    this.$$arez$$_v7 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v7" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV7() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV7( v ) : null );
    this.$$arez$$_v8 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v8" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV8() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV8( v ) : null );
    this.$$arez$$_v9 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v9" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV9() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV9( v ) : null );
    this.$$arez$$_v1 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v1" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.isV1() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV1( v ) : null );
    this.$$arez$$_v2 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v2" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV2() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV2( v ) : null );
    this.$$arez$$_v3 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v3" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV3() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV3( v ) : null );
    this.$$arez$$_v4 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v4" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV4() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV4( v ) : null );
    this.$$arez$$_v5 = $$arez$$_context().createObservable( Arez.areNativeComponentsEnabled() ? this.$$arez$$_component : null, Arez.areNamesEnabled() ? $$arez$$_name() + ".v5" : null, Arez.arePropertyIntrospectorsEnabled() ? () -> super.getV5() : null, Arez.arePropertyIntrospectorsEnabled() ? v -> super.setV5( v ) : null );
    if ( Arez.areNativeComponentsEnabled() ) {
      this.$$arez$$_component.complete();
    }
    this.$$arez$$_state = 2;
    this.$$arez$$_state = 3;
  }

  final ArezContext $$arez$$_context() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arez$$_name() + "'" );
    }
    return Arez.areZonesEnabled() ? this.$$arez$$_context : Arez.context();
  }

  final long $$arez$$_id() {
    return this.$$arez$$_id;
  }

  @Override
  @Nonnull
  public final Long getArezId() {
    return $$arez$$_id();
  }

  String $$arez$$_name() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state == 0, () -> "Method invoked on uninitialized component named '" + $$arez$$_name() + "'" );
    }
    return "DifferentObservableTypesModel." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    if ( $$arez$$_context().isTransactionActive() && !this.$$arez$$_disposedObservable.isDisposed() )  {
      this.$$arez$$_disposedObservable.reportObserved();
    }
    return this.$$arez$$_state < 0;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      this.$$arez$$_state = -2;
      if ( Arez.areNativeComponentsEnabled() ) {
        this.$$arez$$_component.dispose();
      } else {
        $$arez$$_context().safeAction( Arez.areNamesEnabled() ? $$arez$$_name() + ".dispose" : null, () -> { {
          this.$$arez$$_disposedObservable.dispose();
          this.$$arez$$_v6.dispose();
          this.$$arez$$_v7.dispose();
          this.$$arez$$_v8.dispose();
          this.$$arez$$_v9.dispose();
          this.$$arez$$_v1.dispose();
          this.$$arez$$_v2.dispose();
          this.$$arez$$_v3.dispose();
          this.$$arez$$_v4.dispose();
          this.$$arez$$_v5.dispose();
        } } );
      }
      this.$$arez$$_state = -1;
    }
  }

  @Override
  public long getV6() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v6.reportObserved();
    return super.getV6();
  }

  @Override
  public void setV6(final long v6) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v7.reportObserved();
    return super.getV7();
  }

  @Override
  public void setV7(final float v7) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v8.reportObserved();
    return super.getV8();
  }

  @Override
  public void setV8(final double v8) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v9.reportObserved();
    return super.getV9();
  }

  @Override
  public void setV9(final Object v9) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    if ( !Objects.equals( v9, super.getV9() ) ) {
      this.$$arez$$_v9.preReportChanged();
      super.setV9(v9);
      this.$$arez$$_v9.reportChanged();
    }
  }

  @Override
  public boolean isV1() {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v1.reportObserved();
    return super.isV1();
  }

  @Override
  public void setV1(final boolean v1) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v2.reportObserved();
    return super.getV2();
  }

  @Override
  public void setV2(final byte v2) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v3.reportObserved();
    return super.getV3();
  }

  @Override
  public void setV3(final char v3) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v4.reportObserved();
    return super.getV4();
  }

  @Override
  public void setV4(final short v4) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
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
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    this.$$arez$$_v5.reportObserved();
    return super.getV5();
  }

  @Override
  public void setV5(final int v5) {
    if ( Arez.shouldCheckApiInvariants() ) {
      Guards.apiInvariant( () -> this.$$arez$$_state >= 2, () -> "Method invoked on dispos" + (this.$$arez$$_state == -2 ? "ing" : "ed" ) + " component named '" + $$arez$$_name() + "'" );
    }
    if ( v5 != super.getV5() ) {
      this.$$arez$$_v5.preReportChanged();
      super.setV5(v5);
      this.$$arez$$_v5.reportChanged();
    }
  }

  @Override
  public final int hashCode() {
    return Long.hashCode( $$arez$$_id() );
  }

  @Override
  public final boolean equals(final Object o) {
    if ( this == o ) {
      return true;
    } else if ( null == o || !(o instanceof Arez_DifferentObservableTypesModel) ) {
      return false;
    } else {
      final Arez_DifferentObservableTypesModel that = (Arez_DifferentObservableTypesModel) o;;
      return $$arez$$_id() == that.$$arez$$_id();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[" + $$arez$$_name() + "]";
    } else {
      return super.toString();
    }
  }
}
