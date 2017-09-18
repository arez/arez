package org.realityforge.arez.examples;

import org.realityforge.arez.ArezContext;

public final class CalculatedModelExample
{
  public static void main( final String[] args )
    throws Throwable
  {
    final ArezContext context = new ArezContext();
    ExampleUtil.logAllErrors( context );

    final PersonModel person = PersonModel.create( "Bill", "Smith" );

    context.autorun( "NamePrinter",
                     false,
                     () -> System.out.println( "First Name: " + person.getFirstName() ),
                     true );
    context.autorun( "Printer",
                     false,
                     () -> System.out.println( "Full Name: " + person.getFullName() ),
                     true );

    context.procedure( "Name update", true, () -> person.setFirstName( "Fred" ) );
    context.procedure( "Name update2", true, () -> person.setLastName( "Donaldo" ) );
  }
}
