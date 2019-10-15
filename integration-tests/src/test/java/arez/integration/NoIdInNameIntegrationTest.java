package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.util.SpyEventRecorder;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;

public class NoIdInNameIntegrationTest
  extends AbstractArezIntegrationTest
{
  /**
   * Runs through the same scenario as some of the other tests except with
   * component that has name that does not include id.
   */
  @Test
  public void personScenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final PersonModel person = PersonModel.create( "Bill", "Smith" );

    context.observer( "FirstNamePrinter",
                      () -> {
                        observeADependency();
                        recorder.mark( "firstName", person.getFirstName() );
                      } );
    context.observer( "FullNamePrinter",
                      () -> {
                        observeADependency();
                        recorder.mark( "fullname", person.getFullName() );
                      } );

    context.action( "First Name Update", () -> person.setFirstName( "Fred" ) );
    context.action( "Last Name Update", () -> person.setLastName( "Donaldo" ) );

    assertMatchesFixture( recorder );
  }

  @ArezComponent( nameIncludesId = false )
  static abstract class PersonModel
  {
    @Nonnull
    private String _firstName;
    @Nonnull
    private String _lastName;

    @Nonnull
    static PersonModel create( @Nonnull final String firstName, @Nonnull final String lastName )
    {
      return new NoIdInNameIntegrationTest_Arez_PersonModel( firstName, lastName );
    }

    PersonModel( @Nonnull final String firstName, @Nonnull final String lastName )
    {
      _firstName = firstName;
      _lastName = lastName;
    }

    @Observable
    @Nonnull
    String getFirstName()
    {
      return _firstName;
    }

    void setFirstName( @Nonnull final String firstName )
    {
      _firstName = firstName;
    }

    @Observable
    @Nonnull
    String getLastName()
    {
      return _lastName;
    }

    void setLastName( @Nonnull final String lastName )
    {
      _lastName = lastName;
    }

    @Memoize
    @Nonnull
    String getFullName()
    {
      return getFirstName() + " " + getLastName();
    }
  }
}
