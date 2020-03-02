package arez.integration.sting;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Repository;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import sting.Injector;
import static org.testng.Assert.*;

public class StingRepositoryIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  @Repository( sting = Feature.ENABLE )
  static abstract class TestComponent
  {
    @Observable
    @Nonnull
    abstract String getValue();

    abstract void setValue( @Nonnull String value );
  }

  @Injector( includes = StingRepositoryIntegrationTest_TestComponentRepository.class )
  interface MyInjector
  {
    StingRepositoryIntegrationTest_TestComponentRepository repository();
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final MyInjector injector = new StingRepositoryIntegrationTest_Sting_MyInjector();
    injector.repository();
    injector.repository();
    final StingRepositoryIntegrationTest_TestComponentRepository repository = injector.repository();
    final TestComponent component1 = repository.create( "ABCDEF" );
    final TestComponent component2 = repository.create( "CDEFGH" );
    final TestComponent component3 = repository.create( "EFGHIJ" );

    final ArezContext context = Arez.context();

    context.action( () -> assertEquals( repository.findAll().size(), 3 ), ActionFlags.READ_ONLY );
    context.action( () -> assertTrue( repository.findAll().contains( component1 ) ), ActionFlags.READ_ONLY );
    context.action( () -> assertTrue( repository.findAll().contains( component2 ) ), ActionFlags.READ_ONLY );
    context.action( () -> assertTrue( repository.findAll().contains( component3 ) ), ActionFlags.READ_ONLY );
  }
}
