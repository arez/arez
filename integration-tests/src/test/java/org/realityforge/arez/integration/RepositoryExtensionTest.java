package org.realityforge.arez.integration;

import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.Repository;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RepositoryExtensionTest
  extends AbstractIntegrationTest
{
  interface TestComponentRepositoryExtension
    extends RepositoryExtensionTest_TestComponentBaseRepositoryExtension
  {
    @Nonnull
    default List<TestComponent> findAllLike( @Nonnull final String pattern )
    {
      return self().findAllByQuery( c -> c.getValue().contains( pattern ) );
    }
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
