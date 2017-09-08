import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_ObservableModelWithUnconventionalNames
  extends ObservableModelWithUnconventionalNames
{
  private static volatile long $$arez$$_nextId;

  private final long $$arez$$_id;

  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  public Arez_ObservableModelWithUnconventionalNames( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_id = $$arez$$_nextId++;
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time =
      $$arez$$_context.createObservable( this.$$arez$$_context.areNamesEnabled() ? $$arez$$_id() + "time" : null );
  }

  private String $$arez$$_id()
  {
    return "ObservableModelWithUnconventionalNames." + $$arez$$_id + ".";
  }

  @Override
  public long time()
  {
    this.$$arez$$_time.reportObserved();
    return super.time();
  }

  @Override
  public void time( final long time )
  {
    if ( time != super.time() )
    {
      super.time( time );
      this.$$arez$$_time.reportObserved();
    }
  }
}
