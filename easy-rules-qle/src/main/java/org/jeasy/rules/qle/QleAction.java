package org.jeasy.rules.qle;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class QleAction implements Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(QleAction.class);
    private String expression;
    private ExpressRunner expressRunner;

    public QleAction(String expression) {
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
        expressRunner = new ExpressRunner();
    }

    public QleAction(String expression, ExpressRunner expressRunner) {
        this(expression);
        this.expressRunner = expressRunner;
    }

    @Override
    public void execute(Facts facts) throws Exception {
        Objects.requireNonNull(facts, "facts cannot be null");
        try {
            DefaultContext<String, Object> context = new DefaultContext<String, Object>();
            Map<String, Object> mapContext = facts.asMap();
            if (mapContext != null) {
                context.putAll(mapContext);
            }
            this.expressRunner.execute(this.expression, context, null, true, false);
        } catch (Exception e) {
            LOGGER.error("Unable to execute expression: '" + expression + "' on facts: " + facts, e);
            throw e;
        }
    }
}
