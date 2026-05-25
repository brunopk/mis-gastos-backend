package com.bruno.misgastos.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public interface ThymeleafUtils {
  String PREFIX = "templates/";

  static TemplateEngine buildTemplateEngine() {
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix(PREFIX);
    resolver.setTemplateMode(TemplateMode.TEXT);
    resolver.setCharacterEncoding("UTF-8");

    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(resolver);

    return templateEngine;
  }
}
