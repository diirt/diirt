/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;
import java.awt.Font;

/**
 *
 * @author Jiakung
 */
public abstract class BaseGraphTest<T extends Graph2DRendererUpdate<T>, S extends Graph2DRenderer<T>> {

    private String resultPrefix;

    public BaseGraphTest(String resultPrefix) {
        this.resultPrefix = resultPrefix;
    }
    
    public abstract S createRenderer();

    public abstract BufferedImage draw(S renderer);

    @Test
    public void rightMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().rightMargin(10));
        ImageAssert.compareImages(resultPrefix + "RightMargin", draw(renderer));
    }

    @Test
    public void leftMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().leftMargin(10));
        ImageAssert.compareImages(resultPrefix + "RightMargin", draw(renderer));
    }
}
