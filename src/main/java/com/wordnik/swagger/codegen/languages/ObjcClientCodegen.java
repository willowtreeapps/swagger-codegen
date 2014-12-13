package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.models.*;

import java.util.*;
import java.io.File;

public class ObjcClientCodegen extends DefaultCodegen implements CodegenConfig {
    protected Set<String> foundationClasses = new HashSet<String>();
    protected String sourceFolder = "client";
    protected static String PREFIX = "SWG";
  
    protected String modelName = "";

    public String getName() {
        return "objc";
    }

    public String getHelp() {
        return "Generates an Objective-C client library.";
    }

    public ObjcClientCodegen() {
        super();
        outputFolder = "generated-code/objc";
        modelTemplateFiles.put("model-header.mustache", ".h");
        modelTemplateFiles.put("model-body.mustache", ".m");
        apiTemplateFiles.put("api-header.mustache", ".h");
        apiTemplateFiles.put("api-body.mustache", ".m");
        templateDir = "objc";
        modelPackage = "";

        defaultIncludes = new HashSet<String>(
            Arrays.asList(
                "bool",
                    "int",
                        "NSString",
                            "NSObject", 
                                "NSArray",
                                    "NSNumber",
                                        "NSDictionary",
                                            "NSMutableArray",
                                                "NSMutableDictionary")
                                                    );
        languageSpecificPrimitives = new HashSet<String>(
            Arrays.asList(
                "NSNumber",
                    "NSString",
                        "NSObject",
                            "bool")
                                );

        reservedWords = new HashSet<String>(
            Arrays.asList(
                "void", "char", "short", "int", "void", "char", "short", "int",
                    "long", "float", "double", "signed", "unsigned", "id", "const",
                        "volatile", "in", "out", "inout", "bycopy", "byref", "oneway",
                            "self", "super"
                                ));

        typeMapping = new HashMap<String, String>();
        typeMapping.put("enum", "NSString");
        typeMapping.put("Date", "SWGDate");
        typeMapping.put("DateTime", "SWGDate");
        // typeMapping.put("Date", "SWGDate");
        typeMapping.put("boolean", "NSNumber");
        typeMapping.put("string", "NSString");
        typeMapping.put("integer", "NSNumber");
        typeMapping.put("int", "NSNumber");
        typeMapping.put("float", "NSNumber");
        typeMapping.put("long", "NSNumber");
        typeMapping.put("double", "NSNumber");
        typeMapping.put("array", "NSArray");
        typeMapping.put("map", "NSDictionary");
        typeMapping.put("number", "NSNumber");
        typeMapping.put("List", "NSArray");
        typeMapping.put("object", "NSObject");

        importMapping = new HashMap<String, String> ();
        importMapping.put("Date", "SWGDate");

        foundationClasses = new HashSet<String> (
            Arrays.asList(
                "NSNumber",
                    "NSObject",
                        "NSString",
                            "NSDictionary")
                                );

        instantiationTypes.put("array", "NSMutableArray");
        instantiationTypes.put("map", "NSMutableDictionary");

        supportingFiles.add(new SupportingFile("SWGObject.h", sourceFolder, "SWGObject.h"));
        supportingFiles.add(new SupportingFile("SWGObject.m", sourceFolder, "SWGObject.m"));
        supportingFiles.add(new SupportingFile("SWGApiClient.h", sourceFolder, "SWGApiClient.h"));
        supportingFiles.add(new SupportingFile("SWGApiClient.m", sourceFolder, "SWGApiClient.m"));
        supportingFiles.add(new SupportingFile("SWGFile.h", sourceFolder, "SWGFile.h"));
        supportingFiles.add(new SupportingFile("SWGFile.m", sourceFolder, "SWGFile.m"));
        supportingFiles.add(new SupportingFile("SWGDate.h", sourceFolder, "SWGDate.h"));
        supportingFiles.add(new SupportingFile("SWGDate.m", sourceFolder, "SWGDate.m"));
        supportingFiles.add(new SupportingFile("Podfile.mustache", "", "Podfile"));
    }

