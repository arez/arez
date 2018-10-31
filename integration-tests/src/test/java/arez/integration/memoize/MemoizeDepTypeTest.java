package arez.integration.memoize;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.DepType;
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

    @Computed( depType = DepType.AREZ )
    boolean derive1()
    {
      return false;
    }

    @Computed( depType = DepType.AREZ_OR_NONE )
    boolean derive2()
    {
      return false;
    }

    @Computed( depType = DepType.AREZ )
    boolean search1( @Nonnull final String value )
    {
      return false;
    }

    @Computed( depType = DepType.AREZ_OR_NONE )
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

    assertInvariant( () -> safeAction( model::derive1 ),
                     "Arez-0173: ComputableValue named 'Model.0.derive1' completed compute but is not observing any properties. As a result compute will never be rescheduled. This is not a ComputableValue candidate." );

    assertInvariant( () -> safeAction( () -> model.search1( ValueUtil.randomString() ) ),
                     "Arez-0173: ComputableValue named 'Model.0.search1.0' completed compute but is not observing any properties. As a result compute will never be rescheduled. This is not a ComputableValue candidate." );

    safeAction( model::derive2 );
    safeAction( () -> model.search2( ValueUtil.randomString() ) );

    Disposable.dispose( model );
  }
}
