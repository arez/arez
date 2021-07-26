package arez.dom;

import java.util.Objects;
import javaemul.internal.annotations.DoNotAutobox;
import javax.annotation.Nullable;

/**
 * An immutable variant of {@link akasha.Coordinates}.
 */
public final class Position
{
  private final double _accuracy;
  @Nullable
  private final Double _altitude;
  @Nullable
  private final Double _heading;
  private final double _latitude;
  private final double _longitude;
  @Nullable
  private final Double _speed;

  /**
   * Create the position object.
   *
   * @param accuracy  the accuracy.
   * @param altitude  the altitude.
   * @param heading   the heading.
   * @param latitude  the latitude.
   * @param longitude the longitude.
   * @param speed     the speed.
   */
  public Position( final double accuracy,
                   @DoNotAutobox @Nullable final Double altitude,
                   @DoNotAutobox @Nullable final Double heading,
                   final double latitude,
                   final double longitude,
                   @DoNotAutobox @Nullable final Double speed )
  {
    _accuracy = accuracy;
    _altitude = altitude;
    _heading = heading;
    _latitude = latitude;
    _longitude = longitude;
    _speed = speed;
  }

  /**
   * Return the accuracy of the latitude and longitude properties, expressed in meters.
   *
   * @return the accuracy.
   */
  public double getAccuracy()
  {
    return _accuracy;
  }

  /**
   * Return the position's altitude in meters, relative to sea level. This value can be null if the implementation cannot provide the data.
   *
   * @return the altitude.
   */
  @Nullable
  public Double getAltitude()
  {
    return _altitude;
  }

  /**
   * Return  the direction in which the device is traveling.
   * This value, specified in degrees, indicates how far off from heading true north the device is. 0 degrees
   * represents true north, and the direction is determined clockwise (which means that east is 90 degrees and
   * west is 270 degrees). If speed is 0, heading is NaN. If the device is unable to provide heading information,
   * this value is null.
   *
   * @return the heading.
   */
  @Nullable
  public Double getHeading()
  {
    return _heading;
  }

  /**
   * Return the position's latitude in decimal degrees.
   *
   * @return the latitude.
   */
  public double getLatitude()
  {
    return _latitude;
  }

  /**
   * Return the position's longitude in decimal degrees.
   *
   * @return the longitude.
   */
  public double getLongitude()
  {
    return _longitude;
  }

  /**
   * Return the velocity of the device in meters per second.
   *
   * @return the speed.
   */
  @Nullable
  public Double getSpeed()
  {
    return _speed;
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( o instanceof Position )
    {
      final Position other = (Position) o;
      return other._latitude == _latitude && other._longitude == _longitude;
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode()
  {
    return Objects.hash( _latitude, _longitude );
  }
}
