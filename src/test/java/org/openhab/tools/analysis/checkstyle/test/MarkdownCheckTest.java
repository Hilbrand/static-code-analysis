/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.tools.analysis.checkstyle.test;

import static com.puppycrawl.tools.checkstyle.utils.CommonUtils.EMPTY_STRING_ARRAY;
import static org.openhab.tools.analysis.checkstyle.api.CheckConstants.README_MD_FILE_NAME;
import org.openhab.tools.analysis.checkstyle.api.AbstractStaticCheckTest;
import org.openhab.tools.analysis.checkstyle.readme.MarkdownCheck;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link MarkdownCheck}
 *
 * @author Erdoan Hadzhiyusein - Initial implementation
 */
public class MarkdownCheckTest extends AbstractStaticCheckTest {
    private static final String README_MD_CHECK_TEST_DIRECTORY_NAME = "markdownCheckTest";
    private static final String ADDED_README_IN_BUILD_PROPERTIES_MSG = "README.MD file must not be added to the bin.includes property";
    private static final String ADDED_DOC_FOLDER_IN_BUILD_PROPERTIES_MSG = "The doc folder must not be added to the bin.includes property";
    private DefaultConfiguration config;

    @Before
    public void setUpClass() {
        createValidConfig();
    }

    @Test
    public void testHeader() throws Exception {
        String[] expectedMessages = generateExpectedMessages(1, "Missing an empty line after the Markdown header (#).");
        verifyMarkDownFile("testHeader", expectedMessages);
    }

    
    @Ignore("seems to have a problem in Travis") @Test
    public void testDisabledNodeVisit() throws Exception {
        verifyMarkDownFile("testDisabledNodeVisit", noMessagesExpected());
    }

    private String[] noMessagesExpected() {
        String[] expectedMessages = EMPTY_STRING_ARRAY;
        return expectedMessages;
    }

    @Test
    public void headerAtEndOfFile() throws Exception {
        String[] expectedMessages = generateExpectedMessages(6, "There is a header at the end of the Markdown file. Please consider adding some content below.");
        verifyMarkDownFile("testHeaderAtEndOfFile", expectedMessages);
    }

    @Test
    public void testSpecialHeader() throws Exception {
        verifyMarkDownFile("testHeaderOutOfBounds", noMessagesExpected());
    }

    @Test
    public void testEmptyLineBeforeList() throws Exception {
        String[] expectedMessages = generateExpectedMessages(1, "The line before a Markdown list must be empty.");
        verifyMarkDownFile("testEmptyLineBeforeList", expectedMessages);
    }

    @Test
    public void testEmptyLineAfterList() throws Exception {
        String[] expectedMessages = generateExpectedMessages(7, "The line after a Markdown list must be empty.");
        verifyMarkDownFile("testEmptyLineAfterList", expectedMessages);
    }

    @Test
    public void testListAtEndOfFile() throws Exception {
        verifyMarkDownFile("testListAtEndOfFile", noMessagesExpected());
    }

    @Test
    public void testCodeFormattedListBlock() throws Exception {
        verifyMarkDownFile("testCodeFormattedListBlock", noMessagesExpected());
    }

    @Test
    public void testEmptyLinedCodeBlock() throws Exception {
        verifyMarkDownFile("testEmptyLinedCodeBlock", noMessagesExpected());
    }

    @Test
    public void testCodeFormattedListElement() throws Exception {
        verifyMarkDownFile("testCodeFormattedListElement", noMessagesExpected());
    }

    @Test
    public void testCodeSectionAtBeginingOfFile() throws Exception {
        String[] expectedMessages = generateExpectedMessages(1,
                "The line before code formatting section must be empty.");
        verifyMarkDownFile("testCodeSectionAtBeginingOfFile", expectedMessages);
    }

    @Test
    public void testListAtBeginingOfFile() throws Exception {
        String[] expectedMessages = generateExpectedMessages(1, "The line before a Markdown list must be empty.");
        verifyMarkDownFile("testListAtBeginingOfFile", expectedMessages);
    }

    @Test
    public void testOneElementList() throws Exception {
        verifyMarkDownFile("testOneElementedList", noMessagesExpected());
    }

    @Test
    public void testEmphasizeItalicListElement() throws Exception {
        verifyMarkDownFile("testEmphasizeItalicListElement", noMessagesExpected());
    }

    @Test
    public void testPreCodeSection() throws Exception {
        String[] expectedMessages = generateExpectedMessages(36,
                "The line before code formatting section must be empty.");
        verifyMarkDownFile("testPreCodeSection", expectedMessages);
    }

