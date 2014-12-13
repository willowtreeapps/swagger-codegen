package com.wordnik.swagger.codegen;

import com.wordnik.swagger.codegen.languages.*;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.util.*;

import io.swagger.parser.SwaggerParser;

import org.apache.commons.cli.*;

import java.io.File;
import java.util.*;

public class Codegen extends DefaultGenerator {
  public static void main(String[] args) {
    List<CodegenConfig> extensions = getExtensions();
    Map<String, CodegenConfig> configs = new HashMap<String, CodegenConfig>();

    StringBuilder sb = new StringBuilder();
    for(CodegenConfig config : extensions) {
      if(sb.toString().length() != 0)
        sb.append(", ");
      sb.append(config.getName());
      configs.put(config.getName(), config);
    }

    Options options = new Options();
    options.addOption("h", "help", false, "shows this message");
    options.addOption("l", "lang", true, "client language to generate.\nAvailable languages include:\n\t[" + sb.toString() + "]");
    options.addOption("o", "output", true, "where to write the generated files");
    options.addOption("i", "input-spec", true, "location of the swagger spec, as URL or file");
    options.addOption("t", "template-dir", true, "folder containing the template files");
    options.addOption("p", "package-name", true, "package name for the project");

    ClientOptInput clientOptInput = new ClientOptInput();
    ClientOpts clientOpts = new ClientOpts();
    Swagger swagger = null;

    CommandLine cmd = null;
    try {
      CommandLineParser parser = new BasicParser();

      cmd = parser.parse(options, args);
      if (cmd.hasOption("l"))
        clientOptInput.setConfig(getConfig(cmd.getOptionValue("l"), configs));
      if (cmd.hasOption("o"))
        clientOptInput.getConfig().setOutputDir(cmd.getOptionValue("o"));
      if (cmd.hasOption("p")) {
        CodegenConfig config = getConfig(String.valueOf(cmd.getOptionValue("l")), configs);
        if (config != null) {
          config.additionalProperties().put("package", cmd.getOptionValue("p"));
        }
      }
      if (cmd.hasOption("h")) {
        if(cmd.hasOption("l")) {
          CodegenConfig config = getConfig(String.valueOf(cmd.getOptionValue("l")), configs);
          if(config != null) {
            options.addOption("h", "help", true, config.getHelp());
            usage(options);
            return;
          }
        }
        else {
          options.addOption("h", "help", true, "config.getHelp()");
        }
        usage(options);
        return;
      }
      if (cmd.hasOption("i"))
        swagger = new SwaggerParser().read(cmd.getOptionValue("i"));
      if (cmd.hasOption("t"))
        clientOpts.getProperties().put("templateDir", String.valueOf(cmd.getOptionValue("t")));
    }
    catch (Exception e) {
      usage(options);
      return;
    }
    try{
      clientOptInput
        .opts(clientOpts)
        .swagger(swagger);
      new Codegen().opts(clientOptInput).generate();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static List<CodegenConfig> getExtensions() {
    ServiceLoader<CodegenConfig> loader = ServiceLoader.load(CodegenConfig.class);
    List<CodegenConfig> output = new ArrayList<CodegenConfig>();
    Iterator<CodegenConfig> itr = loader.iterator();
    while(itr.hasNext()) {
      output.add(itr.next());
    }
    return output;
  }


  static void usage(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "Codegen", options );
  }

  static CodegenConfig getConfig(String name, Map<String, CodegenConfig> configs) {
    if(configs.containsKey(name)) {
      return configs.get(name);
    }
    else {
      // see if it's a class
      try {
        System.out.println("loading class " + name);
        Class customClass = Class.forName(name);
        System.out.println("loaded");
        return (CodegenConfig)customClass.newInstance();
      }
      catch (Exception e) {
        throw new RuntimeException("can't load class " + name);
      }
    }
  }
}
