package org.jeasy.rules.qle;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.exception.QLException;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class QleCondition implements Condition {
    private static final Logger LOGGER = LoggerFactory.getLogger(QleAction.class);
    private String expression;

    private ExpressRunner expressRunner;


    public QleCondition(String expression) {
        this.expression = expression;
        this.expressRunner = new ExpressRunner();
    }

    public QleCondition(String expression, ExpressRunner expressRunner) {
        this.expression = expression;
        this.expressRunner = expressRunner;
    }

    @Override
    public boolean evaluate(Facts facts) {
        Objects.requireNonNull(facts, "facts cannot be null");
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        Map<String, Object> mapContext = facts.asMap();
        if (mapContext != null) {
            context.putAll(mapContext);
        }
        try {
            Object execute = this.expressRunner.execute(this.expression, context, null, true, false);
            return (Boolean) execute;
        } catch (Exception e) {
            LOGGER.error("Unable to execute expression: '" + expression + "' on facts: " + facts, e);
            throw new RuntimeException(e);
        }
    }
}
