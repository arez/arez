package arez.when;

import arez.Priority;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class WhenIntegrationTest
  extends AbstractTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArrayList<String> marks = new ArrayList<>();

    final TimeModel timeModel = TimeModel.create( 1000L );

    When.when( null,
               "MyWhen",
               false,
               false,
               () -> {
                 final long time = timeModel.getTime();
                 marks.add( "tick-" + time );
                 return 0 == time;
               },
               () -> marks.add( "timeReset" ),
               Priority.NORMAL,
               true );

    timeModel.updateTime( 800L );
    timeModel.updateTime( 500L );
    timeModel.updateTime( 0L );
    timeModel.updateTime( 300L );
    timeModel.updateTime( 700L );

    assertEquals( marks, Arrays.asList( "tick-1000", "tick-800", "tick-500", "tick-0", "timeReset" ) );
  }

  @ArezComponent
  static abstract class TimeModel
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

    @Observable
    long getTime()
    {
      return _time;
    }

    void setTime( final long time )
    {
      _time = time;
    }

    @Action
    void updateTime( final long time )
    {
      setTime( time );
    }
  }
}
