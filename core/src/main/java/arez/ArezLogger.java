package arez;

import grim.annotations.OmitSymbol;
import grim.annotations.OmitType;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Log abstraction for framework.
 */
final class ArezLogger
{
  private static final Logger c_logger =
    "console".equals( ArezConfig.loggerType() ) ? new ConsoleLogger() :
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

  @OmitSymbol
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
  @OmitType
  private static final class NoopLogger
    implements Logger
  {
    @Override
    public void log( @Nonnull final String message, @Nullable final Throwable throwable )
    {
    }
  }

  /**
   * The console log provider implementation.
   */
  @OmitType( unless = "arez.logger=console" )
  private static final class ConsoleLogger
    extends AbstractConsoleLogger
  {
    @GwtIncompatible
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

  @OmitType( unless = "arez.logger=console" )
  @JsType( isNative = true, name = "window.console", namespace = JsPackage.GLOBAL )
  private static final class NativeJsLoggerUtil
  {
    @JsMethod
    public static native void log( Object message );
  }

  /**
   * The console log provider implementation providing javascript based console logging.
   */
  @OmitType( unless = "arez.logger=console" )
  private static abstract class AbstractConsoleLogger
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
   * The log provider implementation that forwards to another logger if present.
   */
  @OmitType( unless = "arez.logger=proxy" )
  static final class ProxyLogger
    implements Logger
  {
    @Nullable
    private Logger _logger;

    void setLogger( @Nullable final Logger logger )
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
