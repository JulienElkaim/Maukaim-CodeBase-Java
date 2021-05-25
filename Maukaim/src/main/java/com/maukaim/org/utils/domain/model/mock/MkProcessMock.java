package com.maukaim.org.utils.domain.model.mock;

import com.maukaim.org.utils.domain.model.MkProcess;
import com.maukaim.org.utils.domain.model.Step;
import com.maukaim.org.utils.domain.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MkProcessMock {
    public static final UUID FIRST_ID = UUID.randomUUID();
    public static final UUID SECOND_ID = UUID.randomUUID();
    public static final UUID THIRD_ID = UUID.randomUUID();

    public static List<MockObject<MkProcess>> processesMocked = List.of(
            MockObject.of(MkProcess.builder()
                            .id(MkProcessMock.FIRST_ID)
                            .manager(new User("Emmanuel", "Macron"))
                            .result(100D)
                            .steps(List.of(Step.builder().name("FirstStep").type("PARSER").build()))
                            .creationTime(LocalDateTime.now())
                            .build(),
                    "{}"
            ),
            MockObject.of(MkProcess.builder()
                            .id(MkProcessMock.SECOND_ID)
                            .manager(new User("Emmanuel", "Macron"))
                            .result(200D)
                            .creationTime(LocalDateTime.now())
                            .steps(new ArrayList<>())
                            .build(),
                    "{}"
            ),

            MockObject.of(MkProcess.builder()
                            .id(MkProcessMock.THIRD_ID)
                            .manager(new User("Vladmir", "Poutine"))
                            .result(300D)
                            .creationTime(LocalDateTime.now())
                            .steps(new ArrayList<>())
                            .build(),
                    "{}"
            )
    );
}
