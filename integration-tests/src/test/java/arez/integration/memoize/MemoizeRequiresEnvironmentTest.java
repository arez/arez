package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.Environment;
import arez.Function;
import arez.SafeFunction;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class MemoizeRequiresEnvironmentTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class Model
  {
    private final ArrayList<String> _steps;

    @Nonnull
    static Model create( @Nonnull final String name, @Nonnull final ArrayList<String> steps )
    {
      return new MemoizeRequiresEnvironmentTest_Arez_Model( steps, name );
    }

    Model( final ArrayList<String> steps )
    {
      _steps = steps;
    }

    @Observable
    @Nonnull
    abstract String getName();

    abstract void setName( @Nonnull String name );

    @SuppressWarnings( "SameParameterValue" )
    @Memoize( requireEnvironment = true )
    boolean search1( @Nonnull final String value )
    {
      _steps.add( "search1" );
      return getName().contains( value );
    }

    @SuppressWarnings( "SameParameterValue" )
    @Memoize( requireEnvironment = false )
    boolean search2( @Nonnull final String value )
    {
      _steps.add( "search2" );
      return getName().contains( value );
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

    final Model model = Model.create( ValueUtil.randomString(), steps );

    safeAction( () -> model.search1( ValueUtil.randomString() ) );

    assertEquals( steps, Arrays.asList( "EnvironmentStart", "search1", "EnvironmentEnd" ) );

    safeAction( () -> model.search2( ValueUtil.randomString() ) );

    assertEquals( steps, Arrays.asList( "EnvironmentStart", "search1", "EnvironmentEnd", "search2" ) );
  }
}
