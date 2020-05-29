package arez.dom;

import java.util.Objects;

/**
 * A class containing width and height dimensions.
 */
public final class Dimension
{
  private final int _width;
  private final int _height;

  /**
   * Create the dimension object.
   *
   * @param width  the width.
   * @param height the height.
   */
  public Dimension( final int width, final int height )
  {
    _width = width;
    _height = height;
  }

  /**
   * Return the width.
   *
   * @return the width.
   */
  public int getWidth()
  {
    return _width;
  }

  /**
   * Return the height.
   *
   * @return the height.
   */
  public int getHeight()
  {
    return _height;
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( o instanceof Dimension )
    {
      final Dimension other = (Dimension) o;
      return other._width == _width && other._height == _height;
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( _width, _height );
  }
}
