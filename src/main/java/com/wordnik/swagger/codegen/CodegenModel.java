package com.wordnik.swagger.codegen;

import com.wordnik.swagger.models.*;
import com.wordnik.swagger.models.properties.*;

import java.util.*;

public class CodegenModel {
  public String parent;
  public String name, classname, description, classVarName, modelJson;
  public String defaultValue;
  public List<CodegenProperty> vars = new ArrayList<CodegenProperty>();
  public CodegenProperty initialVar;
  public List<CodegenProperty> remainingVars = new ArrayList<CodegenProperty>();
  public Set<String> imports = new HashSet<String>();
  public Boolean hasVars, emptyVars, hasMoreModels;
  public ExternalDocs externalDocs;
}