package arez;

class CountAndObserveProcedure
  extends CountingProcedure
{
  @Override
  public void call()
    throws Throwable
  {
    super.call();
    AbstractArezTest.observeADependency();
  }
}
