package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.properties.*;

import java.util.*;
import java.io.File;

public class JavaClientCodegen extends DefaultCodegen implements CodegenConfig {
  protected String groupId = "com.wordnik";
  protected String artifactId = "swagger-client";
  protected String artifactVersion = "1.0.0";
  protected String sourceFolder = "src/main/java";
  protected String basePackage = "com.wordnik.client";

  public String getName() {
    return "java";
  }

  public String getHelp() {
    return "Generates a Java client library.";
  }

  public JavaClientCodegen() {
    super();
    outputFolder = "generated-code/java";
    modelTemplateFiles.put("model.mustache", ".java");
    apiTemplateFiles.put("api.mustache", ".java");
    templateDir = "Java";

    setPackage(basePackage);

    reservedWords = new HashSet<String> (
      Arrays.asList(
        "abstract", "continue", "for", "new", "switch", "assert",
        "default", "if", "package", "synchronized", "boolean", "do", "goto", "private",
        "this", "break", "double", "implements", "protected", "throw", "byte", "else",
        "import", "public", "throws", "case", "enum", "instanceof", "return", "transient",
        "catch", "extends", "int", "short", "try", "char", "final", "interface", "static",
        "void", "class", "finally", "long", "strictfp", "volatile", "const", "float",
        "native", "super", "while")
    );

    additionalProperties.put("invokerPackage", basePackage);
    additionalProperties.put("groupId", groupId);
    additionalProperties.put("artifactId", artifactId);
    additionalProperties.put("artifactVersion", artifactVersion);

    supportingFiles.add(new SupportingFile("pom.mustache", "", "pom.xml"));
    supportingFiles.add(new SupportingFile("apiInvoker.mustache",
      (sourceFolder + File.separator + basePackage).replace(".", java.io.File.separator), "ApiInvoker.java"));
    supportingFiles.add(new SupportingFile("JsonUtil.mustache",
      (sourceFolder + File.separator + basePackage).replace(".", java.io.File.separator), "JsonUtil.java"));
    supportingFiles.add(new SupportingFile("apiException.mustache",
      (sourceFolder + File.separator + basePackage).replace(".", java.io.File.separator), "ApiException.java"));

    languageSpecificPrimitives = new HashSet<String>(
      Arrays.asList(
        "String",
        "boolean",
        "Boolean",
        "Double",
        "Integer",
        "Long",
        "Float",
        "Object")
      );
    instantiationTypes.put("array", "ArrayList");
    instantiationTypes.put("map", "HashMap");
  }

  @Override
  public void processOpts() {
    super.processOpts();
    if(additionalProperties.containsKey("package")) {
      setPackage((String) additionalProperties.get("package"));
    }
  }

  public void setPackage(String basePackage) {
    this.basePackage = basePackage;
    apiPackage = basePackage + ".api";
    modelPackage = basePackage + ".model";
  }

  @Override
  public String escapeReservedWord(String name) {
    return "_" + name;
  }

  @Override
  public String apiFileFolder() {
    return outputFolder + "/" + sourceFolder + "/" + apiPackage().replaceAll("\\.", "/");
  }

  public String modelFileFolder() {
    return outputFolder + "/" + sourceFolder + "/" + modelPackage().replaceAll("\\.", "/");
  }

  @Override
  public String getTypeDeclaration(Property p) {
    if(p instanceof ArrayProperty) {
      ArrayProperty ap = (ArrayProperty) p;
      Property inner = ap.getItems();
      return getSwaggerType(p) + "<" + getTypeDeclaration(inner) + ">";
    }
    else if (p instanceof MapProperty) {
      MapProperty mp = (MapProperty) p;
      Property inner = mp.getAdditionalProperties();

      return getSwaggerType(p) + "<String, " + getTypeDeclaration(inner) + ">";
    }
    return super.getTypeDeclaration(p);
  }

  @Override
  public String getSwaggerType(Property p) {
    String swaggerType = super.getSwaggerType(p);
    String type = null;
    if(typeMapping.containsKey(swaggerType)) {
      type = typeMapping.get(swaggerType);
      if(languageSpecificPrimitives.contains(type))
        return toModelName(type);
    }
    else
      type = swaggerType;
    return toModelName(type);
  }
}