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

package org.ardverk.io;

import java.io.File;
import java.io.IOException;

public class FileUtils {
  
  private FileUtils() {}
  
  /**
   * Creates and returns a {@link File} for the given path.
   * 
   * NOTE: It's being assumed the path represents a directory!
   */
  public static File mkdirs(String path, boolean mkdirs) throws IOException {
    return mkdirs(new File(path), mkdirs);
  }
  
  /**
   * Creates and returns a {@link File} for the given path and name.
   * 
   * NOTE: It's being assumed the path represents a directory!
   */
  public static File mkdirs(File parent, String name, boolean mkdirs) throws IOException {
    return mkdirs(new File(parent, name), mkdirs);
  }
  
  /**
   * Creates and returns a {@link File} for the given path.
   * 
   * NOTE: It's being assumed the path represents a file!
   */
  public static File mkfile(String path, boolean mkdirs) throws IOException {
    return mkfile(new File(path), mkdirs);
  }
  
  /**
   * Creates and returns a {@link File} for the given path and name.
   * 
   * NOTE: It's being assumed the path represents a file!
   */
  public static File mkfile(File parent, String name, boolean mkdirs) throws IOException {
    return mkfile(new File(parent, name), mkdirs);
  }
  
  private static File mkfile(File file, boolean mkdirs) throws IOException {
    mkdirs(file.getParentFile(), mkdirs);
    return file;
  }
  
  private static File mkdirs(File dir, boolean mkdirs) throws IOException {
    if (mkdirs && !dir.exists()) {
      dir.mkdirs();
      
      if (!dir.exists()) {
        throw new IOException();
      }
    }
    return dir;
  }
}
