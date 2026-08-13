package com.workflow.bpmn.runtime.impl;

import jakarta.el.ExpressionFactory;
import jakarta.el.StandardELContext;
import jakarta.el.ValueExpression;

import java.util.Map;

public class JakartaElEvaluator {
    private final ExpressionFactory expressionFactory;

    public JakartaElEvaluator() {
        this.expressionFactory = ExpressionFactory.newInstance();
    }

    public Object evaluate(String expression, Map<String, Object> bindings) {
        // Create the context
        StandardELContext context = new StandardELContext(expressionFactory);
        for (Map.Entry<String, Object> entry : bindings.entrySet()) {
            String variableName = entry.getKey();
            Object variableValue = entry.getValue();
            // Make the variable available to the expression
            context.getVariableMapper().setVariable(
                    variableName,
                    expressionFactory.createValueExpression(
                            variableValue,
                            Object.class
                    )
            );
        }

        // Evaluate expression
        ValueExpression valueExpression = expressionFactory.createValueExpression(
                context,
                expression,
                Object.class
        );

        return valueExpression.getValue(context);
    }

    public static void main(String[] args) {
        JakartaElEvaluator evaluator = new JakartaElEvaluator();

        System.out.println(evaluator.evaluate("${text}", Map.of("text", "Hello World")));
        System.out.println(evaluator.evaluate("${text.toUpperCase()}", Map.of("text", "Hello World")));
        System.out.println(evaluator.evaluate("${1 + 2}", Map.of("text", "Hello World")));
        System.out.println(evaluator.evaluate("${text.length() > 5}", Map.of("text", "Hello World")));
    }
}