import javax.annotation.Generated;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observable;

@Generated( "org.realityforge.arez.processor.ArezProcessor" )
public final class Arez_TimeModel
  extends TimeModel
{
  private final ArezContext $arez$_context;
  private final Observable $arez$_time;

  public Arez_TimeModel( @Nonnull final ArezContext context, final long time )
  {
    super( time );
    $arez$_context = context;
    $arez$_time = context.createObservable( "Time.time" );
  }

  @Override
  public long getTime()
  {
    $arez$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime( final long time )
  {
    if ( super.getTime() != time )
    {
      $arez$_time.reportChanged();
      super.setTime( time );
    }
  }

  @Override
  public void updateTime()
  {
    $arez$_context.safeProcedure( "Time.updateTime", true, () -> super.updateTime() );
  }
}
