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
 * A {@link SecurityToken} is a piece of data that has been hashed with
 * a secret and it may be valid for a limited amount of time.
 */
public interface SecurityToken {

  /**
   * Creates a security token for the given {@code byte[]}.
   */
  public byte[] create(byte[] data);

  /**
   * Creates a security token for the given {@code byte[]}.
   */
  public byte[] create(byte[] data, int offset, int length);

  /**
   * Returns {@code true} if the given security token is valid.
   */
  public boolean isFor(byte[] data, byte[] securityToken);

  /**
   * Returns {@code true} if the given security token is valid.
   */
  public boolean isFor(byte[] data, byte[] securityToken, 
      int offset, int length);

  /**
   * Returns {@code true} if the given security token is valid.
   */
  public boolean isFor(byte[] data, int dataOffset, int dataLength,
      byte[] securityToken, int offset, int length);

}