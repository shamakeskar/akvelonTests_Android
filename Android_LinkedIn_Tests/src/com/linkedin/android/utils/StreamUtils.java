package com.linkedin.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The class contains util methods to work with streams.
 * 
 * @author vasily.gancharov
 * @created Sep 5, 2012 19:30:31 PM
 */
public class StreamUtils {

    /**
     * Copies data from {@code inputStream} and convert it to {@code String}
     * 
     * @param inputStream
     *          source {@code InputStream}
     * @return {@code inputStream} data as {@code String}
     * @throws IOException 
     */
    public static String CopyStreamData(InputStream inputStream) throws IOException {

        final int bufferSize=1024;
        final int bufferReadStartPosition = 0;

        if (null == inputStream)
        {
            return StringDefaultValues.EMPTY_STRING;
        }

        char[] buffer = new char[bufferSize];
        int numRead;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String readData;

        while ((numRead = bufferedReader.read(buffer)) != -1) {
            readData = String.valueOf(buffer, bufferReadStartPosition, numRead);
            stringBuilder.append(readData);
            buffer = new char[bufferSize];
        }

        bufferedReader.close();
        inputStreamReader.close();
        String stringReadFromInputStream = stringBuilder.toString();
        return stringReadFromInputStream;
    }
}
