package org.realityforge.arez;

import java.lang.reflect.Field;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    final ArezConfig.DynamicProvider provider = getConfigProvider();
    provider.setEnableNames( true );
    provider.setVerboseErrorMessages( true );
    provider.setCheckInvariants( true );
    provider.setPurgeReactionsWhenRunawayDetected( false );
    provider.setEnforceTransactionType( true );
    provider.setEnableSpy( true );
    getProxyLogger().setLogger( new TestLogger() );
  }

  @AfterMethod
  protected void afterTest()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = getConfigProvider();
    provider.setEnableNames( false );
    provider.setVerboseErrorMessages( false );
    provider.setCheckInvariants( false );
    provider.setPurgeReactionsWhenRunawayDetected( true );
    provider.setEnforceTransactionType( false );
    provider.setEnableSpy( false );
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
  final ArezConfig.DynamicProvider getConfigProvider()
  {
    return (ArezConfig.DynamicProvider) ArezConfig.getProvider();
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
  @Nonnull
  final Observer ensureDerivationHasObserver( @Nonnull final Observer observer )
  {
    final Observer randomObserver = newReadOnlyObserver( observer.getContext() );
    observer.getDerivedValue().addObserver( randomObserver );
    randomObserver.getDependencies().add( observer.getDerivedValue() );
    return randomObserver;
  }

  @Nonnull
  final Observer newReadWriteObserver( @Nonnull final ArezContext context )
  {
    return new Observer( context, ValueUtil.randomString(), TransactionMode.READ_WRITE, new TestReaction() );
  }

  @Nonnull
  final Observer newDerivation( @Nonnull final ArezContext context )
  {
    return new ComputedValue<>( context, ValueUtil.randomString(), () -> "", Objects::equals ).getObserver();
  }

  @Nonnull
  final Observer newReadOnlyObserverWithNoReaction( @Nonnull final ArezContext context )
  {
    return new Observer( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
  }

  @Nonnull
  final Observer newReadOnlyObserver( @Nonnull final ArezContext context )
  {
    return new Observer( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
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
