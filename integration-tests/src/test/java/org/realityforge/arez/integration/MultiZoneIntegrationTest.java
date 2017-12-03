package org.realityforge.arez.integration;

import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ArezTestUtil;
import org.realityforge.arez.Zone;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Observable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MultiZoneIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void multiZoneScenario()
    throws Throwable
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();

    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context1.getSpy().addSpyEventHandler( recorder );
    context2.getSpy().addSpyEventHandler( recorder );

    final AtomicReference<PersonModel> person = new AtomicReference<>();
    final AtomicReference<PersonModel> person2 = new AtomicReference<>();
    zone1.run( () -> {
      person.set( PersonModel.create( "Bill", "Smith" ) );
      zone2.run( () -> person2.set( PersonModel.create( "Bill", "Smith" ) ) );
    } );

    context1.autorun( "FirstNamePrinter1",
                      () -> record( recorder, "firstName1", person.get().getFirstName() ) );
    context2.autorun( "FirstNamePrinter2",
                      () -> record( recorder, "firstName2", person2.get().getFirstName() ) );

    context1.autorun( "FullNamePrinter1",
                      () -> record( recorder, "fullname1", person.get().getFullName() ) );
    context2.autorun( "FullNamePrinter2",
                      () -> record( recorder, "fullname2", person2.get().getFullName() ) );

    context1.action( "First Name Update1", true, () -> person.get().setFirstName( "Fred" ) );
    context1.action( "Last Name Update1", true, () -> person.get().setLastName( "Donaldo" ) );

    context2.action( "Last Name Update2", true, () -> person2.get().setLastName( "Donaldo" ) );

    assertEqualsFixture( recorder.eventsAsString() );
  }

  @Test
  public void multiZoneScenario_transactionAlignment()
    throws Throwable
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();

    final ArezContext context1 = zone1.getContext();
    final ArezContext context2 = zone2.getContext();

    final AtomicReference<PersonModel> person1 = new AtomicReference<>();
    final AtomicReference<PersonModel> person2 = new AtomicReference<>();

    zone1.run( () -> {
      person1.set( PersonModel.create( "Bill", "Smith" ) );
      zone2.run( () -> person2.set( PersonModel.create( "Bill", "Smith" ) ) );
    } );

    context1.action( () -> assertInTransaction( person1.get() ) );
    context1.action( () -> assertNotInTransaction( person2.get() ) );
    context2.action( () -> assertNotInTransaction( person1.get() ) );
    context2.action( () -> assertInTransaction( person2.get() ) );
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
      return new MultiZoneIntegrationTest_Arez_PersonModel( firstName, lastName );
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
