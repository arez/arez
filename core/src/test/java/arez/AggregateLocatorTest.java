package arez;

import arez.component.TypeBasedLocator;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "SuspiciousMethodCalls" )
public class AggregateLocatorTest
  extends AbstractTest
{
  @Test
  public void registerThenDeregister()
  {
    final TypeBasedLocator locator1 = new TypeBasedLocator();

    final AggregateLocator locator = new AggregateLocator();
    final Disposable disposable = locator.registerLocator( locator1 );

    assertTrue( locator.getLocators().contains( locator1 ) );
    assertFalse( disposable.isDisposed() );

    disposable.dispose();

    assertFalse( locator.getLocators().contains( locator1 ) );
    assertTrue( disposable.isDisposed() );

    // Should be no-op
    disposable.dispose();
  }

  @Test
  public void aggregateSingleLocator()
  {
    final HashMap<Integer, A> entities1 = new HashMap<>();
    final TypeBasedLocator locator1 = new TypeBasedLocator();
    locator1.registerLookup( A.class, entities1::get );

    final AggregateLocator locator = new AggregateLocator();
    locator.registerLocator( locator1 );

    assertNull( locator.findById( A.class, 23 ) );

    final A entity = new A();
    entities1.put( 23, entity );

    assertEquals( locator.findById( A.class, 23 ), entity );
  }

  @Test
  public void aggregateMultipleLocator_disjoint_types()
  {
    final HashMap<Integer, A> entities1 = new HashMap<>();
    final TypeBasedLocator locator1 = new TypeBasedLocator();
    locator1.registerLookup( A.class, entities1::get );

    final HashMap<Integer, B> entities2 = new HashMap<>();
    final TypeBasedLocator locator2 = new TypeBasedLocator();
    locator2.registerLookup( B.class, entities2::get );

    final AggregateLocator locator = new AggregateLocator();
    locator.registerLocator( locator1 );
    locator.registerLocator( locator2 );

    assertNull( locator.findById( A.class, 23 ) );
    assertNull( locator.findById( B.class, 42 ) );

    final A entity1 = new A();
    entities1.put( 23, entity1 );

    assertEquals( locator.findById( A.class, 23 ), entity1 );
    assertNull( locator.findById( B.class, 42 ) );

    final B entity2 = new B();
    entities2.put( 42, entity2 );

    assertEquals( locator.findById( A.class, 23 ), entity1 );
    assertEquals( locator.findById( B.class, 42 ), entity2 );
  }

  @Test
  public void aggregateMultipleLocator_overlapping_types()
  {
    final HashMap<Integer, A> entities1 = new HashMap<>();
    final TypeBasedLocator locator1 = new TypeBasedLocator();
    locator1.registerLookup( A.class, entities1::get );

    final HashMap<Integer, A> entities2 = new HashMap<>();
    final TypeBasedLocator locator2 = new TypeBasedLocator();
    locator2.registerLookup( A.class, entities2::get );

    final AggregateLocator locator = new AggregateLocator();
    locator.registerLocator( locator1 );
    locator.registerLocator( locator2 );

    assertNull( locator.findById( A.class, 23 ) );

    final A entity1 = new A();
    entities2.put( 23, entity1 );

    assertEquals( locator.findById( A.class, 23 ), entity1 );
    final A entity2 = new A();
    entities1.put( 23, entity2 );

    assertEquals( locator.findById( A.class, 23 ), entity2 );
  }

  @Test
  public void registerLookup_duplicate()
  {
    final TypeBasedLocator locator1 = new TypeBasedLocator();

    final AggregateLocator locator = new AggregateLocator();
    locator.registerLocator( locator1 );

    assertInvariantFailure( () -> locator.registerLocator( locator1 ),
                            "Arez-0189: Attempting to register locator " + locator1 +
                            " when the Locator is already present." );
  }

  @Test
  public void deregisterButMissing()
  {
    final TypeBasedLocator locator1 = new TypeBasedLocator();

    final AggregateLocator locator = new AggregateLocator();
    final Disposable disposable = locator.registerLocator( locator1 );

    locator.getLocators().clear();

    assertInvariantFailure( disposable::dispose,
                            "Arez-0190: Attempting to de-register locator " + locator1 +
                            " but the Locator is not present in list." );
  }

  private static class A
  {
  }

  private static class B
  {
  }
}
