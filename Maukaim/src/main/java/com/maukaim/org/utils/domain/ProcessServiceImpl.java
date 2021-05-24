package com.maukaim.org.utils.domain;

import com.maukaim.org.utils.audit.model.MkProcessAuditRepository;
import com.maukaim.org.utils.domain.model.MkProcess;
import com.maukaim.org.utils.domain.model.Step;
import com.maukaim.org.utils.domain.model.User;
import com.maukaim.org.utils.domain.model.mock.MkProcessMock;
import com.maukaim.org.utils.domain.model.mock.MockObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private MkProcessAuditRepository auditRepository;

    @PostConstruct
    public void init() {
        log.info("Initializing FakeService...");
        log.info("Save 3 fake process...");
        this.processRepository.saveAll(MkProcessMock.processesMocked.stream()
                .map(MockObject::getValue).collect(Collectors.toList()));

        log.info("Will modify first, modify 1st step name");
        Optional<MkProcess> process1 = this.processRepository.findById(MkProcessMock.FIRST_ID);
        process1.ifPresentOrElse(mp -> {
                    Optional<Step> firstStep = mp.getSteps().stream().findFirst();
                    firstStep.ifPresent(step -> step.setName("New name After modification"));
                    this.processRepository.saveAndFlush(mp);
                },
                () -> log.info("Seems the 1st didn't persist."));

        log.info("Will modify second, add a step, and modify the result");
        Optional<MkProcess> process2 = this.processRepository.findById(MkProcessMock.SECOND_ID);
        process2.ifPresentOrElse(mp -> {
                    mp.getSteps().add(Step.builder().name("Step de la mort added").type("COOL").build());
                    mp.setResult(210D);
                    this.processRepository.saveAndFlush(mp);
                },
                () -> log.info("Seems the 2nd didn't persist."));

        log.info("Will delete third");
        this.processRepository.deleteById(MkProcessMock.THIRD_ID);

        log.info("Will modify second once again, and modify manager");
        process2 = this.processRepository.findById(MkProcessMock.SECOND_ID);
        process2.ifPresent(mp -> {
            mp.setManager(new User("Frederic", "Oudea"));
            this.processRepository.saveAndFlush(mp);
        });


        log.info("Will modify second again, and modify first step type");
        process2 = this.processRepository.findById(MkProcessMock.SECOND_ID);
        process2.ifPresent(mp -> {
            Optional<Step> firstStep = mp.getSteps().stream().findFirst();
            firstStep.ifPresent(step -> step.setType("NewType"));
            this.processRepository.saveAndFlush(mp);
        });

        log.info("Finished Initializing FakeService !");
    }


    @Override
    public List<MkProcess> getAll() {
        return null;
    }
}
