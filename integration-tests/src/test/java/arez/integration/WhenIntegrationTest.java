package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.extras.ArezExtras;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;

public class WhenIntegrationTest
  extends AbstractIntegrationTest
{
  /**
   * a basic scenario that uses an extension interface with default methods.
   */
  @Test
  public void timeScenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = new SpyEventRecorder( false );
    context.getSpy().addSpyEventHandler( recorder );

    final TimeModel timeModel = TimeModel.create( 0 );

    timeModel.updateTime();

    ArezExtras.when( () -> 0 == timeModel.getTime(),
                     () -> record( recorder, "timeReset", "true" ) );
    context.autorun( "TimePrinter", () -> {
      // Observe time so we get callback
      final long ignored = timeModel.getTime();
      // Can not record actual time here as it will change run to run and
      // our test infra is not up to skipping fields atm
      record( recorder, "timeReset", "true" );
    } );

    timeModel.updateTime();
    Thread.sleep( 2 );
    timeModel.updateTime();
    Thread.sleep( 2 );
    timeModel.resetTime();
    timeModel.updateTime();
    Thread.sleep( 2 );
    timeModel.updateTime();
    assertEqualsFixture( recorder.eventsAsString() );
  }

  @ArezComponent( type = "Time", nameIncludesId = false )
  public static abstract class TimeModel
    implements TimeModelExtension
  {
    private long _time;

    @Nonnull
    static TimeModel create( final long time )
    {
      return new WhenIntegrationTest_Arez_TimeModel( time );
    }

    TimeModel( final long time )
    {
      _time = time;
    }

    @Override
    public TimeModel self()
    {
      return this;
    }

    @Observable
    public long getTime()
    {
      return _time;
    }

    public void setTime( final long time )
    {
      _time = time;
    }

    @Action
    public void updateTime()
    {
      setTime( System.currentTimeMillis() );
    }
  }

  public interface TimeModelExtension
  {
    TimeModel self();

    @Action
    default void resetTime()
    {
      self().setTime( 0 );
    }
  }
}
