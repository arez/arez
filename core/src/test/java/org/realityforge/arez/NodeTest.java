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

    @Override
    public void dispose()
    {
    }
  }

  @Test
  public void basicOperation()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();

    final TestNode node = new TestNode( context, name );
    assertEquals( node.getContext(), context );
    assertEquals( node.getName(), name );
    assertEquals( node.toString(), name );
  }

  @Test
  public void noNameSuppliedWhenNamesDisabled()
    throws Exception
  {
    getConfigProvider().setEnableNames( false );

    final ArezContext context = new ArezContext();

    final TestNode node = new TestNode( context, null );
    assertEquals( node.getContext(), context );
    assertTrue( node.toString().startsWith( node.getClass().getName() + "@" ), "node.toString() == " + node );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, node::getName );
    assertEquals( exception.getMessage(), "Node.getName() invoked when ArezConfig.enableNames() is false" );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
    throws Exception
  {
    getConfigProvider().setEnableNames( false );

    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> new TestNode( context, name ) );

    assertEquals( exception.getMessage(), "Node passed a name '" + name + "' but ArezConfig.enableNames() is false" );
  }
}
