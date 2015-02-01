/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import org.diirt.vtype.VImage;
import org.diirt.vtype.ValueUtil;

public final class VImageView extends BorderPane {
    
    private ImageView imageView;

    public VImageView() {
        imageView = new ImageView();
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(widthProperty());
        setCenter(imageView);
	setMinSize( 0 , 0 );
    }
    
    public void setVImage(VImage image) {
	if ( image != null ) {
	    Image newImage = SwingFXUtils.toFXImage(ValueUtil.toImage(image), (WritableImage) imageView.getImage());
	    imageView.setImage(newImage);
	}
	else {
	    imageView.setImage( null );
	}
    }

}
