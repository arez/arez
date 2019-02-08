package arez.integration;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.revapi.API;
import org.revapi.AnalysisContext;
import org.revapi.AnalysisResult;
import org.revapi.Report;
import org.revapi.Reporter;
import org.revapi.Revapi;
import org.revapi.simple.FileArchive;
import org.testng.annotations.Test;

public class ExposedApiTest
{
  @Test
  public void foo()
    throws Exception
  {
    final Revapi revapi = Revapi.builder()
      .withReporters( CollectingReporter.class )
      .withAllExtensionsFromThreadContextClassLoader()
      .build();

    final FileArchive oldFile =
      new FileArchive( new File( "/Users/peter/.m2/repository/org/realityforge/arez/arez-core/0.117/arez-core-0.117.jar" ) );
    final FileArchive newFile =
      new FileArchive( new File( "/Users/peter/.m2/repository/org/realityforge/arez/arez-core/0.128/arez-core-0.128.jar" ) );
    final String json =
      "[\n" +
      "  {\n" +
      "    \"extension\": \"revapi.java\",\n" +
      "    \"configuration\": {\n" +
      "      \"missing-classes\": {\n" +
      "        \"behavior\": \"ignore\",\n" +
      "        \"ignoreMissingAnnotations\": true\n" +
      "      },\n" +
      "      \"reportUsesFor\": \"all-differences\"\n" +
      "    }\n" +
      "  },\n" +
      "  {\n" +
      "    \"extension\": \"revapi.ignore\",\n" +
      "    \"configuration\": [\n" +
      "      {\n" +
      "        \"code\": \"java.annotation.attributeValueChanged\",\n" +
      "        \"annotationType\": \"scala.reflect.ScalaSignature\"\n" +
      "      }\n" +
      "    ]\n" +
      "  }\n" +
      "]";
    final AnalysisContext analysisContext = AnalysisContext.builder()
      .withOldAPI( API.of( oldFile ).build() )
      .withNewAPI( API.of( newFile ).build() )
      .withConfigurationFromJSON( json ).build();

    final AnalysisResult analyze = revapi.analyze( analysisContext );
    final Map<Reporter, AnalysisContext> reporters = analyze.getExtensions().getReporters();
    analyze.close();
  }

  public static class CollectingReporter
    implements Reporter
  {
    private final List<Report> reports = new ArrayList<>();

    public List<Report> getReports()
    {
      return reports;
    }

    @Nullable
    @Override
    public String getExtensionId()
    {
      return "collectingReporter";
    }

    @Nullable
    @Override
    public Reader getJSONSchema()
    {
      return null;
    }

    @Override
    public void initialize( @Nonnull AnalysisContext properties )
    {
      System.out.println( "CollectingReporter.initialize" );
    }

    @Override
    public void report( @Nonnull Report report )
    {
      if ( !report.getDifferences().isEmpty() )
      {
        reports.add( report );
      }
    }

    @Override
    public void close()
    {
    }
  }
}
