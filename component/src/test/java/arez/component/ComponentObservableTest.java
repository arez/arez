package arez.component;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentObservableTest
  extends AbstractArezComponentTest
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
  public void componentObservable()
  {
    assertEquals( ComponentObservable.observe( new TestElement( true ) ), true );
    assertEquals( ComponentObservable.observe( new TestElement( false ) ), false );

    final TestElement element = new TestElement( true );
    assertEquals( ComponentObservable.asComponentObservable( element ), element );
  }

  @Test
  public void nonComponentObservable()
  {
    final Object element = new Object();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> ComponentObservable.asComponentObservable( element ) );
    assertEquals( exception.getMessage(),
                  "Arez-0179: Object passed to asComponentObservable does not implement " +
                  "ComponentObservable. Object: " + element );
  }
}
