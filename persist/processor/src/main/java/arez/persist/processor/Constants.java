package arez.persist.processor;

import javax.annotation.Nonnull;

final class Constants
{
  @Nonnull
  static final String AREZ_COMPONENT_CLASSNAME = "arez.annotations.ArezComponent";
  @Nonnull
  static final String ACT_AS_COMPONENT_CLASSNAME = "arez.annotations.ActAsComponent";
  @Nonnull
  static final String OBSERVABLE_CLASSNAME = "arez.annotations.Observable";
  @Nonnull
  static final String PERSIST_TYPE_CLASSNAME = "arez.persist.PersistType";
  @Nonnull
  static final String PERSIST_ID_CLASSNAME = "arez.persist.PersistId";
  @Nonnull
  static final String PERSIST_CLASSNAME = "arez.persist.Persist";
  @Nonnull
  static final String WARNING_UNNECESSARY_STORE = "ArezPersist:UnnecessaryStore";

  private Constants()
  {
  }
}
