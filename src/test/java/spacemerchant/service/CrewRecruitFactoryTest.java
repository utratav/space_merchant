package spacemerchant.service;

import org.junit.jupiter.api.Test;
import spacemerchant.model.CrewMember;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CrewRecruitFactoryTest {

    @Test
    void shouldHaveOneHundredFirstNamesAndLastNames() {
        assertEquals(100, CrewRecruitFactory.getFirstNameCount());
        assertEquals(100, CrewRecruitFactory.getLastNameCount());
    }

    @Test
    void shouldGenerateRecruitPoolWithUniqueFullNames() {
        CrewRecruitFactory factory = new CrewRecruitFactory(new Random(42));

        List<CrewMember> recruits = factory.generateRecruitPool(20);

        Set<String> uniqueNames = new HashSet<>();
        for (CrewMember recruit : recruits) {
            assertFalse(recruit.getName().isBlank());
            uniqueNames.add(recruit.getName());
        }

        assertEquals(20, recruits.size());
        assertEquals(20, uniqueNames.size());
    }
}
