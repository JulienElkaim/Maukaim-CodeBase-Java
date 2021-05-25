package com.maukaim.org.utils.diffable.demo;

import com.maukaim.org.utils.diffable.model.ProcessUpdate;

import java.util.List;

public interface MkProcessDifferencesService {

    List<ProcessUpdate> getDiffs(String idEntity);
}
