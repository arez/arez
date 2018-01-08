package arez;

import java.util.ArrayList;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

final class TestSpyEventHandler
  implements SpyEventHandler
{
  private final ArrayList<Object> _events = new ArrayList<>();
  /**
   * When ussing assertNextEvent this tracks the index that we are up to.
   */
  private int _currentAssertIndex;

  @Override
  public void onSpyEvent( @Nonnull final Object event )
  {
    _events.add( event );
  }

  /**
   * Assert Event exists in list and return it.
   */
  @Nonnull
  <T> T assertEvent( @Nonnull final Class<T> type )
  {
    for ( final Object event : _events )
    {
      if ( type.isInstance( event ) )
      {
        return type.cast( event );
      }
    }
    fail( "Unable to locate event of type " + type + " in event list " + _events );
    return null;
  }

  /**
   * Assert Event at index is of specific type and return it.
   */
  @Nonnull
  <T> T assertEvent( @Nonnull final Class<T> type, @Nonnegative final int index )
  {
    assertTrue( eventCount() > index );
    final Object event = _events.get( index );
    assertTrue( type.isInstance( event ),
                "Expected event at index " + index + " to be of type " + type + " but is " +
                " of type " + event.getClass() + " with value " + event );
    return type.cast( event );
  }

  void assertEventCount( final int count )
  {
    assertEquals( eventCount(), count, "Actual events: " + _events );
  }

  void assertEventCountAtLeast( final int count )
  {
    assertTrue( eventCount() >= count, "Expected more than " + count + ". Actual events: " + _events );
  }

  /**
   * Assert "next" Event is of specific type.
   * Increment the next counter.
   */
  @Nonnull
  <T> T assertNextEvent( @Nonnull final Class<T> type )
  {
    final T event = assertEvent( type, _currentAssertIndex );
    _currentAssertIndex++;
    return event;
  }

  private int eventCount()
  {
    return _events.size();
  }
}
