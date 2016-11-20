package org.libex.test.exampleinput;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.extern.slf4j.Slf4j;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.libex.test.exampleinput.DefaultModifiableExampleInput.ConverterInput;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
@Slf4j
public class VelocityEngineConversionFunction
        implements Function<ConverterInput<String>, String> {

    public static final VelocityEngineConversionFunction rootFilepathResourceInstance()
    {
        Properties properties = new Properties();
        properties.put(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
        properties.put(Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER, "org.libex.test.exampleinput");
        properties.put(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "/");
        return new VelocityEngineConversionFunction(properties);
    }

    public static final VelocityEngineConversionFunction classpathResourceInstance()
    {
        Properties properties = new Properties();
        properties.put(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
        properties.put(Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER, "org.libex.test.exampleinput");
        properties.put(RuntimeConstants.RESOURCE_LOADER, "class");
        properties.put("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityEngineConversionFunction(properties);
    }

    private final VelocityEngine velocityEngine;

    public VelocityEngineConversionFunction(
            final Properties properties) {
        this.velocityEngine = new VelocityEngine(properties);
    }

    @Override
    public String apply(
            final ConverterInput<String> input)
    {
        String html = mergeTemplateIntoString(
                getVelocityEngine(),
                input.getInputPath(),
                "utf-8",
                input.getVariables());
        return html;
    }

    protected VelocityEngine getVelocityEngine()
    {
        return velocityEngine;
    }

    /**
     * Merge the specified Velocity template with the given model into a String.
     * <p>
     * When using this method to prepare a text for a mail to be sent with Spring's mail support, consider wrapping
     * VelocityException in MailPreparationException.
     * 
     * @param velocityEngine
     *            VelocityEngine to work with
     * @param templateLocation
     *            the location of template, relative to Velocity's resource loader path
     * @param encoding
     *            the encoding of the template file
     * @param model
     *            the Map that contains model names as keys and model objects as values
     * @return the result as String
     * @throws VelocityException
     *             if the template wasn't found or rendering failed
     */
    public static String mergeTemplateIntoString(
            final VelocityEngine velocityEngine,
            final String templateLocation,
            final String encoding,
            final Map<String, ?> model) throws VelocityException
    {

        StringWriter result = new StringWriter();
        mergeTemplate(velocityEngine, templateLocation, encoding, model, result);
        return result.toString();
    }

    /**
     * Merge the specified Velocity template with the given model and write the result
     * to the given Writer.
     * 
     * @param velocityEngine
     *            VelocityEngine to work with
     * @param templateLocation
     *            the location of template, relative to Velocity's resource loader path
     * @param encoding
     *            the encoding of the template file
     * @param model
     *            the Map that contains model names as keys and model objects as values
     * @param writer
     *            the Writer to write the result to
     * @throws VelocityException
     *             if the template wasn't found or rendering failed
     */
    public static void mergeTemplate(
            final VelocityEngine velocityEngine,
            final String templateLocation,
            final String encoding,
            final Map<String, ?> model,
            final Writer writer) throws VelocityException
    {

        try {
            VelocityContext velocityContext = new VelocityContext(model);
            velocityEngine.mergeTemplate(templateLocation, encoding, velocityContext, writer);
        } catch (VelocityException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Why does VelocityEngine throw a generic checked exception, after all?", ex);
            throw new VelocityException(ex.toString());
        }
    }
}
