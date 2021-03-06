/*
 * Copyright (c) 2017-present Robert Jaros
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package pl.treksoft.kvision

import redux.Action
import redux.Enhancer
import redux.Middleware
import redux.Reducer
import redux.Store
import kotlinx.browser.window

internal val kVManagerReduxInit = KVManagerRedux.init()

/**
 * Internal singleton object which initializes and configures KVision Redux module.
 */
internal object KVManagerRedux {

    private val redux = require("redux/dist/redux.min.js")
    internal val reduxThunk = require("redux-thunk/dist/redux-thunk.min.js").default

    @Suppress("UnsafeCastFromDynamic")
    internal fun <S, A, R> createStore(
        reducer: Reducer<S, A>,
        preloadedState: S,
        enhancer: Enhancer<S, Action, Action, A, R>
    ): Store<S, A, R> {
        return redux.createStore(reducer, preloadedState, enhancer)
    }

    @Suppress("UnsafeCastFromDynamic")
    internal fun <S, A1, R1, A2, R2> applyMiddleware(
        vararg middlewares:
        Middleware<S, A1, R1, A2, R2>
    ): Enhancer<S, A1, R1, A2, R2> {
        return redux.applyMiddleware.apply(null, middlewares)
    }

    @Suppress("UnsafeCastFromDynamic")
    internal fun <A, T1, R> compose(function1: (T1) -> R, function2: (A) -> T1): (A) -> R {
        val composeEnhancers = if (window.asDynamic().__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ != undefined) {
            window.asDynamic().__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
        } else {
            redux.compose
        }
        return composeEnhancers(function1, function2)
    }

    internal fun init() {}
}
