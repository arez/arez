package org.realityforge.arez;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface OnRelatedAddedCallback
{
  void onRelatedAdded( @Nonnull Object entity, @Nonnull Object attributeKey, @Nullable Object related );
}
