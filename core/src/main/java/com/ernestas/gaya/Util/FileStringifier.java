package com.ernestas.gaya.Util;

import com.ernestas.gaya.Exceptions.FileHandlingException;
import com.ernestas.gaya.Exceptions.GayaException;
import com.ernestas.gaya.Exceptions.InvalidFileException;
import com.google.common.base.Joiner;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileStringifier {

    private FileStringifier() {}

    public static String fileToString(String path) throws GayaException {
        return fileToString(new File(path));
    }

    public static String fileToString(File file) throws GayaException {
        BufferedReader reader;
        try {
            FileReader fr = new FileReader(file);
            reader = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            throw new InvalidFileException("File not found.");
        }

        if (!file.isFile()) {
            throw new InvalidFileException("File is actually not a file.");
        }

        if (!file.canRead()) {
            throw new InvalidFileException("Could not read file. Check file permissions.");
        }

        return readerToString(reader);
    }

    public static String readerToString(BufferedReader reader) throws GayaException {
        List<String> lines = new LinkedList<String>();
        try {
            String line;
            while((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            throw new FileHandlingException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new FileHandlingException(e);
            }
        }

        return Joiner.on("").skipNulls().join(lines);
    }

    public static String fileToString(InputStream resourceAsStream) throws GayaException {
        return readerToString(new BufferedReader(new InputStreamReader(resourceAsStream)));
    }
}
