/*		 
 * Neurpheus - Morfological Analyser
 *
 * Copyright (C) 2006 Jakub Strychowski
 *
 *  This library is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation; either version 2.1 of the License, or (at your option)
 *  any later version.
 *
 *  This library is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 */
package org.neurpheus.nlp.morphology.util;
import java.util.HashSet;
import java.util.Iterator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *  Encodes and decodes Unicode characters using a single byte representation.
 *  This class ensures that characters from a particular alphabet, which contains no more
 *  then 255 signs, can be encoded on a single byte or even on few bits.
 *
 * @author  Jakub Strychowski
 */
public class Char2ByteConverter {

    /** Holds version of the format of data serialized by this class. */
    private static final byte VERSION_1 = 0;

    /** An array useful for converting bytes to Unicode characters. */
    private char[] byte2char;

    /** An array useful for converting Unicode characters to bytes. */
    private byte[] char2byte;

    /**
     *  Holds the maximum code of a character which can be converted.
     *  All characters represented by higher codes are treated as unknow
     *  characters during conversion and should be converted to <b>maxByte + 1</b>.
     */
    private char maxChar;

    /**
     *  Holds the maximum value of a byte which can be converted.
     *  All bytes representing higher values are treated as unknow
     *  characters during conversion and should be converted to <b>null</b>.
     */
    private byte maxByte;

    /**
     * Creates a new instance of Char2ByteConverter.
     */
    public Char2ByteConverter() {
        byte2char = new char[0];
        char2byte = new byte[0];
        maxChar = Character.MIN_VALUE;
        maxByte = -1;
    }

    /**
     *  Creates a new instance of Char2ByteConverter and uses given array of strings
     *  to initialize conversion tables (see {@link #addCharacters(String[])}).
     *
     *  @param examples example strings which contain texts written in a particular alphabet.
     */
    public Char2ByteConverter(final String[] examples) {
        byte2char = new char[0];
        char2byte = new byte[0];
        maxChar = Character.MIN_VALUE;
        maxByte = -1;
        addCharacters(examples);
    }

    /**
     *  Creates a new instance of Char2ByteConverter and uses given string
     *  to initialize conversion tables.
     *
     * @param example text written in a particular alphabet.
     */
    public Char2ByteConverter(final String example) {
        byte2char = new char[0];
        char2byte = new byte[0];
        maxChar = Character.MIN_VALUE;
        maxByte = -1;
        addCharacters(example);
    }

