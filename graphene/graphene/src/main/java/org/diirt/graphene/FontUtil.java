/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
public class FontUtil {

    private static Font liberationSansRegular = loadFont("LiberationSans-Regular.ttf");

    private static Font loadFont(String name) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, FontUtil.class.getResourceAsStream(name));
            return font.deriveFont(Font.PLAIN, 10);
        } catch (FontFormatException ex) {
            Logger.getLogger(FontUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FontUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Couldn't load");
    }

    public static Font getLiberationSansRegular() {
        return liberationSansRegular;
    }

}
