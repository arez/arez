package arez;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public abstract class AbstractArezTest
{
  private final ArrayList<String> _observerErrors = new ArrayList<>();
  private boolean _ignoreObserverErrors;

  @BeforeMethod
  protected void beforeTest()
    throws Exception
  {
    BrainCheckTestUtil.resetConfig( false );
    ArezTestUtil.resetConfig( false );
    ArezTestUtil.enableZones();
    ArezTestUtil.resetState();
    getProxyLogger().setLogger( new TestLogger() );
    _ignoreObserverErrors = false;
    _observerErrors.clear();
    Arez.context().addObserverErrorHandler( this::onObserverError );
  }

  @AfterMethod
  protected void afterTest()
    throws Exception
  {
    BrainCheckTestUtil.resetConfig( true );
    ArezTestUtil.resetConfig( true );
    if ( !_ignoreObserverErrors && !_observerErrors.isEmpty() )
    {
      fail( "Unexpected Observer Errors: " + _observerErrors.stream().collect( Collectors.joining( "\n" ) ) );
    }
  }

  @Nonnull
  final TestLogger getTestLogger()
  {
    return (TestLogger) getProxyLogger().getLogger();
  }

  @Nonnull
  private ArezLogger.ProxyLogger getProxyLogger()
  {
    return (ArezLogger.ProxyLogger) ArezLogger.getLogger();
  }

  @Nonnull
  private Field getField( @Nonnull final Class<?> type, @Nonnull final String fieldName )
    throws NoSuchFieldException
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
    throws NoSuchFieldException, IllegalAccessException
  {
    getField( object.getClass(), fieldName ).set( object, value );
  }

  @Nonnull
  final Observer newReadWriteObserver( @Nonnull final ArezContext context )
  {
    return context.autorun( true, new CountAndObserveProcedure() );
  }

  final void setupReadOnlyTransaction()
  {
    setupReadOnlyTransaction( Arez.context() );
  }

  final void setupReadOnlyTransaction( @Nonnull final ArezContext context )
  {
    Transaction.setTransaction( null );
    setCurrentTransaction( context.autorun( new CountAndObserveProcedure() ) );
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
                                                 observer.getMode(),
                                                 observer ) );
  }

  protected final void setIgnoreObserverErrors( final boolean ignoreObserverErrors )
  {
    _ignoreObserverErrors = ignoreObserverErrors;
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
