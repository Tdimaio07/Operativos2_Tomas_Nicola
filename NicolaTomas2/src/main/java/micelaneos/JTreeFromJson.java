/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package micelaneos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;

import java.io.FileReader;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeFromJson {

    public static JTree readJTreeFromJson(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader(filename)) {
            JsonTreeNode jsonRoot = gson.fromJson(reader, JsonTreeNode.class);
            DefaultMutableTreeNode root = convertToDefaultMutableTreeNode(jsonRoot);
            return new JTree(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DefaultMutableTreeNode convertToDefaultMutableTreeNode(JsonTreeNode jsonTreeNode) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(jsonTreeNode.name);
        for (JsonTreeNode child : jsonTreeNode.children) {
            treeNode.add(convertToDefaultMutableTreeNode(child));
        }
        return treeNode;
    }

    static class JsonTreeNode {
        String name;
        JsonTreeNode[] children;
    }
}