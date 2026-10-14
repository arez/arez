goog.module('arez.promise.example.PromiseTest');

const BrowserAssertions = goog.require('arez.browser.BrowserAssertions');
const Example = goog.require('arez.promise.example.ObservablePromiseExample');
const Mode = goog.require('arez.browser.Mode');
const asserts = goog.require('goog.testing.asserts');
const testSuite = goog.require('goog.testing.testSuite');

let originalFetch;
/** @type {!Example} */
let app;
/** @type {function(?Response):void} */
let resolve;
/** @type {function(?Error):void} */
let reject;
let requestedUrl;
/** @type {!Promise<*>} */
let chain;

testSuite({
  setUp() {
    const element = document.createElement('div');
    element.id = 'app';
    document.body.appendChild(element);
    originalFetch = window.fetch;
    window.fetch = (url) => {
      requestedUrl = url;
      const promise = new Promise((fulfill, fail) => {
        resolve = fulfill;
        reject = fail;
      });
      // Observe the branch created by ObservablePromise so its deliberately
      // propagated rejection can be asserted without an unhandled rejection.
      const then = promise.then.bind(promise);
      /**
       * @param {function(*):*} callback
       * @return {!Promise<*>}
       */
      const captureThen = (callback) => {
        const result = then(callback);
        const catchError = result.catch.bind(result);
        /**
         * @param {function(*):*} callback
         * @return {!Promise<*>}
         */
        const captureCatch = (callback) => {
          chain = catchError(callback);
          return chain;
        };
        Reflect.set(result, 'catch', captureCatch);
        return result;
      };
      Reflect.set(promise, 'then', captureThen);
      return promise;
    };
    app = Example.bootstrap();
    BrowserAssertions.expectText('app', 'Promise State: PENDING');
    asserts.assertEquals('index.html', requestedUrl);
    BrowserAssertions.expectChecks('development' === Mode.environment);
  },

  tearDown() {
    app.dispose();
    asserts.assertTrue(app.isDisposed());
    window.fetch = originalFetch;
    document.getElementById('app').remove();
  },

  testPending() {
    BrowserAssertions.expectText('app', 'Promise State: PENDING');
  },

  testFulfilled() {
    resolve(new Response('', {status: 200, statusText: 'OK'}));
    return chain.then(() => BrowserAssertions.expectText('app', 'Promise State: FULFILLED - Response: 200: OK'));
  },

  testFulfilledNull() {
    resolve(null);
    return chain.then(() => BrowserAssertions.expectText('app', 'Promise State: FULFILLED'));
  },

  testRejected() {
    const error = new Error('controlled failure');
    const observed = chain.catch((actual) => {
      asserts.assertEquals(error, actual);
      BrowserAssertions.expectText('app', 'Promise State: REJECTED - Error: controlled failure');
    });
    reject(error);
    return observed;
  },

  testRejectedNull() {
    const observed = chain.catch((actual) => {
      asserts.assertNull(actual);
      BrowserAssertions.expectText('app', 'Promise State: REJECTED');
    });
    reject(null);
    return observed;
  },
});
