/*
 * Copyright 2017 By_syk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.by_syk.lib.checkimgformat;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>对图片文件进行常见格式检查：「.jpg」、「.png」、「.gif」、「.bmp」</p>
 * <p>首先会检查文件后缀；然后判断魔术数字，即检查文件数据流的前几个字节。参考冯立彬的博客：<a href="http://blog.csdn.net/fenglibing/article/details/7728275">使用JAVA如何对图片进行格式检查以及安全检查处理</a></p>
 * <p>注：这些检查手段非常基础，请勿用于安全要求场景，但可作为前期判断。</p>
 */

public class CheckImgFormat {
    /**
     * JPG/JPEG 格式
     */
    public static final String FORMAT_JPG = "image/jpeg";

    /**
     * PNG 格式
     */
    public static final String FORMAT_PNG = "image/png";

    /**
     * GIF 格式
     */
    public static final String FORMAT_GIF = "image/gif";

    /**
     * BMP 格式
     */
    public static final String FORMAT_BMP = "image/bmp";

    /**
     * 其他或未知格式
     */
    public static final String FORMAT_UNDEFINED = "undefined";

    @StringDef({FORMAT_JPG, FORMAT_PNG, FORMAT_GIF, FORMAT_BMP, FORMAT_UNDEFINED})
    public @interface Format {}

    private CheckImgFormat() {}

    /**
     * 从图片文件输入流获取格式。注意返回 {@link #FORMAT_UNDEFINED}
     *
     * @param imgStream 图片文件输入流（非图片文件可能得到错误结果）
     */
    @CheckImgFormat.Format
    public static String get(@Nullable InputStream imgStream) {
        byte[] bytes = getBytes(imgStream, 8);
        String hex = byte2Hex(bytes, true);
        if (hex == null) {
            return FORMAT_UNDEFINED;
        }
        if (hex.startsWith("ffd8")) {
            return FORMAT_JPG;
        }
        if (hex.startsWith("89504e47")) {
            return FORMAT_PNG;
        }
        if (hex.startsWith("47494638")) {
            return FORMAT_GIF;
        }
        if (hex.startsWith("424d")) {
            return FORMAT_BMP;
        }
        return FORMAT_UNDEFINED;
    }

    /**
     * 从图片文件获取格式。注意返回 {@link #FORMAT_UNDEFINED}
     *
     * @param imgFile 图片文件（非图片文件可能得到错误结果）
     */
    @CheckImgFormat.Format
    public static String get(@Nullable File imgFile) {
        if (!checkFile(imgFile)) {
            return FORMAT_UNDEFINED;
        }

        try {
            return get(new FileInputStream(imgFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return FORMAT_UNDEFINED;
    }

    /**
     * 从图片 URI 获取格式。注意返回 {@link #FORMAT_UNDEFINED}
     *
     * @param imgUri 图片 URI（非图片 URI 可能得到错误结果）
     */
    @CheckImgFormat.Format
    public static String get(@NonNull Context context, @Nullable Uri imgUri) {
        if (imgUri == null) {
            return FORMAT_UNDEFINED;
        }
        try {
            return get(context.getContentResolver().openInputStream(imgUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return FORMAT_UNDEFINED;
    }

    /**
     * 与目标格式进行匹配 {@link #get(InputStream)}
     *
     * @param format 目标格式
     */
    public static boolean is(@Format String format, @Nullable InputStream imgStream) {
        return format.equals(get(imgStream));
    }

    /**
     * 与目标格式进行匹配 {@link #get(File)}
     *
     * @param format 目标格式
     */
    public static boolean is(@Format String format, @Nullable File imgFile) {
        return format.equals(get(imgFile));
    }

    /**
     * 与目标格式进行匹配 {@link #get(Context, Uri)}
     *
     * @param format 目标格式
     */
    public static boolean is(@Format String format, @NonNull Context context, @Nullable Uri imgUri) {
        return format.equals(get(context, imgUri));
    }

    private static boolean checkFile(@Nullable File imgFile) {
        if (imgFile == null || !imgFile.isFile()) {
            return false;
        }

        String fileName = imgFile.getName();
        int index = fileName.lastIndexOf('.');
        if (index >= 0) {
            String suffix = fileName.substring(index + 1).toLowerCase();
            return suffix.equals("jpg") || suffix.equals("jpeg")
                    || suffix.equals("png")
                    || suffix.equals("gif")
                    || suffix.equals("bmp");
        }
        return false;
    }

    @Nullable
    private static byte[] getBytes(@Nullable InputStream inputStream, @IntRange(from = 1) int byteNum) {
        if (inputStream == null) {
            return null;
        }

        byte[] bytes = new byte[byteNum];
        try {
            inputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    private static String byte2Hex(@Nullable byte[] bytes, boolean lowerOrUppercase) {
        if (bytes == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format(lowerOrUppercase ? "%02x" : "%02X", b));
        }
        return stringBuilder.toString();
    }
}
