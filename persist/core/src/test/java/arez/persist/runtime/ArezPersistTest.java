package arez.persist.runtime;

import arez.persist.AbstractTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ArezPersistTest
  extends AbstractTest
{
  @Test
  public void isApplicationScopedPersistenceEnabled()
  {
    assertTrue( ArezPersist.isApplicationStoreEnabled() );
    ArezPersistTestUtil.disableApplicationStore();
    assertFalse( ArezPersist.isApplicationStoreEnabled() );
  }

  @Test
  public void shouldCheckApiInvariants()
  {
    assertTrue( ArezPersist.shouldCheckApiInvariants() );
    ArezPersistTestUtil.noCheckApiInvariants();
    assertFalse( ArezPersist.shouldCheckApiInvariants() );
  }
}
