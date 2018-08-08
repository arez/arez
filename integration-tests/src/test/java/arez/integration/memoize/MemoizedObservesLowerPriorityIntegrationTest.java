package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class MemoizedObservesLowerPriorityIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final String name = "MyName";
    final Model model = Model.create( name );

    final ArrayList<String> searches = new ArrayList<>();

    final ArezContext context = Arez.context();
    context.autorun( null,
                     "search1(b)",
                     false,
                     () -> searches.add( model.search1( "b" ) ? "search1(b)" : "NOT(search1(b))" ),
                     Priority.HIGHEST,
                     true,
                     true );

    searches.clear();
    safeAction( () -> model.setName( "bl" ) );

    assertEquals( searches, Collections.singletonList( "search1(b)" ) );

    searches.clear();
    safeAction( () -> model.setName( "blu" ) );

    assertEquals( searches, Collections.emptyList() );
  }

  @ArezComponent
  public static abstract class Model
  {
    @Nonnull
    static Model create( @Nonnull final String name )
    {
      return new MemoizedObservesLowerPriorityIntegrationTest_Arez_Model( name );
    }

    @Observable
    @Nonnull
    abstract String getName();

    abstract void setName( @Nonnull String name );

    @Computed( priority = arez.annotations.Priority.LOWEST )
    boolean isNameEmpty()
    {
      return getName().isEmpty();
    }

    @Memoize( observeLowerPriorityDependencies = true )
    boolean search1( @Nonnull final String value )
    {
      return isNameEmpty() || getName().contains( value );
    }
  }
}
