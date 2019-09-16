package arez.component;

import arez.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentObservableTest
  extends AbstractTest
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
    assertTrue( ComponentObservable.observe( new TestElement( true ) ) );
    assertFalse( ComponentObservable.observe( new TestElement( false ) ) );
  }

  @Test
  public void observe_notComponentObservable()
  {
    final Object element = new Object();
    assertInvariantFailure( () -> ComponentObservable.observe( element ),
                            "Arez-0179: Object passed to asComponentObservable does not implement " +
                            "ComponentObservable. Object: " + element );
  }

  @Test
  public void notObserved()
  {
    assertFalse( ComponentObservable.notObserved( new TestElement( true ) ) );
    assertTrue( ComponentObservable.notObserved( new TestElement( false ) ) );
  }

  @Test
  public void notObserved_notComponentObservable()
  {
    final Object element = new Object();
    assertInvariantFailure( () -> ComponentObservable.notObserved( element ),
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
    assertInvariantFailure( () -> ComponentObservable.asComponentObservable( element ),
                            "Arez-0179: Object passed to asComponentObservable does not implement " +
                            "ComponentObservable. Object: " + element );
  }
}
