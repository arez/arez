package org.realityforge.arez.integration;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.Repository;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class BasicRepositoryIntegrationTest
  extends AbstractIntegrationTest
{
  @ArezComponent
  @Repository
  static class TestComponent
  {
    private final int _id;
    private String _value;

    TestComponent( final int id, final String value )
    {
      _id = id;
      _value = value;
    }

    @ComponentId
    final int getId()
    {
      return _id;
    }

    @Observable
    String getValue()
    {
      return _value;
    }

    void setValue( final String value )
    {
      _value = value;
    }
  }

  @Test
  public void integrationTest()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final BasicRepositoryIntegrationTest$TestComponentRepository repository =
      BasicRepositoryIntegrationTest$TestComponentRepository.newRepository();
    final TestComponent component1 = repository.create( 1, "S1" );
    final TestComponent component2 = repository.create( 2, "S2" );

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final AtomicInteger callCount = new AtomicInteger();

    context.autorun( "ComponentCount",
                     () -> {
                       record( recorder, "ComponentCount", repository.findAll().size() );
                       callCount.incrementAndGet();
                     } );

    assertEquals( callCount.get(), 1 );

    final TestComponent component3 = repository.create( 3, "S3" );

    assertEquals( callCount.get(), 2 );

    context.action( "Value Update", true, () -> component3.setValue( "S3b" ) );

    assertEquals( callCount.get(), 2 );

    repository.destroy( component1 );

    assertEquals( callCount.get(), 3 );

    repository.destroy( component2 );

    assertEquals( callCount.get(), 4 );

    assertEqualsFixture( recorder.eventsAsString() );

    final TestComponent component4 = repository.create( 4, "S4" );

    assertEquals( callCount.get(), 5 );

    context.action( false, () -> assertEquals( repository.findById( 4 ), component4 ) );
    context.action( false, () -> assertEquals( repository.findById( 3 ), component3 ) );
    context.action( false, () -> assertEquals( repository.findById( 2 ), null ) );
    context.action( false, () -> assertEquals( repository.findByQuery( c -> c.getId() == 3 ), component3 ) );
    context.action( false, () -> assertEquals( repository.findAllByQuery( c -> c.getId() == 3 ).size(), 1 ) );
    context.action( false, () -> assertEquals( repository.findAllByQuery( c -> c.getId() >= 3 ).size(), 2 ) );
    context.action( false, () -> assertEquals( repository.findAll().size(), 2 ) );
    context.action( false, () -> assertEquals( repository.contains( component1 ), false ) );
    context.action( false, () -> assertEquals( repository.contains( component2 ), false ) );
    context.action( false, () -> assertEquals( repository.contains( component3 ), true ) );
    context.action( false, () -> assertEquals( repository.contains( component4 ), true ) );
  }

  @Test
  public void canNotQueryRepositoryWithoutTransaction()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final BasicRepositoryIntegrationTest$TestComponentRepository repository =
      BasicRepositoryIntegrationTest$TestComponentRepository.newRepository();

    // component1 has value that will sort after component2 to test sorting below
    final TestComponent component1 = repository.create( 1, "B" );
    final TestComponent component2 = repository.create( 2, "A" );

    assertThrows( () -> repository.findById( 1 ) );
    assertThrows( () -> repository.findByQuery( c -> false ) );
    assertThrows( () -> repository.findAllByQuery( c -> false ) );
    assertThrows( repository::findAll );
    assertThrows( () -> repository.contains( component1 ) );

    context.action( false, () -> assertEquals( repository.findById( 1 ), component1 ) );
    context.action( false, () -> assertEquals( repository.findById( 0 ), null ) );
    context.action( false, () -> assertEquals( repository.findByQuery( c -> c.getId() == 1 ), component1 ) );
    context.action( false, () -> assertEquals( repository.findByQuery( c -> false ), null ) );
    context.action( false, () ->
      assertEquals( repository.findAllByQuery( c -> true,
                                               Comparator.comparing( TestComponent::getValue ) ).get( 0 ),
                    component2 ) );
    context.action( false, () -> assertEquals( repository.findAllByQuery( c -> false ).size(), 0 ) );
    context.action( false, () -> assertEquals( repository.findAll().size(), 2 ) );
    context.action( false, () -> assertEquals( repository.contains( component1 ), true ) );
  }
}
