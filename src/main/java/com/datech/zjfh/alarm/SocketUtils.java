package com.datech.zjfh.alarm;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SocketUtils {

    static final int HEAD_SIZE = 10;
    static final int TOTAL_SIZE = 14;
    static int cmd;



    static void write_short_le(byte[] buf, int offset, short value) {
        buf[offset + 1] = (byte) ((value >> 8) & 0xff);//说明一
        buf[offset + 0] = (byte) ((value) & 0xff);
    }

    static void write_int_le(byte[] buf, int offset, int value) {
        buf[offset + 3] = (byte) ((value >> 24) & 0xff);//说明一
        buf[offset + 2] = (byte) ((value >> 16) & 0xff);
        buf[offset + 1] = (byte) ((value >> 8) & 0xff);
        buf[offset + 0] = (byte) (value & 0xff);
    }

    static void write_bytes(byte[] src, int src_offset, byte[] dst, int dst_offset) {
        for (int i = 0; i < src.length - src_offset; ++i) {
            dst[dst_offset + i] = src[src_offset + i];
        }
    }

    static short read_short_le(byte[] data, int offset) {
        int ret = (data[offset] | (data[offset + 1] << 8)) & 0xFF;
        return (short) ret;
    }

    public static int read_int_le(byte[] data, int offset) {
        int ret = ((data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8) | ((data[offset + 2] & 0xFF << 16)) | ((data[offset + 3] & 0xFF << 24)));
        return ret;
    }

    /**
     * 解析byteBuf内容
     *
     * @param msg
     * @return
     */
    public static String parseByteBuff(ByteBuf msg) {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        log.debug("msg before covert: " + new String(bytes));
        return parse(bytes);
    }

    /**
     * 解析tcp body
     *
     * @param bytes
     * @return
     */
    public static String parse(byte[] bytes) {

        int offset = 0;
        int plen = read_int_le(bytes, offset);
        offset += 4;//pkgLen
        offset += 4;//checkSum
        cmd = read_short_le(bytes, offset);
        offset += 2;//cmd
        offset += 2;//target
        offset += 2;//retCode
        int content_size = (plen - HEAD_SIZE);
        byte[] content_buf = new byte[content_size];
        write_bytes(bytes, offset, content_buf, 0);
        return new String(content_buf);
    }

    public static final String CMD = "CMD";
    public static final String BODY = "BODY";

    /**
     * 解析byteBuf内容
     *
     * @param msg
     * @return
     */
    public static Map<String, Object> parseByteBuffMap(ByteBuf msg) {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        log.debug("msg before covert: " + new String(bytes));
        return parseMap(bytes);
    }

    /**
     * 解析tcp body
     *
     * @param bytes
     * @return
     */
    public static Map<String, Object> parseMap(byte[] bytes) {

        int offset = 0;
        int plen = read_int_le(bytes, offset);
        offset += 4;//pkgLen
        offset += 4;//checkSum
        int cmdx = read_short_le(bytes, offset);
        offset += 2;//cmd
        offset += 2;//target
        offset += 2;//retCode
        int content_size = (plen - HEAD_SIZE);
        byte[] content_buf = new byte[content_size];
        write_bytes(bytes, offset, content_buf, 0);
        return new HashMap<String, Object>() {{
            put(CMD, cmdx);
            put(BODY, new String(content_buf));
        }};
    }

    /**
     * 打包tcp body
     *
     * @param content
     * @param cmd
     * @return
     */
    public static byte[] pack(byte[] content, int cmd) {
        int total_size = content.length + TOTAL_SIZE;
        int pkgLen = total_size - 4;
        int offset = 0;
        byte[] msg = new byte[total_size];

        write_int_le(msg, offset, pkgLen);
        offset += 4;//pkgLen
        write_int_le(msg, offset, 0);
        offset += 4;//checkSum
        write_short_le(msg, offset, (short) cmd);
        offset += 2;//cmd
        write_short_le(msg, offset, (short) 0);
        offset += 2;//target
        write_short_le(msg, offset, (short) 0);
        offset += 2;//retCode
        write_bytes(content, 0, msg, offset);

        return msg;
    }
}
