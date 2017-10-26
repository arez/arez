package org.realityforge.arez.integration;

import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ArezTestUtil;
import org.realityforge.arez.Zone;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Observable;
import org.testng.annotations.Test;
import static org.testng.Assert.assertThrows;

public class MultiZoneIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void multiZoneScenario()
    throws Throwable
  {
    ArezTestUtil.setEnableZones( true );

    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();

    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context1.getSpy().addSpyEventHandler( recorder );
    context2.getSpy().addSpyEventHandler( recorder );

    zone1.activate();
    final PersonModel person = PersonModel.create( "Bill", "Smith" );

    zone2.activate();
    final PersonModel person2 = PersonModel.create( "Bill", "Smith" );

    zone2.deactivate();
    zone1.deactivate();

    context1.autorun( "FirstNamePrinter1",
                     () -> record( recorder, "firstName1", person.getFirstName() ) );
    context2.autorun( "FirstNamePrinter2",
                     () -> record( recorder, "firstName2", person2.getFirstName() ) );

    context1.autorun( "FullNamePrinter1",
                     () -> record( recorder, "fullname1", person.getFullName() ) );
    context2.autorun( "FullNamePrinter2",
                     () -> record( recorder, "fullname2", person2.getFullName() ) );

    context1.action( "First Name Update1", true, () -> person.setFirstName( "Fred" ) );
    context1.action( "Last Name Update1", true, () -> person.setLastName( "Donaldo" ) );

    context2.action( "Last Name Update2", true, () -> person2.setLastName( "Donaldo" ) );

    assertEqualsFixture( recorder.eventsAsString() );
  }

  @Test
  public void multiZoneScenario_transactionAlignment()
    throws Throwable
  {
    ArezTestUtil.setEnableZones( true );

    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();

    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();

    zone1.activate();
    final PersonModel person1 = PersonModel.create( "Bill", "Smith" );

    zone2.activate();
    final PersonModel person2 = PersonModel.create( "Bill", "Smith" );

    zone2.deactivate();
    zone1.deactivate();

    context1.action(  () -> assertInTransaction( person1 ) );
    context1.action(  () -> assertNotInTransaction( person2 ) );
    context2.action(  () -> assertNotInTransaction( person1 ) );
    context2.action(  () -> assertInTransaction( person2 ) );
  }


  /**
   * Test we are in a transaction by trying to observe an observable.
   */
  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  private void assertInTransaction( @Nonnull final PersonModel person )
  {
    person.getFirstName();
  }

  /**
   * Test we are not in a transaction by trying to observe an observable.
   */
  private void assertNotInTransaction( @Nonnull final PersonModel person )
  {
    assertThrows( person::getFirstName );
  }
  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent
  public static class PersonModel
  {
    @Nonnull
    private String _firstName;
    @Nonnull
    private String _lastName;

    @Nonnull
    public static PersonModel create( @Nonnull final String firstName, @Nonnull final String lastName )
    {
      return new MultiZoneIntegrationTest$Arez_PersonModel( firstName, lastName );
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
