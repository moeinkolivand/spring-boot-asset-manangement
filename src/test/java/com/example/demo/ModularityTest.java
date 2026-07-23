package com.example.demo;

import org.springframework.modulith.core.ApplicationModules;
import org.junit.jupiter.api.Test;

class ModularityTests {

    ApplicationModules modules = ApplicationModules.of(DemoApplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }
}
