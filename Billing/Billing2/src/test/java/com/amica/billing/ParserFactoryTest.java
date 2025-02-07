package com.amica.billing;

import com.amica.billing.parse.CSVParser;
import com.amica.billing.parse.FlatParser;
import com.amica.billing.parse.Parser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Supplier;
import static org.assertj.core.api.Assertions.*;

public class ParserFactoryTest {

    Parser mockParser = Mockito.mock(Parser.class);
    Supplier<Parser> mockParserFactory = () -> mockParser;
    @Test
    void testCSVParser() {
        Parser parser = ParserFactory.createParser(".CSV");
        assertThat(parser).isInstanceOf(CSVParser.class);
    }

    @Test
    void testFlatParser() {
        Parser parser = ParserFactory.createParser(".FLAT");
        assertThat(parser).isInstanceOf(FlatParser.class);
    }

    @Test
    void testAddParser() {
        ParserFactory.addParser("txt", mockParserFactory);
        ParserFactory.addParser("pdf", mockParserFactory);
        assertThat(ParserFactory.createParser(".txt")).isEqualTo(mockParser);
        assertThat(ParserFactory.createParser(".pdf")).isEqualTo(mockParser);
    }

    @Test
    void testReplaceParser() {
        ParserFactory.replaceParser("csv", mockParserFactory);
        ParserFactory.replaceParser("flat", mockParserFactory);
        assertThat(ParserFactory.createParser(".csv")).isEqualTo(mockParser);
        assertThat(ParserFactory.createParser(".flat")).isEqualTo(mockParser);
    }

    @Test
    void testRejectAddParser() {
        assertThatThrownBy(() -> ParserFactory.addParser("csv", mockParserFactory))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testRejectReplaceParser() {
        assertThatThrownBy(() -> ParserFactory.replaceParser("jpg", mockParserFactory))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
