package arez;

import arez.spy.SpyEventHandler;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyEventHandlerSupportTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final SpyEventHandlerSupport support = new SpyEventHandlerSupport();

    final Object event = new Object();

    final AtomicInteger callCount = new AtomicInteger();

    final SpyEventHandler handler = e -> {
      callCount.incrementAndGet();
      assertEquals( e, event );
    };

    assertFalse( support.willPropagateSpyEvents() );

    support.addSpyEventHandler( handler );

    assertTrue( support.willPropagateSpyEvents() );

    assertEquals( support.getSpyEventHandlers().size(), 1 );
    assertTrue( support.getSpyEventHandlers().contains( handler ) );

    assertEquals( callCount.get(), 0 );

    support.reportSpyEvent( event );

    assertEquals( callCount.get(), 1 );

    support.removeSpyEventHandler( handler );

    assertFalse( support.willPropagateSpyEvents() );

    assertEquals( support.getSpyEventHandlers().size(), 0 );
  }

  @Test
  public void reportSpyEvent_whenNoListeners()
  {
    final SpyEventHandlerSupport support = new SpyEventHandlerSupport();

    assertFalse( support.willPropagateSpyEvents() );

    final Object event = new Object();

    assertInvariantFailure( () -> support.reportSpyEvent( event ),
                            "Arez-0104: Attempting to report SpyEvent '" + event +
                            "' but willPropagateSpyEvents() returns false." );
  }

  @Test
  public void addSpyEventHandler_alreadyExists()
  {
    final SpyEventHandlerSupport support = new SpyEventHandlerSupport();

    final SpyEventHandler handler = new TestSpyEventHandler( Arez.context() );
    support.addSpyEventHandler( handler );

    assertInvariantFailure( () -> support.addSpyEventHandler( handler ),
                            "Arez-0102: Attempting to add handler " + handler + " that is already " +
                            "in the list of spy handlers." );
  }

  @Test
  public void removeSpyEventHandler_noExists()
  {
    final SpyEventHandlerSupport support = new SpyEventHandlerSupport();

    final SpyEventHandler handler = new TestSpyEventHandler( Arez.context() );

    assertInvariantFailure( () -> support.removeSpyEventHandler( handler ),
                            "Arez-0103: Attempting to remove handler " + handler +
                            " that is not in the list of spy handlers." );
  }

  @Test
  public void multipleHandlers()
  {
    final SpyEventHandlerSupport support = new SpyEventHandlerSupport();

    final Object event = new Object();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final SpyEventHandler handler1 = e -> callCount1.incrementAndGet();
    final SpyEventHandler handler2 = e -> callCount2.incrementAndGet();
    final SpyEventHandler handler3 = e -> callCount3.incrementAndGet();
    support.addSpyEventHandler( handler1 );
    support.addSpyEventHandler( handler2 );
    support.addSpyEventHandler( handler3 );

    assertEquals( support.getSpyEventHandlers().size(), 3 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount2.get(), 2 );
    assertEquals( callCount3.get(), 2 );
  }

  @Test
  public void onSpyEvent_whereOneHandlerGeneratesError()
  {
    final SpyEventHandlerSupport support = new SpyEventHandlerSupport();

    final Object event = new Object();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    final SpyEventHandler handler1 = e -> callCount1.incrementAndGet();
    final SpyEventHandler handler2 = e -> {
      throw exception;
    };
    final SpyEventHandler handler3 = e -> callCount3.incrementAndGet();
    support.addSpyEventHandler( handler1 );
    support.addSpyEventHandler( handler2 );
    support.addSpyEventHandler( handler3 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    final ArrayList<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 1 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(),
                  "Exception when notifying spy handler '" + handler2 + "' of '" + event + "' event." );
    assertEquals( entry1.getThrowable(), exception );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount3.get(), 2 );

    assertEquals( getTestLogger().getEntries().size(), 2 );
  }
}
