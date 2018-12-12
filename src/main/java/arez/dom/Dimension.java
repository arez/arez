package arez.dom;

public final class Dimension
{
  private final int _width;
  private final int _height;

  public Dimension( final int width, final int height )
  {
    _width = width;
    _height = height;
  }

  public int getWidth()
  {
    return _width;
  }

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
}
