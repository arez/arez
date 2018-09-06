package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.Flags;
import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class MemoizedPriorityIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final SpyEventRecorder recorder = new SpyEventRecorder();

    final String name = "MyName";
    final Model model = Model.create( name );

    final ArrayList<String> searches = new ArrayList<>();

    autorun( model, searches, "b" );
    autorun( model, searches, "blue" );
    autorun( model, searches, "r" );

    Arez.context().getSpy().addSpyEventHandler( recorder );

    searches.clear();
    safeAction( () -> model.setName( "bl" ) );

    assertEquals( searches, Arrays.asList( "search2(b)", "search3(b)", "search1(b)" ), "Searches: " + searches );

    searches.clear();
    safeAction( () -> model.setName( "blu" ) );

    assertEquals( searches, Collections.emptyList() );

    searches.clear();
    safeAction( () -> model.setName( "blue and" ) );

    assertEquals( searches,
                  Arrays.asList( "search2(blue)", "search3(blue)", "search1(blue)" ),
                  "Searches: " + searches );

    searches.clear();
    safeAction( () -> model.setName( "blue and red" ) );

    assertEquals( searches, Arrays.asList( "search2(r)", "search3(r)", "search1(r)" ), "Searches: " + searches );

    searches.clear();
    safeAction( () -> model.setName( "XXXXX" ) );

    assertEquals( searches,
                  Arrays.asList( "NOT(search2(b))",
                                 "NOT(search2(blue))",
                                 "NOT(search2(r))",
                                 "NOT(search3(b))",
                                 "NOT(search3(blue))",
                                 "NOT(search3(r))",
                                 "NOT(search1(b))",
                                 "NOT(search1(blue))",
                                 "NOT(search1(r))" ),
                  "Searches: " + searches );

    assertMatchesFixture( recorder );
  }

  private void autorun( @Nonnull final Model model, @Nonnull final ArrayList<String> searches,
                        @Nonnull final String key )
  {
    final ArezContext context = Arez.context();
    {
      final String observerName = "search1(" + key + ")";
      context.observer( observerName,
                        () -> {
                          if ( model.search1( key ) )
                          {
                            searches.add( observerName );
                          }
                          else
                          {
                            searches.add( "NOT(" + observerName + ")" );
                          }
                        },
                        Flags.PRIORITY_HIGHEST | Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    }
    {
      final String observerName = "search2(" + key + ")";
      context.observer( observerName,
                        () -> {
                          if ( model.search2( key ) )
                          {
                            searches.add( observerName );
                          }
                          else
                          {
                            searches.add( "NOT(" + observerName + ")" );
                          }
                        },
                        Flags.PRIORITY_HIGHEST | Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    }
    {
      final String observerName = "search3(" + key + ")";
      context.observer( observerName,
                        () -> {
                          if ( model.search3( key ) )
                          {
                            searches.add( observerName );
                          }
                          else
                          {
                            searches.add( "NOT(" + observerName + ")" );
                          }
                        },
                        Flags.PRIORITY_HIGHEST | Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    }
  }

  @ArezComponent
  public static abstract class Model
  {
    @Nonnull
    static Model create( @Nonnull final String name )
    {
      return new MemoizedPriorityIntegrationTest_Arez_Model( name );
    }

    @Observable
    @Nonnull
    abstract String getName();

    abstract void setName( @Nonnull String name );

    @Memoize( priority = Priority.LOW )
    boolean search1( @Nonnull final String value )
    {
      return getName().contains( value );
    }

    @Memoize( priority = Priority.HIGH )
    boolean search2( @Nonnull final String value )
    {
      return getName().contains( value );
    }

    @Memoize
    boolean search3( @Nonnull final String value )
    {
      return getName().contains( value );
    }
  }
}
