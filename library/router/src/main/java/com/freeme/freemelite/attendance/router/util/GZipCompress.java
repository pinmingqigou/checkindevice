package com.freeme.freemelite.attendance.router.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

public class GZipCompress {
    public static final int BUFFER = 1024;

    public static byte[] deflater(String buff) {
        if ((buff == null) || (buff.length() == 0)) {
            return null;
        }

        byte[] input = (byte[]) null;
        try {
            input = buff.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int len = input.length;

        byte[] output = new byte[len];
        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        compresser.end();

        return output;
    }

    /**
     * 压缩
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        compress(bais, baos);

        byte[] output = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return output;
    }

    /**
     * 解压
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        decompress(bais, baos);

        data = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return data;
    }

    public static void compress(InputStream is, OutputStream os) throws Exception {
        os.flush();
        GZIPOutputStream gos = new GZIPOutputStream(os);

        byte[] data = new byte[1024];
        int count;
        while ((count = is.read(data, 0, 1024)) != -1) {
            gos.write(data, 0, count);
        }

//        gos.finish();

        gos.flush();
        gos.close();
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(is);

        byte[] data = new byte[1024];
        int count;
        while ((count = gis.read(data, 0, 1024)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    public static String inflater(byte[] buff) {
        String str = null;
        int len = 0;
        if (buff != null) {
            len = buff.length;
        }
        byte[] result = new byte[len];
        if ((buff == null) || (len == 0))
            return null;
        try {
            Inflater decompresser = new Inflater();
            int resultlen = 0;
            decompresser.setInput(buff, 0, buff.length);
            resultlen = decompresser.inflate(result);
            decompresser.end();
            str = new String(result, 0, resultlen, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        }
        return str;
    }
}