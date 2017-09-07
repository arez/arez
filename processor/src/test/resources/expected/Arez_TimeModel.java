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

  protected Arez_TimeModel( @Nonnull final ArezContext $arez$_context, final long time )
  {
    super( time );
    this.$arez$_context = $arez$_context;
    this.$arez$_time = $arez$_context.createObservable( "Time.time" );
  }

  @Override
  public long getTime()
  {
    this.$arez$_time.reportObserved();
    return super.getTime();
  }

  @Override
  public void setTime( final long time )
  {
    if ( super.getTime() != time )
    {
      super.setTime( time );
      this.$arez$_time.reportChanged();
    }
  }

  @Override
  public void updateTime()
  {
    this.$arez$_context.safeProcedure( "Time.updateTime", true, () -> super.updateTime() );
  }
}
