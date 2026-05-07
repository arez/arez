package arez.doc.examples.at_requires_transaction;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.RequiresTransaction;
import arez.annotations.TransactionMode;

@ArezComponent
abstract class Wallet
{
  private int _balance;

  @Observable
  int getBalance()
  {
    return _balance;
  }

  void setBalance( final int balance )
  {
    _balance = balance;
  }

  @Action
  void deposit( final int amount )
  {
    currentBalance();
    applyDelta( amount );
  }

  @RequiresTransaction
  int currentBalance()
  {
    return getBalance();
  }

  @RequiresTransaction( mode = TransactionMode.READ_WRITE )
  void applyDelta( final int delta )
  {
    setBalance( getBalance() + delta );
  }
}
