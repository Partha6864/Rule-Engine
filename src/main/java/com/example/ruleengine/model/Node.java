package com.example.ruleengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    private String type;
    private String value;
    private Node left;
    private Node right;

    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
