package arez.processor;

import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class MappedRelationshipDiagnosticTest extends ArezProcessorTestSupport {
    @DataProvider
    public Object[][] invalidMappings() {
        return new Object[][] {
            {"ExplicitWrongFilter", "Invalid @MappedRelationshipFilter"},
            {"ExplicitBoxedFilter", "return boolean"},
            {"ExplicitWrongRetention", "Invalid @MappedRelationshipRetention"},
            {"ExplicitWrongComparator", "Invalid @MappedRelationshipComparator"},
            {"NullableComparator", "must return a non-null value"},
            {"RawComparator", "must have no parameters and return Comparator"},
            {"AmbiguousFilter", "multiple compatible filter callbacks"},
            {"AmbiguousRetention", "multiple compatible retention callbacks"},
            {"DuplicateComparator", "multiple compatible comparator callbacks"},
            {"UnknownFilter", "unknown relationship 'missing'"},
            {"UnknownComparator", "unknown relationship 'missing'"},
            {"UnknownRetention", "unknown relationship 'missing'"},
            {"ExplicitIsNotShared", "'others' requires a compatible factory callback"},
            {"WriteOutsideWrongDependencies", "@MappedRelationship target specified writeOutsideTransaction = ENABLE"},
            {"MissingExternalReference", "@MappedRelationship target specified depType = AREZ_OR_EXTERNAL"},
            {"KeepAliveActivationHook", "@OnActivate exists for @MappedRelationship"},
            {"MissingSource", "requires a compatible source callback"},
            {"MissingFactory", "requires a compatible factory callback"},
            {"WrongSourceSignature", "Nonmatching methods:"},
            {"WrongFactorySignature", "must accept"},
            {"NullableFactory", "must return a non-null value"},
            {"NonNullFactory", "must accept nullable source elements"},
            {"ConcreteGetter", "must be abstract"},
            {"GetterParameters", "must not have any parameters"},
            {"GetterTypeParameters", "have no type parameters"},
            {"WrongTarget", "must be observable Arez components"},
            {"NullableList", "must return a non-null list"},
            {"NullableTarget", "target elements must be non-null"},
            {"WildcardTarget", "must return List<Target>"},
            {"PrivateSource", "private"},
            {"StaticFactory", "concrete instance method"},
            {"FactoryThrows", "declared exceptions"},
            {"AmbiguousFactory", "multiple compatible factory callbacks"},
            {"DuplicateFactory", "multiple compatible factory callbacks"},
            {"UnknownAssociation", "unknown relationship 'missing'"},
            {"ExplicitWrongSource", "Invalid @MappedRelationshipSource"},
            {"ExplicitWrongFactory", "Invalid @MappedRelationshipFactory"},
            {"MemoizeOverlap", "both @Memoize and @MappedRelationship"},
        };
    }

    @Test(dataProvider = "invalidMappings")
    public void invalidMapping(final String model, final String message) {
        assertFailedCompileResource(
                List.of(
                        fixture("input/com/example/mapped_relationship/Target.java"),
                        fixture("bad_input/com/example/mapped_relationship/" + model + ".java")),
                message);
    }
}
