package arez;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
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
  private boolean _printObserverErrors;

  @BeforeMethod
  protected void beforeTest()
    throws Exception
  {
    BrainCheckTestUtil.resetConfig( false );
    ArezTestUtil.resetConfig( false );
    ArezTestUtil.enableZones();
    getProxyLogger().setLogger( new TestLogger() );
    _ignoreObserverErrors = false;
    _printObserverErrors = true;
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

  /**
   * Typically called to stop observer from being deactivate or stop invariant checks failing.
   */
  @SuppressWarnings( "UnusedReturnValue" )
  @Nonnull
  final Observer ensureDerivationHasObserver( @Nonnull final Observer observer )
  {
    final Observer randomObserver = newReadOnlyObserver( observer.getContext() );
    randomObserver.setState( ObserverState.UP_TO_DATE );
    observer.getDerivedValue().addObserver( randomObserver );
    randomObserver.getDependencies().add( observer.getDerivedValue() );
    return randomObserver;
  }

  @Nonnull
  final Observer newReadWriteObserver()
  {
    return newReadWriteObserver( Arez.context() );
  }

  @Nonnull
  final Observer newReadWriteObserver( @Nonnull final ArezContext context )
  {
    return new Observer( context,
                         null,
                         ValueUtil.randomString(),
                         null,
                         TransactionMode.READ_WRITE,
                         new TestReaction(),
                         false );
  }

  @Nonnull
  final Observer newDerivation()
  {
    return newDerivation( Arez.context() );
  }

  @Nonnull
  final Observer newDerivation( @Nonnull final ArezContext context )
  {
    return new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals ).getObserver();
  }

  @Nonnull
  final Observer newObserver( @Nonnull final ArezContext context )
  {
    return newReadOnlyObserver( context );
  }

  @Nonnull
  final Observer newReadOnlyObserver()
  {
    return newReadOnlyObserver( Arez.context() );
  }

  @Nonnull
  final Observer newReadOnlyObserver( @Nonnull final ArezContext context )
  {
    return new Observer( context,
                         null,
                         ValueUtil.randomString(),
                         null,
                         TransactionMode.READ_ONLY,
                         new TestReaction(),
                         false );
  }

  @Nonnull
  final Observable<?> newObservable()
  {
    return newObservable( Arez.context() );
  }

  @Nonnull
  final Observable<?> newObservable( final ArezContext context )
  {
    return new Observable<>( context, null, ValueUtil.randomString(), null, null, null );
  }

  final void setupReadOnlyTransaction()
  {
    setupReadOnlyTransaction( Arez.context() );
  }

  final void setupReadOnlyTransaction( @Nonnull final ArezContext context )
  {
    setCurrentTransaction( newReadOnlyObserver( context ) );
  }

  final void setupReadWriteTransaction()
  {
    setupReadWriteTransaction( Arez.context() );
  }

  private void setupReadWriteTransaction( @Nonnull final ArezContext context )
  {
    setCurrentTransaction( newReadWriteObserver( context ) );
  }

  final void setCurrentTransaction( @Nonnull final Observer observer )
  {
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

  protected final void setPrintObserverErrors( final boolean printObserverErrors )
  {
    _printObserverErrors = printObserverErrors;
  }

  private void onObserverError( @Nonnull final Observer observer,
                                @Nonnull final ObserverError error,
                                @Nullable final Throwable throwable )
  {
    final String message = "Observer: " + observer.getName() + " Error: " + error + " " + throwable;
    _observerErrors.add( message );
    if ( _printObserverErrors )
    {
      System.out.println( message );
    }
  }

  @Nonnull
  final ArrayList<String> getObserverErrors()
  {
    return _observerErrors;
  }
}
