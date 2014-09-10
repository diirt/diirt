/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.common;

/**
 * Given a channel name, it translates it to a channel or formula. This allows
 * the server to provide aliases for actual channels or formulas.
 *
 * @author carcassi
 */
public abstract class ChannelTranslator {
    
    /**
     * Translates the channel name to the actual channel or formula to connect.
     * 
     * @param channelName the incoming channel name
     * @return the translation; null if the translator can't provide one
     */
    public abstract ChannelTranslation translate(String channelName);
    
    /**
     * Creates a channel translator that uses the regex to match the channel and
     * the optional substitution string to perform the translation.
     * 
     * @param regex a regular expression
     * @param substitution the substitution string; can be null
     * @param readOnly whether to allow write or not
     * @return the translator
     */
    public static ChannelTranslator regexTranslator(String regex, String substitution, boolean readOnly) {
        return new RegexChannelTranslator(regex, substitution, readOnly);
    }
}
