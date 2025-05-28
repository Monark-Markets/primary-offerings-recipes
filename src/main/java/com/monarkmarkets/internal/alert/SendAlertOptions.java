package com.monarkmarkets.internal.alert;

import lombok.Data;

@Data
public class SendAlertOptions {
    private String title;
    private String message;
    private String details;
    private String action;
    private String linkUrl;
    private String linkText;
    private String severity;
    private String sourceComponent;
    private String environment;
}
