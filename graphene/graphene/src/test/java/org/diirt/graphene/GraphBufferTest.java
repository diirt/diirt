/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.stats.Ranges;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.diirt.util.array.ArrayDouble;
/**
 *
 * @author carcassi
 */
public class GraphBufferTest {
    //TODO:Throw Exception when using logScale and having range containing zero
    //TestLogScale
    @Test
    public void xScalingAsCell() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setXScaleAsCell(Ranges.range(0, 100), 0, 3, ValueScales.linearScale());
        assertThat(graph.xValueToPixel(0), equalTo(0));
        assertThat(graph.xValueToPixel(24.9999), equalTo(0));
        assertThat(graph.xValueToPixel(25), equalTo(1));
        assertThat(graph.xValueToPixel(49.9999), equalTo(1));
        assertThat(graph.xValueToPixel(50), equalTo(2));
        assertThat(graph.xValueToPixel(74.9999), equalTo(2));
        assertThat(graph.xValueToPixel(75), equalTo(3));
        assertThat(graph.xValueToPixel(99.9999), equalTo(3));
        assertThat(graph.xValueToPixel(100), equalTo(4));
        assertThat(graph.xPixelLeftToValue(0), equalTo(0.0));
        assertThat(graph.xPixelCenterToValue(0), equalTo(12.5));
        assertThat(graph.xPixelRightToValue(0), equalTo(25.0));
        assertThat(graph.xPixelLeftToValue(1), equalTo(25.0));
        assertThat(graph.xPixelCenterToValue(1), equalTo(37.5));
        assertThat(graph.xPixelRightToValue(1), equalTo(50.0));
        assertThat(graph.xPixelLeftToValue(2), equalTo(50.0));
        assertThat(graph.xPixelCenterToValue(2), equalTo(62.5));
        assertThat(graph.xPixelRightToValue(2), equalTo(75.0));
        assertThat(graph.xPixelLeftToValue(3), equalTo(75.0));
        assertThat(graph.xPixelCenterToValue(3), equalTo(87.5));
        assertThat(graph.xPixelRightToValue(3), equalTo(100.0));
    }

    @Test
    public void xScalingAsCell2()throws Exception {

        GraphBuffer test=new GraphBuffer(300,200);
        test.setXScaleAsCell(Ranges.range(0,1), 0 , 3, ValueScales.linearScale());

        assertThat(test.xValueToPixel(0.0),equalTo(0));
        assertThat(test.xValueToPixel(0.249999),equalTo(0));
        assertThat(test.xValueToPixel(0.25), equalTo(1));
        assertThat(test.xValueToPixel(0.499999),equalTo(1));
        assertThat(test.xValueToPixel(0.5),equalTo(2));
        assertThat(test.xValueToPixel(0.749999),equalTo(2));
        assertThat(test.xValueToPixel(0.75),equalTo(3));
        assertThat(test.xValueToPixel(0.999),equalTo(3));
        assertThat(test.xValueToPixel(1.0),equalTo(4));

        assertThat(test.xPixelLeftToValue(0), equalTo(0.0));
        assertThat(test.xPixelCenterToValue(0), equalTo(0.125));
        assertThat(test.xPixelRightToValue(0), equalTo(0.25));

        assertThat(test.xPixelLeftToValue(1), equalTo(0.25));
        assertThat(test.xPixelCenterToValue(1), equalTo(0.375));
        assertThat(test.xPixelRightToValue(1), equalTo(0.5));

        assertThat(test.xPixelLeftToValue(2), equalTo(0.5));
        assertThat(test.xPixelCenterToValue(2), equalTo(0.625));
        assertThat(test.xPixelRightToValue(2), equalTo(0.75));

        assertThat(test.xPixelLeftToValue(3), equalTo(0.75));
        assertThat(test.xPixelCenterToValue(3), equalTo(0.875));
        assertThat(test.xPixelRightToValue(3), equalTo(1.0));
    }
    @Test
    public void xScalingAsCellLog()throws Exception{
        GraphBuffer test=new GraphBuffer(300, 200);
        test.setXScaleAsCell(Ranges.range(1,10000), 0, 3, ValueScales.logScale());

        assertThat(test.xValueToPixel(1),equalTo(0));
        assertThat(test.xValueToPixel(10),equalTo(1));
        assertThat(test.xValueToPixel(100),equalTo(2));
        assertThat(test.xValueToPixel(1000),equalTo(3));

        assertThat(test.xValueToPixel(5),equalTo(0));
        assertThat(test.xValueToPixel(9.99),equalTo(0));
        assertThat(test.xValueToPixel(99.99),equalTo(1));
        assertThat(test.xValueToPixel(999.99),equalTo(2));
        assertThat(test.xValueToPixel(9999.99),equalTo(3));
    }
    @Test
    public void xScalingAsPoint() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setXScaleAsPoint(Ranges.range(0, 30), 0, 3, ValueScales.linearScale());
        assertThat(graph.xValueToPixel(0), equalTo(0));
        assertThat(graph.xValueToPixel(4.9999), equalTo(0));
        assertThat(graph.xValueToPixel(5), equalTo(1));
        assertThat(graph.xValueToPixel(10), equalTo(1));
        assertThat(graph.xValueToPixel(14.9999), equalTo(1));
        assertThat(graph.xValueToPixel(15), equalTo(2));
        assertThat(graph.xValueToPixel(20), equalTo(2));
        assertThat(graph.xValueToPixel(24.9999), equalTo(2));
        assertThat(graph.xValueToPixel(25), equalTo(3));
        assertThat(graph.xValueToPixel(30), equalTo(3));
        assertThat(graph.xPixelLeftToValue(0), equalTo(-5.0));
        assertThat(graph.xPixelCenterToValue(0), equalTo(0.0));
        assertThat(graph.xPixelRightToValue(0), equalTo(5.0));
        assertThat(graph.xPixelLeftToValue(1), equalTo(5.0));
        assertThat(graph.xPixelCenterToValue(1), equalTo(10.0));
        assertThat(graph.xPixelRightToValue(1), equalTo(15.0));
        assertThat(graph.xPixelLeftToValue(2), equalTo(15.0));
        assertThat(graph.xPixelCenterToValue(2), equalTo(20.0));
        assertThat(graph.xPixelRightToValue(2), equalTo(25.0));
        assertThat(graph.xPixelLeftToValue(3), equalTo(25.0));
        assertThat(graph.xPixelCenterToValue(3), equalTo(30.0));
        assertThat(graph.xPixelRightToValue(3), equalTo(35.0));
    }

    @Test
    public void xScalingAsPoint2()throws Exception{

        GraphBuffer test=new GraphBuffer(300,200);
        test.setXScaleAsPoint(Ranges.range(0,60), 0, 3, ValueScales.linearScale());
        assertThat(test.xValueToPixel(0), equalTo(0));
        assertThat(test.xValueToPixel(10), equalTo(1));
        assertThat(test.xValueToPixel(10.01), equalTo(1));
        assertThat(test.xValueToPixel(15), equalTo(1));
        assertThat(test.xValueToPixel(29.99), equalTo(1));
        assertThat(test.xValueToPixel(30), equalTo(2));
        assertThat(test.xValueToPixel(40), equalTo(2));
        assertThat(test.xValueToPixel(50), equalTo(3));
        assertThat(test.xValueToPixel(60), equalTo(3));
    }

    @Test
    public void xScalingAsPointLog()throws Exception{

        GraphBuffer test=new GraphBuffer(300, 200);
        test.setXScaleAsPoint(Ranges.range(1, 1000000), 0, 3, ValueScales.logScale());
        assertThat(test.xValueToPixel(1),equalTo(0));
        assertThat(test.xValueToPixel(10),equalTo(1));
        assertThat(test.xValueToPixel(100),equalTo(1));
        assertThat(test.xValueToPixel(1000),equalTo(2));
        assertThat(test.xValueToPixel(10000),equalTo(2));
        assertThat(test.xValueToPixel(100000),equalTo(3));
        assertThat(test.xValueToPixel(1000000),equalTo(3));

        assertThat(test.xValueToPixel(9.99),equalTo(0));
        assertThat(test.xValueToPixel(999.99),equalTo(1));
        assertThat(test.xValueToPixel(9999.99),equalTo(2));
    }
    @Test
    public void yScalingAsCell() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setYScaleAsCell(Ranges.range(0, 100), 3, 0, ValueScales.linearScale());
        assertThat(graph.yValueToPixel(0), equalTo(3));
        assertThat(graph.yValueToPixel(24.9999), equalTo(3));
        assertThat(graph.yValueToPixel(25), equalTo(2));
        assertThat(graph.yValueToPixel(49.9999), equalTo(2));
        assertThat(graph.yValueToPixel(50), equalTo(1));
        assertThat(graph.yValueToPixel(74.9999), equalTo(1));
        assertThat(graph.yValueToPixel(75), equalTo(0));
        assertThat(graph.yValueToPixel(99.9999), equalTo(0));
        assertThat(graph.yValueToPixel(100), equalTo(-1));
        assertThat(graph.yPixelBottomToValue(3), equalTo(0.0));
        assertThat(graph.yPixelCenterToValue(3), equalTo(12.5));
        assertThat(graph.yPixelTopToValue(3), equalTo(25.0));
        assertThat(graph.yPixelBottomToValue(2), equalTo(25.0));
        assertThat(graph.yPixelCenterToValue(2), equalTo(37.5));
        assertThat(graph.yPixelTopToValue(2), equalTo(50.0));
        assertThat(graph.yPixelBottomToValue(1), equalTo(50.0));
        assertThat(graph.yPixelCenterToValue(1), equalTo(62.5));
        assertThat(graph.yPixelTopToValue(1), equalTo(75.0));
        assertThat(graph.yPixelBottomToValue(0), equalTo(75.0));
        assertThat(graph.yPixelCenterToValue(0), equalTo(87.5));
        assertThat(graph.yPixelTopToValue(0), equalTo(100.0));
    }

    @Test
    public void yScalingAsCell2()throws Exception{


        GraphBuffer test= new GraphBuffer(300, 200);
        test.setYScaleAsCell(Ranges.range(0,1), 3, 0, ValueScales.linearScale());
        assertThat(test.yValueToPixel(0), equalTo(3));
        assertThat(test.yValueToPixel(0.25), equalTo(2));
        assertThat(test.yValueToPixel(0.5), equalTo(1));
        assertThat(test.yValueToPixel(0.75), equalTo(0));

        assertThat(test.yValueToPixel(0.999), equalTo(0));
        assertThat(test.yValueToPixel(0.7499), equalTo(1));
        assertThat(test.yValueToPixel(0.499), equalTo(2));
        assertThat(test.yValueToPixel(0.2499), equalTo(3));

    }

    @Test
    public void yScalingAsPoint() throws Exception {
        GraphBuffer graph = new GraphBuffer(300, 200);
        graph.setYScaleAsPoint(Ranges.range(0, 30), 3, 0, ValueScales.linearScale());
        assertThat(graph.yValueToPixel(0), equalTo(3));
        assertThat(graph.yValueToPixel(4.9999), equalTo(3));
        assertThat(graph.yValueToPixel(5), equalTo(2));
        assertThat(graph.yValueToPixel(10), equalTo(2));
        assertThat(graph.yValueToPixel(14.9999), equalTo(2));
        assertThat(graph.yValueToPixel(15), equalTo(1));
        assertThat(graph.yValueToPixel(20), equalTo(1));
        assertThat(graph.yValueToPixel(24.9999), equalTo(1));
        assertThat(graph.yValueToPixel(25), equalTo(0));
        assertThat(graph.yValueToPixel(30), equalTo(0));

    }
    @Test
    public void yScallingAsPoint2() throws Exception{


        GraphBuffer test=new GraphBuffer(300,200);
        test.setYScaleAsPoint(Ranges.range(0, 6), 0, 3,ValueScales.linearScale());
        assertThat(test.yValueToPixel(0), equalTo(0));
        assertThat(test.yValueToPixel(0.5), equalTo(0));
        assertThat(test.yValueToPixel(1.5), equalTo(1));
        assertThat(test.yValueToPixel(2.5), equalTo(1));
        assertThat(test.yValueToPixel(3.5), equalTo(2));
        assertThat(test.yValueToPixel(4.5), equalTo(2));
        assertThat(test.yValueToPixel(5.5), equalTo(3));
        assertThat(test.yValueToPixel(6), equalTo(3));

    }



    @Test
    public void drawBottomLabels() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        graphBuffer.drawBackground(Color.WHITE);
        List<String> labels = Arrays.asList("0", "50", "100");
        ArrayInt positions = new ArrayInt(10, 150, 289);
        graphBuffer.drawBottomLabels(labels, positions, Color.BLACK, FontUtil.getLiberationSansRegular(),
                10, 289, 190);
        ImageAssert.compareImages("graphBuffer.drawBottomLabels", graphBuffer.getImage());
    }

    @Test
    public void drawLeftLabels() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        graphBuffer.drawBackground(Color.WHITE);
        List<String> labels = Arrays.asList("0", "50", "100");
        ArrayInt positions = new ArrayInt(189, 100, 10);
        graphBuffer.drawLeftLabels(labels, positions, Color.BLACK, FontUtil.getLiberationSansRegular(),
                189, 10, 35);
        ImageAssert.compareImages("graphBuffer.drawLeftLabels", graphBuffer.getImage());
    }

    @Test
    public void drawHorizontalReferenceLines() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        graphBuffer.drawBackground(Color.WHITE);
        ArrayInt positions = new ArrayInt(189, 100, 10);
        graphBuffer.drawHorizontalReferenceLines(positions, Color.BLACK, 5, 294);
        ImageAssert.compareImages("graphBuffer.drawHorizontalReferenceLines", graphBuffer.getImage());
    }

    @Test
    public void drawHorizontalReferenceLines2() throws Exception{
        GraphBuffer test = new GraphBuffer(300, 200);
        test.drawBackground(Color.BLACK);
        ArrayInt position = new ArrayInt(50,100,150);
        test.drawHorizontalReferenceLines(position, Color.WHITE, 0, 300);
        ImageAssert.compareImages("graphBuffer.drawHorizontalReferenceLines2", test.getImage());
    }

     @Test
    public void drawHorizontalReferenceLines3() throws Exception{
         GraphBuffer test = new GraphBuffer(300, 200);
        test.drawBackground(Color.LIGHT_GRAY);
        ArrayInt pos= new ArrayInt(50,100,150);
        test.drawHorizontalReferenceLines(pos, Color.BLACK, 0, 149);
        test.drawHorizontalReferenceLines(pos, Color.GREEN, 151, 300);
        ImageAssert.compareImages("graphBuffer.drawHorizontalReferenceLines3", test.getImage());
    }

    @Test
    public void drawVerticalReferenceLines() throws Exception {
        GraphBuffer graphBuffer = new GraphBuffer(300, 200);
        graphBuffer.drawBackground(Color.WHITE);
        ArrayInt positions = new ArrayInt(10, 150, 289);
        graphBuffer.drawVerticalReferenceLines(positions, Color.BLACK, 5, 194);
        ImageAssert.compareImages("graphBuffer.drawVerticalReferenceLines", graphBuffer.getImage());

    }
    @Test
    public void drawVerticalReferenceLines2() throws Exception{

        GraphBuffer test = new GraphBuffer(300, 200);
        test.drawBackground(Color.WHITE);
        ArrayInt position = new ArrayInt(50,100,150,200,250,300);
        test.drawVerticalReferenceLines(position, Color.BLACK, 0, 200);
        ImageAssert.compareImages("graphBuffer.drawVerticalReferenceLines2", test.getImage());
    }

    @Test
    public void drawDataImage() throws Exception{

        int colorValue=0;

        double[] numbers=new double[200*100];
        for(int i=0; i<200*100;i++){
            if(i%5000==0)
                colorValue++;

            numbers[i]=colorValue;

        }
        ArrayDouble dataList=new ArrayDouble(numbers);
        Cell2DDataset dataSet=Cell2DDatasets.linearRange(dataList,Ranges.range(0,200),200,Ranges.range(0, 100),100 );

        GraphBuffer test=new GraphBuffer(300, 200);
        test.setXScaleAsCell(Ranges.range(0, 200), 0, 200, ValueScales.linearScale());
        test.setYScaleAsCell(Ranges.range(0, 100), 0, 100, ValueScales.linearScale());
        int [] xPointToDataMap= new int [200];
        int [] yPointToDataMap= new int [100];
        for(int i=0; i<200;i++){
            xPointToDataMap [i]=i;
        }
        for(int i=0;i<100;i++){

         yPointToDataMap[i]=i;
        }


        NumberColorMap colormap = NumberColorMaps.JET;
        NumberColorMapInstance colorMapInstance=colormap.createInstance(Ranges.range(1, 4));

        test.drawBackground(Color.white);
        test.drawDataImage(50,50, xPointToDataMap, yPointToDataMap, dataSet, colorMapInstance);

        ImageAssert.compareImages("graphBuffer.drawDataImage", test.getImage());

    }

    /*
    @Test
    public void drawLineGraphTwoValueData() throws Exception {

        Point2DDataset data = Point2DTestDatasets.twoValueDataset();
        GraphBuffer buffer=new GraphBuffer(300, 200);
        buffer.setXScaleAsPoint(data.getXStatistics(), 0, 300, ValueScales.linearScale());
        buffer.setYScaleAsPoint(data.getYStatistics(), 0, 200, ValueScales.linearScale());
        buffer.drawBackground(Color.WHITE);
        buffer.drawLineGraph(data, InterpolationScheme.CUBIC, ReductionScheme.NONE);
        ImageAssert.compareImages("lineGraph2D.cubic.twoValues", buffer.getImage());
    }

    @Test
    public void drawLineGraphOneNanCubic() throws Exception {
        double[] dataSet = {1,8,27,Double.NaN, 125,216};
        Point2DDataset data = Point2DDatasets.lineData(dataSet);

        GraphBuffer buffer=new GraphBuffer(300, 200);
        buffer.setXScaleAsPoint(data.getXStatistics(), 0, 300, ValueScales.linearScale());
        buffer.setYScaleAsPoint(data.getYStatistics(), 0, 200, ValueScales.linearScale());
        buffer.drawBackground(Color.WHITE);
        buffer.drawLineGraph(data, InterpolationScheme.CUBIC, ReductionScheme.NONE);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.1", buffer.getImage());

    }

    @Test
    public void drawLineGraphConsecNaNCubic ()throws Exception{

        Point2DDataset data = Point2DTestDatasets.consecNaNDataset();
        GraphBuffer buffer=new GraphBuffer(300, 200);
        buffer.setXScaleAsPoint(data.getXStatistics(), 0, 300, ValueScales.linearScale());
        buffer.setYScaleAsPoint(data.getYStatistics(), 0, 200, ValueScales.linearScale());
        buffer.drawBackground(Color.WHITE);
        buffer.drawLineGraph(data, InterpolationScheme.CUBIC, ReductionScheme.NONE);
        ImageAssert.compareImages("lineGraph2D.cubic.NaN.consecutive", buffer.getImage());
    }
    */
}
