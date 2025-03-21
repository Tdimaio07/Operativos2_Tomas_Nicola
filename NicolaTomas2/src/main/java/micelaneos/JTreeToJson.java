/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package micelaneos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;

public class JTreeToJson {

    public static void main(String[] args) {
        // Create a sample JTree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("Child 1");
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("Child 2");
        root.add(child1);
        root.add(child2);
        child1.add(new DefaultMutableTreeNode("Grandchild 1"));
        child2.add(new DefaultMutableTreeNode("Grandchild 2"));

        JTree jTree = new JTree(root);

        // Convert and save the JTree to a JSON file
        saveJTreeToJson("files/tree.json", jTree);
    }

    public static void saveJTreeToJson(String filename, JTree jTree) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonTreeNode jsonRoot = convertToJsonTreeNode((DefaultMutableTreeNode) jTree.getModel().getRoot());

        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(jsonRoot, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonTreeNode convertToJsonTreeNode(DefaultMutableTreeNode jTreeNode) {
        JsonTreeNode jsonTreeNode = new JsonTreeNode(jTreeNode.toString());

        Enumeration<TreeNode> children = jTreeNode.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            jsonTreeNode.addChild(convertToJsonTreeNode(child));
        }

        return jsonTreeNode;
    }

    static class JsonTreeNode {
        String name;
        JsonTreeNode[] children;

        public JsonTreeNode(String name) {
            this.name = name;
            this.children = new JsonTreeNode[0];
        }

        public void addChild(JsonTreeNode child) {
            JsonTreeNode[] newChildren = new JsonTreeNode[this.children.length + 1];
            System.arraycopy(this.children, 0, newChildren, 0, this.children.length);
            newChildren[this.children.length] = child;
            this.children = newChildren;
        }
    }
}
