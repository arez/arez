import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;

@Generated("org.realityforge.arez.processor.ArezProcessor")
public final class Arez_ObservableTypeParametersModel extends ObservableTypeParametersModel implements Disposable {
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  private boolean $$arez$$_disposed;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  public Arez_ObservableTypeParametersModel() {
    super();
    this.$$arez$$_context = Arez.context();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_time = this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
  }

  private String $$arez$$_id() {
    return "ObservableTypeParametersModel." + $$arez$$_id + ".";
  }

  @Override
  public boolean isDisposed() {
    return $$arez$$_disposed;
  }

  @Override
  public void dispose() {
    if ( !isDisposed() ) {
      $$arez$$_disposed = true;
      $$arez$$_time.dispose();
    }
  }

  @Override
  public <T extends Integer> T getTime() {
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public <T extends Integer> void setTime(final T time) {
    if ( !Objects.equals(time, super.getTime()) ) {
      super.setTime(time);
      this.$$arez$$_time.reportChanged();
    }
  }
}
