import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TestThymeleaf {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("es"));


  @Test
  public void testGeneratingTemplate() {
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/");
    resolver.setTemplateMode(TemplateMode.TEXT);
    resolver.setCharacterEncoding("UTF-8");

    TemplateEngine engine = new TemplateEngine();
    engine.setTemplateResolver(resolver);

    // Provide data
    Context context = new Context();

    // TODO: check if server is in correct timezone (if not set it on Docker image)
    OffsetDateTime now = OffsetDateTime.now();
    context.setVariable("date", now.format((DateTimeFormatter.ISO_LOCAL_DATE)));
    context.setVariable("amount", 500);

    // Process template
    String result = engine.process("task_description_es.txt", context);
    System.out.println(result);
  }

  @Test
  public void testDateFormatting() {
    OffsetDateTime now = OffsetDateTime.now();
    String formattedDate = StringUtils.capitalize(now.format(DATE_TIME_FORMATTER));
    System.out.println(formattedDate);
  }
}
