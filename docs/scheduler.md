---
title: Scheduler
---

Arez contains a scheduler that is responsible for:

* executing the observer's `observed` function if the observer uses the Arez internal executor and the
  observer has been scheduled.
* executing the observer's `onDepsChange` hook function when any dependencies of the `observe` function
  are changed.
* recomputing the computable value when any dependencies of the computable value are changed.

The Arez runtime schedules a task when dependency changes are detected or when application code
explicitly invokes {@link: arez.Observer#schedule()}.

Arez uses [transactions](transactions.md) to batch changes to Arez state and will only attempt to
execute a task when the outer most transaction is completed. This makes sure intermediate or incomplete
values produced during a transaction are not visible to the rest of the application until the transaction has
completed.

The scheduler supports 5 different priority levels (See {@link: arez.spy.Priority}) when scheduling observers.
All higher priority observers will be be executed prior to lower priority observers. Observers with the same
priority will be executed in the order in which they were scheduled. An observer is scheduled the first time
any dependency of the observer is changed (i.e. the {@link: arez.ObservableValue#reportChanged()}
is invoked on the dependency). The observer will not be rescheduled while the observer is in the pending
observers list.

Recalculation of computable values can be scheduled but before the transaction has completed the transaction may try
to read the computable value in which case the computable value will be re-calculated immediately. The computable value
will still appear in the pending observers list but the task will be skipped by the scheduler.

The API allows applications to create observers that will not run immediately by passing the
{@link: arez.Observer.Flags#RUN_LATER Observer.Flags.RUN_LATER} flag. This is sometimes necessary when creating complex components
that need to be completely constructed prior to executing the observed function. To support this use-case the the
Arez api also provides the {@link: arez.ArezContext#triggerScheduler()} method that
will start the scheduler if there are pending tasks and the scheduler is not currently running.

## Pausing and Resuming

It is also possible to pause and resume the scheduler when needed. This is rarely needed and somewhat dangerous to
use as it is the only mechanism where it is possible to create an inconsistent state in Arez. (i.e. Observables have
updated but not all the observers and computable values have reacted and are consistent with the new state.)

However it is sometimes needed by Arez-based frameworks with complex concurrency needs that can ensure that no code
interacts with Arez components while the scheduler is paused. A simple example of how pausing works is as follows:

{@file_content: file=arez/doc/examples/scheduler/SchedulerExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}
