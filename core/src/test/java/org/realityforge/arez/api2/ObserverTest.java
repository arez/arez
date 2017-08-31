package org.realityforge.arez.api2;

import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserverTest
  extends AbstractArezTest
{
  @Test
  public void initialState()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final int nextNodeId = context.currentNextNodeId();
    final Observer observer = new Observer( context, name );

    // Verify all "Node" behaviour
    assertEquals( observer.getContext(), context );
    assertEquals( observer.getName(), name );
    assertEquals( observer.getId(), nextNodeId );
    assertEquals( observer.toString(), name );

    // Starts out inactive and inactive means no dependencies
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.isActive(), false );
    assertEquals( observer.isInactive(), true );
    assertEquals( observer.getDependencies().size(), 0 );

    // All the hooks start out null
    assertEquals( observer.getOnActivate(), null );
    assertEquals( observer.getOnDeactivate(), null );
    assertEquals( observer.getOnStale(), null );

    final Action onActivate = () -> {
    };
    final Action onDeactivate = () -> {
    };
    final Action onStale = () -> {
    };

    // Ensure hooks can be modified
    observer.setOnActivate( onActivate );
    observer.setOnDeactivate( onDeactivate );
    observer.setOnStale( onStale );

    assertEquals( observer.getOnActivate(), onActivate );
    assertEquals( observer.getOnDeactivate(), onDeactivate );
    assertEquals( observer.getOnStale(), onStale );

    observer.invariantState();
  }
}
