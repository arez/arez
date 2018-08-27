package arez;

import arez.spy.TransactionInfo;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TransactionInfoImplTest
  extends AbstractArezTest
{
  @Test
  public void nonTracking_READ_WRITE()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    final TransactionInfo info = transaction.asInfo();

    assertEquals( info.getName(), transaction.getName() );
    assertEquals( info.getParent(), null );
    assertEquals( info.isReadOnly(), false );
    assertEquals( info.isTracking(), false );
  }

  @Test
  public void getTracker_whenNoTracker()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_WRITE, null );
    final TransactionInfo info = transaction.asInfo();

    final IllegalStateException exception = expectThrows( IllegalStateException.class, info::getTracker );

    assertEquals( exception.getMessage(),
                  "Invoked getTracker on TransactionInfo named '" +
                  transaction.getName() +
                  "' but no tracker exists." );
  }

  @Test
  public void transaction_calculating_computedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer = newComputedValueObserver( context );
    final Transaction transaction =
      new Transaction( context, null, observer.getName(), observer.getMode(), observer );
    final TransactionInfo info = transaction.asInfo();

    assertEquals( info.getName(), transaction.getName() );
    assertEquals( info.getParent(), null );
    assertEquals( info.isReadOnly(), true );
    assertEquals( info.isTracking(), true );
    assertEquals( info.getTracker().getName(), observer.getName() );
  }

  @Test
  public void nested()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer1 = newReadOnlyObserver( context );
    final Observer observer2 = newReadOnlyObserver( context );

    final Transaction transaction1 =
      new Transaction( context, null, observer1.getName(), TransactionMode.READ_ONLY, observer1 );
    final Transaction transaction2 =
      new Transaction( context, transaction1, observer2.getName(), TransactionMode.READ_ONLY, observer2 );
    final TransactionInfo info = transaction2.asInfo();

    assertEquals( info.getName(), transaction2.getName() );
    final TransactionInfo parent = info.getParent();
    assertNotNull( parent );
    assertEquals( parent.getName(), transaction1.getName() );

    // Ensure the same instance is returned if parent is called multiple times
    assertTrue( info.getParent() == parent );
  }
}
