package com.github.geequery.springboot.test.repo;

import com.github.geequery.springboot.test.domain.Sample;
import com.github.geequery.springdata.repository.GqRepository;

public interface SampleRepository extends GqRepository<Sample, Long> {

}
