package arez.when;

import arez.Arez;
import arez.ArezTestUtil;
import arez.Observer;
import arez.ObserverError;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public abstract class AbstractTest
{
  private final ArrayList<String> _observerErrors = new ArrayList<>();
  private boolean _ignoreObserverErrors;

  @BeforeMethod
  protected void beforeTest()
  {
    BrainCheckTestUtil.resetConfig( false );
    ArezTestUtil.resetConfig( false );
    ArezTestUtil.enableZones();
    _ignoreObserverErrors = false;
    _observerErrors.clear();
    Arez.context().addObserverErrorHandler( this::onObserverError );
  }

  @AfterMethod
  protected void afterTest()
  {
    BrainCheckTestUtil.resetConfig( true );
    ArezTestUtil.resetConfig( true );
    if ( !_ignoreObserverErrors && !_observerErrors.isEmpty() )
    {
      fail( "Unexpected Observer Errors: " + String.join( "\n", _observerErrors ) );
    }
  }

  final void ignoreObserverErrors()
  {
    _ignoreObserverErrors = true;
  }

  static void observeDependency()
  {
    Arez.context().observable().reportObserved();
  }

  private void onObserverError( @Nonnull final Observer observer,
                                @Nonnull final ObserverError error,
                                @Nullable final Throwable throwable )
  {
    final String message = "Observer: " + observer.getName() + " Error: " + error + " " + throwable;
    _observerErrors.add( message );
    if ( !_ignoreObserverErrors )
    {
      System.out.println( message );
    }
  }
}
