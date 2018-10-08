package arez.integration.action;

import arez.Arez;
import arez.ArezContext;
import arez.Environment;
import arez.Function;
import arez.SafeFunction;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( { "UnusedReturnValue", "unused", "SameParameterValue" } )
public class RequiresEnvironmentTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class MyComponent
  {
    private final ArrayList<String> _steps;

    MyComponent( @Nonnull final ArrayList<String> steps )
    {
      _steps = steps;
    }

    @Action( requireEnvironment = true, verifyRequired = false )
    void myAction()
    {
      _steps.add( "myAction" );
    }

    @Action( requireEnvironment = false, verifyRequired = false )
    void myOtherAction()
    {
      _steps.add( "myOtherAction" );
    }
  }

  @Test
  public void scenario()
  {
    final ArezContext context = Arez.context();

    // Scheduler paused otherwise reactions will run in environment and upset our environment call count
    context.pauseScheduler();

    final ArrayList<String> steps = new ArrayList<>();
    final AtomicInteger inEnvironmentCallCount = new AtomicInteger();
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

    final MyComponent component = new RequiresEnvironmentTest_Arez_MyComponent( steps );

    component.myAction();

    assertEquals( steps, Arrays.asList( "EnvironmentStart", "myAction", "EnvironmentEnd" ) );

    component.myOtherAction();

    assertEquals( steps, Arrays.asList( "EnvironmentStart", "myAction", "EnvironmentEnd", "myOtherAction" ) );
  }
}
