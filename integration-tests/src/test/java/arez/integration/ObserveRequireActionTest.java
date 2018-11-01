package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "ResultOfMethodCallIgnored" )
public class ObserveRequireActionTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void accessingObservableOutsideTransactionShouldThrowException()
  {
    final CodeModel
      component = CodeModel.create( ValueUtil.randomString(), ValueUtil.randomString() );
    assertThrows( component::getName );

    safeAction( component::getName );
  }

  @Test
  public void mutatingObservableOutsideTransactionShouldThrowException()
  {
    final CodeModel
      component = CodeModel.create( ValueUtil.randomString(), ValueUtil.randomString() );
    assertThrows( () -> component.setName( "X" ) );

    safeAction( () -> component.setName( "X" ) );
  }

  @Test
  public void accessingMemoizedOutsideTransactionShouldThrowException()
  {
    final CodeModel
      component = CodeModel.create( ValueUtil.randomString(), ValueUtil.randomString() );
    assertThrows( component::getQualifiedName );

    safeAction( component::getQualifiedName );
  }

  @SuppressWarnings( "WeakerAccess" )
  @ArezComponent
  public static abstract class CodeModel
  {
    @Nonnull
    private String _name;
    @Nonnull
    private String _packageName;
    @Nullable
    private String _qualifiedName;

    @Nonnull
    public static CodeModel create( @Nonnull final String packageName, @Nonnull final String name )
    {
      return new ObserveRequireActionTest_Arez_CodeModel( packageName, name );
    }

    CodeModel( @Nonnull final String packageName, @Nonnull final String name )
    {
      _packageName = packageName;
      _name = name;
    }

    @Observable
    @Nonnull
    public String getName()
    {
      return _name;
    }

    public void setName( @Nonnull final String name )
    {
      _name = name;
    }

    @Observable
    @Nonnull
    public String getPackageName()
    {
      return _packageName;
    }

    public void setPackageName( @Nonnull final String packageName )
    {
      _packageName = packageName;
    }

    @Memoize
    @Nonnull
    public String getQualifiedName()
    {
      final String rawQualifiedName = getRawQualifiedName();
      if ( null == rawQualifiedName )
      {
        return getPackageName() + "." + getName();
      }
      else
      {
        return rawQualifiedName;
      }
    }

    @Nullable
    @Observable
    public String getRawQualifiedName()
    {
      return _qualifiedName;
    }

    @Observable( name = "rawQualifiedName" )
    public void setQualifiedName( @Nullable final String qualifiedName )
    {
      _qualifiedName = qualifiedName;
    }
  }
}
