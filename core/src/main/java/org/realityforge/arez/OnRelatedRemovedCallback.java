package org.realityforge.arez;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface OnRelatedRemovedCallback
{
  void onRelatedRemoved( @Nonnull Object entity, @Nonnull Object attributeKey, @Nullable Object related );
}
