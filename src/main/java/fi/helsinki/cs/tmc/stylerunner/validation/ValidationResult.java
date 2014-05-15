package fi.helsinki.cs.tmc.stylerunner.validation;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ValidationResult {

    Map<File, List<ValidationError>> getValidationErrors();

}
