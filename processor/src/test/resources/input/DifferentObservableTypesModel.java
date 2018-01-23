import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public class DifferentObservableTypesModel
{
  private boolean _v1;
  private byte _v2;
  private char _v3;
  private short _v4;
  private int _v5;
  private long _v6;
  private float _v7;
  private double _v8;
  private Object _v9;

  @Observable
  public boolean isV1()
  {
    return _v1;
  }

  @Observable
  public void setV1( final boolean v1 )
  {
    _v1 = v1;
  }

  @Observable
  public byte getV2()
  {
    return _v2;
  }

  @Observable
  public void setV2( final byte v2 )
  {
    _v2 = v2;
  }

  @Observable
  public char getV3()
  {
    return _v3;
  }

  @Observable
  public void setV3( final char v3 )
  {
    _v3 = v3;
  }

  @Observable
  public short getV4()
  {
    return _v4;
  }

  @Observable
  public void setV4( final short v4 )
  {
    _v4 = v4;
  }

  @Observable
  public int getV5()
  {
    return _v5;
  }

  @Observable
  public void setV5( final int v5 )
  {
    _v5 = v5;
  }

  @Observable
  public long getV6()
  {
    return _v6;
  }

  @Observable
  public void setV6( final long v6 )
  {
    _v6 = v6;
  }

  @Observable
  public float getV7()
  {
    return _v7;
  }

  @Observable
  public void setV7( final float v7 )
  {
    _v7 = v7;
  }

  @Observable
  public double getV8()
  {
    return _v8;
  }

  @Observable
  public void setV8( final double v8 )
  {
    _v8 = v8;
  }

  @Observable
  public Object getV9()
  {
    return _v9;
  }

  @Observable
  public void setV9( final Object v9 )
  {
    _v9 = v9;
  }
}
