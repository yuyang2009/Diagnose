package com.diagnose.diagnose.model;

import java.util.Date;

public interface DiagRes {
    int getId();
    String getName();
    String getDescription();
    String getPhotoPath();
    String getTmpFilePath();
    String getResultsPath();
    Date getCreateAt();
}
