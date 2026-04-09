package arez.persist.runtime;

import arez.SafeProcedure;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * A StorageService that stores no state.
 */
final class NoopStorageService
  implements StorageService
{
  @Override
  public void restore( @Nonnull final Map<Scope, Map<String, Map<String, Entry>>> state )
  {
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
    //no-op
  }

  @Nonnull
  @Override
  public Object encodeState( @Nonnull final Map<String, Object> state,
                             @Nonnull final TypeConverter converter )
  {
    // No serialization required as never stored to backend
    return state;
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  @Override
  public Map<String, Object> decodeState( @Nonnull final Object encoded, @Nonnull final TypeConverter converter )
  {
    // No serialization required as never stored to backend
    return (Map<String, Object>) encoded;
  }
}
