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
package org.jeasy.rules.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.runtime.JavaMethodReflectionFunctionMissing;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lauri Kimmel
 * @author Mahmoud Ben Hassine
 */
public class AviatorConditionTest {

    @Test
    public void testAviatorExpressionEvaluation() {
        // given
        Condition isAdult = new AviatorCondition("person.age > 18");
        Facts facts = new Facts();
        facts.put("person", new Person("foo", 20));

        // when
        boolean evaluationResult = isAdult.evaluate(facts);

        // then
        assertThat(evaluationResult).isTrue();
    }

    // This behaviour is similar to MVEL though, where a missing fact results in an exception

    @Test
    public void whenDeclaredFactIsNotPresent_thenShouldThrowRuntimeException() {
        // given
        Condition isHot = new AviatorCondition("temperature > 30");
        Facts facts = new Facts();

        // when
        boolean evaluationResult = isHot.evaluate(facts);

        assertThat(evaluationResult).isFalse();

        // then
        // expected exception
    }

    @Test
    public void testAviatorConditionWithNamespace() {
        // given
      /*  Map<String, Object> namespaces = new HashMap<>();
        namespaces.put("rnd", new Random(123));*/
        AviatorEvaluator.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        Condition condition = new AviatorCondition("use java.util.Random; let rnd  = new Random(123); return nextBoolean(rnd);", instance);
        Facts facts = new Facts();

        // when
        boolean evaluationResult = condition.evaluate(facts);

        // then
        assertThat(evaluationResult).isTrue();
    }
}
