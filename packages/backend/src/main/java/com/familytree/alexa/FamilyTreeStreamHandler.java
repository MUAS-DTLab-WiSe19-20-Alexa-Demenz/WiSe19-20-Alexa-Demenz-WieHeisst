/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.familytree.alexa;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.familytree.alexa.handlers.*;
import com.familytree.model.DynamoDataSource;
import com.familytree.model.IDataSource;

public class FamilyTreeStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        IDataSource dataSource = new DynamoDataSource();
        return Skills.standard()
                .addRequestHandlers(new WhoIsIntentHandler(dataSource), new PhoneNumberIntentHandler(dataSource),
                        new WorkIntentHandler(dataSource), new AgeIntentHandler(dataSource),
                        new BirthdayIntentHandler(dataSource), new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(), new SessionEndedRequestHandler(), new HelpIntentHandler(),
                        new FallbackIntentHandler(), new HobbyIntentHandler(dataSource))
                // Add your skill id below
                // .withSkillId("")
                .build();
    }

    public FamilyTreeStreamHandler() {
        super(getSkill());
    }

}
