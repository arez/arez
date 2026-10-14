package arez.persist.processor;

final class Constants {
    static final com.palantir.javapoet.ClassName NONNULL_CLASSNAME =
            com.palantir.javapoet.ClassName.get("org.jspecify.annotations", "NonNull");

    static final String AREZ_COMPONENT_CLASSNAME = "arez.annotations.ArezComponent";

    static final String AREZ_COMPONENT_LIKE_CLASSNAME = "arez.annotations.ArezComponentLike";

    static final String OBSERVABLE_CLASSNAME = "arez.annotations.Observable";

    static final String PERSIST_TYPE_CLASSNAME = "arez.persist.PersistType";

    static final String PERSIST_ID_CLASSNAME = "arez.persist.PersistId";

    static final String PERSIST_CLASSNAME = "arez.persist.Persist";

    static final String WARNING_UNNECESSARY_STORE = "ArezPersist:UnnecessaryStore";

    private Constants() {}
}
