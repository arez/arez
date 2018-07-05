package arez.component;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentStateTest
  extends AbstractArezComponentTest
{
  @Test
  public void hasBeenInitialized()
  {
    assertFalse( ComponentState.hasBeenInitialized( ComponentState.COMPONENT_CREATED ) );
    assertTrue( ComponentState.hasBeenInitialized( ComponentState.COMPONENT_INITIALIZED ) );
    assertTrue( ComponentState.hasBeenInitialized( ComponentState.COMPONENT_CONSTRUCTED ) );
    assertTrue( ComponentState.hasBeenInitialized( ComponentState.COMPONENT_COMPLETE ) );
    assertTrue( ComponentState.hasBeenInitialized( ComponentState.COMPONENT_READY ) );
    assertTrue( ComponentState.hasBeenInitialized( ComponentState.COMPONENT_DISPOSING ) );
    assertTrue( ComponentState.hasBeenInitialized( ComponentState.COMPONENT_DISPOSED ) );
  }

  @Test
  public void hasBeenConstructed()
  {
    assertFalse( ComponentState.hasBeenConstructed( ComponentState.COMPONENT_CREATED ) );
    assertFalse( ComponentState.hasBeenConstructed( ComponentState.COMPONENT_INITIALIZED ) );
    assertTrue( ComponentState.hasBeenConstructed( ComponentState.COMPONENT_CONSTRUCTED ) );
    assertTrue( ComponentState.hasBeenConstructed( ComponentState.COMPONENT_COMPLETE ) );
    assertTrue( ComponentState.hasBeenConstructed( ComponentState.COMPONENT_READY ) );
    assertTrue( ComponentState.hasBeenConstructed( ComponentState.COMPONENT_DISPOSING ) );
    assertTrue( ComponentState.hasBeenConstructed( ComponentState.COMPONENT_DISPOSED ) );
  }

  @Test
  public void hasBeenCompleted()
  {
    assertFalse( ComponentState.hasBeenCompleted( ComponentState.COMPONENT_CREATED ) );
    assertFalse( ComponentState.hasBeenCompleted( ComponentState.COMPONENT_INITIALIZED ) );
    assertFalse( ComponentState.hasBeenCompleted( ComponentState.COMPONENT_CONSTRUCTED ) );
    assertTrue( ComponentState.hasBeenCompleted( ComponentState.COMPONENT_COMPLETE ) );
    assertTrue( ComponentState.hasBeenCompleted( ComponentState.COMPONENT_READY ) );
    assertTrue( ComponentState.hasBeenCompleted( ComponentState.COMPONENT_DISPOSING ) );
    assertTrue( ComponentState.hasBeenCompleted( ComponentState.COMPONENT_DISPOSED ) );
  }

  @Test
  public void isActive()
  {
    assertFalse( ComponentState.isActive( ComponentState.COMPONENT_CREATED ) );
    assertFalse( ComponentState.isActive( ComponentState.COMPONENT_INITIALIZED ) );
    assertTrue( ComponentState.isActive( ComponentState.COMPONENT_CONSTRUCTED ) );
    assertTrue( ComponentState.isActive( ComponentState.COMPONENT_COMPLETE ) );
    assertTrue( ComponentState.isActive( ComponentState.COMPONENT_READY ) );
    assertFalse( ComponentState.isActive( ComponentState.COMPONENT_DISPOSING ) );
    assertFalse( ComponentState.isActive( ComponentState.COMPONENT_DISPOSED ) );
  }

  @Test
  public void isDisposingOrDisposed()
  {
    assertFalse( ComponentState.isDisposingOrDisposed( ComponentState.COMPONENT_CREATED ) );
    assertFalse( ComponentState.isDisposingOrDisposed( ComponentState.COMPONENT_INITIALIZED ) );
    assertFalse( ComponentState.isDisposingOrDisposed( ComponentState.COMPONENT_CONSTRUCTED ) );
    assertFalse( ComponentState.isDisposingOrDisposed( ComponentState.COMPONENT_COMPLETE ) );
    assertFalse( ComponentState.isDisposingOrDisposed( ComponentState.COMPONENT_READY ) );
    assertTrue( ComponentState.isDisposingOrDisposed( ComponentState.COMPONENT_DISPOSING ) );
    assertTrue( ComponentState.isDisposingOrDisposed( ComponentState.COMPONENT_DISPOSED ) );
  }

  @Test
  public void describe()
  {
    assertEquals( ComponentState.describe( ComponentState.COMPONENT_CREATED ), "created" );
    assertEquals( ComponentState.describe( ComponentState.COMPONENT_INITIALIZED ), "initialized" );
    assertEquals( ComponentState.describe( ComponentState.COMPONENT_CONSTRUCTED ), "constructed" );
    assertEquals( ComponentState.describe( ComponentState.COMPONENT_COMPLETE ), "complete" );
    assertEquals( ComponentState.describe( ComponentState.COMPONENT_READY ), "ready" );
    assertEquals( ComponentState.describe( ComponentState.COMPONENT_DISPOSING ), "disposing" );
    assertEquals( ComponentState.describe( ComponentState.COMPONENT_DISPOSED ), "disposed" );
    assertThrows( () -> ComponentState.describe( (byte) 120 ) );
  }
}
