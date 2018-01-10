/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

/**
 * Puts the image in a panel.
 *
 * @author carcassi
 */
public class ImagePanel extends JComponent {

    private Image image;

    /**
     * Sets the image of the panel.
     * @param image image to put in the panel
     */
    public void setImage(Image image) {
        this.image = image;
        if (image != null) {
            setSize(new Dimension(image.getWidth(this), image.getHeight(this)));
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        } else {

        }
        revalidate();
        repaint();
    }

    /**
     * Gets the image of the panel
     * @return image stored in the panel
     */
    public Image getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
