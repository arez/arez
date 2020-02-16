package arez.processor;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;

final class RepositoryDescriptor
{
  @Nonnull
  private final ComponentDescriptor _component;
  @Nonnull
  private final List<TypeElement> _extensions;
  private final boolean _shouldRepositoryDefineCreate;
  private final boolean _shouldRepositoryDefineAttach;
  private final boolean _shouldRepositoryDefineDestroy;
  private final boolean _shouldRepositoryDefineDetach;
  private final boolean _dagger;

  RepositoryDescriptor( @Nonnull final ComponentDescriptor component,
                        @Nonnull final List<TypeElement> extensions,
                        final boolean shouldRepositoryDefineCreate,
                        final boolean shouldRepositoryDefineAttach,
                        final boolean shouldRepositoryDefineDestroy,
                        final boolean shouldRepositoryDefineDetach,
                        final boolean dagger )
  {
    _component = Objects.requireNonNull( component );
    _extensions = Objects.requireNonNull( extensions );
    _shouldRepositoryDefineCreate = shouldRepositoryDefineCreate;
    _shouldRepositoryDefineAttach = shouldRepositoryDefineAttach;
    _shouldRepositoryDefineDestroy = shouldRepositoryDefineDestroy;
    _shouldRepositoryDefineDetach = shouldRepositoryDefineDetach;
    _dagger = dagger;
  }

  @Nonnull
  ComponentDescriptor getComponent()
  {
    return _component;
  }

  @Nonnull
  List<TypeElement> getExtensions()
  {
    return _extensions;
  }

  boolean isDaggerEnabled()
  {
    return _dagger;
  }

  boolean shouldRepositoryDefineCreate()
  {
    return _shouldRepositoryDefineCreate;
  }

  boolean shouldRepositoryDefineAttach()
  {
    return _shouldRepositoryDefineAttach;
  }

  boolean shouldRepositoryDefineDestroy()
  {
    return _shouldRepositoryDefineDestroy;
  }

  boolean shouldRepositoryDefineDetach()
  {
    return _shouldRepositoryDefineDetach;
  }
}
