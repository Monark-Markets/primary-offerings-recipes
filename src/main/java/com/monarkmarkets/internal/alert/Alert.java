package com.monarkmarkets.internal.alert;

import lombok.Data;

@Data
public class Alert {
    private AlertLabels labels;
    private AlertAnnotations annotations;
}
