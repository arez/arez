package arez.persist.runtime;

import arez.ArezTestUtil;
import arez.Disposable;
import arez.component.Identifiable;
import arez.persist.AbstractTest;
import java.util.Collections;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class ScopeTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final Scope parent = ArezPersist.getRootScope();
    final String name = ValueUtil.randomString();

    assertEquals( parent.getNestedScopes().size(), 0 );

    assertNull( parent.findScope( name ) );

    assertEquals( parent.getNestedScopes().size(), 0 );

    final Scope scope = parent.findOrCreateScope( name );

    assertEquals( parent.getNestedScopes().size(), 1 );
    assertTrue( parent.getNestedScopes().contains( scope ) );

    assertEquals( parent.findOrCreateScope( name ), scope );
    assertEquals( ArezPersist.findScope( name ), scope );
    assertEquals( ArezPersist.findOrCreateScope( name ), scope );

    assertEquals( parent.getNestedScopes().size(), 1 );
    assertTrue( parent.getNestedScopes().contains( scope ) );

    assertEquals( scope.getName(), name );
    assertEquals( scope.getQualifiedName(), name );
    assertEquals( Identifiable.getArezId( scope ), name );
    assertEquals( scope.toString(), name );
    assertFalse( Disposable.isDisposed( scope ) );
    assertEquals( scope.getNestedScopes().size(), 0 );

    ArezPersist.disposeScope( scope );

    assertEquals( parent.getNestedScopes().size(), 0 );
    assertTrue( Disposable.isDisposed( scope ) );
  }

  @Test
  public void findOrCreateScope_qualified()
  {
    final Scope root = ArezPersist.getRootScope();

    assertEquals( ArezPersist.findScope( Scope.ROOT_SCOPE_NAME ), root );
    assertEquals( ArezPersist.findOrCreateScope( Scope.ROOT_SCOPE_NAME ), root );

    final String name1 = "A.B.C";
    final String name2 = "A.B.D";
    final String name3 = "A.B";
    final String name4 = "A.1.D";

    assertEquals( root.getNestedScopes().size(), 0 );

    assertNull( ArezPersist.findScope( name1 ) );
    assertNull( ArezPersist.findScope( name2 ) );
    assertNull( ArezPersist.findScope( name3 ) );
    assertNull( ArezPersist.findScope( name4 ) );

    assertEquals( root.getNestedScopes().size(), 0 );

    final Scope scope1 = ArezPersist.findOrCreateScope( name1 );
    final Scope scope3 = scope1.getParent();

    assertNotNull( scope3 );

    assertEquals( ArezPersist.findScope( name1 ), scope1 );
    assertNull( ArezPersist.findScope( name2 ) );
    assertEquals( ArezPersist.findScope( name3 ), scope3 );
    assertNull( ArezPersist.findScope( name4 ) );

    assertEquals( root.getNestedScopes().size(), 1 );
    assertEquals( scope1.getNestedScopes().size(), 0 );
    assertEquals( scope3.getNestedScopes().size(), 1 );

    final Scope scope2 = ArezPersist.findOrCreateScope( name2 );

    assertEquals( ArezPersist.findScope( name1 ), scope1 );
    assertEquals( ArezPersist.findScope( name2 ), scope2 );
    assertEquals( ArezPersist.findScope( name3 ), scope3 );
    assertNull( ArezPersist.findScope( name4 ) );

    assertEquals( root.getNestedScopes().size(), 1 );
    assertEquals( scope1.getNestedScopes().size(), 0 );
    assertEquals( scope2.getNestedScopes().size(), 0 );
    assertEquals( scope3.getNestedScopes().size(), 2 );
  }

  @Test
  public void toString_output()
  {
    final Scope parent = ArezPersist.getRootScope();
    final String name = ValueUtil.randomString();

    final Scope scope = parent.findOrCreateScope( name );
    assertEquals( scope.toString(), name );

    ArezTestUtil.disableNames();

    assertDefaultToString( scope );
  }

  @Test
  public void findOrCreateScope_on_disposed()
  {
    final Scope parent = ArezPersist.getRootScope();
    final String name1 = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();

    final Scope scope = parent.findOrCreateScope( name1 );

    ArezPersist.disposeScope( scope );

    assertEquals( parent.getNestedScopes().size(), 0 );
    assertTrue( Disposable.isDisposed( scope ) );

    assertInvariantFailure( () -> scope.findOrCreateScope( name2 ),
                            "findOrCreateScope() invoked on disposed scope named '" + name1 + "'" );
  }

  @Test
  public void findOrCreateScope_emptyName()
  {
    final Scope parent = ArezPersist.getRootScope();

    assertInvariantFailure( () -> parent.findOrCreateScope( "" ),
                            "findOrCreateScope() invoked with name '' but the name has invalid characters. Names must contain alphanumeric characters, '-' or '_'" );
  }

  @Test
  public void findOrCreateScope_invalidName()
  {
    final Scope parent = ArezPersist.getRootScope();

    assertInvariantFailure( () -> parent.findOrCreateScope( " * -jhsagdjhg2" ),
                            "findOrCreateScope() invoked with name ' * -jhsagdjhg2' but the name has invalid characters. Names must contain alphanumeric characters, '-' or '_'" );
  }

  @Test
  public void releaseScope()
  {
    final TypeConverter converter = new TypeConverter( Collections.emptyMap() );

    final Scope parent = ArezPersist.getRootScope();

    final Scope scope = parent.findOrCreateScope( ValueUtil.randomString() );
    final Scope childScope = scope.findOrCreateScope( ValueUtil.randomString() );
    final Scope peerScope = parent.findOrCreateScope( ValueUtil.randomString() );

    assertEquals( parent.getNestedScopes().size(), 2 );
    assertTrue( parent.getNestedScopes().contains( scope ) );
    assertTrue( parent.getNestedScopes().contains( peerScope ) );

    final String storeName = ValueUtil.randomString();
    ArezPersist.registerStore( storeName, new NoopStorageService() );
    final Store store = ArezPersist.getStore( storeName );

    final String type = ValueUtil.randomString();
    final String id1 = ValueUtil.randomString();
    final String id2 = ValueUtil.randomString();
    final String id3 = ValueUtil.randomString();
    final String id4 = ValueUtil.randomString();

    store.save( parent, type, id1, randomState(), converter );
    store.save( scope, type, id2, randomState(), converter );
    store.save( childScope, type, id3, randomState(), converter );
    store.save( peerScope, type, id4, randomState(), converter );

    assertNotNull( store.get( parent, type, id1, converter ) );
    assertNotNull( store.get( scope, type, id2, converter ) );
    assertNotNull( store.get( childScope, type, id3, converter ) );
    assertNotNull( store.get( peerScope, type, id4, converter ) );

    ArezPersist.releaseScope( scope );

    assertNotNull( store.get( parent, type, id1, converter ) );
    assertNull( store.get( scope, type, id2, converter ) );
    assertNull( store.get( childScope, type, id3, converter ) );
    assertNotNull( store.get( peerScope, type, id4, converter ) );

    ArezPersist.releaseScope( scope );

    assertNotNull( store.get( parent, type, id1, converter ) );
    assertNull( store.get( scope, type, id2, converter ) );
    assertNull( store.get( childScope, type, id3, converter ) );
    assertNotNull( store.get( peerScope, type, id4, converter ) );

    ArezPersist.releaseScope( parent );

    assertNull( store.get( parent, type, id1, converter ) );
    assertNull( store.get( scope, type, id2, converter ) );
    assertNull( store.get( childScope, type, id3, converter ) );
    assertNull( store.get( peerScope, type, id4, converter ) );
  }

  @Test
  public void noDisposeRootScope()
  {
    final Scope scope = ArezPersist.getRootScope();

    assertInvariantFailure( () -> ArezPersist.disposeScope( scope ),
                            "disposeScope() invoked with the root scope" );
  }

  @Test
  public void canNotInteractWithDisposedScope()
  {
    final TypeConverter converter = new TypeConverter( Collections.emptyMap() );

    final Scope scope = ArezPersist.getRootScope().findOrCreateScope( ValueUtil.randomString() );
    final String storeName = ValueUtil.randomString();
    ArezPersist.registerStore( storeName, mock( StorageService.class ) );
    final Store store = ArezPersist.getStore( storeName );

    ArezPersist.disposeScope( scope );

    assertInvariantFailure( () -> ArezPersist.disposeScope( scope ),
                            "disposeScope() passed a disposed scope named '" + scope.getName() + "'" );
    assertInvariantFailure( () -> ArezPersist.releaseScope( scope ),
                            "releaseScope() passed a disposed scope named '" + scope.getName() + "'" );
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
}
