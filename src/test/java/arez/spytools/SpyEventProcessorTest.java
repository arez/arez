package arez.spytools;

import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import arez.Observer;
import arez.spy.ObserverCreateEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyEventProcessorTest
  extends AbstractSpyToolsTest
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
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();
    final TransactionStartEvent event =
      new TransactionStartEvent( ValueUtil.randomString(), ValueUtil.randomBoolean(), null );

    processor.onSpyEvent( event );

    assertEquals( processor._handleUnhandledEventCallCount, 1 );
  }

  @Test
  public void increaseNesting()
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();
    final TransactionStartEvent event =
      new TransactionStartEvent( ValueUtil.randomString(), ValueUtil.randomBoolean(), null );

    final AtomicInteger handleCallCount = new AtomicInteger();
    final BiConsumer<SpyUtil.NestingDelta, TransactionStartEvent> handler =
      ( delta, e ) -> {
        handleCallCount.incrementAndGet();
        assertEquals( delta, SpyUtil.NestingDelta.INCREASE );
        assertEquals( e, event );
      };
    processor.on( TransactionStartEvent.class, handler );

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
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();
    final TransactionCompleteEvent event =
      new TransactionCompleteEvent( ValueUtil.randomString(),
                                    ValueUtil.randomBoolean(),
                                    null,
                                    Math.min( 1, Math.abs( ValueUtil.randomInt() ) ) );

    final AtomicInteger handleCallCount = new AtomicInteger();
    final BiConsumer<SpyUtil.NestingDelta, TransactionCompleteEvent> handler =
      ( delta, e ) -> {
        handleCallCount.incrementAndGet();
        assertEquals( delta, SpyUtil.NestingDelta.DECREASE );
        assertEquals( e, event );
      };
    processor.on( TransactionCompleteEvent.class, handler );

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
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( observable::reportObserved );

    final ObserverCreateEvent event = new ObserverCreateEvent( context.getSpy().asObserverInfo( observer ) );

    final AtomicInteger handleCallCount = new AtomicInteger();
    final BiConsumer<SpyUtil.NestingDelta, ObserverCreateEvent> handler =
      ( delta, e ) -> {
        handleCallCount.incrementAndGet();
        assertEquals( delta, SpyUtil.NestingDelta.NONE );
        assertEquals( e, event );
      };
    processor.on( ObserverCreateEvent.class, handler );

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
  {
    final TestSpyEventProcessor processor = new TestSpyEventProcessor();

    final BiConsumer<SpyUtil.NestingDelta, ObserverCreateEvent> handler = ( delta, e ) -> {
    };
    processor.on( ObserverCreateEvent.class, handler );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> processor.on( ObserverCreateEvent.class, handler ) );
    assertEquals( exception.getMessage(),
                  "Attempting to call AbstractSpyEventProcessor.on() to register a processor for type class arez.spy.ObserverCreateEvent but an existing processor already exists for type" );
  }
}
