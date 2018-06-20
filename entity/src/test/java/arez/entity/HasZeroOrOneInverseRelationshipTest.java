package arez.entity;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.component.Identifiable;
import arez.integration.util.SpyEventRecorder;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class HasZeroOrOneInverseRelationshipTest
  extends AbstractArezEntityTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    static TestComponent create()
    {
      return new HasZeroOrOneInverseRelationshipTest_Arez_TestComponent();
    }
  }

  @Test
  public void basicOperation()
    throws IOException, JSONException
  {
    final HasZeroOrOneInverseRelationship<TestComponent> relationship = HasZeroOrOneInverseRelationship.create();

    final TestComponent component1 = TestComponent.create();
    final TestComponent component2 = TestComponent.create();

    final int componentId1 = Objects.requireNonNull( Identifiable.getArezId( component1 ) );
    final int componentId2 = Objects.requireNonNull( Identifiable.getArezId( component2 ) );

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final AtomicReference<Integer> expectedId = new AtomicReference<>();

    final AtomicInteger callCount = new AtomicInteger();
    autorun( () -> {
      callCount.incrementAndGet();
      if ( relationship.hasReference() )
      {
        final TestComponent reference = relationship.getReference();
        assertNotNull( reference );
        assertEquals( Identifiable.getArezId( reference ), expectedId.get() );
      }
      else
      {
        assertEquals( expectedId.get(), null );
      }
    } );

    assertEquals( callCount.get(), 1 );
    safeAction( () -> assertFalse( relationship.hasReference() ) );

    expectedId.set( componentId1 );
    safeAction( () -> relationship.link( component1 ) );

    assertEquals( callCount.get(), 2 );
    safeAction( () -> assertTrue( relationship.hasReference() ) );
    safeAction( () -> assertEquals( relationship.getReference(), component1 ) );

    expectedId.set( componentId2 );
    safeAction( () -> relationship.link( component2 ) );

    assertEquals( callCount.get(), 3 );
    safeAction( () -> assertTrue( relationship.hasReference() ) );
    safeAction( () -> assertEquals( relationship.getReference(), component2 ) );

    expectedId.set( null );
    safeAction( relationship::delink );

    assertEquals( callCount.get(), 4 );
    safeAction( () -> assertFalse( relationship.hasReference() ) );

    assertMatchesFixture( recorder );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent2
  {
    static TestComponent2 create()
    {
      return new HasZeroOrOneInverseRelationshipTest_Arez_TestComponent2();
    }
  }

  @Test
  public void disposedEntityUnlinks()
    throws Exception
  {
    final HasOneInverseRelationship<TestComponent2> relationship = HasOneInverseRelationship.create();

    final TestComponent2 component1 = TestComponent2.create();

    final AtomicReference<Integer> expectedId = new AtomicReference<>();
    expectedId.set( Objects.requireNonNull( Identifiable.getArezId( component1 ) ) );
    safeAction( () -> relationship.link( component1 ) );

    final AtomicInteger callCount = new AtomicInteger();
    autorun( () -> {
      callCount.incrementAndGet();
      if ( relationship.hasReference() )
      {
        assertEquals( Identifiable.getArezId( relationship.getReference() ), expectedId.get() );
      }
      else
      {
        assertEquals( expectedId.get(), null );
      }
    } );

    assertEquals( callCount.get(), 1 );
    safeAction( () -> assertTrue( relationship.hasReference() ) );
    safeAction( () -> assertEquals( relationship.getReference(), component1 ) );

    expectedId.set( null );

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();
    Disposable.dispose( component1 );

    assertEquals( callCount.get(), 2 );
    safeAction( () -> assertFalse( relationship.hasReference() ) );

    assertMatchesFixture( recorder );
  }
}
