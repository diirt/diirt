/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.epics.util.array.CollectionNumbers;
import org.epics.vtype.Alarm;
import org.epics.vtype.Time;
import org.epics.vtype.VImage;
import org.epics.vtype.VImageDataType;
import org.epics.vtype.VImageType;


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
        byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        return VImage.of(image.getHeight(), image.getWidth(), CollectionNumbers.toListByte(buffer), VImageDataType.pvByte, VImageType.TYPE_3BYTE_BGR, Alarm.none(), Time.now());

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
