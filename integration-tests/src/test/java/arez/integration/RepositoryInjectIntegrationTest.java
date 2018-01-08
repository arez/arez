package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Injectible;
import arez.annotations.Observable;
import arez.annotations.Repository;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import javax.annotation.Nonnull;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RepositoryInjectIntegrationTest
{
  @ArezComponent
  @Repository( inject = Injectible.TRUE )
  static class TestComponent
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

  @Module
  interface TestComponentDaggerModule
  {
    @Nonnull
    @Singleton
    @Provides
    static RepositoryInjectIntegrationTest_TestComponentRepository repository( final Arez_RepositoryInjectIntegrationTest_TestComponentRepository repository )
    {
      return repository;
    }
  }

  @Singleton
  @Component( modules = TestComponentDaggerModule.class )
  interface TestDaggerComponent
  {
    RepositoryInjectIntegrationTest_TestComponentRepository repository();
  }

  @Test
  public void useDaggerComponentToGetAccessToRepository()
    throws Throwable
  {
    final TestDaggerComponent daggerComponent = DaggerRepositoryInjectIntegrationTest_TestDaggerComponent.create();
    daggerComponent.repository();
    daggerComponent.repository();
    final RepositoryInjectIntegrationTest_TestComponentRepository repository = daggerComponent.repository();
    final TestComponent component1 = repository.create( "ABCDEF" );
    final TestComponent component2 = repository.create( "CDEFGH" );
    final TestComponent component3 = repository.create( "EFGHIJ" );

    final ArezContext context = Arez.context();

    context.action( false, () -> assertEquals( repository.findAll().size(), 3 ) );
    context.action( false, () -> assertEquals( repository.findAll().contains( component1 ), true ) );
    context.action( false, () -> assertEquals( repository.findAll().contains( component2 ), true ) );
    context.action( false, () -> assertEquals( repository.findAll().contains( component3 ), true ) );
  }
}
