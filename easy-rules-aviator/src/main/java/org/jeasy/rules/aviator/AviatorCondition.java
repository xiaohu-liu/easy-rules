package org.jeasy.rules.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;

import java.util.Objects;

public class AviatorCondition implements Condition {

    private Expression compiledExpression;


    public AviatorCondition(String expression) {
        this.compiledExpression = AviatorEvaluator.compile(expression);
    }

    public AviatorCondition(String expression, AviatorEvaluatorInstance aviatorEvaluatorInstance) {
        this.compiledExpression = aviatorEvaluatorInstance.compile(expression);
    }

    @Override
    public boolean evaluate(Facts facts) {
        Objects.requireNonNull(facts, "facts cannot be null");
        Object execute = this.compiledExpression.execute(facts.asMap());
        return (Boolean) execute;
    }
}
