package arez;

import arez.spy.SpyEventHandler;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.testng.Assert.*;

final class TestSpyEventHandler
  implements SpyEventHandler
{
  private final ArrayList<Object> _events = new ArrayList<>();
  /**
   * When using assertNextEvent this tracks the index that we are up to.
   */
  private int _currentAssertIndex;

  @Override
  public void onSpyEvent( @Nonnull final Object event )
  {
    _events.add( event );
  }

  void assertEventCount( final int count )
  {
    assertEquals( _events.size(), count, "Actual events: " + _events );
  }

  /**
   * Assert "next" Event is of specific type.
   * Increment the next counter.
   */
  <T> void assertNextEvent( @Nonnull final Class<T> type )
  {
    assertEvent( type, null );
  }

  /**
   * Assert "next" Event is of specific type.
   * Increment the next counter, run action.
   */
  <T> void assertNextEvent( @Nonnull final Class<T> type, @Nonnull final Consumer<T> action )
  {
    assertEvent( type, action );
  }

  private <T> void assertEvent( @Nonnull final Class<T> type, @Nullable final Consumer<T> action )
  {
    assertTrue( _events.size() > _currentAssertIndex );
    final Object e = _events.get( _currentAssertIndex );
    assertTrue( type.isInstance( e ),
                "Expected event at index " + _currentAssertIndex + " to be of type " + type + " but is " +
                " of type " + e.getClass() + " with value " + e );
    _currentAssertIndex++;
    final T event = type.cast( e );
    if ( null != action )
    {
      action.accept( event );
    }
  }
}
