package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CheckstyleResultListener implements AuditListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckstyleResultListener.class);
    private final CheckstyleResult result = new CheckstyleResult();

    @Override
    public void auditStarted(final AuditEvent auditEvent) {

        LOGGER.info("Starting audit...");
    }

    @Override
    public void auditFinished(final AuditEvent auditEvent) {

        LOGGER.info("Audit finished.");
    }

    @Override
    public void fileStarted(final AuditEvent auditEvent) {

        LOGGER.info("Auditing file " + auditEvent.getFileName() + "...");
    }

    @Override
    public void fileFinished(final AuditEvent auditEvent) {

        LOGGER.info("Auditing file " + auditEvent.getFileName() + " finished.");
    }

    @Override
    public void addError(final AuditEvent auditEvent) {

        LOGGER.info("Validation error " +
                    auditEvent.getSourceName() +
                    ": " +
                    auditEvent.getMessage() +
                    " In " +
                    auditEvent.getFileName() +
                    " (" + auditEvent.getLine() +
                    ":" +
                    auditEvent.getColumn() +
                    ").");

        result.addError(auditEvent);
    }

    @Override
    public void addException(final AuditEvent auditEvent, final Throwable throwable) {

        LOGGER.error("Exception while audit: " + throwable.getMessage());
    }

    public CheckstyleResult getResult() {

        return result;
    }
}
