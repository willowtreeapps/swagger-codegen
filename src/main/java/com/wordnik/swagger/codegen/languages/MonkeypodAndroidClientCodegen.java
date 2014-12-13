package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.codegen.CodegenConfig;
import com.wordnik.swagger.codegen.DefaultCodegen;
import com.wordnik.swagger.codegen.DefaultGenerator;
import com.wordnik.swagger.codegen.SupportingFile;
import com.wordnik.swagger.models.properties.ArrayProperty;
import com.wordnik.swagger.models.properties.MapProperty;
import com.wordnik.swagger.models.properties.Property;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by christhoma on 12/13/14.
 */
public class MonkeypodAndroidClientCodegen extends DefaultCodegen implements CodegenConfig {

    protected String invokerPackage = "com.wordnik.client";
    protected String sourceFolder = "src/main/java";
    protected String packageName = "";


    public MonkeypodAndroidClientCodegen() {
        super();
        outputFolder = (String) additionalProperties.get("output-folder");
        modelTemplateFiles.put("model.mustache", ".java");
        apiTemplateFiles.put("api.mustache", ".java");
        templateDir = "android-java";

        //TODO NEED TO CHANGE API PACKAGE AND MODEL PACKAGE TO BE PASSED IN PARAMETERS
        apiPackage = "com.wordnik.client.api";
        modelPackage = "com.wordnik.client.model";


        additionalProperties.put("invokerPackage", invokerPackage);

        supportingFiles.add(new SupportingFile("pom.mustache", "", "pom.xml"));
        supportingFiles.add(new SupportingFile("apiInvoker.mustache",
                (sourceFolder + File.separator + invokerPackage).replace(".", java.io.File.separator), "ApiInvoker.java"));
        supportingFiles.add(new SupportingFile("httpPatch.mustache",
                (sourceFolder + File.separator + invokerPackage).replace(".", java.io.File.separator), "HttpPatch.java"));
        supportingFiles.add(new SupportingFile("jsonUtil.mustache",
                (sourceFolder + File.separator + invokerPackage).replace(".", java.io.File.separator), "JsonUtil.java"));
        supportingFiles.add(new SupportingFile("apiException.mustache",
                (sourceFolder + File.separator + invokerPackage).replace(".", java.io.File.separator), "ApiException.java"));

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
    public String escapeReservedWord(String name) {
        return "_" + name;
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + File.separator + sourceFolder + File.separator + apiPackage().replaceAll("\\.", File.separator);
    }

    public String modelFileFolder() {
        return outputFolder + File.separator + sourceFolder + File.separator + modelPackage().replaceAll("\\.", File.separator);
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

    @Override
    public String getName() {
        return "android-monkeypod";
    }

    @Override
    public String getHelp() {
        return "Generates android code to interface with a monkeypod api.";
    }
}
