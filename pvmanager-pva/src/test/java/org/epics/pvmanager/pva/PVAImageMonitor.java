package org.epics.pvmanager.pva;

import static org.epics.pvmanager.ExpressionLanguage.channel;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.util.time.TimeDuration;
import org.epics.vtype.VImage;

public class PVAImageMonitor {

	// popular: BufferedImage.TYPE_3BYTE_BGR or BufferedImage.TYPE_BYTE_GRAY
	public static BufferedImage updateBufferedImage(BufferedImage image, VImage vImage, int imageType)
	{
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

	public PVAImageMonitor()
	{
		frame = new JFrame();
		frame.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
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
					// new image, new size?
					frame.setSize(newBufferedImage.getWidth(), newBufferedImage.getHeight());
				}
				frame.getGraphics().drawImage(newBufferedImage, 0, 0, null);
			}
		});
	}
	
	public void execute(String[] args)
	{
		// max 100Hz monitor
		PVManager.setDefaultDataSource(new PVADataSource());
		PVManager.read(channel("testImage", VImage.class, VImage.class)).
				readListener(new PVReaderListener<VImage>() {

					@Override
					public void pvChanged(PVReaderEvent<VImage> event) {
						if (event.isValueChanged())
							showImage(event.getPvReader().getValue());
						else
							System.out.println(event.toString());
					}
				}).maxRate(TimeDuration.ofHertz(100));
	}

	public static void main(String[] args)
	{
		new PVAImageMonitor().execute(args);
	}

}
