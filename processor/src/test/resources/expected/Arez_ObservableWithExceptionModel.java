import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_ObservableWithExceptionModel
  extends ObservableWithExceptionModel
  implements Disposable
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  public Arez_ObservableWithExceptionModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time =
      this.$$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
  }

  private String $$arez$$_id()
  {
    return "ObservableWithExceptionModel." + $$arez$$_id + ".";
  }

  @Override
  public void dispose()
  {
    $$arez$$_time.dispose();
  }

  @Override
  public long getTime()
    throws Exception
  {
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime( final long time )
    throws Exception
  {
    if ( time != super.getTime() )
    {
      super.setTime( time );
      this.$$arez$$_time.reportChanged();
    }
  }
}
