package org.jeasy.rules.fel;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.common.FelBuilder;
import com.greenpineyu.fel.context.ArrayCtxImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.support.AbstractRuleFactory;
import org.jeasy.rules.support.RuleDefinition;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;
import org.jeasy.rules.support.reader.RuleDefinitionReader;
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader;

import java.io.Reader;
import java.util.List;

public class FelRuleFactory extends AbstractRuleFactory {

    private final RuleDefinitionReader reader;
    private final FelContext parserContext;


    /**
     * Create a new {@link FelRuleFactory} with a given reader.
     *
     * @param reader used to read rule definitions
     * @see YamlRuleDefinitionReader
     * @see JsonRuleDefinitionReader
     */
    public FelRuleFactory(RuleDefinitionReader reader) {
        this(reader, new ArrayCtxImpl());
    }

    /**
     * Create a new {@link FelRuleFactory} with a given reader.
     *
     * @param reader        used to read rule definitions
     * @param parserContext used to parse condition/action expressions
     * @see YamlRuleDefinitionReader
     * @see JsonRuleDefinitionReader
     */
    public FelRuleFactory(RuleDefinitionReader reader, FelContext parserContext) {
        this.reader = reader;
        this.parserContext = parserContext;
    }

    /**
     * Create a new {@link FelRule} from a Reader.
     * <p>
     * The rule descriptor should contain a single rule definition.
     * If no rule definitions are found, a {@link IllegalArgumentException} will be thrown.
     * If more than a rule is defined in the descriptor, the first rule will be returned.
     *
     * @param ruleDescriptor descriptor of rule definition
     * @return a new rule
     * @throws Exception if unable to create the rule from the descriptor
     */
    public Rule createRule(Reader ruleDescriptor) throws Exception {
        List<RuleDefinition> ruleDefinitions = reader.read(ruleDescriptor);
        if (ruleDefinitions.isEmpty()) {
            throw new IllegalArgumentException("rule descriptor is empty");
        }
        return createRule(ruleDefinitions.get(0));
    }

    /**
     * Create a set of {@link FelRule} from a rule descriptor.
     *
     * @param rulesDescriptor descriptor of rule definitions
     * @return a set of rules
     * @throws Exception if unable to create rules from the descriptor
     */
    public Rules createRules(Reader rulesDescriptor) throws Exception {
        Rules rules = new Rules();
        List<RuleDefinition> ruleDefinitions = reader.read(rulesDescriptor);
        for (RuleDefinition ruleDefinition : ruleDefinitions) {
            rules.register(createRule(ruleDefinition));
        }
        return rules;
    }

    protected Rule createSimpleRule(RuleDefinition ruleDefinition) {
        FelRule felRule = new FelRule(parserContext)
                .name(ruleDefinition.getName())
                .description(ruleDefinition.getDescription())
                .priority(ruleDefinition.getPriority())
                .when(ruleDefinition.getCondition());
        for (String action : ruleDefinition.getActions()) {
            felRule.then(action);
        }
        return felRule;
    }
}
