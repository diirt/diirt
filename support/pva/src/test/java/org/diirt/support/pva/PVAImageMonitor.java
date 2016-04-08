/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import static org.diirt.datasource.ExpressionLanguage.channel;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;

import static org.diirt.util.time.TimeDuration.ofHertz;
import org.diirt.vtype.VImage;

public class PVAImageMonitor {

        @SuppressWarnings("serial")
        static class ImagePanel extends JComponent {

                private Image image;

                // must be called from GUI thread
                public void setImage(BufferedImage image)
                {
                        this.image = image;
                        Dimension dim = new Dimension(image.getWidth(), image.getHeight());
                        this.setSize(dim);
                        this.setPreferredSize(dim);
                        revalidate();
                }

            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(image, 0, 0, null);
            }

        }

        // popular: BufferedImage.TYPE_3BYTE_BGR or BufferedImage.TYPE_BYTE_GRAY
        public static BufferedImage updateBufferedImage(BufferedImage image, VImage vImage, int imageType)
        {
                if (vImage == null)
                        return image;

                if (image == null ||
                        image.getHeight() != vImage.getHeight() ||
                        image.getWidth() != vImage.getWidth() ||
                        image.getType() != imageType)
                {
                image = new BufferedImage(vImage.getWidth(), vImage.getHeight(), imageType);
                }

        System.arraycopy(vImage.getData(), 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData(), 0, vImage.getData().length);
        return image;
    }

    private final JFrame frame;
    private static final BufferedImage DEFAULT_IMAGE = new BufferedImage(320, 200, BufferedImage.TYPE_3BYTE_BGR);
    private volatile BufferedImage bufferedImage = DEFAULT_IMAGE;
    private final ImagePanel imagePanel;

    public PVAImageMonitor()
    {
        imagePanel = new ImagePanel();
        imagePanel.setImage(bufferedImage);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.CENTER, imagePanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void showImage(VImage vImage)
    {
        final BufferedImage lastBufferedImage = bufferedImage;
        final BufferedImage newBufferedImage = updateBufferedImage(lastBufferedImage, vImage, BufferedImage.TYPE_3BYTE_BGR);
        bufferedImage = newBufferedImage;

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                if (newBufferedImage != lastBufferedImage)
                {
                    imagePanel.setImage(newBufferedImage);
                    frame.pack();
                }
                imagePanel.repaint();
            }
        });
    }

    public void execute(String[] args) throws InterruptedException
    {
        // max 100Hz monitor
        PVManager.setDefaultDataSource(new PVADataSource());
        PVReader<VImage> reader = PVManager.read(channel("testImage", VImage.class, VImage.class)).
                readListener(new PVReaderListener<VImage>() {

                    @Override
                    public void pvChanged(PVReaderEvent<VImage> event) {
                        if (event.isValueChanged())
                            showImage(event.getPvReader().getValue());
                        else
                            System.out.println(event.toString());
                    }
                }).maxRate(ofHertz(100));

        // forever
        while (System.currentTimeMillis() != 0)
            Thread.sleep(Long.MAX_VALUE);

        reader.close();
    }

    public static void main(String[] args) throws InterruptedException
    {
        new PVAImageMonitor().execute(args);
    }

}
