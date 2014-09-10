/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author carcassi
 */
class RegexChannelTranslator extends ChannelTranslator {
    private final Pattern p;
    private final String regex;
    private final String substitution;
    private final ChannelTranslation.Permission permission;

    public RegexChannelTranslator(String regex, String substitution, ChannelTranslation.Permission permission) {
        p = Pattern.compile(regex);
        this.regex = regex;
        this.substitution = substitution;
        this.permission = permission;
    }

    @Override
    public ChannelTranslation translate(String channelName) {
        Matcher matcher = p.matcher(channelName);
        if (matcher.matches()) {
            String translation;
            if (substitution != null) {
                translation = matcher.replaceFirst(substitution);
            } else {
                translation = channelName;
            }
            return new ChannelTranslation(translation, permission);
        } else {
            return null;
        }
    }
    
}
