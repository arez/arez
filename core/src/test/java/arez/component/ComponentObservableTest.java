package arez.component;

import arez.AbstractArezTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentObservableTest
  extends AbstractArezTest
{
  static class TestElement
    implements ComponentObservable
  {
    private final boolean _result;

    TestElement( final boolean result )
    {
      _result = result;
    }

    @Override
    public boolean observe()
    {
      return _result;
    }
  }

  @Test
  public void observe()
  {
    assertEquals( ComponentObservable.observe( new TestElement( true ) ), true );
    assertEquals( ComponentObservable.observe( new TestElement( false ) ), false );
  }

  @Test
  public void observe_notComponentObservable()
  {
    final Object element = new Object();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> ComponentObservable.observe( element ) );
    assertEquals( exception.getMessage(),
                  "Arez-0179: Object passed to asComponentObservable does not implement " +
                  "ComponentObservable. Object: " + element );
  }

  @Test
  public void notObserved()
  {
    assertEquals( ComponentObservable.notObserved( new TestElement( true ) ), false );
    assertEquals( ComponentObservable.notObserved( new TestElement( false ) ), true );
  }

  @Test
  public void notObserved_notComponentObservable()
  {
    final Object element = new Object();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> ComponentObservable.notObserved( element ) );
    assertEquals( exception.getMessage(),
                  "Arez-0179: Object passed to asComponentObservable does not implement " +
                  "ComponentObservable. Object: " + element );
  }

  @Test
  public void asComponentObservable()
  {
    final TestElement element = new TestElement( true );
    assertEquals( ComponentObservable.asComponentObservable( element ), element );
  }

  @Test
  public void asComponentObservable_nonObservable()
  {
    final Object element = new Object();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> ComponentObservable.asComponentObservable( element ) );
    assertEquals( exception.getMessage(),
                  "Arez-0179: Object passed to asComponentObservable does not implement " +
                  "ComponentObservable. Object: " + element );
  }
}
