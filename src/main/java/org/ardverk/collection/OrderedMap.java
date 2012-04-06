/*
 * Copyright 2010-2012 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.collection;

import java.util.Map;

/**
 * A {@link Map} that is ordered (e.g. insertion order) but not 
 * necessarily sorted.
 */
public interface OrderedMap<K, V> extends Map<K, V> {

  public Map.Entry<K, V> firstEntry();
  
  public Map.Entry<K, V> lastEntry();
  
  public Map.Entry<K, V> nthEntry(int index);
  
  public K firstKey();
  
  public K lastKey();
  
  public K nthKey(int index);
  
  public V firstValue();
  
  public V lastValue();
  
  public V nthValue(int index);
}
