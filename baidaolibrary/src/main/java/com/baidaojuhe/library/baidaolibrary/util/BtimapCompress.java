/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.box.app.library.util.ILogCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by box on 2017/11/6.
 * <p>
 * 图片压缩
 */

public class BtimapCompress {

    private static final String TAG = BtimapCompress.class.getSimpleName();

    /**
     * 根据分辨率压缩
     *
     * @param srcPath   图片路径
     * @param ImageSize 图片大小 单位kb
     * @return 是否压缩成功
     */
    public static boolean compressBitmap(String srcPath, int ImageSize, String savePath) {
        int subtract;
        ILogCompat.i(TAG, "图片处理开始..");
        Bitmap bitmap = compressByResolution(srcPath, 1024, 720); //分辨率压缩
        if (bitmap == null) {
            ILogCompat.i(TAG, "bitmap 为空");
            return false;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ILogCompat.i(TAG, "图片分辨率压缩后：" + baos.toByteArray().length / 1024 + "KB");

        while (baos.toByteArray().length > ImageSize * 1024) { //循环判断如果压缩后图片是否大于ImageSize kb,大于继续压缩
            subtract = setSubstractSize(baos.toByteArray().length / 1024);
            baos.reset();//重置baos即清空baos
            options -= subtract;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            ILogCompat.i(TAG, "图片压缩后：" + baos.toByteArray().length / 1024 + "KB");
        }
        ILogCompat.i(TAG, "图片处理完成!" + baos.toByteArray().length / 1024 + "KB");
        try {
            FileOutputStream fos = new FileOutputStream(new File(savePath));//将压缩后的图片保存的本地上指定路径中
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmap.recycle();

        return true; //压缩成功返回ture
    }

    /**
     * 根据分辨率压缩图片比例
     */
    private static Bitmap compressByResolution(String imgPath, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, opts);

        int width = opts.outWidth;
        int height = opts.outHeight;
        int widthScale = width / w;
        int heightScale = height / h;

        int scale;
        if (widthScale < heightScale) { //保留压缩比例小的
            scale = widthScale;
        } else {
            scale = heightScale;
        }

        if (scale < 1) {
            scale = 1;
        }
        ILogCompat.i(TAG, "图片分辨率压缩比例：" + scale);

        opts.inSampleSize = scale;

        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imgPath, opts);
    }

    /**
     * 根据图片的大小设置压缩的比例，提高速度
     */
    private static int setSubstractSize(int imageMB) {
        if (imageMB > 1000) {
            return 60;
        } else if (imageMB > 750) {
            return 40;
        } else if (imageMB > 500) {
            return 20;
        } else {
            return 10;
        }
    }
}
