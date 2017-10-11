package org.realityforge.arez.integration;

import org.realityforge.arez.Arez;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EqualsAndHashCodeTest
{
  @Test
  public void equalsAndHashBasedOnId()
  {
    final PersonModel person1 = PersonModel.create( ValueUtil.randomString(), ValueUtil.randomString() );
    final PersonModel person2 = PersonModel.create( ValueUtil.randomString(), ValueUtil.randomString() );
    final PersonModel person3 =
      Arez.context().safeAction( () -> PersonModel.create( person1.getFirstName(), person1.getLastName() ) );

    assertEquals( person1.hashCode(), person1.hashCode() );
    assertNotEquals( person1.hashCode(), person2.hashCode() );
    assertNotEquals( person1.hashCode(), person3.hashCode() );

    //noinspection EqualsWithItself
    assertTrue( person1.equals( person1 ) );
    assertFalse( person1.equals( person2 ) );
    assertFalse( person1.equals( person3 ) );
  }
}
