package org.realityforge.arez;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface OnAttributeChangedCallback
{
  void onAttributeChanged( @Nonnull Object entity, @Nonnull Object attributeKey );
}
