package arez.component;

import arez.AbstractArezTest;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class IdentifiableTest
  extends AbstractArezTest
{
  static class TestElement
    implements Identifiable<String>
  {
    @Nonnull
    @Override
    public String getArezId()
    {
      return "X";
    }
  }

  @Test
  public void identifiableElement()
  {
    final TestElement element = new TestElement();
    assertEquals( Identifiable.getArezId( element ), "X" );
    assertEquals( Identifiable.<String>asIdentifiable( element ), element );
  }

  @Test
  public void nonIdentifiableElement()
  {
    final Object element = new Object();
    assertNull( Identifiable.getArezId( element ) );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Identifiable.asIdentifiable( element ) );
    assertEquals( exception.getMessage(),
                  "Arez-0158: Object passed to asIdentifiable does not implement Identifiable. Object: " + element );
  }
}
