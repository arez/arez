package arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class StateTest
  extends AbstractArezTest
{
  @Test
  public void isActive()
    throws Exception
  {
    assertFalse( State.isActive( State.STATE_DISPOSED ) );
    assertFalse( State.isActive( State.STATE_DISPOSING ) );
    assertFalse( State.isActive( State.STATE_INACTIVE ) );
    assertTrue( State.isActive( State.STATE_UP_TO_DATE ) );
    assertTrue( State.isActive( State.STATE_POSSIBLY_STALE ) );
    assertTrue( State.isActive( State.STATE_STALE ) );
  }

  @Test
  public void isNotActive()
    throws Exception
  {
    assertTrue( State.isNotActive( State.STATE_DISPOSED ) );
    assertTrue( State.isNotActive( State.STATE_DISPOSING ) );
    assertTrue( State.isNotActive( State.STATE_INACTIVE ) );
    assertFalse( State.isNotActive( State.STATE_UP_TO_DATE ) );
    assertFalse( State.isNotActive( State.STATE_POSSIBLY_STALE ) );
    assertFalse( State.isNotActive( State.STATE_STALE ) );
  }

  @Test
  public void getState()
    throws Exception
  {
    assertEquals( State.getState( Options.PRIORITY_NORMAL | State.STATE_DISPOSED ), State.STATE_DISPOSED );
    assertEquals( State.getState( Options.PRIORITY_NORMAL | State.STATE_DISPOSING ), State.STATE_DISPOSING );
    assertEquals( State.getState( Options.PRIORITY_NORMAL | State.STATE_INACTIVE ), State.STATE_INACTIVE );
    assertEquals( State.getState( Options.PRIORITY_NORMAL | State.STATE_UP_TO_DATE ), State.STATE_UP_TO_DATE );
    assertEquals( State.getState( Options.PRIORITY_NORMAL | State.STATE_POSSIBLY_STALE ), State.STATE_POSSIBLY_STALE );
    assertEquals( State.getState( Options.PRIORITY_NORMAL | State.STATE_STALE ), State.STATE_STALE );
  }

  @Test
  public void setState()
    throws Exception
  {
    assertEquals( State.setState( Options.PRIORITY_NORMAL |
                                  Options.READ_WRITE |
                                  State.STATE_UP_TO_DATE,
                                  State.STATE_DISPOSED ),
                  Options.PRIORITY_NORMAL | Options.READ_WRITE | State.STATE_DISPOSED );
    assertEquals( State.setState( Options.PRIORITY_NORMAL |
                                  Options.READ_WRITE |
                                  State.STATE_UP_TO_DATE,
                                  State.STATE_DISPOSING ),
                  Options.PRIORITY_NORMAL | Options.READ_WRITE | State.STATE_DISPOSING );
    assertEquals( State.setState( Options.PRIORITY_NORMAL |
                                  Options.READ_WRITE |
                                  State.STATE_UP_TO_DATE,
                                  State.STATE_DISPOSING ),
                  Options.PRIORITY_NORMAL | Options.READ_WRITE | State.STATE_DISPOSING );
    assertEquals( State.setState( Options.PRIORITY_NORMAL |
                                  Options.READ_WRITE |
                                  State.STATE_UP_TO_DATE,
                                  State.STATE_INACTIVE ),
                  Options.PRIORITY_NORMAL | Options.READ_WRITE | State.STATE_INACTIVE );
    assertEquals( State.setState( Options.PRIORITY_NORMAL |
                                  Options.READ_WRITE |
                                  State.STATE_UP_TO_DATE,
                                  State.STATE_UP_TO_DATE ),
                  Options.PRIORITY_NORMAL | Options.READ_WRITE | State.STATE_UP_TO_DATE );
    assertEquals( State.setState( Options.PRIORITY_NORMAL |
                                  Options.READ_WRITE |
                                  State.STATE_UP_TO_DATE,
                                  State.STATE_POSSIBLY_STALE ),
                  Options.PRIORITY_NORMAL | Options.READ_WRITE | State.STATE_POSSIBLY_STALE );
    assertEquals( State.setState( Options.PRIORITY_NORMAL |
                                  Options.READ_WRITE |
                                  State.STATE_UP_TO_DATE,
                                  State.STATE_STALE ),
                  Options.PRIORITY_NORMAL | Options.READ_WRITE | State.STATE_STALE );
  }

  @Test
  public void getLeastStaleObserverState()
    throws Exception
  {
    assertEquals( State.getLeastStaleObserverState( Options.PRIORITY_NORMAL | State.STATE_DISPOSED ),
                  State.STATE_UP_TO_DATE );
    assertEquals( State.getLeastStaleObserverState( Options.PRIORITY_NORMAL | State.STATE_DISPOSING ),
                  State.STATE_UP_TO_DATE );
    assertEquals( State.getLeastStaleObserverState( Options.PRIORITY_NORMAL | State.STATE_INACTIVE ),
                  State.STATE_UP_TO_DATE );
    assertEquals( State.getLeastStaleObserverState( Options.PRIORITY_NORMAL | State.STATE_UP_TO_DATE ),
                  State.STATE_UP_TO_DATE );
    assertEquals( State.getLeastStaleObserverState( Options.PRIORITY_NORMAL | State.STATE_POSSIBLY_STALE ),
                  State.STATE_POSSIBLY_STALE );
    assertEquals( State.getLeastStaleObserverState( Options.PRIORITY_NORMAL | State.STATE_STALE ), State.STATE_STALE );
  }

  @Test
  public void getStateName()
    throws Exception
  {
    assertEquals( State.getStateName( State.STATE_DISPOSED ), "DISPOSED" );
    assertEquals( State.getStateName( State.STATE_DISPOSING ), "DISPOSING" );
    assertEquals( State.getStateName( State.STATE_INACTIVE ), "INACTIVE" );
    assertEquals( State.getStateName( State.STATE_UP_TO_DATE ), "UP_TO_DATE" );
    assertEquals( State.getStateName( State.STATE_POSSIBLY_STALE ), "POSSIBLY_STALE" );
    assertEquals( State.getStateName( State.STATE_STALE ), "STALE" );
    // State value should have been passed in
    assertEquals( State.getStateName( Options.PRIORITY_NORMAL | State.STATE_STALE ), "UNKNOWN(100663302)" );
  }
}
