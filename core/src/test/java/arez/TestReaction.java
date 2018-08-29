package arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

class TestReaction
  implements Reaction
{
  private final ArrayList<Observer> _observers = new ArrayList<>();
  private int _currentIndex;

  @Override
  public void react( @Nonnull final Observer observer )
    throws Exception
  {
    observer.getContext().
      safeAction( observer.getName(), observer.getMode(), false, true, () -> performReact( observer ), observer );
  }

  void performReact( @Nonnull final Observer observer )
  {
    observer.getDependencies().stream().filter( o -> o.isNotDisposed() ).forEach( ObservableValue::reportObserved );
    _observers.add( observer );
  }

  void assertNextObserver( @Nonnull final Observer observer )
  {
    final int index = _currentIndex;
    _currentIndex++;
    assertTrue( _observers.size() > index );
    assertEquals( _observers.get( index ), observer );
  }

  int getCallCount()
  {
    return _observers.size();
  }
}
