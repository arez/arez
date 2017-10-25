package org.realityforge.arez;

import java.lang.reflect.Field;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractArezTest
{
  @BeforeMethod
  protected void beforeTest()
    throws Exception
  {
    BrainCheckTestUtil.resetConfig( false );
    ArezTestUtil.setEnableNames( true );
    ArezTestUtil.setPurgeReactionsWhenRunawayDetected( false );
    ArezTestUtil.setEnforceTransactionType( true );
    ArezTestUtil.setEnableSpy( true );
    ArezTestUtil.setEnableZones( false );
    getProxyLogger().setLogger( new TestLogger() );
    ArezTestUtil.clearProvider();
  }

  @AfterMethod
  protected void afterTest()
    throws Exception
  {
    BrainCheckTestUtil.resetConfig( true );
    ArezTestUtil.setEnableNames( false );
    ArezTestUtil.setPurgeReactionsWhenRunawayDetected( true );
    ArezTestUtil.setEnforceTransactionType( false );
    ArezTestUtil.setEnableSpy( false );
    ArezTestUtil.setEnableZones( false );
    getProxyLogger().setLogger( null );
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
  final Observer newReadWriteObserver( @Nonnull final ArezContext context )
  {
    return new Observer( context,
                         ValueUtil.randomString(), null,
                         TransactionMode.READ_WRITE,
                         new TestReaction(),
                         false );
  }

  @Nonnull
  final Observer newDerivation( @Nonnull final ArezContext context )
  {
    return new ComputedValue<>( context, ValueUtil.randomString(), () -> "", Objects::equals ).getObserver();
  }

  @Nonnull
  final Observer newObserver( @Nonnull final ArezContext context )
  {
    return newReadOnlyObserver( context );
  }

  @Nonnull
  final Observer newReadOnlyObserver( @Nonnull final ArezContext context )
  {
    return new Observer( context, ValueUtil.randomString(), null, TransactionMode.READ_ONLY, new TestReaction(), false );
  }

  @Nonnull
  final Observable newObservable( final ArezContext context )
  {
    return new Observable( context, ValueUtil.randomString() );
  }

  final void setCurrentTransaction( @Nonnull final ArezContext context )
  {
    setCurrentTransaction( newReadOnlyObserver( context ) );
  }

  final void setCurrentTransaction( @Nonnull final Observer observer )
  {
    final ArezContext context = observer.getContext();
    context.setTransaction( new Transaction( context,
                                             null,
                                             ValueUtil.randomString(),
                                             observer.getMode(),
                                             observer ) );
  }
}
