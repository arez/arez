package arez.integration.util;

import arez.spy.SpyEventHandler;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

@SuppressWarnings( { "UnusedReturnValue", "WeakerAccess" } )
public final class TestSpyEventHandler
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

  /**
   * Assert Event at index is of specific type and return it.
   */
  @Nonnull
  public <T> T assertEvent( @Nonnull final Class<T> type, final int index )
  {
    assertTrue( eventCount() > index );
    final Object event = _events.get( index );
    assertTrue( type.isInstance( event ),
                "Expected event at index " + index + " to be of type " + type + " but is " +
                "of type " + event.getClass() + " with value " + event );
    return type.cast( event );
  }

  public void assertEventCount( final int count )
  {
    assertEquals( eventCount(), count, "Actual events: " + _events );
  }

  /**
   * Assert "next" Event is of specific type.
   * Increment the next counter.
   */
  @Nonnull
  public <T> T assertNextEvent( @Nonnull final Class<T> type )
  {
    final T event = assertEvent( type, _currentAssertIndex );
    _currentAssertIndex++;
    return event;
  }

  /**
   * Assert "next" Event is of specific type.
   * Increment the next counter, run action.
   */
  @Nonnull
  public <T> T assertNextEvent( @Nonnull final Class<T> type, @Nonnull final Consumer<T> action )
  {
    final T event = assertNextEvent( type );
    action.accept( event );
    return event;
  }

  public int eventCount()
  {
    return _events.size();
  }
}
