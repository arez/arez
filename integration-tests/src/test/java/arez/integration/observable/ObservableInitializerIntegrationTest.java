package arez.integration.observable;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.integration.AbstractIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;

@SuppressWarnings( "Duplicates" )
public class ObservableInitializerIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final PersonModel person = PersonModel.create( "Bill", "Smith" );

    context.autorun( "FirstNamePrinter",
                     () -> recorder.mark( "firstName", person.getFirstName() ) );
    context.autorun( "FullNamePrinter",
                     () -> recorder.mark( "fullname", person.getFullName() ) );

    context.action( "First Name Update", true, () -> person.setFirstName( "Fred" ) );
    context.action( "Last Name Update", true, () -> person.setLastName( "Donaldo" ) );

    assertMatchesFixture( recorder );
  }

  @SuppressWarnings( "SameParameterValue" )
  @ArezComponent
  static abstract class PersonModel
  {
    @Nonnull
    static PersonModel create( @Nonnull final String firstName, @Nonnull final String lastName )
    {
      return new ObservableInitializerIntegrationTest_Arez_PersonModel( firstName, lastName );
    }

    @Observable
    @Nonnull
    abstract String getFirstName();

    abstract void setFirstName( @Nonnull String firstName );

    @Observable
    @Nonnull
    abstract String getLastName();

    abstract void setLastName( @Nonnull String lastName );

    @Computed
    @Nonnull
    String getFullName()
    {
      return getFirstName() + " " + getLastName();
    }
  }
}
