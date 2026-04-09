package arez.integration.basic;

import arez.SafeProcedure;
import arez.persist.runtime.Scope;
import arez.persist.runtime.StorageService;
import arez.persist.runtime.TypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class TestStorageService
  implements StorageService
{
  @Nonnull
  private final Map<Scope, Map<String, Map<String, Entry>>> _initialState;
  @Nonnull
  private final Map<Scope, Map<String, Map<String, Entry>>> _state = new HashMap<>();

  public TestStorageService( @Nonnull final Map<Scope, Map<String, Map<String, Entry>>> initialState )
  {
    _initialState = Objects.requireNonNull( initialState );
  }

  @Nonnull
  public Map<Scope, Map<String, Map<String, Entry>>> getState()
  {
    return _state;
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public void scheduleCommit( @Nonnull final SafeProcedure commitTriggerAction )
  {
    commitTriggerAction.call();
  }

  @Override
  public void commit( @Nonnull final Map<Scope, Map<String, Map<String, Entry>>> state )
  {
    _state.clear();
    _state.putAll( state );
  }

  @Override
  public void restore( @Nonnull final Map<Scope, Map<String, Map<String, Entry>>> state )
  {
    state.clear();
    state.putAll( _initialState );
  }

  @Nonnull
  @Override
  public Object encodeState( @Nonnull final Map<String, Object> state,
                             @Nonnull final TypeConverter converter )
  {
    return state;
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  @Override
  public Map<String, Object> decodeState( @Nonnull final Object encoded,
                                          @Nonnull final TypeConverter converter )
  {
    return (Map<String, Object>) encoded;
  }
}