    @Test
    public void testCodeSectionLineNumberError() throws Exception {
        verifyMarkDownFile("testCodeSectionLineNumberError",noMessagesExpected());
    }

    @Test
    public void testEmptyCodeSection() throws Exception {
        String[] expectedMessages = generateExpectedMessages(1,
                "There is an empty or unclosed code formatting section. Please correct it.");
        verifyMarkDownFile("testEmptyCodeSection", expectedMessages);
    }

    @Test
    public void testEmptyLineAfterCodeSection() throws Exception {
        String[] expectedMessages = generateExpectedMessages(13,
                "The line after code formatting section must be empty.");
        verifyMarkDownFile("testEmptyLineAfterCodeSection", expectedMessages);
    }

    @Test
    public void testComplicatedCodeBlocks() throws Exception {
        verifyMarkDownFile("testComplicatedCodeBlocks", noMessagesExpected());
    }

    @Test
    public void testMultilineListItemsSeparation() throws Exception {
        verifyMarkDownFile("testListSeparation", noMessagesExpected());
    }

    @Test
    public void testCodeSectionAtEndOfFile() throws Exception {
        verifyMarkDownFile("testCodeSectionAtEndOfFile", noMessagesExpected());
    }

    @Test
    public void testMultiLineListItem() throws Exception {
        verifyMarkDownFile("testMultiLineListItems", noMessagesExpected());
    }

    @Test
    public void testValidMarkDown() throws Exception {
        verifyMarkDownFile("testValidMarkDown", noMessagesExpected());
    }

    @Test
    public void testLinkAsHeader() throws Exception {
        verifyMarkDownFile("testLinkAsHeader", noMessagesExpected());
    }

    @Test
    public void testReadMeAddedInBuildProperties() throws Exception {
        String[] expectedMessages = generateExpectedMessages(0, ADDED_README_IN_BUILD_PROPERTIES_MSG);
        String testDirectoryName = "testAddedReadMeInBuildProperties";

        // The message is logged for the build.properties file
        verifyBuildProperties(expectedMessages, testDirectoryName);
    }

    @Test
    public void testAddedDummyDocInBuildProperties() throws Exception {
        String testDirectoryName = "testAddedDummyDocInBuildProperties";
        verifyBuildProperties(noMessagesExpected(), testDirectoryName);
    }

    @Test
    public void testDocFolderAddedInBuildProperties() throws Exception {
        String[] expectedMessages = generateExpectedMessages(0, ADDED_DOC_FOLDER_IN_BUILD_PROPERTIES_MSG);
        String testDirectoryName = "testAddedDocFolderInBuildProperties";

        // The message is logged for the build.properties file
        verifyBuildProperties(expectedMessages, testDirectoryName);
    }

    @Test
    public void testAddedReadmeAndDocInBuildProperties() throws Exception {
        String[] expectedMessages = generateExpectedMessages(0, ADDED_README_IN_BUILD_PROPERTIES_MSG, 0,
                ADDED_DOC_FOLDER_IN_BUILD_PROPERTIES_MSG);
        String testDirectoryName = "testAddedReadmeAndDocInBuildProperties";

        // The message is logged for the build.properties file
        verifyBuildProperties(expectedMessages, testDirectoryName);
    }

    private void verifyBuildProperties(String[] expectedMessages, String testDirectoryName)
            throws IOException, Exception { 
        String testDirectoryAbsolutePath = getPath(README_MD_CHECK_TEST_DIRECTORY_NAME + File.separator + testDirectoryName);
        String messageFilePath = testDirectoryAbsolutePath + File.separator + "build.properties";
        verify(createChecker(config), messageFilePath, expectedMessages);
    }

    private void createValidConfig() {
        config = createCheckConfig(MarkdownCheck.class);
    }

    private void verifyMarkDownFile(String testDirectoryName, String[] expectedMessages) throws Exception {
        String testDirectoryRelativePath = README_MD_CHECK_TEST_DIRECTORY_NAME + File.separator + testDirectoryName
                + File.separator + README_MD_FILE_NAME;
        String testDirectoryAbsolutePath = getPath(testDirectoryRelativePath);
        String messageFilePath = testDirectoryAbsolutePath;
        verify(createChecker(config), testDirectoryAbsolutePath, messageFilePath, expectedMessages);
    }

    @Override
    protected DefaultConfiguration createCheckerConfig(Configuration config) {
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration("root");
        defaultConfiguration.addChild(config);
        return defaultConfiguration;
    }
}
