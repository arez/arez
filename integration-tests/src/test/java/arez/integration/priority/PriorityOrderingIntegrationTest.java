package arez.integration.priority;

import arez.Arez;
import arez.ArezContext;
import arez.Observable;
import arez.Priority;
import arez.Procedure;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.ArrayList;
import java.util.Arrays;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class PriorityOrderingIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable observable = context.observable();
    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final ArrayList<String> runOrder = new ArrayList<>();

    final Procedure action1 = () -> {
      observable.reportObserved();
      runOrder.add( "AR1" );
    };
    final Procedure action2 = () -> {
      observable.reportObserved();
      runOrder.add( "AR2" );
    };
    final Procedure action3 = () -> {
      observable.reportObserved();
      runOrder.add( "AR3" );
    };
    final Procedure action4 = () -> {
      observable.reportObserved();
      runOrder.add( "AR4" );
    };
    final Procedure action5 = () -> {
      observable.reportObserved();
      runOrder.add( "AR5" );
    };
    final Procedure action6 = () -> {
      observable.reportObserved();
      runOrder.add( "AR6" );
    };
    final Procedure action7 = () -> {
      observable.reportObserved();
      runOrder.add( "AR7" );
    };
    context.autorun( "AR1", false, action1, Priority.LOWEST, false );
    context.autorun( "AR2", false, action2, Priority.HIGH, false );
    context.autorun( "AR3", false, action3, Priority.LOW, false );
    context.autorun( "AR4", false, action4, Priority.NORMAL, false );
    context.autorun( "AR5", false, action5, Priority.NORMAL, false );
    context.autorun( "AR6", false, action6, Priority.HIGHEST, false );
    context.autorun( "AR7", false, action7, Priority.HIGH, false );

    assertEquals( runOrder.size(), 0 );

    safeAction( observable::reportChanged );

    assertEquals( runOrder.size(), 7 );
    assertEquals( runOrder, Arrays.asList( "AR6", "AR2", "AR7", "AR4", "AR5", "AR3", "AR1" ) );

    assertMatchesFixture( recorder );
  }
}
