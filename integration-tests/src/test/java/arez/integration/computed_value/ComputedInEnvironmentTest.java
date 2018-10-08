package arez.integration.computed_value;

import arez.Arez;
import arez.ArezContext;
import arez.Environment;
import arez.Function;
import arez.SafeFunction;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedInEnvironmentTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class Element
  {
    private final ArrayList<String> _steps;

    @Nonnull
    public static Element create( @Nonnull final ArrayList<String> steps )
    {
      return new ComputedInEnvironmentTest_Arez_Element( steps );
    }

    Element( @Nonnull final ArrayList<String> steps )
    {
      _steps = steps;
    }

    @Computed( requireEnvironment = true )
    @Nonnull
    String getComputed1()
    {
      _steps.add( "computed1" );
      observeADependency();
      return "";
    }

    @Computed( requireEnvironment = false )
    @Nonnull
    String getComputed2()
    {
      _steps.add( "computed2" );
      observeADependency();
      return "";
    }
  }

  @Test
  public void scenario()
    throws Throwable
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
    final Element element = Element.create( steps );

    safeAction( element::getComputed1 );

    assertEquals( steps, Arrays.asList( "EnvironmentStart", "computed1", "EnvironmentEnd" ) );

    safeAction( element::getComputed2 );

    assertEquals( steps, Arrays.asList( "EnvironmentStart", "computed1", "EnvironmentEnd", "computed2" ) );
  }

}
