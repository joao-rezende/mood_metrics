package com.joaorezende.moodmetrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ReaderClass {

    private String strFile;

    private String className;
    private Type superClass;
    private List<String> attributes = new ArrayList<>();
    private List<String> inheritableAttributes = new ArrayList<>();
    private List<String> methods = new ArrayList<>();
    private List<String> overrideMethods = new ArrayList<>();
    private List<String> inheritableMethods = new ArrayList<>();
    private int numHiddenAttributes = 0;
    private int numHiddenMethods = 0;
    private int numInheritedAttributes = 0;
    private int numInheritedMethods = 0;
    private List<String> connections = new ArrayList<>();
    private List<String> dataTypes = new ArrayList<>();

    public ReaderClass(Path path) {
        if (!this.readFile(path))
            return;

        className = path.getFileName().toString().replace(".java", "");

        ASTParser parser = ASTParser.newParser(AST.JLS8);

        parser.setSource(strFile.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        runVisitor(cu);
    }

    private Boolean readFile(Path path) {
        List<String> listFile;

        try {
            listFile = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("Error read file");
            return false;
        }

        strFile = "";
        for (String filePart : listFile) {
            strFile += filePart + "\n";
        }

        return true;
    }

    private void runVisitor(CompilationUnit cu) {
        List listTypes = cu.types();

        if (listTypes.size() > 0) {
            TypeDeclaration typeDeclaration = (TypeDeclaration) listTypes.get(0);
            FieldDeclaration[] attributes = typeDeclaration.getFields();

            for (FieldDeclaration attribute : attributes) {
                Type type = attribute.getType();
                dataTypes.add(type.toString());

                String name = attribute.fragments().get(0).toString();
                if (name.indexOf("=") != -1) {
                    name = name.substring(0, name.indexOf("="));
                }
                if (name.indexOf("[") != -1) {
                    name = name.substring(0, name.indexOf("["));
                }
                int modifiers = attribute.getModifiers();

                this.attributes.add(name);

                if (Modifier.isPrivate(modifiers)) {
                    numHiddenAttributes++;
                } else {
                    inheritableAttributes.add(name);
                }
            }
        }

        cu.accept(new ASTVisitor() {
            public boolean visit(MethodDeclaration node) {
                ASTNode parent = node.getParent();
                String parentName = className;
                if (parent instanceof TypeDeclaration) {
                    parentName = ((TypeDeclaration) parent).getName().toString();
                }
                int modifiers = node.getModifiers();

                String name = node.getName().toString() + "(";
                List<SingleVariableDeclaration> parameters = node.parameters();
                for (int i = 0; i < parameters.size(); i++) {
                    SingleVariableDeclaration parameter = parameters.get(i);
                    name += parameter.getType();
                    if (i < parameters.size() - 1) {
                        name += ", ";
                    }
                }
                name += ")";

                if (className.equals(parentName) && !node.isConstructor() && !methods.contains(name)) {
                    methods.add(name);

                    if (Modifier.isPrivate(modifiers)) {
                        numHiddenMethods++;
                    } else if (!node.getName().toString().equals(className)) {
                        inheritableMethods.add(name);
                    }
                }

                return super.visit(node);
            }

            public boolean visit(TypeDeclaration node) {
                superClass = node.getSuperclassType();

                if (superClass != null) {
                    connections.add(getSuperclassName());
                }

                return super.visit(node);
            }
        });
    }

    public String getClassName() {
        return className;
    }

    public String getSuperclassName() {
        if (superClass == null)
            return null;

        int position = superClass.toString().indexOf("<");
        if (position != -1) {
            return superClass.toString().substring(0, position);
        }

        return superClass.toString();
    }

    public int getNumHiddenAttributes() {
        return numHiddenAttributes;
    }

    public int getNumHiddenMethods() {
        return numHiddenMethods;
    }

    public int setNumInheritedAttributes(int numInheritedAttributes) {
        return this.numInheritedAttributes = numInheritedAttributes;
    }

    public int setNumInheritedMethods(int numInheritedMethods) {
        return this.numInheritedMethods = numInheritedMethods;
    }

    public int getNumAttributes() {
        return attributes.size();
    }

    public int getNumMethods() {
        return methods.size();
    }

    public int getNumInheritedAttributes() {
        return numInheritedAttributes;
    }

    public int getNumInheritedMethods() {
        return numInheritedMethods;
    }

    public String getAttributes() {
        String strAttributes = "";
        for (String attribute : attributes) {
            strAttributes += attribute + " ";
        }

        return strAttributes;
    }

    public List<String> getInheritableAttributes() {
        return inheritableAttributes;
    }

    public String getMethods() {
        String strMethods = "";
        for (String method : methods) {
            strMethods += method + " ";
        }

        return strMethods;
    }

    public String getOverrideMethods() {
        String strMethods = "";
        for (String method : overrideMethods) {
            strMethods += method + " ";
        }

        return strMethods;
    }

    public List<String> getInheritableMethods() {
        return inheritableMethods;
    }

    public List<String> getConnections() {
        return connections;
    }

    public Boolean connectionExist(String className) {
        for (String connection : connections) {
            if (connection.equals(className)) {
                return true;
            }
        }

        return false;
    }

    public void addConnection(String className) {
        connections.add(className);
    }

    public List<String> getDataTypes() {
        return dataTypes;
    }
}