    @Override
    public String toInstantiationType(Property p) {
        if (p instanceof MapProperty) {
            MapProperty ap = (MapProperty) p;
            String inner = getSwaggerType(ap.getAdditionalProperties());
            return instantiationTypes.get("map");
        }
        else if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            String inner = getSwaggerType(ap.getItems());
            return instantiationTypes.get("array");
        }
        else
            return null;
    }

    @Override
    public String getTypeDeclaration(String name) {
        if(languageSpecificPrimitives.contains(name) && !foundationClasses.contains(name))
            return name;
        else
            return name + " *";
    }

    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        String type = null;
        if(typeMapping.containsKey(swaggerType)) {
            type = typeMapping.get(swaggerType);
            if(languageSpecificPrimitives.contains(type) && !foundationClasses.contains(type))
                return toModelName(type);
        }
        else
            type = swaggerType;
        return toModelName(type);
    }

    @Override
    public String getTypeDeclaration(Property p) {
        String swaggerType = getSwaggerType(p);
        if(languageSpecificPrimitives.contains(swaggerType) && !foundationClasses.contains(swaggerType))
            return toModelName(swaggerType);
        else
            return swaggerType + " *";
    }

    @Override
    public String toModelName(String type) {
        String name = Character.toUpperCase(type.charAt(0)) + type.substring(1);
        if(typeMapping.keySet().contains(type) ||
            foundationClasses.contains(type) ||
                importMapping.values().contains(type) ||
                    defaultIncludes.contains(type) ||
        languageSpecificPrimitives.contains(type)) {
            return name;
        }
        else {
            return PREFIX + name;
        }
    }

    @Override
    public String toModelImport(String name) {
        // name = name + ".h";
        if("".equals(modelPackage()))
            return name;
        else
            return modelPackage() + "." + name;
    }

    @Override
    public String toDefaultValue(Property p) {
        return null;
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + File.separator + sourceFolder;
    }

    @Override
    public String modelFileFolder() {
        return outputFolder + File.separator + sourceFolder;
    }

    @Override
    public String toModelFilename(String name) {
        return PREFIX + initialCaps(name);
    }

    @Override
    public String toApiName(String name) {
        return PREFIX + initialCaps(name) + "Api";
    }

    public String toApiFilename(String name) {
        return PREFIX + initialCaps(name) + "Api";
    }

    @Override
    public String toVarName(String name) {
        String paramName = name.replaceAll("[^a-zA-Z0-9_]","");
        if(paramName.startsWith("new") || reservedWords.contains(paramName)) {
            return escapeReservedWord(paramName);
        }
        else
            return paramName;
    }

    public String escapeReservedWord(String name) {
        return Character.toLowerCase(modelName.charAt(0)) + modelName.substring(1) + initialCaps(name);
    }
  
    @Override
    public CodegenModel fromModel(String name, Model model)
    {
        modelName = name;
        CodegenModel m = super.fromModel(name, model);
        if (!(model instanceof ArrayModel))
        {
            ModelImpl impl = (ModelImpl) model;
            int count = 0;
            if(impl.getProperties() != null && impl.getProperties().size() > 0)
            {
                for(String key: impl.getProperties().keySet())
                {
                    Property prop = impl.getProperties().get(key);

                    if(prop == null)
                    {
                        System.out.println("null property for " + key);
                    }
                    else
                    {
                        CodegenProperty cp = fromProperty(key, prop);
                        cp.required = false;
                        if(impl.getRequired() != null)
                        {
                            for(String req : impl.getRequired()) 
                            {
                                if(key.equals(req))
                                {
                                    cp.required = true;
                                }
                            }
                        }
                        if (count == 0)
                        {
                            cp.lowercaseName = toVarName(cp.name);
                            cp.name = initialCaps(toVarName(cp.name));
                            m.initialVar = cp;
                        }
                        else
                        {
                            m.remainingVars.add(cp);
                        }
                        count += 1;
                    }
                }
            }
        }
        return m;
    }
}