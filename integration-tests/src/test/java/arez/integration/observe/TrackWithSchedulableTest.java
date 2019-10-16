package arez.integration.observe;

import arez.Arez;
import arez.ArezContext;
import arez.Task;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TrackWithSchedulableTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class Model
  {
    @Observe( executor = Executor.EXTERNAL )
    public int render()
    {
      return 23;
    }

    @OnDepsChange
    final void onRenderDepsChange()
    {
    }

    @Observe
    void other()
    {
      Arez.context().observable().reportObserved();
    }
  }

  @Test
  public void scenario()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();
    context.task( callCount::incrementAndGet, Task.Flags.RUN_LATER );

    assertEquals( callCount.get(), 0 );

    // This has a schedulable element and will trigger the scheduler and thus task will run
    new TrackWithSchedulableTest_Arez_Model();

    assertEquals( callCount.get(), 1 );

    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
  }
}
