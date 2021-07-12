package org.jeasy.rules.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AviatorAction implements Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(AviatorAction.class);


    private String expression;

    private Expression compiledExpression;


    public AviatorAction(String expression) {
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
        this.compiledExpression = AviatorEvaluator.compile(expression);
    }

    public AviatorAction(String expression, AviatorEvaluatorInstance aviatorEvaluatorInstance){
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
        this.compiledExpression = aviatorEvaluatorInstance.compile(expression);
    }

    @Override
    public void execute(Facts facts) throws Exception {
        Objects.requireNonNull(facts, "facts cannot be null");
        try {
            compiledExpression.execute(facts.asMap());
        } catch (Exception e) {
            LOGGER.error("Unable to execute expression: '" + expression + "' on facts: " + facts, e);
            throw e;
        }
    }
}
