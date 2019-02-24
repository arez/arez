package arez;

import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public abstract class AbstractArezTest
{
  private final TestLogger _logger = new TestLogger();
  private final ArrayList<String> _observerErrors = new ArrayList<>();
  private boolean _ignoreObserverErrors;

  @BeforeMethod
  protected void beforeTest()
    throws Exception
  {
    ArezTestUtil.resetConfig( false );
    ArezTestUtil.enableZones();
    _logger.getEntries().clear();
    ArezTestUtil.setLogger( _logger );
    _ignoreObserverErrors = false;
    _observerErrors.clear();
    Arez.context().addObserverErrorHandler( this::onObserverError );
    Guards.setOnGuardListener( null );
  }

  @AfterMethod
  protected void afterTest()
  {
    ArezTestUtil.resetConfig( true );
    if ( !_ignoreObserverErrors && !_observerErrors.isEmpty() )
    {
      fail( "Unexpected Observer Errors: " + String.join( "\n", _observerErrors ) );
    }
  }

  @Nonnull
  final TestLogger getTestLogger()
  {
    return _logger;
  }

  @Nonnull
  private ArezLogger.ProxyLogger getProxyLogger()
  {
    return (ArezLogger.ProxyLogger) ArezLogger.getLogger();
  }

  @Nonnull
  private Field getField( @Nonnull final Class<?> type, @Nonnull final String fieldName )
  {
    Class clazz = type;
    while ( null != clazz && Object.class != clazz )
    {
      try
      {
        final Field field = clazz.getDeclaredField( fieldName );
        field.setAccessible( true );
        return field;
      }
      catch ( final Throwable t )
      {
        clazz = clazz.getSuperclass();
      }
    }

    Assert.fail();
    throw new IllegalStateException();
  }

  @SuppressWarnings( "SameParameterValue" )
  final void setField( @Nonnull final Object object, @Nonnull final String fieldName, @Nullable final Object value )
    throws IllegalAccessException
  {
    getField( object.getClass(), fieldName ).set( object, value );
  }

  @Nonnull
  final Observer newReadWriteObserver( @Nonnull final ArezContext context )
  {
    return context.observer( new CountAndObserveProcedure(), Flags.READ_WRITE );
  }

  final void setupReadOnlyTransaction()
  {
    setupReadOnlyTransaction( Arez.context() );
  }

  final void setupReadOnlyTransaction( @Nonnull final ArezContext context )
  {
    Transaction.setTransaction( null );
    setCurrentTransaction( context.observer( new CountAndObserveProcedure() ) );
  }

  final void setupReadWriteTransaction()
  {
    setupReadWriteTransaction( Arez.context() );
  }

  private void setupReadWriteTransaction( @Nonnull final ArezContext context )
  {
    Transaction.setTransaction( null );
    setCurrentTransaction( newReadWriteObserver( context ) );
  }

  final void setCurrentTransaction( @Nonnull final Observer observer )
  {
    Transaction.setTransaction( null );
    final ArezContext context = observer.getContext();
    Transaction.setTransaction( new Transaction( context,
                                                 null,
                                                 ValueUtil.randomString(),
                                                 observer.isMutation(),
                                                 observer,
                                                 false ) );
  }

  protected final void setIgnoreObserverErrors( final boolean ignoreObserverErrors )
  {
    _ignoreObserverErrors = ignoreObserverErrors;
  }

  protected static void assertInvariantFailure( @Nonnull final ThrowingRunnable throwingRunnable,
                                                @Nonnull final String message )
  {
    assertEquals( expectThrows( IllegalStateException.class, throwingRunnable ).getMessage(), message );
  }

  protected static void observeADependency()
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
      if ( null != throwable )
      {
        throwable.printStackTrace();
      }
    }
  }

  @Nonnull
  final ArrayList<String> getObserverErrors()
  {
    return _observerErrors;
  }
}
