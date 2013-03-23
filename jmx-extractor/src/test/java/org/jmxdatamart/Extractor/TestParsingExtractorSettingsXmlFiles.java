/*
 * Copyright (c) 2013, Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   o  Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *
 *   o  Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jmxdatamart.Extractor;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * This test makes sure that we can parse {@link ExtractorSettings} XML configuration
 * files.  It makes use of a special JUnit test runner to generate test cases for each
 * XML file in the
 */
@RunWith(TestParsingExtractorSettingsXmlFiles.TestGenerator.class)
public class TestParsingExtractorSettingsXmlFiles {


  /**
   * A test that validates the contents of an extractor settings XML file
   */
  public static class ExtractorSettingsFileTest {
    private final File file;

    public ExtractorSettingsFileTest(File file) {
      this.file = file;
    }

    public void run() throws FileNotFoundException {
      ExtractorSettings settings = ExtractorSettings.fromXML(new FileInputStream(file));
      assertThat(settings, is(notNullValue()));

    }

    public String getName() {
      return "test" + this.file.getName().replace('.', '_');
    }
  }

  public static class TestGenerator extends ParentRunner<ExtractorSettingsFileTest> {

    public TestGenerator(Class<?> testClass) throws InitializationError {
      super(testClass);
    }

    @Override
    protected List<ExtractorSettingsFileTest> getChildren() {
      SortedSet<File> files = getXmlFilesSortedByName();
      List<ExtractorSettingsFileTest> tests = new ArrayList<ExtractorSettingsFileTest>(files.size());

      for (File file : files) {
        tests.add(new ExtractorSettingsFileTest(file));
      }

      return tests;
    }

    private SortedSet<File> getXmlFilesSortedByName() {
      File dir = getResourceDirectoryForMyClass();
      File[] xmlFiles = dir.listFiles(new FindXmlFilesInDirectory());
      if (xmlFiles.length == 0) {
        String m = "Found no XML files in " + dir;
        throw new IllegalStateException(m);
      }

      SortedSet<File> sorted = new TreeSet<File>(new SortFilesByName());
      sorted.addAll(Arrays.asList(xmlFiles));
      return sorted;
    }

    private File getResourceDirectoryForMyClass() {
      URL url = getMyCodeSourceUrl();
      checkThatUrlIsFileUrl(url);

      File codeSource = new File(url.getFile());
      checkThatFileIsExistingDirectory(codeSource);

      String packageName = this.getClass().getPackage().getName();
      String[] packageParts = packageName.split("\\.");

      File resourceSource = codeSource;

      for (String part : packageParts) {
        resourceSource = new File(resourceSource, part);
      }

      return resourceSource;
    }

    private void checkThatFileIsExistingDirectory(File file) {
      if (!file.exists()) {
        String m = "File does not exist: " + file;
        throw new IllegalStateException(m);
      }

      if (!file.isDirectory()) {
        String m = "File is not a directory " + file;
        throw new IllegalStateException(m);
      }

    }

    private void checkThatUrlIsFileUrl(URL url) {
      String protocol = url.getProtocol();
      if (!protocol.startsWith("file")) {
        String m = "Don't know how to get files from a " + protocol + " URL: " + url;
        throw new IllegalStateException(m);
      }
    }

    private URL getMyCodeSourceUrl() {
      return this.getClass().getProtectionDomain().getCodeSource().getLocation();
    }

    @Override
    protected Description describeChild(ExtractorSettingsFileTest test) {
      return Description.createSuiteDescription(test.getName());
    }

    @Override
    protected void runChild(ExtractorSettingsFileTest test, RunNotifier notifier) {
      Description desc = describeChild( test );
      notifier.fireTestStarted( desc );
      try
      {
          test.run();
      }
      catch ( AssumptionViolatedException e )
      {
          notifier.fireTestAssumptionFailed( new Failure( desc, e) );
      }
      catch ( Throwable e )
      {
          notifier.fireTestFailure( new Failure( desc, e) );
      }
      finally
      {
          notifier.fireTestFinished( desc );
      }
    }

    private class FindXmlFilesInDirectory implements FileFilter {
      @Override
      public boolean accept(File file) {
        return file.getName().endsWith(".xml");
      }
    }

    private class SortFilesByName implements Comparator<File> {
      @Override
      public int compare(File file, File file2) {
        return file.getName().compareTo(file2.getName());
      }
    }
  }
}
