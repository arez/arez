package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;

@SuppressWarnings( "Duplicates" )
public class NoIdInNameIntegrationTest
  extends AbstractIntegrationTest
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

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final PersonModel person = PersonModel.create( "Bill", "Smith" );

    context.autorun( "FirstNamePrinter",
                     () -> record( recorder, "firstName", person.getFirstName() ) );
    context.autorun( "FullNamePrinter",
                     () -> record( recorder, "fullname", person.getFullName() ) );

    context.action( "First Name Update", true, () -> person.setFirstName( "Fred" ) );
    context.action( "Last Name Update", true, () -> person.setLastName( "Donaldo" ) );

    assertEqualsFixture( recorder.eventsAsString() );
  }

  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent( nameIncludesId = false )
  public static class PersonModel
  {
    @Nonnull
    private String _firstName;
    @Nonnull
    private String _lastName;

    @Nonnull
    public static PersonModel create( @Nonnull final String firstName, @Nonnull final String lastName )
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
    public String getFirstName()
    {
      return _firstName;
    }

    public void setFirstName( @Nonnull final String firstName )
    {
      _firstName = firstName;
    }

    @Observable
    @Nonnull
    public String getLastName()
    {
      return _lastName;
    }

    public void setLastName( @Nonnull final String lastName )
    {
      _lastName = lastName;
    }

    @Computed
    @Nonnull
    public String getFullName()
    {
      return getFirstName() + " " + getLastName();
    }
  }
}
