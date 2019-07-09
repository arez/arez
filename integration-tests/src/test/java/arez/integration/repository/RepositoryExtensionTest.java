package arez.integration.repository;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.Procedure;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Repository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RepositoryExtensionTest
  extends AbstractArezIntegrationTest
{
  interface TestComponentRepositoryExtension
  {
    @Nonnull
    default List<TestComponent> findAllLike( @Nonnull final String pattern )
    {
      return self().findAllByQuery( c -> c.getValue().contains( pattern ) );
    }

    default long count()
    {
      return self().entities().count();
    }

    RepositoryExtensionTest_TestComponentRepository self();
  }

  @ArezComponent
  @Repository( extensions = TestComponentRepositoryExtension.class )
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

    final Procedure executable9 = () -> assertEquals( repository.count(), 3 );
    context.action( executable9, ActionFlags.READ_ONLY );
    final Procedure executable8 = () -> assertEquals( repository.findAllLike( "AB" ).size(), 1 );
    context.action( executable8, ActionFlags.READ_ONLY );
    final Procedure executable7 = () -> assertEquals( repository.findAllLike( "AB" ).contains( component1 ), true );
    context.action( executable7, ActionFlags.READ_ONLY );
    final Procedure executable6 = () -> assertEquals( repository.findAllLike( "CD" ).size(), 2 );
    context.action( executable6, ActionFlags.READ_ONLY );
    final Procedure executable5 = () -> assertEquals( repository.findAllLike( "CD" ).contains( component1 ), true );
    context.action( executable5, ActionFlags.READ_ONLY );
    final Procedure executable4 = () -> assertEquals( repository.findAllLike( "CD" ).contains( component2 ), true );
    context.action( executable4, ActionFlags.READ_ONLY );
    final Procedure executable3 = () -> assertEquals( repository.findAllLike( "EF" ).size(), 3 );
    context.action( executable3, ActionFlags.READ_ONLY );
    final Procedure executable2 = () -> assertEquals( repository.findAllLike( "EF" ).contains( component1 ), true );
    context.action( executable2, ActionFlags.READ_ONLY );
    final Procedure executable1 = () -> assertEquals( repository.findAllLike( "EF" ).contains( component2 ), true );
    context.action( executable1, ActionFlags.READ_ONLY );
    final Procedure executable = () -> assertEquals( repository.findAllLike( "EF" ).contains( component3 ), true );
    context.action( executable, ActionFlags.READ_ONLY );
  }
}
