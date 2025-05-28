package com.monarkmarkets.internal.alert;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertLabels {
    private String alertname;
    private String severity;
    private String alerttype;
    private String source;
    private String environment;
}
