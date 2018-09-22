package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TypeVisibilityTest
  extends AbstractArezIntegrationTest
{
  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent( dagger = Feature.ENABLE )
  public static abstract class PersonModel
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

  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent( dagger = Feature.ENABLE )
  public static abstract class PersonModel2
  {
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
  public void typeVisibilities()
  {
    assertFalse( Modifier.isPublic( TypeVisibilityTest_Arez_PersonModel.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( TypeVisibilityTest_Arez_PersonModel2.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( TypeVisibilityTest_PersonModelDaggerModule.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( TypeVisibilityTest_PersonModel2DaggerModule.class.getModifiers() ) );
  }
}
