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
 * @param {string} type
 * @return {{count: function(): number, restore: function(): void}}
 */
function trackEvents(target, type) {
  const listeners = new Set();
  const add = target.addEventListener;
  const remove = target.removeEventListener;
  /**
   * @param {string} eventType
   * @param {(EventListener|function(!Event):*|null)} listener
   * @param {(boolean|!AddEventListenerOptions)=} options
   */
  const trackedAdd = (eventType, listener, options) => {
    if (eventType === type) listeners.add(listener);
    add.call(target, eventType, listener, options);
  };
  Reflect.set(target, 'addEventListener', trackedAdd);
  /**
   * @param {string} eventType
   * @param {(EventListener|function(!Event):*|null)} listener
   * @param {(boolean|!EventListenerOptions)=} options
   */
  const trackedRemove = (eventType, listener, options) => {
    if (eventType === type) listeners.delete(listener);
    remove.call(target, eventType, listener, options);
  };
  Reflect.set(target, 'removeEventListener', trackedRemove);
  return {
    count: () => listeners.size,
    restore: () => {
      Reflect.set(target, 'addEventListener', add);
      Reflect.set(target, 'removeEventListener', remove);
    },
  };
}

exports = {append, click, element, trackEvents};
