import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.braincheck.Guards;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_DifferentObservableTypesModel extends DifferentObservableTypesModel implements Disposable {
  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_v6;

  @Nonnull
  private final Observable $$arez$$_v7;

  @Nonnull
  private final Observable $$arez$$_v8;

  @Nonnull
  private final Observable $$arez$$_v9;

  @Nonnull
  private final Observable $$arez$$_v1;

  @Nonnull
  private final Observable $$arez$$_v2;

  @Nonnull
  private final Observable $$arez$$_v3;

  @Nonnull
  private final Observable $$arez$$_v4;

  @Nonnull
  private final Observable $$arez$$_v5;

  public Arez_DifferentObservableTypesModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_v6 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v6" : null );
    this.$$arez$$_v7 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v7" : null );
    this.$$arez$$_v8 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v8" : null );
    this.$$arez$$_v9 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v9" : null );
    this.$$arez$$_v1 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v1" : null );
    this.$$arez$$_v2 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v2" : null );
    this.$$arez$$_v3 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v3" : null );
    this.$$arez$$_v4 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v4" : null );
    this.$$arez$$_v5 = this.$$arez$$_context.createObservable( Arez.areNamesEnabled() ? "DifferentObservableTypesModel.v5" : null );
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_v6.dispose();
      $$arez$$_v7.dispose();
      $$arez$$_v8.dispose();
      $$arez$$_v9.dispose();
      $$arez$$_v1.dispose();
      $$arez$$_v2.dispose();
      $$arez$$_v3.dispose();
      $$arez$$_v4.dispose();
      $$arez$$_v5.dispose();
    }
  }

  @Override
  public long getV6() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v6.reportObserved();
    return super.getV6();
  }

  @Override
  public void setV6(final long v6) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( v6 != super.getV6() ) {
      super.setV6(v6);
      this.$$arez$$_v6.reportChanged();
    }
  }

  @Override
  public float getV7() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v7.reportObserved();
    return super.getV7();
  }

  @Override
  public void setV7(final float v7) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( v7 != super.getV7() ) {
      super.setV7(v7);
      this.$$arez$$_v7.reportChanged();
    }
  }

  @Override
  public double getV8() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v8.reportObserved();
    return super.getV8();
  }

  @Override
  public void setV8(final double v8) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( v8 != super.getV8() ) {
      super.setV8(v8);
      this.$$arez$$_v8.reportChanged();
    }
  }

  @Override
  public Object getV9() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v9.reportObserved();
    return super.getV9();
  }

  @Override
  public void setV9(final Object v9) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( !Objects.equals(v9, super.getV9()) ) {
      super.setV9(v9);
      this.$$arez$$_v9.reportChanged();
    }
  }

  @Override
  public boolean isV1() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v1.reportObserved();
    return super.isV1();
  }

  @Override
  public void setV1(final boolean v1) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( v1 != super.isV1() ) {
      super.setV1(v1);
      this.$$arez$$_v1.reportChanged();
    }
  }

  @Override
  public byte getV2() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v2.reportObserved();
    return super.getV2();
  }

  @Override
  public void setV2(final byte v2) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( v2 != super.getV2() ) {
      super.setV2(v2);
      this.$$arez$$_v2.reportChanged();
    }
  }

  @Override
  public char getV3() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v3.reportObserved();
    return super.getV3();
  }

  @Override
  public void setV3(final char v3) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( v3 != super.getV3() ) {
      super.setV3(v3);
      this.$$arez$$_v3.reportChanged();
    }
  }

  @Override
  public short getV4() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v4.reportObserved();
    return super.getV4();
  }

  @Override
  public void setV4(final short v4) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( v4 != super.getV4() ) {
      super.setV4(v4);
      this.$$arez$$_v4.reportChanged();
    }
  }

  @Override
  public int getV5() {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    this.$$arez$$_v5.reportObserved();
    return super.getV5();
  }

  @Override
  public void setV5(final int v5) {
    Guards.invariant( () -> !$$arez$$_disposed, () -> "Method invoked on invalid component 'DifferentObservableTypesModel'" );
    if ( v5 != super.getV5() ) {
      super.setV5(v5);
      this.$$arez$$_v5.reportChanged();
    }
  }

  @Override
  public final String toString() {
    if ( Arez.areNamesEnabled() ) {
      return "ArezComponent[DifferentObservableTypesModel]";
    } else {
      return super.toString();
    }
  }
}
