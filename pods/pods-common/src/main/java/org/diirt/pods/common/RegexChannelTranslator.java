/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.pods.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
    private final Set<String> allowedUsers;

    public RegexChannelTranslator(String regex, String substitution, ChannelTranslation.Permission permission) {
        this(regex, substitution, permission, null);
    }

    public RegexChannelTranslator(String regex, String substitution, ChannelTranslation.Permission permission, Collection<String> allowedUsers) {
        p = Pattern.compile(regex);
        this.regex = regex;
        this.substitution = substitution;
        this.permission = permission;
        if (allowedUsers != null) {
            this.allowedUsers = Collections.unmodifiableSet(new HashSet<String>(allowedUsers));
        } else {
            this.allowedUsers = null;
        }
    }

    @Override
    public ChannelTranslation translate(ChannelRequest request) {
        // If needed, match the user. If doesn't match, fail translation.
        if (allowedUsers != null && !allowedUsers.contains(request.getUser())) {
            return null;
        }

        Matcher matcher = p.matcher(request.getChannel());
        if (matcher.matches()) {
            String translation;
            if (substitution != null) {
                translation = matcher.replaceFirst(substitution);
            } else {
                translation = request.getChannel();
            }
            return new ChannelTranslation(translation, permission);
        } else {
            return null;
        }
    }

}
