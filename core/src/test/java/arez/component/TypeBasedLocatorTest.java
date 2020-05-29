package arez.component;

import arez.AbstractTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "SuspiciousMethodCalls" )
public final class TypeBasedLocatorTest
  extends AbstractTest
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

    assertInvariantFailure( () -> locator.registerLookup( A.class, i -> new A() ),
                            "Arez-0188: Attempting to register lookup function for type class arez.component.TypeBasedLocatorTest$A when a function for type already exists." );

  }

  static class A
  {
  }
}
