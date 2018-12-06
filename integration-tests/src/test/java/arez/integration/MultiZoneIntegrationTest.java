package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Zone;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MultiZoneIntegrationTest
  extends AbstractArezIntegrationTest
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

    context1.observer( "FirstNamePrinter1",
                       () -> {
                         observeADependency();
                         recorder.mark( "firstName1", person.get().getFirstName() );
                       } );
    context2.observer( "FirstNamePrinter2",
                       () -> {
                         observeADependency();
                         recorder.mark( "firstName2", person2.get().getFirstName() );
                       } );

    context1.observer( "FullNamePrinter1",
                       () -> {
                         observeADependency();
                         recorder.mark( "fullname1", person.get().getFullName() );
                       } );
    context2.observer( "FullNamePrinter2",
                       () -> {
                         observeADependency();
                         recorder.mark( "fullname2", person2.get().getFullName() );
                       } );

    context1.action( "First Name Update1", () -> person.get().setFirstName( "Fred" ) );
    context1.action( "Last Name Update1", () -> person.get().setLastName( "Donaldo" ) );

    context2.action( "Last Name Update2", () -> person2.get().setLastName( "Donaldo" ) );

    assertMatchesFixture( recorder );
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
    assertNotInTransaction( person2.get() );
    assertNotInTransaction( person1.get() );
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
  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  private void assertNotInTransaction( @Nonnull final PersonModel person )
  {
    assertThrows( person::getFirstName );
  }

  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent
  public static abstract class PersonModel
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

    @Memoize
    @Nonnull
    public String getFullName()
    {
      return getFirstName() + " " + getLastName();
    }
  }
}
