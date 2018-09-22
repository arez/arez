package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.Flags;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NonTransactionEnforcingIntegrationTest
  extends AbstractArezIntegrationTest
{
  /**
   * Tests integration between observed and actions and sequencing of operations.
   */
  @Test
  public void personScenario()
    throws Throwable
  {
    ArezTestUtil.noEnforceTransactionType();
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

    final AtomicBoolean action1ReadOnly = new AtomicBoolean( true );
    final AtomicBoolean action2ReadOnly = new AtomicBoolean( true );

    // Note that the next two actions pass in mutation false but actually perform mutation
    // This is to verify that transactions are really not set
    context.action( "First Name Update",
                    () -> {
                      person.setFirstName( "Fred" );
                      action1ReadOnly.set( context.getSpy().getTransaction().isReadOnly() );
                    },
                    Flags.READ_ONLY );
    context.action( "Last Name Update",
                    () -> {
                      person.setLastName( "Donaldo" );
                      action2ReadOnly.set( context.getSpy().getTransaction().isReadOnly() );
                    },
                    Flags.READ_ONLY );

    // When transactions are not enforced, everything is effectively a write transaction!!!
    assertFalse( action1ReadOnly.get() );
    assertFalse( action2ReadOnly.get() );

    assertMatchesFixture( recorder );

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
      return new NonTransactionEnforcingIntegrationTest_Arez_PersonModel( firstName, lastName );
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
