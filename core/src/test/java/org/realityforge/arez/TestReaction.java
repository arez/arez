package org.realityforge.arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

class TestReaction
  implements Reaction
{
  private final ArrayList<Observer> _observers = new ArrayList<>();

  @Override
  public void react( @Nonnull final Observer observer )
    throws Exception
  {
    observer.getDependencies().forEach( Observable::reportObserved );
    _observers.add( observer );
  }

  void assertObserver( final int index, @Nonnull final Observer observer )
  {
    assertTrue( _observers.size() > index );
    assertEquals( _observers.get( index ), observer );
  }

  int getCallCount()
  {
    return _observers.size();
  }
}
