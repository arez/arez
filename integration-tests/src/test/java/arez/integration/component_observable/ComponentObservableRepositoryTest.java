package arez.integration.component_observable;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;
import arez.annotations.Repository;
import arez.integration.AbstractArezIntegrationTest;
import arez.spy.ObservableInfo;
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentObservableRepositoryTest
  extends AbstractArezIntegrationTest
{
  @Repository
  @ArezComponent
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

    final ComponentObservableRepositoryTest_TestComponentRepository repository =
      ComponentObservableRepositoryTest_TestComponentRepository.newRepository();

    // component1 has value that will sort after component2 to test sorting below
    final ComponentObservableRepositoryTest.TestComponent component1 = repository.create( 1, "B" );

    final Observer observer = context.autorun( () -> repository.findById( 1 ) );

    // When the entity is found then only observe the entity
    {
      final List<ObservableInfo> dependencies = Arez.context().getSpy().getDependencies( observer );
      assertEquals( dependencies.size(), 1 );
      assertEquals( dependencies.get( 0 ).getName(), "TestComponent.1.isDisposed" );
    }

    Disposable.dispose( component1 );

    // when the entity is not found then observe the set ... in case it is added in the future
    {
      final List<ObservableInfo> dependencies = Arez.context().getSpy().getDependencies( observer );
      assertEquals( dependencies.size(), 1 );
      assertEquals( dependencies.get( 0 ).getName(),
                    "ComponentObservableRepositoryTest_TestComponentRepository.entities" );
    }
  }
}
