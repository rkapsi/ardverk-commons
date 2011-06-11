/*
 * Copyright 2010-2011 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.lang;

public class BindableUtils {

    private BindableUtils() {}

    /**
     * Binds the {@link Bindable} to the given value.
     */
    public static <T> boolean bind(Bindable<? super T> bindable, T value) {
        if (bindable != null) {
            try {
                bindable.bind(value);
                return true;
            } catch (Exception err) {
                ExceptionUtils.exceptionCaught(err);
            }
        }
        return false;
    }

    /**
     * Unbinds the given {@link Bindable}.
     */
    public static boolean unbind(Bindable<?> bindable) {
        if (bindable != null) {
            try {
                bindable.unbind();
                return true;
            } catch (Exception err) {
                ExceptionUtils.exceptionCaught(err);
            }
        }
        return false;
    }

    /**
     * Unbinds the given array of {@link Bindable}s.
     */
    public static boolean unbindAll(Bindable<?>... bindables) {
        boolean success = true;
        if (bindables != null) {
            for (Bindable<?> c : bindables) {
                success &= unbind(c);
            }
        }
        return success;
    }

    /**
     * Unbinds the given {@link Iterable} of {@link Bindable}s
     */
    public static boolean unbindAll(Iterable<? extends Bindable<?>> bindables) {
        boolean success = true;
        if (bindables != null) {
            for (Bindable<?> c : bindables) {
                success &= unbind(c);
            }
        }
        return success;
    }
}
