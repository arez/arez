package arez.entity;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.component.Identifiable;
import arez.integration.util.SpyEventRecorder;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.json.JSONException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class HasManyInverseRelationshipTest
  extends AbstractArezEntityTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    static TestComponent create()
    {
      return new HasManyInverseRelationshipTest_Arez_TestComponent();
    }
  }

  @Test
  public void basicOperation()
    throws IOException, JSONException
  {
    final HasManyInverseRelationship<TestComponent> relationship = HasManyInverseRelationship.create();

    final TestComponent component1 = TestComponent.create();
    final TestComponent component2 = TestComponent.create();
    final TestComponent component3 = TestComponent.create();

    final int componentId1 = Objects.requireNonNull( Identifiable.getArezId( component1 ) );
    final int componentId2 = Objects.requireNonNull( Identifiable.getArezId( component2 ) );
    final int componentId3 = Objects.requireNonNull( Identifiable.getArezId( component3 ) );

    final HashSet<Integer> expected = new HashSet<>();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final AtomicInteger callCount = new AtomicInteger();
    autorun( () -> {
      callCount.incrementAndGet();
      if ( Disposable.isNotDisposed( relationship ) )
      {
        assertEquals( extractEntityIds( relationship ), expected );
      }
    } );

    assertEquals( callCount.get(), 1 );

    expected.add( componentId1 );

    safeAction( () -> relationship.link( component1 ) );

    assertEquals( callCount.get(), 2 );

    expected.clear();

    safeAction( () -> relationship.delink( component1 ) );

    assertEquals( callCount.get(), 3 );

    expected.add( componentId1 );
    expected.add( componentId2 );
    expected.add( componentId3 );

    safeAction( () -> {
      relationship.link( component1 );
      relationship.link( component2 );
      relationship.link( component3 );
    } );

    assertEquals( callCount.get(), 4 );

    expected.remove( componentId2 );

    Disposable.dispose( component2 );

    assertEquals( callCount.get(), 5 );

    Disposable.dispose( relationship );

    assertEquals( callCount.get(), 6 );

    assertEquals( Disposable.isDisposed( component1 ), false );
    assertEquals( Disposable.isDisposed( component2 ), true );
    assertEquals( Disposable.isDisposed( component3 ), false );

    assertMatchesFixture( recorder );
  }

  @Nonnull
  private Set<Integer> extractEntityIds( @Nonnull final HasManyInverseRelationship<TestComponent> relationship )
  {
    return relationship
      .getEntities()
      .stream()
      .map( Identifiable::<Integer>getArezId )
      .sorted()
      .collect( Collectors.toSet() );
  }
}
