goog.module('arez.browser.Fixtures');

/**
 * @param {string} html
 * @return {!Element}
 */
function append(html) {
  const root = document.createElement('div');
  root.innerHTML = html;
  document.body.appendChild(root);
  return root;
}

/**
 * @param {string} id
 * @return {!Element}
 */
function element(id) {
  const result = document.getElementById(id);
  if (!result) throw new Error('Missing test element: ' + id);
  return result;
}

/** @param {string} id */
function click(id) {
  element(id).dispatchEvent(new MouseEvent('click'));
}

/**
 * @param {!EventTarget} target
 * @param {(string|!Array<string>)} type
 * @return {{count: function(): number, restore: function(): void}}
 */
function trackEvents(target, type) {
  const types = new Set(typeof type === 'string' ? [type] : type);
  /** @type {!Map<string, !Set<(EventListener|function(!Event):*|null)>>} */
  const listeners = new Map();
  const add = target.addEventListener;
  const remove = target.removeEventListener;
  /**
   * @param {string} eventType
   * @param {(EventListener|function(!Event):*|null)} listener
   * @param {(boolean|!AddEventListenerOptions)=} options
   */
  const trackedAdd = (eventType, listener, options) => {
    if (types.has(eventType)) {
      let set = listeners.get(eventType);
      if (!set) {
        set = new Set();
        listeners.set(eventType, set);
      }
      set.add(listener);
    }
    add.call(target, eventType, listener, options);
  };
  Reflect.set(target, 'addEventListener', trackedAdd);
  /**
   * @param {string} eventType
   * @param {(EventListener|function(!Event):*|null)} listener
   * @param {(boolean|!EventListenerOptions)=} options
   */
  const trackedRemove = (eventType, listener, options) => {
    const set = listeners.get(eventType);
    if (set) set.delete(listener);
    remove.call(target, eventType, listener, options);
  };
  Reflect.set(target, 'removeEventListener', trackedRemove);
  return {
    count: () => {
      /** @type {number} */
      let count = 0;
      listeners.forEach((set) => count += set.size);
      return count;
    },
    restore: () => {
      Reflect.set(target, 'addEventListener', add);
      Reflect.set(target, 'removeEventListener', remove);
    },
  };
}

/**
 * @param {!Object} target
 * @param {string} key
 * @param {*} value
 * @return {function():void}
 */
function replaceProperty(target, key, value) {
  const original = Object.getOwnPropertyDescriptor(target, key);
  Object.defineProperty(target, key, {configurable: true, writable: true, value});
  return () => {
    if (original) Object.defineProperty(target, key, original);
    else Reflect.deleteProperty(target, key);
  };
}

exports = {append, click, element, replaceProperty, trackEvents};
