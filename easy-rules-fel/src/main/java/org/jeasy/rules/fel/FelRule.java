package org.jeasy.rules.fel;

import com.greenpineyu.fel.context.ArrayCtxImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.BasicRule;

import java.util.ArrayList;
import java.util.List;

public class FelRule extends BasicRule {


    private Condition condition = Condition.FALSE;
    private final List<Action> actions = new ArrayList<>();
    private final FelContext parserContext;

    /**
     * Create a new Fel rule.
     */
    public FelRule() {
        this(new MapContext());
    }

    /**
     * Create a new Fel rule.
     *
     * @param parserContext used to parse condition/action expressions
     */
    public FelRule(FelContext parserContext) {
        super(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
        this.parserContext = parserContext;
    }

    /**
     * Set rule name.
     *
     * @param name of the rule
     * @return this rule
     */
    public FelRule name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set rule description.
     *
     * @param description of the rule
     * @return this rule
     */
    public FelRule description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Set rule priority.
     *
     * @param priority of the rule
     * @return this rule
     */
    public FelRule priority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Specify the rule's condition as Fel expression.
     *
     * @param condition of the rule
     * @return this rule
     */
    public FelRule when(String condition) {
        this.condition = new FelCondition(condition, parserContext);
        return this;
    }

    /**
     * Add an action specified as an Fel expression to the rule.
     *
     * @param action to add to the rule
     * @return this rule
     */
    public FelRule then(String action) {
        this.actions.add(new FelAction(action, parserContext));
        return this;
    }

    @Override
    public boolean evaluate(Facts facts) {
        return condition.evaluate(facts);
    }

    @Override
    public void execute(Facts facts) throws Exception {
        for (Action action : actions) {
            action.execute(facts);
        }
    }

}
