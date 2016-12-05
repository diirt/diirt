/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype;

import org.diirt.util.array.ListNumber;

/**
 * Represent an image. Use {@link ValueUtil#toImage(org.diirt.vtype.VImage) }
 * and {@link ValueUtil#toVImage(java.awt.image.BufferedImage)} to convert objects
 * of this class to and from awt images.
 * 
 *
 * @author carcassi
 */
public interface VImage extends VType, Alarm, Time {

    /**
     * Image type constants
     */

    public static int TYPE_CUSTOM = 0;
    /**
     * Monochromatic image
     */
    public static int TYPE_MONO = 1;
    /**
     * Bayer pattern image, 1 value per pixel but with color filter on detector
     */
    public static int TYPE_BAYER = 2;
    /**
     * RGB image with pixel color interleave, data array is [3, NX, NY]
     */
    public static int TYPE_RGB1 = 3;
    /**
     * RGB image with row color interleave, data array is [NX, 3, NY]
     */
    public static int TYPE_RGB2 = 4;
    /**
     * RGB image with plane color interleave, data array is [NX, NY, 3]
     */
    public static int TYPE_RGB3 = 5;
    /**
     * YUV image, 3 bytes encodes 1 RGB pixel
     */
    public static int TYPE_YUV444 = 6;
    /**
     * YUV image, 4 bytes encodes 2 RGB pixel
     */
    public static int TYPE_YUV422 = 7;
    /**
     * YUV image, 6 bytes encodes 4 RGB pixels
     */
    public static int TYPE_YUV411 = 8;
    /**
     * An image with 8-bit RGB color components, corresponding to a
     * Windows-style BGR color model with the colors Blue, Green, and Red
     * stored in 3 bytes.
     */
    public static int TYPE_3BYTE_BGR = 9;

    /**
     * Height of the image in pixels.
     *
     * @return image height
     */
    public int getHeight();

    /**
     * Width of the image in pixels.
     *
     * @return image width
     */
    public int getWidth();

    /**
     * Image data;
     *
     * @return image data
     */
    public ListNumber getData();
    
    /**
     * Describes the type in which the data is stored
     * {@link VImageDataType}
     * 
     * @return image data type 
     */
    public VImageDataType getDataType();

    /**
     * Returns the image type, The image type describes the mechanism in which
     * the data is encoded and how it can be converted to something that can be
     * rendered.
     * 
     * @see #TYPE_MONO
     * @see #TYPE_BAYER
     * @see #TYPE_RGB1
     * @see #TYPE_RGB2
     * @see #TYPE_RGB3
     * @see #TYPE_YUV444
     * @see #TYPE_YUV422
     * @see #TYPE_YUV411
     * @see #TYPE_3BYTE_BGR
     * 
     * @return the image type 
     */
    public int getVImageType();

}
