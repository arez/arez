package arez;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.BrainCheckTestUtil;

final class DiagnosticMessage
{
  private final int _code;
  @Nonnull
  private final BrainCheckTestUtil.GuardType _type;
  @Nonnull
  private final String _messagePattern;
  private final boolean _needsSave;
  private final Set<StackTraceElement> _originalCallers = new HashSet<>();
  private final Set<StackTraceElement> _callers = new HashSet<>();

  DiagnosticMessage( final int code,
                     @Nonnull final BrainCheckTestUtil.GuardType type,
                     @Nonnull final String messagePattern,
                     final boolean needsSave,
                     @Nonnull final Set<StackTraceElement> callers )
  {
    _code = code;
    _type = type;
    _messagePattern = messagePattern;
    _needsSave = needsSave;
    _originalCallers.addAll( callers );
  }

  int getCode()
  {
    return _code;
  }

  @Nonnull
  BrainCheckTestUtil.GuardType getType()
  {
    return _type;
  }

  @Nonnull
  String getMessagePattern()
  {
    return _messagePattern;
  }

  boolean needsSave()
  {
    return _needsSave || !Objects.equals( _originalCallers, _callers ) || _originalCallers.isEmpty();
  }

  void recordCaller( @Nonnull final StackTraceElement caller )
  {
    _callers.add( caller );
  }

  Set<StackTraceElement> getCallers()
  {
    return Collections.unmodifiableSet( _callers );
  }

  @Override
  public String toString()
  {
    return "Arez-" + getCode() + ": " + getMessagePattern();
  }
}
