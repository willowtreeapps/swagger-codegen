package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.codegen.SupportingFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by christhoma on 12/13/14.
 */
public class MonkeypodAndroidClientCodegen extends AndroidClientCodegen {

    public MonkeypodAndroidClientCodegen() {
        super();
        //maybe change this output folder
        outputFolder = "generated-code/android";
        modelTemplateFiles.put("model.mustache", ".java");
        apiTemplateFiles.put("api.mustache", ".java");
        templateDir = "android-java";
        apiPackage = "com.wordnik.client.api";
        modelPackage = "com.wordnik.client.model";

        
        additionalProperties.put("invokerPackage", invokerPackage);
        additionalProperties.put("groupId", groupId);
        additionalProperties.put("artifactId", artifactId);
        additionalProperties.put("artifactVersion", artifactVersion);

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
}
