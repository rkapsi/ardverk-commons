package org.ardverk.security;

import java.security.MessageDigest;
import java.util.zip.CRC32;

/**
 * An implementation of {@link MessageDigest} that is computing 
 * a {@link CRC32} value.
 * 
 * <p>NOTE: A {@link CRC32} is not a cryptographic hash!
 */
public class MessageDigestCRC32 extends MessageDigest {

    /**
     * The name of the algorithm.
     */
    public static final String ALGORITHM = "CRC32";
    
    /**
     * The length of the hash in bytes.
     */
    public static final int LENGTH = 4;
    
    private final CRC32 crc = new CRC32();
    
    public MessageDigestCRC32() {
        super(ALGORITHM);
    }
    
    @Override
    protected int engineGetDigestLength() {
        return LENGTH;
    }
    
    @Override
    protected byte[] engineDigest() {
        long value = crc.getValue();
        return new byte[] {
            (byte)((value >> 24L) & 0xFF),
            (byte)((value >> 16L) & 0xFF),
            (byte)((value >>  8L) & 0xFF),
            (byte)((value       ) & 0xFF),
        };
    }

    @Override
    protected void engineReset() {
        crc.reset();
    }

    @Override
    protected void engineUpdate(byte input) {
        crc.update(input);
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        crc.update(input, offset, len);
    }
}
