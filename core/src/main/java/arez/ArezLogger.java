package arez;

import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import org.realityforge.anodoc.TestOnly;

/**
 * Log abstraction for framework.
 */
final class ArezLogger
{
  private static final Logger c_logger =
    "console".equals( ArezConfig.loggerType() ) ? new BasicLogger() :
    "console_js".equals( ArezConfig.loggerType() ) ? new BasicJsLogger() :
    "jul".equals( ArezConfig.loggerType() ) ? new JavaUtilLogger() :
    "proxy".equals( ArezConfig.loggerType() ) ? new ProxyLogger() :
    new NoopLogger();

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
   * The basic log provider implementation.
   */
  private static final class BasicLogger
    implements Logger
  {
    @Override
    public void log( @Nonnull final String message, @Nullable final Throwable throwable )
    {
      System.out.println( message );
      if ( null != throwable )
      {
        throwable.printStackTrace( System.out );
      }
    }
  }

  @JsType( isNative = true, name = "window.console", namespace = JsPackage.GLOBAL )
  private static final class NativeJsLoggerUtil
  {
    @JsMethod
    public static native void log( Object message );
  }

  /**
   * The basic log provider implementation.
   */
  private static final class BasicJsLogger
    implements Logger
  {

    @Override
    public void log( @Nonnull final String message, @Nullable final Throwable throwable )
    {
      NativeJsLoggerUtil.log( message );
      if ( null != throwable )
      {
        NativeJsLoggerUtil.log( throwable );
      }
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

    Logger getLogger()
    {
      return _logger;
    }

    void setLogger( final Logger logger )
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
