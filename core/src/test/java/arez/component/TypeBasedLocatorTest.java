package arez.component;

import arez.AbstractArezTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "SuspiciousMethodCalls" )
public class TypeBasedLocatorTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final TypeBasedLocator locator = new TypeBasedLocator();

    assertNull( locator.findById( A.class, 23 ) );

    final HashMap<Integer, A> entities = new HashMap<>();

    locator.registerLookup( A.class, entities::get );

    assertNull( locator.findById( A.class, 23 ) );

    final A entity = new A();
    entities.put( 23, entity );

    assertEquals( locator.findById( A.class, 23 ), entity );
  }

  @Test
  public void registerLookup_duplicate()
  {
    final TypeBasedLocator locator = new TypeBasedLocator();

    locator.registerLookup( A.class, i -> new A() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> locator.registerLookup( A.class, i -> new A() ) );
    assertEquals( exception.getMessage(),
                  "Arez-0188: Attempting to register lookup function for type class arez.component.TypeBasedLocatorTest$A when a function for type already exists." );
  }

  static class A
  {
  }
}
