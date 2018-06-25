package arez.component;

import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposeNotifierTest
  extends AbstractArezComponentTest
{
  @Test
  public void construct()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    assertEquals( notifier.getListeners().size(), 0 );
    assertEquals( notifier.isDisposed(), false );
  }

  @Test
  public void addOnDisposeListener()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    assertEquals( notifier.getListeners().size(), 0 );
    assertEquals( callCount.get(), 0 );

    notifier.addOnDisposeListener( key, callCount::incrementAndGet );

    assertEquals( notifier.getListeners().size(), 1 );
    assertEquals( callCount.get(), 0 );

    notifier.getListeners().get( key ).call();

    assertEquals( notifier.getListeners().size(), 1 );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void addOnDisposeListener_afterDispose()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    notifier.dispose();

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> notifier.addOnDisposeListener( key, callCount::incrementAndGet ) );

    assertEquals( exception.getMessage(),
                  "Arez-0170: Attempting to remove add listener but listeners have already been notified." );
  }

  @Test
  public void addOnDisposeListener_duplicate()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    notifier.addOnDisposeListener( key, callCount::incrementAndGet );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> notifier.addOnDisposeListener( key, callCount::incrementAndGet ) );

    assertEquals( exception.getMessage(),
                  "Arez-0166: Attempting to add dispose listener with key '" + key +
                  "' but a listener with that key already exists." );
  }

  @Test
  public void removeOnDisposeListener()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    assertEquals( notifier.getListeners().size(), 0 );

    notifier.addOnDisposeListener( key, callCount::incrementAndGet );

    assertEquals( notifier.getListeners().size(), 1 );

    notifier.removeOnDisposeListener( key );

    assertEquals( notifier.getListeners().size(), 0 );
  }

  @Test
  public void removeOnDisposeListener_after_dispose()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    notifier.addOnDisposeListener( key, callCount::incrementAndGet );

    assertEquals( notifier.getListeners().size(), 1 );

    notifier.dispose();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> notifier.removeOnDisposeListener( key ) );

    assertEquals( exception.getMessage(),
                  "Arez-0169: Attempting to remove dispose listener but listeners have already been notified." );
  }

  @Test
  public void removeOnDisposeListener_notPresent()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key = ValueUtil.randomString();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> notifier.removeOnDisposeListener( key ) );

    assertEquals( exception.getMessage(),
                  "Arez-0167: Attempting to remove dispose listener with key '" + key +
                  "' but no such listener exists." );
  }

  @Test
  public void dispose()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    assertEquals( callCount.get(), 0 );

    notifier.addOnDisposeListener( key, callCount::incrementAndGet );

    assertEquals( callCount.get(), 0 );

    notifier.dispose();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void basicOperation()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key1 = ValueUtil.randomString();
    final String key2 = ValueUtil.randomString();
    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    notifier.addOnDisposeListener( key1, callCount1::incrementAndGet );
    notifier.addOnDisposeListener( key2, callCount2::incrementAndGet );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    notifier.removeOnDisposeListener( key2 );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    assertEquals( notifier.isDisposed(), false );
    notifier.dispose();
    assertEquals( notifier.isDisposed(), true );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 0 );

    // No-op
    notifier.dispose();
  }
}
