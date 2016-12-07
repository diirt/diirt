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
    public VImageType getVImageType();

}
