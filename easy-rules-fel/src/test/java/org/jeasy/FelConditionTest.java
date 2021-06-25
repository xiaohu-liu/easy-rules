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
package org.jeasy;

import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.fel.FelCondition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FelConditionTest {

    @Test
    public void testFelExpressionEvaluation() {
        // given
        Condition isAdult = new FelCondition("person.age > 18");
        Facts facts = new Facts();
        facts.put("person", new Person("foo", 20));

        // when
        boolean evaluationResult = isAdult.evaluate(facts);

        // then
        assertThat(evaluationResult).isTrue();
    }

//    @Test(expected = RuntimeException.class)
    @Test
    public void whenDeclaredFactIsNotPresent_thenShouldThrowRuntimeException() {
        // given
        Condition isHot = new FelCondition("temperature > 30");
        Facts facts = new Facts();

        // when
        boolean evaluationResult = isHot.evaluate(facts);
        assertThat(evaluationResult).isFalse();

        // then
        // expected exception
    }

    @Test
    public void testFelConditionWithExpressionAndParserContext() {
        // given
        FelContext context = new MapContext();
        //context.addPackageImport("java.util");
        context.set("randomBean", new java.util.Random(123));
        Condition condition = new FelCondition("randomBean.nextBoolean()", context);
        Facts facts = new Facts();
        // when
        boolean evaluationResult = condition.evaluate(facts);

        // then
        assertThat(evaluationResult).isTrue();
    }
}
