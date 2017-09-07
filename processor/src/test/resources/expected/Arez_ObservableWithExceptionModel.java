import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_ObservableWithExceptionModel
  extends ObservableWithExceptionModel
{
  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  public Arez_ObservableWithExceptionModel( @Nonnull final ArezContext $$arez$$_context )
  {
    super();
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time = $$arez$$_context.createObservable( "ObservableWithExceptionModel.time" );
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
      this.$$arez$$_time.reportObserved();
    }
  }
}
