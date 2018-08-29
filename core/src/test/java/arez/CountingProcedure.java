package arez;

class CountingProcedure
  implements Procedure
{
  private int _callCount;

  @Override
  public void call()
    throws Throwable
  {
    _callCount++;
  }

  int getCallCount()
  {
    return _callCount;
  }
}
