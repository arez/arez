package arez;

import javax.annotation.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class NodeTest
  extends AbstractTest
{
  static class TestNode
    extends Node
  {
    TestNode( @Nullable final ArezContext context,
              @Nullable final String name )
    {
      super( context, name );
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public boolean isDisposed()
    {
      return false;
    }
  }

  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final TestNode node = new TestNode( context, name );
    assertEquals( node.getContext(), context );
    assertEquals( node.getName(), name );
    assertEquals( node.toString(), name );
  }

  @Test
  public void noNameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();

    final TestNode node = new TestNode( context, null );
    assertEquals( node.getContext(), context );
    assertDefaultToString( node );

    assertInvariantFailure( node::getName, "Arez-0053: Node.getName() invoked when Arez.areNamesEnabled() is false" );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    assertInvariantFailure( () -> new TestNode( context, name ),
                            "Arez-0052: Node passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void contextSuppliedWhenZonesDisabled()
  {
    ArezTestUtil.disableZones();

    final ArezContext context = Arez.context();

    assertInvariantFailure( () -> new TestNode( context, ValueUtil.randomString() ),
                            "Arez-0180: Node passed a context but Arez.areZonesEnabled() is false" );
  }
}
