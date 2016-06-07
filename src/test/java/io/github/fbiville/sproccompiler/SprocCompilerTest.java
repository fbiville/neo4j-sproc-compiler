package io.github.fbiville.sproccompiler;

import com.google.testing.compile.CompilationRule;
import com.google.testing.compile.CompileTester;
import org.junit.Rule;
import org.junit.Test;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import java.net.URL;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaFileObjects.forResource;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Arrays.asList;

public class SprocCompilerTest {

    @Rule public CompilationRule compilation = new CompilationRule();

    Processor processor = new SprocCompiler();

    @Test
    public void fails_if_parameters_are_not_properly_annotated() {
        JavaFileObject sproc = forResource(at("missing_name/MissingNameSproc.java"));

        CompileTester.UnsuccessfulCompilationClause compilation = assert_().about(javaSource())
                .that(sproc)
                .processedWith(processor)
                .failsToCompile()
                .withErrorCount(2);

        compilation
                .withErrorContaining("Missing @org.neo4j.procedure.Name on parameter <parameter>")
                .in(sproc).onLine(18);

        compilation
                .withErrorContaining("Missing @org.neo4j.procedure.Name on parameter <otherParam>")
                .in(sproc).onLine(18);
    }

    @Test
    public void fails_if_return_type_is_not_stream() {
        JavaFileObject sproc = forResource(at("bad_return_type/BadReturnTypeSproc.java"));

        assert_().about(javaSource())
                .that(sproc)
                .processedWith(processor)
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("Return type of BadReturnTypeSproc#niceSproc must be java.util.stream.Stream")
                .in(sproc).onLine(13);
    }

    @Test
    public void fails_if_record_type_has_nonpublic_fields() {
        JavaFileObject record = forResource(at("bad_record_type/BadRecord.java"));

        CompileTester.UnsuccessfulCompilationClause compilation = assert_().about(javaSources())
                .that(asList(forResource("test_classes/bad_record_type/BadRecordTypeSproc.java"), record))
                .processedWith(processor)
                .failsToCompile()
                .withErrorCount(2);

        compilation.withErrorContaining("Field BadRecord#label must be public")
                .in(record).onLine(6);

        compilation.withErrorContaining("Field BadRecord#age must be public")
                .in(record).onLine(7);
    }

    @Test
    public void fails_if_procedure_primitive_input_type_is_not_supported() {
        JavaFileObject sproc = forResource(at("bad_proc_input_type/BadPrimitiveInputSproc.java"));

        assert_().about(javaSource())
                .that(sproc)
                .processedWith(processor)
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining(
                    "Unsupported parameter type <short> of procedure BadPrimitiveInputSproc#doSomething"
                ).in(sproc).onLine(9);
    }

    @Test
    public void fails_if_procedure_generic_input_type_is_not_supported() {
        JavaFileObject sproc = forResource(at("bad_proc_input_type/BadGenericInputSproc.java"));

        CompileTester.UnsuccessfulCompilationClause compilation = assert_().about(javaSource())
                .that(sproc)
                .processedWith(processor)
                .failsToCompile()
                .withErrorCount(2);

        compilation
                .withErrorContaining(
                    "Unsupported parameter type " +
                    "<java.util.List<java.util.List<java.util.Map<java.lang.String,java.lang.Thread>>>>" +
                    " of procedure BadGenericInputSproc#doSomething"
                ).in(sproc).onLine(11);

        compilation
                .withErrorContaining(
                    "Unsupported parameter type " +
                    "<java.util.Map<java.lang.String,java.util.List<java.lang.Object>>>" +
                    " of procedure BadGenericInputSproc#doSomething2"
                ).in(sproc).onLine(16);
    }

    private URL at(String resource) {
        return this.getClass().getResource(String.format("/test_classes/%s", resource));
    }
}