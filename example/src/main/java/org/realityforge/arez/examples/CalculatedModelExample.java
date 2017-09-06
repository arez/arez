package org.realityforge.arez.examples;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

public final class CalculatedModelExample
{
  @Container( name = "Person", singleton = true )
  public static abstract class PersonModel
  {
    public abstract String getFirstName();

    public abstract void setFirstName( @Nonnull String firstName );

    public abstract String getLastName();

    public abstract void setLastName( @Nonnull String lastName );

    @Computed
    @Nonnull
    public String getFullName()
    {
      return getFirstName() + " " + getLastName();
    }
  }

  public static class PersonModelImpl
    extends PersonModel
  {
    private final org.realityforge.arez.Observable $_firstName;
    private final org.realityforge.arez.Observable $_lastName;
    private final org.realityforge.arez.ComputedValue<String> $_fullName;

    private String _firstName;
    private String _lastName;

    PersonModelImpl( @Nonnull final ArezContext context )
    {
      $_firstName = context.createObservable( "Person.firstName" );
      $_lastName = context.createObservable( "Person.lastName" );
      $_fullName = context.createComputedValue( "Person.fullName", super::getFullName, Objects::equals );
    }

    @Override
    public String getFirstName()
    {
      $_firstName.reportObserved();
      return _firstName;
    }

    @Override
    public void setFirstName( @Nonnull final String firstName )
    {
      if ( !Objects.equals( firstName, _firstName ) )
      {
        $_firstName.reportChanged();
        _firstName = firstName;
      }
    }

    @Override
    public String getLastName()
    {
      $_lastName.reportObserved();
      return _lastName;
    }

    @Override
    public void setLastName( @Nonnull final String lastName )
    {
      if ( !Objects.equals( lastName, _lastName ) )
      {
        $_firstName.reportChanged();
        _lastName = lastName;
      }
    }

    @Nonnull
    @Override
    public String getFullName()
    {
      return $_fullName.get();
    }
  }

  public static void main( final String[] args )
    throws Exception
  {
    final ArezContext context = new ArezContext();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> {
      System.out.println( "Observer error: " + error + "\nobserver: " + observer );
      if ( null != throwable )
      {
        throwable.printStackTrace( System.out );
      }
    } );

    final PersonModel person = new PersonModelImpl( context );

    context.procedure( "Initial setup",
                       true,
                       () -> {
                         person.setFirstName( "Bill" );
                         person.setLastName( "Smith" );
                       } );

    context.autorun( "NamePrinter",
                     false,
                     () -> System.out.println( "First Name: " + person.getFirstName() ),
                     true );
    context.autorun( "Printer",
                     false,
                     () -> System.out.println( "Full Name: " + person.getFullName() ),
                     true );

    context.procedure( "Name update", true, () -> person.setFirstName( "Fred" ) );
  }
}
