package arez.persist.runtime.browser;

import akasha.BeforeUnloadEventListener;
import akasha.Storage;
import akasha.WindowGlobal;
import akasha.core.JSON;
import akasha.core.JsObject;
import akasha.lang.JsArray;
import arez.SafeProcedure;
import arez.persist.runtime.ArezPersist;
import arez.persist.runtime.Scope;
import arez.persist.runtime.StorageService;
import arez.persist.runtime.TypeConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * A StorageService that stores state as a single json blob in either the local or session storage of a browser.
 */
final class WebStorageService
  implements StorageService
{
  /**
   * A reference to the "beforeunload" listener so that the listener can be removed on disposed.
   */
  @Nonnull
  private final BeforeUnloadEventListener _beforeUnloadListener = e -> maybeCommit();
  /**
   * The browsers storage api targeted by the service.
   */
  @Nonnull
  private final Storage _storage;
  /**
   * The key used to store/access state in storage api.
   */
  @Nonnull
  private final String _address;
  /**
   * A cached copy of last trigger action supplied. Used to try and trigger save before app unloads.
   */
  @Nullable
  private SafeProcedure _commitTriggerAction;
  private int _idleCallbackId;

  @Nonnull
  static WebStorageService createSessionStorageService( @Nonnull final String persistenceKey )
  {
    return new WebStorageService( WindowGlobal.sessionStorage(), persistenceKey );
  }

  @Nonnull
  static WebStorageService createLocalStorageService( @Nonnull final String persistenceKey )
  {
    return new WebStorageService( WindowGlobal.localStorage(), persistenceKey );
  }

  private WebStorageService( @Nonnull final Storage storage, @Nonnull final String address )
  {
    _storage = Objects.requireNonNull( storage );
    _address = Objects.requireNonNull( address );
    // It should be noted that we don't
    WindowGlobal.addBeforeunloadListener( _beforeUnloadListener );
  }

  @Override
  public void dispose()
  {
    if ( 0 != _idleCallbackId )
    {
      WindowGlobal.cancelIdleCallback( _idleCallbackId );
      _idleCallbackId = 0;
    }
    WindowGlobal.removeBeforeunloadListener(  _beforeUnloadListener );
  }

  @Override
  public void scheduleCommit( @Nonnull final SafeProcedure commitTriggerAction )
  {
    _commitTriggerAction = commitTriggerAction;
    // An alternative strategy is to send a message to a WebWorker containing the
    // state to save and performing the save in the other thread but we have yet
    // to see a scenario where performance requirements would warrant the extra complexity
    _idleCallbackId = WindowGlobal.requestIdleCallback( t -> commitTriggerAction.call() );
  }

  @Override
  public void commit( @Nonnull final Map<Scope, Map<String, Map<String, Entry>>> state )
  {
    _idleCallbackId = 0;
    final JsPropertyMap<Object> data = JsPropertyMap.of();
    for ( final Map.Entry<Scope, Map<String, Map<String, Entry>>> scopeEntry : state.entrySet() )
    {
      final JsPropertyMap<Object> scope = JsPropertyMap.of();
      final Set<Map.Entry<String, Map<String, Entry>>> entries = scopeEntry.getValue().entrySet();
      if ( !entries.isEmpty() )
      {
        for ( final Map.Entry<String, Map<String, Entry>> entry : entries )
        {
          final JsPropertyMap<Object> type = JsPropertyMap.of();
          for ( final Map.Entry<String, Entry> instance : entry.getValue().entrySet() )
          {
            type.set( instance.getKey(), instance.getValue().getEncoded() );
          }
          scope.set( entry.getKey(), type );
        }
        data.set( scopeEntry.getKey().getQualifiedName(), scope );
      }
    }
    if ( 0 == JsObject.keys( data ).length )
    {
      _storage.removeItem( _address );
    }
    else
    {
      _storage.setItem( _address, JSON.stringify( data ) );
    }
  }

  @Nonnull
  @Override
  public Object encodeState( @Nonnull final Map<String, Object> state, @Nonnull final TypeConverter converter )
  {
    final JsPropertyMap<Object> encoded = JsPropertyMap.of();
    for ( final Map.Entry<String, Object> entry : state.entrySet() )
    {
      final String key = entry.getKey();
      encoded.set( key, converter.encode( key, entry.getValue() ) );
    }
    return encoded;
  }

  @Nonnull
  @Override
  public Map<String, Object> decodeState( @Nonnull final Object encoded, @Nonnull final TypeConverter converter )
  {
    final JsPropertyMap<Object> propertyMap = Js.cast( encoded );
    final Map<String, Object> data = new HashMap<>();
    final JsArray<String> keys = JsObject.keys( encoded );
    final int keyCount = keys.length;
    for ( int i = 0; i < keyCount; i++ )
    {
      final String key = keys.getAt( i );
      data.put( key, converter.decode( key, propertyMap.get( key ) ) );
    }
    return data;
  }

  @Override
  public void restore( @Nonnull final Map<Scope, Map<String, Map<String, Entry>>> state )
  {
    final String item = _storage.getItem( _address );
    if ( null != item )
    {
      final Any value = JSON.parse( item );
      assert null != value;
      final JsPropertyMap<Object> scopes = value.cast();
      final JsArray<String> scopeNames = JsObject.keys( scopes );
      final int scopeCount = scopeNames.length;
      for ( int s = 0; s < scopeCount; s++ )
      {
        final String scopeName = scopeNames.getAt( s );
        restoreScope( state, scopeName, scopes.getAsAny( scopeName ).asPropertyMap() );
      }
    }
  }

  private void restoreScope( @Nonnull final Map<Scope, Map<String, Map<String, Entry>>> state,
                             @Nonnull final String scopeName,
                             @Nonnull final JsPropertyMap<Object> types )
  {
    final JsArray<String> typeNames = JsObject.keys( types );
    final int typeCount = typeNames.length;
    for ( int i = 0; i < typeCount; i++ )
    {
      final String typeName = typeNames.getAt( i );
      restoreType( state, scopeName, typeName, types.getAsAny( typeName ).asPropertyMap() );
    }
  }

  private void restoreType( @Nonnull final Map<Scope, Map<String, Map<String, Entry>>> state,
                            @Nonnull final String scopeName,
                            @Nonnull final String typeName,
                            @Nonnull final JsPropertyMap<Object> idMap )
  {
    final Scope scope = ArezPersist.findOrCreateScope( scopeName );
    final Map<String, Entry> entryMap = new HashMap<>();
    final JsArray<String> ids = JsObject.keys( idMap );
    final int idCount = ids.length;
    for ( int j = 0; j < idCount; j++ )
    {
      final String id = ids.getAt( j );
      final JsPropertyMap<Object> encoded = Js.uncheckedCast( idMap.get( id ) );
      entryMap.put( id, new StorageService.Entry( null, encoded ) );
    }
    state.computeIfAbsent( scope, s -> new HashMap<>() ).put( typeName, entryMap );
  }

  /**
   * If we have been supplied an action before, try to trigger a commit in case changes are in progress.
   */
  private void maybeCommit()
  {
    if ( null != _commitTriggerAction )
    {
      _commitTriggerAction.call();
    }
  }
}
