package arez.processor;

import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class ArezProcessorJSpecifyDiagnosticTest extends ArezProcessorTestSupport {
    @DataProvider(name = "invalidContracts")
    public Object[][] invalidContracts() {
        return new Object[][] {
            {"UnmarkedModel", "Unspecified JSpecify nullness"},
            {"UnmarkedMethodModel", "Unspecified JSpecify nullness"},
            {"ParametricInitializerModel", "Parametric JSpecify nullness"},
            {"ConflictingModel", "Conflicting JSpecify"}
        };
    }

    @Test(dataProvider = "invalidContracts")
    public void invalidContract(final String model, final String diagnostic) {
        assertFailedCompile("com.example.jspecify." + model, diagnostic);
    }

    @Test
    public void unmarkedArgumentDoesNotInheritParentDefaults() {
        assertFailedCompileResource(
                List.of(
                        fixture("input/com/example/jspecify/GenericBase.java"),
                        fixture("bad_input/com/example/jspecify/UnmarkedArgumentModel.java")),
                "Unspecified JSpecify nullness");
    }

    @Test
    public void markedSubclassDoesNotMarkBaseDeclaration() {
        assertFailedCompileResource(
                List.of(
                        fixture("bad_input/com/example/jspecify/UnmarkedBase.java"),
                        fixture("bad_input/com/example/jspecify/InheritedUnmarkedModel.java")),
                "Unspecified JSpecify nullness");
    }
}
