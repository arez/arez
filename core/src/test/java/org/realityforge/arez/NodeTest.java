package org.realityforge.arez;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NodeTest
  extends AbstractArezTest
{
  static class TestNode
    extends Node
  {
    TestNode( @Nonnull final ArezContext context,
              @Nullable final String name )
    {
      super( context, name );
    }
  }

  @Test
  public void basicOperation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final int nextNodeId = context.currentNextNodeId();

    final TestNode node = new TestNode( context, name );
    assertEquals( node.getId(), nextNodeId );
    assertEquals( node.getContext(), context );
    assertEquals( node.getName(), name );
    assertEquals( node.toString(), name );

    assertEquals( context.currentNextNodeId(), nextNodeId + 1 );
  }

  @Test
  public void noNameSuppliedWhenNamesDisabled()
    throws Exception
  {
    getConfigProvider().setEnableNames( false );

    final ArezContext context = new ArezContext();
    final int nextNodeId = context.currentNextNodeId();

    final TestNode node = new TestNode( context, null );
    assertEquals( node.getId(), nextNodeId );
    assertEquals( node.getContext(), context );
    assertThrows( node::getName );
    assertTrue( node.toString().startsWith( node.getClass().getName() + "@" ), "node.toString() == " + node );

    assertEquals( context.currentNextNodeId(), nextNodeId + 1 );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
    throws Exception
  {
    getConfigProvider().setEnableNames( false );

    final ArezContext context = new ArezContext();
    final int nextNodeId = context.currentNextNodeId();

    assertThrows( () -> new TestNode( context, ValueUtil.randomString() ) );

    assertEquals( context.currentNextNodeId(), nextNodeId );
  }
}
