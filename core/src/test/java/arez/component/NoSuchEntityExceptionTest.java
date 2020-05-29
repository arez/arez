package arez.component;

import arez.AbstractTest;
import arez.ArezTestUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class NoSuchEntityExceptionTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final NoSuchEntityException exception = new NoSuchEntityException( 23 );

    assertEquals( exception.getId(), 23 );
    assertNull( exception.getMessage() );
    assertEquals( exception.toString(), "NoSuchEntityException[id=23]" );
  }

  @Test
  public void toString_NamesDisabled()
  {
    ArezTestUtil.disableNames();
    final NoSuchEntityException exception = new NoSuchEntityException( 23 );

    assertEquals( exception.toString(), "arez.component.NoSuchEntityException" );
  }
}
