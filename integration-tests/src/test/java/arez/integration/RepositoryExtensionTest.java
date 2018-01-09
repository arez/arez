package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Repository;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RepositoryExtensionTest
  extends AbstractIntegrationTest
{
  interface TestComponentRepositoryExtension
  {
    @Nonnull
    default List<TestComponent> findAllLike( @Nonnull final String pattern )
    {
      return self().findAllByQuery( c -> c.getValue().contains( pattern ) );
    }

    RepositoryExtensionTest_TestComponentRepository self();
  }

  @ArezComponent
  @Repository( extensions = TestComponentRepositoryExtension.class )
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

  @Test
  public void integrationTest()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final RepositoryExtensionTest_TestComponentRepository repository =
      RepositoryExtensionTest_TestComponentRepository.newRepository();
    final TestComponent component1 = repository.create( "ABCDEF" );
    final TestComponent component2 = repository.create( "CDEFGH" );
    final TestComponent component3 = repository.create( "EFGHIJ" );

    context.action( false, () -> assertEquals( repository.findAllLike( "AB" ).size(), 1 ) );
    context.action( false, () -> assertEquals( repository.findAllLike( "AB" ).contains( component1 ), true ) );
    context.action( false, () -> assertEquals( repository.findAllLike( "CD" ).size(), 2 ) );
    context.action( false, () -> assertEquals( repository.findAllLike( "CD" ).contains( component1 ), true ) );
    context.action( false, () -> assertEquals( repository.findAllLike( "CD" ).contains( component2 ), true ) );
    context.action( false, () -> assertEquals( repository.findAllLike( "EF" ).size(), 3 ) );
    context.action( false, () -> assertEquals( repository.findAllLike( "EF" ).contains( component1 ), true ) );
    context.action( false, () -> assertEquals( repository.findAllLike( "EF" ).contains( component2 ), true ) );
    context.action( false, () -> assertEquals( repository.findAllLike( "EF" ).contains( component3 ), true ) );
  }
}
