package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.properties.*;

import java.io.File;
import java.util.*;

/**
 * Created by christhoma on 12/13/14.
 */
public class AndroidGradleClientCodegen extends DefaultCodegen implements CodegenConfig {
    protected String basePackage = "";

    public AndroidGradleClientCodegen() {
        super();
        outputFolder = (String) additionalProperties.get("output-folder");
        modelTemplateFiles.put("model.mustache", ".java");
//        apiTemplateFiles.put("api.mustache", ".java");
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

        typeMapping.put("Long", "long");
        typeMapping.put("Boolean", "boolean");
        typeMapping.put("Double", "double");
        typeMapping.put("Integer", "int");
        typeMapping.put("Float", "float");
    }

    @Override
    public void processOpts() {
        super.processOpts();
        if (additionalProperties.containsKey("package")) {
            setPackage((String) additionalProperties.get("package"));
        }
        supportingFiles.add(new SupportingFile("objects.mustache",
                basePackage.replace(".", java.io.File.separator), "Objects.java"));
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
        return outputFolder + File.separator + apiPackage().replaceAll("\\.", File.separator);
    }

    public String modelFileFolder() {
        return outputFolder + File.separator + modelPackage().replaceAll("\\.", File.separator);
    }

    @Override
    public String toDefaultValue(Property p) {
        if (p instanceof BooleanProperty)
            return "false";
        else if (p instanceof DoubleProperty)
            return "0";
        else if (p instanceof FloatProperty)
            return "0";
        else if (p instanceof IntegerProperty)
            return "0";
        else if (p instanceof LongProperty)
            return "0";
        else
            return super.toDefaultValue(p);
    }

    @Override
    public CodegenProperty fromProperty(String name, Property p) {
        CodegenProperty property = super.fromProperty(name, p);
        property.serializeMethod = getSerializeMethod(property.name, p);
        property.deserializeMethod = getDeserializeMethod(property.name, p);
        return property;
    }

    @Override
    public String getTypeDeclaration(Property p) {
        if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            return getSwaggerType(p) + "<" + getBoxedTypeDeclaration(inner) + ">";
        } else if (p instanceof MapProperty) {
            MapProperty mp = (MapProperty) p;
            Property inner = mp.getAdditionalProperties();

            return getSwaggerType(p) + "<String, " + getBoxedTypeDeclaration(inner) + ">";
        }
        return super.getTypeDeclaration(p);
    }

    public String getBoxedTypeDeclaration(Property p) {
        if (p instanceof BooleanProperty) {
            return "Boolean";
        } else if (p instanceof IntegerProperty) {
            return "Integer";
        } else if (p instanceof LongProperty) {
            return "Long";
        } else if (p instanceof FloatProperty) {
            return "Float";
        } else if (p instanceof DoubleProperty) {
            return "Double";
        } else {
            return getTypeDeclaration(p);
        }
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

    public String getSerializeMethod(String name, Property p) {
        if (p instanceof StringProperty) {
            return "writeString(" + name + ")";
        } else if (p instanceof BooleanProperty) {
            return "writeByte((byte)(" + name + "?1:0))";
        } else if (p instanceof IntegerProperty) {
            return "writeInt(" + name + ")";
        } else if (p instanceof LongProperty) {
            return "writeLong(" + name + ")";
        } else if (p instanceof FloatProperty) {
            return "writeFloat(" + name + ")";
        } else if (p instanceof DoubleProperty) {
            return "writeDouble(" + name + ")";
        } else if (p instanceof DateProperty || p instanceof DateTimeProperty) {
            return "writeLong(" + name + ".getTime())";
        } else if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            if (inner instanceof StringProperty) {
                return "writeStringList(" + name + ")";
            } else if (inner instanceof BooleanProperty
                    || inner instanceof IntegerProperty
                    || inner instanceof LongProperty
                    || inner instanceof FloatProperty
                    || inner instanceof DoubleProperty) {
                return "writeList(" + name + ")";
            } else {
                return "writeTypedList(" + name + ")";
            }
        } else if (p instanceof MapProperty) {
            return "writeMap(" + name + ")";
        } else {
            return "writeParcelable(" + name + ", flags)";
        }
    }

    public String getDeserializeMethod(String name, Property p) {
        if (p instanceof StringProperty) {
            return name + " = in.readString()";
        } else if (p instanceof BooleanProperty) {
            return name + " = in.readByte()==1";
        } else if (p instanceof IntegerProperty) {
            return name + " = in.readInt()";
        } else if (p instanceof LongProperty) {
            return name + " = in.readLong()";
        } else if (p instanceof FloatProperty) {
            return name + " = in.readFloat()";
        } else if (p instanceof DoubleProperty) {
            return name + " = in.readDouble()";
        } else if (p instanceof DateProperty || p instanceof DateTimeProperty) {
            return name + " = new Date(in.readLong())";
        } else if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            if (inner instanceof StringProperty) {
                return "in.readStringList(" + name + ")";
            } else if (inner instanceof BooleanProperty
                    || inner instanceof IntegerProperty
                    || inner instanceof LongProperty
                    || inner instanceof FloatProperty
                    || inner instanceof DoubleProperty) {
                return name + " = in.readArrayList(List.class.getClassLoader())";
            } else {
                String innerType = getSwaggerType(inner);
                return "in.readTypedList(" + name + ", " + innerType + ".CREATOR)";
            }
        } else if (p instanceof MapProperty) {
            return name + " = in.readHashMap(Map.class.getClassLoader())";
        } else {
            String type = getSwaggerType(p);
            return name + " = in.readParcelable(" + type + ".class.getClassLoader())";
        }
    }

    @Override
    public String getName() {
        return "gradle-android";
    }

    @Override
    public String getHelp() {
        return "Generates android code for use with a gradle plugin";
    }
}
