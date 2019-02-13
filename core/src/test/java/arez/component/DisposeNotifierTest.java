package arez.component;

import arez.AbstractArezTest;
import arez.Disposable;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposeNotifierTest
  extends AbstractArezTest
{
  @Test
  public void construct()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    assertEquals( notifier.getListeners().size(), 0 );
    assertFalse( notifier.isDisposed() );
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

    assertInvariantFailure( () -> notifier.addOnDisposeListener( key, callCount::incrementAndGet ),
                            "Arez-0170: Attempting to add OnDispose listener but DisposeNotifier has been disposed." );
  }

  @Test
  public void addOnDisposeListener_duplicate()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();

    notifier.addOnDisposeListener( key, callCount::incrementAndGet );

    assertInvariantFailure( () -> notifier.addOnDisposeListener( key, callCount::incrementAndGet ),
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

    // Perfectly legitimate to remove after dispose and can occur in certain application sequences
    notifier.removeOnDisposeListener( key );

    assertEquals( notifier.getListeners().size(), 0 );
  }

  @Test
  public void removeOnDisposeListener_notPresent()
  {
    final DisposeNotifier notifier = new DisposeNotifier();

    final String key = ValueUtil.randomString();
    assertInvariantFailure( () -> notifier.removeOnDisposeListener( key ),
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

    /*
     * Three listeners required in test. Two that are disposed
     * concurrently and one that is disposed ahead of time
     */

    final String key1 = ValueUtil.randomString();
    final String key2 = ValueUtil.randomString();
    final String key3 = ValueUtil.randomString();
    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );
    assertEquals( callCount3.get(), 0 );

    notifier.addOnDisposeListener( key1, callCount1::incrementAndGet );
    notifier.addOnDisposeListener( key2, callCount2::incrementAndGet );
    notifier.addOnDisposeListener( key3, callCount3::incrementAndGet );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );
    assertEquals( callCount3.get(), 0 );

    notifier.removeOnDisposeListener( key2 );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );
    assertEquals( callCount3.get(), 0 );

    assertFalse( notifier.isDisposed() );
    notifier.dispose();
    assertTrue( notifier.isDisposed() );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 0 );
    assertEquals( callCount3.get(), 1 );

    // No-op
    notifier.dispose();
  }

  @Test
  public void disposeWhenAListenerHasDisposedKey()
  {
    final DisposeNotifier notifier = new DisposeNotifier();
    final String key1 = ValueUtil.randomString();
    final Disposable key2 = new Disposable()
    {
      @Override
      public void dispose()
      {
      }

      @Override
      public boolean isDisposed()
      {
        return true;
      }
    };
    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    notifier.addOnDisposeListener( key1, callCount1::incrementAndGet );
    notifier.addOnDisposeListener( key2, callCount2::incrementAndGet );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    assertEquals( callCount1.get(), 0 );
    assertEquals( callCount2.get(), 0 );

    assertFalse( notifier.isDisposed() );
    notifier.dispose();
    assertTrue( notifier.isDisposed() );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 0 );
  }
}
