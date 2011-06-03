package org.ardverk.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    private FileUtils() {}
    
    public static File mkdirs(String path) throws IOException {
        return mkdirs(new File(path));
    }
    
    public static File mkdirs(File parent, String name) throws IOException {
        return mkdirs(new File(parent, name));
    }
    
    public static File mkfile(String path) throws IOException {
        File file = new File(path);
        mkdirs(file.getParentFile());
        return file;
    }
    
    public static File mkfile(File parent, String name) throws IOException {
        File file = new File(parent, name);
        mkdirs(file.getParentFile());
        return file;
    }
    
    public static File mkdirs(File dir) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
            
            if (!dir.exists()) {
                throw new IOException();
            }
        }
        return dir;
    }
    
    public static void renameTo(File src, File dst) throws IOException {
        renameTo(src, dst, true);
    }
    
    public static void renameTo(File src, File dst, boolean delete) throws IOException {
        if (!src.renameTo(dst)) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src));
                out = new BufferedOutputStream(new FileOutputStream(dst));
                StreamUtils.copy(in, out);
            } finally {
                IoUtils.closeAll(in, out);
            }
            
            if (delete) {
                src.delete();
            }
        }
    }
    
    public static boolean deleteAll(File... files) {
        if (files != null) {
            boolean success = true;
            for (File file : files) {
                success &= delete(file);
            }
            return success;
        }
        return false;
    }
    
    public static boolean delete(File file) {
        if (file != null) {
            return file.delete();
        }
        return false;
    }
}
