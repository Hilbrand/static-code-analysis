/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.tools.analysis.report.test;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openhab.tools.analysis.report.ReportUtility;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests for the {@link ReportUtility}
 *
 * @author Svilen Valkanov - Initial contributation
 * @author Martin van Wingerden - added logging of all messages
 */
@RunWith(MockitoJUnitRunner.class)
public class ReportUtilityTest {

    private static final String TARGET_RELATIVE_DIR = "target" + File.separator + "test-classes" + File.separator
            + "report";
    private static final String TARGET_ABSOLUTE_DIR = System.getProperty("user.dir") + File.separator
            + TARGET_RELATIVE_DIR;
    private static final String RESULT_FILE_PATH = TARGET_ABSOLUTE_DIR + File.separator + ReportUtility.RESULT_FILE_NAME;

    @Mock
    private Log logger;

    private ReportUtility subject;

    private File resultFile = new File(RESULT_FILE_PATH);

    @Before
    public void setUp() throws Exception {
        subject = new ReportUtility();
        subject.setLog(logger);

        if (resultFile.exists()) {
            resultFile.delete();
        }
    }

    @Test(expected = MojoFailureException.class)
    public void assertReportIsCreatedAndBuildFails() throws Exception {
        assertFalse(resultFile.exists());

        subject.setFailOnError(true);
        subject.setSummaryReport(null);
        subject.setTargetDirectory(new File(TARGET_ABSOLUTE_DIR));

        try {
            subject.execute();
        } finally {
            assertTrue(resultFile.exists());
        }
    }

    @Test
    public void assertReportISCreatedAndBuildCompletes() throws MojoFailureException {
        assertFalse(resultFile.exists());

        subject.setFailOnError(false);
        subject.setSummaryReport(null);
        subject.setTargetDirectory(new File(TARGET_ABSOLUTE_DIR));

        subject.execute();

        assertTrue(resultFile.exists());
    }

    @Test
    public void assertWarningAreLoggedWhileExecuting() throws MojoFailureException {
        assertFalse(resultFile.exists());

        subject.setFailOnError(false);
        subject.setSummaryReport(null);
        subject.setTargetDirectory(new File(TARGET_ABSOLUTE_DIR));

        subject.execute();

        verify(logger).warn("org.sprunck.bee.Bee.java:[31]\norg.sprunck.bee.Bee defines clone() but doesn't implement Cloneable");
        verify(logger).warn("org.sprunck.bee.Bee.java:[31]\norg.sprunck.bee.Bee.clone() may return null");
        verify(logger).warn("org.sprunck.foo.Foo.java:[35]\nThe method name org.sprunck.foo.Foo.Went() doesn't start with a lower case letter");
        verify(logger).error("Code Analysis Tool has found: \n 2 error(s)! \n 3 warning(s) \n 3 info(s)");
        verify(logger).error("org.sprunck.bee.Bee.java:[19]\norg.sprunck.bee.Bee.toString() ignores return value of String.concat(String)");
        verify(logger).error("org.eclipse.smarthome.auth.jaas.internal.JaasAuthenticationProvider.java:[69]\nComment matches to-do format '(TODO)|(FIXME)'.");
        verify(logger).debug("org.sprunck.bee.Bee.java:[19]\nAn operation on an Immutable object (String, BigDecimal or BigInteger) won't change the object itself");
        verify(logger).debug("org.sprunck.foo.Foo.java:[36]\nDo not use if statements that are always true or always false");
        verify(logger).debug("C:\\prj\\openHAB\\EclipseIDE\\git\\smarthome\\bundles\\automation\\org.eclipse.smarthome.automation.module.core\\ESH-INF\\automation\\moduletypes\\EventTriggersTypeDefinition.json:[0]\n" +
                "File does not end with a newline.");
        verify(logger).info("Detailed report can be found at: file:///" + RESULT_FILE_PATH);

        verifyNoMoreInteractions(logger);
    }
}
