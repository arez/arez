import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ObservableGuessingModel extends ObservableGuessingModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_string;

  @Nonnull
  private final Observable $$arez$$_foo;

  @Nonnull
  private final Observable $$arez$$_time;

  public Arez_ObservableGuessingModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_string = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".string" : null );
    this.$$arez$$_foo = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".foo" : null );
    this.$$arez$$_time = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_name() + ".time" : null );
  }

  final long $$arez$$_id() {
    return $$arez$$_id;
  }

  String $$arez$$_name() {
    return "ObservableGuessingModel." + $$arez$$_id();
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_string.dispose();
      $$arez$$_foo.dispose();
      $$arez$$_time.dispose();
    }
  }

  @Override
  public String getString() {
    this.$$arez$$_string.reportObserved();
    return super.getString();
  }

  @Override
  public void setString(final String v) {
    if ( !Objects.equals(v, super.getString()) ) {
      super.setString(v);
      this.$$arez$$_string.reportChanged();
    }
  }

  @Override
  public boolean isFoo() {
    this.$$arez$$_foo.reportObserved();
    return super.isFoo();
  }

  @Override
  public void setFoo(final boolean x) {
    if ( x != super.isFoo() ) {
      super.setFoo(x);
      this.$$arez$$_foo.reportChanged();
    }
  }

  @Override
  public long getTime() {
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime(final long time) {
    if ( time != super.getTime() ) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }
}
