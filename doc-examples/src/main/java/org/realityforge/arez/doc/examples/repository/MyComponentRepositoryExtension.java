package org.realityforge.arez.doc.examples.repository;

import java.util.List;
import org.realityforge.arez.annotations.Computed;

public interface MyComponentRepositoryExtension
  extends MyComponentBaseRepositoryExtension
{
  @Computed
  default boolean isEmpty()
  {
    return self().findAll().isEmpty();
  }

  @Computed
  default List<MyComponent> findAllActive()
  {
    return self().findAllByQuery( MyComponent::isActive );
  }
}
