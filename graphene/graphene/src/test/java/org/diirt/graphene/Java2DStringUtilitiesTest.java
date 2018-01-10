/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.diirt.graphene.Java2DStringUtilities.Alignment.*;

/**
 *
 * @author carcassi
 */
public class Java2DStringUtilitiesTest {

    public Java2DStringUtilitiesTest() {
    }

    int width = 320;
    int height = 240;
    int x = 100;
    int y = 100;
    BufferedImage image;
    Graphics2D graphics;

    @Before
    public void createImage() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        graphics = (Graphics2D) image.getGraphics();
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        graphics.drawLine(x, 0, x, height);
        graphics.drawLine(0, y, width, y);
    }

    @After
    public void cleanImage() {
        image = null;
        graphics = null;
    }

    @Test
    public void drawCenteredText1() throws Exception {
        if (!"Windows 7".equals(System.getProperty("os.name"))) {
            return;
        }
        Java2DStringUtilities.drawString(graphics, CENTER, x, y, "ABCD");
        ImageAssert.compareImages("textUtilities.1", image);
    }

    @Test
    public void drawCenteredText2() throws Exception {
        graphics.setFont(FontUtil.getLiberationSansRegular().deriveFont(Font.PLAIN, 10));
        Java2DStringUtilities.drawString(graphics, CENTER, x, y, "0");
        ImageAssert.compareImages("textUtilities.2", image);
    }

    @Test
    public void drawRightText1() throws Exception {
        if (!"Windows 7".equals(System.getProperty("os.name"))) {
            return;
        }
        Java2DStringUtilities.drawString(graphics, RIGHT, x, y, "ABCD");
        ImageAssert.compareImages("textUtilities.3", image);
    }

    @Test
    public void drawRightText2() throws Exception {
        graphics.setFont(FontUtil.getLiberationSansRegular().deriveFont(Font.PLAIN, 10));
        Java2DStringUtilities.drawString(graphics, RIGHT, x, y, "ABCD");
        ImageAssert.compareImages("textUtilities.4", image);
    }

    @Test
    public void drawBottomRightText1() throws Exception {
        if (!"Windows 7".equals(System.getProperty("os.name"))) {
            return;
        }
        Java2DStringUtilities.drawString(graphics, BOTTOM_RIGHT, x, y, "ABCD");
        ImageAssert.compareImages("textUtilities.5", image);
    }

    @Test
    public void drawBottomRightText2() throws Exception {
        graphics.setFont(FontUtil.getLiberationSansRegular().deriveFont(Font.PLAIN, 10));
        Java2DStringUtilities.drawString(graphics, BOTTOM_RIGHT, x, y, "ABCD");
        ImageAssert.compareImages("textUtilities.6", image);
    }

    @Test
    public void drawTopRightText1() throws Exception {
        if (!"Windows 7".equals(System.getProperty("os.name"))) {
            return;
        }
        Java2DStringUtilities.drawString(graphics, TOP_RIGHT, x, y, "ABCD");
        ImageAssert.compareImages("textUtilities.7", image);
    }

    @Test
    public void drawTopRightText2() throws Exception {
        graphics.setFont(FontUtil.getLiberationSansRegular().deriveFont(Font.PLAIN, 10));
        Java2DStringUtilities.drawString(graphics, TOP_RIGHT, x, y, "ABCD");
        ImageAssert.compareImages("textUtilities.8", image);
    }
}
