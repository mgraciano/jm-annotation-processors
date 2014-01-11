package jm.processors;

import static javax.annotation.processing.Completions.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import jm.annotations.Documentacao;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;

@SupportedAnnotationTypes({"jm.annotations.Documentacao"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DocumentacaoProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Documentacao.class)) {
            final Documentacao documentacao = element.getAnnotation(Documentacao.class);
            final String hostname = documentacao.value();
            try {
                final StatusLine status = Request.Head(hostname).connectTimeout(500).
                        execute().returnResponse().getStatusLine();
                if (status.getStatusCode() != 200) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                            "A documentação informada em " + hostname
                            + " parece ter sido (re)movida. Favor verificar.", element);
                }
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                        "A documentação informada em " + hostname
                        + " parece ter sido (re)movida. Favor verificar.", element);
            }
        }
        return false;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation,
            ExecutableElement member, String userText) {
        return Arrays.asList(
                of("\"http://wiki.netbeans.org/\"", "Wiki do projeto NetBeans.org."),
                of("\"http://www.wikipedia.org/\"", "Wiki do projeto Wikipedia - The Free Encyclopedia."),
                of("\"http://wiki.suaempresa.com.br/\"", "Wiki interna da sua empresa."));
    }
}
