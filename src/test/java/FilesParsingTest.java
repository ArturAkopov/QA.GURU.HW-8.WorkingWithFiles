import JsonModels.Root;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.util.Objects.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Анализ файлов")
public class FilesParsingTest {

    private final ClassLoader classLoader = FilesParsingTest.class.getClassLoader();

    @DisplayName("Проверка xlsx файлов в архиве smpZip.zip")
    @Test
    void zipFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(requireNonNull(classLoader.getResourceAsStream("smpZip.zip")))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xlsx")) {
                    XLS xls = new XLS(zis);
                    String value = xls.excel.getSheetAt(0).getRow(2).getCell(1).getStringCellValue();
                    assertThat(value).isEqualTo("Успех");
                }
            }
        }
    }

    @DisplayName("Проверка csv файлов в архиве smpZip.zip")
    @Test
    void zipCheckCsvFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(requireNonNull(classLoader.getResourceAsStream("smpZip.zip")))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv")) {
                    CSVReader csv = new CSVReader(new InputStreamReader(zis));
                    List<String[]> values = csv.readAll();
                    assertThat(values).isNotEmpty().hasSize(5);
                    assertThat(values.get(0)).isEqualTo(new String[]{"name", "phoneNumber", "email", "address", "userAgent", "hexcolor"});
                }
            }
        }
    }

    @DisplayName("Проверка pdf файлов в архиве smpZip.zip")
    @Test
    void zipCheckPdfFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(requireNonNull(classLoader.getResourceAsStream("smpZip.zip")))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("smpPdf.pdf")) {
                    PDF pdf = new PDF(zis);
                    assertThat(pdf.numberOfPages).isEqualTo(1);
                    assertThat(pdf.text).contains("Как правило, в формат PDF");
                }
            }
        }
    }

    @DisplayName("Проверка файла json")
    @Test
    void jsonFileParsingTest() throws Exception {
        try (InputStream js = classLoader.getResourceAsStream("cat.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            Root cat = objectMapper.readValue(js, Root.class);
            Assertions.assertEquals("Мурка", cat.cat.name);
            Assertions.assertEquals("head", cat.cat.bodyParts.get(0));
            Assertions.assertEquals("paws", cat.cat.bodyParts.get(1));
            Assertions.assertEquals("tail", cat.cat.bodyParts.get(2));
            Assertions.assertEquals(2, cat.cat.age);
        }
    }

}


