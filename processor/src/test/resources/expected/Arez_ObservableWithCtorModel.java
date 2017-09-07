import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_ObservableWithCtorModel
  extends ObservableWithCtorModel
{
  @Nonnull
  private final ArezContext $$arez$$_context;

  @Nonnull
  private final Observable $$arez$$_time;

  public Arez_ObservableWithCtorModel( @Nonnull final ArezContext $$arez$$_context, final long time )
  {
    super( time );
    this.$$arez$$_context = $$arez$$_context;
    this.$$arez$$_time = $$arez$$_context.createObservable( "ObservableWithCtorModel.time" );
  }

  @Override
  public long getTime()
  {
    this.$$arez$$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime( final long time )
  {
    if ( time != super.getTime() )
    {
      super.setTime( time );
      this.$$arez$$_time.reportObserved();
    }
  }
}
