package arez.integration.repository;

import arez.Arez;
import arez.ArezContext;
import arez.Flags;
import arez.Procedure;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Repository;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import javax.annotation.Nonnull;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RepositoryInjectIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  @Repository( inject = Feature.ENABLE )
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

    final Procedure executable3 = () -> assertEquals( repository.findAll().size(), 3 );
    context.action( executable3, Flags.READ_ONLY );
    final Procedure executable2 = () -> assertEquals( repository.findAll().contains( component1 ), true );
    context.action( executable2, Flags.READ_ONLY );
    final Procedure executable1 = () -> assertEquals( repository.findAll().contains( component2 ), true );
    context.action( executable1, Flags.READ_ONLY );
    final Procedure executable = () -> assertEquals( repository.findAll().contains( component3 ), true );
    context.action( executable, Flags.READ_ONLY );
  }
}
