package ir.piana.financial.commons.log;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.NamePatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

@Plugin(name = "CleanLoggerConverter", category = PatternConverter.CATEGORY)
@ConverterKeys("pqcn")
public class PianaPackageQualifiedClassNameCGLibFilteredLoggerConverter extends NamePatternConverter implements LocationAware {
    /**
     * Constructs an instance of LoggingEventPatternConverter.
     *
     */
    private PianaPackageQualifiedClassNameCGLibFilteredLoggerConverter(final String[] options) {
        super("Class Name", "class name", options);
    }

    public static PianaPackageQualifiedClassNameCGLibFilteredLoggerConverter newInstance(final String[] options) {
        return new PianaPackageQualifiedClassNameCGLibFilteredLoggerConverter(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
//        NameAbbreviator abbreviator = NameAbbreviator.getAbbreviator(options);
//        abbreviator.abbreviate(event.getLoggerName().replaceAll("\\$\\$.*", ""), toAppendTo);
//        abbreviator.abbreviate(event.getLoggerName().replaceAll("\\$\\$.*?\\$\\$.*", ""), toAppendTo);
        abbreviate(event.getLoggerName().replaceAll("\\$\\$.*?\\$\\$.*", ""), toAppendTo);
    }

    @Override
    public boolean requiresLocation() {
        return true;
    }
}
