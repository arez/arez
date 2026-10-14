goog.module('arez.dom.example.MediaQueryExampleTest');
const BrowserAssertions = goog.require('arez.browser.BrowserAssertions');
const Example = goog.require('arez.dom.example.MediaQueryExample');
const Fixtures = goog.require('arez.browser.Fixtures');
const Mode = goog.require('arez.browser.Mode');
const asserts = goog.require('goog.testing.asserts');
const testSuite = goog.require('goog.testing.testSuite');

class ControlledQuery {
  /** @param {string} query */
  constructor(query) {
    this.matches = query === '(max-width: 1200px)';
    /** @type {!Set<function(!Event):void>} */
    this.listeners = new Set();
    const controller = this;
    const addListener = this.addListener.bind(this);
    const removeListener = this.removeListener.bind(this);
    // Keep the native MediaQueryList boundary explicit under ADVANCED, which
    // can otherwise remove properties on the controller's JavaScript class.
    this.facade = /** @type {!MediaQueryList} */ ({
      'media': query,
      get 'matches'() { return controller.matches; },
      'addListener': addListener,
      'removeListener': removeListener,
    });
  }
  /** @param {function(!Event):void} listener */
  addListener(listener) { this.listeners.add(listener); }
  /** @param {function(!Event):void} listener */
  removeListener(listener) { this.listeners.delete(listener); }
  /** @param {boolean} matches */
  emit(matches) {
    this.matches = matches;
    for (const listener of this.listeners) {
      listener(new Event('change'));
    }
  }
}

const matchMedia = window.matchMedia;
/** @type {!Map<string, !ControlledQuery>} */
let queries;
/** @type {!Example} */
let app;
/** @type {!Element} */
let root;

testSuite({
  setUp() {
    queries = new Map();
    /**
     * @param {string} query
     * @return {!MediaQueryList}
     */
    const createQuery = (query) => {
      const result = new ControlledQuery(query);
      queries.set(query, result);
      return result.facade;
    };
    Reflect.set(window, 'matchMedia', createQuery);
    root = Fixtures.append('<div id="status"></div><span id="query"></span><button id="change-query"></button>');
    app = Example.bootstrap();
    BrowserAssertions.expectText('status', 'Screen size: Wide');
    BrowserAssertions.expectText('query', '(max-width: 600px)');
    BrowserAssertions.expectChecks('development' === Mode.environment);
  },
  tearDown() {
    app.dispose();
    asserts.assertTrue(app.isDisposed());
    for (const query of queries.values()) asserts.assertEquals(0, query.listeners.size);
    Reflect.set(window, 'matchMedia', matchMedia);
    root.remove();
  },
  testMatchChange() {
    const query = queries.get('(max-width: 600px)');
    asserts.assertNotNull(query);
    asserts.assertEquals(1, query.listeners.size);
    query.emit(true);
    BrowserAssertions.expectText('status', 'Screen size: Narrow');
  },
  testQueryChange() {
    const original = queries.get('(max-width: 600px)');
    asserts.assertNotNull(original);
    Fixtures.click('change-query');
    BrowserAssertions.expectText('query', '(max-width: 1200px)');
    BrowserAssertions.expectText('status', 'Screen size: Narrow');
    asserts.assertEquals(0, original.listeners.size);
    const current = queries.get('(max-width: 1200px)');
    asserts.assertNotNull(current);
    asserts.assertEquals(1, current.listeners.size);
  },
  testDisposal() {
    const query = queries.get('(max-width: 600px)');
    asserts.assertNotNull(query);
    app.dispose();
    asserts.assertEquals(0, query.listeners.size);
    query.emit(true);
    Fixtures.click('change-query');
    BrowserAssertions.expectText('status', 'Screen size: Wide');
    BrowserAssertions.expectText('query', '(max-width: 600px)');
  },
});
