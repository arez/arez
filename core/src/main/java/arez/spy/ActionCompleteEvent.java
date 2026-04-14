package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Action completes.
 */
public final class ActionCompleteEvent
  implements SerializableEvent
{
  @Nonnull
  private final String _name;
  /**
   * Is the action a "tracking" action.
   */
  private final boolean _tracked;
  @Nonnull
  private final Object[] _parameters;
  /**
   * True if the action returns a result during normal completion.
   */
  private final boolean _returnsResult;
  @Nullable
  private final Object _result;
  @Nullable
  private final Throwable _throwable;
  private final int _duration;

  public ActionCompleteEvent( @Nonnull final String name,
                              final boolean tracked,
                              @Nonnull final Object[] parameters,
                              final boolean returnsResult,
                              @Nullable final Object result,
                              @Nullable final Throwable throwable,
                              final int duration )
  {
    assert duration >= 0;
    assert null == throwable || null == result;
    _name = Objects.requireNonNull( name );
    _tracked = tracked;
    _parameters = Objects.requireNonNull( parameters );
    _returnsResult = returnsResult;
    _result = result;
    _throwable = throwable;
    _duration = duration;
  }

  @Nonnull
  public String getName()
  {
    return _name;
  }

  public boolean isTracked()
  {
    return _tracked;
  }

  @Nonnull
  public Object[] getParameters()
  {
    return _parameters;
  }

  public boolean returnsResult()
  {
    return _returnsResult;
  }

  @Nullable
  public Object getResult()
  {
    return _result;
  }

  @Nullable
  public Throwable getThrowable()
  {
    return _throwable;
  }

  public int getDuration()
  {
    return _duration;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ActionComplete" );
    map.put( "name", getName() );
    map.put( "tracked", isTracked() );
    map.put( "duration", getDuration() );
    final Throwable throwable = getThrowable();
    final String message =
      null == throwable ? null : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
    map.put( "errorMessage", message );
    map.put( "parameters", getParameters() );
    map.put( "returnsResult", returnsResult() );
    map.put( "result", getResult() );
    SpyEventUtil.maybeAddZone( map );
  }
}
