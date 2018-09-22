package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EqualsAndHashCodeTest
  extends AbstractArezIntegrationTest
{
  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent
  public static abstract class PersonModel
  {
    @Nonnull
    private String _firstName;

    @Nonnull
    public static PersonModel create( @Nonnull final String firstName )
    {
      return new EqualsAndHashCodeTest_Arez_PersonModel( firstName );
    }

    PersonModel( @Nonnull final String firstName )
    {
      _firstName = firstName;
    }

    @Observable
    @Nonnull
    public String getFirstName()
    {
      return _firstName;
    }

    public void setFirstName( @Nonnull final String firstName )
    {
      _firstName = firstName;
    }
  }

  @Test
  public void equalsAndHashBasedOnId()
  {
    final PersonModel person1 = PersonModel.create( ValueUtil.randomString() );
    final PersonModel person2 = PersonModel.create( ValueUtil.randomString() );
    final PersonModel person3 =
      safeAction( () -> PersonModel.create( person1.getFirstName() ) );

    assertEquals( person1.hashCode(), person1.hashCode() );
    assertNotEquals( person1.hashCode(), person2.hashCode() );
    assertNotEquals( person1.hashCode(), person3.hashCode() );

    //noinspection EqualsWithItself
    assertEquals( person1, person1 );
    assertNotEquals( person1, person2 );
    assertNotEquals( person1, person3 );
  }
}
