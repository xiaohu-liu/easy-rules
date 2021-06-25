package org.jeasy.rules.fel;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.context.ContextChain;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import com.greenpineyu.fel.parser.FelNode;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;

public class FelCondition implements Condition {

    public static final FelEngine engine = FelEngine.instance;

    private FelNode compiledExpression;

    private FelContext felContext = engine.getContext();

    /**
     * Create a new {@link FelCondition}.
     *
     * @param expression the condition written in expression language
     */
    public FelCondition(String expression) {
        compiledExpression = engine.parse(expression);
    }

    /**
     * Create a new {@link FelCondition}.
     *
     * @param expression    the condition written in expression language
     * @param parserContext the Fel parser context
     */
    public FelCondition(String expression, FelContext parserContext) {
        compiledExpression = engine.parse(expression);
        this.felContext = parserContext;
    }

    @Override
    public boolean evaluate(Facts facts) {
        FelContext felContext = new ContextChain(this.felContext, new MapContext(facts.asMap()));
        return (boolean) compiledExpression.eval(felContext);
    }
}
