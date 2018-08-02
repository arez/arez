package arez.component;

import arez.AbstractArezTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "SuspiciousMethodCalls" )
public class EntityLocatorTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final TestEntityLocator locator = new TestEntityLocator();

    {
      assertNull( locator.findById( A.class, 23 ) );
      final NoSuchEntityException exception =
        expectThrows( NoSuchEntityException.class, () -> locator.getById( A.class, 23 ) );
      assertEquals( exception.getId(), 23 );
    }

    final HashMap<Integer, A> entities = new HashMap<>();

    locator.registerLookup( A.class, entities::get );

    {
      assertNull( locator.findById( A.class, 23 ) );
      final NoSuchEntityException exception =
        expectThrows( NoSuchEntityException.class, () -> locator.getById( A.class, 23 ) );
      assertEquals( exception.getId(), 23 );
    }

    final A entity = new A();
    entities.put( 23, entity );

    assertEquals( locator.findById( A.class, 23 ), entity );
    assertEquals( locator.getById( A.class, 23 ), entity );
  }

  @Test
  public void registerLookup_duplicate()
  {
    final TestEntityLocator locator = new TestEntityLocator();

    locator.registerLookup( A.class, i -> new A() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> locator.registerLookup( A.class, i -> new A() ) );
    assertEquals( exception.getMessage(),
                  "Arez-0188: Attempting to register lookup function for type class arez.component.EntityLocatorTest$A when a function for type already exists." );
  }

  static class A
  {
  }

  private static class TestEntityLocator
    extends AbstractEntityLocator
  {
  }
}
