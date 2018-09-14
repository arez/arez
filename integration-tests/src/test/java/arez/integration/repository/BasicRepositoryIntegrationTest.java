package arez.integration.repository;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.Flags;
import arez.Procedure;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import arez.annotations.Repository;
import arez.component.ComponentObservable;
import arez.component.NoResultException;
import arez.component.NoSuchEntityException;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class BasicRepositoryIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  @Repository
  static abstract class TestComponent
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
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final BasicRepositoryIntegrationTest_TestComponentRepository repository =
      BasicRepositoryIntegrationTest_TestComponentRepository.newRepository();
    final TestComponent component1 = repository.create( 1, "S1" );
    final TestComponent component2 = repository.create( 2, "S2" );

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final AtomicInteger callCount = new AtomicInteger();

    context.observer( "ComponentCount",
                      () -> {
                        recorder.mark( "ComponentCount", repository.findAll().size() );
                        callCount.incrementAndGet();
                      } );

    assertEquals( callCount.get(), 1 );

    final TestComponent component3 = repository.create( 3, "S3" );

    assertEquals( callCount.get(), 2 );

    context.action( "Value Update", () -> component3.setValue( "S3b" ) );

    assertEquals( callCount.get(), 2 );

    repository.destroy( component1 );

    assertEquals( callCount.get(), 3 );

    repository.destroy( component2 );

    assertEquals( callCount.get(), 4 );

    assertMatchesFixture( recorder );

    final TestComponent component4 = repository.create( 4, "S4" );

    assertEquals( callCount.get(), 5 );

    final Procedure executable13 = () -> assertEquals( repository.findById( 4 ), component4 );
    context.action( executable13, Flags.READ_ONLY );
    final Procedure executable12 = () -> assertEquals( repository.findById( 3 ), component3 );
    context.action( executable12, Flags.READ_ONLY );
    final Procedure executable11 = () -> assertEquals( repository.getById( 4 ), component4 );
    context.action( executable11, Flags.READ_ONLY );
    final Procedure executable10 = () -> assertEquals( repository.getById( 3 ), component3 );
    context.action( executable10, Flags.READ_ONLY );
    final Procedure executable9 = () -> assertEquals( repository.findById( 2 ), null );
    context.action( executable9, Flags.READ_ONLY );
    final Procedure executable8 = () -> assertEquals( repository.findByQuery( c -> c.getId() == 3 ), component3 );
    context.action( executable8, Flags.READ_ONLY );
    final Procedure executable7 = () -> assertEquals( repository.getByQuery( c -> c.getId() == 3 ), component3 );
    context.action( executable7, Flags.READ_ONLY );
    final Procedure executable6 = () -> assertEquals( repository.findAllByQuery( c -> c.getId() == 3 ).size(), 1 );
    context.action( executable6, Flags.READ_ONLY );
    final Procedure executable5 = () -> assertEquals( repository.findAllByQuery( c -> c.getId() >= 3 ).size(), 2 );
    context.action( executable5, Flags.READ_ONLY );
    final Procedure executable4 = () -> assertEquals( repository.findAll().size(), 2 );
    context.action( executable4, Flags.READ_ONLY );
    final Procedure executable3 = () -> assertEquals( repository.contains( component1 ), false );
    context.action( executable3, Flags.READ_ONLY );
    final Procedure executable2 = () -> assertEquals( repository.contains( component2 ), false );
    context.action( executable2, Flags.READ_ONLY );
    final Procedure executable1 = () -> assertEquals( repository.contains( component3 ), true );
    context.action( executable1, Flags.READ_ONLY );
    final Procedure executable = () -> assertEquals( repository.contains( component4 ), true );
    context.action( executable, Flags.READ_ONLY );

    // getByQuery should throw an exception if there is no entity that matches query
    assertThrows( NoResultException.class,
                  () -> context.action( () -> repository.getByQuery( c -> false ) ) );

    //getById should throw an exception if not found
    final NoSuchEntityException exception =
      expectThrows( NoSuchEntityException.class, () -> context.action( () -> repository.getById( 2 ) ) );
    assertEquals( exception.getId(), 2 );
  }

  @Test
  public void canNotQueryRepositoryWithoutTransaction()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final BasicRepositoryIntegrationTest_TestComponentRepository repository =
      BasicRepositoryIntegrationTest_TestComponentRepository.newRepository();

    // component1 has value that will sort after component2 to test sorting below
    final TestComponent component1 = repository.create( 1, "B" );
    final TestComponent component2 = repository.create( 2, "A" );

    assertThrows( () -> repository.findById( 1 ) );
    assertThrows( () -> repository.findByQuery( c -> false ) );
    assertThrows( () -> repository.findAllByQuery( c -> false ) );
    assertThrows( repository::findAll );
    assertThrows( () -> repository.contains( component1 ) );

    final Procedure executable7 = () -> assertEquals( repository.findById( 1 ), component1 );
    context.action( executable7, Flags.READ_ONLY );
    final Procedure executable6 = () -> assertEquals( repository.findById( 0 ), null );
    context.action( executable6, Flags.READ_ONLY );
    final Procedure executable5 = () -> assertEquals( repository.findByQuery( c -> c.getId() == 1 ), component1 );
    context.action( executable5, Flags.READ_ONLY );
    final Procedure executable4 = () -> assertEquals( repository.findByQuery( c -> false ), null );
    context.action( executable4, Flags.READ_ONLY );
    final Procedure executable3 = () ->
      assertEquals( repository.findAllByQuery( c -> true,
                                               Comparator.comparing( TestComponent::getValue ) ).get( 0 ),
                    component2 );
    context.action( executable3, Flags.READ_ONLY );
    final Procedure executable2 = () -> assertEquals( repository.findAllByQuery( c -> false ).size(), 0 );
    context.action( executable2, Flags.READ_ONLY );
    final Procedure executable1 = () -> assertEquals( repository.findAll().size(), 2 );
    context.action( executable1, Flags.READ_ONLY );
    final Procedure executable = () -> assertEquals( repository.contains( component1 ), true );
    context.action( executable, Flags.READ_ONLY );
  }

  @Test
  public void disposeWillRemoveFromRepository()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final BasicRepositoryIntegrationTest_TestComponentRepository repository =
      BasicRepositoryIntegrationTest_TestComponentRepository.newRepository();

    // component1 has value that will sort after component2 to test sorting below
    final TestComponent component1 = repository.create( 1, "B" );
    final TestComponent component2 = repository.create( 2, "A" );

    final AtomicInteger callCount = new AtomicInteger();

    observer( () -> {
      repository.findAll().forEach( ComponentObservable::observe );
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    final Procedure executable1 = () -> assertEquals( repository.findAll().size(), 2 );
    context.action( executable1, Flags.READ_ONLY );

    assertEquals( callCount.get(), 1 );

    Disposable.dispose( component1 );

    // Dispose recreated the list - huzzah
    assertEquals( callCount.get(), 2 );

    final Procedure executable = () -> assertEquals( repository.findAll().size(), 1 );
    context.action( executable, Flags.READ_ONLY );

    assertEquals( callCount.get(), 2 );

    repository.destroy( component2 );

    // Destroy recreated the list - huzzah
    assertEquals( callCount.get(), 3 );
  }
}
