package com.wordnik.swagger.codegen.languages;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.Operation;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.ParameterMetaData;
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
        apiTemplateFiles.put("intentService.mustache", "Service.java");
        apiTemplateFiles.put("intentServiceProxy.mustache", "Proxy.java");
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
        supportingFiles.add(new SupportingFile("apiBroadcastReceiver.mustache",
                basePackage.replace(".", File.separator), "ApiBroadcastReceiver.java"));
        supportingFiles.add(new SupportingFile("apiBroadcastReceiverVoid.mustache",
                basePackage.replace(".", File.separator), "ApiBroadcastReceiverVoid.java"));
        supportingFiles.add(new SupportingFile("apiBroadcastReceiverList.mustache",
                basePackage.replace(".", File.separator), "ApiBroadcastReceiverList.java"));
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
        property.serializeMethod = getSerializeMethod(p);
        property.deserializeMethod = getDeserializeMethod(property.name, p);
        return property;
    }

    @Override
    public CodegenParameter fromParameter(Parameter param, Set<String> imports) {
        CodegenParameter parameter = super.fromParameter(param, imports);
        parameter.serializeMethod = getSerializeMethod(parameter);
        parameter.deserializeMethodName = getDeserializeMethod(parameter);
        return parameter;
    }

    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation) {
        CodegenOperation op = super.fromOperation(path, httpMethod, operation);
        op.serializeMethod = getSerializeMethod(op);
        return op;
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

    public Mustache.Lambda getSerializeMethod(final Property p) {
        return new Mustache.Lambda() {
            @Override
            public void execute(Template.Fragment fragment, Writer writer) throws IOException {
                if (p instanceof StringProperty) {
                    writer.write("writeString(");
                    fragment.execute(writer);
                    writer.write(")");
                } else if (p instanceof BooleanProperty) {
                    writer.write("writeByte((byte)(");
                    fragment.execute(writer);
                    writer.write("?1:0))");
                } else if (p instanceof IntegerProperty) {
                    writer.write("writeInt(");
                    fragment.execute(writer);
                    writer.write(")");
                } else if (p instanceof LongProperty) {
                    writer.write("writeLong(");
                    fragment.execute(writer);
                    writer.write(")");
                } else if (p instanceof FloatProperty) {
                    writer.write("writeFloat(");
                    fragment.execute(writer);
                    writer.write(")");
                } else if (p instanceof DoubleProperty) {
                    writer.write("writeDouble(");
                    fragment.execute(writer);
                    writer.write(")");
                } else if (p instanceof DateProperty || p instanceof DateTimeProperty) {
                    writer.write("writeLong(");
                    fragment.execute(writer);
                    writer.write(".getTime())");
                } else if (p instanceof ArrayProperty) {
                    ArrayProperty ap = (ArrayProperty) p;
                    Property inner = ap.getItems();
                    if (inner instanceof StringProperty) {
                        writer.write("writeStringList(");
                        fragment.execute(writer);
                        writer.write(")");
                    } else if (inner instanceof BooleanProperty
                            || inner instanceof IntegerProperty
                            || inner instanceof LongProperty
                            || inner instanceof FloatProperty
                            || inner instanceof DoubleProperty) {
                        writer.write("writeList(");
                        fragment.execute(writer);
                        writer.write(")");
                    } else {
                        writer.write("writeTypedList(");
                        fragment.execute(writer);
                        writer.write(")");
                    }
                } else if (p instanceof MapProperty) {
                    writer.write("writeMap(");
                    fragment.execute(writer);
                    writer.write(")");
                } else {
                    writer.write("writeParcelable(");
                    fragment.execute(writer);
                    writer.write(", flags)");
                }
            }
        };
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

    public String getSerializeMethod(CodegenParameter param) {
        if (param.dataType.equals("String")) {
            return "getStringExtra";
        } else if (param.dataType.equals("boolean")) {
            return "getBooleanExtra";
        } else if (param.dataType.equals("int")) {
            param.defaultValue = "0";
            return "getIntExtra";
        } else if (param.dataType.equals("long")) {
            param.defaultValue = "0";
            return "getLongExtra";
        } else if (param.dataType.equals("float")) {
            param.defaultValue = "0";
            return "getFloatExtra";
        } else if (param.dataType.equals("double")) {
            param.defaultValue = "0";
            return "getDoubleExtra";
        } else if (param.dataType.startsWith("List")) {
            String inner = param.baseType;
            if (inner.equals("String")) {
                return "getStringArrayListExtra";
            } else {
                return "getParcelableArrayListExtra";
            }

        } else {
            return "getParcelableExtra";
        }
    }

    public String getDeserializeMethod(CodegenParameter param) {
        if (param.dataType.startsWith("List")) {
            String inner = param.baseType;
            if (inner.equals("String")) {
                return "putStringArrayListExtra";
            } else {
                return "putParcelableArrayListExtra";
            }
        } else {
            return "putExtra";
        }
    }

    private String getSerializeMethod(CodegenOperation operation) {
        if (operation.returnType == null) {
            return "ApiBroadcastReceiverVoid";
        } else if (operation.returnType.startsWith("List")) {
            return "ApiBroadcastReceiverList<" + operation.returnBaseType + ">";
        } else {
            return "ApiBroadcastReceiver<" + operation.returnType + ">";
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
