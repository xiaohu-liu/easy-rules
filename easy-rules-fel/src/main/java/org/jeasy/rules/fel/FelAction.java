package org.jeasy.rules.fel;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.Fel;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.common.FelBuilder;
import com.greenpineyu.fel.context.ContextChain;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import com.greenpineyu.fel.parser.FelNode;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class FelAction implements Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(FelAction.class);

    private final String expression;
    private final FelNode compiledExpression;
    private static final FelEngine engine = FelEngine.instance;
    private FelContext felContext = engine.getContext();


    public static FelEngine getFelEngine(){
        return engine;
    }

    /**
     * Create a new {@link FelAction}.
     *
     * @param expression the action written in expression language
     */
    public FelAction(String expression) {
        this.expression = expression;
        compiledExpression = engine.parse(expression);
    }

    /**
     * Create a new {@link FelAction}.
     *
     * @param expression    the action written in expression language
     * @param parserContext the Fel parser context
     */
    public FelAction(String expression, FelContext parserContext) {
        this.expression = expression;
        this.felContext = parserContext;
        compiledExpression = engine.parse(expression);
    }

    @Override
    public void execute(Facts facts) {
        try {
            FelContext felContext = new ContextChain(this.felContext, new MapContext(facts.asMap()));
            compiledExpression.eval(felContext);
        } catch (Exception e) {
            LOGGER.error("Unable to evaluate expression: '" + expression + "' on facts: " + facts, e);
            throw e;
        }
    }
}
