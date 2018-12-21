package arez.integration.dagger;

import arez.Arez;
import arez.ArezContext;
import arez.Flags;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Repository;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DaggerRepositoryIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  @Repository( dagger = Feature.ENABLE )
  static abstract class TestComponent
  {
    private String _value;

    TestComponent( final String value )
    {
      _value = value;
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

  @Singleton
  @Component( modules = DaggerRepositoryIntegrationTest_TestComponentRepositoryDaggerModule.class )
  interface TestDaggerComponent
  {
    DaggerRepositoryIntegrationTest_TestComponentRepository repository();
  }

  @Test
  public void useDaggerComponentToGetAccessToRepository()
    throws Throwable
  {
    final TestDaggerComponent daggerComponent = DaggerDaggerRepositoryIntegrationTest_TestDaggerComponent.create();
    daggerComponent.repository();
    daggerComponent.repository();
    final DaggerRepositoryIntegrationTest_TestComponentRepository repository = daggerComponent.repository();
    final TestComponent component1 = repository.create( "ABCDEF" );
    final TestComponent component2 = repository.create( "CDEFGH" );
    final TestComponent component3 = repository.create( "EFGHIJ" );

    final ArezContext context = Arez.context();

    context.action( () -> assertEquals( repository.findAll().size(), 3 ), Flags.READ_ONLY );
    context.action( () -> assertTrue( repository.findAll().contains( component1 ) ), Flags.READ_ONLY );
    context.action( () -> assertTrue( repository.findAll().contains( component2 ) ), Flags.READ_ONLY );
    context.action( () -> assertTrue( repository.findAll().contains( component3 ) ), Flags.READ_ONLY );
  }
}
