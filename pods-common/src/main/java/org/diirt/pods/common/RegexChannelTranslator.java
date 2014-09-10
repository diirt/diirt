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
    private final boolean readOnly;

    public RegexChannelTranslator(String regex, String substitution, boolean readOnly) {
        p = Pattern.compile(regex);
        this.regex = regex;
        this.substitution = substitution;
        this.readOnly = readOnly;
    }

    @Override
    public ChannelTranslation translate(String channelName) {
        Matcher matcher = p.matcher(channelName);
        if (matcher.matches()) {
            String translation = matcher.replaceFirst(substitution);
            return new ChannelTranslation(translation, readOnly);
        } else {
            return null;
        }
    }
    
}
