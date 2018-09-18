package com.hzgc.jniface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class FaceFunction implements Serializable {

    private static final String SPLIT = ":";

    static {
        System.loadLibrary("JNILIB");
    }

    /**
     * 算法初始化操作，此处为初始化一次,若需要调用n次可以循环调用次方法
     * 算法初始化次数增加在一定程度上会提高特征提取效率,但是会没初始化一次
     * 会占用一定的显存,所以需要适量
     */
    public static native void init();

    /**
     * 算法初始化操作，此处可指定初始化次数,若需要调用n次可以循环调用次方法
     * 算法初始化次数增加在一定程度上会提高特征提取效率,但是会没初始化一次
     * 会占用一定的显存,所以需要适量
     */
    public static native void initThreadNum(int threadNum);

    /**
     * 大图检测功能,此方法可以同时检测人,车,脸三种
     *
     * @param pictureStream 二进制图片
     * @param pictureFormat 图片格式
     * @return 检测结果,检测不到返回 null
     */
    public static native ArrayList<SmallImage> bigPictureCheck(byte[] pictureStream, String pictureFormat);

    /**
     * 大图检测功能,此方法只能检测人脸图片
     *
     * @param pictureStream 二进制图片
     * @param pictureFormat 图片格式
     * @return 检测结果,检测不到返回 null
     */
    public static native ArrayList<SmallImage> faceCheck(byte[] pictureStream, String pictureFormat);

    /**
     * 大图检测功能,此方法只能检测行人图片
     *
     * @param pictureStream 二进制图片
     * @param pictureFormat 图片格式
     * @return 检测结果,检测不到返回 null
     */
    public static native ArrayList<SmallImage> personCheck(byte[] pictureStream, String pictureFormat);

    /**
     * 大图检测功能,此方法只能检测车辆图片
     *
     * @param pictureStream 二进制图片
     * @param pictureFormat 图片格式
     * @return 检测结果,检测不到返回 null
     */
    public static native ArrayList<SmallImage> carCheck(byte[] pictureStream, String pictureFormat);

    /**
     * 大图检测功能,此方法可同时检测人脸和行人图片
     *
     * @param pictureStream 二进制图片
     * @param pictureFormat 图片格式
     * @return 检测结果,检测不到返回 null
     */
    public static native ArrayList<SmallImage> faceAndPersonCheck(byte[] pictureStream, String pictureFormat);

    /**
     * 大图检测功能,此方法可同时检测人脸和车辆图片
     *
     * @param pictureStream 二进制图片
     * @param pictureFormat 图片格式
     * @return 检测结果,检测不到返回 null
     */
    public static native ArrayList<SmallImage> faceAndCarCheck(byte[] pictureStream, String pictureFormat);

    /**
     * 大图检测功能,此方法可同时检测行人和车辆图片
     *
     * @param pictureStream 二进制图片
     * @param pictureFormat 图片格式
     * @return 检测结果,检测不到返回 null
     */
    public static native ArrayList<SmallImage> personAndCarCheck(byte[] pictureStream, String pictureFormat);

    /**
     * 人脸特征提取
     *
     * @param pictureStream 小图二进制图片
     * @param pictureFormat 图片格式
     * @return 提取结果,提取不到则返回 null
     */
    public static native FaceAttribute faceFeatureExtract(byte[] pictureStream, String pictureFormat);


    /**
     * 人脸比对粗筛
     * @param featureList 要查询的特征值集合,即可查单条或者多条
     * @param queryList 被比对的特征值集合
     * @param topN 取前 n 条
     * @return 比对结果
     */
    public static native ArrayList<CompareResult> faceCompareBit(byte[][] featureList, byte[][] queryList, int topN);

    /**
     * 人脸比对精筛
     * @param diku  数据底库
     * @param queryList 图片数据
     * @param topN 取前 n 条
     * @return 比对结果
     */
    public static native ArrayList<CompareResult> faceCompareFloat(float[][] diku, float[][] queryList, int topN);

    /**
     * 特征提取方法 （内）（赵喆）
     *
     * @param imageData 将图片转为字节数组传入
     * @return 输出float[]形式的特征值
     */
    public static FaceAttribute featureExtract(byte[] imageData) {
        BufferedImage faceImage;
        try {
            if (null != imageData) {
                FaceAttribute faceAttribute = new FaceAttribute();
                int successOrfailue;
                faceImage = ImageIO.read(new ByteArrayInputStream(imageData));
                int height = faceImage.getHeight();
                int width = faceImage.getWidth();
                int[] rgbArray = new int[height * width * 3];
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        int pixel = faceImage.getRGB(w, h);//RGB颜色模型
                        rgbArray[h * width * 3 + w * 3] = (pixel & 0xff0000) >> 16;//0xff0000 红色
                        rgbArray[h * width * 3 + w * 3 + 1] = (pixel & 0xff00) >> 8;
                        rgbArray[h * width * 3 + w * 3 + 2] = (pixel & 0xff);
                    }
                }
                successOrfailue = NativeFunction.feature_extract(faceAttribute, rgbArray, width, height);
                if (successOrfailue == 0) {
                    return faceAttribute;
                } else {
                    return new FaceAttribute();
                }
            } else {
                throw new NullPointerException("The data of picture is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FaceAttribute();
    }

    /**
     * 将特征值（String）转换为特征值（float[]）（内）（赵喆）
     *
     * @param feature 传入编码为UTF-8的String
     * @return 返回float[]类型的特征值
     */
    public static float[] string2floatArray(String feature) {
        if (feature != null && feature.length() > 0) {
            float[] featureFloat = new float[512];
            String[] strArr = feature.split(SPLIT);
            for (int i = 0; i < strArr.length; i++) {
                featureFloat[i] = Float.valueOf(strArr[i]);
            }
            return featureFloat;
        }
        return new float[0];
    }

    /**
     * 相似度比较，范围[0-1]
     *
     * @param currentFeatureStr 需要比对的特征值
     * @param historyFeatureStr 库中的特征值
     * @return 相似度
     */
    public static float featureCompare(String currentFeatureStr, String historyFeatureStr) {
        float[] currentFeature = FaceFunction.string2floatArray(currentFeatureStr);
        float[] historyFeature = FaceFunction.string2floatArray(historyFeatureStr);
        return featureCompare(currentFeature, historyFeature);
    }

    public static float featureCompare(float[] currentFeature, float[] historyFeature) {
        double similarityDegree = 0;
        double currentFeatureMultiple = 0;
        double historyFeatureMultiple = 0;
        for (int i = 0; i < currentFeature.length; i++) {
            similarityDegree = similarityDegree + currentFeature[i] * historyFeature[i];
            currentFeatureMultiple = currentFeatureMultiple + Math.pow(currentFeature[i], 2);//pow 返回currentFeature[i] 平方
            historyFeatureMultiple = historyFeatureMultiple + Math.pow(historyFeature[i], 2);
        }

        double tempSim = similarityDegree / Math.sqrt(currentFeatureMultiple) / Math.sqrt(historyFeatureMultiple);//sqrt 平方根 余弦相似度
        double actualValue = new BigDecimal((0.5 + (tempSim / 2)) * 100).//余弦相似度表示为cosineSIM=0.5cosθ+0.5
                setScale(2, BigDecimal.ROUND_HALF_UP).//ROUND_HALF_UP=4 保留两位小数四舍五入
                doubleValue();
        if (actualValue >= 100) {
            return 100;
        }
        return (float) actualValue;
    }

    /**
     * 将特征值（float[]）转换为字符串（String）（内）（赵喆）
     *
     * @param feature 传入float[]类型的特征值
     * @return 输出指定编码为UTF-8的String
     */
    public static String floatArray2string(float[] feature) {
        if (feature != null && feature.length == 512) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < feature.length; i++) {
                if (i == 511) {
                    sb.append(feature[i]);
                } else {
                    sb.append(feature[i]).append(":");
                }
            }
            return sb.toString();
        }
        return "";
    }

}
