/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

/**
 *
 * @author carcassi
 */
public class ImagePanel extends JComponent {

    private Image image;
    private boolean stretch = true;

    public void setImage(Image image) {
        this.image = image;
        if (image != null) {
            setSize(new Dimension(image.getWidth(this), image.getHeight(this)));
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
            //setMaximumSize(new Dimension(image.getHeight(this), image.getWidth(this)));
            //setMinimumSize(new Dimension(image.getHeight(this), image.getWidth(this)));
        } else {

        }
        revalidate();
        repaint();
    }

    public Image getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            if (stretch) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.drawImage(image, 0, 0, this);
            }
        }
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
    }

    public boolean isStretch() {
        return stretch;
    }

}
