package org.realityforge.arez.processor;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
import static com.google.common.truth.Truth.assert_;

abstract class AbstractArezProcessorTest
{
  void assertSuccessfulCompile( @Nonnull final String classname )
  {
    final String file = classname.replaceAll( "\\.", "/" );
    assertSuccessfulCompile( "input/" + file + ".java", "expected/" + file + ".java" );
  }

  void assertSuccessfulCompile( @Nonnull final String inputResource,
                                @Nonnull final String expectedOutputResource )
  {
    final JavaFileObject source = JavaFileObjects.forResource( inputResource );
    assert_().about( JavaSourceSubjectFactory.javaSource() ).
      that( source ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      and().generatesSources( JavaFileObjects.forResource( expectedOutputResource ) );
  }
}
