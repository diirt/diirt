/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.diirt.vtype.ValueUtil;

/**
 * A FileFormat for reading .bmp and .png into VImage
 *
 * @author Kunal Shroff
 *
 */
public class ImageFileFormat implements FileFormat {

    @Override
    public Object readValue(InputStream in) throws Exception {
        BufferedImage image = ImageIO.read(in);
        return ValueUtil.toVImage(image);
    }

    @Override
    public void writeValue(Object value, OutputStream out) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWriteSupported() {
        return false;
    }

    @Override
    public Collection<String> getFileExtensions() {
        return Arrays.asList("bmp", "jpg", "png");
    }

}
