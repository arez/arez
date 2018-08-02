package arez.component;

import arez.AbstractArezTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "SuspiciousMethodCalls" )
public class AggregateEntityLocatorTest
  extends AbstractArezTest
{
  @Test
  public void aggregateSingleEntityLocator()
  {
    final HashMap<Integer, A> entities1 = new HashMap<>();
    final TestEntityLocator locator1 = new TestEntityLocator();
    locator1.registerLookup( A.class, entities1::get );

    final AggregateEntityLocator locator = new AggregateEntityLocator( locator1 );

    assertNull( locator.findById( A.class, 23 ) );
    assertEquals( expectThrows( NoSuchEntityException.class, () -> locator.getById( A.class, 23 ) ).getId(), 23 );

    final A entity = new A();
    entities1.put( 23, entity );

    assertEquals( locator.findById( A.class, 23 ), entity );
    assertEquals( locator.getById( A.class, 23 ), entity );
  }

  @Test
  public void aggregateMultipleEntityLocator_disjoint_types()
  {
    final HashMap<Integer, A> entities1 = new HashMap<>();
    final TestEntityLocator locator1 = new TestEntityLocator();
    locator1.registerLookup( A.class, entities1::get );

    final HashMap<Integer, B> entities2 = new HashMap<>();
    final TestEntityLocator locator2 = new TestEntityLocator();
    locator2.registerLookup( B.class, entities2::get );

    final AggregateEntityLocator locator = new AggregateEntityLocator( locator1, locator2 );

    assertNull( locator.findById( A.class, 23 ) );
    assertEquals( expectThrows( NoSuchEntityException.class, () -> locator.getById( A.class, 23 ) ).getId(), 23 );
    assertNull( locator.findById( B.class, 42 ) );
    assertEquals( expectThrows( NoSuchEntityException.class, () -> locator.getById( B.class, 42 ) ).getId(), 42 );

    final A entity1 = new A();
    entities1.put( 23, entity1 );

    assertEquals( locator.findById( A.class, 23 ), entity1 );
    assertEquals( locator.getById( A.class, 23 ), entity1 );
    assertNull( locator.findById( B.class, 42 ) );
    assertEquals( expectThrows( NoSuchEntityException.class, () -> locator.getById( B.class, 42 ) ).getId(), 42 );

    final B entity2 = new B();
    entities2.put( 42, entity2 );

    assertEquals( locator.findById( A.class, 23 ), entity1 );
    assertEquals( locator.getById( A.class, 23 ), entity1 );
    assertEquals( locator.findById( B.class, 42 ), entity2 );
    assertEquals( locator.getById( B.class, 42 ), entity2 );
  }

  @Test
  public void aggregateMultipleEntityLocator_overlapping_types()
  {
    final HashMap<Integer, A> entities1 = new HashMap<>();
    final TestEntityLocator locator1 = new TestEntityLocator();
    locator1.registerLookup( A.class, entities1::get );

    final HashMap<Integer, A> entities2 = new HashMap<>();
    final TestEntityLocator locator2 = new TestEntityLocator();
    locator2.registerLookup( A.class, entities2::get );

    final AggregateEntityLocator locator = new AggregateEntityLocator( locator1, locator2 );

    assertNull( locator.findById( A.class, 23 ) );
    assertEquals( expectThrows( NoSuchEntityException.class, () -> locator.getById( A.class, 23 ) ).getId(), 23 );

    final A entity1 = new A();
    entities2.put( 23, entity1 );

    assertEquals( locator.findById( A.class, 23 ), entity1 );
    assertEquals( locator.getById( A.class, 23 ), entity1 );
    final A entity2 = new A();
    entities1.put( 23, entity2 );

    assertEquals( locator.findById( A.class, 23 ), entity2 );
    assertEquals( locator.getById( A.class, 23 ), entity2 );
  }

  @Test
  public void registerLookup_duplicate()
  {
    final TestEntityLocator locator1 = new TestEntityLocator();

    final AggregateEntityLocator locator = new AggregateEntityLocator( locator1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> locator.registerEntityLocator( locator1 ) );
    assertEquals( exception.getMessage(),
                  "Arez-0189: Attempting to register entityLocator " + locator1 +
                  " when the EntityLocator is already present." );
  }

  static class A
  {
  }

  static class B
  {
  }

  private static class TestEntityLocator
    extends AbstractEntityLocator
  {
  }
}
