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

    protected String sourceFolder = "src/main/java";
    protected String basePackage = "";


    public MonkeypodAndroidClientCodegen() {
        super();
        outputFolder = (String) additionalProperties.get("output-folder");
        modelTemplateFiles.put("model.mustache", ".java");
        apiTemplateFiles.put("api.mustache", ".java");
        templateDir = "android-java";
        basePackage = (String) additionalProperties.get("package");
        modelPackage = basePackage + ".model";
        apiPackage = basePackage + ".client";

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
        return outputFolder + File.separator + sourceFolder + File.separator + apiPackage().replaceAll("\\.", File.separator);
    }

    public String modelFileFolder() {
        return outputFolder + File.separator + sourceFolder + File.separator + modelPackage().replaceAll("\\.", File.separator);
    }

    @Override
    public String getTypeDeclaration(Property p) {
        if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            return getSwaggerType(p) + "<" + getTypeDeclaration(inner) + ">";
        } else if (p instanceof MapProperty) {
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
        if (typeMapping.containsKey(swaggerType)) {
            type = typeMapping.get(swaggerType);
            if (languageSpecificPrimitives.contains(type))
                return toModelName(type);
        } else
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
