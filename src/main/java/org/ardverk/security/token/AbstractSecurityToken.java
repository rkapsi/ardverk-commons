/*
 * Copyright 2010-2011 Roger Kapsi
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

package org.ardverk.security.token;

/**
 * An abstract implementation of {@link SecurityToken}.
 */
public abstract class AbstractSecurityToken implements SecurityToken {

  @Override
  public byte[] create(byte[] data) {
    return create(data, 0, data.length);
  }
  
  @Override
  public boolean isFor(byte[] data, byte[] securityToken) {
    return isFor(data, securityToken, 0, securityToken.length);
  }
  
  @Override
  public boolean isFor(byte[] data, byte[] securityToken, int offset, int length) {
    return isFor(data, 0, data.length, securityToken, offset, length);
  }
}
