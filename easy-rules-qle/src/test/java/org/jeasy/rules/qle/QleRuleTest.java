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

import org.jeasy.rules.api.Facts;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lauri Kimmel
 * @author Mahmoud Ben Hassine
 */
public class QleRuleTest {

    private final Facts facts = new Facts();
    private final QleRule QleRule = new QleRule();

    @Before
    public void setUp() {
        QleRule.when("person.age > 18");
        QleRule.then("person.adult  = (true);");
    }

    @Test
    public void whenTheRuleIsTriggered_thenConditionShouldBeEvaluated() {
        // given
        facts.put("person", new Person("foo", 20));

        // when
        boolean evaluationResult = QleRule.evaluate(facts);

        // then
        assertThat(evaluationResult).isTrue();
    }

    @Test
    public void whenTheConditionIsTrue_thenActionsShouldBeExecuted() throws Exception {
        // given
        Person foo = new Person("foo", 20);
        facts.put("person", foo);

        // when
        QleRule.execute(facts);

        // then
        assertThat(foo.isAdult()).isTrue();
    }
}
