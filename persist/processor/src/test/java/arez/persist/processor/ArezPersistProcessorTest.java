package arez.persist.processor;

import arez.processor.ArezProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;
import org.realityforge.proton.qa.AbstractProcessorTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ArezPersistProcessorTest
  extends AbstractProcessorTest
{
  @DataProvider( name = "successfulCompiles" )
  public Object[][] successfulCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.persist.BasicPersistModel" },
        new Object[]{ "com.example.persist.CustomNamePersistModel" },
        new Object[]{ "com.example.persist.CustomSetterPersistModel" },
        new Object[]{ "com.example.persist.CustomStorePersistModel" },
        new Object[]{ "com.example.persist.MultiPropertyPersistModel" },
        new Object[]{ "com.example.persist.MultiStorePersistModel" },
        new Object[]{ "com.example.persist.ObjectTypePersistModel" },

        new Object[]{ "com.example.persist_id.BasicPersistIdModel" },

        new Object[]{ "com.example.persist.types.TypeBooleanPersistModel" },
        new Object[]{ "com.example.persist.types.TypeBytePersistModel" },
        new Object[]{ "com.example.persist.types.TypeCharPersistModel" },
        new Object[]{ "com.example.persist.types.TypeDatePersistModel" },
        new Object[]{ "com.example.persist.types.TypeDoublePersistModel" },
        new Object[]{ "com.example.persist.types.TypeFloatPersistModel" },
        new Object[]{ "com.example.persist.types.TypeIntPersistModel" },
        new Object[]{ "com.example.persist.types.TypeRawListPersistModel" },
        new Object[]{ "com.example.persist.types.TypeLongPersistModel" },
        new Object[]{ "com.example.persist.types.TypeShortPersistModel" },
        new Object[]{ "com.example.persist.types.TypeStringPersistModel" },

        new Object[]{ "com.example.persist_type.CustomDefaultStorePersistTypeModel" },
        new Object[]{ "com.example.persist_type.CustomNamePersistTypeModel" },
        new Object[]{ "com.example.persist_type.PersistOnDisposeTruePersistTypeModel" },
        new Object[]{ "com.example.persist_type.PersistOnDisposeFalsePersistTypeModel" }
      };
  }

  @Test( dataProvider = "successfulCompiles" )
  public void processSuccessfulCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulCompile( classname );
  }

  @DataProvider( name = "failedCompiles" )
  public Object[][] failedCompiles()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.persist.BadNamePersistModel",
                      "@Persist target must not specify a name parameter that is not a valid java identifier" },
        new Object[]{ "com.example.persist.BadStorePersistModel",
                      "@Persist target must not specify a store parameter that is not a valid java identifier" },
        new Object[]{ "com.example.persist.DuplicateNamePersistModel",
                      "@Persist target must has the same name 'X' as another persistent property declared by the name. The other property is accessed by the method named getValue" },
        new Object[]{ "com.example.persist.MissingObservablePersistModel",
                      "@Persist target must be also be annotated with the arez.annotations.Observable annotation" },
        new Object[]{ "com.example.persist.MissingSetterPersistModel",
                      "@Persist target must be paired with a setter named setValue" },
        new Object[]{ "com.example.persist.SetterPersistModel",
                      "@Persist target must be present on the accessor method of the @Observable property" },

        new Object[]{ "com.example.persist_id.DuplicatePersistIdModel",
                      "@PersistId target must not be present multiple times within a @PersistType annotated type but another method annotated with @PersistId exists and is named getId1" },
        new Object[]{ "com.example.persist_id.PersistPersistIdModel",
                      "@PersistId target must not also be annotated with the @Persist annotation" },
        new Object[]{ "com.example.persist_id.UnclaimedPersistIdModel",
                      "@PersistId target must be enclosed within a type annotated by either the arez.annotations.ArezComponent annotation or the arez.annotations.ActAsComponent annotation" },

        new Object[]{ "com.example.persist_id.parent_type.AbstractPersistIdModel",
                      "@PersistId target must not be abstract" },
        new Object[]{ "com.example.persist_id.parent_type.GenericPersistIdModel",
                      "@PersistId target must not have any type parameters" },
        new Object[]{ "com.example.persist_id.parent_type.ParameterizedPersistIdModel",
                      "@PersistId target must not have any parameters" },
        new Object[]{ "com.example.persist_id.parent_type.PrivatePersistIdModel",
                      "@PersistId target must not be private" },
        new Object[]{ "com.example.persist_id.parent_type.ProtectedPersistIdModel",
                      "@PersistId target must not be protected" },
        new Object[]{ "com.example.persist_id.parent_type.StaticPersistIdModel",
                      "@PersistId target must not be static" },
        new Object[]{ "com.example.persist_id.parent_type.ThrowsPersistIdModel",
                      "@PersistId target must not throw any exceptions" },
        new Object[]{ "com.example.persist_id.parent_type.VoidPersistIdModel",
                      "@PersistId target must return a value" },

        new Object[]{ "com.example.persist_type.BadDefaultStorePersistTypeModel",
                      "@PersistType target must not specify a defaultStore parameter that is not a valid java identifier" },
        new Object[]{ "com.example.persist_type.BadNamePersistTypeModel",
                      "@PersistType target must not specify a name parameter that is not a valid java identifier" },
        new Object[]{ "com.example.persist_type.NonArezComponentPersistTypeModel",
                      "@PersistType target must be present on a type annotated with the @ArezComponent annotation" },
        new Object[]{ "com.example.persist_type.NoPropertiesPersistTypeModel",
                      "@PersistType target must contain one or more @Observable properties annotated with @Persist" }
      };
  }

  @Test( dataProvider = "failedCompiles" )
  public void processFailedCompile( @Nonnull final String classname, @Nonnull final String messageFragment )
  {
    assertFailedCompile( classname, messageFragment );
  }

  @Test
  public void inaccessiblePersistIdModel()
  {
    final List<JavaFileObject> inputs = Arrays.asList(
      fixture( "bad_input/" + toFilename( "com.example.persist_id.other.BasePackageAccessPersistIdModel" ) ),
      fixture( "bad_input/" + toFilename( "com.example.persist_id.InaccessiblePersistIdModel" ) )
    );
    assertFailedCompileResource( inputs,
                                 "@PersistId target must not be package access if the method is in a different package from the type annotated with the @Persist annotation" );
  }

  @DataProvider( name = "compileWithWarnings" )
  public Object[][] compileWithWarnings()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.persist.UnnecessaryCustomStorePersistModel",
                      "@Persist target should not specify the store parameter when it is the same as the defaultStore parameter in the specified by the @PersistType annotation on the enclosing type. This warning can be suppressed by annotating the element with @SuppressWarnings( \"ArezPersist:UnnecessaryStore\" )" }
      };
  }

  @Test( dataProvider = "compileWithWarnings" )
  public void processCompileWithWarnings( @Nonnull final String classname, @Nonnull final String messageFragment )
  {
    assertCompilesWithSingleWarning( classname, messageFragment );
  }

  @DataProvider( name = "compileWithoutWarnings" )
  public Object[][] compileWithoutWarnings()
  {
    return new Object[][]
      {
        new Object[]{ "com.example.persist.types.TypeBoxedBooleanPersistModel" },
        new Object[]{ "com.example.persist.types.TypeBoxedBytePersistModel" },
        new Object[]{ "com.example.persist.types.TypeBoxedCharacterPersistModel" },
        new Object[]{ "com.example.persist.types.TypeBoxedDoublePersistModel" },
        new Object[]{ "com.example.persist.types.TypeBoxedFloatPersistModel" },
        new Object[]{ "com.example.persist.types.TypeBoxedIntegerPersistModel" },
        new Object[]{ "com.example.persist.types.TypeBoxedLongPersistModel" },
        new Object[]{ "com.example.persist.types.TypeBoxedShortPersistModel" }
      };
  }

  @Test( dataProvider = "compileWithoutWarnings" )
  public void processCompileWithoutWarnings( @Nonnull final String classname )
  {
    assertCompilesWithoutWarnings( classname );
  }

  void assertSuccessfulCompile( @Nonnull final String classname )
    throws Exception
  {
    assertSuccessfulCompile( classname, deriveExpectedOutputs( classname ) );
  }

  @Nonnull
  String[] deriveExpectedOutputs( @Nonnull final String classname )
  {
    final List<String> expectedOutputs = new ArrayList<>();
    expectedOutputs.add( toFilename( classname, "", "_PersistSidecar.java" ) );
    return expectedOutputs.toArray( new String[ 0 ] );
  }

  @Nonnull
  @Override
  protected String getOptionPrefix()
  {
    return "arez.persist";
  }

  @Nonnull
  @Override
  protected ArezPersistProcessor processor()
  {
    return new ArezPersistProcessor();
  }

  @Nonnull
  @Override
  protected Processor[] additionalProcessors()
  {
    return new Processor[]{ new ArezProcessor() };
  }

  @Override
  protected boolean emitGeneratedFile( @Nonnull final String target )
  {
    return super.emitGeneratedFile( target ) &&
           !target.contains( "/Arez_" ) &&
           !target.contains( "_Arez_" ) &&
           !target.startsWith( "Arez_" );
  }
}
