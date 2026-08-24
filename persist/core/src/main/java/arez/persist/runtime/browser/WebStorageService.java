package arez.persist.runtime.browser;

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
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;
import jsinterop.base.JsPropertyMap;

/**
 * A StorageService that stores state as a single json blob in either the local or session storage of a browser.
 */
final class WebStorageService
  implements StorageService
{
  @JsFunction
  private interface BeforeUnloadEventListener
  {
    void handleEvent( Object event );
  }

  @JsFunction
  private interface IdleRequestCallback
  {
    void onInvoke( Object deadline );
  }

  @JsType( isNative = true, name = "Storage", namespace = JsPackage.GLOBAL )
  private static class Storage
  {
    @JsMethod
    native String getItem( String key );

    @JsMethod
    native void setItem( String key, String value );

    @JsMethod
    native void removeItem( String key );
  }

  @JsType( isNative = true, name = "JSON", namespace = JsPackage.GLOBAL )
  private static final class NativeJSON
  {
    @JsMethod
    private static native Any parse( String value );

    @JsMethod
    private static native String stringify( Object value );
  }

  @JsType( isNative = true, name = "Object", namespace = JsPackage.GLOBAL )
  private static final class NativeObject
  {
    @JsMethod
    private static native JsArrayLike<String> keys( Object value );
  }

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
    return new WebStorageService( sessionStorage(), persistenceKey );
  }

  @Nonnull
  static WebStorageService createLocalStorageService( @Nonnull final String persistenceKey )
  {
    return new WebStorageService( localStorage(), persistenceKey );
  }

  private WebStorageService( @Nonnull final Storage storage, @Nonnull final String address )
  {
    _storage = Objects.requireNonNull( storage );
    _address = Objects.requireNonNull( address );
    // It should be noted that we don't
    addEventListener( "beforeunload", _beforeUnloadListener );
  }

  @Override
  public void dispose()
  {
    if ( 0 != _idleCallbackId )
    {
      cancelIdleCallback( _idleCallbackId );
      _idleCallbackId = 0;
    }
    removeEventListener( "beforeunload", _beforeUnloadListener );
  }

  @Override
  public void scheduleCommit( @Nonnull final SafeProcedure commitTriggerAction )
  {
    _commitTriggerAction = commitTriggerAction;
    // An alternative strategy is to send a message to a WebWorker containing the
    // state to save and performing the save in the other thread but we have yet
    // to see a scenario where performance requirements would warrant the extra complexity
    _idleCallbackId = requestIdleCallback( t -> commitTriggerAction.call() );
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
    if ( 0 == NativeObject.keys( data ).getLength() )
    {
      _storage.removeItem( _address );
    }
    else
    {
      _storage.setItem( _address, NativeJSON.stringify( data ) );
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
    final JsArrayLike<String> keys = NativeObject.keys( encoded );
    final int keyCount = keys.getLength();
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
      final Any value = NativeJSON.parse( item );
      assert null != value;
      final JsPropertyMap<Object> scopes = value.cast();
      final JsArrayLike<String> scopeNames = NativeObject.keys( scopes );
      final int scopeCount = scopeNames.getLength();
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
    final JsArrayLike<String> typeNames = NativeObject.keys( types );
    final int typeCount = typeNames.getLength();
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
    final JsArrayLike<String> ids = NativeObject.keys( idMap );
    final int idCount = ids.getLength();
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

  @JsProperty( name = "sessionStorage", namespace = JsPackage.GLOBAL )
  private static native Storage sessionStorage();

  @JsProperty( name = "localStorage", namespace = JsPackage.GLOBAL )
  private static native Storage localStorage();

  @JsMethod( name = "addEventListener", namespace = JsPackage.GLOBAL )
  private static native void addEventListener( String type, BeforeUnloadEventListener listener );

  @JsMethod( name = "removeEventListener", namespace = JsPackage.GLOBAL )
  private static native void removeEventListener( String type, BeforeUnloadEventListener listener );

  @JsMethod( name = "requestIdleCallback", namespace = JsPackage.GLOBAL )
  private static native int requestIdleCallback( IdleRequestCallback callback );

  @JsMethod( name = "cancelIdleCallback", namespace = JsPackage.GLOBAL )
  private static native void cancelIdleCallback( int callbackId );
}
