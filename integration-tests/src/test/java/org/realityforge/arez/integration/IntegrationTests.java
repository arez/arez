package org.realityforge.arez.integration;

import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.testng.annotations.Test;

public class IntegrationTests
  extends AbstractIntegrationTest
{
  /**
   * Tests integration between autorun and actions and sequencing of operations.
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
}
