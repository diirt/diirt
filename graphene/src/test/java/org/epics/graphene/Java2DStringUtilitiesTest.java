/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.epics.util.array.ArrayDouble;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.epics.graphene.Java2DStringUtilities.Alignment.*;

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
        Java2DStringUtilities.drawString(graphics, CENTER, x, y, "ABCD");
        ImageAssert.compareImages("textUtilities.1", image);
    }
    
    @Test
    public void drawCenteredText2() throws Exception {
        graphics.setFont(FontUtil.getLiberationSansRegular().deriveFont(Font.PLAIN, 10));
        Java2DStringUtilities.drawString(graphics, CENTER, x, y, "0");
        ImageAssert.compareImages("textUtilities.2", image);
    }
}
