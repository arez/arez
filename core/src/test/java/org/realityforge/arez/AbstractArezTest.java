package org.realityforge.arez;

import java.lang.reflect.Field;
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
  final Field getField( @Nonnull final Class<?> type, @Nonnull final String fieldName )
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
    final Observer randomObserver = new Observer( observer.getContext(), ValueUtil.randomString() );
    observer.getDerivedValue().addObserver( randomObserver );
    randomObserver.getDependencies().add( observer.getDerivedValue() );
    return observer;
  }

  @Nonnull
  protected final Observer newReadWriteObserver( @Nonnull final ArezContext context )
  {
    return new Observer( context, ValueUtil.randomString(), TransactionMode.READ_WRITE, new TestReaction() );
  }

  @Nonnull
  protected final Observer newDerivation( @Nonnull final ArezContext context )
  {
    return new Observer( context, ValueUtil.randomString(), TransactionMode.READ_WRITE_OWNED, new TestReaction() );
  }

  @Nonnull
  protected final Observer newReadOnlyObserver( @Nonnull final ArezContext context )
  {
    return new Observer( context, ValueUtil.randomString(), TransactionMode.READ_ONLY, new TestReaction() );
  }

  protected final void setCurrentTransaction( @Nonnull final ArezContext context )
  {
    setCurrentTransaction( context, newReadOnlyObserver( context ) );
  }

  protected final void setCurrentTransaction( @Nonnull final ArezContext context, @Nonnull final Observer observer )
  {
    context.setTransaction( new Transaction( context,
                                             null,
                                             ValueUtil.randomString(),
                                             observer.getMode(),
                                             observer ) );
  }
}
