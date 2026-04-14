package arez.persist.runtime;

import arez.SafeProcedure;
import arez.persist.AbstractTest;
import arez.persist.StoreTypes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mockito.ArgumentCaptor;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class StoreTest
  extends AbstractTest
{
  @Test
  public void registerApplicationStoreIfEnabled_disabled()
  {
    ArezPersistTestUtil.disableApplicationStore();
    ArezPersistTestUtil.resetState();

    assertTrue( Registry.getStores().isEmpty() );

    ArezPersist.registerApplicationStoreIfEnabled();

    assertTrue( Registry.getStores().isEmpty() );
  }

  @Test
  public void registerApplicationStoreIfEnabled_enabled()
  {
    ArezPersistTestUtil.disableApplicationStore();
    ArezPersistTestUtil.resetState();

    assertTrue( Registry.getStores().isEmpty() );

    ArezPersistTestUtil.enableApplicationStore();
    ArezPersist.registerApplicationStoreIfEnabled();

    assertEquals( Registry.getStores().size(), 1 );

    assertNotNull( ArezPersist.getStore( StoreTypes.APPLICATION ) );
  }

  @Test
  public void get_save_remove_basicOperation()
  {
    final TypeConverter converter = new TypeConverter( Collections.emptyMap() );
    final Store store = ArezPersist.getStore( StoreTypes.APPLICATION );

    final Scope rootScope = ArezPersist.getRootScope();
    final Scope scope = rootScope.findOrCreateScope( ValueUtil.randomString() );

    final String type1 = ValueUtil.randomString();
    final String type2 = ValueUtil.randomString();

    final String id1 = ValueUtil.randomString();
    final String id2 = ValueUtil.randomString();
    final String id3 = ValueUtil.randomString();

    final Map<String, Object> state1 = randomState();
    final Map<String, Object> state2 = randomState();
    final Map<String, Object> state3 = randomState();

    assertNull( store.get( scope, type1, id1, converter ) );
    assertNull( store.get( rootScope, type1, id1, converter ) );
    assertNull( store.get( scope, type1, id2, converter ) );
    assertNull( store.get( rootScope, type1, id2, converter ) );
    assertNull( store.get( scope, type2, id3, converter ) );
    assertNull( store.get( rootScope, type2, id3, converter ) );

    store.save( scope, type1, id1, state1, converter );

    assertEquals( store.get( scope, type1, id1, converter ), state1 );
    assertNull( store.get( rootScope, type1, id1, converter ) );
    assertNull( store.get( scope, type1, id2, converter ) );
    assertNull( store.get( rootScope, type1, id2, converter ) );
    assertNull( store.get( scope, type2, id3, converter ) );
    assertNull( store.get( rootScope, type2, id3, converter ) );

    store.save( scope, type1, id2, state2, converter );
    store.save( rootScope, type2, id3, state3, converter );

    assertEquals( store.get( scope, type1, id1, converter ), state1 );
    assertNull( store.get( rootScope, type1, id1, converter ) );
    assertEquals( store.get( scope, type1, id2, converter ), state2 );
    assertNull( store.get( rootScope, type1, id2, converter ) );
    assertNull( store.get( scope, type2, id3, converter ) );
    assertEquals( store.get( rootScope, type2, id3, converter ), state3 );

    // A save of an empty map is equivalent to a remove
    store.save( scope, type1, id1, Collections.emptyMap(), converter );

    assertNull( store.get( scope, type1, id1, converter ) );
    assertNull( store.get( rootScope, type1, id1, converter ) );
    assertEquals( store.get( scope, type1, id2, converter ), state2 );
    assertNull( store.get( rootScope, type1, id2, converter ) );
    assertNull( store.get( scope, type2, id3, converter ) );
    assertEquals( store.get( rootScope, type2, id3, converter ), state3 );

    store.remove( scope, type1, id1 );
    store.remove( scope, type1, id2 );

    assertNull( store.get( scope, type1, id1, converter ) );
    assertNull( store.get( rootScope, type1, id1, converter ) );
    assertNull( store.get( scope, type1, id2, converter ) );
    assertNull( store.get( rootScope, type1, id2, converter ) );
    assertNull( store.get( scope, type2, id3, converter ) );
    assertEquals( store.get( rootScope, type2, id3, converter ), state3 );

    ArezPersist.disposeScope( scope );

    assertInvariantFailure( () -> store.remove( scope, ValueUtil.randomString(), ValueUtil.randomString() ),
                            "Store.remove() passed a disposed scope named '" + scope.getName() + "'" );
    assertInvariantFailure( () -> store.get( scope, ValueUtil.randomString(), ValueUtil.randomString(), converter ),
                            "Store.get() passed a disposed scope named '" + scope.getName() + "'" );
    assertInvariantFailure( () -> store.save( scope,
                                              ValueUtil.randomString(),
                                              ValueUtil.randomString(),
                                              randomState(),
                                              converter ),
                            "Store.save() passed a disposed scope named '" + scope.getName() + "'" );
  }

  @Test
  public void registerAndDergisterStoreLifecycle()
  {
    ArezPersistTestUtil.disableApplicationStore();
    ArezPersistTestUtil.resetState();

    final String store1Name = ValueUtil.randomString();
    final String store2Name = ValueUtil.randomString();

    final StorageService service1 = mock( StorageService.class );
    final StorageService service2 = mock( StorageService.class );

    assertEquals( Registry.getStores().size(), 0 );

    final SafeProcedure store1Deregister = ArezPersist.registerStore( store1Name, service1 );

    verify( service1 ).restore( any() );
    reset( service1 );

    assertEquals( Registry.getStores().size(), 1 );
    assertNotNull( ArezPersist.getStore( store1Name ) );

    final SafeProcedure store2Deregister = ArezPersist.registerStore( store2Name, service2 );

    verify( service2 ).restore( any() );
    reset( service2 );

    assertEquals( Registry.getStores().size(), 2 );
    assertNotNull( ArezPersist.getStore( store1Name ) );
    assertNotNull( ArezPersist.getStore( store2Name ) );

    assertNotNull( ArezPersist.getStore( store1Name ) );

    store1Deregister.call();

    verify( service1 ).dispose();
    reset( service1 );

    assertEquals( Registry.getStores().size(), 1 );
    assertNotNull( ArezPersist.getStore( store2Name ) );

    store2Deregister.call();

    verify( service2 ).dispose();
    reset( service2 );

    assertEquals( Registry.getStores().size(), 0 );
  }

  @Test
  public void getStoreForNonExistentStore()
  {
    final String name = ValueUtil.randomString();
    assertInvariantFailure( () -> ArezPersist.getStore( name ),
                            "getStore() invoked with name " + name + " but no such store exists" );
  }

  @Test
  public void duplicateRegister()
  {
    final String name = ValueUtil.randomString();
    ArezPersist.registerStore( name, mock( StorageService.class ) );
    assertInvariantFailure( () -> ArezPersist.registerStore( name, mock( StorageService.class ) ),
                            "registerStore() invoked with name '" + name +
                            "' but a store is already registered with that name" );
  }

  @Test
  public void canNotInteractWithDisposedStore()
  {
    final TypeConverter converter = new TypeConverter( Collections.emptyMap() );
    final Scope scope = ArezPersist.getRootScope();
    final String storeName = ValueUtil.randomString();
    final SafeProcedure disposeAction = ArezPersist.registerStore( storeName, mock( StorageService.class ) );
    final Store store = ArezPersist.getStore( storeName );
    disposeAction.call();

    assertInvariantFailure( () -> store.remove( scope, ValueUtil.randomString(), ValueUtil.randomString() ),
                            "Store.remove() invoked after the store has been disposed" );
    assertInvariantFailure( () -> store.get( scope, ValueUtil.randomString(), ValueUtil.randomString(), converter ),
                            "Store.get() invoked after the store has been disposed" );
    assertInvariantFailure( () -> store.save( scope,
                                              ValueUtil.randomString(),
                                              ValueUtil.randomString(),
                                              randomState(),
                                              converter ),
                            "Store.save() invoked after the store has been disposed" );
  }

  @Test
  public void basicOperationAndInteractionWithStorageService()
  {
    final TypeConverter converter = new TypeConverter( Collections.emptyMap() );
    final ArgumentCaptor<SafeProcedure> actionCaptor = ArgumentCaptor.forClass( SafeProcedure.class );

    final StorageService service = mock( StorageService.class );

    final String storeName = ValueUtil.randomString();
    ArezPersist.registerStore( storeName, service );
    final Store store = ArezPersist.getStore( storeName );

    verify( service ).restore( any() );

    reset( service );

    final Scope scope = ArezPersist.getRootScope();

    final String type = ValueUtil.randomString();

    final String id = ValueUtil.randomString();
    final String id2 = ValueUtil.randomString();

    final Map<String, Object> state1 = randomState();
    final Map<String, Object> state2 = randomState();

    assertNull( store.get( scope, type, id, converter ) );
    assertNull( store.get( scope, type, id2, converter ) );

    verifyNoInteractions( service );

    when( service.encodeState( eq( state1 ), eq( converter ) ) ).thenReturn( state1 );

    store.save( scope, type, id, state1, converter );

    verify( service ).encodeState( eq( state1 ), eq( converter ) );
    verify( service ).scheduleCommit( actionCaptor.capture() );
    verifyNoMoreInteractions( service );

    reset( service );

    assertEquals( store.get( scope, type, id, converter ), state1 );
    assertNull( store.get( scope, type, id2, converter ) );

    verifyNoInteractions( service );

    when( service.encodeState( eq( state2 ), eq( converter ) ) ).thenReturn( state2 );

    store.save( scope, type, id, state2, converter );

    verify( service ).encodeState( eq( state2 ), eq( converter ) );
    verify( service, never() ).scheduleCommit( any() );
    verifyNoMoreInteractions( service );

    reset( service );

    assertEquals( store.get( scope, type, id, converter ), state2 );
    assertNull( store.get( scope, type, id2, converter ) );

    verifyNoInteractions( service );

    final SafeProcedure action = actionCaptor.getValue();

    // Now perform the commit

    action.call();

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    final ArgumentCaptor<Map<Scope, Map<String, Map<String, StorageService.Entry>>>> stateCaptor =
      (ArgumentCaptor) ArgumentCaptor.forClass( Map.class );

    verify( service ).commit( stateCaptor.capture() );

    final Map<Scope, Map<String, Map<String, StorageService.Entry>>> stateSaved = stateCaptor.getValue();
    final Map<String, Map<String, StorageService.Entry>> scopeMap = stateSaved.get( scope );
    assertNotNull( scopeMap );
    final Map<String, StorageService.Entry> typeMap = scopeMap.get( type );
    assertNotNull( typeMap );
    final StorageService.Entry instanceEntry = typeMap.get( id );
    assertEquals( instanceEntry.getData(), state2 );
    assertEquals( instanceEntry.getEncoded(), state2 );

    reset( service );

    // Duplicate commit ignored as already committed an no remaining changes
    action.call();

    verifyNoMoreInteractions( service );
  }

  @Test
  public void encodeOnSave()
  {
    final String propertyKey = ValueUtil.randomString();
    final TypeConverter converter = new TypeConverter( Collections.emptyMap() );

    final StorageService service = mock( StorageService.class );

    final String storeName = ValueUtil.randomString();
    ArezPersist.registerStore( storeName, service );
    final Store store = ArezPersist.getStore( storeName );
    reset( service );

    final Scope scope = ArezPersist.getRootScope();

    final String type = ValueUtil.randomString();

    final String id = ValueUtil.randomString();

    final Map<String, Object> state1 = new HashMap<>();
    state1.put( propertyKey, 'a' );
    final Map<String, Object> state1Encoded = new HashMap<>();
    state1Encoded.put( propertyKey, "a" );

    assertNull( store.get( scope, type, id, converter ) );

    verifyNoInteractions( service );

    when( service.encodeState( eq( state1 ), eq( converter ) ) ).thenReturn( state1Encoded );

    store.save( scope, type, id, state1, converter );

    verify( service ).encodeState( eq( state1 ), eq( converter ) );
    verify( service ).scheduleCommit( any() );
    verifyNoMoreInteractions( service );

    reset( service );

    final StorageService.Entry entry = store.getConfig().get( scope ).get( type ).get( id );

    assertNotNull( entry );
    assertEquals( entry.getData(), state1 );
    assertEquals( entry.getEncoded(), state1Encoded );
  }

  @SuppressWarnings( { "unchecked" } )
  @Test
  public void decodeOnGetencodeOnSave()
  {
    final Scope scope = ArezPersist.getRootScope();
    final String type = ValueUtil.randomString();

    final String id = ValueUtil.randomString();

    final String propertyKey = ValueUtil.randomString();

    final Map<String, Object> state1 = new HashMap<>();
    state1.put( propertyKey, 'a' );
    final Map<String, Object> state1Encoded = new HashMap<>();
    state1Encoded.put( propertyKey, "a" );

    final TypeConverter converter = new TypeConverter( Collections.emptyMap() );

    final StorageService service = mock( StorageService.class );

    doAnswer( invocation -> {
      final Map<Scope, Map<String, Map<String, StorageService.Entry>>> state =
        (Map<Scope, Map<String, Map<String, StorageService.Entry>>>) invocation.getArguments()[ 0 ];
      state.computeIfAbsent( scope, scope1 -> new HashMap<>() )
        .computeIfAbsent( type, t -> new HashMap<>() )
        .put( id, new StorageService.Entry( null, state1Encoded ) );
      return null;
    } ).when( service ).restore( any() );

    final String storeName = ValueUtil.randomString();
    ArezPersist.registerStore( storeName, service );
    final Store store = ArezPersist.getStore( storeName );
    verify( service ).restore( any() );
    reset( service );
    final StorageService.Entry entry = store.getConfig().get( scope ).get( type ).get( id );

    assertNotNull( entry );
    assertNull( entry.getData() );
    assertEquals( entry.getEncoded(), state1Encoded );

    doReturn( state1 ).when( service ).decodeState( entry.getEncoded(), converter );

    assertEquals( store.get( scope, type, id, converter ), state1 );

    assertEquals( entry.getData(), state1 );
  }
}
