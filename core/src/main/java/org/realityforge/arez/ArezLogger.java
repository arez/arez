package org.realityforge.arez;

import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;

/**
 * Log abstraction for framework.
 */
final class ArezLogger
{
  private static final Logger c_logger = createLogger();

  private ArezLogger()
  {
  }

  /**
   * Log a message with an optional exception.
   */
  static void log( @Nonnull final String message, @Nullable final Throwable throwable )
  {
    c_logger.log( message, throwable );
  }

  @TestOnly
  @Nonnull
  static Logger getLogger()
  {
    return c_logger;
  }

  /**
   * Create the logger based on compile time properties.
   */
  @Nonnull
  private static Logger createLogger()
  {
    //This will probably be expanded in the future to support an Elemental Logger implementation
    final String loggerType = System.getProperty( "arez.logger", "jul" );
    return loggerType.equals( "jul" ) ? new JavaUtilLogger() :
           loggerType.equals( "proxy" ) ? new ProxyLogger() :
           new NoopLogger();
  }

  /**
   * Abstraction used to provide logging for Arez system.
   * This abstraction is used to support compile time constants during GWT and/or closure
   * compiler phases and thus allow elimination of code during production variants of the runtime.
   */
  interface Logger
  {
    void log( @Nonnull String message, @Nullable Throwable throwable );
  }

  /**
   * The noop log provider implementation.
   */
  private static final class NoopLogger
    implements Logger
  {
    @Override
    public void log( @Nonnull final String message, @Nullable final Throwable throwable )
    {
    }
  }

  /**
   * The normal log provider implementation.
   */
  private static final class JavaUtilLogger
    implements Logger
  {
    private final java.util.logging.Logger _logger = java.util.logging.Logger.getLogger( ArezLogger.class.getName() );

    @Override
    public void log( @Nonnull final String message, @Nullable final Throwable throwable )
    {
      _logger.log( Level.INFO, message, throwable );
    }
  }

  /**
   * The log provider implementation that forwards to another logger if present.
   */
  static final class ProxyLogger
    implements Logger
  {
    private Logger _logger;

    public Logger getLogger()
    {
      return _logger;
    }

    public void setLogger( final Logger logger )
    {
      _logger = logger;
    }

    @Override
    public void log( @Nonnull final String message, @Nullable final Throwable throwable )
    {
      if ( null != _logger )
      {
        _logger.log( message, throwable );
      }
    }
  }
}
