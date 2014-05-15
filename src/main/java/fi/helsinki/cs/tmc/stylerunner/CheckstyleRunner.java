package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

import fi.helsinki.cs.tmc.stylerunner.listener.CheckstyleResultListener;
import fi.helsinki.cs.tmc.stylerunner.validation.CheckstyleResult;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.xml.sax.InputSource;

public final class CheckstyleRunner {

    private final Checker checker = new Checker();
    private final List<File> files;

    public CheckstyleRunner(final File projectDirectory) throws CheckstyleException {

        // Default configuration
        final InputSource inputSource = new InputSource(this.getClass()
                                                            .getClassLoader()
                                                            .getResourceAsStream("default-checkstyle.xml"));

        final Configuration config = ConfigurationLoader.loadConfiguration(inputSource,
                                                                           new PropertiesExpander(System.getProperties()),
                                                                           false);

        // Get all .java files from project’s source directory
        final File sourceDirectory = getSourceDirectory(projectDirectory);
        files = (List<File>) FileUtils.listFiles(sourceDirectory, new String[] { "java" }, true);

        // Configuration
        checker.setModuleClassLoader(Checker.class.getClassLoader());
        checker.configure(config);
    }

    private File getSourceDirectory(final File projectDirectory) throws CheckstyleException {

        // Ant-project
        File sourceDirectory = new File(projectDirectory, "src/");

        // Maven-project
        if (new File(projectDirectory, "pom.xml").exists()) {
            sourceDirectory = new File(projectDirectory, "src/main/");
        }

        // Invalid directory
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            throw new CheckstyleException("Path does not contain a testable project.");
        }

        return sourceDirectory;
    }

    public CheckstyleResult run() {

        // Listener
        final CheckstyleResultListener listener = new CheckstyleResultListener();
        checker.addListener(listener);

        // Process
        checker.process(files);

        // Clean up
        checker.destroy();

        return listener.getResult();
    }
}
