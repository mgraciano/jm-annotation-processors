package jm.processors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import jm.annotations.ToString;

@SupportedAnnotationTypes("jm.annotations.ToString")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ToStringProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ToString.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().
                        printMessage(Diagnostic.Kind.WARNING, "Anotação permitida apenas em classes", element);
                continue;
            }

            final ToString anotacao = element.getAnnotation(ToString.class);
            final PackageElement pacoteElement = (PackageElement) element.getEnclosingElement();
            final TypeElement classeElement = (TypeElement) element;

            final Map<String, Element> propriedades = new LinkedHashMap<>();
            for (String propriedade : anotacao.value()) {
                for (Element ee : classeElement.getEnclosedElements()) {
                    if (ee.getKind() != ElementKind.FIELD || !ee.getSimpleName().contentEquals(propriedade)) {
                        //Iremos avaliar os campos necessários
                        continue;
                    }

                    propriedades.put(propriedade, ee);
                }
            }

            gerarClasseExtras(pacoteElement, classeElement, anotacao, propriedades);
        }

        return true;
    }

    private void gerarClasseExtras(final PackageElement pacoteElement, final TypeElement classeElement,
            final ToString anotacao, final Map<String, Element> propriedades) {
        try {
            final JavaFileObject f = processingEnv.getFiler().createSourceFile(
                    classeElement.getQualifiedName() + "Extras");
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Criando " + f.toUri());
            try (Writer w = f.openWriter()) {
                PrintWriter pw = new PrintWriter(w);
                pw.println("package " + pacoteElement.getQualifiedName().toString() + ";\n");

                pw.print("public abstract class " + classeElement.getSimpleName() + "Extras");
                if (!anotacao.superClass().equals("java.lang.Object")) {
                    pw.print(" extends " + anotacao.superClass());
                }
                pw.println(" {\n");

                pw.println("    protected " + classeElement.getSimpleName() + "Extras() {}\n");

                for (Element metodo : propriedades.values()) {
                    pw.println(
                            "    protected abstract " + criarAssinaturaBasicaGetter(metodo) + ";\n");
                }

                pw.println("    @Override");
                pw.println("    public final String toString() {");
                pw.print("        return \"" + classeElement.getSimpleName() + "[");
                for (Iterator<Map.Entry<String, Element>> it = propriedades.entrySet().iterator(); it.hasNext();) {
                    final Map.Entry<String, Element> item = it.next();
                    pw.print(item.getKey() + " = \" + " + criarMetodoGetter(item.getValue()) + " + \"");

                    if (it.hasNext()) {
                        pw.print(", ");
                    }
                }

                pw.println("]\";");
                pw.println("    }\n");
                pw.println("}");
                pw.flush();
            }
        } catch (IOException ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.toString());
        }
    }

    private String criarAssinaturaBasicaGetter(final Element metodo) {
        return metodo.asType() + " " + criarMetodoGetter(metodo);
    }

    private String criarMetodoGetter(final Element metodo) {
        return "get" + capitalizar(metodo.getSimpleName().toString()) + "()";
    }

    private static String capitalizar(final String nome) {
        char[] c = nome.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }
}
