/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

/**
 *
 * @author carcassi
 */
public class LineTimeGraph2DRendererUpdate extends TemporalGraph2DRendererUpdate<LineTimeGraph2DRendererUpdate> {

    private InterpolationScheme interpolation;

    public LineTimeGraph2DRendererUpdate interpolation(InterpolationScheme scheme) {
        if (scheme == null) {
            throw new NullPointerException("Interpolation scheme can't be null");
        }
        if (!LineTimeGraph2DRenderer.supportedInterpolationScheme.contains(scheme)) {
            throw new IllegalArgumentException("Interpolation " + scheme + " is not supported");
        }
        this.interpolation = scheme;
        return this;
    }

    public InterpolationScheme getInterpolation() {
        return interpolation;
    }

}
