package arez.integration.repository;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Repository;
import arez.component.NoResultException;
import arez.component.NoSuchEntityException;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ReadAndWriteOutsideTransactionRepositoryIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( defaultReadOutsideTransaction = Feature.ENABLE, defaultWriteOutsideTransaction = Feature.ENABLE )
  @Repository( sting = Feature.DISABLE, dagger = Feature.DISABLE )
  static abstract class TestComponent
  {
    private final int _id;

    TestComponent( final int id )
    {
      _id = id;
    }

    @ComponentId
    final int getId()
    {
      return _id;
    }

    @Observable
    abstract String getValue();

    abstract void setValue( String value );
  }

  @Test
  public void scenario()
    throws Throwable
  {

    final ReadAndWriteOutsideTransactionRepositoryIntegrationTest_TestComponentRepository repository =
      ReadAndWriteOutsideTransactionRepositoryIntegrationTest_TestComponentRepository.newRepository();
    final TestComponent component1 = repository.create( 1 );
    final TestComponent component2 = repository.create( 2 );
    final TestComponent component3 = repository.create( 3 );

    component3.setValue( "S3b" );

    repository.destroy( component1 );

    repository.destroy( component2 );

    final TestComponent component4 = repository.create( 4 );

    assertEquals( repository.findById( 4 ), component4 );
    assertEquals( repository.findById( 3 ), component3 );
    assertEquals( repository.getById( 4 ), component4 );
    assertEquals( repository.getById( 3 ), component3 );
    assertNull( repository.findById( 2 ) );
    assertEquals( repository.findByQuery( c -> c.getId() == 3 ), component3 );
    assertEquals( repository.getByQuery( c -> c.getId() == 3 ), component3 );
    assertEquals( repository.findAllByQuery( c -> c.getId() == 3 ).size(), 1 );
    assertEquals( repository.findAllByQuery( c -> c.getId() >= 3 ).size(), 2 );
    assertEquals( repository.findAll().size(), 2 );
    assertFalse( repository.contains( component1 ) );
    assertFalse( repository.contains( component2 ) );
    assertTrue( repository.contains( component3 ) );
    assertTrue( repository.contains( component4 ) );

    // getByQuery should throw an exception if there is no entity that matches query
    assertThrows( NoResultException.class, () -> repository.getByQuery( c -> false ) );

    //getById should throw an exception if not found
    final NoSuchEntityException exception = expectThrows( NoSuchEntityException.class, () -> repository.getById( 2 ) );
    assertEquals( exception.getId(), 2 );
  }
}
