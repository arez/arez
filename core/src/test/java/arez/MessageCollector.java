package arez;

import java.io.File;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.GuardMessageCollector;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import static org.testng.Assert.*;

public final class MessageCollector
  extends TestListenerAdapter
  implements ITestListener
{
  @Nonnull
  private final GuardMessageCollector _messages = createCollector();

  @Override
  public void onTestStart( @Nonnull final ITestResult result )
  {
    if ( shouldCheckDiagnosticMessages() )
    {
      _messages.onTestStart();
    }
  }

  @Override
  public void onTestSuccess( @Nonnull final ITestResult result )
  {
    if ( shouldCheckDiagnosticMessages() )
    {
      _messages.onTestComplete();
    }
  }

  @Override
  public void onStart( @Nonnull final ITestContext context )
  {
    if ( shouldCheckDiagnosticMessages() )
    {
      _messages.onTestSuiteStart();
    }
  }

  @Override
  public void onFinish( @Nonnull final ITestContext context )
  {
    if ( 0 == context.getFailedTests().size() && shouldCheckDiagnosticMessages() )
    {
      _messages.onTestSuiteComplete();
    }
  }

  private boolean shouldCheckDiagnosticMessages()
  {
    return System.getProperty( "arez.check_diagnostic_messages", "true" ).equals( "true" );
  }

  @Nonnull
  private GuardMessageCollector createCollector()
  {
    final boolean saveIfChanged = "true".equals( System.getProperty( "arez.output_fixture_data", "false" ) );
    final String fixtureDir = System.getProperty( "arez.diagnostic_messages_file" );
    assertNotNull( fixtureDir,
                   "Expected System.getProperty( \"arez.diagnostic_messages_file\" ) to return location of diagnostic messages file" );
    return new GuardMessageCollector( "Arez", new File( fixtureDir ), saveIfChanged );
  }
}
