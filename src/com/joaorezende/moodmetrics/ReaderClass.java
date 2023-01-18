package com.joaorezende.moodmetrics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class ReaderClass {

    private String strFile;

    private String className;
    private int numPublicVars = 0;
    private int numPrivateVars = 0;
    private int numProtectedVars = 0;
    private int numPublicMethods = 0;
    private int numPrivateMethods = 0;
    private int numProtectedMethods = 0;

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
        cu.accept(new ASTVisitor() {
            public boolean visit(VariableDeclarationFragment node) {
                int modifiers = 0;
                if (node.getParent() instanceof FieldDeclaration) {
                    modifiers = ((FieldDeclaration) node.getParent()).getModifiers();
                } else if (node.getParent() instanceof VariableDeclarationStatement) {
                    modifiers = ((VariableDeclarationStatement) node.getParent()).getModifiers();
                }

                if (modifiers == 1) {
                    numPublicVars++;
                } else if (modifiers == 2) {
                    numPrivateVars++;
                } else if (modifiers == 4) {
                    numProtectedVars++;
                }

                return false;
            }

            public boolean visit(MethodDeclaration node) {
                int modifiers = node.getModifiers();

                if (modifiers == 1) {
                    numPublicMethods++;
                } else if (modifiers == 2) {
                    numPrivateMethods++;
                } else if (modifiers == 4) {
                    numProtectedMethods++;
                }

                return false;
            }
        });
    }

    public String getClassName() {
        return className;
    }

    public int getNumPublicVars() {
        return numPublicVars;
    }

    public int getNumPrivateVars() {
        return numPrivateVars;
    }

    public int getNumProtectedVars() {
        return numProtectedVars;
    }

    public int getNumPublicMethods() {
        return numPublicMethods;
    }

    public int getNumPrivateMethods() {
        return numPrivateMethods;
    }

    public int getNumProtectedMethods() {
        return numProtectedMethods;
    }
}
