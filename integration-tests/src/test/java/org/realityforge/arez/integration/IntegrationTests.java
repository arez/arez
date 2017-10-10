package org.realityforge.arez.integration;

import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observer;
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

  /**
   * Another example that incorporates actions, computed and disposes.
   */
  @Test
  public void codeModelScenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final CodeModel codeModel = CodeModel.create( "com.example", "MyType" );

    final Observer observer =
      context.autorun( "Printer",
                       () -> record( recorder, "qualifiedName", codeModel.getQualifiedName() ) );

    context.action( "Specific Qualified Name", true, () -> codeModel.setQualifiedName( "com.biz.Fred" ) );
    context.action( "Reset Qualified Name to default", true, () -> codeModel.setQualifiedName( null ) );
    context.action( "Change Local Name", true, () -> codeModel.setName( "MyType2" ) );

    observer.dispose();
    context.action( "Dispose Model", true, () -> Disposable.dispose( codeModel ) );

    assertEqualsFixture( recorder.eventsAsString() );
  }
}