    /**
     *  Creates a mapping between characters and bytes analysing given array of texts.
     *
     * @param examples table of texts written in a particular alphabet.
     */
    public void addCharacters(final String[] examples) {
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < examples.length; i++) {
            if (examples[i] != null && examples[i].length() > 0) {
                tmp.append(examples[i]);
            }
        }
        addCharacters(tmp.toString());
    }

    /**
     *  Creates a mapping between characters and bytes analysing given text.
     *
     * @param example text written in a particular alphabet.
     */
    public void addCharacters(final String example) {
        HashSet tmp = new HashSet();
        if (byte2char != null) {
            for (int i = 1; i < byte2char.length; i++) {
                tmp.add((new Character(byte2char[i])));
            }
        }
        char[] tab = example.toCharArray();
        for (int i = tab.length - 1; i >= 0; i--) {
            tmp.add(new Character(tab[i]));
        }
        if (tmp.size() > Byte.MAX_VALUE) {
            throw new java.lang.AssertionError(
                    "To many characters to code. Max characters number is 255. Found "
                    + tmp.size() + " characters to code!");
        }
        maxByte = (byte) tmp.size();
        byte2char = new char[maxByte + 1];
        byte2char[0] = '?';
        maxChar = Character.MIN_VALUE;
        int index = 1;
        for (Iterator it = tmp.iterator(); it.hasNext(); index++) {
            Character ch = (Character) it.next();
            char c = ch.charValue();
            byte2char[index] = c;
            if (c > maxChar) {
                maxChar = c;
            }
        }
        determineChar2ByteArray();
    }

    /**
     *  Creates a table for converting characters to bytes.
     *  The table is created as an inversion of the byte-to-character table stored
     *  in this object.
     */
    private void determineChar2ByteArray() {
        char2byte = new byte[maxChar + 1];
        for (int i = char2byte.length - 1; i >= 0; i--) {
            char2byte[i] = 0;
        }
        for (int i = 0; i < byte2char.length; i++) {
            char2byte[byte2char[i]] = (byte) i;
        }
    }

    public char toChar(final byte b) {
        if (b > maxByte) {
            return '?';
        } else {
            return byte2char[b];
        }
    }
    
    public byte toByte(final char c) {
        if (c < char2byte.length) {
            return  char2byte[c];
        } else {
            return 0;
        }
    }
    
    /**
     *  Decodes given array of bytes to string.
     *
     * @param tab string in the encoded representation.
     * @return decoded string.
     */
    public String toString(final byte[] tab) {
        char[] tmp = new char[tab.length];
        for (int i = tab.length - 1; i >= 0; i--) {
            if (tab[i] > maxByte) {
                tmp[i] = '?';
            } else {
                tmp[i] = byte2char[tab[i]];
            }
        }
        return new String(tmp);
    }
    
    /**
     *  Decodes given array of bytes to string.
     *
     * @param tab string in the encoded representation.
     * @return decoded string.
     */
    public String toString(final byte[] tab, final int start, final int len) {
        return new String(toCharArray(tab, start, len));
    }

    /**
     *  Decodes given array of bytes to array of characters.
     *
     * @param tab string in the encoded representation.
     * @return decoded string.
     */
    public char[] toCharArray(final byte[] tab, final int start, final int len) {
        char[] tmp = new char[len];
        for (int i = 0; i < len; i++) {
            if (tab[i] > maxByte) {
                tmp[i] = '?';
            } else {
                tmp[i] = byte2char[tab[i + start]];
            }
        }
        return tmp;
    }
    

    /**
     *  Encodes given string to the array of strings representation.
     *
     * @param s string to encode.
     * @return string in the encoded representation.
     */
    public byte[] toBytes(final String s) {
        byte[] res = new byte[s.length()];
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c < char2byte.length) {
                res[i] = char2byte[c];
            } else {
                res[i] = 0;
            }
        }
        return res;
    }

    /**
     *  Writer the internal data of the converter to the given output stream.
     *
     * @param out output stream.
     * @throws java.io.IOException if an error occurred during write opperation.
     */
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(VERSION_1);
        out.writeByte(maxByte);
        out.writeChar(maxChar);
        for (int i = 0; i <= maxByte; i++) {
            out.writeChar(byte2char[i]);
        }
    }

    /**
     *  Creates a new instance of the converter reading its data from the input stream.
     *
     * @param in input stream.
     * @throws java.io.IOException  if an error occurred during read opperation.
     * @return new instance of the converter.
     */
    public static Char2ByteConverter read(final DataInputStream in) throws IOException {
        byte version = in.readByte();
        if (version != VERSION_1) {
            throw new IOException("Incorrect version of the Char2ByteCoder input data!");
        }
        Char2ByteConverter res = new Char2ByteConverter();
        res.maxByte = in.readByte();
        res.maxChar = in.readChar();
        res.byte2char = new char[res.maxByte + 1];
        for (int i = 0; i <= res.maxByte; i++) {
            res.byte2char[i] = in.readChar();
        }
        res.determineChar2ByteArray();
        return res;
    }

    /**
     *  Gets number of bytes allocated by this converter.
     *  This method returns only estimated size of the allocated memory
     *  because the number of bytes consumes by particular Java types depends on a JVM.
     *
     * @return number of bytes consumed by this converter.
     */
    public long getAllocationSize() {
        int res = JavaTypesMemoryAllocation.EMPTY_OBJECT_SIZE;
        //byte2char;
        res += JavaTypesMemoryAllocation.EMPTY_ARRAY_SIZE;
        res += this.byte2char.length * JavaTypesMemoryAllocation.CHAR_SIZE;
        //char2byte;
        res += JavaTypesMemoryAllocation.EMPTY_ARRAY_SIZE;
        res += this.char2byte.length * JavaTypesMemoryAllocation.BYTE_SIZE;
        //maxChar;
        res += JavaTypesMemoryAllocation.CHAR_SIZE;
        //maxByte;
        res += JavaTypesMemoryAllocation.BYTE_SIZE;
        return res;
    }

    /**
     *  Gets an array useful for converting bytes to characters.
     *
     * @return byte to character conversion table.
     */
    public char[] getByte2Char() {
        return byte2char;
    }

    /**
     *  Gets an array useful for converting characters to bytes.
     *
     * @return character to byte conversion table.
     */
    public byte[] getChar2Byte() {
        return char2byte;
    }

    /**
     *  Gets the maximum code of a character which can be converted.
     *  All characters represented by higher codes are treated as unknow
     *  characters during conversion and should be converted to <b>{@link #getMaxByte()} + 1</b>.
     *
     * @return maximum convertable character.
     */
    public char getMaxChar() {
        return maxChar;
    }

    /**
     *  Gets the maximum value of a byte which can be converted.
     *  All bytes representing higher values are treated as unknow
     *  characters during conversion and should be converted to <b>null</b>.
     *
     * @return maximum value of convertable byte.
     */
    public byte getMaxByte() {
        return maxByte;
    }

    /**
     * Converts characters from the given string into the destination byte array.
     * <p>
     * The first character to be copied is at index <code>srcBegin</code>;
     * the last character to be copied is at index <code>srcEnd-1</code>
     * (thus the total number of characters to be copied is
     * <code>srcEnd-srcBegin</code>). The characters are copied into the
     * subarray of <code>dst</code> starting at index <code>dstBegin</code>
     * and ending at index:
     * <p><blockquote><pre>
     *     dstbegin + (srcEnd-srcBegin) - 1
     * </pre></blockquote>
     *
     * @param      srcBegin   index of the first character in the string
     *                        to copy.
     * @param      srcEnd     index after the last character in the string
     *                        to copy.
     * @param      dst        the destination array.
     * @param      dstBegin   the start offset in the destination array.
     * @exception IndexOutOfBoundsException If any of the following
     *            is true:
     *            <ul><li><code>srcBegin</code> is negative.
     *            <li><code>srcBegin</code> is greater than <code>srcEnd</code>
     *            <li><code>srcEnd</code> is greater than the length of this
     *                string
     *            <li><code>dstBegin</code> is negative
     *            <li><code>dstBegin+(srcEnd-srcBegin)</code> is larger than
     *                <code>dst.length</code></ul>
     */
    public void getBytes(final CharSequence s, final int srcBegin, final int srcEnd, final byte dst[], final int dstBegin) {
        if (srcBegin < 0) {
            throw new StringIndexOutOfBoundsException(srcBegin);
        }
        if (srcEnd > s.length()) {
            throw new StringIndexOutOfBoundsException(srcEnd);
        }
        if (srcBegin > srcEnd) {
            throw new StringIndexOutOfBoundsException(srcEnd - srcBegin);
        }
        int dstPos = dstBegin;
        for (int i = srcBegin; i < srcEnd; i++, dstPos++) {
            char c = s.charAt(i);
            if (c <= maxChar) {
                dst[dstPos] = char2byte[c];
            } else {
                dst[dstPos] = 0;
            }
        }
    }
    
    public void getBytes(final char[] a, final int srcBegin, final int srcEnd, final byte dst[], final int dstBegin) {
        if (srcBegin < 0) {
            throw new StringIndexOutOfBoundsException(srcBegin);
        }
        if (srcEnd > a.length) {
            throw new StringIndexOutOfBoundsException(srcEnd);
        }
        if (srcBegin > srcEnd) {
            throw new StringIndexOutOfBoundsException(srcEnd - srcBegin);
        }
        int dstPos = dstBegin;
        for (int i = srcBegin; i < srcEnd; i++, dstPos++) {
            char c = a[i];
            if (c <= maxChar) {
                dst[dstPos] = char2byte[c];
            } else {
                dst[dstPos] = 0;
            }
        }
    }

}
