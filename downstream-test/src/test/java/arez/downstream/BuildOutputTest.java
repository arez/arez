package arez.downstream;

import arez.gwt.qa.ArezBuildAsserts;
import org.realityforge.gwt.symbolmap.SymbolEntryIndex;
import org.testng.annotations.Test;

public class BuildOutputTest
{
  @Test
  public void arezProduction()
    throws Exception
  {
    final String build = "arez.after";

    final SymbolEntryIndex index =
      SymbolEntryIndex.readSymbolMapIntoIndex( WorkspaceTestUtil.getSymbolMapPath( "todomvc", build ) );

    ArezBuildAsserts.assertArezOutputs( index,
                                        false,
                                        false,
                                        false,
                                        false,
                                        false,
                                        false,
                                        false,
                                        false,
                                        false,
                                        false );

    // This pattern should apply if nameIncludeId is false (or @Singleton present), no @Repository annotation and !Arez.areNativeComponentsEnabled()
    ArezBuildAsserts.assertSyntheticId( index, "arez\\.browser\\.extras\\.Arez_BrowserLocation", false );

    // No services should have equals defined
    ArezBuildAsserts.assertEquals( index, "react4j\\.todomvc\\.model\\.Arez_ViewService", false );
    ArezBuildAsserts.assertEquals( index, "react4j\\.todomvc\\.model\\.Arez_TodoService", false );

    // Part of a repository so needs an equals
    ArezBuildAsserts.assertEquals( index, "react4j\\.todomvc\\.model\\.Arez_Todo", true );
  }
}
