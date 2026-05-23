package com.bruno.misgastos.utils;

import com.bruno.misgastos.entities.TaskConfig;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public interface GoogleUtils {

  DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("es"));

  static String generateGoogleTasksTaskTitle(TaskConfig taskConfig) {
    String taskTitlePrefix = taskConfig.getGoogleTaskTitle();
    String formattedDate = StringUtils.capitalize(OffsetDateTime.now().format(DATE_TIME_FORMATTER));
    return String.format(
      "%s %s",
      taskTitlePrefix,
      formattedDate);
  }

  static String generateGoogleTasksTaskNotes(TemplateEngine templateEngine, TaskConfig taskConfig) {
    Context context = new Context();

    // TODO: check if server is in correct timezone (if not set it on Docker image)

    OffsetDateTime now = OffsetDateTime.now();
    context.setVariable("date", now.format((DateTimeFormatter.ISO_LOCAL_DATE)));
    context.setVariable("amount", taskConfig.getSpendValue());

    // String template = taskConfig.getGoogleTaskDescriptionTemplate();
    // return templateEngine.process(template, context);

    // TODO: remove this (just for test)

    return "Test description";
  }
}
