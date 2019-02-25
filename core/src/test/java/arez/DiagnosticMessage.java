package arez;

import javax.annotation.Nonnull;

final class DiagnosticMessage
{
  private final int _code;
  @Nonnull
  private final Guards.Type _type;
  @Nonnull
  private final String _messagePattern;
  private final boolean _loadedFromFixture;

  DiagnosticMessage( final int code,
                     @Nonnull final Guards.Type type,
                     @Nonnull final String messagePattern,
                     final boolean loadedFromFixture )
  {
    _code = code;
    _type = type;
    _messagePattern = messagePattern;
    _loadedFromFixture = loadedFromFixture;
  }

  int getCode()
  {
    return _code;
  }

  @Nonnull
  Guards.Type getType()
  {
    return _type;
  }

  @Nonnull
  String getMessagePattern()
  {
    return _messagePattern;
  }

  boolean isLoadedFromFixture()
  {
    return _loadedFromFixture;
  }
}
