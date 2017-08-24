package org.realityforge.arez.api2;

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
    final TestNode node = new TestNode( context, name );
    assertEquals( node.getName(), name );
    assertEquals( node.getContext(), context );
    assertEquals( node.toString(), name );
  }

  @Test
  public void noNameSuppliedWhenNamesDisabled()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setEnableNames( false );

    final ArezContext context = new ArezContext();
    final TestNode node = new TestNode( context, null );
    assertThrows( node::getName );
    assertEquals( node.getContext(), context );
    assertTrue( node.toString().startsWith( node.getClass().getName() + "@" ), "node.toString() == " + node );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setEnableNames( false );

    final ArezContext context = new ArezContext();
    assertThrows( () -> new TestNode( context, ValueUtil.randomString() ) );
  }
}
