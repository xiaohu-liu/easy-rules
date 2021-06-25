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
import com.greenpineyu.fel.exception.EvalException;
import com.greenpineyu.fel.function.CommonFunction;
import com.greenpineyu.fel.function.Function;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.fel.FelAction;
import org.junit.Test;
import sun.security.action.GetPropertyAction;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.AccessController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FelActionTest {
    @Test
    public void testFelActionExecution() throws Exception {
        // given
        Action markAsAdult = new FelAction("person.setAdult(false)");
        Facts facts = new Facts();
        Person foo = new Person("foo", 20);
        facts.put("person", foo);

        // when
        markAsAdult.execute(facts);

        // then
        assertThat(foo.isAdult()).isFalse();
    }

    @Test
    public void testFelFunctionExecution() throws Exception {
        // given
        PrintStream originStream = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        FelAction.getFelEngine().getContext().set("out", printStream);
        Action printAction = new FelAction("out.println('Hello from Fel!')");
        Facts facts = new Facts();
        // when
        /*String output = tapSystemOut(
                () -> printAction.execute(facts));*/
        printAction.execute(facts);
        System.setOut(originStream);
        // then
        String s = AccessController.doPrivileged(
                new GetPropertyAction("line.separator"));
        assertThat(outputStream.toString()).isEqualTo("Hello from Fel!" + s);
    }

    @Test
    public void testFelActionExecutionWithFailure() {
        // given
        Action action = new FelAction("person.setBlah(true)");
        Facts facts = new Facts();
        Person foo = new Person("foo", 20);
        facts.put("person", foo);
        // when
        assertThatThrownBy(() -> action.execute(facts))
                // then
                .isInstanceOf(EvalException.class)
                .hasMessageContaining("no such method setBlah");
    }

    @Test
    public void testFelActionWithExpressionAndParserContext() throws Exception {
        // given

        Function random = new CommonFunction() {
            @Override
            public String getName() {
                return "random";
            }

            @Override
            public Object call(Object[] arguments) {
                System.out.println("Random from Fel = " + new java.util.Random(123).nextInt(10));
                return null;
            }
        };
        FelAction.getFelEngine().addFun(random);
        PrintStream originStream = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        FelContext context = new MapContext();
        Action printAction = new FelAction("random()", context);
        Facts facts = new Facts();
        // when
        printAction.execute(facts);
        System.setOut(originStream);
        String s = AccessController.doPrivileged(
                new GetPropertyAction("line.separator"));
        // then
        assertThat(outputStream.toString()).isEqualTo("Random from Fel = 2" + s);
    }


}
