/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.common;

import java.util.List;

/**
 *
 * @author carcassi
 */
class CompositeChannelTranslator extends ChannelTranslator {
    private final List<ChannelTranslator> translators;

    public CompositeChannelTranslator(List<ChannelTranslator> translators) {
        this.translators = translators;
    }

    @Override
    public ChannelTranslation translate(ChannelRequest request) {
        for (ChannelTranslator channelTranslator : translators) {
            ChannelTranslation translation = channelTranslator.translate(request);
            if (translation != null) {
                return translation;
            }
        }
        return null;
    }

}
