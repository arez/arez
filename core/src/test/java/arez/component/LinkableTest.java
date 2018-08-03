package arez.component;

import arez.AbstractArezTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LinkableTest
  extends AbstractArezTest
{
  static class TestElement
    implements Linkable
  {
    boolean _linked;

    @Override
    public void link()
    {
      _linked = true;
    }
  }

  @Test
  public void linkableElement()
  {
    final TestElement element = new TestElement();
    assertEquals( element._linked, false );
    Linkable.link( element );
    assertEquals( element._linked, true );
  }

  @Test
  public void nonLinkableElement()
  {
    // Should produce no exceptions
    Linkable.link( new Object() );
  }
}
