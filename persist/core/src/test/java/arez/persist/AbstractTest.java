package arez.persist;

import arez.persist.runtime.ArezPersistTestUtil;
import arez.testng.ArezTestSupport;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public abstract class AbstractTest
  implements ArezTestSupport
{
  @Nonnull
  private final TestLogger _logger = new TestLogger();

  @BeforeMethod
  public void preTest()
    throws Exception
  {
    ArezTestSupport.super.preTest();
    ArezPersistTestUtil.resetConfig( false );
    _logger.getEntries().clear();
    ArezPersistTestUtil.setLogger( _logger );
  }

  @AfterMethod
  public void postTest()
  {
    ArezPersistTestUtil.resetConfig( true );
    ArezTestSupport.super.postTest();
  }

  @Nonnull
  protected final TestLogger getTestLogger()
  {
    return _logger;
  }

  protected final void assertDefaultToString( @Nonnull final Object object )
  {
    assertEquals( object.toString(), object.getClass().getName() + "@" + Integer.toHexString( object.hashCode() ) );
  }

  protected final void assertInvariantFailure( @Nonnull final ThrowingRunnable throwingRunnable,
                                               @Nonnull final String message )
  {
    assertEquals( expectThrows( IllegalStateException.class, throwingRunnable ).getMessage(), message );
  }

  @Nonnull
  protected final Map<String, Object> randomState()
  {
    final HashMap<String, Object> state = new HashMap<>();
    state.put( ValueUtil.randomString(), ValueUtil.randomString() );
    return state;
  }
}
