document.getElementById("createRuleBtn").addEventListener("click", createRule);
document.getElementById("combineRulesBtn").addEventListener("click", combineRules);
document.getElementById("evaluateRuleBtn").addEventListener("click", evaluateRule);

function createRule() {
    const ruleString = document.getElementById("ruleString").value;
    fetch("/api/rules/create_rule", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ruleString)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to create rule");
        }
        return response.json();
    })
    .then(ast => {
        document.getElementById("resultOutput").textContent = JSON.stringify(ast, null, 2);
        alert("Rule created successfully!");
    })
    .catch(error => {
        alert("Error: " + error.message);
    });
}

function combineRules() {
    const ruleStrings = document.getElementById("combineRulesInput").value.split('\n').filter(Boolean);
    fetch("/api/rules/combine_rules", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(ruleStrings)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to combine rules");
        }
        return response.json();
    })
    .then(ast => {
        document.getElementById("combinedRulesOutput").textContent = JSON.stringify(ast, null, 2);
        alert("Rules combined successfully!");
    })
    .catch(error => {
        alert("Error: " + error.message);
    });
}

function evaluateRule() {
    const data = JSON.parse(document.getElementById("dataInput").value);
    const ruleId = prompt("Enter the rule ID to evaluate:");
    
    fetch(`/api/rules/evaluate_rule?ruleId=${ruleId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to evaluate rule");
        }
        return response.json();
    })
    .then(result => {
        document.getElementById("resultOutput").textContent = "Evaluation Result: " + result;
    })
    .catch(error => {
        alert("Error: " + error.message);
    });
}
