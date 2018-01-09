package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.Injectible;
import arez.annotations.Observable;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TypeVisibilityTest
{
  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent( dagger = Injectible.ENABLE )
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

  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent( dagger = Injectible.ENABLE )
  public static class PersonModel2
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
    throws Exception
  {
    assertFalse( Modifier.isPublic( TypeVisibilityTest_Arez_PersonModel.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( TypeVisibilityTest_Arez_PersonModel2.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( TypeVisibilityTest_PersonModelDaggerModule.class.getModifiers() ) );
    assertTrue( Modifier.isPublic( TypeVisibilityTest_PersonModel2DaggerModule.class.getModifiers() ) );
  }
}
