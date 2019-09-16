package arez;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.realityforge.braincheck.GuardMessageCollector;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import static org.testng.Assert.*;

public abstract class AbstractTest
{
  private static final GuardMessageCollector c_messages = createCollector();
  private final TestLogger _logger = new TestLogger();
  private final ArrayList<String> _observerErrors = new ArrayList<>();
  private boolean _ignoreObserverErrors;

  @BeforeMethod
  protected void beforeTest()
    throws Exception
  {
    BrainCheckTestUtil.resetConfig( false );
    ArezTestUtil.resetConfig( false );
    ArezTestUtil.enableZones();
    _logger.getEntries().clear();
    ArezTestUtil.setLogger( _logger );
    _ignoreObserverErrors = false;
    _observerErrors.clear();
    Arez.context().addObserverErrorHandler( this::onObserverError );
    c_messages.onTestStart();
  }

  @AfterMethod
  protected void afterTest()
  {
    c_messages.onTestComplete();
    BrainCheckTestUtil.resetConfig( true );
    ArezTestUtil.resetConfig( true );
    if ( !_ignoreObserverErrors && !_observerErrors.isEmpty() )
    {
      fail( "Unexpected Observer Errors: " + String.join( "\n", _observerErrors ) );
    }
  }

  @BeforeSuite
  protected void beforeSuite()
  {
    c_messages.onTestSuiteStart();
  }

  @Nonnull
  private static GuardMessageCollector createCollector()
  {
    final boolean saveIfChanged = "true".equals( System.getProperty( "arez.output_fixture_data", "false" ) );
    final String fixtureDir = System.getProperty( "arez.diagnostic_messages_file" );
    assertNotNull( fixtureDir,
                   "Expected System.getProperty( \"arez.diagnostic_messages_file\" ) to return location of diagnostic messages file" );
    return new GuardMessageCollector( "Arez", new File( fixtureDir ), saveIfChanged );
  }

  @AfterSuite
  protected void afterSuite()
  {
    if ( !System.getProperty( "arez.check_diagnostic_messages", "true" ).equals( "false" ) )
    {
      c_messages.onTestSuiteComplete();
    }
  }

  @Nonnull
  final TestLogger getTestLogger()
  {
    return _logger;
  }

  @Nonnull
  private Field getField( @Nonnull final Class<?> type, @Nonnull final String fieldName )
  {
    Class clazz = type;
    while ( null != clazz && Object.class != clazz )
    {
      try
      {
        final Field field = clazz.getDeclaredField( fieldName );
        field.setAccessible( true );
        return field;
      }
      catch ( final Throwable t )
      {
        clazz = clazz.getSuperclass();
      }
    }

    Assert.fail();
    throw new IllegalStateException();
  }

  @SuppressWarnings( "SameParameterValue" )
  final void setField( @Nonnull final Object object, @Nonnull final String fieldName, @Nullable final Object value )
    throws IllegalAccessException
  {
    getField( object.getClass(), fieldName ).set( object, value );
  }

  @Nonnull
  final Observer newReadWriteObserver( @Nonnull final ArezContext context )
  {
    return context.observer( new CountAndObserveProcedure(), Observer.Flags.READ_WRITE );
  }

  final void setupReadOnlyTransaction()
  {
    setupReadOnlyTransaction( Arez.context() );
  }

  final void setupReadOnlyTransaction( @Nonnull final ArezContext context )
  {
    Transaction.setTransaction( null );
    setCurrentTransaction( context.observer( new CountAndObserveProcedure() ) );
  }

  final void setupReadWriteTransaction()
  {
    setupReadWriteTransaction( Arez.context() );
  }

  private void setupReadWriteTransaction( @Nonnull final ArezContext context )
  {
    Transaction.setTransaction( null );
    setCurrentTransaction( newReadWriteObserver( context ) );
  }

  final void setCurrentTransaction( @Nonnull final Observer observer )
  {
    Transaction.setTransaction( null );
    final ArezContext context = observer.getContext();
    Transaction.setTransaction( new Transaction( context,
                                                 null,
                                                 ValueUtil.randomString(),
                                                 observer.isMutation(),
                                                 observer,
                                                 false ) );
  }

  protected final void ignoreObserverErrors()
  {
    _ignoreObserverErrors = true;
  }

  protected static void assertInvariantFailure( @Nonnull final ThrowingRunnable throwingRunnable,
                                                @Nonnull final String message )
  {
    assertEquals( expectThrows( IllegalStateException.class, throwingRunnable ).getMessage(), message );
  }

  protected static void observeADependency()
  {
    Arez.context().observable().reportObserved();
  }

  private void onObserverError( @Nonnull final Observer observer,
                                @Nonnull final ObserverError error,
                                @Nullable final Throwable throwable )
  {
    final String message = "Observer: " + observer.getName() + " Error: " + error + " " + throwable;
    _observerErrors.add( message );
    if ( !_ignoreObserverErrors )
    {
      System.out.println( message );
      if ( null != throwable )
      {
        throwable.printStackTrace();
      }
    }
  }

  @Nonnull
  final ArrayList<String> getObserverErrors()
  {
    return _observerErrors;
  }
}
