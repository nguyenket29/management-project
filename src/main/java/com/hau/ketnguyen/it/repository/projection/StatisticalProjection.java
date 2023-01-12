package com.hau.ketnguyen.it.repository.projection;

import java.util.Date;

public interface StatisticalProjection {
    String getNameTopic();
    String getNameStudent();
    String getNameClass();
    Date getTopicYear();
    Float getScoreGuide();
    Float getScoreCounterArgument();
    Float getScoreProcessOne();
    Float getScoreProcessTwo();
    Float getScoreAssembly();
}
