package arez.integration.observe;

import arez.Arez;
import arez.ArezContext;
import arez.Environment;
import arez.Function;
import arez.SafeFunction;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TrackInEnvironmentTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class TestComponent1
  {
    private final ArrayList<String> _steps;

    TestComponent1( final ArrayList<String> steps )
    {
      _steps = steps;
    }

    @Observe( executor = Executor.APPLICATION, requireEnvironment = true )
    public void render()
    {
      _steps.add( "render1" );
      observeADependency();
    }

    @OnDepsChanged
    final void onRenderDepsChanged()
    {
    }

    @Observe( executor = Executor.APPLICATION, requireEnvironment = false )
    public void render2()
    {
      _steps.add( "render2" );
      observeADependency();
    }

    @OnDepsChanged
    final void onRender2DepsChanged()
    {
    }
  }

  @Test
  public void scenario()
  {
    final ArezContext context = Arez.context();

    // Scheduler paused otherwise reactions will run in environment and upset our environment call count
    context.pauseScheduler();

    final ArrayList<String> steps = new ArrayList<>();
    context.setEnvironment( new Environment()
    {
      @Override
      public <T> T run( @Nonnull final SafeFunction<T> function )
      {
        try
        {
          steps.add( "EnvironmentStart" );
          return function.call();
        }
        finally
        {
          steps.add( "EnvironmentEnd" );
        }
      }

      @Override
      public <T> T run( @Nonnull final Function<T> function )
        throws Throwable
      {
        try
        {
          steps.add( "EnvironmentStart" );
          return function.call();
        }
        finally
        {
          steps.add( "EnvironmentEnd" );
        }
      }
    } );

    final TestComponent1 component = new TrackInEnvironmentTest_Arez_TestComponent1( steps );

    component.render();

    assertEquals( steps, Arrays.asList( "EnvironmentStart", "render1", "EnvironmentEnd" ) );

    component.render2();

    assertEquals( steps, Arrays.asList( "EnvironmentStart", "render1", "EnvironmentEnd", "render2" ) );
  }
}
