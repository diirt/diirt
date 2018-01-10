/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.image.BufferedImage;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import java.awt.Font;
import java.awt.Color;

/**
 *
 *
 * @author Jiakung
 */
public abstract class BaseGraphTest<T extends Graph2DRendererUpdate<T>, S extends Graph2DRenderer<T>> {

    private final String resultPrefix;

    public BaseGraphTest(String resultPrefix) {
        this.resultPrefix = resultPrefix + ".base.";
    }

    public abstract S createRenderer();

    public abstract BufferedImage draw(S renderer);

    @Test
    public void rightMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().rightMargin(10));
        ImageAssert.compareImages(resultPrefix + "rightMargin", draw(renderer));
    }

    @Test
    public void leftMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().leftMargin(10));
        ImageAssert.compareImages(resultPrefix + "leftMargin", draw(renderer));
    }

    @Test
    public void backgroundColor() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().backgroundColor(Color.BLUE));
        ImageAssert.compareImages(resultPrefix + "backgroundColor", draw(renderer));
    }

    @Test
    public void labelColor() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().labelColor(Color.GREEN));
        ImageAssert.compareImages(resultPrefix + "labelColor", draw(renderer));
    }

    @Test
    public void labelFont() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().labelFont(FontUtil.getLiberationSansRegular().deriveFont(Font.BOLD, 12)));
        ImageAssert.compareImages(resultPrefix + "labelFont", draw(renderer));
    }

    @Test
    public void bottomMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().bottomMargin(10));
        ImageAssert.compareImages(resultPrefix + "bottomMargin", draw(renderer));
    }

    @Test
    public void topMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().topMargin(10));
        ImageAssert.compareImages(resultPrefix + "topMargin", draw(renderer));
    }

    @Test
    public void allMargins() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().rightMargin(10));
        renderer.update(renderer.newUpdate().leftMargin(10));
        renderer.update(renderer.newUpdate().bottomMargin(10));
        renderer.update(renderer.newUpdate().topMargin(10));
        ImageAssert.compareImages(resultPrefix + "allMargins", draw(renderer));
    }

    @Test
    public void xLabelMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().xLabelMargin(10));
        ImageAssert.compareImages(resultPrefix + "xLabelMargin", draw(renderer));
    }

    @Test
    public void yLabelMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().yLabelMargin(10));
        ImageAssert.compareImages(resultPrefix + "yLabelMargin", draw(renderer));
    }

    @Test
    public void allLabelMargins() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().xLabelMargin(10));
        renderer.update(renderer.newUpdate().yLabelMargin(10));
        ImageAssert.compareImages(resultPrefix + "allLabelMargins", draw(renderer));
    }

    @Test
    public void bottomAreaMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().bottomAreaMargin(10));
        ImageAssert.compareImages(resultPrefix + "bottomAreaMargin", draw(renderer));
    }

    @Test
    public void topAreaMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().topAreaMargin(10));
        ImageAssert.compareImages(resultPrefix + "topAreaMargin", draw(renderer));
    }

    @Test
    public void leftAreaMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().leftAreaMargin(10));
        ImageAssert.compareImages(resultPrefix + "leftAreaMargin", draw(renderer));
    }

    @Test
    public void xrightAreaMargin() throws Exception {
        S renderer = createRenderer();
        renderer.update(renderer.newUpdate().rightAreaMargin(10));
        ImageAssert.compareImages(resultPrefix + "rightAreaMargin", draw(renderer));
    }
}
