package arez.integration.repository;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import arez.annotations.Repository;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposeEntityInRepositoryIntegrationTest
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
    final DisposeEntityInRepositoryIntegrationTest_TestComponentRepository repository =
      DisposeEntityInRepositoryIntegrationTest_TestComponentRepository.newRepository();
    final TestComponent component1 = repository.create( 1, "S1" );
    final TestComponent component2 = repository.create( 2, "S2" );

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    Disposable.dispose( component1 );

    assertMatchesFixture( recorder );

    assertFalse( Disposable.isDisposed( repository ) );
    assertTrue( Disposable.isDisposed( component1 ) );
    assertFalse( Disposable.isDisposed( component2 ) );

    safeAction( () -> assertEquals( repository.findById( 1 ), null ) );
    safeAction( () -> assertEquals( repository.findById( 2 ), component2 ) );
  }
}
