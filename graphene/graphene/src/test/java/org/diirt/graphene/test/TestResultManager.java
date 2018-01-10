/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author carcassi
 */
public class TestResultManager {
    private File outputDirectory = new File("src/test/resources/org/diirt/graphene");

    public TestResultManager() {
        refresh();
    }


    public class Result {
        private File failedImage;
        private File referenceImage;

        public Result(File failedImage, File referenceImage) {
            this.failedImage = failedImage;
            this.referenceImage = referenceImage;
        }

        public void accept() {
            TestResultManager.this.accept(this);
        }

        public File getFailedImage() {
            return failedImage;
        }

        public File getReferenceImage() {
            return referenceImage;
        }

        @Override
        public String toString() {
            return referenceImage.getName();
        }

    }

    private List<Result> currentResults;

    public void refresh() {
        File[] failedFiles = outputDirectory.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("failed.png");
            }
        });
        currentResults = new LinkedList<>();
        for (int i = 0; i < failedFiles.length; i++) {
            File file = failedFiles[i];
            File referenceFile = new File(file.getParentFile(), file.getName().substring(0, file.getName().length() - 10) + "png");
            currentResults.add(new Result(file, referenceFile));
        }
    }

    public List<Result> getCurrentResults() {
        return currentResults;
    }

    public void accept(Result result) {
        if (result.referenceImage.exists()) {
            result.referenceImage.delete();
        }

        result.failedImage.renameTo(result.referenceImage);
        currentResults.remove(result);
    }

    public static void main(String[] args) {
        TestResultManager manager = new TestResultManager();
        manager.refresh();
        System.out.println(manager.currentResults);
    }
}
