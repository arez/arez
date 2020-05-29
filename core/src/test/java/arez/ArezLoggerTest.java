package arez;

import java.util.List;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ArezLoggerTest
  extends AbstractTest
{
  @Test
  public void log()
  {
    final String message1 = ValueUtil.randomString();
    final String message2 = ValueUtil.randomString();
    ArezLogger.log( message1, null );
    final Throwable throwable = new Throwable();
    ArezLogger.log( message2, throwable );

    final List<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 2 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(), message1 );
    assertNull( entry1.getThrowable() );
    final TestLogger.LogEntry entry2 = entries.get( 1 );
    assertEquals( entry2.getMessage(), message2 );
    assertEquals( entry2.getThrowable(), throwable );
  }
}
