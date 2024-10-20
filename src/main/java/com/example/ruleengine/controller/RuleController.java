package com.example.ruleengine.controller;

import com.example.ruleengine.model.Rule;
import com.example.ruleengine.model.Node;
import com.example.ruleengine.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
public class RuleController {
    @Autowired
    private RuleService ruleService;

    @PostMapping("/create_rule")
    public ResponseEntity<Node> createRule(@RequestBody String ruleString) {
        Node ast = ruleService.createRule(ruleString);
        if (ast == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ast);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Rule>> getAllRules() {
        List<Rule> rules = ruleService.getAllRules();
        return ResponseEntity.ok(rules);
    }

    @PostMapping("/combine_rules")
    public ResponseEntity<Node> combineRules(@RequestBody List<String> ruleStrings) {
        Node combinedAST = ruleService.combineRules(ruleStrings.stream()
            .map(ruleService::createRule)
            .toList());
        return ResponseEntity.ok(combinedAST);
    }

    @PostMapping("/evaluate_rule")
    public ResponseEntity<Boolean> evaluateRule(@RequestBody Map<String, Object> data, @RequestParam Long ruleId) {
        Rule rule = ruleService.getAllRules().stream().filter(r -> r.getId().equals(ruleId)).findFirst().orElse(null);
        if (rule == null) {
            return ResponseEntity.badRequest().build();
        }

        Node ast = ruleService.createRule(rule.getRuleString());
        boolean result = ruleService.evaluateRule(ast, data);
        return ResponseEntity.ok(result);
    }
}
