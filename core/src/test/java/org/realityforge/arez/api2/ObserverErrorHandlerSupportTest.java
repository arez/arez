package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserverErrorHandlerSupportTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ObserverErrorHandlerSupport support = new ObserverErrorHandlerSupport();

    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    final ObserverError error = ObserverError.ON_ACTIVATE_ERROR;
    final Throwable throwable = null;

    final AtomicInteger callCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( observerArg, errorArg, throwableArg ) -> {
      callCount.incrementAndGet();
      assertEquals( observerArg, observer );
      assertEquals( errorArg, error );
      assertEquals( throwableArg, throwable );
    };
    support.addObserverErrorHandler( handler );

    support.onObserverError( observer, error, throwable );

    assertEquals( callCount.get(), 1 );

    support.onObserverError( observer, error, throwable );

    assertEquals( callCount.get(), 2 );

    support.removeObserverErrorHandler( handler );

    support.onObserverError( observer, error, throwable );

    // Not called again
    assertEquals( callCount.get(), 2 );
  }

  @Test
  public void addObserverErrorHandler_alreadyExists()
    throws Exception
  {
    final ObserverErrorHandlerSupport support = new ObserverErrorHandlerSupport();

    final ObserverErrorHandler handler = ( observerArg, errorArg, throwableArg ) -> {
    };
    support.addObserverErrorHandler( handler );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> support.addObserverErrorHandler( handler ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add handler " + handler + " that is already in the list of error handlers." );
  }

  @Test
  public void removeObserverErrorHandler_noExists()
    throws Exception
  {
    final ObserverErrorHandlerSupport support = new ObserverErrorHandlerSupport();

    final ObserverErrorHandler handler = ( observerArg, errorArg, throwableArg ) -> {
    };

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> support.removeObserverErrorHandler( handler ) );

    assertEquals( exception.getMessage(),
                  "Attempting to remove handler " + handler + " that is not in the list of error handlers." );
  }

  @Test
  public void multipleHandlers()
  {
    final ObserverErrorHandlerSupport support = new ObserverErrorHandlerSupport();

    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    final ObserverError error = ObserverError.ON_ACTIVATE_ERROR;
    final Throwable throwable = null;

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final ObserverErrorHandler handler1 = ( observerArg, errorArg, throwableArg ) -> callCount1.incrementAndGet();
    final ObserverErrorHandler handler2 = ( observerArg, errorArg, throwableArg ) -> callCount2.incrementAndGet();
    final ObserverErrorHandler handler3 = ( observerArg, errorArg, throwableArg ) -> callCount3.incrementAndGet();
    support.addObserverErrorHandler( handler1 );
    support.addObserverErrorHandler( handler2 );
    support.addObserverErrorHandler( handler3 );

    support.onObserverError( observer, error, throwable );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    support.onObserverError( observer, error, throwable );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount2.get(), 2 );
    assertEquals( callCount3.get(), 2 );
  }

  @Test
  public void onObserverError_whereOneHandlerGeneratesError()
  {
    final ObserverErrorHandlerSupport support = new ObserverErrorHandlerSupport();

    final ArezContext context = new ArezContext();
    final Observer observer = new Observer( context, ValueUtil.randomString() );
    final ObserverError error = ObserverError.ON_ACTIVATE_ERROR;
    final Throwable throwable = null;

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    final ObserverErrorHandler handler1 = ( observerArg, errorArg, throwableArg ) -> callCount1.incrementAndGet();
    final ObserverErrorHandler handler2 = ( observerArg, errorArg, throwableArg ) -> {
      throw exception;
    };
    final ObserverErrorHandler handler3 = ( observerArg, errorArg, throwableArg ) -> callCount3.incrementAndGet();
    support.addObserverErrorHandler( handler1 );
    support.addObserverErrorHandler( handler2 );
    support.addObserverErrorHandler( handler3 );

    support.onObserverError( observer, error, throwable );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    final ArrayList<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 1 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(),
                  "Exception when notifying error handler '" + handler2 + "' of 'ON_ACTIVATE_ERROR' error " +
                  "in observer named '" + observer.getName() + "'." );
    assertEquals( entry1.getThrowable(), exception );

    support.onObserverError( observer, error, throwable );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount3.get(), 2 );

    assertEquals( getTestLogger().getEntries().size(), 2 );
  }
}
