goog.module('arez.browser.Clock');
const Fixtures = goog.require('arez.browser.Fixtures');

/**
 * @return {{advance: function(number):void, pending: function():number, restore: function():void}}
 */
function install() {
  /** @type {number} */
  let now = 100000;
  /** @type {number} */
  let nextId = 1;
  /** @type {!Map<number, {deadline:number, callback:function():void}>} */
  const timers = new Map();
  const restoreNow = Fixtures.replaceProperty(Date, 'now', () => now);
  /**
   * @param {function():void} callback
   * @param {number} delay
   * @return {number}
   */
  const schedule = (callback, delay) => {
    const id = nextId++;
    timers.set(id, {deadline: now + delay, callback});
    return id;
  };
  /** @param {number} id */
  const cancel = (id) => { timers.delete(id); };
  const restoreTimeout = Fixtures.replaceProperty(window, 'setTimeout', schedule);
  const restoreClear = Fixtures.replaceProperty(window, 'clearTimeout', cancel);
  /** @param {number} delta */
  const advance = (delta) => {
    const target = now + delta;
    while (true) {
      /** @type {number} */
      let selected = 0;
      /** @type {number} */
      let deadline = target;
      timers.forEach((timer, id) => {
        if (timer.deadline <= deadline) {
          selected = id;
          deadline = timer.deadline;
        }
      });
      if (!selected) {
        now = target;
        return;
      }
      const timer = timers.get(selected);
      if (!timer) throw new Error('Scheduled timer disappeared');
      timers.delete(selected);
      now = deadline;
      timer.callback();
    }
  };
  return {
    advance,
    pending: () => timers.size,
    restore: () => {
      restoreClear();
      restoreTimeout();
      restoreNow();
    },
  };
}

exports = {install};
