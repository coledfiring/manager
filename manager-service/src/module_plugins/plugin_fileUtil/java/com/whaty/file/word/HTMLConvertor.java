package com.whaty.file.word;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * html转word的转换器
 *
 * @author weipengsen
 */
public class HTMLConvertor {

    private final static String UTF_8_CHARSET = "UTF-8";

    private final static String WORD_DOCUMENT = "WordDocument";

    /**
     * 从html转换为word
     * @param htmlStr
     * @param outputStream
     */
    public void convertFromHTML(String htmlStr, OutputStream outputStream) throws IOException {
        try (ByteArrayInputStream byteInput = new ByteArrayInputStream(htmlStr.getBytes(UTF_8_CHARSET))) {
            POIFSFileSystem fs = new POIFSFileSystem();
            fs.getRoot().createDocument(WORD_DOCUMENT, byteInput);
            fs.writeFilesystem(outputStream);
        }
    }

}
