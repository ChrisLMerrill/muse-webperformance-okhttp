package com.webperformance.muse.okhttp;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class GetStepTests
    {
    @Test
    public void testBadDomain() throws MuseExecutionError
        {
        final SimpleProject project = new SimpleProject();
        StepConfiguration config = new StepConfiguration(GetStep.class.getAnnotation(MuseTypeId.class).value());
        config.addSource(GetStep.URL_PARAM, ValueSourceConfiguration.forValue("http://doesnt.exist/"));

        GetStep step = new GetStep(config, project);
        final DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(project, null);
        final DefaultSteppedTestExecutionContext step_test_context = new DefaultSteppedTestExecutionContext(test_context);
        final SingleStepExecutionContext step_context = new SingleStepExecutionContext(step_test_context, config, false);
        final StepExecutionResult result = step.executeImplementation(step_context);
//        Assert.assertEquals(StepExecutionStatus.FAILURE, result.getStatus());
//        Assert.assertNotNull(step_test_context.getVariable("result"));
//        Assert.assertNotNull(step_context.getVariable("result"));
//        Assert.assertNotNull(test_context.getVariable("result"));
        }
    }


