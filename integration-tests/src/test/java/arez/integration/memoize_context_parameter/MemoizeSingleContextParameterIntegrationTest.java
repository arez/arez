package arez.integration.memoize_context_parameter;

import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.Stack;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class MemoizeSingleContextParameterIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Nonnull
  private static Stack<String> _contextStack = new Stack<>();

  @Test
  public void personScenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final PersonModel person = PersonModel.create( "Bill", "Smith" );

    final Observer observer1 =
      context.observer( "FullNamePrinterC1", () -> {
        _contextStack.push( "C1" );
        final String fullName = person.getFullName();
        _contextStack.pop();
        recorder.mark( "fullname", fullName );
        assertTrue( fullName.endsWith( "(--C1--)" ) );
      } );
    final Observer observer2 =
      context.observer( "FullNamePrinterC2", () -> {
        _contextStack.push( "C2" );
        final String fullName = person.getFullName();
        _contextStack.pop();
        recorder.mark( "fullname", fullName );
        assertTrue( fullName.endsWith( "(--C2--)" ) );
      } );

    context.action( "First Name Update", () -> person.setFirstName( "Fred" ) );
    context.action( "Second Name Update that does nothing", () -> {
      person.setFirstName( "FredSomethingElse" );
      person.setFirstName( "Fred" );
    } );
    context.action( "Last Name Update", () -> person.setLastName( "Donaldo" ) );

    observer1.dispose();
    observer2.dispose();

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
      return new MemoizeSingleContextParameterIntegrationTest_Arez_PersonModel( firstName, lastName );
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
      return getFirstName() + " " + getLastName() + " (--" + currentMyContextVar() + "--)";
    }

    @MemoizeContextParameter
    String captureMyContextVar()
    {
      return currentMyContextVar();
    }

    private String currentMyContextVar()
    {
      return _contextStack.peek();
    }

    void pushMyContextVar( @Nonnull final String item )
    {
      _contextStack.push( item );
    }

    void popMyContextVar( @Nonnull final String item )
    {
      assertEquals( _contextStack.pop(), item, "Expected context items to match" );
    }
  }
}
