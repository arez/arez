package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TypeVisibilityTest
{
  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent
  public static class PersonModel
  {
    PersonModel()
    {
    }

    @Observable
    @Nonnull
    public String getFirstName()
    {
      return "";
    }

    public void setFirstName( @Nonnull final String firstName )
    {
    }
  }

  @Test
  public void equalsAndHashBasedOnId()
    throws Exception
  {
    assertFalse( Modifier.isPublic( TypeVisibilityTest_Arez_PersonModel.class.getModifiers() ) );
  }
}
