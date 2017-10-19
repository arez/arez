package org.realityforge.arez.integration;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Spy;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Observable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NonTransactionEnforcingIntegrationTest
  extends AbstractIntegrationTest
{
  /**
   * Tests integration between autorun and actions and sequencing of operations.
   */
  @Test
  public void personScenario()
    throws Throwable
  {
    setEnforceTransactionTypeToFalse();
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    final Spy spy = context.getSpy();
    spy.addSpyEventHandler( recorder );

    final PersonModel person = PersonModel.create( "Bill", "Smith" );

    context.autorun( "FirstNamePrinter",
                     () -> record( recorder, "firstName", person.getFirstName() ) );
    context.autorun( "FullNamePrinter",
                     () -> record( recorder, "fullname", person.getFullName() ) );

    final AtomicBoolean action1ReadOnly = new AtomicBoolean( true );
    final AtomicBoolean action2ReadOnly = new AtomicBoolean( true );

    // Note that the next two actions pass in mutation false but actually perform mutation
    // This is to verify that transactions are really not set
    context.action( "First Name Update",
                    false,
                    () -> {
                      person.setFirstName( "Fred" );
                      action1ReadOnly.set( spy.getTransaction().isReadOnly() );
                    } );
    context.action( "Last Name Update",
                    false,
                    () -> {
                      person.setLastName( "Donaldo" );
                      action2ReadOnly.set( spy.getTransaction().isReadOnly() );
                    } );

    // When transactions are not enforced, everything is effectively a write transaction!!!
    assertEquals( action1ReadOnly.get(), false );
    assertEquals( action2ReadOnly.get(), false );

    assertEqualsFixture( recorder.eventsAsString() );

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
      return new NonTransactionEnforcingIntegrationTest$Arez_PersonModel( firstName, lastName );
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
