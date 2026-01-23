package arez.integration.observable;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObservableInitialIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final PersonModel person = PersonModel.create();

    safeAction( () -> assertEquals( person.getName(), "Bob" ) );
    safeAction( () -> assertEquals( person.getAge(), 42 ) );
    safeAction( () -> assertEquals( person.getNickname(), "Bobby" ) );

    context.action( () -> {
      person.setName( "Jane" );
      person.setAge( 37 );
      person.setNickname( null );
    } );

    safeAction( () -> assertEquals( person.getName(), "Jane" ) );
    safeAction( () -> assertEquals( person.getAge(), 37 ) );
    safeAction( () -> assertNull( person.getNickname() ) );
  }

  @ArezComponent
  abstract static class PersonModel
  {
    @Nonnull
    static PersonModel create()
    {
      return new ObservableInitialIntegrationTest_Arez_PersonModel();
    }

    @Observable
    @Nonnull
    abstract String getName();

    abstract void setName( @Nonnull String name );

    @Observable
    abstract int getAge();

    abstract void setAge( int age );

    @Observable
    @Nullable
    abstract String getNickname();

    abstract void setNickname( @Nullable String nickname );

    @ObservableInitial
    @Nonnull
    static final String INITIAL_NAME = "Bob";

    @ObservableInitial
    static int getInitialAge()
    {
      return 42;
    }

    @ObservableInitial( name = "nickname" )
    static String initialNickname()
    {
      return "Bobby";
    }
  }
}
