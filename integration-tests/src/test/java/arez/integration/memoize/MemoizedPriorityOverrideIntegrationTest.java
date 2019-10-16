package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.PriorityOverride;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MemoizedPriorityOverrideIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    searchTest( ComputableValue.Flags.PRIORITY_LOWEST, "search(b),dynamicPrioritySearch(b)" );
    searchTest( ComputableValue.Flags.PRIORITY_LOW, "search(b),dynamicPrioritySearch(b)" );
    searchTest( ComputableValue.Flags.PRIORITY_NORMAL, "search(b),dynamicPrioritySearch(b)" );
    searchTest( ComputableValue.Flags.PRIORITY_HIGH, "dynamicPrioritySearch(b),search(b)" );
    searchTest( ComputableValue.Flags.PRIORITY_HIGHEST, "dynamicPrioritySearch(b),search(b)" );
  }

  private void searchTest( final int priority, @Nonnull final String expected )
  {
    final ArrayList<String> searches = new ArrayList<>();

    final Model model = Model.create( priority, "ZZZZZZ" );
    final ArezContext context = Arez.context();
    context.observer( () -> {
                        if ( model.search( "b" ) )
                        {
                          searches.add( "search(b)" );
                        }
                        else
                        {
                          searches.add( "NOT(search(b))" );
                        }
                      },
                      Observer.Flags.PRIORITY_HIGHEST |
                      Observer.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    context.observer( () -> {
                        if ( model.dynamicPrioritySearch( "b" ) )
                        {
                          searches.add( "dynamicPrioritySearch(b)" );
                        }
                        else
                        {
                          searches.add( "NOT(dynamicPrioritySearch(b))" );
                        }
                      },
                      Observer.Flags.PRIORITY_HIGHEST |
                      Observer.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    searches.clear();

    model.setName( "bl" );

    assertEquals( String.join( ",", searches ), expected );
  }

  @ArezComponent
  public static abstract class Model
  {
    private final int _dynamicPrioritySearchPriority;

    @Nonnull
    static Model create( final int dynamicPrioritySearchPriority,
                         @SuppressWarnings( "SameParameterValue" ) @Nonnull final String name )
    {
      return new MemoizedPriorityOverrideIntegrationTest_Arez_Model( dynamicPrioritySearchPriority, name );
    }

    Model( final int dynamicPrioritySearchPriority )
    {
      _dynamicPrioritySearchPriority = dynamicPrioritySearchPriority;
    }

    @PriorityOverride
    final int dynamicPrioritySearchPriority()
    {
      return _dynamicPrioritySearchPriority;
    }

    @Observable( writeOutsideTransaction = true )
    @Nonnull
    abstract String getName();

    abstract void setName( @Nonnull String name );

    @Memoize
    boolean search( @Nonnull final String value )
    {
      return getName().contains( value );
    }

    @Memoize
    boolean dynamicPrioritySearch( @Nonnull final String value )
    {
      return getName().contains( value );
    }
  }
}
