package arez.extras.spy;

import arez.extras.AbstractArezExtrasTest;
import arez.spy.ObserverCreatedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyEventProcessorTest
  extends AbstractArezExtrasTest
{
  private class TestSpyEventProcessor
    extends AbstractSpyEventProcessor
  {
    int _increaseNestingLevelCallCount;
    int _decreaseNestingLevelCallCount;
    int _handleUnhandledEventCallCount;

    @Override
    protected void increaseNestingLevel()
    {
      super.increaseNestingLevel();
      _increaseNestingLevelCallCount += 1;
    }

    @Override
    protected void decreaseNestingLevel()
    {
      super.decreaseNestingLevel();
      _decreaseNestingLevelCallCount += 1;
    }

    @Override
    protected void handleUnhandledEvent( @Nonnull final Object event )
    {
      super.handleUnhandledEvent( event );
      _handleUnhandledEventCallCount += 1;
    }
  }

  @Test
  public void handleUnhandledEvent()
    throws Throwable
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();
    final TransactionStartedEvent event =
      new TransactionStartedEvent( ValueUtil.randomString(), ValueUtil.randomBoolean(), null );

    processor.onSpyEvent( event );

    assertEquals( processor._handleUnhandledEventCallCount, 1 );
  }

  @Test
  public void increaseNesting()
    throws Throwable
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();
    final TransactionStartedEvent event =
      new TransactionStartedEvent( ValueUtil.randomString(), ValueUtil.randomBoolean(), null );

    final AtomicInteger handleCallCount = new AtomicInteger();
    final BiConsumer<SpyUtil.NestingDelta, TransactionStartedEvent> handler =
      ( delta, e ) -> {
        handleCallCount.incrementAndGet();
        assertEquals( delta, SpyUtil.NestingDelta.INCREASE );
        assertEquals( e, event );
      };
    processor.on( TransactionStartedEvent.class, handler );

    assertEquals( processor.getNestingDelta( event ), SpyUtil.NestingDelta.INCREASE );

    assertEquals( handleCallCount.get(), 0 );
    assertEquals( processor.getNestingLevel(), 0 );
    assertEquals( processor._handleUnhandledEventCallCount, 0 );

    processor.onSpyEvent( event );

    assertEquals( handleCallCount.get(), 1 );
    assertEquals( processor.getNestingLevel(), 1 );
    assertEquals( processor._handleUnhandledEventCallCount, 0 );
  }

  @Test
  public void decreaseNesting()
    throws Throwable
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();
    final TransactionCompletedEvent event =
      new TransactionCompletedEvent( ValueUtil.randomString(),
                                     ValueUtil.randomBoolean(),
                                     null,
                                     Math.min( 1, Math.abs( ValueUtil.randomLong() ) ) );

    final AtomicInteger handleCallCount = new AtomicInteger();
    final BiConsumer<SpyUtil.NestingDelta, TransactionCompletedEvent> handler =
      ( delta, e ) -> {
        handleCallCount.incrementAndGet();
        assertEquals( delta, SpyUtil.NestingDelta.DECREASE );
        assertEquals( e, event );
      };
    processor.on( TransactionCompletedEvent.class, handler );

    assertEquals( processor.getNestingDelta( event ), SpyUtil.NestingDelta.DECREASE );

    assertEquals( handleCallCount.get(), 0 );
    assertEquals( processor.getNestingLevel(), 0 );
    assertEquals( processor._handleUnhandledEventCallCount, 0 );

    processor.onSpyEvent( event );

    assertEquals( handleCallCount.get(), 1 );
    assertEquals( processor.getNestingLevel(), -1 );
    assertEquals( processor._handleUnhandledEventCallCount, 0 );
  }

  @Test
  public void maintainNesting()
    throws Throwable
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();

    final ObserverCreatedEvent event = new ObserverCreatedEvent( new NullObserverInfo() );

    final AtomicInteger handleCallCount = new AtomicInteger();
    final BiConsumer<SpyUtil.NestingDelta, ObserverCreatedEvent> handler =
      ( delta, e ) -> {
        handleCallCount.incrementAndGet();
        assertEquals( delta, SpyUtil.NestingDelta.NONE );
        assertEquals( e, event );
      };
    processor.on( ObserverCreatedEvent.class, handler );

    assertEquals( processor.getNestingDelta( event ), SpyUtil.NestingDelta.NONE );

    assertEquals( handleCallCount.get(), 0 );
    assertEquals( processor.getNestingLevel(), 0 );
    assertEquals( processor._handleUnhandledEventCallCount, 0 );

    processor.onSpyEvent( event );

    assertEquals( handleCallCount.get(), 1 );
    assertEquals( processor.getNestingLevel(), 0 );
    assertEquals( processor._handleUnhandledEventCallCount, 0 );
  }

  @Test
  public void onFailsOnDuplicates()
    throws Throwable
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();

    final BiConsumer<SpyUtil.NestingDelta, ObserverCreatedEvent> handler =
      ( delta, e ) -> {
      };
    processor.on( ObserverCreatedEvent.class, handler );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> processor.on( ObserverCreatedEvent.class, handler ) );
    assertEquals( exception.getMessage(),
                  "Attempting to call AbstractSpyEventProcessor.on() to register a processor for type class arez.spy.ObserverCreatedEvent but an existing processor already exists for type" );
  }
}
