package org.realityforge.arez.examples;

import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;

public final class CalculatedModelExample
{
  public static void main( final String[] args )
    throws Throwable
  {
    final ArezContext context = Arez.context();
    ExampleUtil.logAllErrors( context );

    final PersonModel person = PersonModel.create( "Bill", "Smith" );

    context.autorun( "NamePrinter",
                     () -> System.out.println( "First Name: " + person.getFirstName() ) );
    context.autorun( "Printer",
                     () -> System.out.println( "Full Name: " + person.getFullName() ) );

    context.procedure( "Name update", true, () -> person.setFirstName( "Fred" ) );
    context.procedure( "Name update2", true, () -> person.setLastName( "Donaldo" ) );
  }
}
