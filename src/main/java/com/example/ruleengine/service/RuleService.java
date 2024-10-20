package com.example.ruleengine.service;

import com.example.ruleengine.model.Node;
import com.example.ruleengine.model.Rule;
import com.example.ruleengine.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Stack;

@Service
public class RuleService {
    @Autowired
    private RuleRepository ruleRepository;

    public Node createRule(String ruleString) {
        Node ast = parseRule(ruleString);
        saveRule(ruleString);
        return ast;
    }

    private Node parseRule(String ruleString) {
        String[] tokens = ruleString.split(" ");
        Stack<Node> stack = new Stack<>();

        for (String token : tokens) {
            if (token.equals("AND") || token.equals("OR")) {
                Node right = stack.pop();
                Node left = stack.pop();
                Node operatorNode = new Node("Operator", token);
                operatorNode.setLeft(left);
                operatorNode.setRight(right);
                stack.push(operatorNode);
            } else {
                stack.push(new Node("Operand", token));
            }
        }
        return stack.pop();
    }

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    public Rule saveRule(String ruleString) {
        Rule rule = new Rule();
        rule.setRuleString(ruleString);
        return ruleRepository.save(rule);
    }

    public boolean evaluateRule(Node root, Map<String, Object> data) {
        if (root == null) {
            return false;
        }
        
        if ("Operand".equals(root.getType())) {
            String[] parts = root.getValue().split(" ");
            String attribute = parts[0];
            String operator = parts[1];
            String value = parts[2];
            Object attributeValue = data.get(attribute);
            return compare(attributeValue, operator, value);
        } else if ("Operator".equals(root.getType())) {
            boolean leftResult = evaluateRule(root.getLeft(), data);
            boolean rightResult = evaluateRule(root.getRight(), data);
            return "AND".equals(root.getValue()) ? leftResult && rightResult : leftResult || rightResult;
        }
        return false;
    }

    public Node combineRules(List<Node> nodes) {
        Node combined = nodes.get(0);
        for (int i = 1; i < nodes.size(); i++) {
            Node operatorNode = new Node("Operator", "OR");
            operatorNode.setLeft(combined);
            operatorNode.setRight(nodes.get(i));
            combined = operatorNode;
        }
        return combined;
    }

    private boolean compare(Object attributeValue, String operator, String value) {
        if (attributeValue instanceof Number) {
            double numericValue = Double.parseDouble(value);
            double numericAttributeValue = Double.parseDouble(attributeValue.toString());
            switch (operator) {
                case ">":
                    return numericAttributeValue > numericValue;
                case "<":
                    return numericAttributeValue < numericValue;
                case "=":
                    return numericAttributeValue == numericValue;
                default:
                    return false;
            }
        } else {
            return attributeValue.toString().equals(value);
        }
    }
}
