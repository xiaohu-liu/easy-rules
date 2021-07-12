/*
 * The MIT License
 *
 *  Copyright (c) 2021, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.jeasy.rules.qle;

import com.ql.util.express.ExpressRunner;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.BasicRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Lauri Kimmel
 * @author Mahmoud Ben Hassine
 */
public class QleRule extends BasicRule {

    static final ExpressRunner DEFAULT_INSTANCE = new ExpressRunner();

    private Condition condition = Condition.FALSE;
    private final List<Action> actions = new ArrayList<>();
    private final ExpressRunner aviatorEvaluatorInstance;

    public QleRule() {
        this(DEFAULT_INSTANCE);
    }

    public QleRule(ExpressRunner instance) {
        super(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
        this.aviatorEvaluatorInstance = Objects.requireNonNull(instance, "instance cannot be null");
    }

    public QleRule name(String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        return this;
    }

    public QleRule description(String description) {
        this.description = Objects.requireNonNull(description, "description cannot be null");
        return this;
    }

    public QleRule priority(int priority) {
        this.priority = priority;
        return this;
    }

    public QleRule when(String condition) {
        Objects.requireNonNull(condition, "condition cannot be null");
        this.condition = new QleCondition(condition, aviatorEvaluatorInstance);
        return this;
    }

    public QleRule then(String action) {
        Objects.requireNonNull(action, "action cannot be null");
        this.actions.add(new QleAction(action, aviatorEvaluatorInstance));
        return this;
    }

    @Override
    public boolean evaluate(Facts facts) {
        Objects.requireNonNull(facts, "facts cannot be null");
        return condition.evaluate(facts);
    }

    @Override
    public void execute(Facts facts) throws Exception {
        Objects.requireNonNull(facts, "facts cannot be null");
        for (Action action : actions) {
            action.execute(facts);
        }
    }
}
