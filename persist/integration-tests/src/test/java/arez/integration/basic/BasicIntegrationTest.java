package arez.integration.basic;

import arez.Arez;
import arez.integration.AbstractIntegrationTest;
import arez.persist.StoreTypes;
import arez.persist.runtime.ArezPersist;
import arez.persist.runtime.Scope;
import arez.persist.runtime.StorageService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class BasicIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void scenario()
  {
    // a super simple scenario where we model a tree and persist expanded state of tree

    final String type = "TreeNode";
    final Map<Scope, Map<String, Map<String, StorageService.Entry>>> initialState = new HashMap<>();
    final Map<String, StorageService.Entry> typeMap =
      initialState
        .computeIfAbsent( ArezPersist.getRootScope(), s -> new HashMap<>() )
        .computeIfAbsent( type, t -> new HashMap<>() );
    expandTreeNode( typeMap, "0" );
    expandTreeNode( typeMap, "0-0" );
    expandTreeNode( typeMap, "0-1" );
    expandTreeNode( typeMap, "0-1-1" );
    expandTreeNode( typeMap, "0-1-2" );
    expandTreeNode( typeMap, "0-1-2-0" );

    final TestStorageService storageService = new TestStorageService( initialState );
    ArezPersist.registerStore( StoreTypes.LOCAL, storageService );

    Arez.context().safeAction( () -> {
      final TreeNodeService service = TreeNodeService.create();

      final TreeNode n0 = service.getRoot();
      final TreeNode n00 = n0.getChildren().get( 0 );
      final TreeNode n01 = n0.getChildren().get( 1 );
      final TreeNode n02 = n0.getChildren().get( 2 );
      final TreeNode n010 = n01.getChildren().get( 0 );
      final TreeNode n011 = n01.getChildren().get( 1 );
      final TreeNode n012 = n01.getChildren().get( 2 );
      final TreeNode n0120 = n012.getChildren().get( 0 );
      final TreeNode n0121 = n012.getChildren().get( 1 );
      final TreeNode n0122 = n012.getChildren().get( 2 );

      assertTrue( n0.isExpanded() );
      assertTrue( n00.isExpanded() );
      assertTrue( n01.isExpanded() );
      assertFalse( n02.isExpanded() );
      assertFalse( n010.isExpanded() );
      assertTrue( n011.isExpanded() );
      assertTrue( n012.isExpanded() );
      assertTrue( n0120.isExpanded() );
      assertFalse( n0121.isExpanded() );
      assertFalse( n0122.isExpanded() );

      n0122.toggleExpanded();

      assertTrue( n0122.isExpanded() );
    } );

    // At the end of the action the transactions resolve and thus a commit
    // should have occurred to storage service

    final StorageService.Entry entry =
      storageService.getState().get( ArezPersist.getRootScope() ).get( type ).get( "0-1-2-2" );
    assertNotNull( entry );
    final Map<String, Object> data = entry.getData();
    assertNotNull( data );
    assertEquals( data.get( "expanded" ), true );
  }

  private void expandTreeNode( @Nonnull final Map<String, StorageService.Entry> typeMap, @Nonnull final String id )
  {
    final Map<String, Object> encoded = new HashMap<>();
    encoded.put( "expanded", true );
    typeMap.put( id, new StorageService.Entry( null, encoded ) );
  }
}
