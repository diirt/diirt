/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.graphene;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import junit.framework.AssertionFailedError;
import org.epics.util.array.ArrayDouble;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.Matchers.*;
import java.awt.Font;

/**
 *
 * @author Jiakung
 */
 public abstract class BaseGraphTest{

        private String testImages;

        public BaseGraphTest(String s){
            this.testImages = s;
        }

        abstract BufferedImage draw(Graph2DRendererUpdate update);

       @Test
        public void changeFont() throws Exception{
           //Graph2DRenderer renderer = new Graph2DRenderer(300,200); cannot instantiate because it is abstract
           //BufferedImage image = renderer.draw(new Graph2DRenderer().getLabelFont(); regular draw method?
           //ImageAssert.compareImages(testImages + "testFont", image);
        }
       
       @Test
       public void changeColor() throws Exception{
           
       }
 }
