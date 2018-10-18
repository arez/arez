package arez.integration.memoize;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;

@SuppressWarnings( "Duplicates" )
public class MemoizeDepTypeTest
  extends AbstractArezIntegrationTest
{
  @SuppressWarnings( { "SameParameterValue", "unused" } )
  @ArezComponent
  public static abstract class Model
  {
    @Nonnull
    static Model create()
    {
      return new MemoizeDepTypeTest_Arez_Model();
    }

    @Memoize( depType = DepType.AREZ )
    boolean search1( @Nonnull final String value )
    {
      return false;
    }

    @Memoize( depType = DepType.AREZ_OR_NONE )
    boolean search2( @Nonnull final String value )
    {
      return false;
    }
  }

  @Test
  public void scenario()
  {
    setIgnoreObserverErrors( true );

    final Model model = Model.create();

    assertInvariant( () -> safeAction( () -> model.search1( ValueUtil.randomString() ) ),
                     "Arez-0173: ComputedValue named 'Model.0.search1.0' completed compute but is not observing any properties. As a result compute will never be rescheduled. This is not a ComputedValue candidate." );

    safeAction( () -> model.search2( ValueUtil.randomString() ) );

    Disposable.dispose( model );
  }
}
