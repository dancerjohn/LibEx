package org.libex.test.exampleinput;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.exampleinput.DefaultModifiableExampleInput.ConverterInput;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.common.base.Function;
import com.google.common.base.Throwables;

@ParametersAreNonnullByDefault
@ThreadSafe
public class ThymeLeafConversionFunction
        implements Function<ConverterInput<String>, String> {

    public static enum TemplateMode {
        XML
    }

    public static final ThymeLeafConversionFunction filepathResourceInstance()
    {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.XML.name());

        return new ThymeLeafConversionFunction(templateResolver);
    }

    public static final ThymeLeafConversionFunction classpathResourceInstance()
    {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.XML.name());

        return new ThymeLeafConversionFunction(templateResolver);
    }

    private final TemplateResolver templateResolver;

    public ThymeLeafConversionFunction(
            final TemplateResolver templateResolver) {
        this.templateResolver = checkNotNull(templateResolver);
    }

    @Override
    public String apply(
            final ConverterInput<String> input)
    {
        // Build Thymeleaf context
        Context context = new Context();
        context.setVariables(input.getVariables());

        try {
            // build spring template (adding SpEL, beans wiring, etc)
            SpringTemplateEngine te = new SpringTemplateEngine();
            te.setTemplateResolver(templateResolver);

            return te.process(input.getInputPath(), context);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
