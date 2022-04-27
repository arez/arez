package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class TypeVisibilityTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class PersonModel
  {
    PersonModel()
    {
    }

    @Observable
    @Nonnull
    String getFirstName()
    {
      return "";
    }

    void setFirstName( @Nonnull final String firstName )
    {
    }
  }

  @ArezComponent
  public static abstract class PersonModel2
  {
    @Observable
    @Nonnull
    String getFirstName()
    {
      return "";
    }

    void setFirstName( @Nonnull final String firstName )
    {
    }
  }

  @Test
  public void typeVisibilities()
  {
    assertFalse( Modifier.isPublic( TypeVisibilityTest_Arez_PersonModel.class.getModifiers() ) );
    assertFalse( Modifier.isPublic( TypeVisibilityTest_Arez_PersonModel2.class.getModifiers() ) );
  }
}
