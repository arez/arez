package org.realityforge.arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

final class TestSpyEventHandler
  implements SpyEventHandler
{
  private final ArrayList<Object> _events = new ArrayList<>();

  @Override
  public void onSpyEvent( @Nonnull final Object event )
  {
    _events.add( event );
  }

  <T> T assertEvent( @Nonnull final Class<T> type, final int index )
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
    assertEquals( eventCount(), count );
  }

  void assertEventCountAtLeast( final int count )
  {
    assertTrue( eventCount() >= count );
  }

  private int eventCount()
  {
    return _events.size();
  }
}
