package arez;

import javax.annotation.Nullable;

/**
 * Hook that can be registered for the current observer.
 */
public final class Hook
{
  @Nullable
  private final Procedure _onActivate;
  @Nullable
  private final Procedure _onDeactivate;

  Hook( @Nullable final Procedure onActivate, @Nullable final Procedure onDeactivate )
  {
    _onActivate = onActivate;
    _onDeactivate = onDeactivate;
  }

  @Nullable
  Procedure getOnActivate()
  {
    return _onActivate;
  }

  @Nullable
  Procedure getOnDeactivate()
  {
    return _onDeactivate;
  }
}
